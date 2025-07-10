package com.stark.jarvis.security.oauth2.server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.stark.jarvis.security.oauth2.authentication.core.OAuth2ResourceOwnerAuthenticationProvider;
import com.stark.jarvis.security.oauth2.authentication.core.UserDetailsServiceProvider;
import com.stark.jarvis.security.oauth2.authentication.util.WebUtils;
import com.stark.jarvis.security.oauth2.server.authentication.core.PermitAllRequestsSupplier;
import com.stark.jarvis.security.oauth2.server.authentication.core.TokenCustomizer;
import com.stark.jarvis.security.oauth2.server.authentication.core.UsernameUserDetailsServiceProvider;
import com.stark.jarvis.security.oauth2.server.handler.*;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import com.stark.jarvis.security.oauth2.server.properties.SecurityProperties;
import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 授权服务器配置
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/19
 * @see <a href="https://docs.spring.io/spring-authorization-server/reference/getting-started.html">Spring Authorization Server / Getting Started</a>
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@ComponentScan(basePackages = "com.stark.jarvis.security.oauth2", excludeFilters = {
        @ComponentScan.Filter(Configuration.class)
})
@Slf4j
public class AuthorizationServerConfig {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired(required = false)
    private List<AuthenticationConverter> authenticationConverters;
    @Autowired(required = false) @Lazy
    private List<OAuth2ResourceOwnerAuthenticationProvider> authenticationProvider;
    @Autowired(required = false)
    private List<ExceptionHandler> exceptionHandlers;
    @Autowired(required = false)
    private List<PermitAllRequestsSupplier> permitAllRequestsSuppliers;
    @Autowired(required = false)
    private ProblemDetailConverter problemDetailConverter;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired(required = false)
    private List<TokenCustomizer> tokenCustomizers;
    @Autowired(required = false)
    private List<UserDetailsServiceProvider> userDetailsServiceProviders;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .oidc(Customizer.withDefaults())    // Enable OpenID Connect 1.0
                        .tokenEndpoint(tokenEndpoint -> {
                            tokenEndpoint
                                    .accessTokenResponseHandler(new OAuth2AccessTokenResponseAuthenticationSuccessHandler(applicationEventPublisher))   // 登录成功返回 json
                                    .errorResponseHandler(new JsonAuthenticationFailureHandler(exceptionHandlers, problemDetailConverter));             // 登录失败返回 json
                            if (!CollectionUtils.isEmpty(authenticationConverters)) {
                                authenticationConverters.forEach(tokenEndpoint::accessTokenRequestConverter);
                            }
                            if (!CollectionUtils.isEmpty(authenticationProvider)) {
                                authenticationProvider.forEach(authenticationProvider -> tokenEndpoint.authenticationProvider(new AuthenticationProvider() {

                                    @Override
                                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                                        return authenticationProvider.authenticate(authentication);
                                    }

                                    @Override
                                    public boolean supports(Class<?> authentication) {
                                        return authenticationProvider.supports(authentication);
                                    }
                                }));
                            }
                        })
                        .clientAuthentication(clientAuthentication -> clientAuthentication
                                .errorResponseHandler(new JsonAuthenticationFailureHandler(exceptionHandlers, problemDetailConverter))  // 客户端认证失败返回 json
                        )
                )
                .exceptionHandling(exceptions -> exceptions
                        // Redirect to the login page when not authenticated from the
                        // authorization endpoint
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login", securityProperties.getExceptionClassesReturnJson(), exceptionHandlers, problemDetailConverter),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .defaultAuthenticationEntryPointFor(    // 请求未认证返回 json
                                new JsonAuthenticationEntryPoint(exceptionHandlers, problemDetailConverter),
                                new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)
                        )
                        .accessDeniedHandler(new JsonAccessDeniedHandler(exceptionHandlers, problemDetailConverter))    // 请求未授权返回 json
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userDetailsService())
                .authorizeHttpRequests(authorize -> {
                    List<SecurityProperties.Request> permitAllRequests = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(securityProperties.getPermitAllRequests())) {
                        securityProperties.getPermitAllRequests().forEach(request -> {
                            if (StringUtils.hasLength(request.getPath())) {
                                permitAllRequests.add(request);
                            }
                        });
                    }
                    if (!CollectionUtils.isEmpty(permitAllRequestsSuppliers)) {
                        permitAllRequestsSuppliers.forEach(permitAllRequestsSupplier -> permitAllRequests.addAll(permitAllRequestsSupplier.get()));
                    }
                    if (!CollectionUtils.isEmpty(permitAllRequests)) {
                        permitAllRequests.forEach(request -> {
                            if (request.getMethod() != null) {
                                authorize
                                        .requestMatchers(HttpMethod.valueOf(request.getMethod().name()), request.getPath())
                                        .permitAll();
                            } else {
                                authorize
                                        .requestMatchers(request.getPath())
                                        .permitAll();
                            }
                        });
                    }
                    authorize.anyRequest().authenticated();
                })
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            HttpServletRequest request = WebUtils.getRequest();
            if (request == null) {
                throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
            }
            String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

            for (UserDetailsServiceProvider userDetailsServiceProvider : userDetailsServiceProviders) {
                if (userDetailsServiceProvider.supports(grantType)) {
                    return userDetailsServiceProvider.loadUserByUsername(username);
                }
            }

            throw new InternalAuthenticationServiceException(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public UsernameUserDetailsServiceProvider usernameUserDetailsServiceProvider() {
        throw new RuntimeException("UsernameUserDetailsServiceProvider implementation required");
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository() {
        throw new RuntimeException("RegisteredClientRepository implementation required");
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        String keyPairFile = securityProperties.getOauth2().getJwt().getKeyPairFile();
        String keyStorePassword = securityProperties.getOauth2().getJwt().getKeyStorePassword();
        String keyPairAlias = securityProperties.getOauth2().getJwt().getKeyPairAlias();
        Resource keyPairResource;
        if (keyPairFile.startsWith("classpath:")) {
            keyPairResource = new ClassPathResource(keyPairFile.substring("classpath:".length()));
        } else {
            keyPairResource = new FileSystemResource(keyPairFile);
        }
        KeyPair keyPair = new KeyStoreKeyFactory(keyPairResource, keyStorePassword.toCharArray()).getKeyPair(keyPairAlias);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                // gty: 授权类型 grantType
                context.getClaims().claim("gty", context.getAuthorizationGrantType().getValue());
                // cih: 客户端IP的MD5哈希
                String clientIp = WebUtils.getClientIp(WebUtils.getRequest());
                context.getClaims().claim("cih", DigestUtils.md5DigestAsHex(Objects.requireNonNull(clientIp).getBytes(StandardCharsets.UTF_8)));
                if (!CollectionUtils.isEmpty(tokenCustomizers)) {
                    tokenCustomizers.forEach(tokenCustomizer -> context.getClaims().claims(tokenCustomizer));
                }
            }
        };
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource, OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtTokenCustomizer);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService() {
        log.warn("Using default InMemoryOAuth2AuthorizationService. Consider using a persistent store.");
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        log.warn("Using default InMemoryOAuth2AuthorizationConsentService. Consider using a persistent store.");
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
