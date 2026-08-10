package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom Pages Requests
 * <ul>
 * <li>Retrieve All Client Code Distribution Metadata</li>
 * <li>Retrieve Single Client Code Distribution Metadata</li>
 * <li>Download Single Client Code Distribution</li>
 * <li>Add or Replace Single Client Code Distribution</li>
 * <li>Delete Single Client Code Distribution</li>
 * </ul>
 *
 * @vapil.apicoverage <a href="https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages">https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages</a>
 */

public class CustomPagesRequest extends VaultRequest<CustomPagesRequest> {
	// API Endpoints
	private static final String URL_DISTRIBUTIONS = "/uicode/distributions";
	private static final String URL_DISTRIBUTION = "/uicode/distributions/{distribution_name}";
	private static final String URL_DISTRIBUTION_DOWNLOAD = "/uicode/distributions/{distribution_name}/code";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile file;
	private String inputPath;
	private String outputPath;

	// API Request Parameter Constants
	public static final String FILE_PARAMETER = "file";

	private CustomPagesRequest() {
	}

	/**
	 * <b>Retrieve All Client Code Distribution Metadata</b>
	 * <p>
	 * Retrieves a list of all client code distributions in the Vault and their metadata.
	 *
	 * @return CustomPagesDistributionBulkResponse
	 * @vapil.api <pre> GET /api/{version}/uicode/distributions </pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/retrieve-all-client-code-distribution-metadata' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/retrieve-all-client-code-distribution-metadata</a>
	 * @vapil.request <pre>
	 * CustomPagesDistributionBulkResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.retrieveAllClientCodeDistributionMetadata();
	 * </pre>
	 * @vapil.response <pre>
	 * for (ClientCodeDistribution dist : response.getData()) {
	 * 		System.out.println("--------Distribution---------");
	 * 		System.out.println("Name: " + dist.getName());
	 * 		System.out.println("Size: " + dist.getSize());
	 * 		System.out.println("Checksum: " + dist.getChecksum());
	 * }
	 * </pre>
	 */
	public CustomPagesDistributionBulkResponse retrieveAllClientCodeDistributionMetadata() {
		String url = vaultClient.getAPIEndpoint(URL_DISTRIBUTIONS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpRequestConnector.HttpMethod.GET, request, CustomPagesDistributionBulkResponse.class);
	}

	/**
	 * <b>Retrieve Single Client Code Distribution Metadata</b>
	 * <p>
	 * Retrieve metadata for a specific client code distribution, including its name and size.
	 *
	 * @param distributionName The name attribute of the client code distribution to retrieve.
	 * @return CustomPagesDistributionRetrieveResponse
	 * @vapil.api <pre> GET /api/{version}/uicode/distributions/{distribution_name} </pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/retrieve-single-client-code-distribution-metadata' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/retrieve-single-client-code-distribution-metadata</a>
	 * @vapil.request <pre>
	 * CustomPagesDistributionRetrieveResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.retrieveSingleClientCodeDistributionMetadata(distributionName);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Distribution Name: " + response.getData().getName());
	 * System.out.println("Size: " + response.getData().getSize());
	 * System.out.println("Checksum: " + response.getData().getChecksum());
	 * </pre>
	 */
	public CustomPagesDistributionRetrieveResponse retrieveSingleClientCodeDistributionMetadata(String distributionName) {
		String url = vaultClient.getAPIEndpoint(URL_DISTRIBUTION);
		url = url.replace("{distribution_name}", distributionName);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpRequestConnector.HttpMethod.GET, request, CustomPagesDistributionRetrieveResponse.class);
	}

	/**
	 * <b>Download Single Client Code Distribution</b>
	 * <p>
	 * Download a ZIP file containing the client code distribution directory, including the client code files and the distribution manifest
	 *
	 * @param distributionName The name attribute of the client code distribution to download.
	 * @return VaultResponse
	 * @vapil.api <pre> GET /api/{version}/uicode/distributions/{distribution_name}/code </pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/download-single-client-code-distribution' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/download-single-client-code-distribution</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Binary</i>
	 * VaultResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.downloadSingleClientCodeDistribution(distributionName);
	 *
	 * <i>Example 2 - To File</i>
	 * VaultResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.setOutputPath(outputPath)
	 * 		.downloadSingleClientCodeDistribution(distributionName);
	 * </pre>
	 * @vapil.response <pre>
	 * <i>Example 1 - Binary</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * byte[] fileContent = response.getBinaryContent();
	 *
	 * <i>Example 2 - To File</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Output Path: " + response.getOutputFilePath());
	 * </pre>
	 */
	public VaultResponse downloadSingleClientCodeDistribution(String distributionName) {
		String url = vaultClient.getAPIEndpoint(URL_DISTRIBUTION_DOWNLOAD);
		url = url.replace("{distribution_name}", distributionName);
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpRequestConnector.HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Add or Replace Single Client Code Distribution</b>
	 * <p>
	 * Add or replace client code in Vault by uploading a ZIP file of the client code distribution. Vault unpacks and compares the uploaded distribution with other distributions in the Vault
	 *
	 * @return CustomPagesDistributionResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/uicode/distributions/
	 * </pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/add-or-replace-single-client-code-distribution' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/add-or-replace-single-client-code-distribution</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV input</i>
	 * CustomPagesDistributionResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.setInputPath(inputPath)
	 * 		.addOrReplaceSingleClientCodeDistribution();
	 *
	 * <i>Example 2 - Binary input</i>
	 * File inputFile = new File(inputPath);
	 * byte[] fileContent = Files.readAllBytes(inputFile.toPath());
	 * response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.setFile("vapil_test_dist.zip", fileContent)
	 * 		.addOrReplaceSingleClientCodeDistribution();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Distribution Name: " + response.getData().getName());
	 * System.out.println("Update Type: " + response.getData().getUpdateType());
	 * System.out.println("Checksum: " + response.getData().getChecksum());
	 * </pre>
	 */
	public CustomPagesDistributionResponse addOrReplaceSingleClientCodeDistribution() {
		String url = vaultClient.getAPIEndpoint(URL_DISTRIBUTIONS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart(FILE_PARAMETER, inputPath);
		}
		if (this.file != null) {
			request.addFileBinary(FILE_PARAMETER, file.getBinaryContent(), file.getFileName());
		}

		return send(HttpRequestConnector.HttpMethod.POST, request, CustomPagesDistributionResponse.class);
	}

	/**
	 * <b>Delete Single Client Code Distribution</b>
	 * <p>
	 * Delete a specific client code distribution. To delete a distribution, you must first remove all Page components associated with it from your Vault.
	 *
	 * @param distributionName The name attribute of the client code distribution to delete.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/uicode/distributions/{distribution_name}
	 * </pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/delete-single-client-code-distribution' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/custom-pages/delete-single-client-code-distribution</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(CustomPagesRequest.class)
	 * 		.deleteSingleClientCodeDistribution(distName);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse deleteSingleClientCodeDistribution(String distributionName) {
		String url = vaultClient.getAPIEndpoint(URL_DISTRIBUTION);
		url = url.replace("{distribution_name}", distributionName);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpRequestConnector.HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename    file name (no path)
	 * @param fileContent byte array of the file content
	 * @return The Request
	 */
	public CustomPagesRequest setFile(String filename, byte[] fileContent) {
		this.file = new HttpRequestConnector.BinaryFile(filename, fileContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public CustomPagesRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public CustomPagesRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}
}
