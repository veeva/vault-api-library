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
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Tag("AuthenticationRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Authentication request should")
public class AuthenticationRequestTest {

	private static final String BASIC_SETTINGS_FILE_NAME = "settings_vapil_basic.json";
	private static JsonNode basicSettingsNode;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

		File settingsFile = FileHelper.getSettingsFile(BASIC_SETTINGS_FILE_NAME);
		basicSettingsNode = FileHelper.readSettingsFile(settingsFile);
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully authenticate using a valid username and password")
	class TestLogin {
		AuthenticationResponse loginResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			loginResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.login(basicSettingsNode.get("vaultUsername").asText(), basicSettingsNode.get("vaultPassword").asText());

			assertNotNull(loginResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(loginResponse.isSuccessful());
			assertNotNull(loginResponse.getSessionId());
			assertNotNull(loginResponse.getVaultId());
			assertNotNull(loginResponse.getUserId());
			assertNotNull(loginResponse.getVaultIds());
			for (AuthenticationResponse.Vault vault : loginResponse.getVaultIds()) {
				assertNotNull(vault.getId());
				assertNotNull(vault.getName());
				assertNotNull(vault.getUrl());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully refresh a session duration")
	class TestKeepAlive {
		VaultResponse keepAliveResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			keepAliveResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.sessionKeepAlive();

			assertNotNull(keepAliveResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertEquals("SUCCESS", keepAliveResponse.getResponseStatus());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve API versions")
	class TestRetrieveApiVersions {
		ApiVersionResponse retrieveApiVersionsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveApiVersionsResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveApiVersions();

			assertNotNull(retrieveApiVersionsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertEquals("SUCCESS", retrieveApiVersionsResponse.getResponseStatus());
			assertNotNull(retrieveApiVersionsResponse.getValues());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully validate a session user")
	class TestValidateSessionUser {
		VaultResponse validateSessionUserResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			validateSessionUserResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.validateSessionUser();

			assertNotNull(validateSessionUserResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertEquals("SUCCESS", validateSessionUserResponse.getResponseStatus());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully return a valid Discovery Response")
	class TestAuthenticationTypeDiscovery {

		private DiscoveryResponse authenticationTypeDiscoveryResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			VaultClient vaultClient = VaultClient
					.newClientBuilder(VaultClient.AuthenticationType.NO_AUTH)
					.withVaultClientId(basicSettingsNode.get("vaultClientId").asText())
					.build();

			DiscoveryResponse response = vaultClient.newRequest(AuthenticationRequest.class)
					.authenticationTypeDiscovery(basicSettingsNode.get("vaultUsername").asText());

			assertTrue(response != null);
			authenticationTypeDiscoveryResponse = response;
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertEquals("SUCCESS", authenticationTypeDiscoveryResponse.getResponseStatus());
		}
	}


	@Nested
	@Disabled("Sandbox Snapshots do not retain Delegated User Access Configuration. Config is lost after refresh. Test Manually")
	@DisplayName("Test Delegate Session")
	class testDelegateSession {

		@Test
		@DisplayName("Test retrieve delegations")
		public void testRetrieveDelegations() {
			DelegationsResponse response = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();

			assertTrue(response.isSuccessful());
		}

		@Test
		@DisplayName("Test initiate delegated session")
		public void testInitiateDelegatedSession() {
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
		public void testVaultClientBuildWithDelegateSessionId() {
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
