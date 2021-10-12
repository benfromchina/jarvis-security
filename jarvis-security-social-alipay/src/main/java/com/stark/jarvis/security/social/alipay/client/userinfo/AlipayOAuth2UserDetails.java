package com.stark.jarvis.security.social.alipay.client.userinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝用户信息。
 * @author Ben
 * @since 1.0.1
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlipayOAuth2UserDetails extends DefaultOAuth2UserDetails {
	
	private static final long serialVersionUID = -2546748094849477968L;

	/** 支付宝用户id */
	@JsonProperty("user_id")
	private String userId;

	/** 用户头像 */
	private String avatar;
	
	/** 用户昵称 */
	@JsonProperty("nick_name")
	private String nickname;
	
	/** 省份 */
	private String province;
	
	/** 城市 */
	private String city;
	
	/**
	 * 用户性别
	 * <ul>
	 * <li>M为男性</li>
	 * <li>F为女性</li>
	 * </ul>
	 */
	private String gender;
	
}
