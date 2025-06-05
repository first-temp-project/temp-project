package com.example.spring83.domain.dto;

import lombok.Data;

@Data
public class CommentDto {
	private Long commentNum;
	private Long commentParentNum;
	private String commentParentId;
	private String commentContent;
	private String commentRegisterDate;
	private String commentUpdateDate;
	private Long userNum;
	private Long boardNum;
	private String userId;
}
