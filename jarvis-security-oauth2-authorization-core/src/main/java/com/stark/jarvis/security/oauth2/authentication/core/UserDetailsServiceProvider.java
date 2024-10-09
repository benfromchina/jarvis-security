package com.stark.jarvis.security.oauth2.authentication.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户接口提供者
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
public interface UserDetailsServiceProvider {

    /**
     * 是否支持此授权类型
     *
     * @param grantType 授权类型
     * @return 支持返回 {@literal true} ，否则返回 {@literal false}
     */
    boolean supports(String grantType);

    /**
     * 根据广义的用户名获取用户
     *
     * @param username 广义的用户名
     * @return 用户
     * @throws UsernameNotFoundException 用户名不存在时抛出异常
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
