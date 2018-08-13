package com.stark.jarvis.security.core.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * 对 UserDetailsService 接口 和 SocialUserDetailsService 接口的扩展。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface JarvisUserDetailsService extends UserDetailsService, SocialUserDetailsService {
	
	/**
	 * 根据手机号码查询用户。
	 * @param phoneNumber 手机号码。
	 * @return 用户详情。
	 * @throws UsernameNotFoundException 找不到用户时抛出异常。
	 */
	SocialUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException;
	
}
