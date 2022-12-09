/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.veeva.vault.vapil.api.model.response.ApiVersionResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.AuthenticationRequest;
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
	public static final String VAULT_API_VERSION = "v22.3";

	private static final String VAULT_CLIENT_SETTER = "setVaultClient"; // The VaultRequest VaultClient setter
	private static final String URL_LOGIN = "login.veevavault.com"; // The VaultRequest VaultClient setter

	/**
	 * The Vault DNS in format myvault.veevavault.com
	 */
	private String vaultDNS;
	private String username;
	private String password;
	private VaultClientId vaultClientId;
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
		if (vaultClientId == null || !vaultClientId.isValid()) {
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
	 * @return The client_id class for Vault API calls
	 */
	public VaultClientId getVaultClientId() {
		return vaultClientId;
	}

	/**
	 * Set the client id for Vault API requests {@link VaultClientId}
	 *
	 * @param vaultClientId Vault Client Id
	 */
	protected void setVaultClientId(VaultClientId vaultClientId) {
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
		SESSION_ID("SESSION_ID");

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
		}

		return isValid;
	}
}
