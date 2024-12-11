package com.stark.jarvis.security.oauth2.authentication.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stark.jarvis.security.oauth2.authentication.core.UserDetailsServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

/**
 * 根据交换用户信息查询用户
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/29
 */
public interface ExchangeUserDetailsServiceProvider extends UserDetailsServiceProvider {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    default boolean supports(String grantType) {
        return OAuth2ParameterNamesExtended.GRANT_TYPE.equals(grantType);
    }

    /**
     * 根据交换用户信息获取用户
     *
     * @param username 格式如: <code>{"type":"type","userId":"userId","token":"token"}</code>
     * @return 用户
     * @throws UsernameNotFoundException 用户名不存在时抛出异常
     */
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExchangeInfoDTO exchangeInfo;
        try {
            exchangeInfo = OBJECT_MAPPER.readValue(username, ExchangeInfoDTO.class);
        } catch (Exception e) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "Deserialize exchange info failed.",
                    null);
            throw new OAuth2AuthenticationException(error);
        }
        return loadUserByExchangeInfo(exchangeInfo);
    }

    /**
     * 根据交换用户信息获取用户
     *
     * @param exchangeInfo 交换用户信息
     * @return 用户
     */
    UserDetails loadUserByExchangeInfo(ExchangeInfoDTO exchangeInfo) throws UsernameNotFoundException;

    @Configuration
    class ExchangeUserDetailsServiceProviderConfig {

        @Bean
        @ConditionalOnMissingBean
        public ExchangeUserDetailsServiceProvider exchangeUserDetailsServiceProvider() {
            throw new RuntimeException("ExchangeUserDetailsServiceProvider implementation required");
        }

    }

}
