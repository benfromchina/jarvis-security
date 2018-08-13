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
	 * @param request HTTP 请求对象。
	 * @throws Exception 未知异常。
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 校验验证码
	 * @param servletWebRequest spring 请求响应对象。
	 */
	void validate(ServletWebRequest servletWebRequest);

}
