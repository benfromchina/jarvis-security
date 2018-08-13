package com.stark.jarvis.security.core.boot.properties;

/**
 * 短信验证码配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class SmsCodeProperties {
	
	/**
	 * 验证码长度
	 */
	private int length = 6;
	
	/**
	 * 过期时间，单位秒
	 */
	private int expireIn = 60;
	
	/**
	 * 要拦截的 url ，多个 url 用逗号隔开，ant pattern
	 */
	private String url;

	public int getLength() {
		return length;
	}
	
	public void setLength(int lenght) {
		this.length = lenght;
	}
	
	public int getExpireIn() {
		return expireIn;
	}
	
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

}
