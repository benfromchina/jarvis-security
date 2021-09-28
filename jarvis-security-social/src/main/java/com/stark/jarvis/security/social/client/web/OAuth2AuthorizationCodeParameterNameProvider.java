package com.stark.jarvis.security.social.client.web;

/**
 * 获取授权码请求的 <i>code</i> 参数名提供者。
 * <p>如果获取授权码请求重定向地址中的 <i>code</i> 参数名不是 <i>code</i> ，比如支付宝叫 <i>auth_code</i> ，可扩展该接口返回参数名。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface OAuth2AuthorizationCodeParameterNameProvider {
	
	/**
	 * 是否支持传入的第三方标识。
	 * @param registrationId 第三方标识。
	 * @return 支持返回 {@literal true} ，否则返回 {@literal false} 。
	 */
	boolean supports(String registrationId);
	
	/**
	 * 获取 <i>code</i> 参数名，比如支付宝的 <i>auth_code</i>
	 * @return <i>code</i> 参数名，比如支付宝的 <i>auth_code</i> 。
	 */
	String getCodeParameterName();

}
