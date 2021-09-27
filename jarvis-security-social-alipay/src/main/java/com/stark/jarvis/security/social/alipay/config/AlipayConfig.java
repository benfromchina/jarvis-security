package com.stark.jarvis.security.social.alipay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stark.jarvis.security.social.alipay.properties.AlipayProperties;
import com.stark.jarvis.security.social.alipay.security.AlipayPublicKeySupplier;
import com.stark.jarvis.security.social.alipay.security.FileAlipayPublicKeySupplier;
import com.stark.jarvis.security.social.alipay.security.FilePrivateKeySupplier;
import com.stark.jarvis.security.social.alipay.security.PrivateKeySupplier;

/**
 * 支付宝第三方登录配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfig {
	
	@Autowired
	private AlipayProperties alipayProperties;
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.alipay", name = "private-key-path")
	public PrivateKeySupplier privateKeySupplier() {
		return new FilePrivateKeySupplier(alipayProperties.getPrivateKeyPath());
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.alipay", name = "alipay-public-key-path")
	public AlipayPublicKeySupplier alipayPublicKeySupplier() {
		return new FileAlipayPublicKeySupplier(alipayProperties.getAlipayPublicKeyPath());
	}

}
