package com.stark.jarvis.security.oauth2.authentication.sms.code;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信验证码认证服务提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
@Component
@Slf4j
public class OAuth2SmsCodeAuthenticationProvider extends OAuth2ResourceOwnerBaseAuthenticationProvider {

    public OAuth2SmsCodeAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, @Lazy AuthenticationManager authenticationManager) {
        super(authorizationService, tokenGenerator, authenticationManager);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> additionalParameters) {
        Object phoneNumber = additionalParameters.get(OAuth2ParameterNamesExtended.PHONE_NUMBER);
        Object smsCode = additionalParameters.get(OAuth2ParameterNamesExtended.SMS_CODE);
        return new UsernamePasswordAuthenticationToken(phoneNumber, smsCode);
    }

}
