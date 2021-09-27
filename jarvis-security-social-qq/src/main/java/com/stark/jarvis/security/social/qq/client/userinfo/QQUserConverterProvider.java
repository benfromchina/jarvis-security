package com.stark.jarvis.security.social.qq.client.userinfo;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;
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
public class QQUserConverterProvider implements OAuth2UserConverterProvider {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://wiki.connect.qq.com/get_user_info">get_user_info</a>
	 */
	@Override
	public UserConnectionForm convert(ClientRegistration clientRegistration, Map<String, Object> userAttributes) {
		String openid = MapUtils.getString(userAttributes, "openid");
//		Integer is_lost = MapUtils.getInteger(userAttributes, "is_lost");
		String nickname = MapUtils.getString(userAttributes, "nickname");				// 用户在QQ空间的昵称。
//		String gender = MapUtils.getString(userAttributes, "gender");					// 性别。 如果获取不到则默认返回"男"
//		Integer gender_type = MapUtils.getInteger(userAttributes, "gender_type");
//		String province = MapUtils.getString(userAttributes, "province");				// 省份
//		String city = MapUtils.getString(userAttributes, "city");						// 城市
//		String year = MapUtils.getString(userAttributes, "year");						// 出生年份
//		String constellation = MapUtils.getString(userAttributes, "constellation");		// 星座
//		String figureurl = MapUtils.getString(userAttributes, "figureurl");				// 大小为30×30像素的QQ空间头像URL。
//		String figureurl_1 = MapUtils.getString(userAttributes, "figureurl_1");			// 大小为50×50像素的QQ空间头像URL。
//		String figureurl_2 = MapUtils.getString(userAttributes, "figureurl_2");			// 大小为100×100像素的QQ空间头像URL。
		String figureurl_qq_1 = MapUtils.getString(userAttributes, "figureurl_qq_1");	// 大小为40×40像素的QQ头像URL。
//		String figureurl_qq_2 = MapUtils.getString(userAttributes, "figureurl_qq_1");	// 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有.
//		String figureurl_qq = MapUtils.getString(userAttributes, "figureurl_qq_1");		// 大小为640×640像素的QQ头像URL。
//		String figureurl_type = MapUtils.getString(userAttributes, "figureurl_type");
//		String is_yellow_vip = MapUtils.getString(userAttributes, "is_yellow_vip");
//		String vip = MapUtils.getString(userAttributes, "vip");
//		String yellow_vip_level = MapUtils.getString(userAttributes, "yellow_vip_level");
//		String level = MapUtils.getString(userAttributes, "level");
//		String is_yellow_year_vip = MapUtils.getString(userAttributes, "is_yellow_year_vip");	// 是否年费黄钻
		
		DefaultOAuth2UserDetails user = new DefaultOAuth2UserDetails()
				.setName(nickname);
		DefaultUserConnection userConnection = new DefaultUserConnection(clientRegistration.getRegistrationId(), openid)
				.setDisplayName(nickname)
				.setImageUrl(figureurl_qq_1);
		return new UserConnectionForm(user, userConnection);
	}

}
