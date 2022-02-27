package com.dayone.api.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestRequestInterceptor implements ClientHttpRequestInterceptor {
	private final int MAX_BODY_CONTENTS_LENGTH = 2000;

	@Override public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) 
			throws IOException 
	{
		ClientHttpResponse response = execution.execute(request, body); 
		HttpStatus httpStatus = response.getStatusCode(); 
		if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError())  { 
			writeErrorLog(request, body, response); 
		} else { 
			writeDebugLog(request, body, response); 
		} 
		return response; 
	}

	private void writeDebugLog(HttpRequest request, byte[] body, ClientHttpResponse response) 
			throws IOException 
	{ 
		if (log.isDebugEnabled()) { 
			ToStringBuilder builder = new ToStringBuilder("\n", ToStringStyle.MULTI_LINE_STYLE) 
					.append("=========================================================") 
					.append("| RestServiceDebugLog") .append("|--------------------------------------------------------") 
					.append("| URI :: " + request.getURI()) .append("| Method :: " + request.getMethod()) 
					//.append("| Headers :: " + response.getHeaders()) 
					.append("| RequestBody :: " + getRequestBody(body)) 
					.append("|--------------------------------------------------------") 
					.append("| Status Code :: " + response.getStatusCode()) 
					.append("| Status Text :: " + response.getStatusText()) 
					//.append("| Headers :: " + response.getHeaders()) 
					.append("| ResponseBody :: " + getResponseBody(response.getBody())); 
			builder.append("========================================================="); 
			log.debug(builder.toString()); 
		}
	} 
	
	private void writeErrorLog(HttpRequest request, byte[] body, ClientHttpResponse response) 
			throws IOException 
	{ 
		if (log.isErrorEnabled()) { 
			ToStringBuilder builder = new ToStringBuilder("\n", ToStringStyle.MULTI_LINE_STYLE) 
					.append("=========================================================") 
					.append("| RestServicErrorLog") .append("|--------------------------------------------------------") 
					.append("| URI :: " + request.getURI()) .append("| Method :: " + request.getMethod()) 
					//.append("| Headers :: " + response.getHeaders()) 
					.append("| RequestBody :: " + getRequestBody(body)) 
					.append("|--------------------------------------------------------") 
					.append("| Status Code :: " + response.getStatusCode())
					.append("| Status Text :: " + response.getStatusText()) 
					//.append("| Headers :: " + response.getHeaders()) 
					.append("| ResponseBody :: " + getResponseBody(response.getBody())); 
			builder.append("========================================================="); 
			log.error(builder.toString()); 
		} 
	} 
	
	private String getRequestBody(byte[] body) { 
		return StringUtils.left( IOUtils.toString(body, StandardCharsets.UTF_8.name()), MAX_BODY_CONTENTS_LENGTH); 
	} 
	
	private String getResponseBody(InputStream stream) { 
		try { 
			return StringUtils.left( IOUtils.toString(stream, StandardCharsets.UTF_8.name()), MAX_BODY_CONTENTS_LENGTH); 
		} catch (IOException e) { 
			return ""; 
		} 
	} 
}