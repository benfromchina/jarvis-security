package com.stark.jarvis.security.social.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 第三方登录用户转换器。
 * <p>将获取用户请求的响应参数集合转为第三方登录表单。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2UserConverterProvider extends ClientProvider {
	
	/**
	 * 将获取用户请求的响应参数集合转为第三方登录表单。
	 * @param clientRegistration 第三方客户端。
	 * @param userAttributes 获取用户请求的响应参数。
	 * @return 第三方登录表单。
	 */
	UserConnectionForm convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes);

}
