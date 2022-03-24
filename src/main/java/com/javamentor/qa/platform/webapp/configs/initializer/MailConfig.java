package com.javamentor.qa.platform.webapp.configs.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Configuration
public class MailConfig {
    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String enable;

    @Value("${mail.smtp.ssl.trust}")
    private String trust;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", enable);
        props.put("mail.debug", debug);
        props.put("mail.smtp.ssl.trust", trust);

        return mailSender;
    }
}