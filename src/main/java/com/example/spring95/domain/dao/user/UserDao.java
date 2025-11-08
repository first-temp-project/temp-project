package com.example.spring95.domain.dao.user;

import org.apache.ibatis.annotations.Param;

import com.example.spring95.domain.dto.UserDto;

public interface UserDao {
	public abstract int insert(UserDto userDto);
	public abstract int update(UserDto userDto);
	public abstract int delete(Long userNum);
	public abstract UserDto findByUserNum(Long userNum);
	public abstract UserDto findByUserId(String userId);
	public abstract UserDto findByUserEmail(String userEmail);
}
