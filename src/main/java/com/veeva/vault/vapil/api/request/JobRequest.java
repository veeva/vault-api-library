/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

import java.time.ZonedDateTime;

/**
 * Job - Retrieve Job Status Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/21.2/#jobs">https://developer.veevavault.com/api/21.2/#jobs</a>
 */
public class JobRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_JOB = "/services/jobs/{job_id}";
	private static final String URL_JOB_HISTORIES = "/services/jobs/histories";
	private static final String URL_JOB_MONITORS = "/services/jobs/monitors";
	private static final String URL_JOB_TASKS = "/services/jobs/{job_id}/tasks";
	private static final String URL_JOB_START_NOW = "/services/jobs/start_now/{job_id}";

	// API Request Parameters
	private ZonedDateTime endDate;
	private String limit;
	private String offset;
	private String status;
	private ZonedDateTime startDate;

	private JobRequest() {
	}

	/*
	 *
	 * Public endpoints
	 *
	 */

	/**
	 * Retrieve Job Histories
	 * <p>
	 * Retrieve a history of all completed jobs in the authenticated vault.
	 * A completed job is any job which has started and finished running,
	 * including jobs which did not complete successfully.
	 * In-progress or queued jobs do not appear here.
	 *
	 * @return JobHistoryResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/jobs/{job_id}/tasks</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.2/#retrieve-job-histories' target='_blank'>https://developer.veevavault.com/api/21.2/#retrieve-job-histories</a>
	 * @vapil.request <pre>
	 * JobHistoryResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobHistories();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + response.getResponseStatus());
	 * if (response.isSuccessful()) {
	 *   System.out.println("Url = " + response.getUrl());
	 * }</pre>
	 */
	public JobHistoryResponse retrieveJobHistories() {
		String url = vaultClient.getAPIEndpoint(URL_JOB_HISTORIES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (endDate != null) {
			request.addQueryParam("end_date", endDate);
		}

		if (limit != null) {
			request.addQueryParam("limit", limit);
		}

		if (offset != null) {
			request.addQueryParam("offset", offset);
		}

		if (startDate != null) {
			request.addQueryParam("start_date", startDate);
		}

		if (status != null) {
			request.addQueryParam("status", status);
		}

		return send(HttpMethod.GET, request, JobHistoryResponse.class);
	}

	/**
	 * Retrieve Job Monitors
	 * <p>
	 * Retrieve monitors for jobs which have not yet completed in the authenticated vault.
	 * An uncompleted job is any job which has not finished running,
	 * such as scheduled, queued, or actively running jobs. Completed jobs do not appear here.
	 *
	 * @return JobMonitorResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/jobs/monitors</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.2/#retrieve-job-monitors' target='_blank'>https://developer.veevavault.com/api/21.2/#retrieve-job-monitors</a>
	 * @vapil.request <pre>
	 * JobMonitorResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobMonitors();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + response.getResponseStatus());
	 * if (response.isSuccessful()) {
	 *   System.out.println("Url = " + response.getUrl());
	 * }</pre>
	 */
	public JobMonitorResponse retrieveJobMonitors() {
		String url = vaultClient.getAPIEndpoint(URL_JOB_MONITORS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (endDate != null) {
			request.addQueryParam("end_date", endDate);
		}

		if (limit != null) {
			request.addQueryParam("limit", limit);
		}

		if (offset != null) {
			request.addQueryParam("offset", offset);
		}

		if (startDate != null) {
			request.addQueryParam("start_date", startDate);
		}

		if (status != null) {
			request.addQueryParam("status", status);
		}

		return send(HttpMethod.GET, request, JobMonitorResponse.class);
	}

	/**
	 * Retrieve Job Status
	 * <p>
	 * After submitting a request, you can query your vault to determine the status of the request.
	 * To do this, you must have a valid job_id for a job previously requested through the API.
	 * </p>
	 * <p>
	 * The Job Status endpoint can only be requested once every 10 seconds.
	 * When this limit is reached, Vault returns API_LIMIT_EXCEEDED.
	 * </p>
	 *
	 * @param jobId job id
	 * @return JobStatusResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/jobs/{job_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.2/#retrieve-job-status' target='_blank'>https://developer.veevavault.com/api/21.2/#retrieve-job-status</a>
	 * @vapil.request <pre>
	 * JobStatusResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);</pre>
	 * @vapil.response <pre>
	 * if ((response != null)
	 *   &amp;&amp; (!response.hasErrors())
	 *   &amp;&amp; (response.getData() != null)) {
	 *
	 *   JobStatusResponse.Job job = response.getData();
	 *   if (job.getRunEndDate() != null) {
	 *     System.out.println("Made it!");
	 *   }
	 * }</pre>
	 */
	public JobStatusResponse retrieveJobStatus(int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_JOB);
		url = url.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, JobStatusResponse.class);
	}


	/**
	 * Retrieve Job Tasks
	 * <p>
	 * Retrieve the tasks associated with an SDK job.
	 *
	 * @param jobId job id
	 * @return JobTaskResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/jobs/{job_id}/tasks</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.2/#retrieve-job-tasks' target='_blank'>https://developer.veevavault.com/api/21.2/#retrieve-job-tasks</a>
	 * @vapil.request <pre>
	 * JobTaskResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobTasks(jobId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + response.getResponseStatus());
	 * if (response.isSuccessful()) {
	 *   System.out.println("Number of Tasks = " + response.getResponseDetails().getTotal());
	 *   System.out.println("Url = " + response.getUrl());
	 * }</pre>
	 */
	public JobTaskResponse retrieveJobTasks(int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_JOB_TASKS);
		url = url.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (limit != null) {
			request.addQueryParam("limit", limit);
		}

		if (offset != null) {
			request.addQueryParam("offset", offset);
		}

		return send(HttpMethod.GET, request, JobTaskResponse.class);
	}


	/**
	 * Start Job
	 * <p>
	 * Moves up a scheduled job instance to start immediately. Each time a user calls this API, Vault cancels the
	 * next scheduled instance of the specified job. For example, calling this API against a scheduled
	 * daily job three times would cause the job to not run according to its schedule for three days.
	 * Once the affected Job ID is complete, Vault will schedule the next instance.
	 *
	 * This is analogous to the Start Now option in the Vault UI.
	 *
	 * @param jobId job id
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/jobs/start_now/{job_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.2/#start-job' target='_blank'>https://developer.veevavault.com/api/21.2/#start-job</a>
	 * @vapil.request <pre>
	 * JobCreateResponse response = vaultClient.newRequest(JobRequest.class).startJob(jobId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + response.getResponseStatus());
	 * if (response.isSuccessful()) {
	 *   System.out.println("JobId = " + response.getJobId());
	 *   System.out.println("Url = " + response.getUrl());
	 * }</pre>
	 */
	public JobCreateResponse startJob(int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_JOB_START_NOW);
		url = url.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Sets the date to end retrieving completed jobs, in the format YYYY-MM-DDTHH:MM:SSZ.
	 * For example, for 7AM on January 15, 2016, use 2016-01-15T07:00:00Z.
	 * If omitted, defaults to the current date and time.
	 *
	 * @param endDate end date
	 * @return JobRequest
	 */
	public JobRequest setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * Paginate the results by specifying the maximum number of histories per page in the response.
	 * This can be any value between 1 and 200. If omitted, defaults to 50.
	 * <p>
	 *
	 * @param limit the size of the result set in the page
	 * @return JobRequest
	 */
	public JobRequest setLimit(String limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Paginate the results displayed per page by specifying
	 * the amount of offset from the first job history returned.
	 * If omitted, defaults to 0.
	 * <p>
	 *
	 * @param offset page offset
	 * @return JobRequest
	 */
	public JobRequest setOffset(String offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Filter to only retrieve jobs in a certain status.
	 * <p>
	 *
	 * @param status job status
	 * @return JobRequest
	 */
	public JobRequest setStats(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Sets the date to start retrieving completed jobs, in the format YYYY-MM-DDTHH:MM:SSZ.
	 * For example, for 7AM on January 15, 2016, use 2016-01-15T07:00:00Z.
	 * If omitted, defaults to the first completed job.
	 *
	 * @param startDate start date
	 * @return The Request
	 */
	public JobRequest setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
		return this;
	}


}
