package com.stark.jarvis.security.core.boot.properties;

/**
 * 验证码配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class ValidateCodeProperties {
	
	/**
	 * 过期时间，单位分钟
	 */
	private int expiresIn = 5;
	
	/**
	 * 图片验证码配置项
	 */
	private ImageCodeProperties image = new ImageCodeProperties();
	
	/**
	 * 短信验证码配置项
	 */
	private SmsCodeProperties sms = new SmsCodeProperties();

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public ImageCodeProperties getImage() {
		return image; 
	}

	public void setImage(ImageCodeProperties image) {
		this.image = image;
	}

	public SmsCodeProperties getSms() {
		return sms;
	}

	public void setSms(SmsCodeProperties sms) {
		this.sms = sms;
	}
	
}
