package com.example.spring83.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class CommentsDto {
	private List<CommentDto> comments;
	private int nextCountPage;
}
