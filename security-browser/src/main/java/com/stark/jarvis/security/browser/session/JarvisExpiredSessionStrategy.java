package com.stark.jarvis.security.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;

/**
 * 并发登录导致 session 失效时，默认的处理策略。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class JarvisExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

	public JarvisExpiredSessionStrategy(SecurityProperties securityPropertie) {
		super(securityPropertie);
	}

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		onSessionInvalid(event.getRequest(), event.getResponse());
	}
	
	@Override
	protected boolean isConcurrency() {
		return true;
	}

}
