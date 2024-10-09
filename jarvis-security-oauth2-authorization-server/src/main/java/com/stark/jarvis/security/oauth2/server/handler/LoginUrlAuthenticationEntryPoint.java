package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * 扩展默认的 {@link org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint}，支持指定的异常返回 JSON 格式响应。
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/8/16
 */
public class LoginUrlAuthenticationEntryPoint extends AbstractExceptionHandler implements AuthenticationEntryPoint {

    private final org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint delegate;

    private final List<Class<?>> exceptionClassesReturnJson;

    public LoginUrlAuthenticationEntryPoint(String loginFormUrl, List<Class<?>> exceptionClassesReturnJson,
                                            List<ExceptionHandler> exceptionHandlers, ProblemDetailConverter problemDetailConverter) {
        super(exceptionHandlers, problemDetailConverter);
        delegate = new org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint(loginFormUrl);
        this.exceptionClassesReturnJson = exceptionClassesReturnJson;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (!CollectionUtils.isEmpty(exceptionClassesReturnJson)) {
            for (Class<?> exceptionClass : exceptionClassesReturnJson) {
                if (exceptionClass.isAssignableFrom(authException.getClass())) {
                    sendErrorResponse(response, authException);
                    return;
                }
            }
        }

        delegate.commence(request, response, authException);
    }

}
