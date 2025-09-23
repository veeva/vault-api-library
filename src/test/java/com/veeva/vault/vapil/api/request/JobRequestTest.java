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

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag("JobRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Job Request should")
public class JobRequestTest {

	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
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
}
