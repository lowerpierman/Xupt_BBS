package com.xupt.nowcoder.util;

/**
 * @Author yzw
 * @Date 2022-07-05 11:55 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from_mail;

    public void sendMail(String to_mail,String title,String text)  {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from_mail);
            mimeMessageHelper.setTo(to_mail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(text,true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        }catch (MessagingException messagingException){
            logger.error("发送邮件失败"+messagingException.getMessage());
            messagingException.printStackTrace();
        }
    }
}
