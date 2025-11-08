package com.example.spring95.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring95.common.MessageConstants;
import com.example.spring95.common.SessionUtil;
import com.example.spring95.common.ViewPathUtil;
import com.example.spring95.domain.dto.UserDto;
import com.example.spring95.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/join")
@Controller
@RequiredArgsConstructor
public class JoinController {
	private final UserService userService;
	private final HttpSession session;
	private final String BASE_PATH = "join";
	private final String KEY_USER = "user";

	@GetMapping
	public String join(RedirectAttributes attributes) {
		if (SessionUtil.isLogin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ALREADY_LOGGED_IN_MSG);
			return ViewPathUtil.REDIRECT_MAIN;
		}
		return ViewPathUtil.getForwardPath(BASE_PATH, BASE_PATH);
	}

	@PostMapping
	public String join(UserDto userDto, RedirectAttributes attributes) {
		if (SessionUtil.isLogin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ALREADY_LOGGED_IN_MSG);
			return ViewPathUtil.REDIRECT_MAIN;
		}
		boolean isSuccess = userService.insert(userDto);
		if (!isSuccess) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute(KEY_USER, userDto);
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, "");
		}
		MessageConstants.addSuccessMessage(attributes, MessageConstants.JOIN_SUCCESS_MESSAGE);
		return ViewPathUtil.REDIRECT_LOGIN;
	}

	@ResponseBody
	@PostMapping(value = "/checkId", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkId(@RequestBody UserDto userDto) {
		String userId = userDto.getUserId();
		if (userId == null) {
			return ResponseEntity.badRequest().build();
		}
		boolean result = userService.findByUserId(userId) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/checkEmail", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkEmail(@RequestBody UserDto userDto) {
		String userEmail = userDto.getUserEmail();
		if (userEmail == null) {
			return ResponseEntity.badRequest().build();
		}
		boolean result = userService.findByUserEmail(userEmail) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

}
