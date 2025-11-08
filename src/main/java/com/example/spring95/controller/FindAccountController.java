package com.example.spring95.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.example.spring95.domain.service.user.MailService;
import com.example.spring95.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/find-account")
@RequiredArgsConstructor
@Controller
public class FindAccountController {
	private final UserService userService;
	private final HttpSession session;

	@GetMapping
	public String find(RedirectAttributes attributes) {
		if (SessionUtil.isLogin(session)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ALREADY_LOGGED_IN_MSG);
			return ViewPathUtil.REDIRECT_MAIN;
		}
		return ViewPathUtil.getForwardPath("find", "find-account");
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> find(@RequestBody UserDto userDto) {
		String userEmail = userDto.getUserEmail();
		if (userEmail == null) {
			return new ResponseEntity<String>("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		UserDto dbUser = userService.findByUserEmail(userEmail);
		if (dbUser == null) {
			return new ResponseEntity<String>("존재하지 않는 이메일 입니다.", HttpStatus.BAD_REQUEST);
		}
		try {
			userService.sendEmailAndSetPassword(dbUser);
			return new ResponseEntity<String>("해당메일로 정상적으로 전송되었습니다.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("메일전송중 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
