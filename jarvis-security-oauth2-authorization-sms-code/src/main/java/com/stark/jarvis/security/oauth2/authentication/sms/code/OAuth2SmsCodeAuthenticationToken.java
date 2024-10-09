package com.stark.jarvis.security.oauth2.authentication.sms.code;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * 短信验证码认证令牌
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
@Getter
public class OAuth2SmsCodeAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 6584876414264179875L;

    public OAuth2SmsCodeAuthenticationToken(Set<String> scopes, Authentication clientPrincipal,
                                            @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE), scopes, clientPrincipal, additionalParameters);
    }

}
