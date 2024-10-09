package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.List;

/**
 * 认证失败处理器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/18
 */
public class JsonAuthenticationEntryPoint extends AbstractExceptionHandler implements AuthenticationEntryPoint {

    public JsonAuthenticationEntryPoint(List<ExceptionHandler> exceptionHandlers, ProblemDetailConverter problemDetailConverter) {
        super(exceptionHandlers, problemDetailConverter);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        sendErrorResponse(response, authException);
    }

}
