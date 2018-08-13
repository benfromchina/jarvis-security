package com.stark.jarvis.security.app.social;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.boot.properties.SecurityConstants;
import com.stark.jarvis.security.core.social.support.JarvisSpringSocialConfigurer;

/**
 * APP 环境下，如果用户未注册，重定向到社交账户信息页，并返回 401 代码。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof JarvisSpringSocialConfigurer) {
			JarvisSpringSocialConfigurer config = (JarvisSpringSocialConfigurer) bean;
			config.signupUrl(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL);
			return config;
		}
		return bean;
	}

}
