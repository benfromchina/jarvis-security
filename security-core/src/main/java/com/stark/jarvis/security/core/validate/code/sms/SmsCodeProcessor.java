package com.stark.jarvis.security.core.validate.code.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.core.boot.properties.SecurityConstants;
import com.stark.jarvis.security.core.validate.code.AbstractValidateCodeProcessor;
import com.stark.jarvis.security.core.validate.code.ValidateCode;

/**
 * 短信验证码处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

	@Autowired
	private SmsCodeSender smsCodeSender;
	
	@Override
	protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
		String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
		String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
		smsCodeSender.send(mobile, validateCode.getCode());
	}

}
