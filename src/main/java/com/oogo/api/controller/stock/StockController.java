package com.oogo.api.controller.stock;

import com.oogo.api.domain.dto.stock.KospiStockDto;
import com.oogo.api.service.stock.StockService;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

	private final StockService stockService;

	@GetMapping("/kospi/all")
	public List<KospiStockDto> getKosPiStockList(HttpServletRequest request) {
		return stockService.getKosPiStockList();

	}
	
	@GetMapping("/kospi/all/excel_download")
	public void getKosPiStockListExcelDownload(HttpServletRequest request, HttpServletResponse  response, String macket, int stocksCount) {
		Workbook wb = stockService.getKosPiStockListExcelDownload(macket, stocksCount);
		
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

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
}
