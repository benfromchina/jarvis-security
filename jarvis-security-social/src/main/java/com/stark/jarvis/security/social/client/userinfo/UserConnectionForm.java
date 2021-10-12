package com.stark.jarvis.security.social.client.userinfo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户第三方登录信息表单。
 * @author Ben
 * @param <U> 用户类型。
 * @param <C> 第三方登录信息类型。
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionForm<U extends OAuth2UserDetails, C extends UserConnection> implements Serializable {
	
	private static final long serialVersionUID = 5663283007658261761L;

	/***
	 * 用户信息。
	 */
	private U user;
	
	/**
	 * 第三方登录信息。
	 */
	private C userConnection;

}
