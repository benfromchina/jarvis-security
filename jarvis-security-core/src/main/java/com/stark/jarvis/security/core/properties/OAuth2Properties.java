package com.stark.jarvis.security.core.properties;

import lombok.Data;

/**
 * OAuth2 配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
public class OAuth2Properties {
	
	/**
	 * 允许的授权类型
	 */
	private String authorizedGrantTypes = "authorization_code,refresh_token";
	
	/** 认证请求失效时间，单位秒，默认 60 秒 */
	private int authorizationRequestExpirySeconds = 60;
	
	/**
	 * token 有效时间，单位秒，默认 2 小时
	 */
	private int accessTokenValiditySeconds = 3600 * 2;
	
	/**
	 * refresh_token 有效时间，单位秒，默认 1 个月
	 */
	private int refreshTokenValiditySeconds = 3600 * 24 * 30;
	
	/**
	 * jwt 签名秘钥
	 */
	private JwtProperties jwt = new JwtProperties();
	
}
