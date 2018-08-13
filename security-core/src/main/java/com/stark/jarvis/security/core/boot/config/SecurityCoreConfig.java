package com.stark.jarvis.security.core.boot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 安全核心配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ComponentScan(basePackages = "com.stark.jarvis.security")
public class SecurityCoreConfig {
	
}
