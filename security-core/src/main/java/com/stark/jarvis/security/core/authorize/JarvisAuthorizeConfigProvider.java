package com.stark.jarvis.security.core.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.boot.properties.SecurityConstants;
import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 核心模块的授权配置提供器，安全模块涉及的 url 的授权配置在这里。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
@Order(Integer.MIN_VALUE)
public class JarvisAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.antMatchers(SecurityConstants.UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPENID,
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
				securityProperties.getBrowser().getLoginPage(),
				securityProperties.getBrowser().getRegisterPage(),
				securityProperties.getBrowser().getSession().getSessionInvalidUrl()).permitAll();

		if (StringUtils.isNotBlank(securityProperties.getBrowser().getLogoutUrl())) {
			config.antMatchers(securityProperties.getBrowser().getLogoutUrl()).permitAll();
		}
		
		if (StringUtils.isNotBlank(securityProperties.getPermitAllUrl())) {
			String[] urls = securityProperties.getPermitAllUrl().split(",");
			String method, url;
			for (String methodUrl : urls) {
				if (StringUtils.contains(methodUrl, "(") && StringUtils.contains(methodUrl, ")")) {
					method = StringUtils.substringBefore(methodUrl, "(").trim();
					url = StringUtils.substringBetween(methodUrl, "(", ")").trim();
					config.antMatchers(HttpMethod.valueOf(method.toUpperCase()), url).permitAll();
				} else {
					config.antMatchers(methodUrl).permitAll();
				}
			}
		}
		
		return false;
	}

}
