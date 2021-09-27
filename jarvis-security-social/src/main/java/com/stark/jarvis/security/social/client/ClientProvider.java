package com.stark.jarvis.security.social.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * 第三方客户端提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ClientProvider {
	
	/**
	 * 该提供者是否支持当前的服务端。
	 * @param clientRegistration 第三方客户端。
	 * @return 支持返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean supports(ClientRegistration clientRegistration);

}
