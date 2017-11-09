package com.capitalone.dashboard.auth;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.AuthType;
import com.google.common.collect.Lists;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthProperties.class);
	
	private Long expirationTime;
	private String secret;
	private String ldapUserDnPattern;
	private String ldapServerUrl;
	private List<AuthType> authenticationProviders = Lists.newArrayList();

	private String adDomain;
	private String adRootDn;
	private String adUrl;

	private String ldapBindUser;
	private String ldapBindPass;
	
	// -- Ping SSO properties
	private String useVerboseErrorMessages;
	private String httpOnly;
	private String secureCookie;
	private String cookiePath;
	private String tokenRenewuntil;
	private String tokenNotbeforeTolerance;
	private String cookieDomain;
	private String password;
	private String tokenName;
	private String useCookie;
	private String cipherSuite;
	private String useSunjce;
	private String sessionCookie;
	private int tokenLifetime;
	private String obfuscatePassword;
	//-- end Ping SSO properties
	
	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public Long getExpirationTime() {
		return expirationTime;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public String getLdapUserDnPattern() {
		return ldapUserDnPattern;
	}

	public void setLdapUserDnPattern(String ldapUserDnPattern) {
		this.ldapUserDnPattern = ldapUserDnPattern;
	}

	public String getLdapServerUrl() {
		return ldapServerUrl;
	}

	public void setLdapServerUrl(String ldapServerUrl) {
		this.ldapServerUrl = ldapServerUrl;
	}
	
    public List<AuthType> getAuthenticationProviders() {
        return authenticationProviders;
    }

    public void setAuthenticationProviders(List<AuthType> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

	public String getAdDomain() {
		return adDomain;
	}

	public void setAdDomain(String adDomain) {
		this.adDomain = adDomain;
	}

	public String getAdRootDn() {
		return adRootDn;
	}

	public void setAdRootDn(String adRootDn) {
		this.adRootDn = adRootDn;
	}
	
    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

	public String getLdapBindUser() {
		return ldapBindUser;
	}

	public void setLdapBindUser(String ldapBindUser) {
		this.ldapBindUser = ldapBindUser;
	}

	public String getLdapBindPass() {
		return ldapBindPass;
	}

	public void setLdapBindPass(String ldapBindPass) {
		this.ldapBindPass = ldapBindPass;
	}

	@PostConstruct
	public void applyDefaultsIfNeeded() {
		if (getSecret() == null) {
			LOGGER.info("No JWT secret found in configuration, generating random secret by default.");
			setSecret(UUID.randomUUID().toString().replace("-", ""));			
		}
		
		if (getExpirationTime() == null) {
			LOGGER.info("No JWT expiration time found in configuration, setting to one day.");
			setExpirationTime((long) 1000*60*60*24);
		}
		
		if (CollectionUtils.isEmpty(authenticationProviders)) {
		    authenticationProviders.add(AuthType.STANDARD);
		}
	}
	
	public String getUseVerboseErrorMessages() {
		return useVerboseErrorMessages;
	}

	public void setUseVerboseErrorMessages(String useVerboseErrorMessages) {
		this.useVerboseErrorMessages = useVerboseErrorMessages;
	}

	public String getHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(String httpOnly) {
		this.httpOnly = httpOnly;
	}

	public String getSecureCookie() {
		return secureCookie;
	}

	public void setSecureCookie(String secureCookie) {
		this.secureCookie = secureCookie;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public String getTokenRenewuntil() {
		return tokenRenewuntil;
	}

	public void setTokenRenewuntil(String tokenRenewuntil) {
		this.tokenRenewuntil = tokenRenewuntil;
	}

	public String getTokenNotbeforeTolerance() {
		return tokenNotbeforeTolerance;
	}

	public void setTokenNotbeforeTolerance(String tokenNotbeforeTolerance) {
		this.tokenNotbeforeTolerance = tokenNotbeforeTolerance;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getUseCookie() {
		return useCookie;
	}

	public void setUseCookie(String useCookie) {
		this.useCookie = useCookie;
	}

	public String getCipherSuite() {
		return cipherSuite;
	}

	public void setCipherSuite(String cipherSuite) {
		this.cipherSuite = cipherSuite;
	}

	public String getUseSunjce() {
		return useSunjce;
	}

	public void setUseSunjce(String useSunjce) {
		this.useSunjce = useSunjce;
	}

	public String getSessionCookie() {
		return sessionCookie;
	}

	public void setSessionCookie(String sessionCookie) {
		this.sessionCookie = sessionCookie;
	}

	public int getTokenLifetime() {
		return tokenLifetime;
	}

	public void setTokenLifetime(int tokenLifetime) {
		this.tokenLifetime = tokenLifetime;
	}

	public String getObfuscatePassword() {
		return obfuscatePassword;
	}

	public void setObfuscatePassword(String obfuscatePassword) {
		this.obfuscatePassword = obfuscatePassword;
	}
	
}
