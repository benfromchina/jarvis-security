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
	 * @param request
	 * @param code
	 * @param validateCodeType
	 */
	void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);
	
	/**
	 * 获取验证码
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);
	
	/**
	 * 移除验证码
	 * @param request
	 * @param codeType
	 */
	void remove(ServletWebRequest request, ValidateCodeType codeType);

}
