package com.example.spring95.domain.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class Criteria {
	private int page, amount, pageRange;
	private String type, keyword, sort, category;

	public Criteria() {
		this(1, 10, 5);
	}

	public Criteria(int page, int amount, int pageRange) {
		this.page = page;
		this.amount = amount;
		this.pageRange = pageRange;
	}

	public int getOffset() {
		return (page - 1) * amount;
	}

	public int getRowCount() {
		return page * amount;
	}

	public String[] getTypes() {
		return type == null ? new String[0] : type.split("");
	}

	public String getParams() {
		return UriComponentsBuilder.newInstance().queryParam("page", page).queryParam("type", type)
				.queryParam("keyword", keyword).queryParam("category", category).queryParam("sort", sort).toUriString();
	}

}
