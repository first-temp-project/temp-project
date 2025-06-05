package com.example.spring83.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring83.common.MessageConstants;
import com.example.spring83.common.SessionUtil;
import com.example.spring83.domain.dto.UserDto;
import com.example.spring83.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {
	private final UserService userService;
	private final HttpSession session;
	private static final String SUCCESS_KEY = "success";

	@GetMapping("/check-password")
	public String checkPassword() {
		if (session.getAttribute(SessionUtil.KEY) == null) {
			return "redirect:/mypage/login";
		}
		return "mypage/check-password";
	}

	@PostMapping("/check-password")
	public String checkPassword(String userPassword, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return "redirect:/mypage/login";
		}
		UserDto userDto = userService.findByUserNumAndUserPassword(userNum, userPassword);
		if (userDto == null) {
			String wrongPassword = "잘못된 비밀번호 입니다.";
			attributes.addFlashAttribute("msg", wrongPassword);
			return "redirect:/mypage/check-password";
		}
		attributes.addFlashAttribute(SUCCESS_KEY, MessageConstants.SUCCESS_MSG);
		return "redirect:/mypage/update";
	}

	@GetMapping("/update")
	public String mypage(RedirectAttributes attributes, Model model) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return "redirect:/login";
		}
		if (!MessageConstants.SUCCESS_MSG.equals((String) model.getAttribute(SUCCESS_KEY))) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/mypage/check-password";
		}
		UserDto userDto = userService.findByUserNum(userNum);
		if (userDto == null) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			session.invalidate();
			return "redirect:/login";
		}
		model.addAttribute("user", userDto);
		return "mypage/update";
	}

	@PostMapping("/update")
	public String mypage(UserDto userDto, RedirectAttributes attributes) {
		Long userNum = (Long) session.getAttribute(SessionUtil.KEY);
		if (userNum == null) {
			return "redirect:/login";
		}
		if (!userService.update(userDto)) {
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute(SUCCESS_KEY, MessageConstants.SUCCESS_MSG);
			return "redirect:/mypage/update";
		}
		session.invalidate();
		return "redirect:/login";
	}

}
