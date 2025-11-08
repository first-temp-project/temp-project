package com.example.spring95.domain.dao.admin;

import com.example.spring95.domain.dto.CommentDto;

public interface AdminDao {
	public abstract int deleteComment(Long commentNum);
	public abstract int updateComment(CommentDto commentDto);
	public abstract int deleteBoard(Long boardNum);
}
