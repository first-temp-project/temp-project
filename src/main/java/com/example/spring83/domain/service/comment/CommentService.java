package com.example.spring83.domain.service.comment;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring83.domain.dto.CommentDto;
import com.example.spring83.domain.dto.Criteria;

public interface CommentService {
	public abstract boolean insert(CommentDto commentDto);

	public abstract boolean update(CommentDto commentDto);

	public abstract boolean delete(@Param("userNum") Long userNum, @Param("userNum") Long commentNum);

	public abstract Long countByBoardNum(Long boardNum);

	public abstract List<CommentDto> findByCriteriaAndBoardNum(@Param("criteria") Criteria criteria,
			@Param("boardNum") Long boardNum);
	
	public abstract List<CommentDto> getNextPageCount(@Param("criteria") Criteria criteria, @Param("boardNum") Long boardNum);
}
