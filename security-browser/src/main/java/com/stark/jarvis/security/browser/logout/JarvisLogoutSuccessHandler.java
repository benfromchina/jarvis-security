package com.stark.jarvis.security.browser.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.stark.jarvis.security.browser.support.SimpleResponse;
import com.stark.jarvis.security.core.util.Utils;

/**
 * 默认的退出成功处理器。
 * <ul>
 * <li>如果设置了 jarvis.security.browser.logout-success-url，则重定向到配置的地址；</li>
 * <li>否则，返回 json 格式的响应。</li>
 * </ul>
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class JarvisLogoutSuccessHandler implements LogoutSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public JarvisLogoutSuccessHandler(String logoutSuccessUrl) {
		this.logoutSuccessUrl = logoutSuccessUrl;
	}

	private String logoutSuccessUrl;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		logger.info("退出成功");

		if (StringUtils.isBlank(logoutSuccessUrl)) {
			Utils.responseAsJson(response, new SimpleResponse("退出成功"));
		} else {
			response.sendRedirect(logoutSuccessUrl);
		}

	}

}
