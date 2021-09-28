package com.stark.jarvis.security.core.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 安全配置器。
 * <p>扩展该接口，可接入自己的安全配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see WebSecurityConfig
 */
public interface HttpSecurityConfigurer {
	
	/**
	 * 配置安全策略。
	 * @param http 配置器。
	 * @throws Exception
	 */
	void configure(HttpSecurity http) throws Exception;

}
