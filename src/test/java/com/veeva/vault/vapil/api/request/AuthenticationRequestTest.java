/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DelegatedSessionResponse;
import com.veeva.vault.vapil.api.model.response.DelegationsResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("AuthenticationRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Authentication request")
public class AuthenticationRequestTest {

	private static final String basicSettingsFilePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "settings_files" + File.separator + "settings_vapil_basic.json";
	private static File basicSettingsFile;
	private static Map<String, String> basicMap ;

	@BeforeAll
	static void setup() {
		basicSettingsFile = FileHelper.getSettingsFile(basicSettingsFilePath);
		JsonNode basicNode = FileHelper.readSettingsFile(basicSettingsFile);
		basicMap = FileHelper.convertJsonNodeToMap(basicNode);
	}
	@Test
	@DisplayName("Should successfully build a client from a valid username and password")
	public void testLoginWithUsernameAndPassword(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
				.login(basicMap.get("vaultUsername"), basicMap.get("vaultPassword"));

		assertTrue(response.isSuccessful());
	}


	@Test
	@DisplayName("Should successfully validate a session user")
	public void testValidateSessionUser(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
				.validateSessionUser();

		assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should successfully refresh a session duration")
	public void testSessionKeepAlive(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
						.sessionKeepAlive();
		assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should successfully retrieve API versions")
	public void testRetrieveApiVersions(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
				.retrieveApiVersions();

		assertTrue(response.isSuccessful());
	}

	@Nested
	@Disabled("Sandbox Snapshots do not retain Delegated User Access Configuration. Config is lost after refresh. Test Manually")
	@DisplayName("Test Delegate Session")
	class testDelegateSession {

		@Test
		@DisplayName("Test retrieve delegations")
		public void testRetrieveDelegations(VaultClient vaultClient) {
			DelegationsResponse response = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();

			assertTrue(response.isSuccessful());
		}
		@Test
		@DisplayName("Test initiate delegated session")
		public void testInitiateDelegatedSession(VaultClient vaultClient) {
//			Step 1: Get the Vault Id and Delegator User Id
			DelegationsResponse delegationsResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();
			assertTrue(delegationsResponse.isSuccessful());

			DelegationsResponse.DelegatedVault delegatedVault = delegationsResponse.getDelegatedVaults().get(0);
			int vaultId = delegatedVault.getId();
			String delegatorUserId = delegatedVault.getDelegatorUserId();

//			Step 2: Initiate a Delegated Session
			DelegatedSessionResponse delegatedSessionResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.initiateDelegatedSession(vaultId, delegatorUserId);

			assertTrue(delegatedSessionResponse.isSuccessful());
		}

		@Test
		@DisplayName("Test building vault client with delegate session Id")
		public void testVaultClientBuildWithDelegateSessionId(VaultClient vaultClient) {
//			Step 1: Get the Vault Id and Delegator User Id
			DelegationsResponse delegationsResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();
			assertTrue(delegationsResponse.isSuccessful());

			DelegationsResponse.DelegatedVault delegatedVault = delegationsResponse.getDelegatedVaults().get(0);
			int vaultId = delegatedVault.getId();
			String delegatorUserId = delegatedVault.getDelegatorUserId();

//			Step 2: Initiate a Delegated Session
			DelegatedSessionResponse delegatedSessionResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.initiateDelegatedSession(vaultId, delegatorUserId);

			assertTrue(delegatedSessionResponse.isSuccessful());
			String delegatedSessionId = delegatedSessionResponse.getDelegatedSessionId();

//			Step 3: Verify client build with delegated session Id
			VaultClient delegateVaultClient = VaultClient
					.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
					.withVaultDNS(vaultClient.getVaultDNS())
					.withVaultClientId(vaultClient.getVaultClientId())
					.withVaultSessionId(delegatedSessionId)
					.build();

			assertTrue(delegateVaultClient.validateSession());
		}
	}
}
