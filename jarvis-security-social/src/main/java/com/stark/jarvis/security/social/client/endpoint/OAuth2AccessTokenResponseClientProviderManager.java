package com.stark.jarvis.security.social.client.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 获取 accessToken 客户端提供者。
 * <p>扩展该接口，可实现自定义的获取 accessToken 。
 * @author Ben
 *
 */
@Component
public class OAuth2AccessTokenResponseClientProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2AccessTokenResponseClientProvider> providers;
	
	/**
	 * 实现获取 accessToken 逻辑。
	 * @param authorizationGrantRequest 授权请求。
	 * @return accessToken 响应。
	 * @see DefaultAuthorizationCodeTokenResponseClient#getTokenResponse(OAuth2AuthorizationCodeGrantRequest)
	 */
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2AccessTokenResponseClientProvider provider : providers) {
				if (provider.supports(authorizationGrantRequest.getClientRegistration())) {
					return provider.getTokenResponse(authorizationGrantRequest);
				}
			}
		}
		return null;
	}

}
