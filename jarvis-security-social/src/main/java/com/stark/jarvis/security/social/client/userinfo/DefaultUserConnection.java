package com.stark.jarvis.security.social.client.userinfo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 默认的第三方登录信息实现。
 * @author Ben
 * @version <table border="1">
 * <thead>
 * <tr><th>版本号</th><th>修改日期</th><th>修改内容</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>1.0.1</td><td>2021/10/12</td><td>增加请求令牌字段 accessToken</td></tr>
 * <tr><td>1.0.0</td><td>2021/9/26</td><td>基本功能完成</td></tr>
 * </tbody>
 * </table>
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
	 * 请求令牌
	 */
	private String accessToken;
	
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
