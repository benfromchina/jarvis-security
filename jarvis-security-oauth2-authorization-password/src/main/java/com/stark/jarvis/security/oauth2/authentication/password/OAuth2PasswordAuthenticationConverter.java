package com.stark.jarvis.security.oauth2.authentication.password;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 密码模式认证转换器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/20
 */
@Component
public class OAuth2PasswordAuthenticationConverter extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2PasswordAuthenticationToken> {

    @Override
    public AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE);
    }

    @Override
    public OAuth2PasswordAuthenticationToken buildToken(Set<String> requestedScopes, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        return new OAuth2PasswordAuthenticationToken(requestedScopes, clientPrincipal, additionalParameters);
    }

}
