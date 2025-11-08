package com.example.spring95.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring95.common.FlashAttributeUtil;
import com.example.spring95.common.MessageConstants;
import com.example.spring95.common.SessionUtil;
import com.example.spring95.common.ViewPathUtil;
import com.example.spring95.domain.dto.UserDto;
import com.example.spring95.domain.service.user.UserService;
import com.google.protobuf.Message;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
	private final UserService userService;
	private final HttpSession session;
	private static final String BASE_PATH = "mypage";
	private static final String CHECK_PASSWORD_PATH = "check-password";
	private static final String UPDATE_PATH = "update";
	private static final String DELETE_PATH = "delete";
	private static final String KEY_USER = "user";

	@GetMapping("/check-password")
	public String checkPassword(RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ViewPathUtil.getRedirectLoginPath();
		}
		return ViewPathUtil.getForwardPath(BASE_PATH, CHECK_PASSWORD_PATH);
	}

	@PostMapping("/check-password")
	public String checkPassword(String userPassword, String isDelete, RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ViewPathUtil.getRedirectLoginPath();
		}
		UserDto userDto = userService.findByUserNumAndUserPassword(userNum, userPassword);
		if (userDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.PASSWORD_ERROR_MSG);
			addDeleteToAttribute(attributes, parseBoolean(isDelete));
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, CHECK_PASSWORD_PATH);
		}
		FlashAttributeUtil.addSuccess(attributes); /* check-password 통과 플래그 */
		return ViewPathUtil.getRedirectPath(null, BASE_PATH, parseBoolean(isDelete) ? DELETE_PATH : UPDATE_PATH);
	}

	@GetMapping("/update")
	public String update(RedirectAttributes attributes, Model model) {
		return getUpdateOrDeleteView(attributes, model, false);
	}

	@GetMapping("/delete")
	public String delete(RedirectAttributes attributes, Model model) {
		return getUpdateOrDeleteView(attributes, model, true);
	}

	@PostMapping("/update")
	public String update(RedirectAttributes attributes, UserDto userDto) {
		return handleUpdateOrDelete(attributes, false, userDto);
	}

	@PostMapping("/delete")
	public String delete(RedirectAttributes attributes, UserDto userDto) {
		return handleUpdateOrDelete(attributes, true, userDto);
	}

	private String getUpdateOrDeleteView(RedirectAttributes attributes, Model model, boolean isDelete) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ViewPathUtil.getRedirectLoginPath();
		}

		if (!FlashAttributeUtil.isSuccess(model)) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			addDeleteToAttribute(attributes, isDelete);
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, CHECK_PASSWORD_PATH);
		}
		UserDto userDto = userService.findByUserNum(userNum);
		if (userDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.USER_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectLoginPath();
		}
		model.addAttribute(KEY_USER, userDto);
		SessionUtil.addSuccess(session); /* get요청을 거쳐갔는지 플래그 */
		return ViewPathUtil.getForwardPath(BASE_PATH, isDelete ? DELETE_PATH : UPDATE_PATH);

	}

	private String handleUpdateOrDelete(RedirectAttributes attributes, boolean isDelete, UserDto userDto) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ViewPathUtil.getRedirectLoginPath();
		}
		if (!SessionUtil.checkSuccess(session)) { /* getUpdateOrDeleteView 거쳐왔는지 검사 */
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			addDeleteToAttribute(attributes, isDelete);
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, CHECK_PASSWORD_PATH);
		}
		if (!userNum.equals(userDto.getUserNum())) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.PERMISSION_NOT_ALLOW_MSG);
			addDeleteToAttribute(attributes, isDelete);
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, CHECK_PASSWORD_PATH);
		}
		userDto.setUserNum(userNum);
		boolean isSuccess = isDelete ? userService.delete(userNum) : userService.update(userDto);
		if (!isSuccess) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			FlashAttributeUtil.addSuccess(attributes); /* 전 get컨트롤러로 갈수있는 플래그 */
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, isDelete ? DELETE_PATH : UPDATE_PATH);
		}
		MessageConstants.addSuccessMessage(attributes,
				isDelete ? MessageConstants.DELETE_ACCOUNT_MSG : MessageConstants.PASSWORD_UPDATE_SUCCESS_MESSAGE);
		session.invalidate();
		return ViewPathUtil.getRedirectLoginPath();
	}

	private boolean parseBoolean(String isDelete) {
		return "true".equalsIgnoreCase(isDelete);
	}

	private void addDeleteToAttribute(RedirectAttributes attributes, boolean isDelete) {
		attributes.addAttribute("isDelete", isDelete);
	}

}
