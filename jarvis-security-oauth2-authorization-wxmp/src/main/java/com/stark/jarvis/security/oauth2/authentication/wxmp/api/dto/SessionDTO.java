package com.stark.jarvis.security.oauth2.authentication.wxmp.api.dto;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 微信小程序 session 信息
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SessionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 9045922922511606839L;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID 机制说明</a>
     */
    private String unionid;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 创建凭证
     *
     * @return 凭证
     */
    public String createCredentials() {
        if (StringUtils.isNotBlank(unionid)) {
            return DigestUtils.md5DigestAsHex(unionid.getBytes(StandardCharsets.UTF_8));
        }
        return DigestUtils.md5DigestAsHex(openid.getBytes(StandardCharsets.UTF_8));
    }

}
