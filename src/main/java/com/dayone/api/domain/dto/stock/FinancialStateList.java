package com.dayone.api.domain.dto.stock;

import java.util.List;

import lombok.Data;

@Data
public class FinancialStateList {
	
	private String status;
	private String message;
	private List<FinancialStatement> list;
}
