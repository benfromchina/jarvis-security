package com.stark.jarvis.security.browser.authorize;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.authorize.AuthorizeConfigProvider;

/**
 * 浏览器环境默认的授权配置，对常见的静态资源不验证身份。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
@Order(Integer.MIN_VALUE)
public class BrowserAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config
			.antMatchers(HttpMethod.GET,
				"/**/*.js",
				"/**/*.css",
				"/**/*.jpg",
				"/**/*.png",
				"/**/*.gif",
				"/**/*.bmp",
				"/**/*.ico").permitAll();
		return false;
	}

}
