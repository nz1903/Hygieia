package com.capitalone.dashboard.auth.ping;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.AuthProperties;
import com.capitalone.dashboard.auth.ldap.CustomUserDetails;
import com.capitalone.dashboard.model.AuthType;

@Component
public class PingAuthenticationUtil {
	private static final Logger LOGGER = Logger.getLogger(PingAuthenticationUtil.class);
	
	@Autowired
	private AuthProperties authProperties;
	
	CustomUserDetails createUser(Map<String, String> userInfo) {
		CustomUserDetails customUserDetails = null;
		
		try {
			if (userInfo.get(authProperties.getUserHeader()) != null) {
				customUserDetails = new CustomUserDetails();
				
				customUserDetails.setUsername("" + userInfo.get(authProperties.getUserHeader()));
				customUserDetails.setAccountNonExpired(true);
				customUserDetails.setAccountNonLocked(true);
				customUserDetails.setCredentialsNonExpired(true);
				customUserDetails.setEnabled(true);
				customUserDetails.setAuthorities(new ArrayList<GrantedAuthority>());
				
				if (userInfo.get("givenName") != null) {
					LOGGER.info("givenName from attr: " + userInfo.get("givenName"));
					customUserDetails.setFirstName("" + userInfo.get("givenName"));
				}

				if (userInfo.get("initials") != null) {
					LOGGER.info("initials from attr: " + userInfo.get("initials"));
					customUserDetails.setMiddleName("" + userInfo.get("initials"));
				}

				if (userInfo.get("sn") != null) {
					LOGGER.info("sn from attr: " + userInfo.get("sn"));
					customUserDetails.setLastName("" + userInfo.get("sn"));
				}

				if (userInfo.get("displayName") != null) {
					LOGGER.info("displayName from attr: " + userInfo.get("displayName"));
					customUserDetails.setDisplayName("" + userInfo.get("displayName"));
				}

				if (userInfo.get("mail") != null) {
					LOGGER.info("mail from attr: " + userInfo.get("mail"));
					customUserDetails.setEmailAddress("" + userInfo.get("mail"));
				}
			}
			else {
				LOGGER.error("Authenticated user cannot be loaded");
			}
		} catch (Exception e) {
			LOGGER.error("Exception in mapping user details: " + e);
		}
		return customUserDetails;
	}

	Authentication createSuccessfulAuthentication(CustomUserDetails user) {
		if(user == null) {
			return null;
		}
		PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(
				user, null, user.getAuthorities());
		
		// -- Ping SSO Authentication will fetch the user details from LDAP system. The Authentication Type therefore can be given as LDAP.
		result.setDetails(AuthType.LDAP);
		return result;
	}
}
