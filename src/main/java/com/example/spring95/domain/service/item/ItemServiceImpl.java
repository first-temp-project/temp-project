package com.example.spring95.domain.service.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring95.common.Dbconstants;
import com.example.spring95.domain.dao.item.ItemDao;
import com.example.spring95.domain.dto.Criteria;
import com.example.spring95.domain.dto.FileDto;
import com.example.spring95.domain.dto.ItemDto;
import com.example.spring95.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemDao itemDao;
	private final FileService fileService;
	private static final int THUMBNAIL_SIZE = 400;

	@Override
	public boolean insert(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath) {
		upload(multipartFile, itemDto, directoryPath, datePath);
		return itemDao.insert(itemDto) == Dbconstants.SUCCESS_CODE;
	}

	@Override
	public boolean update(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath) {
		upload(multipartFile, itemDto, directoryPath, datePath);
		return itemDao.update(itemDto) == Dbconstants.SUCCESS_CODE;
	}

	@Override
	public boolean deleteByItemNum(Long itemNum) {
		return itemDao.deleteByItemNum(itemNum) == Dbconstants.SUCCESS_CODE;
	}

	@Override
	public List<ItemDto> findByCriteria(Criteria criteria) {
		return itemDao.findByCriteria(criteria);
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		return itemDao.countByCriteria(criteria);
	}

	@Override
	public ItemDto findByItemNum(Long itemNum) {
		return itemDao.findByItemNum(itemNum);
	}

	@Override
	public List<ItemDto> findByYesterDay() {
		return itemDao.findByYesterDay();
	}

	@Override
	public void upload(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return;
		}

		String itemUuid = UUID.randomUUID().toString();
		String originalItemName = multipartFile.getOriginalFilename();
		String itemName = itemUuid + "_" + originalItemName;
		File uploadPath = fileService.getUploadPath(directoryPath, datePath);
		File item = new File(uploadPath, itemName);
		try {
			multipartFile.transferTo(item);
			itemDto.setItemUuid(itemUuid);
			itemDto.setItemName(originalItemName);
			itemDto.setItemUploadPath(datePath);
			fileService.createThumbnails(multipartFile, new File(uploadPath, "t_" + itemName), THUMBNAIL_SIZE);
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("itemService upload error");
		}
	}

	@Override
	public String getItemThumbnailPath(ItemDto itemDto) {
		return itemDto.getItemUploadPath() + "/t_" + itemDto.getItemUuid() + "_" + itemDto.getItemName();
	}

	@Override
	public String getItemPath(ItemDto itemDto) {
		return getItemThumbnailPath(itemDto).replace("t_", "");
	}

	@Override
	public void setItemThumbnailPath(ItemDto itemDto) {
		itemDto.setItemThumbnailPath(getItemThumbnailPath(itemDto));
	}

	@Override
	public void setItemDiscountPrice(ItemDto itemDto) {
		itemDto.setItemDiscountPrice((int) (itemDto.getItemPrice() * 0.9));
	}

	@Override
	public void autoDeleteFiles(List<ItemDto> yesterdayFiles, String directoryPath, String yesterdayPath) {
		List<Path> paths = new ArrayList<>();
		yesterdayFiles.stream().map(item -> Paths.get(directoryPath, getItemPath(item))).forEach(paths::add);
		yesterdayFiles.stream().map(item -> Paths.get(directoryPath, getItemThumbnailPath(item))).forEach(paths::add);
		File[] files = new File(directoryPath, yesterdayPath).listFiles();
		files = files == null ? new File[0] : files;
		Arrays.stream(files).filter(file -> !paths.contains(file.toPath())).forEach(File::delete);
	}

}
