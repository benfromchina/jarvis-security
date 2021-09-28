package com.stark.jarvis.security.social.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.stark.jarvis.security.social.client.ClientRegistrationBuilderProviderManager;
import com.stark.jarvis.security.social.client.OAuth2ClientPropertiesRegistrationAdapter;

/**
 * 第三方登录配置。
 * @author Ben
 * @see {@link org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientRegistrationRepositoryConfiguration}
 */
@Configuration
@ComponentScan(basePackages = "com.stark.jarvis.security.social",
	excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = Configuration.class)
	}
)
public class SocialConfig {
	
	@Autowired
	private ClientRegistrationBuilderProviderManager clientRegistrationBuilder;
	
	/**
	 * 支持 QQ、支付宝等。
	 * @see org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientRegistrationRepositoryConfiguration
	 */
	@Bean
	public InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oauth2ClientProperties) {
		List<ClientRegistration> registrations = new ArrayList<>(
				OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauth2ClientProperties, clientRegistrationBuilder).values());
		return new InMemoryClientRegistrationRepository(registrations);
	}

}
