package com.stark.jarvis.security.core.boot.properties;

/**
 * session 配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class SessionProperties {
	
	/**
	 * 同一个用户在系统中的最大 session 数，默认 1
	 */
	private int maximumSessions = 1;
	
	/**
	 * 同一个用户达到最大 session 数时是否阻止新的登录请求，默认为 false 不阻止，新的登录会将老的登录踢除
	 */
	private boolean maxSessionsPreventsLogin;
	
	/**
	 * session 失效时跳转的地址
	 */
	private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;

	public int getMaximumSessions() {
		return maximumSessions;
	}

	public void setMaximumSessions(int maximumSessions) {
		this.maximumSessions = maximumSessions;
	}

	public boolean isMaxSessionsPreventsLogin() {
		return maxSessionsPreventsLogin;
	}

	public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
		this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
	}

	public String getSessionInvalidUrl() {
		return sessionInvalidUrl;
	}

	public void setSessionInvalidUrl(String sessionInvalidUrl) {
		this.sessionInvalidUrl = sessionInvalidUrl;
	}
	
}
