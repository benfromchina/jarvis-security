package com.stark.jarvis.security.oauth2.authentication.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * OAuth2 资源方所有者基础认证服务提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
@Slf4j
public abstract class OAuth2ResourceOwnerBaseAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    public OAuth2ResourceOwnerBaseAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                                         OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                                         AuthenticationManager authenticationManager) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        OAuth2ResourceOwnerBaseAuthenticationToken resourceOwnerBaseAuthentication = (OAuth2ResourceOwnerBaseAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(resourceOwnerBaseAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        Assert.notNull(registeredClient, "registeredClient cannot be null");

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }

        // Validate the requested scope
        Set<String> requestedScopes = resourceOwnerBaseAuthentication.getScopes();
        if (CollectionUtils.isEmpty(requestedScopes) ||
                !registeredClient.getScopes().containsAll(requestedScopes)) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE,
                    "OAuth 2.0 Parameter: " + OAuth2ParameterNames.SCOPE, ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(resourceOwnerBaseAuthentication.getAdditionalParameters());
        Authentication usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(resourceOwnerBaseAuthentication.getScopes())
                .authorizationGrantType(resourceOwnerBaseAuthentication.getGrantType())
                .authorizationGrant(resourceOwnerBaseAuthentication);
        // @formatter:on

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(resourceOwnerBaseAuthentication.getGrantType())
                .authorizedScopes(resourceOwnerBaseAuthentication.getScopes());

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        if (log.isTraceEnabled()) {
            log.trace("Generated access token");
        }

        OAuth2AccessToken accessToken = accessToken(authorizationBuilder,
                generatedAccessToken, tokenContext);
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.id(accessToken.getTokenValue())
                    .authorizedScopes(requestedScopes)
                    .attribute(Principal.class.getName(), usernamePasswordAuthentication);
        }
        else {
            authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        // Do not issue refresh token to public client
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (generatedRefreshToken != null) {
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate a valid refresh token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }

                if (log.isTraceEnabled()) {
                    log.trace("Generated refresh token");
                }

                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }
        }

        OAuth2Authorization authorization = authorizationBuilder.build();
        if (authorizationService != null) {
            this.authorizationService.save(authorization);

            if (log.isTraceEnabled()) {
                log.trace("Saved authorization");
            }
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
                Objects.requireNonNull(authorization.getAccessToken().getClaims()));
    }

    @Override
    public abstract boolean supports(Class<?> authentication);

    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> getAdditionalParameters);

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    private <T extends OAuth2Token> OAuth2AccessToken accessToken(OAuth2Authorization.Builder builder, T token,
                                                                  OAuth2TokenContext accessTokenContext) {

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token.getTokenValue(),
                token.getIssuedAt(), token.getExpiresAt(), accessTokenContext.getAuthorizedScopes());
        OAuth2TokenFormat accessTokenFormat = accessTokenContext.getRegisteredClient()
                .getTokenSettings()
                .getAccessTokenFormat();
        builder.token(accessToken, (metadata) -> {
            if (token instanceof ClaimAccessor claimAccessor) {
                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims());
            }
            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
            metadata.put(OAuth2TokenFormat.class.getName(), accessTokenFormat.getValue());
        });

        return accessToken;
    }

}
