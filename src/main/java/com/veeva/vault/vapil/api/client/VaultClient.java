/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.ApiVersionResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.AuthenticationRequest;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import org.apache.log4j.Logger;

import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;

/**
 * Base class for all Vault integration calls where a Vault session is established via:<br>
 * 1. Basic authentication using Vault user name and password<br>
 * 2. Existing Vault session, such as a session passed from
 * Vault Web Action/Tab or Vault Job Scheduler<br>
 * 3. OAuth
 * <p>
 * New API requests are created via the "newRequest" method,
 * passing in the request class to instantiate.
 */
public class VaultClient {
	private static Logger log = Logger.getLogger(VaultClient.class);

	/**
	 * The current Vault API Version {@value #VAULT_API_VERSION}. This variable drives the version
	 * used in all API calls.
	 */
	public static final String VAULT_API_VERSION = "v23.2";

	private static final String VAULT_CLIENT_SETTER = "setVaultClient"; // The VaultRequest VaultClient setter
	private static final String URL_LOGIN = "login.veevavault.com"; // The VaultRequest VaultClient setter

	/**
	 * The Vault DNS in format myvault.veevavault.com
	 */
	private String vaultDNS;
	private String username;
	private String password;
	private String vaultClientId;
	private boolean logApiErrors = true;

	private AuthenticationResponse authenticationResponse = null;

	protected VaultClient() {
	}

	/**
	 * Instantiate a new request to the Vault API endpoint.
	 * <p>
	 * The Vault Client Id is required for all new requests.
	 * An error is thrown if no client id is set.
	 *
	 * @param <T>          The request class to instantiate
	 * @param requestClass The request class to instantiate
	 * @return Object of the instantiated class
	 */
	public <T> T newRequest(Class<T> requestClass) {
		T obj = null;

		// vaultClientId is required if this is not authentication
		if (vaultClientId == null) {
			log.error("Unable to create a new request. Vault Client Id is required for all API calls and is not properly set.");
		} else {
			try {
				/*
				 * Get the base constructor and override security to make public.
				 * This allows the Request class constructor to remain private
				 * and ensure immutability (since the only method to instantiate
				 * the Request class is via this newRequest method).
				 */
				Constructor<T> constructor = requestClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				obj = constructor.newInstance();

				// Set the VaultClient for the VaultRequest
				Method method = requestClass.getMethod(VAULT_CLIENT_SETTER, VaultClient.class);

				method.invoke(obj, this);
			} catch (InstantiationException | IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
				log.error(e.getMessage());
			}
		}
		return obj;
	}

	/**
	 * @return Vault DNS in format "myvault.veevavault.com"
	 */
	public String getVaultDNS() {
		return vaultDNS;
	}

	/**
	 * @param vaultDNS Vault DNS in format "myvault.veevavault.com"
	 */
	public void setVaultDNS(String vaultDNS) {
		this.vaultDNS = vaultDNS;
	}

	/**
	 * @return Vault URL in format "https://myvault.veevavault.com"
	 */
	public String getVaultUrl() {
		return "https://" + vaultDNS;
	}

	/**
	 * @return Vault user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return Vault user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return The client id for Vault API calls
	 */
	public String getVaultClientId() {
		return vaultClientId;
	}

	/**
	 * Set the client id for Vault API requests
	 *
	 * @param vaultClientId Vault Client Id
	 */
	protected void setVaultClientId(String vaultClientId) {
		this.vaultClientId = vaultClientId;
	}

	/**
	 * Get the fully formed root API URL consisting of the Vault DNS
	 *
	 * @param includeVersion include version
	 * @return URL for the API endpoint in form https://myvault.com/api/{version}
	 */
	public String getAPIEndpoint(boolean includeVersion) {
		return getAPIEndpoint("", true);
	}

	/**
	 * Get a fully formed API URL consisting of the Vault DNS,
	 * API version, and the API endpoint.
	 *
	 * @param endpoint API endpoint in form "/objects/documents"
	 * @return URL for the API endpoint in form https://myvault.com/api/{version}/objects/documents
	 */
	public String getAPIEndpoint(String endpoint) {
		return getAPIEndpoint(endpoint, true);
	}

