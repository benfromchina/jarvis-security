package com.stark.jarvis.security.oauth2.authentication.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 组件扫描配置。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/22
 */
@Configuration
@ComponentScan(value = "com.stark.jarvis.security.oauth2.authentication", excludeFilters = @ComponentScan.Filter(Configuration.class))
public class ComponentScanConfig {

}
