package com.stark.jarvis.security.social.alipay.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.internal.util.file.IOUtils;
import com.stark.jarvis.security.social.alipay.properties.Constants;
import com.stark.jarvis.security.social.alipay.security.AlipayPublicKeySupplier;
import com.stark.jarvis.security.social.alipay.security.PrivateKeySupplier;

/**
 * 支付宝工具类。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class AlipayUtils {
	
	/**
	 * 读取文件到字符串。
	 * @param path 磁盘绝对路径或 "classpath:" + 文件名。
	 * @return 文件内容。
	 * @throws IOException
	 */
	public static String readFileToString(String path) throws IOException {
		String str;
		if (path.toLowerCase().startsWith("classpath:")) {
			path = path.substring("classpath:".length());
			ClassPathResource classPathResource = new ClassPathResource(path);
			str = IOUtils.toString(classPathResource.getInputStream(), Constants.CHARSET);
		} else {
			str = FileUtils.readFileToString(new File(path), Charset.forName(Constants.CHARSET));
		}
		return str;
	}
	
	/**
	 * 创建支付宝客户端。
	 * @param serverUrl 服务网关。
	 * @param appId 客户端标识。
	 * @param privateKeySupplier 获取私钥接口。
	 * @param format 响应格式。
	 * @param charset 字符集。
	 * @param alipayPublicKeySupplier 获取支付宝公钥接口。
	 * @param signType 签名类型。
	 * @return 支付宝客户端。
	 */
	public static AlipayClient createAlipayClient(String serverUrl, String appId, PrivateKeySupplier privateKeySupplier,
			String format, String charset, AlipayPublicKeySupplier alipayPublicKeySupplier, String signType) {
		String appPrivateKey = null;
		try {
			appPrivateKey = privateKeySupplier.getPrivateKey();
		} catch (IOException e) {
			OAuth2Error oauth2Error = new OAuth2Error("get_private_key_error",
					"An error occurred while attempting to retrieve the private key: " + e.getMessage(), null);
			throw new OAuth2AuthorizationException(oauth2Error, e);
		}
		String alipayPublicKey = null;
		try {
			alipayPublicKey = alipayPublicKeySupplier.getAlipayPublicKey();
		} catch (IOException e) {
			OAuth2Error oauth2Error = new OAuth2Error("get_alipay_public_key_error",
					"An error occurred while attempting to retrieve the alipay public key: " + e.getMessage(), null);
			throw new OAuth2AuthorizationException(oauth2Error, e);
		}
		
		AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, appPrivateKey, format, charset, alipayPublicKey, signType);
		return alipayClient;
	}
	
	/**
	 * 执行请求返回响应。
	 * @param <T> 响应类型。
	 * @param alipayClient 支付宝客户端。
	 * @param request 请求。
	 * @return 响应。
	 */
	public static <T extends AlipayResponse> T executeRequest(AlipayClient alipayClient, AlipayRequest<T> request) {
		return executeRequest(alipayClient, request, null);
	}
	
	/**
	 * 执行请求返回响应。
	 * @param <T> 响应类型。
	 * @param alipayClient 支付宝客户端。
	 * @param request 请求。
	 * @param authToken 请求令牌。
	 * @return 响应。
	 */
	public static <T extends AlipayResponse> T executeRequest(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) {
		T response;
		try {
			if (StringUtils.isNotBlank(authToken)) {
				response = alipayClient.execute(request, authToken);
			} else {
				response = alipayClient.execute(request);
			}
		} catch (AlipayApiException e) {
			OAuth2Error oauth2Error = new OAuth2Error("alipay_client_execute_request_error",
					"An error occurred while attempting to use alipay client to retrieve response : " + e.getMessage(), null);
			throw new OAuth2AuthorizationException(oauth2Error, e);
		}
		if (!response.isSuccess()) {
			String errorCode = response.getSubCode();
			String description = response.getSubMsg();
			OAuth2Error oauth2Error = new OAuth2Error(errorCode, description, null);
			throw new OAuth2AuthorizationException(oauth2Error);
		}
		return response;
	}

}
