/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stark.jarvis.security.social.client.userinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

/**
 * A {@link Converter} that converts the provided {@link OAuth2UserRequest}
 * to a {@link RequestEntity} representation of a request for the UserInfo Endpoint.
 *
 * @author Joe Grandja
 * @since 5.1
 * @see Converter
 * @see OAuth2UserRequest
 * @see RequestEntity
 */
@Component
public class OAuth2UserRequestEntityConverterProviderManager implements Converter<OAuth2UserRequest, RequestEntity<?>> {
	
	@Autowired(required = false)
	private List<OAuth2UserRequestEntityConverterProvider> providers;
	
	private OAuth2UserRequestEntityConverter delegate = new OAuth2UserRequestEntityConverter();

	/**
	 * Returns the {@link RequestEntity} used for the UserInfo Request.
	 *
	 * @param userRequest the user request
	 * @return the {@link RequestEntity} used for the UserInfo Request
	 */
	@Override
	public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
		if (CollectionUtils.isNotEmpty(providers)) {
			for (OAuth2UserRequestEntityConverterProvider provider : providers) {
				if (provider.supports(userRequest)) {
					return provider.convert(userRequest);
				}
			}
		}
		
		return delegate.convert(userRequest);
	}
	
}
