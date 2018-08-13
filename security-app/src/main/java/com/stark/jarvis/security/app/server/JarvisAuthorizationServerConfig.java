package com.stark.jarvis.security.app.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 认证服务器配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class JarvisAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired(required = false)
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	@Autowired(required = false)
	private TokenEnhancer jwtTokenEnhancer;
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired(required = false)
	private JarvisClientDetailsService jarvisClientDetailsService;

	/**
	 * 认证及 token 配置。
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints.tokenStore(tokenStore)
				.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService);

		if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
			TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
			List<TokenEnhancer> enhancers = new ArrayList<>();
			enhancers.add(jwtTokenEnhancer);
			enhancers.add(jwtAccessTokenConverter);
			enhancerChain.setTokenEnhancers(enhancers);
			endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
		}

	}

	/**
	 * tokenKey 的访问权限表达式配置。
	 */
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()");
	}

	/**
	 * 客户端配置。
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		if (jarvisClientDetailsService != null) {
			List<ClientDetails> clientList = jarvisClientDetailsService.getClients();
			if (!CollectionUtils.isEmpty(clientList)) {
				InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
				for (ClientDetails client : clientList) {
					builder.withClient(client.getClientId())
							.secret(client.getClientSecret())
							.authorizedGrantTypes("refresh_token", "authorization_code", "password")
							.accessTokenValiditySeconds(securityProperties.getOauth2().getAccessTokenValidateSeconds())
							.refreshTokenValiditySeconds(securityProperties.getOauth2().getRefreshTokenValiditySeconds())
							.scopes("all");
				}
			}
		}
	}

}
