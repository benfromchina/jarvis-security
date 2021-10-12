package com.stark.jarvis.security.social.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.stark.jarvis.security.social.client.ClientProvider;

/**
 * 第三方登录用户转换器。
 * <p>将获取用户请求的响应参数集合转为第三方登录表单。
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
public interface OAuth2UserConverterProvider<U extends OAuth2UserDetails, C extends UserConnection> extends ClientProvider {
	
	/**
	 * 将获取用户请求的响应参数集合转为第三方登录表单。
	 * @param clientRegistration 第三方客户端。
	 * @param userAttributes 获取用户请求的响应参数。
	 * @return 第三方登录表单。
	 */
	UserConnectionForm<U, C> convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes);

}
