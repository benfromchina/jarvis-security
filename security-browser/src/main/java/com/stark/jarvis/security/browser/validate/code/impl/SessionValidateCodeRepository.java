package com.stark.jarvis.security.browser.validate.code.impl;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.core.validate.code.ValidateCode;
import com.stark.jarvis.security.core.validate.code.ValidateCodeRepository;
import com.stark.jarvis.security.core.validate.code.ValidateCodeType;

/**
 * 基于 session 的验证码存取器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

	/**
	 * 验证码放入 session 时的前缀
	 */
	String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
	
	/**
	 * 操作 session 的工具类
	 */
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
		sessionStrategy.setAttribute(request, getSessionKey(request, validateCodeType), code);
	}
	
	/**
	 * 构建验证码放入 session 时的 key 。
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	private String getSessionKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return SESSION_KEY_PREFIX + validateCodeType.toString().toUpperCase();
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return (ValidateCode) sessionStrategy.getAttribute(request, getSessionKey(request, validateCodeType));
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType codeType) {
		sessionStrategy.removeAttribute(request, getSessionKey(request, codeType));
	}

}
