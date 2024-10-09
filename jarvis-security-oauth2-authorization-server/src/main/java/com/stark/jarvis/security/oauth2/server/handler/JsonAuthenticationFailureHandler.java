package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.List;

/**
 * 认证失败处理器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/18
 */
@Slf4j
public class JsonAuthenticationFailureHandler extends AbstractExceptionHandler implements AuthenticationFailureHandler {

    public JsonAuthenticationFailureHandler(List<ExceptionHandler> exceptionHandlers, ProblemDetailConverter problemDetailConverter) {
        super(exceptionHandlers, problemDetailConverter);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("认证失败", exception);
        sendErrorResponse(response, exception);
    }

}
