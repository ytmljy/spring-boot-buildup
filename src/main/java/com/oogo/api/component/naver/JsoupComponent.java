package com.oogo.api.component.naver;

import com.oogo.api.domain.dto.stock.KospiStockDto;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsoupComponent {

	public List<KospiStockDto> getKosPiStockList() {
		final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
		Connection conn = Jsoup.connect(stockList);
		try {
			Document document = conn.get();
			return getKosPiStockList(document);
		} catch (IOException ignored) {
		}
		return null;
	}

	public List<KospiStockDto> getKosPiStockList(Document document) {
		Elements kosPiTable = document.select("table.type_2 tbody tr");
		List<KospiStockDto> list = new ArrayList<>();
		for (Element element : kosPiTable) {
			if (element.attr("onmouseover").isEmpty()) {
				continue;
			}
			list.add(createKosPiStockDto(element.select("td")));
		}
		return list;
	}

	public KospiStockDto createKosPiStockDto(Elements td) {
		KospiStockDto kospiStockDto = KospiStockDto.builder().build();
		Class<?> clazz = kospiStockDto.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < td.size(); i++) {
			String text;
			if (td.get(i).select(".center a").attr("href").isEmpty()) {
				text = td.get(i).text();
			} else {
				text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
			}
			fields[i].setAccessible(true);
			try {
				fields[i].set(kospiStockDto, text);
			} catch (Exception ignored) {
			}
		}
		return kospiStockDto;
	}
	
	public List<String> getHeaderList() {
		KospiStockDto kospiStockDto = KospiStockDto.builder().build();
		Class<?> clazz = kospiStockDto.getClass();
		Field[] fields = clazz.getDeclaredFields();

		List<String> headerList = new ArrayList();
		for (int i = 0; i < fields.length; i++) {
			headerList.add(fields[i].getName());
		}
		return headerList;
	}
	
	public List<HashMap<String, Object>> getKosPiStockListExcel(String macket, int stocksCount) {
		List<HashMap<String,Object>> mapList = new ArrayList<HashMap<String,Object>>();
		
		try {
			int pageCount = ( (stocksCount-1) / PAGE_NUM) + 1;
			
			for(int k=0; k<pageCount; k++)
			{
				List<KospiStockDto> list = getStockList(macket, k+1);
				
				KospiStockDto kospiStockDto = KospiStockDto.builder().build();
				Class<?> clazz = kospiStockDto.getClass();
				Field[] fields = clazz.getDeclaredFields();
				
				for(int i=0; i<list.size(); i++){
					KospiStockDto kosipStockDto = list.get(i);
					HashMap map = new HashMap();
					for(int j=0; j<fields.length; j++) {
						fields[j].setAccessible(true);
						
						String key = fields[j].getName();
						try {
							map.put(key, fields[j].get(kosipStockDto));
						} catch (IllegalArgumentException e) {
							log.error("", e);
						} catch (IllegalAccessException e) {
							log.error("", e);
						}
						
					}
					mapList.add(map);
				}
			}
			
			return mapList;
		} catch (Exception ignored) {
			log.error("", ignored);
		}
		
		return null;
	}
	
	private List<KospiStockDto> getStockList(String market, int page) {
		String stockList = null;
		
		if( KOSPI.equals(market) )
			stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=" + page;
		else
			stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?sosok=1&page=" + page;
		
		log.info("URL:" + stockList);
		
		Connection conn = Jsoup.connect(stockList);
		try {
			Document document = conn.get();
			List<KospiStockDto> list = getKosPiStockList(document);
			
			return list;
		} catch (IOException ignored) {
			log.error("", ignored);
		}
		
		return null;
	}
	
	public static final String KOSPI = "KOSPI";
	public static final String KOSDAQ = "KOSDAQ";
	
	public static final int PAGE_NUM = 50;
}
