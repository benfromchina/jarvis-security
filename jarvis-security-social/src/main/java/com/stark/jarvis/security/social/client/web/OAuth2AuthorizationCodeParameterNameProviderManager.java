package com.stark.jarvis.security.social.client.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 获取授权码请求的 <i>code</i> 参数名提供者。
 * <p>如果获取授权码请求重定向地址中的 <i>code</i> 参数名不是 <i>code</i> ，比如支付宝叫 <i>auth_code</i> ，可扩展该接口返回参数名。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class OAuth2AuthorizationCodeParameterNameProviderManager {
	
	@Autowired(required = false)
	private List<OAuth2AuthorizationCodeParameterNameProvider> providers;
	
	/**
	 * 获取 <i>code</i> 参数名，比如支付宝的 <i>auth_code</i>
	 * @param registrationId 第三方标识。
	 * @return <i>code</i> 参数名，比如支付宝的 <i>auth_code</i> 。
	 */
	public String getCodeParameterName(String registrationId) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2AuthorizationCodeParameterNameProvider provider : providers) {
				if (provider.supports(registrationId)) {
					return provider.getCodeParameterName();
				}
			}
		}
		return null;
	}

}
