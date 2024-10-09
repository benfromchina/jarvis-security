package com.stark.jarvis.security.oauth2.server.authentication.core;

import com.stark.jarvis.security.oauth2.server.properties.SecurityProperties;

import java.util.List;
import java.util.function.Supplier;

/**
 * 无需鉴权的请求提供者
 * <p>扩展该接口，可以配置自己无需鉴权的请求
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/24
 */
public interface PermitAllRequestsSupplier extends Supplier<List<SecurityProperties.Request>> {

}
