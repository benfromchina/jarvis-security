package com.stark.jarvis.security.oauth2.authentication.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信验证码认证服务提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/28
 */
@Component
public class OAuth2ExchangeAuthenticationProvider extends OAuth2ResourceOwnerBaseAuthenticationProvider {

    private final OAuth2AuthorizationService authorizationService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public OAuth2ExchangeAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, @Lazy AuthenticationManager authenticationManager) {
        super(authorizationService, tokenGenerator, authenticationManager);
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ExchangeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> additionalParameters) {
        String type = (String) additionalParameters.get(OAuth2ParameterNamesExtended.TYPE);
        String token = (String) additionalParameters.get(OAuth2ParameterNamesExtended.TOKEN);
        String userId = (String) additionalParameters.get(OAuth2ParameterNamesExtended.USER_ID);

        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN,
                    "Access Token not found.", null);
            throw new OAuth2AuthenticationException(error);
        }

        ExchangeInfoDTO exchangeInfo = new ExchangeInfoDTO(type, userId, token);
        String username;
        try {
            username = OBJECT_MAPPER.writeValueAsString(exchangeInfo);
        } catch (JsonProcessingException e) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "Serialize username failed.", null);
            throw new OAuth2AuthenticationException(error);
        }

        return new UsernamePasswordAuthenticationToken(username, authorization.getId());
    }

}
