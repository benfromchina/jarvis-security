package com.stark.jarvis.security.social.client.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.MultiValueMap;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 获取授权码请求转换器。
 * <p>将获取授权码请求对象转换为请求体。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2AuthorizationCodeGrantRequestEntityConverterProvider extends ClientProvider {
	
	/**
	 * 将获取授权码请求对象转换为请求体。
	 * @param authorizationCodeGrantRequest 获取授权码请求对象。
	 * @return 请求体。
	 */
	RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest);
	
	/**
	 * 根据第三方客户端生成请求头。
	 * @param clientRegistration 第三方客户端。
	 * @return 请求头。
	 */
	default HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration) {
		return OAuth2AuthorizationGrantRequestEntityUtils.getTokenRequestHeaders(clientRegistration);
	}
	
	/**
	 * 生成用来创建获取令牌请求的表单参数集合。
	 * @param authorizationCodeGrantRequest 获取授权码请求。
	 * @return 用来创建获取令牌请求的表单参数集合。
	 */
	default MultiValueMap<String, String> buildFormParameters(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		return OAuth2AuthorizationGrantRequestEntityUtils.buildFormParameters(authorizationCodeGrantRequest);
	}
	
}
