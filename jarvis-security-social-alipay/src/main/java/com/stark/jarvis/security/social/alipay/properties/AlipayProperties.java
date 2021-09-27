package com.stark.jarvis.security.social.alipay.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.alipay")
@Data
public class AlipayProperties {
	
	/** 私钥文件路径 */
	private String privateKeyPath;
	
	/** 支付宝公钥文件路径 */
	private String alipayPublicKeyPath;
	
}
