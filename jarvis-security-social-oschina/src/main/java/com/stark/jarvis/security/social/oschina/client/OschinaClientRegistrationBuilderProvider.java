package com.stark.jarvis.security.social.oschina.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.social.client.ClientRegistrationBuilderProvider;
import com.stark.jarvis.security.social.oschina.properties.Constants;

@Component
public class OschinaClientRegistrationBuilderProvider implements ClientRegistrationBuilderProvider {

	@Override
	public boolean supports(String registrationId) {
		return Constants.REGISTRATION_ID.equals(registrationId);
	}

	@Override
	public Builder getBuilder() {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(Constants.REGISTRATION_ID)
				.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUriTemplate(DEFAULT_REDIRECT_URL)
//				.scope("get_user_info", "list_album", "upload_pic")
				.authorizationUri("https://www.oschina.net/action/oauth2/authorize")
				.tokenUri("https://www.oschina.net/action/openapi/token")
				.userInfoUri("https://www.oschina.net/action/openapi/user")
				.userNameAttributeName("id")
				.clientName("开源中国");
		return builder;
	}

}