	/**
	 * Get a fully formed API URL consisting of the Vault DNS,
	 * API version, and the API endpoint.
	 *
	 * @param endpoint       API endpoint in form "/objects/documents"
	 * @param includeVersion include version in final endpoint
	 * @return URL for the API endpoint in form:
	 * includeVersion(true) - https://myvault.com/api/{version}/objects/documents
	 * includeVersion(false) - https://myvault.com/api/mdl/components
	 */
	public String getAPIEndpoint(String endpoint, boolean includeVersion) {
		if (includeVersion) {
			return getVaultUrl() + "/api/" + VAULT_API_VERSION + endpoint;
		} else {
			return getVaultUrl() + "/api/" + endpoint;
		}
	}

	/**
	 * Get a fully formed API URL consisting of the Vault login URL
	 * and the API endpoint.
	 *
	 * @param endpoint API endpoint in form "/objects/documents"
	 * @return URL for the API endpoint in form https://login.veevavault.com/auth/discovery
	 */
	public String getLoginEndpoint(String endpoint) {
		return URL_LOGIN + endpoint;
	}

	/**
	 * Get a fully formed API URL consisting of the Vault DNS,
	 * API version, and the API endpoint.
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return URL for the API endpoint in form https://myvault.com/api/{version}/objects/documents
	 */
	public String getPaginationEndpoint(String pageUrl) {
		if (pageUrl.startsWith("https://" + vaultDNS))
			return pageUrl;

		if (pageUrl.startsWith("/api/" + VAULT_API_VERSION))
			return getAPIEndpoint(pageUrl.substring(VAULT_API_VERSION.length() + 5), true);

		if (pageUrl.startsWith("/api/"))
			return getAPIEndpoint(pageUrl.substring(5), false);

		return getAPIEndpoint(pageUrl, true);
	}

	public String getHrefEndpoint(String href) {
		if (href.startsWith("https://" + vaultDNS))
			return href;

		if (href.startsWith("/api/" + VAULT_API_VERSION))
			return getAPIEndpoint(href.substring(VAULT_API_VERSION.length() + 5), true);

		if (href.startsWith("/api/"))
			return getAPIEndpoint(href.substring(5), false);

		return getAPIEndpoint(href, true);
	}

	/**
	 * @return The response from an authentication request, which includes
	 * the Vault session id and the vaults that the user has access to.
	 * Note that only the session id is set if VaultConnection was
	 * instantiated with an existing Vault session (since an authentication
	 * call was not performed).
	 */
	public AuthenticationResponse getAuthenticationResponse() {
		return authenticationResponse;
	}

	/**
	 * @return The Vault session id from an Authentication Response
	 */
	public String getSessionId() {
		return authenticationResponse == null ? null : authenticationResponse.getSessionId();
	}

	/**
	 * @return True if the session id has been set
	 */
	public boolean hasSessionId() {
		return authenticationResponse != null && authenticationResponse.getSessionId() != null && !authenticationResponse.getSessionId().isEmpty();
	}

	/**
	 * @return The user id for the authenticated user. Note that this will
	 * return null if VaultConnection was instantiated with an existing
	 * Vault session (since an authentication call was not performed).
	 */
	public String getUserId() {
		return authenticationResponse == null ? null : authenticationResponse.getUserId();
	}

	/**
	 * Sets whether or not to log api errors. Default=true
	 *
	 * @param logApiErrors true/false
	 * @return {@link VaultClient}
	 */
	public VaultClient setLogApiErrors(boolean logApiErrors) {
		this.logApiErrors = logApiErrors;
		return this;
	}

	/**
	 * Indicates whether or not to log api errors. Default=true
	 *
	 * @return true/false
	 */
	public Boolean isLogApiErrorsEnabled() {
		return logApiErrors;
	}


	/**
	 * <p>Type of Authentication with the Vault API</p>
	 * <p>&nbsp;</p>
	 * <p>BASIC = Vault username and password</p>
	 * 	<p>&nbsp;</p>
	 * 	<p>OAUTH_ACCESS_TOKEN = OAuth OpenID Connect with IDP Access Token</p>
	 * 	<p>&nbsp;</p>
	 * 	<p>OAUTH_DISCOVERY = OAuth OpenID Connect with Vault Client Discovery</p>
	 * 	<p>&nbsp;</p>
	 * 	<p>SESSION_ID = Existing Vault session ID</p>
	 *
	 */
	public enum AuthenticationType {
		BASIC("BASIC"),
		OAUTH_ACCESS_TOKEN("OAUTH_ACCESS_TOKEN"),
		OAUTH_DISCOVERY("OAUTH_DISCOVERY"),
		SESSION_ID("SESSION_ID"),
		NO_AUTH("NO_AUTH");


