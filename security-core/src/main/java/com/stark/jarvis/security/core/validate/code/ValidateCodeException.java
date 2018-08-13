package com.stark.jarvis.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码相关异常。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class ValidateCodeException extends AuthenticationException {

	private static final long serialVersionUID = -7285211528095468156L;

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
