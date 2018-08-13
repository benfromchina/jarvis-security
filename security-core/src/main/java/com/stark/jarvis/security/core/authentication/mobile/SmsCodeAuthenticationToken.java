package com.stark.jarvis.security.core.authentication.mobile;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * 短信登录票据。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	// ~ Instance fields
	// ================================================================================================

	private final Object phoneNumber;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * 构造一个 {@link SmsCodeAuthenticationToken} 对象。
	 * @param phoneNumber 手机号码。
	 */
	public SmsCodeAuthenticationToken(String phoneNumber) {
		super(null);
		this.phoneNumber = phoneNumber;
		setAuthenticated(false);
	}

	/**
	 * 构造一个 {@link SmsCodeAuthenticationToken} 对象。
	 * @param phoneNumber 手机号码。
	 * @param authorities 权限集合。
	 */
	public SmsCodeAuthenticationToken(Object phoneNumber,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.phoneNumber = phoneNumber;
		super.setAuthenticated(true); // must use super, as we override
	}

	// ~ Methods
	// ========================================================================================================

	public Object getCredentials() {
		return null;
	}

	public Object getPrincipal() {
		return this.phoneNumber;
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
