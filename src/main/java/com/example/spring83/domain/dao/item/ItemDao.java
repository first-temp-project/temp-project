package com.example.spring83.domain.dao.item;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;

public interface ItemDao {
	public abstract int insert(ItemDto itemDto);
	public abstract int update(ItemDto itemDto);
	public abstract int deleteByItemNum(Long itemNum);
	public abstract List<ItemDto> findByCriteria(Criteria criteria);
	public abstract Long countByCriteria(Criteria criteria);
}
