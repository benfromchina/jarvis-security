package com.stark.jarvis.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.stark.jarvis.security.core.util.Utils;

/**
 * 认证成功处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class JarvisAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (Utils.acceptHtml(request)) {
			super.onAuthenticationSuccess(request, response, authentication);
		} else {
			Utils.responseAsJson(response, authentication);
		}
	}

}
