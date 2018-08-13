package com.stark.jarvis.security.browser.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.stark.jarvis.security.browser.support.SimpleResponse;
import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.util.Utils;

/**
 * 抽象的 session 失效处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class AbstractSessionStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 跳转的url
	 */
	private String destinationUrl;
	
	/**
	 * 系统配置信息
	 */
	private SecurityProperties securityPropertie;
	
	/**
	 * 重定向策略
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * 跳转前是否创建新的 session
	 */
	private boolean createNewSession = true;

	/**
	 * @param invalidSessionUrl
	 * @param invalidSessionHtmlUrl
	 */
	public AbstractSessionStrategy(SecurityProperties securityPropertie) {
		String invalidSessionUrl = securityPropertie.getBrowser().getSession().getSessionInvalidUrl();
		Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
		Assert.isTrue(StringUtils.endsWithIgnoreCase(invalidSessionUrl, ".html"), "url must end with '.html'");
		this.destinationUrl = invalidSessionUrl;
		this.securityPropertie = securityPropertie;
	}

	protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info("session失效");
		
		if (createNewSession) {
			request.getSession();
		}

		String sourceUrl = request.getRequestURI();
		String targetUrl;

		if (Utils.acceptHtml(request)) {
			if (StringUtils.equals(sourceUrl, securityPropertie.getBrowser().getLoginPage())
					|| StringUtils.equals(sourceUrl, securityPropertie.getBrowser().getLogoutUrl())) {
				targetUrl = sourceUrl;
			} else {
				targetUrl = destinationUrl;
			}
			logger.info("跳转到:" + targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
		} else {
			Object result = buildResponseContent(request);
			Utils.responseAsJson(HttpStatus.UNAUTHORIZED, response, result);
		}

	}

	/**
	 * @param request
	 * @return
	 */
	protected Object buildResponseContent(HttpServletRequest request) {
		String message = "session已失效";
		if (isConcurrency()) {
			message = message + "，可能由于并发登录导致";
		}
		return new SimpleResponse(message);
	}

	/**
	 * session失效是否是并发导致的
	 * @return
	 */
	protected boolean isConcurrency() {
		return false;
	}

	/**
	 * Determines whether a new session should be created before redirecting (to
	 * avoid possible looping issues where the same session ID is sent with the
	 * redirected request). Alternatively, ensure that the configured URL does
	 * not pass through the {@code SessionManagementFilter}.
	 *
	 * @param createNewSession
	 *            defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

}
