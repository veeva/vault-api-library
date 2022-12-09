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
import com.veeva.vault.vapil.api.client.VaultClientBuilder;
import com.veeva.vault.vapil.api.client.VaultClientId;
import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.model.response.DelegationsResponse;
import com.veeva.vault.vapil.api.model.response.DelegatedSessionResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("Authentication")
@ExtendWith(VaultClientParameterResolver.class)
public class AuthenticationRequestTest {

	private static VaultClientId vaultClientId;
	@BeforeAll
	static void beforeAll() {
		TestProperties prop = new TestProperties();
		vaultClientId = new VaultClientId(prop.getClientIdCompany(),
				prop.getClientIdOrganization(),
				prop.getClientIdTeam(),
				prop.isClient(),
				"AuthenticationTest");
	}
	
	@Test
	public void testAuthentication() {
		TestProperties prop = new TestProperties();
		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS(prop.getVaultDNS())
				.withVaultUsername(prop.getVaultUsername())
				.withVaultPassword(prop.getVaultPassword())
				.withVaultClientId(vaultClientId)
				.build();


		AuthenticationResponse response = vaultClient.getAuthenticationResponse();

		Assertions.assertNotNull(response.getResponseStatus());
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testBadAuthentication() {
		TestProperties prop = new TestProperties();
		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS(prop.getVaultDNS())
				.withVaultUsername(prop.getVaultUsername())
				.withVaultPassword("badpassword")
				.withVaultClientId(vaultClientId)
				.build();

		AuthenticationResponse response = vaultClient.getAuthenticationResponse();
		Assertions.assertFalse(vaultClient.hasSessionId());
		Assertions.assertFalse(response.isSuccessful());
	}
	
	@Test
	public void testExistingSession() {
		TestProperties prop = new TestProperties();

		VaultClient vaultClient = VaultClientBuilder
				.newClientBuilder(VaultClient.AuthenticationType.BASIC)
				.withVaultDNS(prop.getVaultDNS())
				.withVaultSessionId(prop.getSessionId())
				.withVaultClientId(vaultClientId)
				.build();

		AuthenticationResponse response = vaultClient.getAuthenticationResponse();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testKeepAlive(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(AuthenticationRequest.class)
						.sessionKeepAlive();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Nested
	@DisplayName("Test Delegate Session")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class testDelegateSession {

		@Test
		@DisplayName("Test retrieve delegations ")
		@Order(1)
		public void testRetrieveDelegations(VaultClient vaultClient) {
			DelegationsResponse response = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();

			System.out.println("Response Status: " + response.getResponseStatus());
			System.out.println("Response Message: " + response.getResponse());
			assertTrue(response.isSuccessful());

			for (DelegationsResponse.DelegatedVault delegatedVault : response.getDelegatedVaults()) {
				System.out.println("Id: " + delegatedVault.getId());
				System.out.println("Name: " + delegatedVault.getName());
				System.out.println("DNS: " + delegatedVault.getDns());
				System.out.println("Delegator user Id: " + delegatedVault.getDelegatorUserId());
			}
		}
		@Test
		@DisplayName("Test initiate delegated session ")
		@Order(2)
		public void testInitiateDelegatedSession(VaultClient vaultClient) {
//			Step 1: Get the Vault Id and Delegator User Id
			DelegationsResponse delegationsResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();
			assertTrue(delegationsResponse.isSuccessful());

			DelegationsResponse.DelegatedVault delegatedVault = delegationsResponse.getDelegatedVaults().get(0);
			int vaultId = delegatedVault.getId();
			String delegatorUserId = delegatedVault.getDelegatorUserId();

			System.out.println("Vault Id: " + vaultId);
			System.out.println("Delegator user Id: " + delegatorUserId);

//			Step 2: Initiate a Delegated Session
			DelegatedSessionResponse delegatedSessionResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.initiateDelegatedSession(vaultId, delegatorUserId);

			System.out.println("Response Status: " + delegatedSessionResponse.getResponseStatus());
			System.out.println("Response Message: " + delegatedSessionResponse.getResponse());
			assertTrue(delegatedSessionResponse.isSuccessful());

			System.out.println("Delegated session Id: " + delegatedSessionResponse.getDelegatedSessionId());
		}

		@Test
		@DisplayName("Test building vault client with delegate session Id")
		@Order(3)
		public void testVaultClientBuildWithDelegateSessionId(VaultClient vaultClient) {
//			Step 1: Get the Vault Id and Delegator User Id
			DelegationsResponse delegationsResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.retrieveDelegations();
			assertTrue(delegationsResponse.isSuccessful());

			DelegationsResponse.DelegatedVault delegatedVault = delegationsResponse.getDelegatedVaults().get(0);
			int vaultId = delegatedVault.getId();
			String delegatorUserId = delegatedVault.getDelegatorUserId();

			System.out.println("Vault Id: " + vaultId);
			System.out.println("Delegator user Id: " + delegatorUserId);

//			Step 2: Initiate a Delegated Session
			DelegatedSessionResponse delegatedSessionResponse = vaultClient.newRequest(AuthenticationRequest.class)
					.initiateDelegatedSession(vaultId, delegatorUserId);

			System.out.println("Response Status: " + delegatedSessionResponse.getResponseStatus());
			System.out.println("Response Message: " + delegatedSessionResponse.getResponse());
			assertTrue(delegatedSessionResponse.isSuccessful());

			System.out.println("Delegated session Id: " + delegatedSessionResponse.getDelegatedSessionId());
			String delegatedSessionId = delegatedSessionResponse.getDelegatedSessionId();

//			Step 3: Verify client build with delegated session Id
			VaultClient delegateVaultClient = VaultClientBuilder
					.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
					.withVaultDNS(vaultClient.getVaultDNS())
					.withVaultClientId(vaultClient.getVaultClientId())
					.withVaultSessionId(delegatedSessionId)
					.build();

			assertTrue(delegateVaultClient.validateSession());

			System.out.println("Original Session Id: " + vaultClient.getSessionId());
			System.out.println("Delegate Session Id: " + delegateVaultClient.getSessionId());

		}
	}


}
