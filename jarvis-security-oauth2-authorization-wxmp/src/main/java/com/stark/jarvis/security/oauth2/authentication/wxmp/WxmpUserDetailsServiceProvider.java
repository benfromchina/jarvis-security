package com.stark.jarvis.security.oauth2.authentication.wxmp;

import com.stark.jarvis.security.oauth2.authentication.core.UserDetailsServiceProvider;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.dto.SessionDTO;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.util.WxmpUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

/**
 * 根据手机号码查询用户
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
public interface WxmpUserDetailsServiceProvider extends UserDetailsServiceProvider {

    @Override
    default boolean supports(String grantType) {
        return OAuth2ParameterNamesExtended.GRANT_TYPE.equals(grantType);
    }

    /**
     * 根据<code>微信开放平台唯一标识</code>或<code>小程序唯一标识</code>获取用户
     *
     * @param username 格式如: <code>{"sessionKey":"sessionKey","unionid":"unionid","openid":"openid"}</code>，
     *                 可用 {@link WxmpUtils#deserialize(String, Class)} 方法转为 {@link SessionDTO} 对象
     * @return 用户
     * @throws UsernameNotFoundException 用户名不存在时抛出异常
     */
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SessionDTO session;
        try {
            session = WxmpUtils.deserialize(username, SessionDTO.class);
        } catch (Exception e) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "Deserialize session failed.",
                    OAuth2ParameterNamesExtended.URL_CODE_TO_SESSION);
            throw new OAuth2AuthenticationException(error);
        }
        return loadUserBySession(session);
    }

    /**
     * 根据微信小程序 session 信息获取用户
     *
     * @param session 微信小程序 session 信息
     * @return 用户
     */
    UserDetails loadUserBySession(SessionDTO session) throws UsernameNotFoundException;

}
