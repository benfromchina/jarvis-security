package com.stark.jarvis.security.social.client.userinfo;

import java.io.Serializable;

/**
 * 用户第三方登录信息。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface UserConnection extends Serializable {

	/**
	 * 获取服务提供商标识。
	 * @return 服务提供商标识。
	 */
	String getProviderId();
	
	/**
	 * 获取服务提供商用户标识。
	 * @return 服务提供商用户标识。
	 */
	String getProviderUserId();
	
	/**
	 * 获取显示名称。
	 * @return 显示名称。
	 */
	String getDisplayName();
	
	/**
	 * 获取个人主页链接地址。
	 * @return 个人主页链接地址。
	 */
	String getProfileUrl();
	
	/**
	 * 获取照片链接地址。
	 * @return 照片链接地址。
	 */
	String getImageUrl();
	
}
