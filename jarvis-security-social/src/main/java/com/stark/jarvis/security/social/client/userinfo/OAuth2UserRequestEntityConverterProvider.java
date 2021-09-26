package com.stark.jarvis.security.social.client.userinfo;

import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

/**
 * 用户请求转换器提供者。
 * <p>将获取用户信息请求对象转换为请求体。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2UserRequestEntityConverterProvider {
	
	/**
	 * 该提供者是否支持当前的服务端。
	 * @param userRequest 获取用户信息请求对象。
	 * @return 支持返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean supports(OAuth2UserRequest userRequest);
	
	/**
	 * 将获取用户信息请求对象转换为请求体。
	 * @param userRequest 获取用户信息请求对象。
	 * @return 请求体。
	 */
	RequestEntity<?> convert(OAuth2UserRequest userRequest);
	
	/**
	 * 从获取用户信息请求对象中获取第三方客户端。
	 * @param userRequest 获取用户信息请求对象。
	 * @return 第三方客户端。
	 */
	default ClientRegistration getClientRegistration(OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration();
	}

}
