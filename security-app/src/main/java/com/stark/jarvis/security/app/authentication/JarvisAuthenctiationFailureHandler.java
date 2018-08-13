package com.stark.jarvis.security.app.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.support.SimpleResponse;
import com.stark.jarvis.security.core.util.Utils;

/**
 * APP 环境下认证失败处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class JarvisAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		logger.info("登录失败");
		
		Utils.responseAsJson(HttpStatus.INTERNAL_SERVER_ERROR, response, new SimpleResponse(exception.getMessage()));
	
	}

}
