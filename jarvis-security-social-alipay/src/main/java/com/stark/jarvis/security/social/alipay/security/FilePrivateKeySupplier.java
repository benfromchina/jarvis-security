package com.stark.jarvis.security.social.alipay.security;

import java.io.IOException;

import com.stark.jarvis.security.social.alipay.util.AlipayUtils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePrivateKeySupplier implements PrivateKeySupplier {
	
	private String privateKeyPath;
	
	@Override
	public String getPrivateKey() throws IOException {
		return AlipayUtils.readFileToString(privateKeyPath);
	}

}
