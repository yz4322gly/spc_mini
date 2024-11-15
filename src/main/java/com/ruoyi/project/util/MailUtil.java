package com.ruoyi.project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.management.timer.Timer.ONE_HOUR;

/**
 * @author guolinyuan
 */
@Service
public class MailUtil
{

    @Value("${spring.mail.username}")
    private String from; //这是发送人
    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 固定线程池,持有一个线程
     * 如果线程池中的线程数量不足，任务会被放入阻塞队列中等待执行。
     * 即所有邮件排队发送,而且就一个队
     */
    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    //存储主题->发送时间的对象
    private static final Map<String,Long> TOPIC_SEND_TIME = new HashMap<>();

    /**
     * 避免统一主题的消息被疯狂发送,每一个主题的消息在一定时间内只允许发送一次
     * @param subject 邮件标题
     * @param text 内容
     * @param topic 主题名,最好定义成静态常量
     * @param sendingInterval 发送间隔,以毫秒计算,注意,多次调用此方法时sendingInterval应一致,
     * 不一致时,下一次能否正常发送,以当前传入的间隔时间来计算
     * 间隔时间,可以直接用常量写,避免出错,参见:{@link javax.management.timer.Timer#ONE_HOUR}
     */
    public void sendMailByTopic(String subject, String text,String topic,long sendingInterval)
    {
        Long time = TOPIC_SEND_TIME.get(topic);
        long now = System.currentTimeMillis();
        if (time == null || (time +  sendingInterval) < now)
        {
            sendMail(subject,text);
            TOPIC_SEND_TIME.put(topic,now);
        }
    }

    public void sendMail(String subject, String text)
    {
        executorService.submit(() ->
        {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo("yz4322gly@163.com");
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
        });
    }

    public void sendMailTo(String subject, String text,String to)
    {
        executorService.submit(() ->
        {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
        });
    }

    /**
     * 将 StackTrace 美化输出
     * @param e
     * @return
     */
    public static String getST(Exception e)
    {
        StringBuilder sb = new StringBuilder(e.getMessage() + "\n");
        for (StackTraceElement element : e.getStackTrace())
        {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
