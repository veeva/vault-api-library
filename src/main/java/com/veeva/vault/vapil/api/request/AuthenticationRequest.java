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
import org.apache.log4j.Logger;
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
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/22.3/#authentication">https://developer.veevavault.com/api/22.3/#authentication</a>
 */
public class AuthenticationRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(AuthenticationRequest.class);

	// API Endpoints
	private static final String URL_API = "";
	private static final String URL_AUTH = "/auth";
	private static final String URL_KEEP_ALIVE = "/keep-alive";
	private static final String URL_VALIDATE_SESSION_USER = "/objects/users/me";

	private static final String URL_RETRIEVE_DELEGATIONS = "/delegation/vaults";

	private static final String URL_INITIATE_DELEGATED_SESSION = "/delegation/login";

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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#retrieve-api-versions' target='_blank'>https://developer.veevavault.com/api/22.3/#retrieve-api-versions</a>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#user-name-and-password' target='_blank'>https://developer.veevavault.com/api/22.3/#user-name-and-password</a>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#user-name-and-password' target='_blank'>https://developer.veevavault.com/api/22.3/#user-name-and-password</a>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#oauth-2-0-openid-connect' target='_blank'>https://developer.veevavault.com/api/22.3/#oauth-2-0-openid-connect</a>
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
	 * Authenticate using Vault Discovery endpoints. First Vault is queried for the user's
	 * authentication method, and if SSO, this method attempts to acquire an OAuth token.
	 * If the user is basic username/password, the simple login method is used.
	 *
	 * @param vaultUserName The user name for authentication
	 * @param password      The user password
	 * @param vaultDNS      The DNS of the vault for which you want to generate a session
	 * @return AuthenticationResponse
	 */
	public AuthenticationResponse loginWithDiscovery(String vaultUserName, String password, String vaultDNS) {
		//hit the discovery endpoint
		DiscoveryResponse discoveryResponse = this.authenticationTypeDiscovery(vaultUserName);

		AuthenticationResponse authenticationResponse = null;
		if (discoveryResponse.isSuccessful()) {
			switch (discoveryResponse.getData().getAuthType()) {
				case "sso":

					try {
						//acquire the OAuth Profile and token endpoint
						DiscoveryResponse.DiscoveryData.AuthProfile authProfile = discoveryResponse
								.getData()
								.getAuthProfiles().get(0);

						String tokenEndpoint = authProfile.getAsMetadata().getTokenEndpoint();

						//if a idp username was supplied, use that instead of the Vault username
						String tokenUserName = idpUserName;
						if (tokenUserName == null) {
							tokenUserName = vaultUserName;
						}

						//get the access token
						String accessToken = getOauthAccessToken(
								tokenEndpoint,
								tokenUserName,
								password,
								authProfile.getAsClientId()
						);

						authenticationResponse = loginOAuth(authProfile.getId(), accessToken, vaultDNS);
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;

				//login using basic username/password
				default:
					authenticationResponse = login(vaultUserName, password, vaultDNS);
			}
		}
		return authenticationResponse;
	}

	/**
	 * Authenticate via standard Vault user name and password
	 * in the user's default Vault.
	 *
	 * @param username The user name for authentication
	 * @return AuthenticationResponse
	 * @vapil.api <pre>
	 * POST login.veevavault.com/auth/discovery</pre>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#session-keep-alive' target='_blank'>https://developer.veevavault.com/api/22.3/#session-keep-alive</a>
	 */
	public VaultResponse sessionKeepAlive() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_KEEP_ALIVE));
		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * Validate Session User
	 * <p>
	 * Given a valid session ID, this request returns information for the currently authenticated user.
	 * If the session ID is not valid, this request returns an INVALID_SESSION_ID error type.
	 * This is similar to a whoami request.
	 *
	 * @return UserRetrieveResponse Vault returns an array of size 1
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/users/me</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#validate-session-user' target='_blank'>https://developer.veevavault.com/api/22.3/#validate-session-user</a>
	 * @vapil.request <pre>
	 * UserRetrieveResponse resp = vaultClient.newRequest(AuthenticationRequest.class).validateSessionUser();</pre>
	 * @vapil.response <pre>
	 * for(UserRetrieveResponse.UserNode userNode : resp.getUsers()) {
	 *   User user = userNode.getUser();
	 *   System.out.println("User name: " + user.getUserName());
	 *   System.out.println("User: " + user.getUserFirstName() + " " + user.getUserLastName());
	 *   System.out.println("Email: " + user.getUserEmail());
	 *   System.out.println("Id: " + user.getId());
	 * }</pre>
	 */
	public UserRetrieveResponse validateSessionUser() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_VALIDATE_SESSION_USER));

		return send(HttpMethod.GET, request, UserRetrieveResponse.class);
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#retrieve-delegations' target='_blank'>https://developer.veevavault.com/api/22.3/#retrieve-delegations</a>
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
	 *
	 * @return InitiateDelegatedSessionResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/delegation/login</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.3/#retrieve-delegations' target='_blank'>https://developer.veevavault.com/api/22.3/#retrieve-delegations</a>
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
