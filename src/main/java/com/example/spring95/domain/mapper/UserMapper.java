package com.example.spring95.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring95.domain.dto.UserDto;

@Mapper
public interface UserMapper {
	public abstract int insert(UserDto userDto);
	public abstract int update(UserDto userDto);
	public abstract int delete(Long userNum);
	public abstract UserDto findByUserNum(Long userNum);
	public abstract UserDto findByUserId(String userId);
	public abstract UserDto findByUserEmail(String userEmail);
}
