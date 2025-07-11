package com.stark.jarvis.security.oauth2.server.authentication.core;

import com.nimbusds.jwt.JWTClaimNames;

/**
 * 扩展 {@link JWTClaimNames}
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/7/11
 */
public final class JWTClaimNamesExtended {

    /**
     * 授权类型
     */
    public static final String GRANT_TYPE = "gty";

    /**
     * 客户端IP哈希
     */
    public static final String CLIENT_IP_HASH = "cih";

}
