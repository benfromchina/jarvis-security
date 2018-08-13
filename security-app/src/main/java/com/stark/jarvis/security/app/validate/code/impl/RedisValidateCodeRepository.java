package com.stark.jarvis.security.app.validate.code.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.validate.code.ValidateCode;
import com.stark.jarvis.security.core.validate.code.ValidateCodeException;
import com.stark.jarvis.security.core.validate.code.ValidateCodeRepository;
import com.stark.jarvis.security.core.validate.code.ValidateCodeType;

/**
 * 基于 redis 的验证码存取器，避免由于没有 session 导致无法存取验证码的问题。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
		redisTemplate.opsForValue().set(buildKey(request, type), code, securityProperties.getValidateCode().getExpiresIn(), TimeUnit.MINUTES);
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
		Object value = redisTemplate.opsForValue().get(buildKey(request, type));
		if (value == null) {
			return null;
		}
		return (ValidateCode) value;
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType type) {
		redisTemplate.delete(buildKey(request, type));
	}

	private String buildKey(ServletWebRequest request, ValidateCodeType type) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new ValidateCodeException("请在请求头中携带 deviceId 参数");
		}
		return "jarvis:security:code:" + type.toString().toLowerCase() + ":" + deviceId;
	}

}
