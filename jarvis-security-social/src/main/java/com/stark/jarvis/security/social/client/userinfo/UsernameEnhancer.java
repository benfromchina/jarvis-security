package com.stark.jarvis.security.social.client.userinfo;

/**
 * 用户名增强器。
 * <p>如果 {@link DefaultOAuth2UserDetails} 的用户名为空，默认以 "oauth2:{providerId}:{providerUserId}" 格式生成用户名。
 * 扩展该接口，可对生成的用户名进行进一步的操作。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface UsernameEnhancer {
	
	/**
	 * 增强用户名。
	 * @param username 用户名。
	 * @return 增强后的用户名。
	 * @throws Exception
	 */
	String enhance(String username) throws Exception;

}
