package com.dayone.api.component.dart;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dayone.api.domain.dto.stock.FinancialStateList;
import com.dayone.api.domain.dto.stock.FinancialStatement;
import com.dayone.api.domain.dto.stock.KospiStockDto;
import com.dayone.api.interceptor.RestRequestInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DartApi {
	
	private static final String AUTH_KEY = "8c21de02c708970df6104b0eb537368f3fff63c2";
	
	public List<FinancialStatement> getFinancialStat(String stockNum, String bsnsYear )
	{
		final String url = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.json";
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new RestRequestInterceptor()));
        HttpHeaders httpHeaders = new HttpHeaders();
        
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("crtfc_key", AUTH_KEY) //crtfc_key
                .queryParam("corp_code", getCorpCode(stockNum)) //corp_code
                .queryParam("bsns_year", bsnsYear) //bsns_year
                .queryParam("reprt_code", getReprtCode()) //bsns_year
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        //GetForObject는 헤더를 정의할 수 없음
        ResponseEntity<FinancialStateList> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<FinancialStateList>() {});
        if( result.getStatusCode() != HttpStatus.OK ) {
        	log.error("Dart Http Resp({})", result.getStatusCodeValue());
        	return null;
        }
        
        FinancialStateList resultList = result.getBody();
        if( result == null ) {
        	log.error("Dart Http Resp body null");
        	return null;
        }
        
        String status = (String)resultList.getStatus();
        List<FinancialStatement> list = null;
        if( status.equals("0000") )
        {    
            list = resultList.getList();
        } else
        {
        	log.error("staus({}) message({})", status, resultList.getMessage());
        }
        
        return list; //내용 반환
	}	
	
	public List<HashMap<String,Object>> getFinancialStatExcel(List<FinancialStatement> list) {
		FinancialStatement dto = FinancialStatement.builder().build();
		Class<?> clazz = dto.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<HashMap<String,Object>> excelList = new ArrayList();

		for(int i=0; i<list.size();i++) {
			FinancialStatement finacialStatementDto = (FinancialStatement)list.get(i);
			HashMap<String,Object> map = new HashMap<String, Object>();
			
			for(int j=0; j<fields.length; j++) {
				fields[j].setAccessible(true);
				String key = fields[j].getName();
				try {
					map.put(key, fields[j].get(finacialStatementDto));
				} catch (IllegalArgumentException e) {
					log.error("", e);
				} catch (IllegalAccessException e) {
					log.error("", e);
				} 
			}
			excelList.add(map);
		}
		
		return excelList;
	}
	
	private String getCorpCode(String stockNum)
	{
		//852087	시디즈	134790
		return "00852087";
	}
	
	private String getReprtCode()
	{
		/*
		 * 	1분기보고서 : 11013
			반기보고서 : 11012
			3분기보고서 : 11014
			사업보고서 : 11011
		 */
		
		return "11013";
	}
	
	public List<String> getHeaderList() {
		FinancialStatement dto = FinancialStatement.builder().build();
		Class<?> clazz = dto.getClass();
		Field[] fields = clazz.getDeclaredFields();

		List<String> headerList = new ArrayList();
		for (int i = 0; i < fields.length; i++) {
			headerList.add(fields[i].getName());
		}
		return headerList;
	}

}
