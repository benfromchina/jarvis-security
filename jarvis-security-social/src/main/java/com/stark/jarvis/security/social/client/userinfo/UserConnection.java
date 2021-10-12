package com.stark.jarvis.security.social.client.userinfo;

import java.io.Serializable;

/**
 * 用户第三方登录信息。
 * @author Ben
 * @since 1.0.0
 * @version <table border="1">
 * <thead>
 * <tr><th>版本号</th><th>修改日期</th><th>修改内容</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>1.0.1</td><td>2021/10/12</td><td>增加获取请求令牌接口 getAccessToken 。</td></tr>
 * <tr><td>1.0.0</td><td>2021/9/26</td><td>基本功能完成</td></tr>
 * </tbody>
 * </table>
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
	 * 获取请求令牌。
	 * @return 请求令牌。
	 */
	String getAccessToken();
	
	/**
	 * 设置请求令牌。
	 * @param accessToken 请求令牌。
	 * @return 自定义返回值。
	 */
	Object setAccessToken(String accessToken);
	
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
