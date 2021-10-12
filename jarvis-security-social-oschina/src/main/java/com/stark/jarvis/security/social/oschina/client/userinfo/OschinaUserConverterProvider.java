package com.stark.jarvis.security.social.oschina.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OschinaUserConverterProvider implements OAuth2UserConverterProvider<OschinaOAuth2UserDetails, DefaultUserConnection> {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://www.oschina.net/openapi/docs/openapi_user">openapi_user</a>
	 */
	@Override
	public UserConnectionForm<OschinaOAuth2UserDetails, DefaultUserConnection> convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json;
		try {
			json = objectMapper.writeValueAsString(userAttributes);
		} catch (Exception e) {
			throw new RuntimeException("Serialize userAttributes error", e);
		}
		OschinaOAuth2UserDetails user;
		try {
			user = objectMapper.readValue(json, OschinaOAuth2UserDetails.class);
		} catch (Exception e) {
			throw new RuntimeException("Deserialize userAttributes error", e);
		}
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), user.getId())
				.setDisplayName(user.getName())
				.setImageUrl(user.getAvatar())
				.setProfileUrl(user.getUrl());
		return new UserConnectionForm<>(user, userConnection);
	}

}
