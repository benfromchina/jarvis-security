package com.stark.jarvis.security.social.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * 客户端注册构造器提供者。
 * <p>实现该接口，可自动注册客户端。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class ClientRegistrationBuilderProviderManager {
	
	@Autowired(required = false)
	private List<ClientRegistrationBuilderProvider> providers;
	
	/**
	 * 获取客户端注册构造器。
	 * @param registrationId 第三方服务标识。
	 * @return 客户端注册构造器。
	 */
	public Builder getBuilder(String registrationId) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (ClientRegistrationBuilderProvider provider : providers) {
				if (provider.supports(registrationId)) {
					return provider.getBuilder();
				}
			}
		}
		return null;
	}

}
