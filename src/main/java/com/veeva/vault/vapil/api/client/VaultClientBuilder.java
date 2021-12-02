/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.client;

import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.request.AuthenticationRequest;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import org.apache.log4j.Logger;

public class VaultClientBuilder {

	private static Logger log = Logger.getLogger(VaultClientBuilder.class);

	private VaultClient.AuthenticationType authenticationType;
	private String idpOauthAccessToken;
	private String idpOauthScope = "openid";
	private String idpUsername;
	private String idpPassword;
	private boolean logApiErrors = true;
	private Integer httpTimeout = null;
	private String vaultDNS;
	private String vaultPassword;
	private String vaultUsername;
	private String vaultSessionId;
	private String vaultOauthClientId;
	private String vaultOauthProfileId;
	private VaultClientId vaultClientId;
	private boolean validateSession = true;

	protected VaultClientBuilder () {}

	/**
	 * Create a new instance of a {@link VaultClientBuilder}
	 *
	 * @param authenticationType {@link VaultClient.AuthenticationType}
	 * @return {@link VaultClientBuilder}
	 */
	public static VaultClientBuilder newClientBuilder(VaultClient.AuthenticationType authenticationType) {
		VaultClientBuilder result = new VaultClientBuilder()
				.withAuthenticationType(authenticationType);
		return result;
	}

	/**
	 * Build a Vault API Client with the configured settings from the builder.
	 * Throws {@link IllegalArgumentException} when required parameters are missing.
	 *
	 * @return {@link VaultClient}
	 */
	public VaultClient build() throws IllegalArgumentException {
		if (authenticationType == null) {
			log.error("Vault Authentication Type is required");
			throw new IllegalArgumentException("Vault Authentication Type is required");
		}

		if (vaultDNS == null || vaultDNS.isEmpty()) {
			log.error("Vault DNS is required");
			throw new IllegalArgumentException("Vault DNS is required");
		}

		if (vaultClientId == null || vaultClientId.getClientId().isEmpty()) {
			log.error("Vault Client Id is required");
			throw new IllegalArgumentException("Vault Client Id is required");
		}

		//create a new client and set the required fields
		VaultClient vaultClient = new VaultClient();
		vaultClient.setVaultClientId(vaultClientId);
		vaultClient.setVaultDNS(vaultDNS);
		vaultClient.setLogApiErrors(logApiErrors);

		//if the user specified an http timeout, then set the global time out
		if (httpTimeout != null) {
			HttpRequestConnector.setGlobalTimeout(httpTimeout);
		}

		//create a generic auth request and response
		AuthenticationRequest authRequest = vaultClient.newRequest(AuthenticationRequest.class);
		authRequest.setValidateDNS(validateSession);
		AuthenticationResponse authResponse = null;

		switch (authenticationType) {
			case BASIC:
				if (vaultUsername == null || vaultUsername.isEmpty()) {
					log.error("Vault user name is required");
					throw new IllegalArgumentException("Vault user name is required");
				}
				if (vaultPassword == null || vaultPassword.isEmpty()) {
					log.error("Vault password is required");
					throw new IllegalArgumentException("Vault password is required");
				}
				vaultClient.setUsername(vaultUsername);
				vaultClient.setPassword(vaultPassword);

				authResponse = authRequest.login(vaultUsername, vaultPassword, vaultDNS);
				vaultClient.setAuthenticationResponse(authResponse);
				break;

			case OAUTH_ACCESS_TOKEN:
				if (vaultOauthProfileId == null || vaultOauthProfileId.isEmpty()) {
					log.error("Vault OAuth Profile Id is required");
					throw new IllegalArgumentException("Vault OAuth Profile Id is required");
				}
				if (idpOauthAccessToken == null || idpOauthAccessToken.isEmpty()) {
					log.error("IDP OAuth Access Token is required");
					throw new IllegalArgumentException("IDP OAuth Access Token is required");
				}
				authRequest.setIdpOAuthScope(idpOauthScope); //always pass on oauth scope
				if (vaultOauthClientId != null && !vaultOauthClientId.isEmpty()) {
					authRequest.setVaultOAuthClientId(vaultOauthClientId);
				}
				authResponse = authRequest.loginOAuth(vaultOauthProfileId, idpOauthAccessToken, vaultDNS);

				vaultClient.setAuthenticationResponse(authResponse);
				break;

			case OAUTH_DISCOVERY:
				if (vaultUsername == null || vaultUsername.isEmpty()) {
					log.error("Vault user name is required");
					throw new IllegalArgumentException("Vault user name is required");
				}
				if (idpPassword == null || idpPassword.isEmpty()) {
					log.error("IDP password is required");
					throw new IllegalArgumentException("IDP password is required");
				}
				authRequest.setIdpOAuthScope(idpOauthScope); //always pass on oauth scope
				if (idpUsername != null && !idpPassword.isEmpty()) {
					authRequest.setIdpUserName(idpUsername);
				}
				if (vaultOauthClientId != null && !vaultOauthClientId.isEmpty()) {
					authRequest.setVaultOAuthClientId(vaultOauthClientId);
				}
				authResponse = authRequest.loginWithDiscovery(vaultUsername, idpPassword, vaultDNS);
				vaultClient.setAuthenticationResponse(authResponse);
				break;
			case SESSION_ID:
				if (vaultSessionId == null || vaultSessionId.isEmpty()) {
					log.error("Vault session ID is required");
					throw new IllegalArgumentException("Vault session ID is required");
				}

				authResponse = new AuthenticationResponse();
				authResponse.setSessionId(vaultSessionId);
				vaultClient.setAuthenticationResponse(authResponse);
				if (validateSession) {
					vaultClient.validateSession();
				}
				break;
		}
		return vaultClient;
	}

