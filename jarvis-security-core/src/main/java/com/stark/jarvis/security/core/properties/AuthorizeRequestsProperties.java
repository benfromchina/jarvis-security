package com.stark.jarvis.security.core.properties;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 请求授权配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
public class AuthorizeRequestsProperties {
	
	/** 无需鉴权的请求列表 */
	private List<Request> permitAll = new ArrayList<>();
	
	@Data
	public static class Request {
		
		/**
		 * 请求方式:
		 * <ul>
		 * <li>* 表示所有</li>
		 * <li>GET</li>
		 * <li>HEAD</li>
		 * <li>POST</li>
		 * <li>PUT</li>
		 * <li>DELETE</li>
		 * <li>OPTIONS</li>
		 * <li>TRACE</li>
		 * </ul>
		 */
		private String httpMethod;
		
		/** 请求路径，支持 ant 表达式，多个以 "," 隔开 */
		private String path;
		
	}

}
