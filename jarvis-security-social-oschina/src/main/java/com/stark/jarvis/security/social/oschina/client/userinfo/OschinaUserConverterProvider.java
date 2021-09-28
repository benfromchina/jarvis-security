package com.stark.jarvis.security.social.oschina.client.userinfo;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;
import com.stark.jarvis.security.social.client.userinfo.DefaultUserConnection;
import com.stark.jarvis.security.social.client.userinfo.OAuth2UserConverterProvider;
import com.stark.jarvis.security.social.client.userinfo.UserConnectionForm;
import com.stark.jarvis.security.social.oschina.properties.Constants;

/**
 * 开源中国用户转换器。
 * <p>将获取用户请求的响应参数集合转为第三方登录表单。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://www.oschina.net/openapi/docs/openapi_user">openapi_user</a>
 */
@Component
public class OschinaUserConverterProvider implements OAuth2UserConverterProvider {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://www.oschina.net/openapi/docs/openapi_user">openapi_user</a>
	 */
	@Override
	public UserConnectionForm convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		String id = MapUtils.getString(userAttributes, "id");
//		String email = MapUtils.getString(userAttributes, "email");
		String name = MapUtils.getString(userAttributes, "name");
//		String gender = MapUtils.getString(userAttributes, "gender");		// 性别: male、female
		String avatar = MapUtils.getString(userAttributes, "avatar");		// 头像
//		String location = MapUtils.getString(userAttributes, "location");	// 地点，如 "广东 深圳"
		String url = MapUtils.getString(userAttributes, "url");				// 主页
		
		DefaultOAuth2UserDetails user = new DefaultOAuth2UserDetails()
				.setName(name);
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), id)
				.setDisplayName(name)
				.setImageUrl(avatar)
				.setProfileUrl(url);
		return new UserConnectionForm(user, userConnection);
	}

}
