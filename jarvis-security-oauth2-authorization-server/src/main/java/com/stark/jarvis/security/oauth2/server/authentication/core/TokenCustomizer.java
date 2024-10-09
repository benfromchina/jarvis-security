package com.stark.jarvis.security.oauth2.server.authentication.core;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 令牌自定义字段增强器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/22
 */
public interface TokenCustomizer extends Consumer<Map<String, Object>> {

}
