package com.example.spring95.domain.service.item;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring95.domain.dto.Criteria;
import com.example.spring95.domain.dto.FileDto;
import com.example.spring95.domain.dto.ItemDto;

public interface ItemService {
	public abstract boolean insert(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath);
	public abstract boolean update(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath);
	public abstract boolean deleteByItemNum(Long itemNum);
	public abstract List<ItemDto> findByCriteria(Criteria criteria);
	public abstract Long countByCriteria(Criteria criteria);
	public abstract ItemDto findByItemNum(Long itemNum);
	public abstract List<ItemDto> findByYesterDay();
	public abstract void upload(MultipartFile multipartFile, ItemDto itemDto, String directoryPath, String datePath);
	public abstract String getItemThumbnailPath(ItemDto itemDto);
	public abstract String getItemPath(ItemDto itemDto);
	public abstract void setItemThumbnailPath(ItemDto itemDto);
	public abstract void setItemDiscountPrice(ItemDto itemDto);
	public abstract void autoDeleteFiles(List<ItemDto> yesterdayFiles, String directoryPath, String yesterdayPath);
}
