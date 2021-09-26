package com.stark.jarvis.security.social.client.userinfo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * 扩展 {@link OAuth2User} ，建立本地用户和第三方登录账户的绑定关系。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2UserDetails extends OAuth2User, UserDetails {
	
}
