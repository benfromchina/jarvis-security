package com.stark.jarvis.security.social.client.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 令牌响应转换器提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class OAuth2AccessTokenResponseConverterProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2AccessTokenResponseConverterProvider> providers;
	
	/**
	 * 将获取令牌响应参数转换为响应对象。
	 * @param clientRegistration 第三方客户端。
	 * @param tokenResponseParameters 获取令牌响应参数。
	 * @return 响应对象。
	 */
	public OAuth2AccessTokenResponse convert(ClientRegistration clientRegistration, Map<String, String> tokenResponseParameters) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2AccessTokenResponseConverterProvider provider : providers) {
				if (provider.supports(clientRegistration)) {
					return provider.convert(clientRegistration, tokenResponseParameters);
				}
			}
		}
		
		return new OAuth2AccessTokenResponseConverter().convert(tokenResponseParameters);
	}

}
