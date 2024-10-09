package com.stark.jarvis.security.oauth2.server.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * {@link OAuth2AuthenticationException} 异常处理器。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/20
 */
@Component
public class OAuth2AuthenticationExceptionHandler implements ExceptionHandler {

    @Override
    public boolean support(Exception ex) {
        return ex instanceof OAuth2AuthenticationException;
    }

    @Override
    public ProblemDetail handle(Exception ex) {
        OAuth2AuthenticationException oAuth2AuthenticationException = (OAuth2AuthenticationException) ex;
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, oAuth2AuthenticationException.getError().toString());
    }

}
