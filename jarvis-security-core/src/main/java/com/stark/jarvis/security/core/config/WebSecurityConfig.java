package com.stark.jarvis.security.core.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import com.stark.jarvis.security.core.properties.AuthorizeRequestsProperties.Request;
import com.stark.jarvis.security.core.properties.SecurityProperties;

/**
 * 安全配置。
 * @author Ben
 * @since 2.0.0
 * @version 1.0.0
 */
@Configuration
@Order(99)
@ComponentScan(basePackages = "com.stark.jarvis.security.core",
	excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = Configuration.class)
	}
)
@EnableConfigurationProperties(SecurityProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired(required = false)
	private List<HttpSecurityConfigurer> httpSecurityConfigures;
	@Autowired(required = false)
	private List<AuthorizeRequestsPermitAllProvider> authorizeRequestsPermitAllProviders;
	@Autowired(required = false)
	private OAuth2ClientProperties oauth2ClientProperties;
	@Autowired(required = false)
	private OAuth2LoginCustomizer oauth2LoginCustomizer;
	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			.and().csrf().disable();
		
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
		List<Request> requestsPermitAll = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(authorizeRequestsPermitAllProviders)) {
			authorizeRequestsPermitAllProviders.forEach(provider -> {
				requestsPermitAll.addAll(provider.getRequests());
			});
		}
		if (CollectionUtils.isNotEmpty(securityProperties.getAuthorizeRequests().getPermitAll())) {
			requestsPermitAll.addAll(securityProperties.getAuthorizeRequests().getPermitAll());
		}
		if (CollectionUtils.isNotEmpty(requestsPermitAll)) {
			requestsPermitAll.forEach(request -> {
				if ("*".equals(request.getHttpMethod())) {
					authorizeRequests
						.antMatchers(request.getPath().split(","))
						.permitAll();
				} else {
					authorizeRequests
						.antMatchers(HttpMethod.valueOf(request.getHttpMethod()), request.getPath().split(","))
						.permitAll();
				}
			});
		}
		authorizeRequests
			.anyRequest()
				.authenticated();
		
		if (oauth2ClientProperties != null && MapUtils.isNotEmpty(oauth2ClientProperties.getRegistration())) {
			if (oauth2LoginCustomizer != null) {
				http.oauth2Login(oauth2LoginCustomizer);
			} else {
				http.oauth2Login();
			}
		}
		
		if (CollectionUtils.isNotEmpty(httpSecurityConfigures)) {
			for (HttpSecurityConfigurer configurer : httpSecurityConfigures) {
				configurer.configure(http);
			}
		}
	}
	
}
