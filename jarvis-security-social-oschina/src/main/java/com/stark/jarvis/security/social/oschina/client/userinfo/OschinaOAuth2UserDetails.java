package com.stark.jarvis.security.social.oschina.client.userinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 开源用户信息。
 * @author Ben
 * @since 1.0.1
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OschinaOAuth2UserDetails extends DefaultOAuth2UserDetails {
	
	private static final long serialVersionUID = -3912467767979035999L;

	/** 用户ID */
	private String id;

	/** 用户email */
	private String email;
	
	/** 用户名 */
	private String name;
	
	/**
	 * 用户性别
	 * <ul>
	 * <li>male为男性</li>
	 * <li>female为女性</li>
	 * </ul>
	 */
	private String gender;
	
	/** 头像 */
	private String avatar;
	
	/** 地点 */
	private String location;
	
	/** 主页 */
	private String url;
	
}
