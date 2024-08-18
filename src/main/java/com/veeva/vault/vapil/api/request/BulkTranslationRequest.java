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


/**
 * Bulk Translation Requests
 * <ul>
 * <li>Export Bulk Translation File</li>
 * <li>Import Bulk Translation File</li>
 * <li>Retrieve Import Bulk Translation File Job Summary</li>
 * <li>Retrieve Import Bulk Translation File Job Errors</li>
 * </ul>
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.2/#bulk-translation">https://developer.veevavault.com/api/24.2/#bulk-translation</a>
 */
public class BulkTranslationRequest extends VaultRequest<BulkTranslationRequest> {

	// API Endpoints
	private static final String URL_EXPORT_BULK_TRANSLATION_FILE = "/messages/{message_type}/language/{lang}/actions/export";
	private static final String URL_IMPORT_BULK_TRANSLATION_FILE = "/messages/{message_type}/actions/import";
	private static final String URL_TRANSLATION_IMPORT_JOB_SUMMARY = "/services/jobs/{job_id}/summary";
	private static final String URL_TRANSLATION_IMPORT_JOB_ERRORS = "/services/jobs/{job_id}/errors";

	// API Request Parameters


	// API Request Parameter Constants
	public static final String FILE_PATH = "file_path";


	/**
	 * <b>Export Bulk Translation File</b>
	 * <p>
	 * Export a bulk translation file from your Vault.
	 * The exported bulk translation file is a CSV editable in any text editor or translation software.
	 * You can request one (1) message type in one (1) language per request.
	 *
	 * @param messageType The message type to export.
	 * @param lang        The language to export.
	 * @return BulkTranslationJobResponse
	 * @vapil.api <pre> POST /api/{version}/messages/{message_type}/language/{lang}/actions/export</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#export-bulk-translation-file' target='_blank'>https://developer.veevavault.com/api/24.2/#export-bulk-translation-file</a>
	 * @vapil.request <pre>
	 * BulkTranslationJobResponse response = vaultClient.newRequest(BulkTranslationRequest.class)
	 * 		.exportBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, "ja");
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Job ID = " + response.getData().getJobId());
	 * System.out.println("Job Status = " + response.getData().getUrl());
	 * </pre>
	 */
	public BulkTranslationJobResponse exportBulkTranslationFile(MESSAGE_TYPE messageType, String lang) {
		String url = vaultClient.getAPIEndpoint(URL_EXPORT_BULK_TRANSLATION_FILE)
				.replace("{message_type}", messageType.getValue())
				.replace("{lang}", lang);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, BulkTranslationJobResponse.class);
	}


	/**
	 * <b>Import Bulk Translation File</b>
	 * <p>
	 * Import a bulk translation file into Vault. While an exported bulk translation file can contain only one (1) language,
	 * your import file may include multiple languages.
	 *
	 * @param messageType The message type to export.
	 * @param filePath   The file path of the CSV file on the file staging server.
	 * @return BulkTranslationJobResponse
	 * @vapil.api <pre> POST /api/{version}/messages/{message_type}/actions/import</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#import-bulk-translation-file' target='_blank'>https://developer.veevavault.com/api/24.2/#import-bulk-translation-file</a>
	 * @vapil.request <pre>
	 * BulkTranslationJobResponse response = vaultClient.newRequest(BulkTranslationRequest.class)
	 * 		.importBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, "/bulk_translation_test.csv");
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Job ID = " + response.getData().getJobId());
	 * System.out.println("Job URL = " + response.getData().getUrl());
	 * </pre>
	 */
	public BulkTranslationJobResponse importBulkTranslationFile(MESSAGE_TYPE messageType, String filePath) {
		String url = vaultClient.getAPIEndpoint(URL_IMPORT_BULK_TRANSLATION_FILE)
				.replace("{message_type}", messageType.getValue());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		request.addBodyParam(FILE_PATH, filePath);

		return send(HttpMethod.POST, request, BulkTranslationJobResponse.class);
	}

	/**
	 * <b>Retrieve Import Bulk Translation File Job Summary</b>
	 * <p>
	 * After submitting a request to import a bulk translation file, you can query Vault to determine the results of the request.
	 *
	 * @param jobId The job ID of the import bulk translation file job.
	 * @return BulkTranslationImportSummaryResponse
	 * @vapil.api <pre> GET /api/{version}/services/jobs/{job_id}/summary</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-import-bulk-translation-file-job-summary' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-import-bulk-translation-file-job-summary</a>
	 * @vapil.request <pre>
	 * BulkTranslationImportSummaryResponse response = vaultClient.newRequest(BulkTranslationRequest.class)
	 * 		.retrieveImportBulkTranslationFileJobSummary(jobId);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Ignored = " + response.getData().getIgnored());
	 * System.out.println("Updated = " + response.getData().getUpdated());
	 * System.out.println("Failed = " + response.getData().getFailed());
	 * System.out.println("Added = " + response.getData().getAdded());
	 * </pre>
	 */
	public BulkTranslationImportSummaryResponse retrieveImportBulkTranslationFileJobSummary(String jobId) {
		String url = vaultClient.getAPIEndpoint(URL_TRANSLATION_IMPORT_JOB_SUMMARY)
				.replace("{job_id}", jobId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, BulkTranslationImportSummaryResponse.class);
	}

	/**
	 * <b>Retrieve Import Bulk Translation File Job Errors</b>
	 * <p>
	 * After submitting a request to import a bulk translation file, you can query Vault to determine the errors from the request (if any).
	 *
	 * @param jobId The job ID of the import bulk translation file job.
	 * @return VaultResponse
	 * @vapil.api <pre>GET /api/{version}/services/jobs/{job_id}/errors</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-import-bulk-translation-file-job-errors' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-import-bulk-translation-file-job-errors</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(BulkTranslationRequest.class)
	 * 		.retrieveImportBulkTranslationFileJobErrors(jobId);
	 * </pre>
	 * @vapil.response <pre>
	 * String text = new String(response.getBinaryContent(), StandardCharsets.UTF_16);
	 * System.out.println(text);
	 * </pre>
	 */
	public VaultResponse retrieveImportBulkTranslationFileJobErrors(String jobId) {
		String url = vaultClient.getAPIEndpoint(URL_TRANSLATION_IMPORT_JOB_ERRORS)
				.replace("{job_id}", jobId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
	}

	/**
	 * Enum MESSAGE_TYPE represents the message types to import/export.
	 */
	public enum MESSAGE_TYPE {
		FIELD_LABELS("field_labels__sys"),
		SYSTEM_MESSAGES("system_messages__sys"),
		NOTIFICATION_TEMPLATE_MESSAGES("notification_template_messages__sys"),
		USER_ACCOUNT_MESSAGES("user_account_messages__sys");

		private final String messageType;

		MESSAGE_TYPE(String messageType) {
			this.messageType = messageType;
		}

		public String getValue() {
			return messageType;
		}
	}
}