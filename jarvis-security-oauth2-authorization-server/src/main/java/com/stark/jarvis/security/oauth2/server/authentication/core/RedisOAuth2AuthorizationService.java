package com.stark.jarvis.security.oauth2.server.authentication.core;

import com.stark.jarvis.security.oauth2.server.properties.SecurityProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis 实现的令牌存储
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 0.0.1, 2023/9/19
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final SecurityProperties securityProperties;

    public RedisOAuth2AuthorizationService(RedisTemplate<Object, Object> redisTemplate, SecurityProperties securityProperties) {
        this.redisTemplate = redisTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        long TIMEOUT = 0;
        // state
        String state = authorization.getAttribute("state");
        if (StringUtils.hasText(state)) {
            long timeout = securityProperties.getOauth2().getStateExpiresInSeconds();
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.STATE, state), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // code
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            long timeout = securityProperties.getOauth2().getCodeExpiresInSeconds();
            if (authorizationCode.getToken().getIssuedAt() != null && authorizationCode.getToken().getExpiresAt() != null) {
                timeout = ChronoUnit.SECONDS.between(authorizationCode.getToken().getIssuedAt(), authorizationCode.getToken().getExpiresAt());
            }
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.CODE, authorizationCode.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // access_token
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            long timeout = ChronoUnit.SECONDS.between(Objects.requireNonNull(accessToken.getToken().getIssuedAt()), accessToken.getToken().getExpiresAt());
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // id_token
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken != null) {
            long timeout = ChronoUnit.SECONDS.between(Objects.requireNonNull(oidcIdToken.getToken().getIssuedAt()), oidcIdToken.getToken().getExpiresAt());
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey("oidc_id_token", oidcIdToken.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // refresh_token
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            long timeout = ChronoUnit.SECONDS.between(Objects.requireNonNull(refreshToken.getToken().getIssuedAt()), refreshToken.getToken().getExpiresAt());
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // user_code
        OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
        if (userCode != null) {
            long timeout = ChronoUnit.SECONDS.between(Objects.requireNonNull(userCode.getToken().getIssuedAt()), userCode.getToken().getExpiresAt());
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey("user_code", userCode.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }
        // device_code
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCode != null) {
            long timeout = ChronoUnit.SECONDS.between(Objects.requireNonNull(deviceCode.getToken().getIssuedAt()), deviceCode.getToken().getExpiresAt());
            TIMEOUT = Long.max(TIMEOUT, timeout);
            redisTemplate.opsForValue().set(buildKey("device_code", deviceCode.getToken().getTokenValue()), authorization,
                    timeout, TimeUnit.SECONDS);
        }

        // id
        if (TIMEOUT > 0) {
            redisTemplate.opsForValue().set(buildKey("id", authorization.getId()), authorization,
                    TIMEOUT, TimeUnit.SECONDS);
        } else {
            TIMEOUT = 60 * 5;
            redisTemplate.opsForValue().set(buildKey("id", authorization.getId()), authorization,
                    TIMEOUT, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        List<String> keys = new ArrayList<>();
        keys.add(buildKey("id", authorization.getId()));
        // state
        String state = authorization.getAttribute("state");
        if (StringUtils.hasText(state)) {
            keys.add(buildKey(OAuth2ParameterNames.STATE, state));
        }
        // code
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            keys.add((buildKey(OAuth2ParameterNames.CODE, authorizationCode.getToken().getTokenValue())));
        }
        // access_token
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            keys.add((buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getToken().getTokenValue())));
        }
        // id_token
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken != null) {
            keys.add((buildKey("oidc_id_token", oidcIdToken.getToken().getTokenValue())));
        }
        // refresh_token
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            keys.add((buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getToken().getTokenValue())));
        }
        // user_code
        OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
        if (userCode != null) {
            keys.add((buildKey("user_code", userCode.getToken().getTokenValue())));
        }
        // device_code
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCode != null) {
            keys.add((buildKey("device_code", deviceCode.getToken().getTokenValue())));
        }
        redisTemplate.delete(keys);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey("id", id));
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        if (tokenType != null) {
            return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(tokenType.getValue(), token));
        }

        // access_token
        OAuth2Authorization oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // refresh_token
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // state
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(OAuth2ParameterNames.STATE, token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // code
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(OAuth2ParameterNames.CODE, token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // id_token
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey("oidc_id_token", token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // user_code
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey("user_code", token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }
        // device_code
        oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey("device_code", token));
        if (oAuth2Authorization != null) {
            return oAuth2Authorization;
        }

        return null;
    }

    private String buildKey(String type, String id) {
        String key = "this_is_stark_authorization:" + DigestUtils.md5DigestAsHex((type + ":" + id).getBytes(StandardCharsets.UTF_8));
        if (io.micrometer.common.util.StringUtils.isNotBlank(securityProperties.getOauth2().getAuthorizationCacheName())) {
            key = securityProperties.getOauth2().getAuthorizationCacheName() + "::" + key;
        }
        return key;
    }

}
