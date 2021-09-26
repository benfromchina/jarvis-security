package com.stark.jarvis.security.social.client.userinfo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.stark.jarvis.security.social.client.HttpMessageConverters;

public class OAuth2UserInfoResponseHttpMessageConverterProviderManager extends AbstractGenericHttpMessageConverter<Map<String, Object>> {
	
	private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
			new ParameterizedTypeReference<Map<String, Object>>() {};

	private static final GenericHttpMessageConverter<Object> jsonMessageConverter = HttpMessageConverters.getJsonMessageConverter();
	
	private ClientRegistration clientRegistration;
	
	private List<OAuth2UserInfoResponseHttpMessageConverterProvider> providers;
	
	private String accessToken;
	
	public OAuth2UserInfoResponseHttpMessageConverterProviderManager(ClientRegistration clientRegistration, List<OAuth2UserInfoResponseHttpMessageConverterProvider> providers, String accessToken) {
		this.clientRegistration = clientRegistration;
		this.providers = providers;
		this.accessToken = accessToken;
	}
	
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		List<MediaType> mediaTypes = new ArrayList<>();
		if (CollectionUtils.isEmpty(providers)) {
			return mediaTypes;
		}
		providers.forEach(provider -> {
			if (!mediaTypes.contains(provider.getSupportedMediaType())) {
				mediaTypes.add(provider.getSupportedMediaType());
			}
		});
		return mediaTypes;
	}

	@Override
	public Map<String, Object> read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Map<String, Object> map = readInternal(null, inputMessage);
		
		if (CollectionUtils.isNotEmpty(providers)) {
			map.put("accessToken", accessToken);
			for (OAuth2UserInfoResponseHttpMessageConverterProvider provider : providers) {
				if (provider.supports(clientRegistration)) {
					map = provider.enhance(clientRegistration, map);
					break;
				}
			}
			map.remove("accessToken");
		}
		
		return map;
	}
	
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return false;
	}
	
	@Override
	protected boolean canWrite(MediaType mediaType) {
		return false;
	}
	
	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	protected void writeInternal(Map<String, Object> t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// do nothing
	}

	@Override
	protected Map<String, Object> readInternal(Class<? extends Map<String, Object>> clazz,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) jsonMessageConverter.read(
					PARAMETERIZED_RESPONSE_TYPE.getType(), null, inputMessage);
			return map;
		} catch (Exception ex) {
			throw new HttpMessageNotReadableException("An error occurred reading the OAuth 2.0 Access Token Response: " +
					ex.getMessage(), ex, inputMessage);
		}
	}

}
