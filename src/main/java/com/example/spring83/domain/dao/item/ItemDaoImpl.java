package com.example.spring83.domain.dao.item;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;
import com.example.spring83.domain.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {
	private final ItemMapper itemMapper;

	@Override
	public int insert(ItemDto itemDto) {
		return itemMapper.insert(itemDto);
	}

	@Override
	public int update(ItemDto itemDto) {
		return itemMapper.update(itemDto);
	}

	@Override
	public int deleteByItemNum(Long itemNum) {
		return itemMapper.deleteByItemNum(itemNum);
	}

	@Override
	public List<ItemDto> findByCriteria(Criteria criteria) {
		return itemMapper.findByCriteria(criteria);
	}

	@Override
	public Long countByCriteria(Criteria criteria) {
		return itemMapper.countByCriteria(criteria);
	}


}
