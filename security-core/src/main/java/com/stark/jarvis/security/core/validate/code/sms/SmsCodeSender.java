package com.stark.jarvis.security.core.validate.code.sms;


/**
 * 短信验证码发送器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface SmsCodeSender {
	
	/**
	 * 发送短信验证码。
	 * @param mobile 手机号。
	 * @param code 验证码。
	 */
	void send(String mobile, String code);

}
