package com.stark.jarvis.security.oauth2.authentication.password;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 密码模式认证服务提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
@Component
@Slf4j
public class OAuth2PasswordAuthenticationProvider extends OAuth2ResourceOwnerBaseAuthenticationProvider {

    public OAuth2PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, @Lazy AuthenticationManager authenticationManager) {
        super(authorizationService, tokenGenerator, authenticationManager);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> getAdditionalParameters) {
        Object username = getAdditionalParameters.get(OAuth2ParameterNames.USERNAME);
        Object password = getAdditionalParameters.get(OAuth2ParameterNames.PASSWORD);
        return new UsernamePasswordAuthenticationToken(username, password);
    }

}
