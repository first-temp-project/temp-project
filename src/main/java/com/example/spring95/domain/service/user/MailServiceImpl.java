package com.example.spring95.domain.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.spring95.domain.dto.UserDto;

import lombok.RequiredArgsConstructor;

@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

	@Value("${spring.mail.username}")
	private String from;
	private static final String TITLE = "안녕하세요 중앙경제평론사 입니다.";
	private final JavaMailSender mailSender;

	@Override
	public boolean sendMail(UserDto userDto) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from); // 보내는 사람 이메일
		message.setTo(userDto.getUserEmail()); // 받는 사람 이메일
		message.setSubject(TITLE); // 이메일 제목
		message.setText(getMailContent(userDto)); // 이메일 본문
		try {
			mailSender.send(message);
			return true;
		} catch (MailException e) {
			e.printStackTrace();
			System.out.println("mailService sendEmail error");
			return false;
		}
	}

	@Override
	public String getMailContent(UserDto userDto) {
		return "귀하의 아이디는" + userDto.getUserId() + "이고 임시 비밀번호는 " + userDto.getUserPassword() + "입니다";
	}

}
