package com.oogo.api.service.stock;

import com.oogo.api.component.naver.JsoupComponent;
import com.oogo.api.domain.dto.stock.KospiStockDto;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

	private final JsoupComponent jsoupComponent;

	public List<KospiStockDto> getKosPiStockList() {
		return jsoupComponent.getKosPiStockList();
	}

	public Workbook getKosPiStockListExcelDownload(String macket, int stocksCount) {
		List<HashMap<String,Object>> bodyList = jsoupComponent.getKosPiStockListExcel(macket, stocksCount);
		
		Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(macket);
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
        
    	cell = row.createCell(headerList.size());
        cell.setCellValue("roe/per");
    
        
        // Body
        for (int i=0; i<bodyList.size(); i++) {
            row = sheet.createRow(rowNum++);
            
            String per = null;
            String roe = null;
            for(int j=0; j<headerList.size(); j++) {
            	cell = row.createCell(j);
            	String obj = (String)bodyList.get(i).get(headerList.get(j));
            	
            	if( "per".equals(headerList.get(j)) ) {
            		if( "N/A".equals(obj) ) {
                		cell.setCellValue("");
                		per = "";
            		} else {
            			obj = obj.replaceAll(",", "");
                		cell.setCellValue(Double.valueOf(obj));
                		per = obj +"";
            		}
            	}
            	else if( "roe".equals(headerList.get(j)) )
            	{
            		if( "N/A".equals(obj) ) {
                		cell.setCellValue("");
                		roe = "";
            		} else {
            			obj = obj.replaceAll(",", "");
                		cell.setCellValue(Double.valueOf(obj));
                		roe = obj +"";
                	}
            	} else
            	{
            		cell.setCellValue(obj);
            	}
            }
            
        	cell = row.createCell(headerList.size());
        	
        	if( !"".equals(per) && !"".equals(roe) )
        		cell.setCellValue(Double.valueOf(roe)/Double.valueOf(per));
        	else
        		cell.setCellValue("");
            
        }
        
        return wb;
	}
	
	public Workbook getFinancialStateExcelDownload(String stockNum) {
		List<HashMap<String,Object>> bodyList = jsoupComponent.getKosPiStockListExcel(macket, stocksCount);
		
		Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(macket);
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
        
    	cell = row.createCell(headerList.size());
        cell.setCellValue("roe/per");
    
        
        // Body
        for (int i=0; i<bodyList.size(); i++) {
            row = sheet.createRow(rowNum++);
            
            String per = null;
            String roe = null;
            for(int j=0; j<headerList.size(); j++) {
            	cell = row.createCell(j);
            	String obj = (String)bodyList.get(i).get(headerList.get(j));
            	
            	if( "per".equals(headerList.get(j)) ) {
            		if( "N/A".equals(obj) ) {
                		cell.setCellValue("");
                		per = "";
            		} else {
            			obj = obj.replaceAll(",", "");
                		cell.setCellValue(Double.valueOf(obj));
                		per = obj +"";
            		}
            	}
            	else if( "roe".equals(headerList.get(j)) )
            	{
            		if( "N/A".equals(obj) ) {
                		cell.setCellValue("");
                		roe = "";
            		} else {
            			obj = obj.replaceAll(",", "");
                		cell.setCellValue(Double.valueOf(obj));
                		roe = obj +"";
                	}
            	} else
            	{
            		cell.setCellValue(obj);
            	}
            }
            
        	cell = row.createCell(headerList.size());
        	
        	if( !"".equals(per) && !"".equals(roe) )
        		cell.setCellValue(Double.valueOf(roe)/Double.valueOf(per));
        	else
        		cell.setCellValue("");
            
        }
        
        return wb;
	}
}
