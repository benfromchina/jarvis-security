package com.stark.jarvis.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ValidateCodeGenerator {

	/**
	 * 生成验证码。
	 * @param request HTTP 请求对象。
	 * @return 验证码。
	 */
	ValidateCode generate(ServletWebRequest request);
	
}
