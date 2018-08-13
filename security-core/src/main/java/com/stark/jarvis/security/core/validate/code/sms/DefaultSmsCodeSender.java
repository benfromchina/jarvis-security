package com.stark.jarvis.security.core.validate.code.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的短信验证码发送器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void send(String mobile, String code) {
		logger.error("请实现验证码发送器接口 SmsCodeSender ！");
	}

}
