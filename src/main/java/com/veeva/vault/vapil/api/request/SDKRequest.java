/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.SDKResponse;
import com.veeva.vault.vapil.api.model.response.QueueResponse;
import com.veeva.vault.vapil.api.model.response.ValidatePackageResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Managing Vault Java SDK Code
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#managing-vault-java-sdk-code">https://developer.veevavault.com/api/23.1/#managing-vault-java-sdk-code</a>
 */
public class SDKRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_CERT = "/services/certificate/{cert_id}";
	private static final String URL_CODE = "/code";
	private static final String URL_CODE_CLASS = "/code/{class_name}";
	private static final String URL_CODE_DISABLE = "/code/{class_name}/disable";
	private static final String URL_CODE_ENABLE = "/code/{class_name}/enable";
	private static final String URL_QUEUE = "/services/queues/{queue_name}";
	private static final String URL_QUEUE_DISABLE_DELIVERY = "/services/queues/{queue_name}/actions/disable_delivery";
	private static final String URL_QUEUE_ENABLE_DELIVERY = "/services/queues/{queue_name}/actions/enable_delivery";
	private static final String URL_QUEUE_RESET = "/services/queues/{queue_name}/actions/reset";
	private static final String URL_QUEUES = "/services/queues";
	private static final String URL_VALIDATE = "/services/package/actions/validate";
	private static final String URL_VALIDATE_IMPORTED = "/services/vobject/vault_package__v/{package_id}/actions/validate";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private String inputPath;
	private String outputPath;

	private SDKRequest() {

	}

	/**
	 * <b>Retrieve Single Source Code File</b>
	 * <p>
	 * Retrieve a single source code file from the currently authenticated vault
	 *
	 * @param className The fully qualified class name of your file.
	 * @return SDKResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/code/{class_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-single-source-code-file' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-single-source-code-file</a>
	 * @vapil.request <pre>
	 * SDKResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.retrieveSingleSourceCodeFile(className);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Binary Length = " + resp.getBinaryContent().length);</pre>
	 */
	public SDKResponse retrieveSingleSourceCodeFile(String className) {
		String url = vaultClient.getAPIEndpoint(URL_CODE_CLASS);
		url = url.replace("{class_name}", className);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, SDKResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, SDKResponse.class);
		}
	}

	/**
	 * <b>Disable Vault Extension</b>
	 * <p>
	 * Disable a deployed Vault extension in the currently authenticated vault.
	 * Only available on entry-point classes, such as triggers and actions.
	 *
	 * @param className The fully qualified class name of your file.
	 * @return SDKResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/code/{class_name}/disable</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#enable-or-disable-vault-extension' target='_blank'>https://developer.veevavault.com/api/23.1/#enable-or-disable-vault-extension</a>
	 * @vapil.request <pre>
	 * SDKResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.disableVaultExtension(className);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public SDKResponse disableVaultExtension(String className) {
		String url = vaultClient.getAPIEndpoint(URL_CODE_DISABLE);
		url = url.replace("{class_name}", className);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.PUT, request, SDKResponse.class);
	}

	/**
	 * <b>Enable Vault Extension</b>
	 * <p>
	 * Enable a deployed Vault extension in the currently authenticated vault. Only available on entry-point classes, such as triggers and actions.
	 *
	 * @param className The fully qualified class name of your file.
	 * @return SDKResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/code/{class_name}/enable</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#enable-or-disable-vault-extension' target='_blank'>https://developer.veevavault.com/api/23.1/#enable-or-disable-vault-extension</a>
	 * @vapil.request <pre>
	 * SDKResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.enableVaultExtension(className);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * </pre>
	 */
	public SDKResponse enableVaultExtension(String className) {
		String url = vaultClient.getAPIEndpoint(URL_CODE_ENABLE);
		url = url.replace("{class_name}", className);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.PUT, request, SDKResponse.class);
	}

	/**
	 * <b>Add or Replace Single Source Code File</b>
	 * <p>
	 * Add or replace a single .java file in the currently authenticated Vault.
	 * If the given file does not already exist in the vault, it is added.
	 * If the file already exists in the vault, the file is updated.
	 * Maximum allowed size is 1MB.
	 *
	 * @return SDKResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/code</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-or-replace-single-source-code-file' target='_blank'>https://developer.veevavault.com/api/23.1/#add-or-replace-single-source-code-file</a>
	 * @vapil.request <pre>
	 * SDKResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 					.setBinaryFile(sdkFile.getName(), Files.readAllBytes(sdkFile.toPath()))
	 * 					.addOrReplaceSingleSourceCodeFile();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Message = " + resp.getResponseMessage());
	 * System.out.println("Url = " + resp.getUrl());
	 *
	 * System.out.println("\n****** Add/Replace SDK Component - Local File ******");
	 * resp = vaultClient.newRequest(SDKRequest.class)
	 * .setInputPath(fileName)
	 * .addOrReplaceSingleSourceCodeFile();
	 *
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Message = " + resp.getResponseMessage());
	 * System.out.println("Url = " + resp.getUrl());</pre>
	 */
	public SDKResponse addOrReplaceSingleSourceCodeFile() {
		String url = vaultClient.getAPIEndpoint(URL_CODE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.PUT, request, SDKResponse.class);
	}

	/**
	 * <b>Delete Single Source Code File</b>
	 * <p>
	 * Delete a single source code file from the currently authenticated Vault.
	 * You cannot delete a code component currently in-use.
	 *
	 * @param className The fully qualified class name of your file.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/code/{class_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-single-source-code-file' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-single-source-code-file</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.deleteSingleSourceCodeFile(className);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public VaultResponse deleteSingleSourceCodeFile(String className) {
		String url = vaultClient.getAPIEndpoint(URL_CODE_CLASS);
		url = url.replace("{class_name}", className);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * <b>Validate Package</b>
	 * Validate a VPK package with Vault Java SDK code attached to this request.
	 * This checks checks code compilation and restrictions in use of the JDK.
	 *
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/package/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#validate-package' target='_blank'>https://developer.veevavault.com/api/23.1/#validate-package</a>
	 * @vapil.request <pre>
	 * ValidatePackageResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.setInputPath(fileName)
	 * 				.validatePackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Author = " + resp.getResponseDetails().getAuthor());</pre>
	 */
	public ValidatePackageResponse validatePackage() {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}
		return send(HttpMethod.POST, request, ValidatePackageResponse.class);
	}

	/**
	 * <b>Validate Imported Package
	 * </b>
	 * <p>
	 * Validate a previously imported VPK package with Vault Java SDK code.
	 *
	 * @param packageId id of package to validate
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/vobject/vault_package__v/{package_id}/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#validate-imported-package' target='_blank'>https://developer.veevavault.com/api/23.1/#validate-imported-package</a>
	 * @vapil.request <pre>
	 * ValidatePackageResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.validateImportedPackage(packageId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Author = " + resp.getResponseDetails().getAuthor());</pre>
	 */
	public ValidatePackageResponse validateImportedPackage(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE_IMPORTED)
				.replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, ValidatePackageResponse.class);
	}

	/**
	 * <b>Retrieve all Queues</b>
	 *
	 * @return QueueResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/queues</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-queues' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-queues</a>
	 * @vapil.request <pre>
	 * QueueResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.retrieveAllQueues();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * String queueName = null;
	 * if (resp.isSuccessful()) {
	 *   System.out.println("# Queues = " + resp.getData().size());
	 *
	 *   for (QueueResponse.Queue queue : resp.getData()) {
	 *     queueName = queue.getName();
	 *     System.out.println("Queue Name = " + queue.getName());
	 *     }
	 *   }
	 * }</pre>
	 */
	public QueueResponse retrieveAllQueues() {
		String url = vaultClient.getAPIEndpoint(URL_QUEUES);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, QueueResponse.class);
	}

	/**
	 * <b>Retrieve Queue Status</b>
	 * <p>
	 * Retrieve the status of a specific queue.
	 *
	 * @param queueName name of queue
	 * @return QueueResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/queues/{queue_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-queue-status' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-queue-status</a>
	 * @vapil.request <pre>
	 * QueueResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 	.retrieveQueueStatus(queueName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("# Queues = " + resp.getData().size());
	 *
	 *   for (QueueResponse.Queue queue : resp.getData()) {
	 *     System.out.println("Queue Name = " + queue.getName());
	 *   }
	 * }</pre>
	 */
	public QueueResponse retrieveQueueStatus(String queueName) {
		String url = vaultClient.getAPIEndpoint(URL_QUEUE)
				.replace("{queue_name}", queueName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, QueueResponse.class);
	}

	/**
	 * <b>Disable Delivery</b>
	 * <p>
	 * The following endpoint allows you to disable the delivery of messages in an outbound queue.
	 * This endpoint is not available for inbound queues, as inbound queues process received messages
	 * rather than deliver messages. Vault uses Message Processor to process received messages.
	 * There is no way to stop received messages from processing.
	 *
	 * @param queueName name of queue
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/services/queues/{queue_name}/actions/disable_delivery</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#disable-delivery' target='_blank'>https://developer.veevavault.com/api/23.1/#disable-delivery</a>
	 * @vapil.request <pre>
	 * VaultResponse disableResponse = vaultClient.newRequest(SDKRequest.class)
	 * .disableDelivery(queueName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + disableResponse.getResponseStatus());</pre>
	 */
	public VaultResponse disableDelivery(String queueName) {
		String url = vaultClient.getAPIEndpoint(URL_QUEUE_DISABLE_DELIVERY)
				.replace("{queue_name}", queueName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * <b>Enable Delivery</b>
	 * <p>
	 * The following endpoint allows you to enable the delivery of messages in an outbound queue.
	 * This endpoint is not available for inbound queues, as inbound queues process received messages
	 * rather than deliver messages.
	 *
	 * @param queueName name of queue
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/services/queues/{queue_name}/actions/enable_delivery</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#enable-delivery' target='_blank'>https://developer.veevavault.com/api/23.1/#enable-delivery</a>
	 * @vapil.request <pre>
	 * VaultResponse enableResponse = vaultClient.newRequest(SDKRequest.class)
	 * 		.enableDelivery(queueName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + enableResponse.getResponseStatus());</pre>
	 */
	public VaultResponse enableDelivery(String queueName) {
		String url = vaultClient.getAPIEndpoint(URL_QUEUE_ENABLE_DELIVERY)
				.replace("{queue_name}", queueName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * <b>Reset Queue</b>
	 * <p>
	 * Delete all messages in a specific queue. This action is final and cannot be undone.
	 *
	 * @param queueName name of queue
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/services/queues/{queue_name}/actions/reset</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#reset-queue' target='_blank'>https://developer.veevavault.com/api/23.1/#reset-queue</a>
	 * @vapil.request <pre>
	 * VaultResponse resetResponse = vaultClient.newRequest(SDKRequest.class)
	 * 			.resetQueue(queueName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resetResponse.getResponseStatus());</pre>
	 */
	public VaultResponse resetQueue(String queueName) {
		String url = vaultClient.getAPIEndpoint(URL_QUEUE_RESET)
				.replace("{queue_name}", queueName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * The following endpoint allows you to retrieve a signing certificate included
	 * in a Spark message header to verify that the received message came from Vault.
	 *
	 * @param certificateId certificate id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/services/certificate/{cert_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-signing-certificate' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-signing-certificate</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(SDKRequest.class)
	 * 				.setOutputPath(fileName)
	 * 				.retrieveSigningCertificate("00001");</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public VaultResponse retrieveSigningCertificate(String certificateId) {
		String url = vaultClient.getAPIEndpoint(URL_CERT)
				.replace("{cert_id}", certificateId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

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
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public SDKRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public SDKRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public SDKRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}
}
