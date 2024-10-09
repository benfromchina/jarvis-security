package com.stark.jarvis.security.oauth2.server.support.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * 自定义异常对象转换器。
 * <p>把 {@link ProblemDetail} 对象转换为自定义的异常对象。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@FunctionalInterface
public interface ProblemDetailConverter {

    /**
     * 把 {@link ProblemDetail} 对象转换为自定义的异常对象。
     *
     * @param problemDetail 异常对象
     * @return 自定义异常对象
     */
    <T> ResponseEntity<T> convert(ProblemDetail problemDetail);

}
