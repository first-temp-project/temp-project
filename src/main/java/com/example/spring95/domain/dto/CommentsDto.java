package com.example.spring95.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {
	private List<CommentDto> comments;
	private int nextCountPage;
}
