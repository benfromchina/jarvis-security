package com.stark.jarvis.security.social.client.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stark.jarvis.security.social.config.OAuth2AuthorizationCodeConfigurer;

import lombok.AllArgsConstructor;

/**
 * 获取授权码请求过滤器。
 * <p>用来识别非 OAuth2.0 协议实现的 <i>code</i> 参数。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 * @see OAuth2AuthorizationCodeConfigurer
 */
@AllArgsConstructor
public class OAuth2AuthorizationCodeRequestFilter extends OncePerRequestFilter {
	
	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
	
	private OAuth2AuthorizationCodeParameterNameProviderManager codeParameterNameProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (PATH_MATCHER.match(OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI, requestURI)) {
			String registrationId = StringUtils.substringAfter(requestURI, "/login/oauth2/code/");
			registrationId = StringUtils.substringBefore(registrationId, "/");
			
			String codeParameterName = codeParameterNameProvider.getCodeParameterName(registrationId);
			
			if (StringUtils.isBlank(codeParameterName) || OAuth2ParameterNames.CODE.equals(codeParameterName)) {
				filterChain.doFilter(request, response);
			} else {
				filterChain.doFilter(new RequestWrapper(request, codeParameterName), response);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}
	
	class RequestWrapper extends HttpServletRequestWrapper {
		
		private String codeAttribute;

		public RequestWrapper(HttpServletRequest request, String codeAttribute) {
			super(request);
			this.codeAttribute = codeAttribute;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> map = new HashMap<>(super.getParameterMap());
			map.put(OAuth2ParameterNames.CODE, map.get(codeAttribute));
			map.remove(codeAttribute);
			return map;
		}
		
	}

}
