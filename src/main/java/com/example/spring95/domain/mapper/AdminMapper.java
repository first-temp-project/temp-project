package com.example.spring95.domain.mapper;

import com.example.spring95.domain.dto.CommentDto;

public interface AdminMapper {
	public abstract int deleteComment(Long commentNum);
	public abstract int updateComment(CommentDto commentDto);
	public abstract int deleteBoard(Long boardNum);
}
