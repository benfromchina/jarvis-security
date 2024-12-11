package com.stark.jarvis.security.oauth2.authentication.exchange;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 短信认证转换器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/20
 */
@Component
public class OAuth2ExchangeAuthenticationConverter extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ExchangeAuthenticationToken> {

    @Override
    public AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE);
    }

    @Override
    public OAuth2ExchangeAuthenticationToken buildToken(Set<String> requestedScopes, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        return new OAuth2ExchangeAuthenticationToken(requestedScopes, clientPrincipal, additionalParameters);
    }

}
