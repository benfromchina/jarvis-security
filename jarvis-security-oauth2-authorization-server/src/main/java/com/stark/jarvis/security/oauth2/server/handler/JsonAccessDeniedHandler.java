package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.List;

/**
 * 认证失败处理器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
public class JsonAccessDeniedHandler extends AbstractExceptionHandler implements AccessDeniedHandler {

    public JsonAccessDeniedHandler(List<ExceptionHandler> exceptionHandlers, ProblemDetailConverter problemDetailConverter) {
        super(exceptionHandlers, problemDetailConverter);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendErrorResponse(response, accessDeniedException);
    }

}
