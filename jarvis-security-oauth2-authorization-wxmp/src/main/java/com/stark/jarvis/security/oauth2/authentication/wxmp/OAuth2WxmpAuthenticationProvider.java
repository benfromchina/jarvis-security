package com.stark.jarvis.security.oauth2.authentication.wxmp;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.WxmpLoginService;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.dto.SessionDTO;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.util.WxmpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信小程序认证服务提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Component
@Slf4j
public class OAuth2WxmpAuthenticationProvider extends OAuth2ResourceOwnerBaseAuthenticationProvider {

    private final WxmpLoginService loginService;

    public OAuth2WxmpAuthenticationProvider(WxmpLoginService loginService, OAuth2AuthorizationService authorizationService,
                                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                            @Lazy AuthenticationManager authenticationManager) {
        super(authorizationService, tokenGenerator, authenticationManager);
        this.loginService = loginService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2WxmpAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> additionalParameters) {
        String code = (String) additionalParameters.get(OAuth2ParameterNamesExtended.CODE);

        SessionDTO sessionDTO;
        try {
            sessionDTO = loginService.code2Session(code);
        } catch (Throwable e) {
            log.error("Failed to get session by code: {}", code, e);
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "Failed to get session by code.", OAuth2ParameterNamesExtended.URL_CODE_TO_SESSION);
            throw new OAuth2AuthenticationException(error, e);
        }
        String username;
        try {
            username = WxmpUtils.serialize(sessionDTO);
        } catch (Exception e) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "Failed to serialize session.", OAuth2ParameterNamesExtended.URL_CODE_TO_SESSION);
            throw new OAuth2AuthenticationException(error, e);
        }

        return new UsernamePasswordAuthenticationToken(username, sessionDTO.createCredentials());
    }

}
