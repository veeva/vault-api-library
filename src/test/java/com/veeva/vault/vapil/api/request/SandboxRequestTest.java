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
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@Tag("SandboxRequest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SandboxRequestTest {

	static VaultClient vaultClient;
	static final String SANDBOX_NAME = "VAPIL Test Sandbox";
	static final String SNAPSHOT_NAME = "VAPIL Test Snapshot";
	static final String snapshotApiName = "vapil_test_snapshot__c";
	static final String DOMAIN = "vaultdevsupport.com";
	static int sandboxId;

	@BeforeAll
	static void setup() {
//			Authenticate with Source vault
		vaultClient = VaultClient
				.newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
				.withVaultSessionId("")
				.build();
	}

	@Order(1)
	@DisplayName("Should successfully create a sandbox")
	@Test
	public void createTestSandbox() {
//			Create job to build test sandbox
		JobCreateResponse jobCreateResponse = vaultClient.newRequest(SandboxRequest.class)
				.setSource(SandboxRequest.SandboxSource.VAULT)
				.setSize(SandboxRequest.SandboxSize.SMALL)
				.setRelease(SandboxRequest.ReleaseType.LIMITED)
				.setAddRequester(true)
				.createOrRefreshSandbox("config", DOMAIN, SANDBOX_NAME);

//			Wait til job completes
		int jobId = jobCreateResponse.getJobId();
		Assertions.assertTrue(jobCreateResponse.isSuccessful());
		Assertions.assertNotNull(jobId);

		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));

//			Get the sandbox Vault ID
		SandboxResponse sandboxResponse = vaultClient.newRequest(SandboxRequest.class)
				.retrieveSandboxes();
		Assertions.assertTrue(sandboxResponse.isSuccessful());

		for (SandboxResponse.Sandbox sandbox : sandboxResponse.getData().getActive()) {
			if (sandbox.getName().equals(SANDBOX_NAME)) {
				sandboxId = sandbox.getVaultId();
			}
		}
	}

	@Order(2)
	@DisplayName("Should successfully set sandbox entitlements for the given sandbox")
	@Test
	public void setSandboxEntitlements() {
		SandboxResponse response = vaultClient.newRequest(SandboxRequest.class)
				.setSandboxEntitlements(SANDBOX_NAME, "config", 99, true, 0, SandboxRequest.SandboxSize.SMALL);

		Assertions.assertFalse(response.isSuccessful());
	}

	@Order(3)
	@Test
	@DisplayName("Should successfully retrieve the total number of available and number of in-use sandbox Vaults for the authenticated Vault")
	public void testRetrieveSandboxEntitlements() {
		SandboxEntitlementResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxEntitlements();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Order(4)
	@Test
	@DisplayName("Should successfully retrieve information about the sandbox Vaults for the authenticated Vault")
	public void testRetrieveSandboxes() {
		SandboxResponse response = vaultClient.newRequest(SandboxRequest.class).retrieveSandboxes();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Order(5)
	@Test
	@DisplayName("Should retrieve information about the sandbox for the given Vault ID")
	public void testRetrieveSandboxDetailsById() {
		SandboxDetailsResponse response = vaultClient.newRequest(SandboxRequest.class)
				.retrieveSandboxDetailsById(sandboxId);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@Order(6)
	@DisplayName("Should successfully create a sandbox snapshot")
	public void testCreateSandboxSnapshot() {
		JobCreateResponse jobCreateResponse = vaultClient.newRequest(SandboxRequest.class)
				.createSandboxSnapshot(SANDBOX_NAME, SNAPSHOT_NAME, "Description", false);

		int jobId = jobCreateResponse.getJobId();
		Assertions.assertTrue(jobCreateResponse.isSuccessful());
		Assertions.assertNotNull(jobId);
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
	}

	@Order(7)
	@Test
	@DisplayName("Should successfully return sandbox snapshots managed by the authenticated vault")
	public void testRetrieveSandboxSnapshots() {
		SandboxSnapshotResponse response = vaultClient.newRequest(SandboxRequest.class)
				.retrieveSandboxSnapshots();

		Assertions.assertTrue(response.isSuccessful());
		if (!response.getData().getSnapshots().isEmpty()) {
			for (SandboxSnapshotResponse.Snapshot snapshot : response.getData().getSnapshots()) {
				Assertions.assertNotNull(snapshot.getName());
				Assertions.assertNotNull(snapshot.getApiName());
			}
		}
	}

	@Test
	@Order(8)
	@DisplayName("Should successfully update a sandbox snapshot")
	public void testUpdateSandboxSnapshot() {
		JobCreateResponse jobCreateResponse = vaultClient.newRequest(SandboxRequest.class)
				.updateSandboxSnapshot(snapshotApiName);

		int jobId = jobCreateResponse.getJobId();
		Assertions.assertTrue(jobCreateResponse.isSuccessful());
		Assertions.assertNotNull(jobId);
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
	}

	@Test
	@Order(9)
	@DisplayName("Should successfully upgrade a sandbox snapshot")
	public void testUpgradeSandboxSnapshot() {
		JobCreateResponse response = vaultClient.newRequest(SandboxRequest.class)
				.upgradeSandboxSnapshot(SNAPSHOT_NAME);

		Assertions.assertFalse(response.isSuccessful());
	}

	@Order(10)
	@Test
	@DisplayName("Should successfully change the size of a sandbox")
	public void testChangeSandboxSize() {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.changeSandboxSize("V2V Connection Target", SandboxRequest.SandboxSize.LARGE);

		Assertions.assertTrue(response.isSuccessful());
	}

	@Order(11)
	@Test
	@DisplayName("Should successfully refresh a sandbox from a snapshot")
	public void testRefreshSandboxFromSnapshot() {
		JobCreateResponse jobCreateResponse = vaultClient.newRequest(SandboxRequest.class)
				.refreshSandboxFromSnapshot(sandboxId, snapshotApiName);

		int jobId = jobCreateResponse.getJobId();
		Assertions.assertTrue(jobCreateResponse.isSuccessful());
		Assertions.assertNotNull(jobId);
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
	}

	@Order(12)
	@Test
	@DisplayName("Should successfully delete a sandbox snapshot")
	public void testDeleteSandboxSnapshot() {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.deleteSandboxSnapshot(snapshotApiName);

		Assertions.assertTrue(response.isSuccessful());
	}

	@Order(13)
	@Test
	@DisplayName("Should successfully recalculate the usage values of the sandbox Vaults for the authenticated Vault")
//	This can only be run 3 times in 24 hour period
	public void testRecheckUsageLimit() {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.recheckUsageLimit();

		Assertions.assertTrue(response.isSuccessful());
	}

	@Disabled
	@Test
	public void testBuildProduction(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(SandboxRequest.class)
				.buildProductionVault("12345");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}

	@Disabled
	@Test
	public void testPromoteToProduction(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.promoteToProduction("Test Vault");
		Assertions.assertTrue(response.isSuccessful());
	}

	@Order(16)
	@DisplayName("Should successfully delete a sandbox")
	@Test
	public void deleteTestSandbox() {
		VaultResponse response = vaultClient.newRequest(SandboxRequest.class)
				.deleteSandbox(SANDBOX_NAME);

		Assertions.assertTrue(response.isSuccessful());
	}
}
