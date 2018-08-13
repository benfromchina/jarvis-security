package com.stark.jarvis.security.core.validate.code.sms;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.validate.code.ValidateCode;
import com.stark.jarvis.security.core.validate.code.ValidateCodeGenerator;

/**
 * 短信验证码生成器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(securityProperties.getValidateCode().getSms().getLength());
		return new ValidateCode(code, securityProperties.getValidateCode().getSms().getExpireIn());
	}

}
