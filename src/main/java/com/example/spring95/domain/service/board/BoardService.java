package com.example.spring95.domain.service.board;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring95.domain.dto.BoardDto;
import com.example.spring95.domain.dto.Criteria;

public interface BoardService {
	public abstract List<BoardDto> findByCriteria(Criteria criteria);

	public abstract BoardDto findByBoardNum(Long boardNum);

	public abstract void insert(BoardDto boardDto);

	public abstract void update(BoardDto boardDto);

	public abstract boolean delete(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);

	public abstract Long countByCriteria(Criteria criteria);

	public abstract boolean incrementBoardReadCount(Long boardNum);
	
	public abstract BoardDto findByUserNumAndBoardNum(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);
	
	public abstract void setBoardRegisterDate(BoardDto boardDto);
	
}
