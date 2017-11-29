package com.capitalone.dashboard.auth.ping;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.ldap.CustomUserDetails;
import com.google.common.collect.Sets;

@Component
public class PingAuthenticationServiceImpl implements PingAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(PingAuthenticationServiceImpl.class);

	@Autowired
	private PingAuthenticationUtil pingAuthenticationUtil;
	
	@Override
	public Authentication getAuthenticationFromHeaders(Map<String, String> requestHeadersMap) {
		Authentication authentication = this.getAuthenticationDataFromHeaders(requestHeadersMap);
		return authentication;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<String> roles) {
		Collection<GrantedAuthority> authorities = Sets.newHashSet();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});

		return authorities;
	}
	
	private Authentication getAuthenticationDataFromHeaders(Map<String, String> headersMap) {
		CustomUserDetails customUserDetails = null;
		try {
			if (headersMap != null) {
				customUserDetails = pingAuthenticationUtil.createUser(headersMap);
				return pingAuthenticationUtil.createSuccessfulAuthentication(customUserDetails);
			} else {
				LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> userInfo is Null");
			}
		} catch (Exception exception) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> Exception :"
					+ exception);
		}
		return null;
	}

}
