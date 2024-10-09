package com.stark.jarvis.security.oauth2.server.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * {@link BadCredentialsException} 异常处理器。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/8/16
 */
@Component
public class BadCredentialsExceptionHandler implements ExceptionHandler {

    @Override
    public boolean support(Exception ex) {
        return ex instanceof BadCredentialsException;
    }

    @Override
    public ProblemDetail handle(Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
