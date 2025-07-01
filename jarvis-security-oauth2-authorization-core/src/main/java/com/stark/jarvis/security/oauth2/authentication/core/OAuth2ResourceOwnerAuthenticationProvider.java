package com.stark.jarvis.security.oauth2.authentication.core;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * OAuth2 资源所有者认证提供者接口，跟 {@link AuthenticationProvider} 保持一致
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/7/1
 */
public interface OAuth2ResourceOwnerAuthenticationProvider {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> authentication);

}
