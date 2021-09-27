package com.stark.jarvis.security.social.alipay.security;

import java.io.IOException;

/**
 * 支付宝公钥提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface AlipayPublicKeySupplier {
	
	/**
	 * 获取支付宝公钥。
	 * @return 支付宝公钥。
	 * @throws IOException
	 */
	String getAlipayPublicKey() throws IOException;

}
