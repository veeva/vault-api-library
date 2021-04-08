/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.SandboxEntitlementResponse;
import com.veeva.vault.vapil.api.model.response.SandboxResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("SandboxRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class SandboxRequestTest {

    @Test
    public void TestRetrieveSandboxEntitlements(VaultClient vaultClient) {
		SandboxEntitlementResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxEntitlements();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void TestRetrieveSandboxes(VaultClient vaultClient) {
		SandboxResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxes();
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
}
