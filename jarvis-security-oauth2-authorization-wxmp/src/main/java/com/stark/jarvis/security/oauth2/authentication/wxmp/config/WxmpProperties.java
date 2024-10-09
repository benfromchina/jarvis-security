package com.stark.jarvis.security.oauth2.authentication.wxmp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信小程序配置项
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Data
@ConfigurationProperties(prefix = "spring.security.oauth2.wxmp")
public class WxmpProperties {

    /** 应用ID */
    private String appId;

    /** 应用秘钥 */
    private String appSecret;

}
