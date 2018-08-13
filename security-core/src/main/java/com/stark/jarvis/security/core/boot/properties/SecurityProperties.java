package com.stark.jarvis.security.core.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jarvis 安全配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "jarvis.security")
public class SecurityProperties {
	
	/**
	 * 浏览器端配置项
	 */
	private BrowserProperties browser = new BrowserProperties();
	
	/**
	 * 验证码配置项
	 */
	private ValidateCodeProperties validateCode = new ValidateCodeProperties();
	
	/**
	 * Spring OAuth2 配置项
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();
	
	/**
	 * <ul>
	 * <li>不做验证的 url ,多组用 "," 隔开；</li>
	 * <li>method(url) 表示拦截指定请求方式的 url ，如 post(/user) 表示只拦截 post 方式的 /user 请求。</li>
	 * </ul>
	 */
	private String permitAllUrl;
	
	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ValidateCodeProperties getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(ValidateCodeProperties validateCode) {
		this.validateCode = validateCode;
	}

	public OAuth2Properties getOauth2() {
		return oauth2;
	}

	public void setOauth2(OAuth2Properties oauth2) {
		this.oauth2 = oauth2;
	}

	public String getPermitAllUrl() {
		return permitAllUrl;
	}

	public void setPermitAllUrl(String permitAllUrl) {
		this.permitAllUrl = permitAllUrl;
	}
	
}
