package com.stark.jarvis.security.app.support;

/**
 * APP 秘钥相关异常。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class AppSecretException extends RuntimeException {

	private static final long serialVersionUID = -1629364510827838114L;

	public AppSecretException(String msg){
		super(msg);
	}
	
}
