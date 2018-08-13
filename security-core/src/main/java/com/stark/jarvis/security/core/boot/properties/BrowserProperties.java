package com.stark.jarvis.security.core.boot.properties;

/**
 * 浏览器端配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class BrowserProperties {
	
	private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE;
	
	private String logoutUrl = SecurityConstants.DEFAULT_LOGOUT_URL;
	
	private String registerPage = SecurityConstants.DEFAULT_REGISTER_PAGE;
	
	private int rememberMeSeconds = 3600;
	
	private SessionProperties session = new SessionProperties();

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getRegisterPage() {
		return registerPage;
	}

	public void setRegisterPage(String registerPage) {
		this.registerPage = registerPage;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public SessionProperties getSession() {
		return session;
	}

	public void setSession(SessionProperties session) {
		this.session = session;
	}
	
}
