package com.stark.jarvis.security.oauth2.server.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 安全配置项
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/19
 */
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {

    /**
     * 无需鉴权的请求
     */
    private List<Request> permitAllRequests;

    /**
     * 返回 json 响应的异常
     */
    private List<Class<?>> exceptionClassesReturnJson;

    /**
     * OAuth2 配置项
     */
    private OAuth2Properties oauth2 = new OAuth2Properties();

    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Request {

        /**
         * 请求路径
         */
        @NonNull
        private String path;

        /**
         * 请求方式
         */
        private HttpMethod method;

    }

    public enum HttpMethod {
        GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
    }

    @Data
    public static class OAuth2Properties {

        /**
         * 存储 authorization 的缓存命名空间
         */
        private String authorizationCacheName = "authorization.server.authorization";

        /**
         * state 有效时间，单位秒，默认 300
         */
        private int stateExpiresInSeconds = 60 * 5;

        /**
         * code 有效时间，单位秒，默认 300
         */
        private int codeExpiresInSeconds = 60 * 5;

        /**
         * jwt 配置项
         */
        private JwtProperties jwt = new JwtProperties();

        @Data
        public static class JwtProperties {

            /**
             * RSA 秘钥对文件名
             */
            private String keyPairFile = "keystore.jks";

            /**
             * RSA 秘钥密码
             */
            private String keyStorePassword = "jarvis";

            /**
             * RSA 秘钥对别名
             */
            private String keyPairAlias = "jarvis";

        }

    }

}
