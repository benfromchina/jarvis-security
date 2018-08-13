package com.stark.jarvis.security.app.authentication.openid;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * openId 认证票据。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class OpenIdAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	// ~ Instance fields
	// ================================================================================================

	private final Object openId;
	private String providerId;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * 构造一个 {@link OpenIdAuthenticationToken} 对象。
	 * @param openId 第三方应用的用户标识。
	 * @param providerId 第三方应用标识。
	 */
	public OpenIdAuthenticationToken(String openId, String providerId) {
		super(null);
		this.openId = openId;
		this.providerId = providerId;
		setAuthenticated(false);
	}

	/**
	 * 构造一个 {@link OpenIdAuthenticationToken} 对象。
	 * @param openId 第三方应用的用户标识。
	 * @param authorities 权限集合。
	 */
	public OpenIdAuthenticationToken(Object openId,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.openId = openId;
		super.setAuthenticated(true); // must use super, as we override
	}

	// ~ Methods
	// ========================================================================================================

	public Object getCredentials() {
		return null;
	}

	public Object getPrincipal() {
		return openId;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}
	
}
