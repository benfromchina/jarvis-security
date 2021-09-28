package com.stark.jarvis.security.social.alipay.client.web;

import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.client.web.OAuth2AuthorizationCodeParameterNameProvider;

@Component
public class AlipayAuthorizationCodeParameterNameProvider implements OAuth2AuthorizationCodeParameterNameProvider {

	@Override
	public boolean supports(String registrationId) {
		return Constants.REGISTRATION_ID.equals(registrationId);
	}

	@Override
	public String getCodeParameterName() {
		return Constants.CODE_PARAMETER_NAME;
	}

}
