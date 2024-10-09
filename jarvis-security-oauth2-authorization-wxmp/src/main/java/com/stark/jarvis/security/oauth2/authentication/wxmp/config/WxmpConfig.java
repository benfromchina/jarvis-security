package com.stark.jarvis.security.oauth2.authentication.wxmp.config;

import com.stark.jarvis.security.oauth2.authentication.wxmp.WxmpUserDetailsServiceProvider;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.WxmpLoginService;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.impl.WxmpLoginServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 微信小程序配置
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Configuration
@EnableConfigurationProperties(WxmpProperties.class)
public class WxmpConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxmpUserDetailsServiceProvider wxmpUserDetailsServiceProvider() {
        throw new RuntimeException("WxmpUserDetailsServiceProvider implementation required");
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.wxmp", name = { "app-id", "app-secret" })
    public WxmpLoginService wxmpLoginService(WxmpProperties wxmpProperties, RestTemplate restTemplate) {
        return new WxmpLoginServiceImpl(wxmpProperties, restTemplate);
    }

}
