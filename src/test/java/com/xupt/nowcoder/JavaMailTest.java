package com.xupt.nowcoder;

import com.xupt.nowcoder.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-05 11:54 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class JavaMailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine  templateEngine;
    @Test
    public void mailTest(){
        mailClient.sendMail("2210385544@qq.com","这是一首简单的小情歌~","唱着我心头的快乐~");
    }
    @Test
    public void htmlMailTest(){
        /*Context context = new Context();
        context.setVariable("username","zhangsan");
        context.setVariable("time", new SimpleDateFormat().format(new Date()));*/
        Context context = new Context();
        context.setVariable("email","user.getEmail()");
        String content1 = "127.0.0.1/nowcoder/activation/ss/sdsd";
        context.setVariable("url",content1);
        String content=templateEngine.process("/mail/activation.html",context);
        System.out.println(content);
        mailClient.sendMail("952012079@stu.xupt.edu.cn","点击链接激活账户信息",content);
    }
}
