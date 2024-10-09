package com.stark.jarvis.security.oauth2.authentication.sms.code;

import com.stark.jarvis.security.oauth2.authentication.core.UserDetailsServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 根据手机号码查询用户，{@link #loadUserByUsername} 方法返回的 {@link UserDetails#getPassword()} 应为<code>短信验证码</code>。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
public interface SmsCodeUserDetailsServiceProvider extends UserDetailsServiceProvider {

    @Override
    default boolean supports(String grantType) {
        return OAuth2ParameterNamesExtended.GRANT_TYPE.equals(grantType);
    }

    /**
     * 根据手机号码查询用户，返回的 {@link UserDetails#getPassword()} 应为<code>短信验证码</code>
     *
     * @param username 手机号码
     * @return 用户
     */
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByPhoneNumber(username);
    }

    /**
     * 根据手机号码查询用户，返回的 {@link UserDetails#getPassword()} 应为<code>短信验证码</code>
     *
     * @param phoneNumber 手机号码
     * @return 用户
     */
    UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException;

    @Configuration
    class SmsCodeUserDetailsServiceProviderConfig {

        @Bean
        @ConditionalOnMissingBean
        public SmsCodeUserDetailsServiceProvider smsCodeUserDetailsServiceProvider() {
            throw new RuntimeException("SmsCodeUserDetailsServiceProvider implementation required");
        }

    }

}
