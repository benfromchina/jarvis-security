package com.stark.jarvis.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {
	
	/**
	 * 请求授权配置项
	 */
	private AuthorizeRequestsProperties authorizeRequests = new AuthorizeRequestsProperties();
	
	/**
	 * Spring OAuth2 配置项
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();

}
