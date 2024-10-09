package com.stark.jarvis.security.oauth2.server.authentication.core;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 授权类型扩展
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
public class AuthorizationGrantTypeExtended {

    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");

}
