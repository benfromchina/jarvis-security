package com.stark.jarvis.security.oauth2.authentication.base;

import com.stark.jarvis.security.oauth2.authentication.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * OAuth2 资源所有者基础认证转换器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/21
 */
public abstract class OAuth2ResourceOwnerBaseAuthenticationConverter<T extends OAuth2ResourceOwnerBaseAuthenticationToken> implements AuthenticationConverter {

    /**
     * 获取授权类型
     *
     * @return 授权类型
     */
    public abstract AuthorizationGrantType getGrantType();

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!getGrantType().getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // scope (REQUIRED)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (!StringUtils.hasText(scope)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        Set<String> scopes = new HashSet<>(Arrays.asList(scope.split(",")));

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_SECRET) &&
                    !key.equals(OAuth2ParameterNames.REFRESH_TOKEN) &&
                    !key.equals(OAuth2ParameterNames.REDIRECT_URI) &&
                    !key.equals(OAuth2ParameterNames.SCOPE) &&
                    !key.equals(OAuth2ParameterNames.STATE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return buildToken(scopes, clientPrincipal, additionalParameters);
    }

    public abstract T buildToken(Set<String> requestedScopes, Authentication clientPrincipal, Map<String, Object> additionalParameters);

}
