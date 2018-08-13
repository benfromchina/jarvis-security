package com.stark.jarvis.security.core.authentication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证相关的扩展点配置。
 * <p>配置在这里的 bean ，业务系统可以通过声明同类型或同名的 bean 来覆盖安全模块默认的配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class AuthenticationBeanConfig {
	
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	@Description("默认密码处理器")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	@Description("默认的 UserDetailsService 接口实现")
	public UserDetailsService userDetailsService() {
		return new DefaultUserDetailsService();
	}
	
}
