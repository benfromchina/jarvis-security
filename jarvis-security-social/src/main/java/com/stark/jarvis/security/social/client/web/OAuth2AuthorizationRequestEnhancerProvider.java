package com.stark.jarvis.security.social.client.web;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 认证请求增强器提供者。
 * <p>扩展该接口，可用于非标准 OAuth2.0 实现的第三方修改请求参数。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2AuthorizationRequestEnhancerProvider extends ClientProvider {
	
	/**
	 * 编辑请求。
	 * @param clientRegistration 第三方客户端。
	 * @param request 请求。
	 * @return 编辑后的请求。
	 */
	OAuth2AuthorizationRequest enhance(ClientRegistration clientRegistration, OAuth2AuthorizationRequest request); 

}
