package com.stark.jarvis.security.social.client.endpoint;

import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 获取 accessToken 客户端提供者。
 * <p>扩展该接口，可实现自定义的获取 accessToken 。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2AccessTokenResponseClientProvider extends ClientProvider {
	
	
	/**
	 * 实现获取 accessToken 逻辑。
	 * @param authorizationGrantRequest 授权请求。
	 * @return accessToken 响应。
	 * @see DefaultAuthorizationCodeTokenResponseClient#getTokenResponse(OAuth2AuthorizationCodeGrantRequest)
	 */
	OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest);

}
