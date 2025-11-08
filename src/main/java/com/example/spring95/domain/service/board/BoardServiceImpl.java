package com.example.spring95.domain.service.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring95.common.DateUtil;
import com.example.spring95.common.Dbconstants;
import com.example.spring95.domain.dao.board.BoardDao;
import com.example.spring95.domain.dto.BoardDto;
import com.example.spring95.domain.dto.Criteria;
import com.example.spring95.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardDao boardDao;
	private final FileService fileService;
	private static final int SUCCESS_CODE = 1;

	@Override
	public List<BoardDto> findByCriteria(Criteria criteria) {
		return boardDao.findByCriteria(criteria);
	}

	@Override
	public BoardDto findByBoardNum(Long boardNum) {
		return boardDao.findByBoardNum(boardNum);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insert(BoardDto boardDto) {
		if (boardDao.insert(boardDto) != Dbconstants.SUCCESS_CODE) {
			throw new RuntimeException("boardService insert error");
		}
		fileService.insertFiles(boardDto.getInsertFiles(), boardDto.getBoardNum());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(BoardDto boardDto) {
		if (boardDao.update(boardDto) != Dbconstants.SUCCESS_CODE) {
			throw new RuntimeException("boardService insert error");
		}
		fileService.deleteFiles(boardDto.getDeleteFiles());
		fileService.insertFiles(boardDto.getInsertFiles(), boardDto.getBoardNum());
	}

	@Override
	public boolean delete(Long userNum, Long boardNum) {
		return boardDao.delete(userNum, boardNum) == Dbconstants.SUCCESS_CODE;
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		return boardDao.countByCriteria(criteria);
	}

	@Override
	public boolean incrementBoardReadCount(Long boardNum) {
		return boardDao.incrementBoardReadCount(boardNum) == Dbconstants.SUCCESS_CODE;
	}

	@Override
	public BoardDto findByUserNumAndBoardNum(Long userNum, Long boardNum) {
		return boardDao.findByUserNumAndBoardNum(userNum, boardNum);
	}

	@Override
	public void setBoardRegisterDate(BoardDto boardDto) {
		boardDto.setBoardRegisterDate(DateUtil.formatDateString(boardDto.getBoardRegisterDate()));

	}

}
