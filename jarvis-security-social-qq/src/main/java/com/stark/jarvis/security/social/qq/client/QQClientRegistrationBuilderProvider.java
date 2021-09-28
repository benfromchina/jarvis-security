package com.stark.jarvis.security.social.qq.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.client.ClientRegistrationBuilderProvider;
import com.stark.jarvis.security.social.qq.properties.Constants;

@Component
public class QQClientRegistrationBuilderProvider implements ClientRegistrationBuilderProvider {

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
				.scope("get_user_info", "list_album", "upload_pic")
				.authorizationUri("https://graph.qq.com/oauth2.0/authorize")
				.tokenUri("https://graph.qq.com/oauth2.0/token")
				.userInfoUri("https://graph.qq.com/oauth2.0/me")
				.userNameAttributeName("openid")
				.clientName("QQ");
		return builder;
	}

}
