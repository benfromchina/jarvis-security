package com.stark.jarvis.security.social.alipay.client.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.alipay.security.AlipayPublicKeySupplier;
import com.stark.jarvis.security.social.alipay.security.PrivateKeySupplier;
import com.stark.jarvis.security.social.alipay.util.AlipayUtils;
import com.stark.jarvis.security.social.client.endpoint.OAuth2AccessTokenResponseClientProvider;

@Component
public class AlipayAccessTokenResponseClientProvider implements OAuth2AccessTokenResponseClientProvider {
	
	@Autowired
	private PrivateKeySupplier privateKeySupplier;
	@Autowired
	private AlipayPublicKeySupplier alipayPublicKeySupplier;

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equals(clientRegistration.getRegistrationId());
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		Map<String, Object> additionalParameters = authorizationGrantRequest.getAuthorizationExchange().getAuthorizationRequest().getAdditionalParameters();
		String grantType = authorizationGrantRequest.getGrantType().getValue();
		String code = authorizationGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode();
		
		ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
		String serverUrl = clientRegistration.getProviderDetails().getTokenUri();
		String appId = clientRegistration.getClientId();
		AlipayClient alipayClient = AlipayUtils.createAlipayClient(serverUrl, appId, privateKeySupplier, "json", Constants.CHARSET, alipayPublicKeySupplier, "RSA2");
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(code);
		request.setGrantType(grantType);
		AlipaySystemOauthTokenResponse response = AlipayUtils.executeRequest(alipayClient, request);
		String accessToken = response.getAccessToken();
		String userId = response.getUserId();
		String refreshToken = response.getRefreshToken();
		String expiresIn = response.getExpiresIn();
		String reExpiresIn = response.getReExpiresIn();
		
		additionalParameters = new HashMap<>();
		additionalParameters.put("reExpiresIn", Long.parseLong(reExpiresIn));
		additionalParameters.put("userId", userId);
		
		OAuth2AccessTokenResponse auth2AccessTokenResponse = OAuth2AccessTokenResponse.withToken(accessToken)
				.expiresIn(Long.parseLong(expiresIn))
				.refreshToken(refreshToken)
				.tokenType(TokenType.BEARER)
				.additionalParameters(additionalParameters)
				.build();
		return auth2AccessTokenResponse;
	}

}