		String typeName;

		AuthenticationType(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}
	}

	protected void setAuthenticationResponse(AuthenticationResponse authenticationResponse) {
		this.authenticationResponse = authenticationResponse;
	}


	/**
	 * Validate the current sessionId. The session must be active and the vaultDNS
	 * from the request and the response must equal. If the session or vaultDNS
	 * is not valid, the current sessionId will be cleared and an error logged.
	 *
	 * @return true/false
	 */
	public boolean validateSession() {
		boolean isValid = false;
		log.info("Validating session");
		AuthenticationResponse authResponse = this.getAuthenticationResponse();
		ApiVersionResponse versionResponse = this.newRequest(AuthenticationRequest.class).retrieveApiVersions();
		if (versionResponse != null && versionResponse.isSuccessful()) {

			//set the generic vault response data
			authResponse.setHeaders(versionResponse.getHeaders());
			authResponse.setResponseStatus(versionResponse.getResponseStatus());
			authResponse.setResponseMessage(versionResponse.getResponseMessage());
			authResponse.setErrors(versionResponse.getErrors());
			authResponse.setUserId(versionResponse.getHeaderVaultUserId());

			if (versionResponse.getValues() != null) {
				String responseUrl = versionResponse.getValues().getVersionUrl(VaultClient.VAULT_API_VERSION);
				isValid = responseUrl.equals(this.getAPIEndpoint(true));
				if (isValid) {
					log.info("Session validation successful");
				}
				else {
					log.error("vaultDNS verification failed");
					log.error("Response endpoint = " + responseUrl);
				}
			}
		}

		if (!isValid) {
			authResponse.setResponseStatus(VaultResponse.HTTP_RESPONSE_FAILURE);
			authResponse.setSessionId(null);
			if (versionResponse != null && versionResponse.hasErrors()) {
				authResponse.setErrors(versionResponse.getErrors());
			}
		}

		return isValid;
	}

	/**
	 * Create a new instance of a {@link Builder}
	 *
	 * @param authenticationType {@link VaultClient.AuthenticationType}
	 * @return {@link Builder}
	 */
	public static Builder newClientBuilder(VaultClient.AuthenticationType authenticationType) {
		Builder result = new Builder()
				.withAuthenticationType(authenticationType);
		return result;
	}

	/**
	 * Create a new instance of a {@link Builder} with client
	 * parameters initialized from a .json file
	 *
	 * <p>
	 * Example
	 * <pre>
	 * File settingsFile = new File("settings.json");
	 *
	 * VaultClient vaultClient = VaultClient
	 * 		.newClientBuilderFromSettings(settingsFile)
	 * 		.build();
	 * </pre>
	 * @param settingsFile settings from a .json file
	 * @return {@link VaultClient.Builder}
	 *
	 */
	public static Builder newClientBuilderFromSettings(File settingsFile) {
		String jsonString = "";

		try {
			jsonString = new String(Files.readAllBytes(Paths.get(settingsFile.getPath())));
		} catch (IOException e) {
			log.error("Failed to load Client Vault Settings; " + e.getMessage());
			throw new IllegalArgumentException("Invalid Vault Client Settings");
		}

		return newClientBuilderFromSettings(jsonString);
	}

