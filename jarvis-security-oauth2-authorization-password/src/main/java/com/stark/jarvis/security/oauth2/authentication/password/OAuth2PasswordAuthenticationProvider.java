package com.stark.jarvis.security.oauth2.authentication.password;

import com.stark.jarvis.security.oauth2.authentication.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.stark.jarvis.security.oauth2.authentication.core.BadCredentialsExceptionHandler;
import com.stark.jarvis.security.oauth2.authentication.core.PreAuthenticationChecker;
import com.stark.jarvis.security.oauth2.authentication.password.config.PasswordProperties;
import com.stark.jarvis.security.oauth2.authentication.password.util.IpUtils;
import com.stark.jarvis.security.oauth2.authentication.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
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

    private final PasswordProperties passwordProperties;

    public OAuth2PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                                OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                                List<PreAuthenticationChecker> preAuthenticationCheckers,
                                                @Lazy AuthenticationManager authenticationManager,
                                                PasswordProperties passwordProperties,
                                                List<BadCredentialsExceptionHandler> badCredentialsExceptionHandlers) {
        super(authorizationService, tokenGenerator, preAuthenticationCheckers, authenticationManager, badCredentialsExceptionHandlers);
        this.passwordProperties = passwordProperties;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(OAuth2ParameterNamesExtended.GRANT_TYPE);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> getAdditionalParameters) {
        Object username = getAdditionalParameters.get(OAuth2ParameterNames.USERNAME);
        Object password = getAdditionalParameters.get(OAuth2ParameterNames.PASSWORD);

        if (!CollectionUtils.isEmpty(passwordProperties.getAllowIp())) {
            HttpServletRequest request = WebUtils.getRequest();
            String clientIp = WebUtils.getClientIp(request);
            if (!StringUtils.hasText(clientIp)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "非法请求",
                        null);
                log.warn("获取客户端IP地址失败: username={}", username);
                throw new OAuth2AuthenticationException(error);
            }
            boolean isAllowed = false;
            for (String cidr : passwordProperties.getAllowIp()) {
                if (cidr.contains("/")) {
                    isAllowed = IpUtils.isInCidr(clientIp, cidr);
                } else {
                    isAllowed = cidr.equals(clientIp);
                }
                if (isAllowed) {
                    break;
                }
            }
            if (!isAllowed) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "非法请求",
                        null);
                log.warn("IP地址不在白名单内: username={},clientIp={},allowIp={}",
                        username, clientIp, passwordProperties.getAllowIp());
                throw new OAuth2AuthenticationException(error);
            }
            log.info("IP地址在白名单内: username={},clientIp={},allowIp={}",
                    username, clientIp, passwordProperties.getAllowIp());
        }

        return new UsernamePasswordAuthenticationToken(username, password);
    }

}
