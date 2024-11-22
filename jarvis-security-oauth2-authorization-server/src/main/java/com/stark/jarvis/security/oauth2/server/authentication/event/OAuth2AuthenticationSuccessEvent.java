package com.stark.jarvis.security.oauth2.server.authentication.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * OAuth2 令牌认证成功事件
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/22
 */
public class OAuth2AuthenticationSuccessEvent extends ApplicationEvent {

    public OAuth2AuthenticationSuccessEvent(OAuth2AccessTokenAuthenticationToken accessTokenAuthentication) {
        super(accessTokenAuthentication);
    }

    public OAuth2AccessTokenAuthenticationToken getOAuth2AccessTokenAuthenticationToken() {
        return (OAuth2AccessTokenAuthenticationToken) super.getSource();
    }

}
