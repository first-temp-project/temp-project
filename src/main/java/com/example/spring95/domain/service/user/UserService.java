package com.example.spring95.domain.service.user;

import com.example.spring95.domain.dto.UserDto;

public interface UserService {
	public abstract boolean insert(UserDto userDto);
	public abstract boolean update(UserDto userDto);
	public abstract boolean delete(Long userNum);
	public abstract UserDto findByUserNum(Long userNum);
	public abstract UserDto findByUserId(String userId);
	public abstract UserDto findByUserEmail(String userEmail);
	public abstract UserDto login(String userId, String userPassword);
	public abstract UserDto findByUserNumAndUserPassword(Long userNum, String userPassword);
	public abstract void sendEmailAndSetPassword(UserDto userDto);
}
