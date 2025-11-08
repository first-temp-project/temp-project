package com.example.spring95.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.core.util.WrappedFileWatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring95.common.DateUtil;
import com.example.spring95.common.MessageConstants;
import com.example.spring95.common.SessionUtil;
import com.example.spring95.common.ViewPathUtil;
import com.example.spring95.domain.dto.Criteria;
import com.example.spring95.domain.dto.ItemDto;
import com.example.spring95.domain.dto.PageDto;
import com.example.spring95.domain.service.file.FileService;
import com.example.spring95.domain.service.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
	private final ItemService itemService;
	private final FileService fileService;
	private final HttpSession session;
	private static final String DEFAULT_DIRECTORY = "C:/upload/item";
	private static final String DEFAULT_SORT = "recent";
	private static final int DEFAULT_AMOUNT = 30;
	private static final String BASE_PATH = "item";
	private static final String LIST_PATH = "list";
	private static final String REGISTER_PATH = "register";
	private static final String UPDATE_PATH = "update";
	private static final String ITEM_KEY = "item";

	@GetMapping("/list")
	public void list(Criteria criteria, Model model) {
		criteria.setAmount(DEFAULT_AMOUNT);
		setSort(criteria);
		List<ItemDto> items = itemService.findByCriteria(criteria);
		items.forEach(itemService::setItemDiscountPrice);
		items.forEach(itemService::setItemThumbnailPath);
		model.addAttribute("items", items);
		model.addAttribute("pageDto", new PageDto(criteria, itemService.countByCriteria(criteria)));
	}

	@GetMapping("/register")
	public String register(Criteria criteria, RedirectAttributes attributes) {
		if (!SessionUtil.isLogin(session)) {
			return ViewPathUtil.REDIRECT_LOGIN;
		}

		String redirectPath = redirectIfNotAdmin(SessionUtil.isAdmin(session), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		return ViewPathUtil.getForwardPath(BASE_PATH, REGISTER_PATH);
	}

	@PostMapping("/register")
	public String register(ItemDto itemDto, MultipartFile multipartFile, Criteria criteria,
			RedirectAttributes attributes) {
		Long userNum = SessionUtil.getSessionNum(session);
		if (userNum == null) {
			return ViewPathUtil.REDIRECT_LOGIN;
		}

		String redirectPath = redirectIfNotAdmin(SessionUtil.isAdmin(session), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		itemDto.setUserNum(userNum);
		boolean isSuccess = itemService.insert(multipartFile, itemDto, DEFAULT_DIRECTORY, DateUtil.getDatePath());

		if (!isSuccess) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			attributes.addFlashAttribute(ITEM_KEY, itemDto);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, REGISTER_PATH);
		}

		return ViewPathUtil.getRedirectPath(null, BASE_PATH, LIST_PATH);
	}

	@GetMapping("/update")
	public String update(Long itemNum, Criteria criteria, RedirectAttributes attributes, Model model) {
		if (!SessionUtil.isLogin(session)) {
			return ViewPathUtil.REDIRECT_LOGIN;
		}

		String redirectPath = redirectIfNotAdmin(SessionUtil.isAdmin(session), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		if (model.getAttribute(ITEM_KEY) != null) {
			return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
		}

		redirectPath = redirectIfItemNumIsNull(itemNum, attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		ItemDto itemDto = itemService.findByItemNum(itemNum);
		redirectPath = redirectIfItemNotFound(itemDto, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}

		itemService.setItemThumbnailPath(itemDto);
		model.addAttribute(ITEM_KEY, itemDto);
		return ViewPathUtil.getForwardPath(BASE_PATH, UPDATE_PATH);
	}

	@PostMapping("/update")
	public String update(ItemDto itemDto, MultipartFile multipartFile, Criteria criteria,
			RedirectAttributes attributes) {
		if (!SessionUtil.isLogin(session)) {
			return ViewPathUtil.REDIRECT_LOGIN;
		}

		String redirectPath = redirectIfNotAdmin(SessionUtil.isAdmin(session), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		redirectPath = redirectIfItemNumIsNull(itemDto.getItemNum(), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		boolean isSuccess = itemService.update(multipartFile, itemDto, DEFAULT_DIRECTORY, DateUtil.getDatePath());
		if (isSuccess) {
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}

		ItemDto dbItem = itemService.findByItemNum(itemDto.getItemNum());
		redirectPath = redirectIfItemNotFound(dbItem, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}

		attributes.addFlashAttribute(ITEM_KEY, itemDto);
		MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
		return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, UPDATE_PATH);
	}

	@GetMapping("/delete")
	public String delete(Long itemNum, Criteria criteria, RedirectAttributes attributes) {
		if (!SessionUtil.isLogin(session)) {
			return ViewPathUtil.REDIRECT_LOGIN;
		}

		String redirectPath = redirectIfNotAdmin(SessionUtil.isAdmin(session), attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		redirectPath = redirectIfItemNumIsNull(itemNum, attributes, criteria);
		if (redirectPath != null) {
			return redirectPath;
		}

		boolean isSuccess = itemService.deleteByItemNum(itemNum);
		if (isSuccess) {
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}

		ItemDto itemDto = itemService.findByItemNum(itemNum);
		redirectPath = redirectIfItemNotFound(itemDto, criteria, attributes);
		if (redirectPath != null) {
			return redirectPath;
		}

		MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
		return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
	}

	private void setSort(Criteria criteria) {
		String sort = criteria.getSort();
		if (sort == null || sort.isBlank()) {
			criteria.setSort(DEFAULT_SORT);
		}
	}

	private String redirectIfItemNumIsNull(Long itemNum, RedirectAttributes attributes, Criteria criteria) {
		if (itemNum == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ERROR_MSG);
			return ViewPathUtil.getRedirectPath(null, BASE_PATH, LIST_PATH);
		}
		return null;
	}

	private String redirectIfNotAdmin(boolean isAdmin, RedirectAttributes attributes, Criteria criteria) {
		if (!isAdmin) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.ADMIN_NOT_ALLOW_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		return null;
	}

	private String redirectIfItemNotFound(ItemDto itemDto, Criteria criteria, RedirectAttributes attributes) {
		if (itemDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.PRODUCT_NOT_FOUND_MSG);
			return ViewPathUtil.getRedirectPath(criteria, BASE_PATH, LIST_PATH);
		}
		return null;
	}
}