	/**
	 * Initialize with a specific auth types
	 *
	 * @param authenticationType {@link VaultClient.AuthenticationType}
	 * @return {@link VaultClientBuilder}
	 */
	private VaultClientBuilder withAuthenticationType(VaultClient.AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
		return this;
	}

	/**
	 * Log errors from the Vault API responses
	 *
	 * @param enabled Enable api error logging. default = true
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withApiErrorLogging(boolean enabled) {
		this.logApiErrors = enabled;
		return this;
	}

	/**
	 * Set the http timeout for the Vault Client. Default = 60 minutes.
	 * <p>&nbsp;</p>
	 * Can only be set before any and all HTTP calls are first executed including authentication
	 *
	 * @param minutes Number of minutes before http timeout occurs
	 * @return VaultClientBuilder
	 */
	public VaultClientBuilder withHttpTimeout(int minutes) {
		this.httpTimeout = minutes;
		return this;
	}

	/**
	 * Initialize with an Idp Oauth Access Token.
	 * <p>&nbsp;</p>
	 * Required for OAuth with Token
	 *
	 * @param idpOauthAccessToken Idp Oauth Access Token
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withIdpOauthAccessToken(String idpOauthAccessToken) {
		this.idpOauthAccessToken = idpOauthAccessToken;
		return this;
	}

	/**
	 * Initialize with an Idp Oauth Access Token.
	 * <p>&nbsp;</p>
	 * Optional for OAuth with Token. Default = "openid"
	 *
	 * @param idpOauthScope Idp Oauth Scope
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withIdpOauthScope(String idpOauthScope) {
		this.idpOauthScope = idpOauthScope;
		return this;
	}

	/**
	 * Initialize with a user's Idp Oauth Password.
	 * <p>&nbsp;</p>
	 * Required for OAuth with Discovery
	 *
	 * @param idpPassword Idp Oauth Password
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withIdpPassword(String idpPassword) {
		this.idpPassword = idpPassword;
		return this;
	}

	/**
	 * Initialize with a user's Idp Oauth user name.
	 * <p>&nbsp;</p>
	 * Required for OAuth with Discovery when Vault and Idp user name do not match
	 *
	 * @param idpUsername Idp Oauth user name
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withIdpUsername(String idpUsername) {
		this.idpUsername = idpUsername;
		return this;
	}

	/**
	 * Validate sessionId and vaultDNS after authentication
	 *
	 * @param enabled Enable session and vaultDNS validation. default = true
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withValidation(boolean enabled) {
		this.validateSession = enabled;
		return this;
	}

	/**
	 * Initialize with a Vault Client Id.
	 * <p>&nbsp;</p>s
	 * Always Required
	 *
	 * @param vaultClientId Vault Client Id (ex. verteobiotech-vault-quality-server-myapp)
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultClientId(VaultClientId vaultClientId) {
		this.vaultClientId = vaultClientId;
		return this;
	}

	/**
	 * Initialize with a specific Vault DNS.
	 * <p>&nbsp;</p>
	 * Always Required
	 *
	 * @param vaultDNS Vault DNS
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultDNS(String vaultDNS) {
		this.vaultDNS = vaultDNS;
		return this;
	}

	/**
	 * Initialize with the OAuth Client Id configured in Vault OAuth Profiles.
	 * <p>&nbsp;</p>
	 * Required for OAuth
	 *
	 * @param vaultOauthClientId Vault Oauth Client Id
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultOauthClientId(String vaultOauthClientId) {
		this.vaultOauthClientId = vaultOauthClientId;
		return this;
	}

	/**
	 * Initialize with the OAuth Profile Id configured in Vault OAuth Profiles.
	 * <p>&nbsp;</p>
	 * Required for OAuth
	 *
	 * @param vaultOauthProfileId Vault Oauth Profile Id
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultOauthProfileId(String vaultOauthProfileId) {
		this.vaultOauthProfileId = vaultOauthProfileId;
		return this;
	}

	/**
	 * Initialize with a user's Vault password.
	 * <p>&nbsp;</p>
	 * Required for Basic Auth
	 *
	 * @param vaultPassword Vault password
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultPassword(String vaultPassword) {
		this.vaultPassword = vaultPassword;
		return this;
	}

	/**
	 * Initialize with an existing Vault sessionId
	 *
	 * @param vaultSessionId Vault session ID
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultSessionId(String vaultSessionId) {
		this.vaultSessionId = vaultSessionId;
		return this;
	}

	/**
	 * Initialize with a user's Vault user name.
	 * <p>&nbsp;</p>
	 * Required for Basic Auth
	 *
	 * @param vaultUsername Vault user name
	 * @return {@link VaultClientBuilder}
	 */
	public VaultClientBuilder withVaultUsername(String vaultUsername) {
		this.vaultUsername = vaultUsername;
		return this;
	}
}
