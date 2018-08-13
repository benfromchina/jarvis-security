package com.stark.jarvis.security.core.validate.code;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码处理器的管理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class ValidateCodeProcessorHolder {

	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * 查找指定类型的验证码处理器。
	 * @param type 验证码类型。
	 * @return 验证码处理器。
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * 查找指定类型的验证码处理器。
	 * @param type 验证码类型。
	 * @return 验证码处理器。
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器 " + name + " 不存在");
		}
		return processor;
	}

}
