package com.stark.jarvis.security.oauth2.server.authentication.core;

import com.stark.jarvis.security.oauth2.authentication.core.UserDetailsServiceProvider;
import org.springframework.util.StringUtils;

/**
 * 根据用户名查询用户
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
public interface UsernameUserDetailsServiceProvider extends UserDetailsServiceProvider {

    @Override
    default boolean supports(String grantType) {
        return !StringUtils.hasText(grantType)
                || AuthorizationGrantTypeExtended.PASSWORD.getValue().equals(grantType);
    }

}
