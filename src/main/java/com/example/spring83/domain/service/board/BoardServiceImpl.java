package com.example.spring83.domain.service.board;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring83.common.Dbconstants;
import com.example.spring83.domain.dao.board.BoardDao;
import com.example.spring83.domain.dto.BoardDto;
import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardDao boardDao;
	private final FileService fileService;
	private static final int SUCCESS_CODE = 1;

	@Override
	public List<BoardDto> findByCriteria(Criteria criteria) {
		try {
			return boardDao.findByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService findByCriteria error");
			return Collections.emptyList();
		}
	}

	@Override
	public BoardDto findByBoardNum(Long boardNum) {
		try {
			return boardDao.findByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService findByBoardNum error");
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(BoardDto BoardDto) {
		if (boardDao.insert(BoardDto) != Dbconstants.SUCCESS_CODE) {
			throw new RuntimeException("boardService insert error");
		}
		fileService.insertFiles(BoardDto);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(BoardDto BoardDto) {
		fileService.insertFiles(BoardDto);
		fileService.deleteFiles(BoardDto);
		if (boardDao.update(BoardDto) != Dbconstants.SUCCESS_CODE) {
			throw new RuntimeException("boardService insert error");
		}
	}

	@Override
	public boolean delete(Long userNum, Long boardNum) {
		try {
			return boardDao.delete(userNum, boardNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService delete error");
			return false;
		}
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		try {
			return boardDao.countByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService countByBoardNum error");
			return 0l;
		}
	}

	@Override
	public boolean incrementBoardReadCount(Long boardNum) {
		try {
			return boardDao.incrementBoardReadCount(boardNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardService incrementBoardReadCount error");
			return false;
		}
	}

}
