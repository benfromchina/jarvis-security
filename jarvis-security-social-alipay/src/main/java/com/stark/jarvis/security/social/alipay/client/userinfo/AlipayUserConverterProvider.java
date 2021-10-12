package com.stark.jarvis.security.social.alipay.client.userinfo;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.client.userinfo.DefaultUserConnection;
import com.stark.jarvis.security.social.client.userinfo.OAuth2UserConverterProvider;
import com.stark.jarvis.security.social.client.userinfo.UserConnectionForm;

/**
 * 支付宝用户转换器。
 * <p>将获取用户请求的响应参数集合转为第三方登录表单。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://opendocs.alipay.com/open/284/web#%E7%AC%AC%E5%9B%9B%E6%AD%A5%EF%BC%9A%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">第四步：获取用户信息</a>
 */
@Component
public class AlipayUserConverterProvider implements OAuth2UserConverterProvider<AlipayOAuth2UserDetails, DefaultUserConnection> {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://opendocs.alipay.com/open/284/web#%E7%AC%AC%E5%9B%9B%E6%AD%A5%EF%BC%9A%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">第四步：获取用户信息</a>
	 */
	@Override
	public UserConnectionForm<AlipayOAuth2UserDetails, DefaultUserConnection> convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json;
		try {
			json = objectMapper.writeValueAsString(userAttributes);
		} catch (Exception e) {
			throw new RuntimeException("Serialize userAttributes error", e);
		}
		AlipayOAuth2UserDetails user;
		try {
			user = objectMapper.readValue(json, AlipayOAuth2UserDetails.class);
		} catch (Exception e) {
			throw new RuntimeException("Deserialize userAttributes error", e);
		}
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), user.getUserId())
				.setDisplayName(user.getNickname())
				.setImageUrl(user.getAvatar());
		return new UserConnectionForm<>(user, userConnection);
	}

}
