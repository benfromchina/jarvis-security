package com.stark.jarvis.security.browser.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.stark.jarvis.security.browser.logout.JarvisLogoutSuccessHandler;
import com.stark.jarvis.security.browser.session.JarvisExpiredSessionStrategy;
import com.stark.jarvis.security.browser.session.JarvisInvalidSessionStrategy;
import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 浏览器环境下扩展点配置。
 * <p>配置在这里的 bean ，业务系统都可以通过声明同类型或同名的 bean 来覆盖安全模块默认的配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class BrowserSecurityBeanConfig {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	@ConditionalOnMissingBean(InvalidSessionStrategy.class)
	@Description("session失效的处理策略")
	public InvalidSessionStrategy invalidSessionStrategy() {
		return new JarvisInvalidSessionStrategy(securityProperties);
	}
	
	@Bean
	@ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
	@Description("并发登录导致前一个session失效的处理策略")
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new JarvisExpiredSessionStrategy(securityProperties);
	}
	
	@Bean
	@ConditionalOnMissingBean(LogoutSuccessHandler.class)
	@Description("退出成功的处理策略")
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new JarvisLogoutSuccessHandler(securityProperties.getBrowser().getLogoutUrl());
	}
	
}
