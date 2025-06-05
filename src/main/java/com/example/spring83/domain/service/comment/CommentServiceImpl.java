package com.example.spring83.domain.service.comment;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring83.domain.dao.comment.CommentDao;
import com.example.spring83.domain.dto.CommentDto;
import com.example.spring83.domain.dto.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentDao commentDao;
	private static final int SUCCESS_CODE = 1;

	@Override
	public boolean insert(CommentDto commentDto) {
		try {
			return commentDao.insert(commentDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService insert error");
			return false;
		}
	}

	@Override
	public boolean update(CommentDto commentDto) {
		try {
			return commentDao.update(commentDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService update error");
			return false;
		}
	}

	@Override
	public boolean delete(Long userNum, Long commentNum) {
		try {
			return commentDao.delete(userNum, commentNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService delete error");
			return false;
		}
	}

	@Override
	public Long countByBoardNum(Long boardNum) {
		try {
			return commentDao.countByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService countByBoardNum error");
			return 0l;
		}
	}

	@Override
	public List<CommentDto> findByCriteriaAndBoardNum(Criteria criteria, Long boardNum) {
		try {
			return commentDao.findByCriteriaAndBoardNum(criteria, boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService findByCriteriaAndBoardNum error");
			return Collections.emptyList();
		}
	}

	@Override
	public List<CommentDto> getNextPageCount(Criteria criteria, Long boardNum) {
		try {
			return commentDao.getNextPageCount(criteria, boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService getNextPageCount error");
			return Collections.emptyList();
		}
	}
}
