package com.example.spring83.domain.service.item;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.common.Dbconstants;
import com.example.spring83.domain.dao.item.ItemDao;
import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;
import com.example.spring83.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemDao itemDao;
	private final FileService fileService;
	private static final int THUMBNAIL_SIZE = 400;

	@Override
	public ItemDto upload(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath) {
		String itemUuid = UUID.randomUUID().toString();
		String originalItemName = multipartFile.getOriginalFilename();
		String itemName = itemUuid + "_" + originalItemName;
		File file = new File(uploadPath, itemName);
		try {
			multipartFile.transferTo(file);
			itemDto.setItemUuid(itemUuid);
			itemDto.setItemUploadPath(datePath);
			itemDto.setItemName(originalItemName);
			String thumbnailPath = "t_" + itemName;
			File thumbnailFile = new File(uploadPath, thumbnailPath);
			fileService.createThumbnail(file, thumbnailFile, THUMBNAIL_SIZE);
			itemDto.setItemThumbnailPath(thumbnailPath);
			setItemDiscountPrice(itemDto, itemDto.getItemPrice());
			return itemDto;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("itemService upload error");
		}
	}

	@Override
	public boolean insert(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath) {
		try {
			if (!multipartFile.isEmpty()) {
				itemDto = upload(multipartFile, itemDto, uploadPath, datePath);
			}
			return itemDao.insert(itemDto) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("itemService insert error");
			return false;
		}
	}

	@Override
	public boolean update(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath) {
		if (!multipartFile.isEmpty()) {
			upload(multipartFile, itemDto, uploadPath, datePath);
		}
		try {
			return itemDao.update(itemDto) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("itemService update error");
			return false;
		}
	}

	@Override
	public boolean deleteByItemNum(Long itemNum) {
		try {
			return itemDao.deleteByItemNum(itemNum) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("itemService deleteByItemNum error");
			return false;
		}
	}

	@Override
	public List<ItemDto> findByCriteria(Criteria criteria) {
		try {
			return itemDao.findByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("itemService findByCriteria error");
			return Collections.emptyList();
		}
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		try {
			return itemDao.countByCriteria(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("itemService countByCriteria error");
			return 0l;
		}
	}

	@Override
	public void setItemDiscountPrice(ItemDto itemDto, int itemDiscountPrice) {
		itemDto.setItemDiscountPrice((int) (itemDiscountPrice * 0.9));
	}

}
