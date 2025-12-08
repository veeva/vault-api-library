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
import com.veeva.vault.vapil.extension.JobRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled("Jobs are set to Inactive after the sandbox is refreshed. " +
//		"Verify that the VAPIL Test Job is active before running tests.")
@Tag("JobRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Job Request should")
public class JobRequestTest {

	private static final String VAPIL_TEST_JOB_NAME = JobRequestHelper.VAPIL_TEST_JOB_NAME;
	private static final String VAPIL_TEST_JOB_LABEL = JobRequestHelper.VAPIL_TEST_JOB_LABEL;
	private static final String PATH_CANCEL_JOB_JSON_FILE = JobRequestHelper.PATH_CANCEL_JOB_JSON_FILE;
	private static int jobId;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully retrieve monitors for jobs which have not yet completed in the authenticated Vault")
	public void testRetrieveJobMonitors() {
		JobMonitorResponse response = vaultClient.newRequest(JobRequest.class)
				.retrieveJobMonitors();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());
		for (Job job : response.getJobs()) {
			if (job.getTitle().equals(VAPIL_TEST_JOB_NAME)) {
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
	public void testStartJob() {
		JobCreateResponse response = vaultClient.newRequest(JobRequest.class)
				.startJob(jobId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
		Assertions.assertEquals(jobId, response.getJobId());

	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve the status of a previously submitted job request")
	public void testRetrieveJobStatus() {
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
	public void testRetrieveJobHistories() {
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

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve the tasks associated with an SDK job")
	@Disabled("Test Manually. May be automated in the future")
	class TestRetrieveSdkJobTasks {
		JobTaskResponse response = null;
		int jobId = 282871;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(JobRequest.class)
							.retrieveSdkJobTasks(jobId);
			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getUrl());
			assertNotNull(response.getJobId());
			assertNotNull(response.getTasks());
			for (JobTaskResponse.JobTask task : response.getTasks()) {
				assertNotNull(task.getId());
				assertNotNull(task.getState());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully a history of all completed jobs in the authenticated Vault.")
	class TestRetrieveJobMonitors {
		JobMonitorResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(JobRequest.class)
					.setStartDate(ZonedDateTime.now().minusDays(1))
					.setEndDate(ZonedDateTime.now())
					.retrieveJobMonitors();
			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getUrl());
			assertNotNull(response.getJobs());
			for (Job job : response.getJobs()) {
				assertNotNull(job.getJobId());
				assertNotNull(job.getTitle());
				assertNotNull(job.getStatus());
				assertNotNull(job.getCreatedBy());
				assertNotNull(job.getCreatedDate());
				assertNotNull(job.getModifiedBy());
				assertNotNull(job.getModifiedDate());
				assertNotNull(job.getRunStartDate());
			}

			assertNotNull(response.getResponseDetails());
			assertNotNull(response.getResponseDetails().getLimit());
			assertNotNull(response.getResponseDetails().getOffset());
			assertNotNull(response.getResponseDetails().getTotal());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully a history of all completed jobs in the authenticated Vault.")
	class TestRetrieveJobHistories {
		JobHistoryResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(JobRequest.class)
					.setStartDate(ZonedDateTime.now().minusDays(1))
					.setEndDate(ZonedDateTime.now())
					.retrieveJobHistories();
			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getUrl());
			assertNotNull(response.getJobs());
			for (Job job : response.getJobs()) {
				assertNotNull(job.getJobId());
				assertNotNull(job.getTitle());
				assertNotNull(job.getStatus());
				assertNotNull(job.getCreatedBy());
				assertNotNull(job.getCreatedDate());
				assertNotNull(job.getModifiedBy());
				assertNotNull(job.getModifiedDate());
				assertNotNull(job.getRunStartDate());
				assertNotNull(job.getRunEndDate());
			}

			assertNotNull(response.getResponseDetails());
			assertNotNull(response.getResponseDetails().getLimit());
			assertNotNull(response.getResponseDetails().getOffset());
			assertNotNull(response.getResponseDetails().getTotal());
		}
	}

	@Nested
	@Tag("SmokeTest")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully cancel job instances")
	class TestCancelJob {
		JobCancelBulkResponse response = null;
		List<Integer> jobIds = new ArrayList<>();

		@BeforeAll
		public void setup() throws IOException {
			String jobQuery = """
					SELECT id
					FROM job_instance__sys
					WHERE job_name__sys = '%s'
					""".formatted(VAPIL_TEST_JOB_NAME);

			QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
					.query(jobQuery);
			assertFalse(queryResponse.isFailure());
			jobIds.add(queryResponse.getData().get(0).getInteger("id"));

			for (int jobId : jobIds) {
				JobCreateResponse jobCreateResponse = vaultClient.newRequest(JobRequest.class)
						.startJob(jobId);
				assertTrue(jobCreateResponse.isSuccessful());
			}

			JobRequestHelper.writeToCancelJobFile(jobIds);
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(JobRequest.class)
					.setInputPath(PATH_CANCEL_JOB_JSON_FILE)
					.cancelJob();
			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			for (JobCancelBulkResponse.JobCancelResponse cancelResponse : response.getData()) {
				assertEquals("SUCCESS", cancelResponse.getResponseStatus());
				assertNotNull(cancelResponse.getData());
				assertNotNull(cancelResponse.getData().getId());
			}
		}
	}
}
