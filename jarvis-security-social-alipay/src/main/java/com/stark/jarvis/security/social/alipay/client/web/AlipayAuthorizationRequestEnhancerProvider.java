package com.stark.jarvis.security.social.alipay.client.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationRequestEnhancerProvider;

@Component
public class AlipayAuthorizationRequestEnhancerProvider implements OAuth2AuthorizationRequestEnhancerProvider {

	@Override
	public boolean supports(ClientRegistration clientRegistration) {
		return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
	}

	/**
	 * @see <a href="https://opendocs.alipay.com/open/284/web#%E7%AC%AC%E4%B8%80%E6%AD%A5%EF%BC%9AURL%20%E6%8B%BC%E6%8E%A5">第一步：URL 拼接</a>
	 */
	@Override
	public OAuth2AuthorizationRequest enhance(ClientRegistration clientRegistration, OAuth2AuthorizationRequest request) {
		OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.from(request);
		
		Map<String, Object> additionalParameters = new HashMap<>(request.getAdditionalParameters());
		additionalParameters.put("app_id", clientRegistration.getClientId());
		builder.additionalParameters(additionalParameters);
		
		String scope = request.getScopes().contains("auth_base") && !request.getScopes().contains("auth_user")
				? "auth_base"
				: "auth_user";
		builder.scope(scope);
		
		return builder.build();
	}

}
