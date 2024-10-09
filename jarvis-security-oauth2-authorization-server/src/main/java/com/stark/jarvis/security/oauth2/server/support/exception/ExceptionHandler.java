package com.stark.jarvis.security.oauth2.server.support.exception;

import org.springframework.http.ProblemDetail;

/**
 * 异常处理器接口
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/16
 */
public interface ExceptionHandler {

    /**
     * 是否支持处理该异常
     *
     * @param ex 异常
     * @return 支持返回 {@literal true} ，否则返回 {@literal false}
     */
    boolean support(Exception ex);

    /**
     * 处理异常，返回错误信息
     *
     * @param ex 异常
     * @return 错误信息
     */
    ProblemDetail handle(Exception ex);

}
