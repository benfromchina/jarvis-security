package com.stark.jarvis.security.social.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.config.OAuth2LoginCustomizer;
import com.stark.jarvis.security.core.properties.SecurityProperties;
import com.stark.jarvis.security.social.authentication.RestfulAuthenticationSuccessHandler;
import com.stark.jarvis.security.social.client.endpoint.OAuth2AccessTokenResponseClientProviderManager;
import com.stark.jarvis.security.social.client.web.HttpCookieOAuth2AuthorizationRequestRepository;
import com.stark.jarvis.security.social.client.web.OAuth2AccessTokenResponseConverterProviderManager;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationCodeGrantRequestEntityConverterProviderManager;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationRequestEnhancerProviderManager;
import com.stark.jarvis.security.social.client.web.SocialAuthorizationCodeTokenResponseClient;
import com.stark.jarvis.security.social.client.web.SocialOAuth2AuthorizationRequestResolver;

@Component("oauth2LoginCustomizer")
public class OAuth2LoginCustomizerImpl implements OAuth2LoginCustomizer {
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	@Autowired
	private OAuth2AccessTokenResponseClientProviderManager oauth2AccessTokenResponseClientProviderManager;
	@Autowired
	private OAuth2AccessTokenResponseConverterProviderManager oauth2AccessTokenResponseConverterManager;
	@Autowired
	private OAuth2AuthorizationRequestEnhancerProviderManager oauth2AuthorizationRequestEnhancerManager;
	@Autowired
	private OAuth2AuthorizationCodeGrantRequestEntityConverterProviderManager oauth2AuthorizationCodeGrantRequestConverterManager;
	@Autowired(required = false)
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private RestfulAuthenticationSuccessHandler successHandler;
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void customize(OAuth2LoginConfigurer<HttpSecurity> http) {
		http
			.clientRegistrationRepository(clientRegistrationRepository)
			.authorizationEndpoint()
				.authorizationRequestRepository(redisTemplate != null ? new HttpSessionOAuth2AuthorizationRequestRepository() : new HttpCookieOAuth2AuthorizationRequestRepository(securityProperties.getOauth2().getAuthorizationRequestExpirySeconds()))
				.authorizationRequestResolver(new SocialOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI, oauth2AuthorizationRequestEnhancerManager))
			.and().successHandler(successHandler)
			.tokenEndpoint().accessTokenResponseClient(new SocialAuthorizationCodeTokenResponseClient(oauth2AuthorizationCodeGrantRequestConverterManager, oauth2AccessTokenResponseConverterManager, oauth2AccessTokenResponseClientProviderManager));
	}

}
