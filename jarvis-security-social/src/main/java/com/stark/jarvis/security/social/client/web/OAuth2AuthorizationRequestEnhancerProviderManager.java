package com.stark.jarvis.security.social.client.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 认证请求增强器提供者。
 * <p>扩展该接口，可用于非标准 OAuth2.0 实现的第三方修改请求参数。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class OAuth2AuthorizationRequestEnhancerProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2AuthorizationRequestEnhancerProvider> providers;
	
	/**
	 * 编辑请求。
	 * @param clientRegistration 第三方客户端。
	 * @param request 请求。
	 * @return 编辑后的请求。
	 */
	public OAuth2AuthorizationRequest enhance(ClientRegistration clientRegistration, OAuth2AuthorizationRequest request) {
		if (clientRegistration == null || CollectionUtils.isEmpty(providers)) {
			return request;
		}
		for (OAuth2AuthorizationRequestEnhancerProvider provider : providers) {
			if (provider.supports(clientRegistration)) {
				return provider.enhance(clientRegistration, request);
			}
		}
		return request;
	}

}
