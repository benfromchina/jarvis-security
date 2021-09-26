package com.stark.jarvis.security.social.client.web;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * 令牌响应转换器提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2AccessTokenResponseConverterProvider {
	
	/**
	 * 该提供者是否支持当前的服务端。
	 * @param clientRegistration 第三方客户端。
	 * @return 支持返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean supports(ClientRegistration clientRegistration);
	
	/**
	 * 转换响应参数为响应对象。
	 * @param clientRegistration 第三方客户端。
	 * @param tokenResponseParameters 响应内容。
	 * @return 响应对象。
	 */
	OAuth2AccessTokenResponse convert(ClientRegistration clientRegistration, Map<String, String> tokenResponseParameters);
	
	/**
	 * 使用 {@link OAuth2AccessTokenResponseConverter} 代理转换。
	 * @see OAuth2AccessTokenResponseConverter
	 */
	default OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
		return new OAuth2AccessTokenResponseConverter().convert(tokenResponseParameters);
	}

}
