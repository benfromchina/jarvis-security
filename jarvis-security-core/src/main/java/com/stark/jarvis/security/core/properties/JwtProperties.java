package com.stark.jarvis.security.core.properties;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * jwt 配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Data
public class JwtProperties {
	
	/**
	 * RSA 秘钥对文件名
	 */
	private String keyPairFile = "keystore.jks";
	
	/**
	 * RSA 秘钥密码
	 */
	private String keyStorePassword = "jarvis";
	
	/**
	 * RSA 秘钥对别名
	 */
	private String keyPairAlias = "jarvis";
	
	/**
	 * 附加信息
	 */
	private Map<String, Object> additionalInformation = new HashMap<>();

}
