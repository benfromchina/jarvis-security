package com.stark.jarvis.security.oauth2.authentication.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 认证前检查
 *
 * @author Ben
 * @version 1.0.0
 * @since 2026/10/23
 */
@FunctionalInterface
public interface PreAuthenticationChecker {

    /**
     * 检查用户是否可以进行认证
     *
     * @param authentication      认证信息类型
     * @param authenticationToken 认证令牌
     */
    void check(Class<?> authentication, UsernamePasswordAuthenticationToken authenticationToken);

}
