package com.stark.jarvis.security.social.client.userinfo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * 用户接口实现。
 * @author Ben
 * @since 2.0.0
 * @version 1.0.0
 */
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

	private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

	private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

	private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
			new ParameterizedTypeReference<Map<String, Object>>() {};

	@Autowired(required = false)
	private List<OAuth2UserInfoResponseHttpMessageConverterProvider> oauth2UserInfoResponseHttpMessageConverterProviders;
	@Autowired
	private OAuth2UserConverterProviderManager oauth2UserConverter;
	@Autowired
	private OAuth2UserRequestEntityConverterProviderManager requestEntityConverter;
	@Autowired(required = false)
	private UserConnectionRepository userConnectionRepository;
	@Autowired(required = false)
	private UsernameEnhancer usernameEnhancer;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		Assert.notNull(userRequest, "userRequest cannot be null");

		if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
			OAuth2Error oauth2Error = new OAuth2Error(
				MISSING_USER_INFO_URI_ERROR_CODE,
				"Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " +
					userRequest.getClientRegistration().getRegistrationId(),
				null
			);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		if (!StringUtils.hasText(userNameAttributeName)) {
			OAuth2Error oauth2Error = new OAuth2Error(
				MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
				"Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " +
					userRequest.getClientRegistration().getRegistrationId(),
				null
			);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

		RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);

		ResponseEntity<Map<String, Object>> response;
		try {
			RestOperations restOperations = createRestOperations(userRequest.getClientRegistration(), userRequest.getAccessToken().getTokenValue());
			response = restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
		} catch (OAuth2AuthorizationException ex) {
			OAuth2Error oauth2Error = ex.getError();
			StringBuilder errorDetails = new StringBuilder();
			errorDetails.append("Error details: [");
			errorDetails.append("UserInfo Uri: ").append(
					userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
			errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
			if (oauth2Error.getDescription() != null) {
				errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
			}
			errorDetails.append("]");
			oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
					"An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
		} catch (RestClientException ex) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
					"An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
		}

		Map<String, Object> userAttributes = response.getBody();
		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		authorities.add(new OAuth2UserAuthority(userAttributes));
		OAuth2AccessToken token = userRequest.getAccessToken();
		for (String authority : token.getScopes()) {
			authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
		}
		
		OAuth2UserDetails user;
		String providerUserId = MapUtils.getString(userAttributes, userNameAttributeName);
		UserConnectionForm userConnectionForm = oauth2UserConverter.convert(userRequest.getClientRegistration(), userAttributes);
		if (userConnectionRepository != null) {
			userConnectionForm = userConnectionRepository.saveForm(userConnectionForm);
		}
		user = userConnectionForm.getUser();
		
		authorities.addAll(user.getAuthorities());
		DefaultOAuth2UserDetails oauth2UserDetails = new DefaultOAuth2UserDetails(authorities, userAttributes, userNameAttributeName);
		BeanUtils.copyProperties(user, oauth2UserDetails);
		if (StringUtils.isEmpty(oauth2UserDetails.getUsername())) {
			oauth2UserDetails.setUsername("oauth2:" + registrationId + ":" + providerUserId);
		}
		if (usernameEnhancer != null) {
			try {
				String username = oauth2UserDetails.getUsername();
				username = usernameEnhancer.enhance(username);
				oauth2UserDetails.setUsername(username);
			} catch (Exception e) {
				OAuth2Error oauth2Error = new OAuth2Error("enhance_username_error", "An error occurred while attempting to enhance the username: " + e.getMessage(), null);
				throw new OAuth2AuthenticationException(oauth2Error, e);
			}
		}
		return oauth2UserDetails;
	}
	
	private RestOperations createRestOperations(ClientRegistration clientRegistration, String accessToken) {
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(new OAuth2UserInfoResponseHttpMessageConverterProviderManager(clientRegistration, oauth2UserInfoResponseHttpMessageConverterProviders, accessToken), new FormHttpMessageConverter()));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		return restTemplate;
	}
	
}
