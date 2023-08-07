/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.response.LoaderResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.json.JSONArray;

import java.util.*;

/**
 * Vault Loader Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#vault-loader">https://developer.veevavault.com/api/23.1/#vault-loader</a>
 */
public class LoaderRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_LOADER_EXTRACT = "/services/loader/extract";
	private static final String URL_LOADER_LOAD = "/services/loader/load";
	private static final String URL_LOADER_TASK_FAILURE_LOG = "/services/loader/{job_id}/tasks/{task_id}/failurelog";
	private static final String URL_LOADER_TASK_SUCCESS_LOG = "/services/loader/{job_id}/tasks/{task_id}/successlog";
	private static final String URL_LOADER_TASK_RESULTS = "/services/loader/{job_id}/tasks/{task_id}/results";
	private static final String URL_LOADER_TASK_RENDITION_RESULTS = "/services/loader/{job_id}/tasks/{task_id}/results/renditions";

	// API Request Parameters
	private String json;
	private String outputPath;
	private Boolean sendNotification = false;
	private List<LoaderTask> loaderTasks = new ArrayList<>();

	private LoaderRequest() {
	}

	/**
	 * <b>Extract Data Files</b>
	 *
	 * @return LoaderResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/loader/extract</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#extract-data-files' target='_blank'>https://developer.veevavault.com/api/23.1/#extract-data-files</a>
	 */
	public LoaderResponse extractDataFiles() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_LOADER_EXTRACT));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (sendNotification != null) {
			request.addQueryParam("sendNotification", sendNotification);
		}

		if (json != null) {
			request.addRawString("application/json", json);
		} else {
			JSONArray loaderTaskArray = new JSONArray();
			for (LoaderTask loaderTask : loaderTasks) {
				loaderTaskArray.put(loaderTask.toJSONObject());
			}
			request.addRawString("application/json", loaderTaskArray.toString());
		}


		return send(HttpMethod.POST, request, LoaderResponse.class);
	}

	/**
	 * <b>Load Data Objects</b>
	 *
	 * @return LoaderResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/loader/load
	 * </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#load-data-objects' target='_blank'>https://developer.veevavault.com/api/23.1/#load-data-objects</a>
	 */
	public LoaderResponse loadDataObjects() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_LOADER_LOAD));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (sendNotification != null) {
			request.addQueryParam("sendNotification", sendNotification);
		}

		if (json != null) {
			request.addRawString("application/json", json);
		} else {
			JSONArray loaderTaskArray = new JSONArray();
			for (LoaderTask loaderTask : loaderTasks) {
				loaderTaskArray.put(loaderTask.toJSONObject());
			}
			request.addRawString("application/json", loaderTaskArray.toString());
		}

		return send(HttpMethod.POST, request, LoaderResponse.class);
	}

	/**
	 * <b>Retrieve Loader Extract Results</b>
	 *
	 * @param jobId  The Job Id
	 * @param taskId The Task Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/loader/{job_id}/tasks/{task_id}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-loader-extract-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-extract-results</a>
	 */
	public VaultResponse retrieveLoaderExtractResults(int jobId, int taskId) {
		return retrieveResults(URL_LOADER_TASK_RESULTS, jobId, taskId);
	}

	/**
	 * <b>Retrieve Loader Extract Renditions Results</b>
	 *
	 * @param jobId  The Job Id
	 * @param taskId The Task Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/loader/{job_id}/tasks/{task_id}/results/renditions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-loader-extract-renditions-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-loader-extract-renditions-results</a>
	 */
	public VaultResponse retrieveLoaderExtractRenditionsResults(int jobId, int taskId) {
		return retrieveResults(URL_LOADER_TASK_RENDITION_RESULTS, jobId, taskId);
	}

	/**
	 * <b>Retrieve Load Success Log Results</b>
	 *
	 * @param jobId  The Job Id
	 * @param taskId The Task Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/loader/{job_id}/tasks/{task_id}/successlog</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-load-success-log-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-load-success-log-results</a>
	 */
	public VaultResponse retrieveLoadSuccessLogResults(int jobId, int taskId) {
		return retrieveResults(URL_LOADER_TASK_SUCCESS_LOG, jobId, taskId);
	}

	/**
	 * <b>Retrieve Load Success Log Results</b>
	 *
	 * @param jobId  The Job Id
	 * @param taskId The Task Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/loader/{job_id}/tasks/{task_id}/failurelog</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-load-failure-log-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-load-failure-log-results</a>
	 */
	public VaultResponse retrieveLoadFailureLogResults(int jobId, int taskId) {
		return retrieveResults(URL_LOADER_TASK_FAILURE_LOG, jobId, taskId);
	}

	/**
	 * <b>Retrieve the results from the job</b>
	 *
	 * @param endpoint Job endpoint
	 * @param jobId    The Job Id
	 * @param taskId   The Task Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/loader/{job_id}/tasks/{task_id}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-extract-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-extract-results</a>
	 */
	private VaultResponse retrieveResults(String endpoint, int jobId, int taskId) {
		String url = vaultClient.getAPIEndpoint(endpoint);
		url = url.replace("{job_id}", String.valueOf(jobId));
		url = url.replace("{task_id}", String.valueOf(taskId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set a raw JSON string to the Vault Loader request
	 *
	 * @param json Fully formed Vault Loader request
	 * @return The Request
	 */
	public LoaderRequest setJson(String json) {
		this.json = json;
		return this;
	}

	/**
	 * Add an individual Vault Loader Task to the request
	 * Throws {@link IllegalArgumentException} when maximum number of loader tasks is reached (10).
	 *
	 * @param loaderTask The Vault Loader Task to add
	 * @return The Request
	 * @throws Exception The exception
	 */
	public LoaderRequest addLoaderTask(LoaderTask loaderTask) throws Exception {
		this.loaderTasks.add(loaderTask);

		if (loaderTasks.size() > 10) {
			throw new IllegalArgumentException("Maximum # Loader Tasks Exceeded.");
		}
		return this;
	}

	/**
	 * Add a full list of Vault Loader Tasks to the request
	 * Throws {@link IllegalArgumentException} when maximum number of loader tasks is reached (10).
	 *
	 * @param loaderTasks The Vault Loader Task to add
	 * @return The Request
	 * @throws Exception maximum number of loader tasks exceeded
	 */
	public LoaderRequest addLoaderTasks(List<LoaderTask> loaderTasks) throws Exception {

		this.loaderTasks.addAll(loaderTasks);
		if (loaderTasks.size() > 10) {
			throw new IllegalArgumentException("Maximum # Loader Tasks Exceeded.");
		}
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public LoaderRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * To send a Vault notification when the job completes, set to true.
	 * If omitted, this defaults to false and Vault does not send a notification when the job completes.
	 *
	 * @param sendNotification send notification to user
	 * @return The Request
	 */
	public LoaderRequest setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
		return this;
	}


}
