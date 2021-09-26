package com.stark.jarvis.security.core.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;

/**
 * OAuth2.0 登录配置器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2LoginCustomizer extends Customizer<OAuth2LoginConfigurer<HttpSecurity>> {
	
}
