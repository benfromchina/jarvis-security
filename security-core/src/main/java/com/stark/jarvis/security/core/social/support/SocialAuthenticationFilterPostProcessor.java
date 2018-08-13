package com.stark.jarvis.security.core.social.support;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * 社交登录成功的后处理器，用于在不同环境下个性化社交登录成功的配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface SocialAuthenticationFilterPostProcessor {
	
	/**
	 * 登录成功后进行业务处理。
	 * @param socialAuthenticationFilter
	 */
	void process(SocialAuthenticationFilter socialAuthenticationFilter);

}
