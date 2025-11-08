package com.example.spring95.domain.service.user;

import com.example.spring95.domain.dto.UserDto;

public interface MailService {
	public abstract boolean sendMail(UserDto userDto);
	public abstract String getMailContent(UserDto userDto);
}
