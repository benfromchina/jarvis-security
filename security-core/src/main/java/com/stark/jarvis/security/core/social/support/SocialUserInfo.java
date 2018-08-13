package com.stark.jarvis.security.core.social.support;

import java.io.Serializable;

/**
 * 社交用户信息。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class SocialUserInfo implements Serializable {
	
	private static final long serialVersionUID = 5245627812643723900L;

	private String providerId;
	
	private String providerUserId;
	
	private String nickname;
	
	private String imageUrl;

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "SocialUserInfo [providerId=" + providerId + ", providerUserId=" + providerUserId + ", nickname="
				+ nickname + ", imageUrl=" + imageUrl + "]";
	}
	
}
