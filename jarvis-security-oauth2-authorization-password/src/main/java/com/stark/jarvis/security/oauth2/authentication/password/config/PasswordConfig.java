package com.stark.jarvis.security.oauth2.authentication.password.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 密码登录配置项
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2026/11/19
 */
@Configuration
@EnableConfigurationProperties(PasswordProperties.class)
public class PasswordConfig {

}
