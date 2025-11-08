package com.example.spring95.domain.dao.user;

import org.springframework.stereotype.Repository;

import com.example.spring95.domain.dto.UserDto;
import com.example.spring95.domain.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
	private final UserMapper userMapper;

	@Override
	public int insert(UserDto userDto) {
		return userMapper.insert(userDto);
	}

	@Override
	public int update(UserDto userDto) {
		return userMapper.update(userDto);
	}

	@Override
	public int delete(Long userNum) {
		return userMapper.delete(userNum);
	}

	@Override
	public UserDto findByUserNum(Long userNum) {
		return userMapper.findByUserNum(userNum);
	}

	@Override
	public UserDto findByUserId(String userId) {
		return userMapper.findByUserId(userId);
	}

	@Override
	public UserDto findByUserEmail(String userEmail) {
		return userMapper.findByUserEmail(userEmail);
	}

}
