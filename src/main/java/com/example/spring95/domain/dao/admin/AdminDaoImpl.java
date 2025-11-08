package com.example.spring95.domain.dao.admin;

import org.springframework.stereotype.Repository;

import com.example.spring95.domain.dto.CommentDto;
import com.example.spring95.domain.mapper.AdminMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminDaoImpl implements AdminDao{
	private final AdminMapper adminMapper;

	@Override
	public int deleteComment(Long commentNum) {
		return adminMapper.deleteComment(commentNum);
	}

	@Override
	public int updateComment(CommentDto commentDto) {
		return adminMapper.updateComment(commentDto);
	}

	@Override
	public int deleteBoard(Long boardNum) {
		return adminMapper.deleteBoard(boardNum);
	}
}
