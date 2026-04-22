/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpResponseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Authenticate to Vault using standard username/password, OAuth,
 * or Salesforce delegated authentication. Successful connections
 * return an AuthenticationResponse, which stores the Vault session ID.
 * <p>
 * Note that the VaultClient automatically performs Authentication requests
 * to establish the Vault session.
 *
 * @vapil.apicoverage <a href="https://general.veevavault.dev/vault-api/api-reference/26.1/authentication">https://general.veevavault.dev/vault-api/api-reference/26.1/authentication</a>
 */
public class AuthenticationRequest extends VaultRequest<AuthenticationRequest> {
	private static Logger log = LoggerFactory.getLogger(AuthenticationRequest.class);

	// API Endpoints
	private static final String URL_API = "";
	private static final String URL_AUTH = "/auth";
	private static final String URL_KEEP_ALIVE = "/keep-alive";
	private static final String URL_RETRIEVE_DELEGATIONS = "/delegation/vaults";
	private static final String URL_INITIATE_DELEGATED_SESSION = "/delegation/login";
	private static final String URL_END_SESSION = "/session";

	/**
	 * Value = {@value #URL_DISCOVERY}
	 */
	private static final String URL_DISCOVERY = "https://login.veevavault.com/auth/discovery";

	/**
	 * Value = {@value #URL_OAUTH}
	 */
	private static final String URL_OAUTH = "https://login.veevavault.com/auth/oauth/session/{oath_oidc_profile_id}";

	// API Request Parameters
	private String idpOAuthScope = "openid";
	private String idpUserName;
	private String vaultOAuthClientId;
	private boolean validateDNS = true;
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String CLIENT_ID = "client_id";
	private static final String VAULT_DNS = "vaultDNS";
	private static final String VAULT_ID = "vault_id";
	private static final String DELEGATOR_USERID = "delegator_userid";
	private static final String GRANT_TYPE = "grant_type";
	private static final String SCOPE = "scope";

	private AuthenticationRequest() {
	}

