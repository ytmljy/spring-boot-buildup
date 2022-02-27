package com.dayone.api.domain.dto.stock;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class FinancialStatement {
	
	private String rcept_no;
	private String reprt_code;
	private String bsns_year;
	private String corp_code;
	private String stock_code;
	private String fs_div;
	private String fs_nm;
	private String sj_div;
	private String sj_nm;
	private String account_nm;
	private String thstrm_nm;
	private String thstrm_dt;
	private String thstrm_amount;
	private String frmtrm_nm;
	private String frmtrm_dt;
	private String frmtrm_amount;
	private String ord;
	
	

}
