package com.stark.jarvis.security.social.alipay.client.userinfo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.alipay.security.AlipayPublicKeySupplier;
import com.stark.jarvis.security.social.alipay.security.PrivateKeySupplier;
import com.stark.jarvis.security.social.alipay.util.AlipayUtils;
import com.stark.jarvis.security.social.client.userinfo.OAuth2UserInfoResponseClientProvider;

@Component
public class AlipayUserInfoResponseClientProvider implements OAuth2UserInfoResponseClientProvider {
	
	@Autowired
	private PrivateKeySupplier privateKeySupplier;
	@Autowired
	private AlipayPublicKeySupplier alipayPublicKeySupplier;

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUserInfoResponse(OAuth2UserRequest userRequest) {
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		String appId = clientRegistration.getClientId();
		String serverUrl = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri();
		AlipayClient alipayClient = AlipayUtils.createAlipayClient(serverUrl, appId, privateKeySupplier, "json", Constants.CHARSET, alipayPublicKeySupplier, "RSA2");
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		String authToken = userRequest.getAccessToken().getTokenValue();
		AlipayUserInfoShareResponse response = AlipayUtils.executeRequest(alipayClient, request, authToken);
		String body = response.getBody();
		
		Map<String, Object> userAttributes = null;
		try {
			Map<String, Object> map = new ObjectMapper().readValue(body, Map.class);
			userAttributes = (Map<String, Object>) map.get("alipay_user_info_share_response");
		} catch (Exception e) {
			OAuth2Error oauth2Error = new OAuth2Error("deserialize_user_attributes_error",
					"An error occurred while attempting to deserialize the user attributes: " + e.getMessage(), null);
			throw new OAuth2AuthenticationException(oauth2Error, e);
		}
		userAttributes.remove("code");
		userAttributes.remove("msg");
		return userAttributes;
	}

}
