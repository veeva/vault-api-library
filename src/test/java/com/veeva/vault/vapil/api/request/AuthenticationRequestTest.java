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
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
}
