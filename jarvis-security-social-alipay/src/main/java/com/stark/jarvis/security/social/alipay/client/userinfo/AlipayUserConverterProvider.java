package com.stark.jarvis.security.social.alipay.client.userinfo;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;
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
public class AlipayUserConverterProvider implements OAuth2UserConverterProvider {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://opendocs.alipay.com/open/284/web#%E7%AC%AC%E5%9B%9B%E6%AD%A5%EF%BC%9A%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">第四步：获取用户信息</a>
	 */
	@Override
	public UserConnectionForm convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		String userId = MapUtils.getString(userAttributes, "user_id");		// 支付宝用户id，不为空
		String avatar = MapUtils.getString(userAttributes, "avatar");		// 用户头像，可能为空
		String nickname = MapUtils.getString(userAttributes, "nick_name");	// 用户昵称，可能为空
//		String province = MapUtils.getString(userAttributes, "province");	// 省份，可能为空
//		String city = MapUtils.getString(userAttributes, "city");			// 城市，可能为空
//		String gender = MapUtils.getString(userAttributes, "gender");		// 用户性别，可能为空，M为男性，F为女性
		
		DefaultOAuth2UserDetails user = new DefaultOAuth2UserDetails()
				.setName(nickname);
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), userId)
				.setDisplayName(nickname)
				.setImageUrl(avatar);
		return new UserConnectionForm(user, userConnection);
	}

}
