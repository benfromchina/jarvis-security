package com.stark.jarvis.security.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 默认的 session 失效处理策略。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class JarvisInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

	public JarvisInvalidSessionStrategy(SecurityProperties securityProperties) {
		super(securityProperties);
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		onSessionInvalid(request, response);
	}

}
