package com.stark.jarvis.security.social.client.userinfo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 默认的第三方登录信息实现。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class DefaultUserConnection implements UserConnection {
	
	private static final long serialVersionUID = -6068844096369765042L;

	/**
	 * 第三方服务提供商标识
	 */
	@NonNull
	private String providerId;
	
	/**
	 * 第三方用户标识
	 */
	@NonNull
	private String providerUserId;
	
	/**
	 * 显示名称
	 */
	private String displayName;
	
	/**
	 * 个人主页链接地址
	 */
	private String profileUrl;
	
	/**
	 * 照片链接地址
	 */
	private String imageUrl;

}
