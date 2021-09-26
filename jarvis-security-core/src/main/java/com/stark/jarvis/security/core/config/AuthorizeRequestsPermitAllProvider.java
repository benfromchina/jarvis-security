package com.stark.jarvis.security.core.config;

import java.util.List;

import com.stark.jarvis.security.core.properties.AuthorizeRequestsProperties.Request;

/**
 * 无需鉴权的请求提供者。
 * <p>扩展该接口，可以配置自己无需鉴权的请求。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface AuthorizeRequestsPermitAllProvider {
	
	/**
	 * 获取无需鉴权的请求列表。
	 * @return  无需鉴权的请求列表。
	 */
	List<Request> getRequests();

}