	/**
	 * Create a new instance of a {@link Builder} with client
	 * parameters initialized from a JSON string
	 *
	 * <p>
	 * Example
	 * <pre>
	 * String jsonString =
	 * 		"{\n" +
	 * 		"  \"authenticationType\": \"BASIC\",\n" +
	 * 		"  \"idpOauthAccessToken\": \"\",\n" +
	 * 		"  \"idpOauthScope\": \"openid\",\n" +
	 * 		"  \"idpUsername\": \"\",\n" +
	 * 		"  \"idpPassword\": \"\",\n" +
	 * 		"  \"vaultUsername\": \"USERNAME\",\n" +
	 * 		"  \"vaultPassword\": \"PASSWORD\",\n" +
	 * 		"  \"vaultDNS\": \"DNS\",\n" +
	 * 		"  \"vaultSessionId\": \"\",\n" +
	 * 		"  \"vaultClientId\": \"veeva-vault-devsupport-client-vapil\",\n" +
	 * 		"  \"vaultOauthClientId\": \"\",\n" +
	 * 		"  \"vaultOauthProfileId\": \"\",\n" +
	 * 		"  \"logApiErrors\": true,\n" +
	 * 		"  \"httpTimeout\": null,\n" +
	 * 		"  \"validateSession\": true\n" +
	 * 		"}";
	 *
	 * VaultClient vaultClient = VaultClient
	 * 		.newClientBuilderFromSettings(jsonString)
	 * 		.build();
	 * </pre>
	 * @param settingsJson settings in JSON format
	 * @return {@link VaultClient.Builder}
	 *
	 */
	public static Builder newClientBuilderFromSettings(String settingsJson) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			VaultClient.Settings settings = mapper.readValue(settingsJson, VaultClient.Settings.class);

