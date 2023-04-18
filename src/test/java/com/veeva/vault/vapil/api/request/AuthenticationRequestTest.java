/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.model.response.DelegatedSessionResponse;
import com.veeva.vault.vapil.api.model.response.DelegationsResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Authentication")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Authentication request")
public class AuthenticationRequestTest {
	@BeforeAll
	static void beforeAll() {
	}
	
	@Test
	@DisplayName("Test Client Build")
	public void testAuthentication() {
		TestProperties testProperties = new TestProperties();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultUsername(testProperties.getVaultUsername())
				.withVaultPassword(testProperties.getVaultPassword())
				.withVaultClientId(testProperties.getVaultClientId())
				.build();

		AuthenticationResponse response = vaultClient.getAuthenticationResponse();

		assertNotNull(response.getResponseStatus());
		assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Test client build with incorrect password")
	public void testBadAuthentication() {
		TestProperties testProperties = new TestProperties();
		VaultClient vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS(testProperties.getVaultDNS())
				.withVaultUsername(testProperties.getVaultUsername())
				.withVaultPassword("badpassword")
				.withVaultClientId(testProperties.getVaultClientId())
				.build();

		AuthenticationResponse response = vaultClient.getAuthenticationResponse();
		assertFalse(vaultClient.hasSessionId());
		assertFalse(response.isSuccessful());
	}

	@Test
	@DisplayName("Test login with username and password")
	public void testLoginWithUsernameAndPassword(VaultClient vaultClient) {
		TestProperties testProperties = new TestProperties();

		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
				.login(testProperties.getVaultUsername(), testProperties.getVaultPassword());

		assertTrue(response.isSuccessful());
	}


	@Test
	@DisplayName("Test validating a session user")
	public void testValidateSessionUser(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
				.validateSessionUser();

		assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Test Session Keep Alive")
	public void testSessionKeepAlive(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
						.sessionKeepAlive();
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

	@Test
	@DisplayName("Test retrieve API versions")
	public void testRetrieveApiVersions(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
						.retrieveApiVersions();
		
		assertTrue(response.isSuccessful());
	}


}
