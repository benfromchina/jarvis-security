package com.stark.jarvis.security.core.social.support;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 继承默认的社交登录配置，加入自定义的后处理逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class JarvisSpringSocialConfigurer extends SpringSocialConfigurer {

	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T postProcess(T object) {
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		if (socialAuthenticationFilterPostProcessor != null) {
			socialAuthenticationFilterPostProcessor.process(filter);
		}
		return (T) filter;
	}


	public SocialAuthenticationFilterPostProcessor getSocialAuthenticationFilterPostProcessor() {
		return socialAuthenticationFilterPostProcessor;
	}

	public void setSocialAuthenticationFilterPostProcessor(
			SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor) {
		this.socialAuthenticationFilterPostProcessor = socialAuthenticationFilterPostProcessor;
	}

}
