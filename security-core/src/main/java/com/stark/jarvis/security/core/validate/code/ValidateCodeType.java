package com.stark.jarvis.security.core.validate.code;

import com.stark.jarvis.security.core.boot.properties.SecurityConstants;

/**
 * 枚举验证码类型。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public enum ValidateCodeType {
	
	/**
	 * 图片验证码
	 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
		}
	},
	/**
	 * 短信验证码
	 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
		}
	};

	/**
	 * 校验时从请求中获取参数的名称
	 * @return 参数名称
	 */
	public abstract String getParamNameOnValidate();

}
