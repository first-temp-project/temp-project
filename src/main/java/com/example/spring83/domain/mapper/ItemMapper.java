package com.example.spring83.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;

@Mapper
public interface ItemMapper {
	public abstract int insert(ItemDto itemDto);
	public abstract int update(ItemDto itemDto);
	public abstract int deleteByItemNum(Long itemNum);
	public abstract List<ItemDto> findByCriteria(Criteria criteria);
	public abstract Long countByCriteria(Criteria criteria);
}

