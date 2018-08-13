package com.stark.jarvis.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置提供器，各个模块和业务系统可以通过实现此接口向系统添加授权配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface AuthorizeConfigProvider {
	
	/**
	 * 配置授权。
	 * @param config
	 * @return 返回的 boolean 表示配置中是否有针对 anyRequest 的配置。在整个授权配置中，
	 * 应该有且仅有一个针对 anyRequest 的配置，如果所有的实现都没有针对 anyRequest 的配置，
	 * 系统会自动增加一个 anyRequest().authenticated() 的配置。如果有多个针对 anyRequest
	 * 的配置，则会抛出异常。
	 */
	boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
