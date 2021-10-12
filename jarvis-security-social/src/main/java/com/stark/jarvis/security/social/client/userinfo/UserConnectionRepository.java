package com.stark.jarvis.security.social.client.userinfo;

/**
 * 第三方登录表单存储器。
 * <p>扩展该接口，可实现自己的持久化逻辑。
 * @author Ben
 * @since 1.0.0
 * @version <table border="1">
 * <thead>
 * <tr><th>版本号</th><th>修改日期</th><th>修改内容</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>1.0.1</td><td>2021/4/29</td><td>支持自定义 {@link OAuth2UserDetails} 和 {@link UserConnection} 类型</td></tr>
 * <tr><td>1.0.0</td><td>2021/9/26</td><td>基本功能完成</td></tr>
 * </tbody>
 * </table>
 */
public interface UserConnectionRepository<U extends OAuth2UserDetails, C extends UserConnection> {
	
	/**
	 * 保存表单。
	 * @param form 第三方登录表单。
	 * @return 保存后的表单。
	 */
	UserConnectionForm<U, C> saveForm(UserConnectionForm<? extends OAuth2UserDetails, ? extends UserConnection> form);

}
