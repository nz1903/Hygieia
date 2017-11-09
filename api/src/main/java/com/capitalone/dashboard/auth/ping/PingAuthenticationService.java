package com.capitalone.dashboard.auth.ping;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface PingAuthenticationService {
	
	Authentication getAuthentication(HttpServletRequest request);
}
