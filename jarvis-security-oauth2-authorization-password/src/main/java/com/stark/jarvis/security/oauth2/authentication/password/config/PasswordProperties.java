package com.stark.jarvis.security.oauth2.authentication.password.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码登录配置项
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2026/11/19
 */
@Data
@ConfigurationProperties(prefix = "spring.security.oauth2.password")
public class PasswordProperties {

    /**
     * 允许登录的IP地址列表，为空表示所有IP允许访问
     */
    private List<String> allowIp = new ArrayList<>();

}
