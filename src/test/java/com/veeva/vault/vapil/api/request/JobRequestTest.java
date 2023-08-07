/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Disabled("Jobs are set to Inactive after the sandbox is refreshed. " +
		"Verify that the VAPIL Test Doc Job is active before running tests.")
@Tag("JobRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Job Request should")
public class JobRequestTest {

	private static final String JOB_NAME = "VAPIL Test Doc Job";
	private static int jobId;


	@Test
	@Order(1)
	@DisplayName("successfully retrieve monitors for jobs which have not yet completed in the authenticated Vault")
	public void testRetrieveJobMonitors(VaultClient vaultClient) {
		JobMonitorResponse response = vaultClient.newRequest(JobRequest.class)
				.retrieveJobMonitors();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());
		for (Job job : response.getJobs()) {
			if (job.getTitle().equals(JOB_NAME)) {
				jobId = job.getJobId();
				break;
			}
		}

		if (response.isPaginated()) {
			JobMonitorResponse paginatedResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobMonitorsByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	@Order(2)
	@DisplayName("successfully move up a scheduled job instance to start immediately")
	public void testStartJob(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(JobRequest.class)
				.startJob(jobId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
		Assertions.assertEquals(jobId, response.getJobId());

	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve the status of a previously submitted job request")
	public void testRetrieveJobStatus(VaultClient vaultClient) {
		String jobStatus = "";
		boolean jobCompleted = false;
		for (int i = 0; i < 30; i++) {
			if (jobStatus.equals("SUCCESS")) break;
			JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobStatus(jobId);
			jobStatus = jobStatusResponse.getData().getStatus();
			switch (jobStatus) {
				case "SUCCESS":
					jobCompleted = true;
					break;
				default:
					try {
						Thread.sleep(10000);
						break;
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
			}
		}
		Assertions.assertTrue(jobCompleted);
		Assertions.assertEquals("SUCCESS", jobStatus);
	}

	@Test
	@Order(4)
	@DisplayName("successfully retrieve a history of all completed jobs in the authenticated Vault")
	public void testRetrieveJobHistories(VaultClient vaultClient) {
		JobHistoryResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobHistories();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());

		for(Job job : response.getJobs()) {
			if(job.getJobId() == jobId) {
				Assertions.assertEquals("SUCCESS", job.getStatus());
				break;
			}
		}

		if (response.isPaginated()) {
			JobHistoryResponse paginatedResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobHistoriesByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	@Disabled
	@Order(5)
	@DisplayName("successfully retrieve the tasks associated with an SDK job")
	public void testRetrieveJobTasks(VaultClient vaultClient) {
		JobTaskResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobTasks(1);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getTasks());
		Assertions.assertNotEquals(0, response.getTasks().size());

		if (response.isPaginated()) {
			JobTaskResponse paginatedResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobTasksByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}
}
