package com.stark.jarvis.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权信息管理器。
 * <p>用于收集系统中所有 {@link AuthorizeConfigProvider} 并加载其配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface AuthorizeConfigManager {
	
	/**
	 * 配置授权策略。
	 * @param config 基于 url 过滤的授权配置器。
	 */
	void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
