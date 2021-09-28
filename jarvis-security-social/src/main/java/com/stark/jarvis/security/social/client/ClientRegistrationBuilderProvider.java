package com.stark.jarvis.security.social.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;

/**
 * 客户端注册构造器提供者。
 * <p>实现该接口，可自动注册客户端。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ClientRegistrationBuilderProvider {
	
	String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";
	
	/**
	 * 是否适配传入的第三方服务标识。
	 * @return 适配返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean supports(String registrationId);
	
	/**
	 * 获取客户端注册构造器。
	 * @return 客户端注册构造器。
	 */
	Builder getBuilder();
	
}
