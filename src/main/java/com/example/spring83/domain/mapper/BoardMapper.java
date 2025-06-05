package com.example.spring83.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring83.domain.dto.BoardDto;
import com.example.spring83.domain.dto.Criteria;

@Mapper
public interface BoardMapper {
	public abstract List<BoardDto> findByCriteria(Criteria criteria);

	public abstract BoardDto findByBoardNum(Long boardNum);

	public abstract int insert(BoardDto BoardDto);

	public abstract int update(BoardDto BoardDto);

	public abstract int delete(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);

	public abstract Long countByCriteria(Criteria criteria);

	public abstract int incrementBoardReadCount(Long boardNum);
}
