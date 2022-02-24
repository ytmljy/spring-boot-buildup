package com.oogo.api.component.dart;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oogo.api.domain.dto.stock.KospiStockDto;

@Component
public class DartApi {
	
	private static final String AUTH_KEY = "8c21de02c708970df6104b0eb537368f3fff63c2";
	
	public List<FinancialStatement> callFinancialStat(String stockNum, String bsnsYear )
	{
		final String url = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.json";
		
		RestTemplate restTemplate = new RestTemplate();
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
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        if( result.getStatusCode() != HttpStatus.OK )
        	return null;
        
        String status = (String)result.getBody().get("status");
        List<FinancialStatement> list = null;
        if( status.equals("0000") )
        {    
            Gson gson = new Gson();
            
            Type listType = new TypeToken<ArrayList<FinancialStatement>>(){}.getType();
            list = gson.fromJson((String)result.getBody().get("list"), listType);
        }
        
        return list; //내용 반환
	}
	
	private String getCorpCode(String stockNum)
	{
		//126487	F&F 홀딩스	007700
		return "126487";
	}
	
	private String getReprtCode()
	{
		/*
		 * 	1분기보고서 : 11013
			반기보고서 : 11012
			3분기보고서 : 11014
			사업보고서 : 11011
		 */
		
		return "11011";
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
