package com.example.spring95.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring95.common.SessionUtil;
import com.example.spring95.domain.dto.CommentDto;
import com.example.spring95.domain.dto.CommentsDto;
import com.example.spring95.domain.dto.Criteria;
import com.example.spring95.domain.service.admin.AdminService;
import com.example.spring95.domain.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final AdminService adminService;
	private final HttpSession session;
	private static final ResponseEntity<Void> SUCCESS_CODE = ResponseEntity.ok().build();
	private static final ResponseEntity<Void> ERROR_CODE = ResponseEntity.badRequest().build();

	@PostMapping("/{boardNum}")
	public ResponseEntity<Void> insert(@PathVariable Long boardNum, @RequestBody CommentDto commentDto) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ERROR_CODE;
		}

		commentDto.setUserNum(userNum);
		commentDto.setBoardNum(boardNum);
		return commentService.insert(commentDto) ? SUCCESS_CODE : ERROR_CODE;
	}

	@PatchMapping("/{commentNum}")
	public ResponseEntity<Void> update(@PathVariable Long commentNum, @RequestBody CommentDto commentDto) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ERROR_CODE;
		}

		commentDto.setUserNum(userNum);
		commentDto.setCommentNum(commentNum);
		boolean isAdmin = SessionUtil.isAdmin(session);
		boolean isSuccess = isAdmin ? adminService.updateComment(commentDto) : commentService.update(commentDto);
		return isSuccess ? SUCCESS_CODE : ERROR_CODE;
	}

	@DeleteMapping("/{commentNum}")
	public ResponseEntity<Void> delete(@PathVariable Long commentNum) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ERROR_CODE;
		}

		boolean isAdmin = SessionUtil.isAdmin(session);
		boolean isSuccess = isAdmin ? adminService.deleteComment(commentNum)
				: commentService.delete(userNum, commentNum);
		return isSuccess ? SUCCESS_CODE : ERROR_CODE;
	}

	@GetMapping("/{boardNum}")
	public ResponseEntity<CommentsDto> getCommentsDto(Criteria criteria, @PathVariable Long boardNum) {
		List<CommentDto> comments = commentService.findByCriteriaAndBoardNum(criteria, boardNum);
		int getNextPageCount = commentService.getNextPageCount(criteria, boardNum);
		return new ResponseEntity<CommentsDto>(new CommentsDto(comments, getNextPageCount), HttpStatus.OK);
	}

}
