package com.stark.jarvis.security.social.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 获取用户信息客户端提供者。
 * <p>扩展该接口，可实现自定义的获取用户信息逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2UserInfoResponseClientProvider extends ClientProvider {
	
	/**
	 * 实现获取用户信息逻辑。
	 * @param userRequest 获取用户信息请求。
	 * @return 用户信息响应。
	 * @see DefaultOAuth2UserService#loadUser(OAuth2UserRequest)
	 */
	Map<String, Object> getUserInfoResponse(OAuth2UserRequest userRequest);

}
