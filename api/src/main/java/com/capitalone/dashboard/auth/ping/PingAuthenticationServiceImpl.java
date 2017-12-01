package com.capitalone.dashboard.auth.ping;

import java.util.Collection;
import java.util.HashMap;
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
				Object cookiesHeader = headersMap.get("cookiesheader");
				
				/*int count = 0;
				for(String header : headersMap.keySet()) {
					LOGGER.debug("Header (" + ++count + ".) : " + header + ", value : " + headersMap.get(header));
				}
				LOGGER.debug("cookiesHeader : ==> =====> =======>  " + cookiesHeader);*/
				
				if(cookiesHeader != null) {
					String[] cookieHeaders = cookiesHeader.toString().split("\",\"");
					
					if(cookieHeaders.length > 1) {
						Map<String, String> userInfo = new HashMap<>();
						
						for(int countCookieHeader = 0 ; countCookieHeader < cookieHeaders.length ; countCookieHeader++) {
				            String[] userInfoHeadersVal = cookieHeaders[countCookieHeader].split("\":\"");
				            String[] processUserInfoCookieHeader = {"",""};
				            
				            int processedCookieValueCount = 0;
				            for(String userInfoHeaderVal : userInfoHeadersVal) {
				                if(userInfoHeaderVal.startsWith("{"))
				                    userInfoHeaderVal = userInfoHeaderVal.substring(1);
				                if(userInfoHeaderVal.startsWith("\""))
				                    userInfoHeaderVal = userInfoHeaderVal.substring(1);
				                
				                if(userInfoHeaderVal.endsWith("}"))
				                    userInfoHeaderVal = userInfoHeaderVal.substring(0, userInfoHeaderVal.length()-1);
				                if(userInfoHeaderVal.endsWith("\""))
				                    userInfoHeaderVal = userInfoHeaderVal.substring(0, userInfoHeaderVal.length()-1);
				                
				                if(userInfoHeaderVal.contains("null")) {
				                	userInfoHeaderVal = null;
				                }
				                processUserInfoCookieHeader[processedCookieValueCount] = userInfoHeaderVal;
				                processedCookieValueCount++;
				            }
				            userInfo.put(processUserInfoCookieHeader[0].substring(0, processUserInfoCookieHeader[0].length()-1), processUserInfoCookieHeader[1]);
				        }
						/*LOGGER.debug("userInfo : )==> " + userInfo);*/
						customUserDetails = pingAuthenticationUtil.createUser(userInfo);
					}	
				}
				return pingAuthenticationUtil.createSuccessfulAuthentication(customUserDetails);
			} else {
				LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationDataFromHeaders() :=> userInfo is Null");
			}
		} catch (Exception exception) {
			LOGGER.error("PingAuthenticationServiceImpl.getAuthenticationDataFromHeaders() :=> Exception :"
					+ exception);
		}
		return null;
	}

}
