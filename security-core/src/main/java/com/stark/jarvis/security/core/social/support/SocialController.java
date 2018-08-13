package com.stark.jarvis.security.core.social.support;

import org.springframework.social.connect.Connection;

import com.stark.jarvis.security.core.social.support.SocialUserInfo;

/**
 * 抽象的社交登录控制器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class SocialController {

	/**
	 * 根据 {@link Connection} 信息构建 {@link SocialUserInfo} 对象。
	 * @param connection {@link Connection} 接口的实现。
	 * @return 社交用户信息。
	 */
	protected SocialUserInfo buildSocialUserInfo(Connection<?> connection) {
		SocialUserInfo userInfo = new SocialUserInfo();
		userInfo.setProviderId(connection.getKey().getProviderId());
		userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		userInfo.setNickname(connection.getDisplayName());
		userInfo.setImageUrl(connection.getImageUrl());
		return userInfo;
	}
	
}
