package com.exam.util;

import com.exam.bean.Authentication;
import com.exam.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * Created by deng on 2017/10/10.
 */
@Service
public class MailUtil {
    private static Properties mailProperties;

    private final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private final static String USERNAME = "egg592737616@163.com";
    private final static String PASSWORD = "Deng911";

    static {
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", "smtp.163.com");
        mailProperties.put("mail.smtp.port", "25");
        mailProperties.put("mail.smtp.auth", "true");
    }

    public void sendEmail(String toAddress, String title, String content, boolean isHTMLEmail) throws Exception {
        ThreadPool.execute(new MailThread(toAddress, title, content, isHTMLEmail));
    }

    class MailThread implements Runnable {
        private String toAddress;
        private String title;
        private String content;
        private boolean isHTMLEmail;

        public MailThread(String toAddress, String title, String content, boolean isHTMLEmail) {
            this.toAddress = toAddress;
            this.title = title;
            this.content = content;
            this.isHTMLEmail = isHTMLEmail;
        }

        @Override
        public void run() {
            logger.info("开始发送邮件[收件人" + toAddress + "]");

            Authentication authenticator = new Authentication(USERNAME, PASSWORD);
            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session sendMailSession = Session.getDefaultInstance(mailProperties, authenticator);
            try {
                Message mailMessage = new MimeMessage(sendMailSession);
                Address from = new InternetAddress(USERNAME);
                mailMessage.setFrom(from);
                Address to = new InternetAddress(toAddress);
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                mailMessage.setSubject(title);
                mailMessage.setSentDate(new Date());

                if (isHTMLEmail) {
                    Multipart mainPart = new MimeMultipart();
                    BodyPart html = new MimeBodyPart();
                    html.setContent(content, "text/html; charset=utf-8");
                    mainPart.addBodyPart(html);
                    mailMessage.setContent(mainPart);
                } else {
                    mailMessage.setText(content);
                }

                Transport.send(mailMessage);

                logger.info("邮件发送成功[收件人" + toAddress + "]");
            } catch (MessagingException ex) {
                logger.error("邮件发送失败：" + ex.getMessage());
            }
        }
    }
}
