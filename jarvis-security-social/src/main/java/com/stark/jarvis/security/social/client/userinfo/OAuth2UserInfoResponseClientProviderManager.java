package com.stark.jarvis.security.social.client.userinfo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 获取用户信息客户端提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class OAuth2UserInfoResponseClientProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2UserInfoResponseClientProvider> providers;
	
	/**
	 * 实现获取用户信息逻辑。
	 * @param userRequest 获取用户信息请求。
	 * @return 用户信息响应。
	 * @see DefaultOAuth2UserService#loadUser(OAuth2UserRequest)
	 */
	public Map<String, Object> getUserInfoResponse(OAuth2UserRequest userRequest) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2UserInfoResponseClientProvider provider : providers) {
				if (provider.supports(userRequest.getClientRegistration())) {
					return provider.getUserInfoResponse(userRequest);
				}
			}
		}
		return null;
	}

}
