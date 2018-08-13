package com.stark.jarvis.security.core.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;

/**
 * 默认的 UserDetailsService 接口实现。
 * <p>不做任何业务处理，只在控制台打印日志，并抛出异常，提醒业务系统配置 UserDetailsService 接口的实现。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class DefaultUserDetailsService implements JarvisUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.error("请配置 SocialUserDetailsService 接口的实现！");
		throw new UsernameNotFoundException("username=" + username);
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.error("请配置 UserDetailsService 接口的实现！");
		throw new UsernameNotFoundException("userId=" + userId);
	}

	@Override
	public SocialUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
		logger.error("请配置 JarvisUserDetailsService 接口的实现！");
		throw new UsernameNotFoundException("phoneNumber=" + phoneNumber);
	}

}
