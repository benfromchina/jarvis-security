package com.stark.jarvis.security.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.config.HttpSecurityConfigurer;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationCodeParameterNameProviderManager;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationCodeRequestFilter;

@Component
public class OAuth2AuthorizationCodeConfigurer implements HttpSecurityConfigurer {
	
	@Autowired
	private OAuth2AuthorizationCodeParameterNameProviderManager codeParameterProvider;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new OAuth2AuthorizationCodeRequestFilter(codeParameterProvider), OAuth2LoginAuthenticationFilter.class);
	}

}
