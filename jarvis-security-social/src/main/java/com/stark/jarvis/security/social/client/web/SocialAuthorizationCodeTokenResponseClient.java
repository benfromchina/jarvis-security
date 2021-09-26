package com.stark.jarvis.security.social.client.web;

import java.util.Arrays;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class SocialAuthorizationCodeTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
	private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";

	private OAuth2AuthorizationCodeGrantRequestEntityConverterProviderManager requestEntityConverter;

	private OAuth2AccessTokenResponseConverterProviderManager responseConverterManager;

	public SocialAuthorizationCodeTokenResponseClient(OAuth2AuthorizationCodeGrantRequestEntityConverterProviderManager requestEntityConverter,
			OAuth2AccessTokenResponseConverterProviderManager responseConverterManager) {
		this.requestEntityConverter = requestEntityConverter;
		this.responseConverterManager = responseConverterManager;
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");

		RequestEntity<?> request = this.requestEntityConverter.convert(authorizationCodeGrantRequest);
		
		ResponseEntity<OAuth2AccessTokenResponse> response;
		RestOperations restOperations = createRestOperations(authorizationCodeGrantRequest.getClientRegistration());
		try {
			response = restOperations.exchange(request, OAuth2AccessTokenResponse.class);
		} catch (RestClientException ex) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
					"An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: " + ex.getMessage(), null);
			throw new OAuth2AuthorizationException(oauth2Error, ex);
		}

		OAuth2AccessTokenResponse tokenResponse = response.getBody();

		if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
			// As per spec, in Section 5.1 Successful Access Token Response
			// https://tools.ietf.org/html/rfc6749#section-5.1
			// If AccessTokenResponse.scope is empty, then default to the scope
			// originally requested by the client in the Token Request
			tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
					.scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes())
					.build();
		}

		return tokenResponse;
	}

	private RestOperations createRestOperations(ClientRegistration clientRegistration) {
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter(clientRegistration, responseConverterManager)));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		return restTemplate;
		
	}
	
}
