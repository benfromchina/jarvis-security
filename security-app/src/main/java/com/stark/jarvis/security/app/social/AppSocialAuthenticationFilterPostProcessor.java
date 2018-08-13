package com.stark.jarvis.security.app.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.social.support.SocialAuthenticationFilterPostProcessor;

/**
 * APP 环境下，认证成功后将 token 返回到前台。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterPostProcessor  {

	@Autowired
	private AuthenticationSuccessHandler jarvisAuthenticationSuccessHandler;
	
	@Override
	public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
		socialAuthenticationFilter.setAuthenticationSuccessHandler(jarvisAuthenticationSuccessHandler);
	}

}
