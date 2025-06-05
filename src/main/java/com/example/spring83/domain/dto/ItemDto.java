package com.example.spring83.domain.dto;

import lombok.Data;

@Data
public class ItemDto {
	private Long itemNum;
	private String itemUuid;
	private String itemName;
	private String itemUploadPath;
	private String itemTitle;
	private int itemPrice;
	private int itemDiscountPrice;
	private String itemUrl;
	private String itemCategory;
	private String itemRegisterDate;
	private String itemUpdateDate;
	private String itemThumbnailPath;
	private boolean itemIsRecommend;
}
