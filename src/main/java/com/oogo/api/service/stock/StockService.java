package com.oogo.api.service.stock;

import com.oogo.api.component.stock.JsoupComponent;
import com.oogo.api.domain.dto.stock.KospiStockDto;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

	private final JsoupComponent jsoupComponent;

	public List<KospiStockDto> getKosPiStockList() {
		return jsoupComponent.getKosPiStockList();
	}

	public Workbook getKosPiStockListExcelDownload() {
		List<HashMap<String,String>> bodyList = jsoupComponent.getKosPiStockListExcel();
		
		Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Kospi");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        List<String> headerList = jsoupComponent.getHeaderList();
        row = sheet.createRow(rowNum++);
        
        for(int i=0; i<headerList.size(); i++)
        {
        	cell = row.createCell(i);
            cell.setCellValue(headerList.get(i));
        }
        
        // Body
        for (int i=0; i<bodyList.size(); i++) {
            row = sheet.createRow(rowNum++);
            
            for(int j=0; j<headerList.size(); j++) {
            	cell = row.createCell(j);
                cell.setCellValue(bodyList.get(i).get(headerList.get(j)));
            }
        }
        
        return wb;
	}
}