			return newClientBuilderFromSettings(settings);
		} catch (JsonProcessingException e) {
			log.error("Failed to load Client Vault Settings; " + e.getMessage());
			throw new IllegalArgumentException("Invalid Vault Client Settings");
		}
	}

	public static Builder newClientBuilderFromSettings(VaultClient.Settings vaultClientSettings) {
		return new Builder(vaultClientSettings);
	}

	public static class Builder extends VaultModel {

		private static Logger log = Logger.getLogger(Builder.class);

		private VaultClient.Settings settings;

		protected Builder() {
			this.settings = new VaultClient.Settings();
		}

		protected Builder(VaultClient.Settings settings) {
			this.settings = settings;
		}

		/**
		 * Build a Vault API Client with the configured settings from the builder.
		 * Throws {@link IllegalArgumentException} when required parameters are missing.
		 *
		 * @return {@link VaultClient}
		 */
		public VaultClient build() throws IllegalArgumentException {
			if (settings.getAuthenticationType() == null) {
				log.error("Vault Authentication Type is required");
				throw new IllegalArgumentException("Vault Authentication Type is required");
			}

			if ((settings.getAuthenticationType() != AuthenticationType.NO_AUTH) && (settings.getVaultDNS() == null || settings.getVaultDNS().isEmpty())) {
				log.error("Vault DNS is required");
				throw new IllegalArgumentException("Vault DNS is required");
			}

			if (settings.getVaultClientId() == null || settings.getVaultClientId().isEmpty()) {
				log.error("Vault Client ID is required");
				throw new IllegalArgumentException("Vault Client ID is required");
			}

			//create a new client and set the required fields
			VaultClient vaultClient = new VaultClient();
			vaultClient.setVaultClientId(settings.getVaultClientId());
			vaultClient.setVaultDNS(settings.getVaultDNS());
			vaultClient.setLogApiErrors(settings.getLogApiErrors());

			//if the user specified an http timeout, then set the global time out
			if (settings.getHttpTimeout() != null) {
				HttpRequestConnector.setGlobalTimeout(settings.getHttpTimeout());
			}

			//create a generic auth request and response
			AuthenticationRequest authRequest = vaultClient.newRequest(AuthenticationRequest.class);
			if (settings.getAuthenticationType() == AuthenticationType.NO_AUTH) {
				authRequest.setValidateDNS(false);
			} else {
				authRequest.setValidateDNS(settings.getValidateSession());
			}

			AuthenticationResponse authResponse = null;

			switch (settings.getAuthenticationType()) {
				case BASIC:
					if (settings.getVaultUsername() == null || settings.getVaultUsername().isEmpty()) {
						log.error("Vault user name is required");
						throw new IllegalArgumentException("Vault user name is required");
					}
					if (settings.getVaultPassword() == null || settings.getVaultPassword().isEmpty()) {
						log.error("Vault password is required");
						throw new IllegalArgumentException("Vault password is required");
					}
					vaultClient.setUsername(settings.getVaultUsername());
					vaultClient.setPassword(settings.getVaultPassword());

					authResponse = authRequest.login(settings.getVaultUsername(), settings.getVaultPassword(), settings.getVaultDNS());
					vaultClient.setAuthenticationResponse(authResponse);
					break;

				case OAUTH_ACCESS_TOKEN:
					if (settings.getVaultOauthProfileId() == null || settings.getVaultOauthProfileId().isEmpty()) {
						log.error("Vault OAuth Profile Id is required");
						throw new IllegalArgumentException("Vault OAuth Profile Id is required");
					}
					if (settings.getIdpOauthAccessToken() == null || settings.getIdpOauthAccessToken().isEmpty()) {
						log.error("IDP OAuth Access Token is required");
						throw new IllegalArgumentException("IDP OAuth Access Token is required");
					}
					authRequest.setIdpOAuthScope(settings.getIdpOauthScope()); //always pass on oauth scope
					if (settings.getVaultOauthClientId() != null && !settings.getVaultOauthClientId().isEmpty()) {
						authRequest.setVaultOAuthClientId(settings.getVaultOauthClientId());
					}
					authResponse = authRequest.loginOAuth(settings.getVaultOauthProfileId(), settings.getIdpOauthAccessToken(), settings.getVaultDNS());

					vaultClient.setAuthenticationResponse(authResponse);
					break;

				case OAUTH_DISCOVERY:
					if (settings.getVaultUsername() == null || settings.getVaultUsername().isEmpty()) {
						log.error("Vault user name is required");
						throw new IllegalArgumentException("Vault user name is required");
					}
					if (settings.getIdpPassword() == null || settings.getIdpPassword().isEmpty()) {
						log.error("IDP password is required");
						throw new IllegalArgumentException("IDP password is required");
					}
					authRequest.setIdpOAuthScope(settings.getIdpOauthScope()); //always pass on oauth scope
					if (settings.getIdpUsername() != null && !settings.getIdpPassword().isEmpty()) {
						authRequest.setIdpUserName(settings.getIdpUsername());
					}
					if (settings.getVaultOauthClientId() != null && !settings.getVaultOauthClientId().isEmpty()) {
						authRequest.setVaultOAuthClientId(settings.getVaultOauthClientId());
					}
					authResponse = authRequest.loginWithDiscovery(settings.getVaultUsername(), settings.getIdpPassword(), settings.getVaultDNS());
					vaultClient.setAuthenticationResponse(authResponse);
					break;
				case SESSION_ID:
					if (settings.getVaultSessionId() == null || settings.getVaultSessionId().isEmpty()) {
						log.error("Vault session ID is required");
						throw new IllegalArgumentException("Vault session ID is required");
					}

					authResponse = new AuthenticationResponse();
					authResponse.setSessionId(settings.getVaultSessionId());
					vaultClient.setAuthenticationResponse(authResponse);
					if (settings.getValidateSession()) {
						vaultClient.validateSession();
					}
					break;
				case NO_AUTH:
					if (settings.getVaultUsername() != null && !settings.getVaultUsername().isEmpty()) {
						vaultClient.setUsername(settings.getVaultUsername());
					}
					break;
			}
			return vaultClient;
		}

		/**
		 * Initialize with a specific auth types
		 *
		 * @param authenticationType {@link VaultClient.AuthenticationType}
		 * @return {@link Builder}
		 */
		private Builder withAuthenticationType(VaultClient.AuthenticationType authenticationType) {
			this.settings.setAuthenticationType(authenticationType);
			return this;
		}

		/**
		 * Log errors from the Vault API responses
		 *
		 * @param enabled Enable api error logging. default = true
		 * @return {@link Builder}
		 */
		public Builder withApiErrorLogging(boolean enabled) {
			this.settings.setLogApiErrors(enabled);
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
		public Builder withHttpTimeout(int minutes) {
			this.settings.setHttpTimeout(minutes);
			return this;
		}

		/**
		 * Initialize with an Idp Oauth Access Token.
		 * <p>&nbsp;</p>
		 * Required for OAuth with Token
		 *
		 * @param idpOauthAccessToken Idp Oauth Access Token
		 * @return {@link Builder}
		 */
		public Builder withIdpOauthAccessToken(String idpOauthAccessToken) {
			this.settings.setIdpOauthAccessToken(idpOauthAccessToken);
			return this;
		}

		/**
		 * Initialize with an Idp Oauth Access Token.
		 * <p>&nbsp;</p>
		 * Optional for OAuth with Token. Default = "openid"
		 *
		 * @param idpOauthScope Idp Oauth Scope
		 * @return {@link Builder}
		 */
		public Builder withIdpOauthScope(String idpOauthScope) {
			this.settings.setIdpOauthScope(idpOauthScope);
			return this;
		}

		/**
		 * Initialize with a user's Idp Oauth Password.
		 * <p>&nbsp;</p>
		 * Required for OAuth with Discovery
		 *
		 * @param idpPassword Idp Oauth Password
		 * @return {@link Builder}
		 */
		public Builder withIdpPassword(String idpPassword) {
			this.settings.setIdpPassword(idpPassword);
			return this;
		}

		/**
		 * Initialize with a user's Idp Oauth user name.
		 * <p>&nbsp;</p>
		 * Required for OAuth with Discovery when Vault and Idp user name do not match
		 *
		 * @param idpUsername Idp Oauth user name
		 * @return {@link Builder}
		 */
		public Builder withIdpUsername(String idpUsername) {
			this.settings.setIdpUsername(idpUsername);
			return this;
		}

		/**
		 * Validate sessionId and vaultClientSettings.getVaultDNS() after authentication
		 *
		 * @param enabled Enable session and vaultClientSettings.getVaultDNS() validation. default = true
		 * @return {@link Builder}
		 */
		public Builder withValidation(boolean enabled) {
			this.settings.setValidateSession(enabled);
			return this;
		}

		/**
		 * Initialize with a Vault Client Id.
		 * <p>&nbsp;</p>s
		 * Always Required
		 *
		 * @param vaultClientId Vault Client Id (ex. verteobiotech-vault-quality-server-myapp)
		 * @return {@link Builder}
		 */
		public Builder withVaultClientId(String vaultClientId) {
			this.settings.setVaultClientId(vaultClientId);
			return this;
		}

		/**
		 * Initialize with a specific Vault DNS.
		 * <p>&nbsp;</p>
		 * Always Required
		 *
		 * @param vaultDNS Vault DNS
		 * @return {@link Builder}
		 */
		public Builder withVaultDNS(String vaultDNS) {
			this.settings.setVaultDNS(vaultDNS);
			return this;
		}

		/**
		 * Initialize with the OAuth Client Id configured in Vault OAuth Profiles.
		 * <p>&nbsp;</p>
		 * Required for OAuth
		 *
		 * @param vaultOauthClientId Vault Oauth Client Id
		 * @return {@link Builder}
		 */
		public Builder withVaultOauthClientId(String vaultOauthClientId) {
			this.settings.setVaultOauthClientId(vaultOauthClientId);
			return this;
		}

		/**
		 * Initialize with the OAuth Profile Id configured in Vault OAuth Profiles.
		 * <p>&nbsp;</p>
		 * Required for OAuth
		 *
		 * @param vaultOauthProfileId Vault Oauth Profile Id
		 * @return {@link Builder}
		 */
		public Builder withVaultOauthProfileId(String vaultOauthProfileId) {
			this.settings.setVaultOauthProfileId(vaultOauthProfileId);
			return this;
		}

		/**
		 * Initialize with a user's Vault password.
		 * <p>&nbsp;</p>
		 * Required for Basic Auth
		 *
		 * @param vaultPassword Vault password
		 * @return {@link Builder}
		 */
		public Builder withVaultPassword(String vaultPassword) {
			this.settings.setVaultPassword(vaultPassword);
			return this;
		}

		/**
		 * Initialize with an existing Vault sessionId
		 *
		 * @param sessionId Vault session ID
		 * @return {@link Builder}
		 */
		public Builder withVaultSessionId(String sessionId) {
			this.settings.setVaultSessionId(sessionId);
			return this;
		}

		/**
		 * Initialize with a user's Vault user name.
		 * <p>&nbsp;</p>
		 * Required for Basic Auth
		 *
		 * @param vaultUsername Vault user name
		 * @return {@link Builder}
		 */
		public Builder withVaultUsername(String vaultUsername) {
			this.settings.setVaultUsername(vaultUsername);
			return this;
		}
	}

	public static class Settings extends VaultModel {

		//	JSON model for creating a Vault Client Builder from a settings file
		@JsonProperty("authenticationType")
		public VaultClient.AuthenticationType getAuthenticationType() {
			String authenticationType = this.getString("authenticationType");
			if (authenticationType != null) {
				return VaultClient.AuthenticationType.valueOf(authenticationType);
			}
			return null;
		}
		public void setAuthenticationType(String authenticationType) {
			if (authenticationType != null) {
				this.set("authenticationType", authenticationType.toUpperCase());
			} else {
				this.set("authenticationType", null);
			}
		}
		@JsonIgnore
		public void setAuthenticationType(VaultClient.AuthenticationType authenticationType) {
			this.set("authenticationType", authenticationType.toString());
		}

		@JsonProperty("idpOauthAccessToken")
		public String getIdpOauthAccessToken() {
			return this.getString("idpOauthAccessToken");
		}
		public void setIdpOauthAccessToken(String idpOauthAccessToken) {this.set("idpOauthAccessToken", idpOauthAccessToken);}

		@JsonProperty("idpOauthScope")
		public String getIdpOauthScope() {
			String idpOauthScope = this.getString("idpOauthScope");
			if (idpOauthScope != null && !idpOauthScope.isEmpty()) {
				return idpOauthScope;
			}
//			Default to "openid"
			return "openid";
		}

		public void setIdpOauthScope(String idpOauthScope) {
			this.set("idpOauthScope", idpOauthScope);
		}

		@JsonProperty("idpUsername")
		public String getIdpUsername() {
			return this.getString("idpUsername");
		}
		public void setIdpUsername(String idpUsername) {
			this.set("idpUsername", idpUsername);
		}

		@JsonProperty("idpPassword")
		public String getIdpPassword() {
			return this.getString("idpPassword");
		}
		public void setIdpPassword(String idpPassword) {
			this.set("idpPassword", idpPassword);
		}

		@JsonProperty("vaultUsername")
		public String getVaultUsername() {
			return this.getString("vaultUsername");
		}
		public void setVaultUsername(String vaultUsername) {
			this.set("vaultUsername", vaultUsername);
		}

		@JsonProperty("vaultPassword")
		public String getVaultPassword() {
			return this.getString("vaultPassword");
		}
		public void setVaultPassword(String vaultPassword) {
			this.set("vaultPassword", vaultPassword);
		}

		@JsonProperty("vaultClientId")
		public String getVaultClientId() {
			return this.getString("vaultClientId");
		}
		public void setVaultClientId(String vaultClientId) {
			this.set("vaultClientId", vaultClientId);
		}

		@JsonProperty("vaultDNS")
		public String getVaultDNS() {
			return this.getString("vaultDNS");
		}
		public void setVaultDNS(String vaultDNS) {
			this.set("vaultDNS", vaultDNS);
		}

		@JsonProperty("vaultSessionId")
		public String getVaultSessionId() {
			return this.getString("vaultSessionId");
		}
		public void setVaultSessionId(String vaultSessionId) {
			this.set("vaultSessionId", vaultSessionId);
		}


		@JsonProperty("vaultOauthClientId")
		public String getVaultOauthClientId() {
			return this.getString("vaultOauthClientId");
		}
		public void setVaultOauthClientId(String vaultOauthClientId) {this.set("vaultOauthClientId", vaultOauthClientId);}

		@JsonProperty("vaultOauthProfileId")
		public String getVaultOauthProfileId() {
			return this.getString("vaultOauthProfileId");
		}
		public void setVaultOauthProfileId(String vaultOauthProfileId) {this.set("vaultOauthProfileId", vaultOauthProfileId);}

		@JsonProperty("logApiErrors")
		public Boolean getLogApiErrors() {
			Boolean logApiErrors = this.getBoolean("logApiErrors");
			if (logApiErrors != null) {
				return logApiErrors;
			}
			//default to true
			return true;
		}
		public void setLogApiErrors(Boolean logApiErrors) {this.set("logApiErrors", logApiErrors);}

		@JsonProperty("httpTimeout")
		public Integer getHttpTimeout() {
			return this.getInteger("httpTimeout");
		}
		public void setHttpTimeout(Integer httpTimeout) {this.set("httpTimeout", httpTimeout);}

		@JsonProperty("validateSession")
		public Boolean getValidateSession() {
			Boolean validateSession = this.getBoolean("validateSession");
			if (validateSession != null) {
				return validateSession;
			}
			//default to true
			return true;
		}
		public void setValidateSession(Boolean validateSession) {this.set("validateSession", validateSession);}
	}
}
