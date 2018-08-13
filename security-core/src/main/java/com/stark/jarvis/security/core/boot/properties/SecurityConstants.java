package com.stark.jarvis.security.core.boot.properties;

/**
 * jarvis 安全配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface SecurityConstants {
	
	/**
	 * 身份认证控制器 URL
	 */
	String UNAUTHENTICATION_URL = "/authentication/require";
	/**
	 * 表单登录处理器 URL
	 */
	String LOGIN_PROCESSING_URL = "/authentication/form";
	/**
	 * 默认的登录页面，当请求头的 Accept 包含 text/html 时，重定向到这个链接，否则返回 json 数据
	 */
	String DEFAULT_LOGIN_PAGE = "/login";
	/**
	 * 默认的退出登录页面，当请求头的 Accept 包含 text/html 时，重定向到这个链接，否则返回 json 数据
	 */
	String DEFAULT_LOGOUT_URL = "/logout";
	/**
	 * 默认的注册页面
	 */
	String DEFAULT_REGISTER_PAGE = "/register";
	/**
	 * 默认的用户名密码登录请求处理 URL
	 */
	String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
	/**
	 * 默认的手机验证码登录请求处理 URL
	 */
	String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";
	/**
	 * 默认的 OPENID 登录请求处理 URL
	 */
	String DEFAULT_LOGIN_PROCESSING_URL_OPENID = "/authentication/openid";
	/**
	 * 默认的处理验证码的 URL 前缀
	 */
	String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
	/**
	 * 验证图片验证码时，http请求中默认的图片验证码参数名
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
	/**
	 * 验证短信验证码时，http请求中默认的短信验证码参数名
	 */
	String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
	/**
	 * 发送短信验证码或验证短信验证码时，http请求中默认的手机号参数名
	 */
	String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
	/**
	 * 默认的 session 失效跳转地址
	 */
	String DEFAULT_SESSION_INVALID_URL = "/session/invalid";
	/**
	 * 默认的获取社交登录用户信息的 URL
	 */
	String DEFAULT_SOCIAL_USER_INFO_URL = "/social/user";
	/**
	 * providerId 参数名
	 */
	String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";
	/**
	 * openid 参数名
	 */
	String DEFAULT_PARAMETER_NAME_OPENID = "openId";
	
}
