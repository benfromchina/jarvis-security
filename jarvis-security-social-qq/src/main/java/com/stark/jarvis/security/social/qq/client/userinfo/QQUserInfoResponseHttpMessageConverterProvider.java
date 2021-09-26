package com.stark.jarvis.security.social.qq.client.userinfo;

import java.net.URI;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.social.client.userinfo.OAuth2UserInfoResponseHttpMessageConverterProvider;

/**
 * QQ获取用户信息响应转换器。
 * <p>通过<a href="https://wiki.connect.qq.com/%e8%8e%b7%e5%8f%96%e7%94%a8%e6%88%b7openid_oauth2-0">获取用户OpenID_OAuth2.0</a>获取到<code>openid</code>后，再通过<a href="https://wiki.connect.qq.com/get_user_info">get_user_info</a>接口获取用户昵称、头像等信息。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://wiki.connect.qq.com/%e8%8e%b7%e5%8f%96%e7%94%a8%e6%88%b7openid_oauth2-0">获取用户OpenID_OAuth2.0</a>
 * @see <a href="https://wiki.connect.qq.com/get_user_info">get_user_info</a>
 */
@Component
public class QQUserInfoResponseHttpMessageConverterProvider implements OAuth2UserInfoResponseHttpMessageConverterProvider {
	
	@Override
	public MediaType getSupportedMediaType() {
		return MediaType.TEXT_HTML;
	}

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return "qq".equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> enhance(ClientRegistration clientRegistration, Map<String, Object> map) {
		String accessToken = MapUtils.getString(map, "accessToken");
		String openid = MapUtils.getString(map, "openid");
		
		String url = "https://graph.qq.com/user/get_user_info"
				+ "?access_token=" + accessToken
				+ "&oauth_consumer_key=" + clientRegistration.getClientId()
				+ "&openid=" + openid;
		URI uri = UriComponentsBuilder.fromUriString(url)
				.build()
				.toUri();
		
		// 根据 openid 获取用户信息，参考 https://wiki.connect.qq.com/get_user_info
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		String responseStr = restTemplate.getForObject(uri, String.class);
		Map<String, Object> response;
		try {
			response = new ObjectMapper().readValue(responseStr, Map.class);
		} catch (Exception e) {
			OAuth2Error oauth2Error = new OAuth2Error("get_user_info_error", "An error occurred while attempting to resolve the userinfo: " + e.getMessage(), null);
			throw new OAuth2AuthenticationException(oauth2Error, e);
		}
		Integer ret = MapUtils.getInteger(response, "ret");
		String msg = MapUtils.getString(response, "msg");
		if (ret != 0) {
			OAuth2Error oauth2Error = new OAuth2Error(String.valueOf(ret), msg, null);
			throw new OAuth2AuthenticationException(oauth2Error);
		}
		response.remove("ret");
		response.remove("msg");
		map.putAll(response);
		return map;
	}

}
