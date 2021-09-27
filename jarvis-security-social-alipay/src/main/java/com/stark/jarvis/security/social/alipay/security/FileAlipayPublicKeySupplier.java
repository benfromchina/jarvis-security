package com.stark.jarvis.security.social.alipay.security;

import java.io.IOException;

import com.stark.jarvis.security.social.alipay.util.AlipayUtils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileAlipayPublicKeySupplier implements AlipayPublicKeySupplier {
	
	private String alipayPublicKeyPath;
	
	@Override
	public String getAlipayPublicKey() throws IOException {
		return AlipayUtils.readFileToString(alipayPublicKeyPath);
	}

}
