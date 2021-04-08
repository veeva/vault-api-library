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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("Job")
@ExtendWith(VaultClientParameterResolver.class)
public class JobRequestTest {

	@Test
	public void testRetrieveJobHistories(VaultClient vaultClient) {
		JobHistoryResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobHistories();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());
		System.out.println(response.getResponse());
	}

	@Test
	public void testRetrieveJobMonitors(VaultClient vaultClient) {
		JobMonitorResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobMonitors();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());
		System.out.println(response.getResponse());
	}

	@Test
	public void testRetrieveJobStats(VaultClient vaultClient) {
		JobStatusResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(1);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotEquals(0, response.getData().getId());
		System.out.println(response.getResponse());
	}

	@Test
	public void testRetrieveJobTasks(VaultClient vaultClient) {
		JobTaskResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobTasks(1);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getTasks());
		Assertions.assertNotEquals(0, response.getTasks().size());
		System.out.println(response.getResponse());
	}
}