	/**
	 * Retrieves api versions supported by the current Vault
	 *
	 * @return ApiVersionResponse
	 * @vapil.api <pre>
	 * GET /api</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/retrieve-api-versions' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/retrieve-api-versions</a>
	 */
	public ApiVersionResponse retrieveApiVersions() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_API));
		return send(HttpMethod.GET, request, ApiVersionResponse.class);
	}

	/**
	 * Authenticate via standard Vault user name and password in the user's default Vault.
	 *
	 * @param userName     The user name for authentication
	 * @param userPassword The user password
	 * @return AuthenticationResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/auth</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/user-name-and-password' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/user-name-and-password</a>
	 */
	public AuthenticationResponse login(String userName, String userPassword) {
		return login(userName, userPassword, null);
	}

	/**
	 * Authenticate via standard Vault user name and password
	 * in a specific Vault Domain.
	 *
	 * @param username The user name for authentication
	 * @param password The user password
	 * @param vaultDNS The DNS of the vault for which you want to generate a session
	 * @return AuthenticationResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/auth</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/user-name-and-password' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/user-name-and-password</a>
	 */
	public AuthenticationResponse login(String username, String password, String vaultDNS) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_AUTH));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addBodyParam(USERNAME, username);
		request.addBodyParam(PASSWORD, password);
		request.addBodyParam(VAULT_DNS, vaultDNS);

		AuthenticationResponse authenticationResponse = send(HttpMethod.POST, request, AuthenticationResponse.class);
		return validateLoginResponse(authenticationResponse);
	}

	/**
	 * Authenticate your account using OAuth 2.0 / Open ID Connect token to obtain a Vault session ID.
	 * Learn more about OAuth 2.0 / Open ID Connect in Vault Help.
	 * <p>
	 * When requesting a sessionId, Vault allows the ability for Oauth2/OIDC client applications
	 * to pass the client_id with the request. Vault uses this client_id when talking
	 * with the introspection endpoint at the authorization server to validate
	 * that the access_token presented by the application is valid.
	 * Learn more about Client ID in the REST API Documentation.
	 *
	 * @param oauthOidcProfileId The ID of your OAuth2.0 / Open ID Connect profile.
	 * @param accessToken        OAuth Access Token (access_token)
	 * @param vaultDNS           The DNS of the vault for which you want to generate a session
	 * @return AuthenticationResponse
	 * @vapil.api <pre>
	 * POST login.veevavault.com/auth/oauth/session/{oath_oidc_profile_id}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/oauth-20-openid-connect' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/oauth-20-openid-connect</a>
	 */
	public AuthenticationResponse loginOAuth(String oauthOidcProfileId, String accessToken, String vaultDNS) {
		String url = URL_OAUTH;
		url = url.replace("{oath_oidc_profile_id}", oauthOidcProfileId);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addHeaderParam("Authorization", "Bearer " + accessToken);

		if (vaultOAuthClientId != null && !vaultOAuthClientId.isEmpty())
			request.addBodyParam(CLIENT_ID, vaultOAuthClientId);

		if (vaultDNS != null && !vaultDNS.isEmpty())
			request.addBodyParam(VAULT_DNS, vaultDNS);

		AuthenticationResponse authenticationResponse = send(HttpMethod.POST, request, AuthenticationResponse.class);
		return validateLoginResponse(authenticationResponse);
	}


	/**
	 * Discover the authentication type of a user. With this API,
	 * applications can dynamically adjust the login requirements per user,
	 * and support either username/password or OAuth2.0 / OpenID Connect authentication schemes.
	 * <p>
	 * Create an unauthenticated Vault Client to call this endpoint.
	 * @param username The user name for authentication
	 * @return DiscoveryResponse
	 * @vapil.api <pre>
	 * POST login.veevavault.com/auth/discovery</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/authentication-type-discovery' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/authentication-type-discovery</a>
	 * @vapil.request <pre>
	 * VaultClient vaultClient = VaultClientBuilder
	 * 				.newClientBuilder(VaultClient.AuthenticationType.NO_AUTH)
	 * 				.withVaultClientId(vaultClientId)
	 * 				.build();
	 *
	 * DiscoveryResponse response = vaultClient.newRequest(AuthenticationRequest.class)
	 * 				.setVaultOAuthClientId("OAuthClientId")
	 * 				.authenticationTypeDiscovery("username@cholecap.com");
     * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Auth Type: " + response.getData().getAuthType());
	 *
	 * for (DiscoveryResponse.DiscoveryData.AuthProfile authProfile : response.getData().getAuthProfiles()) {
	 * 		System.out.println("ID: " + authProfile.getId());
	 * 		System.out.println("Label: " + authProfile.getLabel());
	 * 		System.out.println("AS Client ID: " + authProfile.getAsClientId());
	 * 		System.out.println("*** AS Metadata ***");
	 * 		System.out.println("    Token Endpoint: " + authProfile.getAsMetadata().getTokenEndpoint());
	 * }
	 * </pre>
	 */
	public DiscoveryResponse authenticationTypeDiscovery(String username) {
		HttpRequestConnector request = new HttpRequestConnector(URL_DISCOVERY);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addBodyParam(USERNAME, username);
		if (vaultOAuthClientId != null)
			request.addBodyParam(CLIENT_ID, vaultOAuthClientId);

		return send(HttpMethod.POST, request, DiscoveryResponse.class);
	}

	/**
	 * Session Keep Alive
	 * <p>
	 * Developers are now able to keep a Vault API Session alive with a light-weight endpoint that returns SUCCESS
	 * when a valid Session Id is supplied. If an invalid Session Id is supplied, Vault returns INVALID_SESSION_ID.
	 * Vault always enforces a 48-hour maximum session duration even when used with the Session Keep Alive.
	 * </p>
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/keep-alive</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/session-keep-alive' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/session-keep-alive</a>
     * @vapil.request <pre>
     * VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
     *      .sessionKeepAlive();
     * }
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Response Status:" + vaultClient.getAuthenticationResponse().getResponseStatus());
     * </pre>
	 */
	public VaultResponse sessionKeepAlive() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_KEEP_ALIVE));
		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * Retrieve delegations
	 * <p>
	 * Retrieve Vaults where the currently authenticated user has delegate access.
	 * You can then use this information to Initiate a Delegated Session.
	 *
	 * @return DelegationsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/delegation/vaults</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/delegated-access/retrieve-delegations' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/delegated-access/retrieve-delegations</a>
	 * @vapil.request <pre>
	 * DelegationsResponse response = vaultClient.newRequest(AuthenticationRequest.class)
	 * 				.retrieveDelegations();</pre>
	 * @vapil.response <pre>
	 * for (DelegationsResponse.DelegatedVault delegatedVault : response.getDelegatedVaults()) {
	 *   System.out.println("Id: " + delegatedVault.getId());
	 * 	 System.out.println("Name: " + delegatedVault.getName());
	 * 	 System.out.println("DNS: " + delegatedVault.getDns());
	 * 	 System.out.println("Delegator user Id: " + delegatedVault.getDelegatorUserId());
	 * }</pre>
	 */
	public DelegationsResponse retrieveDelegations() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_RETRIEVE_DELEGATIONS));

		return send(HttpMethod.GET, request, DelegationsResponse.class);
	}

	/**
	 * Initiate Delegated Session
	 * <p>
	 * Generate a delegated session ID.
	 * This allows you to call the Vault REST API on behalf of a user who granted you delegate access.
	 * @param vaultId vault id
	 * @param delegatorUserId delegator id
	 * @return InitiateDelegatedSessionResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/delegation/login</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/delegated-access/initiate-delegated-session' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/delegated-access/initiate-delegated-session</a>
	 * @vapil.request <pre>
	 * InitiateDelegatedSessionResponse response = vaultClient.newRequest(AuthenticationRequest.class)
	 * 				.initiateDelegatedSession(vaultId, delegatorUserId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Delegated session Id: " + response.getDelegatedSessionId());
	 * </pre>
	 */
	public DelegatedSessionResponse initiateDelegatedSession(int vaultId, String delegatorUserId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_INITIATE_DELEGATED_SESSION));

		request.addBodyParam(VAULT_ID, vaultId);
		request.addBodyParam(DELEGATOR_USERID, delegatorUserId);

		return send(HttpMethod.POST, request, DelegatedSessionResponse.class);
	}

	/**
	 * End Session
	 * <p>
	 * Given an active sessionId, inactivate an API session.
	 * If a user has multiple active sessions, inactivating one session does not inactivate all sessions for that user.
	 * Each session has its own unique sessionId
	 * </p>
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/session</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/end-session' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/authentication/end-session</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
	 * 		.endSession();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse endSession() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_END_SESSION));
		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/*
	 *
	 * Private methods
	 *
	 */

	private String getOauthAccessToken(String tokenEndpoint,
									   String username,
									   String password,
									   String asClientId) {

		try {
			HttpRequestConnector request = new HttpRequestConnector(tokenEndpoint);
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
			request.addBodyParam(GRANT_TYPE, "password");
			request.addBodyParam(USERNAME, username);
			request.addBodyParam(PASSWORD, password);
			request.addBodyParam(CLIENT_ID, asClientId);

			if (idpOAuthScope != null) {
				request.addBodyParam(SCOPE, idpOAuthScope);
			}

			HttpResponseConnector response = request.sendPost();
			OauthTokenResponse tokenResponse = getBaseObjectMapper().readValue(response.getResponse(), OauthTokenResponse.class);
			if (tokenResponse != null) {
				return tokenResponse.getAccessToken();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private AuthenticationResponse validateLoginResponse(AuthenticationResponse response) {
		if (response != null && response.isSuccessful()) {
			if (validateDNS) {
				String userSuppliedApiEndpoint = vaultClient.getAPIEndpoint(true);
				Integer authenticatedVaultId = response.getVaultId();
				String responseUrl = null;

				for (AuthenticationResponse.Vault vault : response.getVaultIds()) {
					if (authenticatedVaultId.equals(vault.getId())) {
						responseUrl = vault.getUrl() + "/" + VaultClient.VAULT_API_VERSION;
						if (userSuppliedApiEndpoint.startsWith(responseUrl)) {
							log.info("Authentication succeeded");
							return response;
						}
					}
				}

				AuthenticationResponse failedResponse = new AuthenticationResponse();
				failedResponse.setResponseStatus(VaultResponse.HTTP_RESPONSE_FAILURE);
				failedResponse.setResponse(response.getResponse());
				failedResponse.setResponseMessage("vaultDNS verification failed");
				log.error(failedResponse.getResponseMessage());
				log.error("Response endpoint = " + responseUrl);
				return failedResponse;
			}
			else {
				log.info("Authentication succeeded");
				return response;
			}
		}

		log.error("Authentication failed");
		return response;
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @param vaultOAuthClientId Client Id for the Vault App
	 * @return The Request
	 */
	public AuthenticationRequest setVaultOAuthClientId(String vaultOAuthClientId) {
		this.vaultOAuthClientId = vaultOAuthClientId;
		return this;
	}

	public AuthenticationRequest setIdpUserName(String idpUserName) {
		this.idpUserName = idpUserName;
		return this;
	}

	/**
	 * Sets the scope body param for an OAuth Access Token Call
	 *
	 * @param idpOAuthScope OAuth "scope" body param. Default = "openid"
	 * @return The request
	 */
	public AuthenticationRequest setIdpOAuthScope(String idpOAuthScope) {
		this.idpOAuthScope = idpOAuthScope;
		return this;
	}

	/**
	 * Validate Vault DNS after successful login
	 *
	 * @param validateDNS true/false
	 * @return The request
	 */
	public AuthenticationRequest setValidateDNS(Boolean validateDNS) {
		this.validateDNS = validateDNS;
		return this;
	}
}
