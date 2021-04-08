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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Authenticate to Vault using standard username/password, OAuth,
 * or Salesforce delegated authentication. Successful connections
 * return an AuthenticationResponse, which stores the Vault session ID.
 * <p>
 * Note that the VaultClient automatically performs Authentication requests
 * to establish the Vault session.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/21.1/#authentication">https://developer.veevavault.com/api/21.1/#authentication</a>
 */
public class AuthenticationRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(AuthenticationRequest.class);

	// API Endpoints
	private static final String URL_API = "";
	private static final String URL_AUTH = "/auth";
	private static final String URL_VALIDATE_SESSION_USER = "/objects/users/me";

	/**
	 * Value = {@value #URL_DISCOVERY}
	 */
	private static final String URL_DISCOVERY = "https://login.veevavault.com/auth/discovery";

	/**
	 * Value = {@value #URL_OAUTH}
	 */
	private static final String URL_OAUTH = "https://login.veevavault.com/auth/oauth/session/{oath_oidc_profile_id}";

	// API Request Parameters
	private String idpUserName;
	private String vaultOAuthClientId;
	private boolean validateDNS = true;

	private AuthenticationRequest() {
	}

	/**
	 * Retrieves api versions supported by the current Vault
	 *
	 * @return ApiVersionResponse
	 * @vapil.api <pre>
	 * GET /api</pre>
	 * @vapil.vaultlink (Undocumented Endpoint)
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#user-name-and-password' target='_blank'>https://developer.veevavault.com/api/21.1/#user-name-and-password</a>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#user-name-and-password' target='_blank'>https://developer.veevavault.com/api/21.1/#user-name-and-password</a>
	 */
	public AuthenticationResponse login(String username, String password, String vaultDNS) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_AUTH));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addBodyParam("username", username);
		request.addBodyParam("password", password);
		request.addBodyParam("vaultDNS", vaultDNS);

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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#oauth-2-0-openid-connect' target='_blank'>https://developer.veevavault.com/api/21.1/#oauth-2-0-openid-connect</a>
	 */
	public AuthenticationResponse loginOAuth(String oauthOidcProfileId, String accessToken, String vaultDNS) {
		String url = URL_OAUTH;
		url = url.replace("{oath_oidc_profile_id}", oauthOidcProfileId);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addHeaderParam("Authorization", "Bearer " + accessToken);

		if (vaultOAuthClientId != null && !vaultOAuthClientId.isEmpty())
			request.addBodyParam("client_id", vaultOAuthClientId);

		if (vaultDNS != null && !vaultDNS.isEmpty())
			request.addBodyParam("vaultDNS", vaultDNS);

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

						Map<String, String> formData = new HashMap<>();

						//special case for Azure
						// -replace v2.0 with non version specific
						// -add a resource parameter
						if (tokenEndpoint.toLowerCase().contains("microsoftonline.com")) {
							tokenEndpoint = tokenEndpoint.replace("/v2.0", "");
							formData.put("resource", "https://login.veevavault.com");
						}

						//if a idp username was supplied, use that instead of the Vault username
						String tokenUserName = idpUserName;
						if (tokenUserName == null) {
							tokenUserName = vaultUserName;
						}

						//get the access token
						String accessToken = getOAuthAccessToken(
								tokenEndpoint,
								tokenUserName,
								password,
								authProfile.getAsClientId(),
								formData
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
	 * GET login.veevavault.com/auth/discovery</pre>
	 */
	public DiscoveryResponse authenticationTypeDiscovery(String username) {
		HttpRequestConnector request = new HttpRequestConnector(URL_DISCOVERY);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addQueryParam("username", username);
		if (vaultOAuthClientId != null)
			request.addQueryParam("client_id", vaultOAuthClientId);

		return send(HttpMethod.GET, request, DiscoveryResponse.class);
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#validate-session-user' target='_blank'>https://developer.veevavault.com/api/21.1/#validate-session-user</a>
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

	/*
	 *
	 * Private methods
	 *
	 */

	private String getOAuthAccessToken(String tokenEndpoint,
									   String username,
									   String password,
									   String asClientId,
									   Map<String, String> formData) {

		try {
			HttpRequestConnector request = new HttpRequestConnector(tokenEndpoint);
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
			request.addBodyParam("grant_type", "password");
			request.addBodyParam("scope", "openid");
			request.addBodyParam("username", username);
			request.addBodyParam("password", password);
			request.addBodyParam("client_id", asClientId);

			if (formData != null) {
				for (Map.Entry<String, String> entry : formData.entrySet()) {
					request.addBodyParam(entry.getKey(), entry.getValue());
				}
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

				for (AuthenticationResponse.Vault vault : response.getVaults()) {
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
