package com.se2.alert.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Configuration
public class MailSenderConfig {

	@Value("${mail.port.number.sending}")
	private int mailPortNumberSending;
	@Value("${mail.server.ip}")
	private String mailServerIp;
	@Value("${mail.transport.protocol}")
	private String mailTransportProtocol;
	@Value("${mail.username}")
	private String mailUsername;
	@Value("${mail.password}")
	private String mailPassword;
	@Value("${mail.debug}")
	private String mailDebug;
	@Value("${mail.smtp.auth}")
	private String mailSmtpAuth;
	@Value("${mail.smtp.starttls.enable}")
	private String mailSmtpStarttlsEnable;
	@Value("${mail.smtp.timeout}")
	private int mailSmtpTimeout;

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(mailServerIp);
		mailSender.setPort(mailPortNumberSending);
		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPassword);
		mailSender.setProtocol(mailTransportProtocol);

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
		javaMailProperties.put("mail.smtp.auth", mailSmtpAuth);
		javaMailProperties.put("mail.debug", mailDebug);
		javaMailProperties.put("mail.smtp.timeout", mailSmtpTimeout);
		
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
}