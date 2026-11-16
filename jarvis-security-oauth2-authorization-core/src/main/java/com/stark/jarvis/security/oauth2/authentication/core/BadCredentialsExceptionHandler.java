package com.stark.jarvis.security.oauth2.authentication.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * {@link org.springframework.security.authentication.BadCredentialsException} 异常处理器
 * <p>支持自定义处理 {@link org.springframework.security.authentication.BadCredentialsException} 异常
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2026/11/16
 */
@FunctionalInterface
public interface BadCredentialsExceptionHandler {

    /**
     * 处理 {@link org.springframework.security.authentication.BadCredentialsException} 异常
     *
     * @param authenticationToken 认证令牌
     */
    void handle(UsernamePasswordAuthenticationToken authenticationToken);

}
