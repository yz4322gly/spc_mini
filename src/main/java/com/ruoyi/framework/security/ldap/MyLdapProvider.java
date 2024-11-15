package com.ruoyi.framework.security.ldap;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.SysPermissionService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * @author guolinyuan
 */
@Service
public class MyLdapProvider implements AuthenticationProvider
{

    private static final Logger logger = LoggerFactory.getLogger(MyLdapProvider.class);

    @Autowired
    ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        //先去域控问账号密码对不对
        authLdap(username, password);

        //不抛异常则正确
        //获取用户,没有就注册一个
        final UserDetails user = loadOrRegisterUserByUsername(authentication.getName());

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user,
                authentication.getCredentials(), null);
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    //@Value("${ldap.domain}")
    private String domain = "@etronprobe.com";
    //@Value("${ldap.url}")
    private String url = "ldap://192.168.30.100:389";

    public void authLdap(String username, String password)
    {
        String user = username + domain;
        Hashtable<String, String> env = new Hashtable<>();//实例化一个Env
        LdapContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        try
        {
            ctx = new InitialLdapContext(env, null);// 初始化上下文
        }
        catch (javax.naming.AuthenticationException e)
        {
            e.printStackTrace();
            logger.info("用户不存在/密码错误:{}", username);
            throw new ServiceException("用户不存在/密码错误");
        }
        catch (javax.naming.CommunicationException e)
        {
            logger.info("AD域连接失败!");
            throw new ServiceException("AD域连接失败!");
        }
        catch (Exception e)
        {
            logger.info("身份验证未知异常!");
            e.printStackTrace();
            throw new ServiceException("身份验证未知异常!");
        }
        finally
        {
            if (null != ctx)
            {
                try
                {
                    ctx.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 有就取出来,没有就注册一个
     *
     * @param username
     * @return
     */
    public UserDetails loadOrRegisterUserByUsername(String username)
    {
        SysUser user = userService.selectUserByUserName(username);

        if (user == null)
        {
            user = new SysUser();
            user.setUserName(username);
            user.setNickName(username);
            boolean regFlag = userService.registerUser(user);
            if (!regFlag)
            {
                throw new ServiceException("注册失败,请联系系统管理人员");
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            logger.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            logger.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
