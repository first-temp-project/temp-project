package com.example.spring83.domain.service.item;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;

public interface ItemService {
	public abstract ItemDto upload(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath);

	public abstract boolean insert(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath);

	public abstract boolean update(MultipartFile multipartFile, ItemDto itemDto, String uploadPath, String datePath);

	public abstract boolean deleteByItemNum(Long itemNum);

	public abstract List<ItemDto> findByCriteria(Criteria criteria);

	public abstract Long countByCriteria(Criteria criteria);

	public abstract void setItemDiscountPrice(ItemDto itemDto, int itemDiscountPrice);
}
