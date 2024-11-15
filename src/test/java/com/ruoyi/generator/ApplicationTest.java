package com.ruoyi.generator;


import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import com.ruoyi.project.util.MailUtil;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_MINUTE;


@SpringBootTest(classes = RuoYiApplication.class)
public class ApplicationTest {

    @Autowired
    ISpcDataSummaryService spcDataSummaryService;


    @Autowired
    private MailUtil mailUtil;

    /**
     * 执行重算测试
     * @throws ParseException
     */
    @Test
    public void test() throws ParseException {
//        spcDataSummaryService.summarizeFromDetails("WIDTH_P100_V2_TOP",null,null);
        spcDataSummaryService.summarizeFromDetails("WIDTH_P100_V2_MID",null,null);
//        Date startTime = DateUtils.parseDate("2024-02-28 15:19:00",DateUtils.YYYY_MM_DD_HH_MM_SS);
//        Date endTime = DateUtils.parseDate("2024-02-29 16:19:00",DateUtils.YYYY_MM_DD_HH_MM_SS);
//        spcDataSummaryService.checkOOC("WIDTH_P100_V2_TOP",startTime,endTime);
    }

//    @Test
    public void test2() throws InterruptedException {
        mailUtil.sendMailByTopic("测试邮件","1测试邮件,这不是一个垃圾邮件,请不要屏蔽他.内容./....","TEST",ONE_MINUTE);
        mailUtil.sendMailByTopic("测试邮件","2测试邮件,这不是一个垃圾邮件,请不要屏蔽他.内容./....","TEST",ONE_MINUTE);
        mailUtil.sendMailByTopic("测试邮件","3测试邮件,这不是一个垃圾邮件,请不要屏蔽他.内容./....","TEST",ONE_MINUTE);
        TimeUnit.MINUTES.sleep(2);
        mailUtil.sendMailByTopic("测试邮件","4测试邮件,这不是一个垃圾邮件,请不要屏蔽他.内容./....","TEST",ONE_MINUTE);
        // 队列名称
//        String queueName = "simple.queue";
//         消息
//        String message = "hello, spring amqp!";
//         发送消息
//        rabbitTemplate.convertAndSend(queueName, message);



    }
}
