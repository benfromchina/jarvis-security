package com.stark.jarvis.security.core.social;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.boot.properties.SocialProperties;
import com.stark.jarvis.security.core.social.support.JarvisSpringSocialConfigurer;
import com.stark.jarvis.security.core.social.support.SocialAuthenticationFilterPostProcessor;

/**
 * 社交登录配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SocialProperties.class)
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private SocialProperties socialProperties;
	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;
	@Autowired(required = false)
	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		if (!StringUtils.isBlank(socialProperties.getTablePrefix())) {
			repository.setTablePrefix(socialProperties.getTablePrefix());
		}
		if (connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}
	
	/**
	 * 社交登录配置类，供浏览器或 app 模块引入设计登录配置用。
	 * @return
	 */
	@Bean
	public SpringSocialConfigurer jarvisSocialSecurityConfig() {
		JarvisSpringSocialConfigurer configurer = new JarvisSpringSocialConfigurer();
		configurer.signupUrl(securityProperties.getBrowser().getRegisterPage());
		configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
		return configurer;
	}

	/**
	 * 用来处理注册流程的工具类。
	 * @param connectionFactoryLocator
	 * @return
	 */
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
		return new ProviderSignInUtils(connectionFactoryLocator,
				getUsersConnectionRepository(connectionFactoryLocator)) {
		};
	}
}
