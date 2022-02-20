package com.oogo.api.controller.stock;

import com.oogo.api.domain.dto.stock.KospiStockDto;
import com.oogo.api.service.stock.StockService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

	private final StockService stockService;

	@GetMapping("/stock/all")
	public List<KospiStockDto> getKosPiStockList(HttpServletRequest request) {
		return stockService.getKosPiStockList();

	}
	
	@GetMapping("/stock/all/excel_download")
	public void getStockListExcelDownload(HttpServletRequest request, HttpServletResponse  response, 
			@RequestParam String macket, @RequestParam int stockcnt) {
		Workbook wb = stockService.getKosPiStockListExcelDownload(macket, stockcnt);
		
		String yyyyMMdd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=stok_list_"+macket+"_"+yyyyMMdd+".xlsx");
        
        // Excel File Output
        try {
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
        {
			try
			{
				if( wb != null )
					wb.close();
			} catch( Exception ex )
			{}
        }
	}
	
	@GetMapping("/financial/excel_download")
	public void getFinancialStateExcelDownload(HttpServletRequest request, HttpServletResponse  response, 
			@RequestParam String stocknum) {
		
		/*
		Workbook wb = stockService.getKosPiStockListExcelDownload(macket, stocksCount);
		
		String yyyyMMdd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=stok_list_"+macket+"_"+yyyyMMdd+".xlsx");
        
        // Excel File Output
        try {
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
        {
			try
			{
				if( wb != null )
					wb.close();
			} catch( Exception ex )
			{}
        }
        */
	}
}
