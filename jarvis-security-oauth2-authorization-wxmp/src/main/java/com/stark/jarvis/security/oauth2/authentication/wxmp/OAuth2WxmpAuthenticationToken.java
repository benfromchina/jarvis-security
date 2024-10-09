package com.stark.jarvis.security.oauth2.authentication.wxmp;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * 微信小程序认证令牌
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Getter
public class OAuth2WxmpAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 6111416337931401627L;

    public OAuth2WxmpAuthenticationToken(Set<String> scopes, Authentication clientPrincipal,
                                         @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE), scopes, clientPrincipal, additionalParameters);
    }

}
