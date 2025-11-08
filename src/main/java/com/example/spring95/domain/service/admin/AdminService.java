package com.example.spring95.domain.service.admin;

import com.example.spring95.domain.dto.CommentDto;

public interface AdminService {
	public abstract boolean deleteComment(Long commentNum);
	public abstract boolean updateComment(CommentDto commentDto);
	public abstract boolean deleteBoard(Long boardNum);
}
