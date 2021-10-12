package com.stark.jarvis.security.social.qq.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.social.client.userinfo.DefaultUserConnection;
import com.stark.jarvis.security.social.client.userinfo.OAuth2UserConverterProvider;
import com.stark.jarvis.security.social.client.userinfo.UserConnectionForm;
import com.stark.jarvis.security.social.qq.properties.Constants;

/**
 * QQ用户转换器。
 * <p>将获取用户请求的响应参数集合转为第三方登录表单。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://wiki.connect.qq.com/get_user_info">get_user_info</a>
 */
@Component
public class QQUserConverterProvider implements OAuth2UserConverterProvider<QQOAuth2UserDetails, DefaultUserConnection> {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://wiki.connect.qq.com/get_user_info">get_user_info</a>
	 */
	@Override
	public UserConnectionForm<QQOAuth2UserDetails, DefaultUserConnection> convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json;
		try {
			json = objectMapper.writeValueAsString(userAttributes);
		} catch (Exception e) {
			throw new RuntimeException("Serialize userAttributes error", e);
		}
		QQOAuth2UserDetails user;
		try {
			user = objectMapper.readValue(json, QQOAuth2UserDetails.class);
		} catch (Exception e) {
			throw new RuntimeException("Deserialize userAttributes error: userAttributes=" + json, e);
		}
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), user.getOpenid())
				.setDisplayName(user.getNickname())
				.setImageUrl(user.getFigureurlQq1());
		return new UserConnectionForm<>(user, userConnection);
	}

}
