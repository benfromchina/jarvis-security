package com.stark.jarvis.security.oauth2.authentication.exchange;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 短信验证码认证令牌
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/28
 */
@Getter
public class OAuth2ExchangeAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

    public OAuth2ExchangeAuthenticationToken(Set<String> scopes, Authentication clientPrincipal,
                                             @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE), scopes, clientPrincipal, additionalParameters);
    }

}
