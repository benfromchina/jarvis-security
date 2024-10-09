package com.stark.jarvis.security.oauth2.authentication.base;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2 资源所有者基础认证令牌
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
@Getter
public abstract class OAuth2ResourceOwnerBaseAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 1303152290526658474L;

    private final Set<String> scopes;

    /**
     * Sub-class constructor.
     *
     * @param authorizationGrantType the authorization grant type
     * @param scopes                 the scopes
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    protected OAuth2ResourceOwnerBaseAuthenticationToken(AuthorizationGrantType authorizationGrantType, Set<String> scopes, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.scopes = scopes;
    }

}
