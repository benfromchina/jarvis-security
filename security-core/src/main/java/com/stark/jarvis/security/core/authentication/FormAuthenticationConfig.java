package com.stark.jarvis.security.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.boot.properties.SecurityConstants;

/**
 * 表单登录配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class FormAuthenticationConfig {

	@Autowired
	protected AuthenticationSuccessHandler jarvisAuthenticationSuccessHandler;
	@Autowired
	protected AuthenticationFailureHandler jarvisAuthenticationFailureHandler;
	
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin()
			.loginPage(SecurityConstants.UNAUTHENTICATION_URL)
			.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
			.successHandler(jarvisAuthenticationSuccessHandler)
			.failureHandler(jarvisAuthenticationFailureHandler);
	}
	
}
