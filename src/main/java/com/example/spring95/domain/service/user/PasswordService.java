package com.example.spring95.domain.service.user;

public interface PasswordService {
	public abstract String encode(String rawPassword);
	public abstract boolean matches(String rawPasswrd, String encodedPassword);
	public abstract String getTempPassword();
}
