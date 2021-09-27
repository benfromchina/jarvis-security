package com.stark.jarvis.security.social.alipay.security;

import java.io.IOException;

/**
 * 私钥提供者。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface PrivateKeySupplier {
	
	/**
	 * 获取私钥。
	 * @return 私钥。
	 * @throws IOException
	 */
	String getPrivateKey() throws IOException;

}
