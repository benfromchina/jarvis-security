package com.stark.jarvis.security.social.client.userinfo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户第三方登录信息表单。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionForm implements Serializable {
	
	private static final long serialVersionUID = 5663283007658261761L;

	/***
	 * 用户信息。
	 */
	private OAuth2UserDetails user;
	
	/**
	 * 第三方登录信息。
	 */
	private UserConnection userConnection;

}
