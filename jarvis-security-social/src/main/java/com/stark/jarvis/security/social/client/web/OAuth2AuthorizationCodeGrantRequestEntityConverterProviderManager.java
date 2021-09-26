package com.stark.jarvis.security.social.client.web;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 获取授权码请求转换器管理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see SocialAuthorizationCodeTokenResponseClient
 */
@Component
public class OAuth2AuthorizationCodeGrantRequestEntityConverterProviderManager {

	@Autowired(required = false)
	private List<OAuth2AuthorizationCodeGrantRequestEntityConverterProvider> providers;
	
	/**
	 * 将获取授权码请求对象转换为请求体。
	 * @param authorizationCodeGrantRequest 获取授权码请求对象。
	 * @return 请求体。
	 */
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2AuthorizationCodeGrantRequestEntityConverterProvider provider : providers) {
				if (provider.supports(clientRegistration)) {
					return provider.convert(authorizationCodeGrantRequest);
				}
			}
		}

		HttpHeaders headers = OAuth2AuthorizationGrantRequestEntityUtils.getTokenRequestHeaders(clientRegistration);
		MultiValueMap<String, String> formParameters = OAuth2AuthorizationGrantRequestEntityUtils.buildFormParameters(authorizationCodeGrantRequest);
		String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
		HttpMethod httpMethod = HttpMethod.POST;
		URI uri = UriComponentsBuilder.fromUriString(tokenUri)
				.build()
				.toUri();
		return new RequestEntity<>(formParameters, headers, httpMethod, uri);
	}

}
