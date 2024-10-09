package com.stark.jarvis.security.oauth2.authentication.wxmp.api;

import com.stark.jarvis.security.oauth2.authentication.wxmp.api.dto.SessionDTO;
import org.springframework.lang.NonNull;

/**
 * 微信小程序登录接口
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
public interface WxmpLoginService {

    /**
     * 小程序登录
     *
     * @param code 登录时获取的 code，可通过<a href="https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.login.html">wx.login</a>获取
     * @return session 信息
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">小程序登录</a>
     */
    SessionDTO code2Session(@NonNull String code);

}
