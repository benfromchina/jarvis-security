package com.stark.jarvis.security.social.qq.client.web;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationCodeGrantRequestEntityConverterProvider;
import com.stark.jarvis.security.social.qq.properties.Constants;

/**
 * QQ 获取授权码请求转换器。
 * <p>将获取授权码请求对象转换为请求体。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://wiki.connect.qq.com/%e4%bd%bf%e7%94%a8authorization_code%e8%8e%b7%e5%8f%96access_token">使用Authorization_Code获取Access_Token</a>
 */
@Component
public class QQAuthorizationCodeGrantRequestConverterProvider implements OAuth2AuthorizationCodeGrantRequestEntityConverterProvider {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}
	
	@Override
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
		HttpHeaders headers = getTokenRequestHeaders(clientRegistration);
		MultiValueMap<String, String> formParameters = buildFormParameters(authorizationCodeGrantRequest);
		HttpMethod httpMethod = HttpMethod.GET;
		// 因历史原因，默认是x-www-form-urlencoded格式，如果填写json，则返回json格式
		String tokenUri = clientRegistration.getProviderDetails().getTokenUri()
				+ "?grant_type=" + formParameters.getFirst("grant_type")
				+ "&client_id=" + clientRegistration.getClientId()
				+ "&client_secret=" + clientRegistration.getClientSecret()
				+ "&code=" + formParameters.getFirst("code")
				+ "&redirect_uri=" + formParameters.getFirst("redirect_uri")
				+ "&fmt=json";
		formParameters.clear();
		URI uri = UriComponentsBuilder.fromUriString(tokenUri)
				.build()
				.toUri();
		return new RequestEntity<>(formParameters, headers, httpMethod, uri);
	}

}
