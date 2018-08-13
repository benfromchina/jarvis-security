package com.stark.jarvis.security.core.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 工具类。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class Utils {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 判断请求是否接收 text/html 类型的响应。
	 * @param request HTTP 请求对象。
	 * @return 接收返回 true ，否则返回 false 。
	 */
	public static boolean acceptHtml(HttpServletRequest request) {
		String accept = request.getHeader("Accept");
		boolean flag = StringUtils.isNotBlank(accept) && accept.contains(MediaType.TEXT_HTML_VALUE);
		return flag;
	}
	
	/**
	 * 以 json 类型返回响应。
	 * @param httpStatus 状态码。
	 * @param response HTTP 响应对象。
	 * @param javabean 响应对象。
	 * @throws IOException {@link HttpServletResponse#getWriter()} 抛出异常。
	 * @throws JsonProcessingException javabean 转 json 对象时抛出异常。
	 */
	public static void responseAsJson(HttpStatus httpStatus, HttpServletResponse response, Object javabean) throws JsonProcessingException, IOException {
		if (httpStatus == null) {
			httpStatus = HttpStatus.OK;
		}
		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(javabean));
	}
	
	/**
	 * 以 json 类型返回响应，状态码 200 。
	 * @param response HTTP 响应对象。
	 * @param javabean 响应对象。
	 * @throws IOException {@link HttpServletResponse#getWriter()} 抛出异常。
	 * @throws JsonProcessingException javabean 转 json 对象时抛出异常。
	 */
	public static void responseAsJson(HttpServletResponse response, Object javabean) throws JsonProcessingException, IOException {
		Utils.responseAsJson(HttpStatus.OK, response, javabean);
	}
	
}
