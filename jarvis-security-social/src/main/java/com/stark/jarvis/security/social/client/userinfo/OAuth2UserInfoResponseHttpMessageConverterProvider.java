package com.stark.jarvis.security.social.client.userinfo;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 获取用户信息响应转换器。
 * <p>扩展该接口，可对获取用户信息响应做处理。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2UserInfoResponseHttpMessageConverterProvider extends ClientProvider {
	
	/**
	 * 获取支持的媒体类型。
	 * @return 支持的媒体类型。
	 */
	MediaType getSupportedMediaType();
	
	/**
	 * 响应参数集合增强。
	 * <p>扩展该接口，可对相应参数做处理。
	 * @param clientRegistration 第三方客户端。
	 * @param userAttributes 响应参数集合。
	 * @return 处理后的响应参数集合。
	 */
	Map<String, Object> enhance(ClientRegistration clientRegistration, Map<String, Object> userAttributes);

}
