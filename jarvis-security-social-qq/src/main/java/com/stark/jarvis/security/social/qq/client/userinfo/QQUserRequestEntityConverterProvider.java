package com.stark.jarvis.security.social.qq.client.userinfo;

import java.net.URI;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.stark.jarvis.security.social.client.userinfo.OAuth2UserRequestEntityConverterProvider;
import com.stark.jarvis.security.social.qq.properties.Constants;

/**
 * QQ用户请求转换器。
 * <p>将获取用户信息请求对象转换为请求体。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://wiki.connect.qq.com/%e8%8e%b7%e5%8f%96%e7%94%a8%e6%88%b7openid_oauth2-0">获取用户OpenID_OAuth2.0</a>
 */
@Component
public class QQUserRequestEntityConverterProvider implements OAuth2UserRequestEntityConverterProvider {
	
	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}
	
	@Override
	public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
		ClientRegistration clientRegistration = userRequest.getClientRegistration();

		HttpMethod httpMethod = HttpMethod.GET;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		String url = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri()
				+ "?access_token=" + userRequest.getAccessToken().getTokenValue()
				+ "&fmt=json";
		URI uri = UriComponentsBuilder.fromUriString(url)
				.build()
				.toUri();
		
		return new RequestEntity<>(headers, httpMethod, uri);
	}

}
