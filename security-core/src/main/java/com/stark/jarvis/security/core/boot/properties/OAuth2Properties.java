package com.stark.jarvis.security.core.boot.properties;

/**
 * Spring OAuth2 配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class OAuth2Properties {
	
	/**
	 * token 有效时间，单位秒，默认 2 小时
	 */
	private int accessTokenValidateSeconds = 7200;
	
	/**
	 * refresh_token 有效时间，单位秒，默认 1 个月
	 */
	private int refreshTokenValiditySeconds = 2592000;
	
	/**
	 * 使用 jwt 时为 token 签名的秘钥
	 */
	private String jwtSigningKey = "jarvis";
	
	public int getAccessTokenValidateSeconds() {
		return accessTokenValidateSeconds;
	}

	public void setAccessTokenValidateSeconds(int accessTokenValidateSeconds) {
		this.accessTokenValidateSeconds = accessTokenValidateSeconds;
	}

	public String getJwtSigningKey() {
		return jwtSigningKey;
	}

	public void setJwtSigningKey(String jwtSigningKey) {
		this.jwtSigningKey = jwtSigningKey;
	}

	public int getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
	
}
