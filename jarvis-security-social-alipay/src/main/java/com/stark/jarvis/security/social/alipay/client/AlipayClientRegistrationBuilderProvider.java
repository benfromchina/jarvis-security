package com.stark.jarvis.security.social.alipay.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.client.ClientRegistrationBuilderProvider;

@Component
public class AlipayClientRegistrationBuilderProvider implements ClientRegistrationBuilderProvider {

	@Override
	public boolean supports(String registrationId) {
		return Constants.REGISTRATION_ID.equals(registrationId);
	}

	@Override
	public Builder getBuilder() {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(Constants.REGISTRATION_ID)
				.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUriTemplate(DEFAULT_REDIRECT_URL)
				.scope("auth_base", "auth_user")
				.authorizationUri("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm")
				.tokenUri("https://openapi.alipay.com/gateway.do")
				.userInfoUri("https://openapi.alipay.com/gateway.do")
				.userNameAttributeName("user_id")
				.clientName("支付宝");
		return builder;
	}

}
