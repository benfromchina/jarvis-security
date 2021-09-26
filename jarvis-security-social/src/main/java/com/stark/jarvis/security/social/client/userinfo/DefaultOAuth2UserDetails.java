package com.stark.jarvis.security.social.client.userinfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DefaultOAuth2UserDetails implements OAuth2UserDetails {
	
	private static final long serialVersionUID = 9008815303778308705L;

	private DefaultOAuth2User oauth2UserDelegate;
	
	private String name;
	
	private String password;
	
	private String username;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	private boolean enabled;
	
	public DefaultOAuth2UserDetails(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
		oauth2UserDelegate = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2UserDelegate != null
				? oauth2UserDelegate.getAttributes()
				: new HashMap<>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2UserDelegate != null
				? oauth2UserDelegate.getAuthorities()
				: new ArrayList<>();
	}

}
