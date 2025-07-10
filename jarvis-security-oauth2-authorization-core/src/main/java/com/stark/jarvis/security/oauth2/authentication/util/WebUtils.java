package com.stark.jarvis.security.oauth2.authentication.util;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 线程安全的请求响应工具类
 * <p>用于获取当前线程中的 {@link HttpServletRequest} 对象和 {@link HttpServletResponse} 对象
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/19
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private WebUtils() {
    }

    /**
     * 获取当前线程中的 HTTP 请求对象
     *
     * @return 当前线程中的 HTTP 请求对象
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }

    /**
     * 获取当前线程中的 HTTP 响应对象
     *
     * @return 当前线程中的 HTTP 响应对象
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getResponse() : null;
    }

    /**
     * 获取客户端 ip 地址
     *
     * @param request HTTP 请求对象
     * @return 客户端 ip 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // nginx
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        // apache proxy
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        // weblogic proxy
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        // X-Forwarded-For
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            String[] array = ip.replace(" ", "").split(",");
            for (String item : array) {
                if (StringUtils.isNotBlank(item)) {
                    return item;
                }
            }
        }
        // no proxy
        return request.getRemoteAddr();
    }

}
