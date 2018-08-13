package com.stark.jarvis.security.app.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * token 存储配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class TokenStoreConfig {
	
	@Configuration
	@ConditionalOnProperty(prefix = "jarvis.security.oauth2", name = "token-store", havingValue = "redis")
	@Description("使用 redis 存储 token 时的配置，只有在 jarvis.security.oauth2.token-store 配置为 redis 时生效")
	public static class RedisConfig {
		
		@Autowired
		private RedisConnectionFactory redisConnectionFactory;
		
		@Bean
		public TokenStore redisTokenStore() {
			return new RedisTokenStore(redisConnectionFactory);
		}
		
	}

	@Configuration
	@ConditionalOnProperty(prefix = "jarvis.security.oauth2", name = "token-store", havingValue = "jwt", matchIfMissing = true)
	@Description("使用 jwt 时的配置，默认生效")
	public static class JwtConfig {
		
		@Autowired
		private SecurityProperties securityProperties;
		
		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}
		
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	        converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
	        return converter;
		}
		
		@Bean
		@ConditionalOnBean(TokenEnhancer.class)
		public TokenEnhancer jwtTokenEnhancer(){
			return new TokenJwtEnhancer();
		}
		
	}

}
