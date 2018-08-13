package com.stark.jarvis.security.browser.boot.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.stark.jarvis.security.core.authentication.FormAuthenticationConfig;
import com.stark.jarvis.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.stark.jarvis.security.core.authorize.AuthorizeConfigManager;
import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.social.support.JarvisSpringSocialConfigurer;
import com.stark.jarvis.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * 浏览器端安全配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	@Autowired
	private JarvisSpringSocialConfigurer jarvisSocialSecurityConfig;
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private UserDetailsService userDetailsSevice;
	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;
	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		formAuthenticationConfig.configure(http);
		
		http.apply(validateCodeSecurityConfig)
				.and()
			.apply(smsCodeAuthenticationSecurityConfig)
				.and()
			.apply(jarvisSocialSecurityConfig)
				.and()
			//记住我配置，如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
			.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsSevice)
				.and()
			.sessionManagement()
				.invalidSessionStrategy(invalidSessionStrategy)
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
				.expiredSessionStrategy(sessionInformationExpiredStrategy)
				.and()
				.and()
			.logout()
				.logoutUrl(securityProperties.getBrowser().getLogoutUrl())
				.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("JSESSIONID")
				.and()
			.csrf().disable();
		
		authorizeConfigManager.config(http.authorizeRequests());
		
	}

	/**
	 * 记住我功能的 token 存取器。
	 * @return 记住我功能的 token 存取器。
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
}
