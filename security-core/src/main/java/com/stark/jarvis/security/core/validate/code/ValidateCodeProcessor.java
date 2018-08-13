package com.stark.jarvis.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ValidateCodeProcessor {

	/**
	 * 生成验证码。
	 * @param request
	 * @throws Exception
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 校验验证码
	 * @param servletWebRequest
	 */
	void validate(ServletWebRequest servletWebRequest);

}
