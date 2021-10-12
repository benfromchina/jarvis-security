package com.stark.jarvis.security.social.client.userinfo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Component
public class OAuth2UserConverterProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2UserConverterProvider<? extends OAuth2UserDetails, ? extends UserConnection>> providers;
	
	/**
	 * 将获取用户请求的响应参数集合转为第三方登录表单。
	 * @param clientRegistration 第三方客户端。
	 * @param userAttributes 获取用户请求的响应参数。
	 * @return 第三方登录表单。
	 */
	public UserConnectionForm<? extends OAuth2UserDetails, ? extends UserConnection> convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2UserConverterProvider<? extends OAuth2UserDetails, ? extends UserConnection> provider : providers) {
				if (provider.supports(clientRegistration)) {
					return provider.convert(clientRegistration, userAttributes);
				}
			}
		}
		return null;
	}

}
