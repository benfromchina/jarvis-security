package com.stark.jarvis.security.oauth2.server.handler;

import com.stark.jarvis.security.oauth2.server.support.exception.ExceptionHandler;
import com.stark.jarvis.security.oauth2.server.support.exception.ProblemDetailConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 抽象的异常处理器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/18
 */
@AllArgsConstructor
public abstract class AbstractExceptionHandler {

    private static final MappingJackson2HttpMessageConverter ERROR_HTTP_RESPONSE_CONVERTER = new MappingJackson2HttpMessageConverter();

    private final List<ExceptionHandler> exceptionHandlers;

    private final ProblemDetailConverter problemDetailConverter;

    protected void sendErrorResponse(HttpServletResponse response, Exception exception) throws IOException {
        ProblemDetail problemDetail = null;
        if (!CollectionUtils.isEmpty(exceptionHandlers)) {
            for (ExceptionHandler handler : exceptionHandlers) {
                if (handler.support(exception)) {
                    problemDetail = handler.handle(exception);
                    break;
                }
            }
        }

        if (problemDetail == null) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        if (problemDetailConverter != null) {
            ResponseEntity<?> responseEntity = problemDetailConverter.convert(problemDetail);
            httpResponse.setStatusCode(responseEntity.getStatusCode());
            ERROR_HTTP_RESPONSE_CONVERTER.write(Objects.requireNonNull(responseEntity.getBody()), MediaType.APPLICATION_JSON, httpResponse);
        } else {
            httpResponse.setStatusCode(HttpStatusCode.valueOf(problemDetail.getStatus()));
            ERROR_HTTP_RESPONSE_CONVERTER.write(problemDetail, MediaType.APPLICATION_JSON, httpResponse);
        }
    }

}
