package com.stark.jarvis.security.social.client;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * 扩展第三方登录。
 * @author Ben
 * @see CommonOAuth2Provider
 */
public enum SocialOAuth2Provider {
	
	QQ {

		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("get_user_info", "list_album", "upload_pic");
			builder.authorizationUri("https://graph.qq.com/oauth2.0/authorize");
			builder.tokenUri("https://graph.qq.com/oauth2.0/token");
			builder.userInfoUri("https://graph.qq.com/oauth2.0/me");
			builder.userNameAttributeName("openid");
			builder.clientName("QQ");
			return builder;
		}
	},
	
	/**
	 * @see <a href="https://opendocs.alipay.com/open/284/web">PC 网页内获取用户信息</a>
	 */
	ALIPAY {

		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("auth_base", "auth_user");
			builder.authorizationUri("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm");
			builder.tokenUri("https://openapi.alipay.com/gateway.do");
			builder.userInfoUri("https://openapi.alipay.com/gateway.do");
			builder.userNameAttributeName("user_id");
			builder.clientName("ALIPAY");
			return builder;
		}
	};

	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

	protected final ClientRegistration.Builder getBuilder(String registrationId,
															ClientAuthenticationMethod method, String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(redirectUri);
		return builder;
	}

	/**
	 * Create a new
	 * {@link org.springframework.security.oauth2.client.registration.ClientRegistration.Builder
	 * ClientRegistration.Builder} pre-configured with provider defaults.
	 * @param registrationId the registration-id used with the new builder
	 * @return a builder instance
	 */
	public abstract ClientRegistration.Builder getBuilder(String registrationId);

}
