package com.stark.jarvis.security.oauth2.server.authentication.core;

import com.stark.jarvis.security.oauth2.server.properties.SecurityProperties;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Redis 实现的 OAuth2 授权同意接口
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/31
 */
@AllArgsConstructor
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final RegisteredClientRepository registeredClientRepository;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final SecurityProperties securityProperties;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        RegisteredClient registeredClient = registeredClientRepository.findById(authorizationConsent.getRegisteredClientId());
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        Duration accessTokenTimeToLive = registeredClient.getTokenSettings().getAccessTokenTimeToLive();
        Duration refreshTokenTimeToLive = registeredClient.getTokenSettings().getRefreshTokenTimeToLive();
        Duration timeout = refreshTokenTimeToLive.compareTo(accessTokenTimeToLive) > 0
                ? refreshTokenTimeToLive
                : accessTokenTimeToLive;
        redisTemplate.opsForValue().set(buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()), authorizationConsent,
                timeout);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        redisTemplate.delete(buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(buildKey(registeredClientId, principalName));
    }

    private String buildKey(String registeredClientId, String principalName) {
        String key = "this_is_stark_authorization_consent:" + DigestUtils.md5DigestAsHex((registeredClientId + ":" + principalName).getBytes(StandardCharsets.UTF_8));
        if (StringUtils.isNotBlank(securityProperties.getOauth2().getAuthorizationCacheName())) {
            key = securityProperties.getOauth2().getAuthorizationCacheName() + "::" + key;
        }
        return key;
    }

}
