package com.capitalone.dashboard.auth.ping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.AuthProperties;
import com.capitalone.dashboard.auth.ldap.CustomUserDetails;
import com.google.common.collect.Sets;
import com.pingidentity.opentoken.Agent;

@Component
public class PingAuthenticationServiceImpl implements PingAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(PingAuthenticationServiceImpl.class);
	
	@Autowired
	private AuthProperties authProperties;
	
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
			Map<String, String> userInfo = new HashMap<String, String>();
			userInfo.put(Agent.TOKEN_SUBJECT, "");

			ServletContext context = req.getServletContext();
			Agent agent = null;
			String agentConfig = authProperties.getAgentConfig();
			
			if (context != null) {
				ByteArrayInputStream configStream = new ByteArrayInputStream(agentConfig.getBytes());
				agent = new Agent(configStream);
			}
			
			if (agent != null) {
				userInfo = agent.readToken(req);
			} else {
				LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> Agent is Null");
			}
			
			if (userInfo != null) {
				customUserDetails = PingAuthenticationUtil.createUser(userInfo);
			}
			else {
				LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> userInfo is Null");
				return null;
			}
		} catch (IOException ioe) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> IOException :" + ioe);
			return null;
		} catch (Exception exception) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationData() :=> Exception :" + exception);
			return null;
		}
		
		return PingAuthenticationUtil.createSuccessfulAuthentication(customUserDetails);
	}
	
}
