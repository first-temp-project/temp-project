package com.example.spring83.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring83.common.MessageConstants;
import com.example.spring83.common.SessionUtil;
import com.example.spring83.domain.dto.BoardDto;
import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.PageDto;
import com.example.spring83.domain.service.board.BoardService;
import com.example.spring83.domain.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final HttpSession session;
	private final BoardService boardService;
	private final CommentService commentService;
	private static final String LOGIN_URL = "redirect:/login";

	@GetMapping("/list")
	public void list(Criteria criteria, Model model) {
		if (criteria.getCategory() == null) {
			criteria.setCategory("free");
		}
		List<BoardDto> boards = boardService.findByCriteria(criteria);
		boards.forEach(board -> {
			String boardRegisterDate = formatDateString(board.getBoardRegisterDate());
			board.setBoardRegisterDate(boardRegisterDate);
		});
		model.addAttribute("boards", boards);
		model.addAttribute("pageDto", new PageDto(criteria, boardService.countByCriteria(criteria)));
	}

	@GetMapping("/write")
	public String write(Criteria criteria) {
		if (session.getAttribute(SessionUtil.KEY) == null) {
			return LOGIN_URL;
		}
		return "board/write";
	}

	@PostMapping("/write")
	public String write(BoardDto boardDto, Criteria criteria, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return LOGIN_URL;
		}
		boardDto.setUserNum(userNum);
		boardDto.setBoardCategory(criteria.getCategory());
		try {
			boardService.insert(boardDto);
			attributes.addAttribute("category", criteria.getCategory());
			return "redirect:/board/list";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/write" + criteria.getParams();
		}
	}

	@GetMapping("/detail")
	public String detail(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			attributes.addFlashAttribute("msg", MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		boardService.incrementBoardReadCount(boardNum);
		boardDto = boardService.findByBoardNum(boardNum);
		boardDto.setBoardCommentCount(commentService.countByBoardNum(boardNum));
		boardDto.setBoardRegisterDate(formatDateString(boardDto.getBoardRegisterDate()));
		model.addAttribute("board", boardDto);
		return "board/detail";
	}

	@GetMapping("/delete")
	public String delete(Criteria criteria, Long boardNum, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return LOGIN_URL;
		}
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		if (!boardService.delete(userNum, boardNum)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addAttribute("boardNum", boardNum);
			return "redirect:/board/detail" + criteria.getParams();
		}
		return "redirect:/board/list" + criteria.getParams();
	}

	@GetMapping("/update")
	public String update(Criteria criteria, Long boardNum, RedirectAttributes attributes, Model model) {
		if (session.getAttribute(SessionUtil.KEY) == null) {
			return LOGIN_URL;
		}
		if (model.getAttribute("board") != null) {
			return "board/update";
		}
		if (boardNum == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		BoardDto boardDto = boardService.findByBoardNum(boardNum);
		if (boardDto == null) {
			attributes.addFlashAttribute("msg", MessageConstants.BOARD_NOT_FOUND_MSG);
			return "redirect:/board/list" + criteria.getParams();
		}
		model.addAttribute("board", boardDto);
		return "board/update";
	}

	@PostMapping("/update")
	public String update(Criteria criteria, BoardDto boardDto, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return LOGIN_URL;
		}
		boardDto.setUserNum(userNum);
		try {
			boardService.update(boardDto);
			attributes.addAttribute("boardNum", boardDto.getBoardNum());
			return "redirect:/board/detail" + criteria.getParams();
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute("board", boardDto);
			return "redirect:/board/update" + criteria.getParams();
		}
	}

	private String formatDateString(String boardRegisterDate) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(boardRegisterDate);
			return new SimpleDateFormat("yy-MM-dd HH:mm").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return boardRegisterDate;
		}
	}
}
