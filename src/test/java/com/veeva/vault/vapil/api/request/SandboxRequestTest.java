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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("SandboxRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class SandboxRequestTest {

	@Test
	@DisplayName("Should successfully retrieve the total number of available and number of in-use sandbox Vaults for the authenticated Vault")
	public void testRetrieveSandboxEntitlements(VaultClient vaultClient) {
		SandboxEntitlementResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxEntitlements();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should successfully retrieve information about the sandbox Vaults for the authenticated Vault")
	public void testRetrieveSandboxes(VaultClient vaultClient) {
		SandboxResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxes();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should retrieve information about the sandbox for the given Vault ID")
	public void testRetrieveSandboxDetailsById(VaultClient vaultClient) {
		SandboxDetailsResponse response = vaultClient.newRequest(SandboxRequest.class)
				.retrieveSandboxDetailsById(1001190);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void TestCreateOrRefreshSandbox(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(SandboxRequest.class)
                .setRelease(SandboxRequest.ReleaseType.LIMITED)
                .setAddRequester(true)
                .createOrRefreshSandbox("demo","sameerm.com","ExampleSBX");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}

	@Test
	public void testBuildProduction(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(SandboxRequest.class)
				.buildProductionVault("12345");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}

	@Test
	public void testPromoteToProduction(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.promoteToProduction("Test Vault");
		Assertions.assertTrue(response.isSuccessful());
	}
}
