package com.stark.jarvis.security.app.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.app.social.AppSignInUtils;
import com.stark.jarvis.security.core.boot.properties.SecurityConstants;
import com.stark.jarvis.security.core.social.support.SocialController;
import com.stark.jarvis.security.core.social.support.SocialUserInfo;

/**
 * APP 环境下的公共处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@RestController
public class AppSecurityController extends SocialController {
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	@Autowired
	private AppSignInUtils appSignInUtils;
	
	/**
	 * 需要注册时跳到这里，返回 401 和用户信息给前端。
	 * @param request
	 * @return
	 */
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		appSignInUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		return buildSocialUserInfo(connection);
	}

}
