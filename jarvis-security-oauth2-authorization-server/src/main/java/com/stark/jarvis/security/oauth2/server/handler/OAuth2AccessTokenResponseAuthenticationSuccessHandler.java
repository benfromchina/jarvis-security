package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.authentication.event.OAuth2AuthenticationSuccessEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 扩展 {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AccessTokenResponseAuthenticationSuccessHandler OAuth2AccessTokenResponseAuthenticationSuccessHandler}，在写回响应前发布 {@link OAuth2AuthenticationSuccessEvent OAuth2AuthenticationSuccessEvent} 事件。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/22
 */
@AllArgsConstructor
public class OAuth2AccessTokenResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AccessTokenResponseAuthenticationSuccessHandler delegate = new org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AccessTokenResponseAuthenticationSuccessHandler();

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        applicationEventPublisher.publishEvent(new OAuth2AuthenticationSuccessEvent((OAuth2AccessTokenAuthenticationToken) authentication));
        delegate.onAuthenticationSuccess(request, response, authentication);
    }

}
