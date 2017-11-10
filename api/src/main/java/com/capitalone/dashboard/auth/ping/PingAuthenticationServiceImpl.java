package com.capitalone.dashboard.auth.ping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.ldap.CustomUserDetails;
import com.google.common.collect.Sets;
import com.pingidentity.opentoken.Agent;

@Component
public class PingAuthenticationServiceImpl implements PingAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(PingAuthenticationServiceImpl.class);

	@Override
	public Authentication getAuthentication(HttpServletRequest request) {
		Authentication authentication = this.getAuthenticationData(request);
		return authentication;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<String> roles) {
		Collection<GrantedAuthority> authorities = Sets.newHashSet();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});

		return authorities;
	}

	private Authentication getAuthenticationData(HttpServletRequest req) {
		CustomUserDetails customUserDetails = null;
		try {
			Map<String, String> userInfo = null;
			String agentConfig = PingAuthenticationUtil.getAgentConfig();

			if (agentConfig != null) {
				ByteArrayInputStream configStream = new ByteArrayInputStream(agentConfig.getBytes());
				Agent agent = new Agent(configStream);
				userInfo = agent.readToken(req);
				if (userInfo != null) {
					customUserDetails = PingAuthenticationUtil.createUser(userInfo);
					return PingAuthenticationUtil.createSuccessfulAuthentication(customUserDetails);
				} else {
					LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> userInfo is Null");
				}
			}
		} catch (IOException ioe) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> IOException :" + ioe);
		} catch (Exception exception) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> Exception :" + exception);
		}
		return null;
	}

}
