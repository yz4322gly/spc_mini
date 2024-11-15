package com.ruoyi.common.utils;

/**
 * @author guolinyuan
 */
public class URLConstant
{
    /**
     * 登录凭证校验。通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程<br/>
     * <a href=https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html>参见文档</a>
     */
    public static final String WX_CODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 微信小程序客服发送消息
     */
    public static final String KF_WX_SEND_MSG = "https://api.weixin.qq.com/cgi-bin/message/custom/send";

    /**
     * 微信统一下单URL
     */
    public static final String WX_UNIFIED_SERVER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 微信申请退款
     */
    public static final String WX_REFUND_SERVER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 微信企业付款
     */
    public static final String WX_TRANSFERS_SERVER_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

//    /**
//     * 微信客服url
//     */
//    public static final String WX_TRANSFERS_SERVER_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 微信获取小程序码
     */
    public static final String WX_WXACODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

    /**
     * 阿里服务url
     */
    public static final String ALI_SERVER_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 微信公众号发送模板消息
     */
    public static final String WX_GZH_MSG_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send";

}
