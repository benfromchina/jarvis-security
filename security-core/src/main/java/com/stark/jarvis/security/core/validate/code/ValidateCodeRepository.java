package com.stark.jarvis.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码存取器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ValidateCodeRepository {

	/**
	 * 保存验证码。
	 * @param request HTTP 请求对象。
	 * @param code 验证码。
	 * @param validateCodeType 验证码类型。
	 */
	void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);
	
	/**
	 * 获取验证码。
	 * @param request HTTP 请求对象。
	 * @param validateCodeType 验证码类型。
	 * @return 验证码。
	 */
	ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);
	
	/**
	 * 移除验证码。
	 * @param request HTTP 请求独享。
	 * @param codeType 验证码类型。
	 */
	void remove(ServletWebRequest request, ValidateCodeType codeType);

}
