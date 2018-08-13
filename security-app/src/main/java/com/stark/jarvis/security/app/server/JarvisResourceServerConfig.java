package com.stark.jarvis.security.app.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

import com.stark.jarvis.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import com.stark.jarvis.security.core.authentication.FormAuthenticationConfig;
import com.stark.jarvis.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.stark.jarvis.security.core.authorize.AuthorizeConfigManager;
import com.stark.jarvis.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * 资源服务器配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableResourceServer
public class JarvisResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	@Autowired
	private SpringSocialConfigurer jarvisSocialSecurityConfig;
	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		formAuthenticationConfig.configure(http);
		
		http.apply(validateCodeSecurityConfig)
				.and()
			.apply(smsCodeAuthenticationSecurityConfig)
				.and()
			.apply(jarvisSocialSecurityConfig)
				.and()
			.apply(openIdAuthenticationSecurityConfig)
				.and()
			.csrf().disable();
		
		authorizeConfigManager.config(http.authorizeRequests());
	}
	
}