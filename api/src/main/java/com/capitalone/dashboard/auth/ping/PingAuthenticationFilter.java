package com.capitalone.dashboard.auth.ping;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.AuthProperties;
import com.capitalone.dashboard.auth.AuthenticationResultHandler;

@Component
public class PingAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger LOGGER = Logger.getLogger(PingAuthenticationFilter.class);
	
	@Autowired
	private PingAuthenticationService pingAuthenticationService;
	
	@Autowired
	private AuthProperties authProperties;
	
	public PingAuthenticationFilter(String path, AuthenticationManager authManager, AuthenticationResultHandler authenticationResultHandler) {
		super();
		setAuthenticationManager(authManager);
		setAuthenticationSuccessHandler(authenticationResultHandler);
		setFilterProcessesUrl(path);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		Authentication authenticated = null;
		Map<String, String> headersMap = new HashMap<>();
		
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			headersMap.put(headerName, request.getHeader(headerName));
		}
		
		if(request.getHeader(authProperties.getUserHeader()) == null) {
			LOGGER.debug("no header found for user details");
			return null;
		}
		authenticated = pingAuthenticationService.getAuthenticationFromHeaders(headersMap);
    	
    	return authenticated;
	}
}
