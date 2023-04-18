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
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Configuration Migration
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#configuration-migration">https://developer.veevavault.com/api/23.1/#configuration-migration</a>
 */
public class ConfigurationMigrationRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_PACKAGE = "/services/package";
	private static final String URL_PACKAGE_DEPLOY = "/vobject/vault_package__v/{package_id}/actions/deploy";
	private static final String URL_PACKAGE_DEPLOY_RESULTS = "/vobject/vault_package__v/{package_id}/actions/deploy/results";
	private static final String URL_PACKAGE_IMPORT_RESULTS = "/vobject/vault_package__v/{package_id}/actions/import/results";
	private static final String URL_OUTBOUND_PACKAGE_DEPENDENCIES = "/vobjects/outbound_package__v/{package_id}/dependencies";
	private static final String URL_VALIDATE = "/services/package/actions/validate";
	private static final String URL_VALIDATE_INBOUND = "/services/vobject/vault_package_v/{package_id}/actions/validate";
	private static final String URL_VAULT_COMPARE = "/objects/vault/actions/compare";
	private static final String URL_VAULT_CONFIG = "/objects/vault/actions/configreport";


	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private List<String> componentTypes;
	private DetailsType detailsType;
	private Boolean generateOutboundPackages;
	private ZonedDateTime includeComponentsModifiedSince;
	private Boolean includeDocBinderTemplates;
	private Boolean includeInactiveComponents;
	private Boolean includeVaultSettings;
	private OutputFormat outputFormat;
	private ResultsType resultsType;
	private Boolean suppressEmptyResults;
	private String inputPath;

	private ConfigurationMigrationRequest() {
	}

	/**
	 * Export Package
	 *
	 * @param packageName name of package to export
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/package</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-package' target='_blank'>https://developer.veevavault.com/api/23.1/#export-package</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.exportPackage(packageName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse exportPackage(String packageName) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam("packageName", packageName);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Import Package
	 *
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/services/package</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#import-package' target='_blank'>https://developer.veevavault.com/api/23.1/#import-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * PackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.importPackage();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary</i>
	 * PackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *   .setBinaryFile("file", Files.readAllBytes(new File(filePath).toPath()))
	 *   .importPackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Id = " + resp.getJobId());
	 * 	 System.out.println("Url = " + resp.getUrl());
	 * }</pre>
	 */
	public JobCreateResponse importPackage() {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}
		return send(HttpMethod.PUT, request, JobCreateResponse.class);
	}

	/**
	 * Deploy Package
	 *
	 * @param packageId id of package to deploy
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobject/vault_package__v/{package_id}/actions/deploy</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#deploy-package' target='_blank'>https://developer.veevavault.com/api/23.1/#deploy-package</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.deployPackage(packageId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse deployPackage(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_DEPLOY).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Retrieve Package Deploy Results
	 *
	 * @param packageId id of package to retrieve results
	 * @return PackageDeploymentResultsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobject/vault_package__v/{package_id}/actions/deploy/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-package-deploy-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-package-deploy-results</a>
	 * @vapil.request <pre>
	 * PackageDeploymentResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.retrievePackageDeployResults(packageId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Total Components = " + resp.getResponseDetails().getTotalComponents());
	 *   System.out.println("Deployment Status = " + resp.getResponseDetails().getDeploymentStatus());
	 * }</pre>
	 */
	public PackageDeploymentResultsResponse retrievePackageDeployResults(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_DEPLOY_RESULTS).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, PackageDeploymentResultsResponse.class);
	}

	/**
	 * Retrieve Package Import Results
	 *
	 * <p>
	 *     Retrieve the results of an imported VPK given the package id
	 * </p>
	 * @param packageId id of package to retrieve results
	 * @return PackageImportResultsResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/vault_package__v/{package_id}/actions/import/results</pre>
	 * @vapil.request <pre>
	 *     PackageImportResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *			.retrievePackageImportResults(packageId);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *         System.out.println("Package Name = " + resp.getVaultPackage().getName());
	 *     }
	 * </pre>
	 */
	public PackageImportResultsResponse retrievePackageImportResults(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_IMPORT_RESULTS).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, PackageImportResultsResponse.class);
	}

	/**
	 * Retrieve Package Import Results
	 *
	 * <p>
	 *     Retrieve the results of an imported VPK given the href provided in the artifacts section of the
	 *     Job Status response.
	 * </p>
	 * @param href url of the retrieve package import results endpoint
	 * @return PackageImportResultsResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/vault_package__v/{package_id}/actions/import/results</pre>
	 * @vapil.request <pre>
	 *     PackageImportResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *			.retrievePackageImportResults(href);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *         System.out.println("Package Name = " + resp.getVaultPackage().getName());
	 *     }
	 * </pre>
	 */
	public PackageImportResultsResponse retrievePackageImportResultsByHref(String href) {
		return vaultClient.newRequest(JobRequest.class).retrieveJobArtifactByHref(href, PackageImportResultsResponse.class);
	}

	/**
	 * Retrieve Outbound Package Dependencies
	 * <p>
	 *     Retrieve existing dependencies for an outbound package.
	 * </p>
	 * @param packageId id of package to retrieve dependencies
	 * @return OutboundPackageDependenciesResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/outbound_package__v/{package_id}/dependencies
	 * </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-outbound-package-dependencies' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-outbound-package-dependencies</a>
	 * @vapil.request <pre>
	 *     OutboundPackageDependenciesResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *     					.retrieveOutboundPackageDependencies(packageId);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *     	System.out.println("Total Dependencies = " + resp.getResponseDetails().getTotalDependencies());
	 *     }
	 * </pre>
	 */
	public OutboundPackageDependenciesResponse retrieveOutboundPackageDependencies(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_OUTBOUND_PACKAGE_DEPENDENCIES).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, OutboundPackageDependenciesResponse.class);
	}

	/**
	 * Vault Compare
	 *
	 * @param targetVaultId target vault id
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/vault/actions/compare</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#vault-compare' target='_blank'>https://developer.veevavault.com/api/23.1/#vault-compare</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setResultsType(ConfigurationMigrationRequest.ResultsType.COMPLETE)
	 * 				.setDetailsType(ConfigurationMigrationRequest.DetailsType.COMPLEX)
	 * 				.setIncludeDocBinderTemplates(true)
	 * 				.setIncludeVaultSettings(true)
	 * 				.setComponentsType(componentTypes)
	 * 				.vaultCompare(targetVaultId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse vaultCompare(int targetVaultId) {
		String url = vaultClient.getAPIEndpoint(URL_VAULT_COMPARE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam("vault_id", Integer.valueOf(targetVaultId));

		if (resultsType != null) {
			request.addBodyParam("results_type", resultsType.getValue());
		}

		if (detailsType != null) {
			request.addBodyParam("details_type", detailsType.getValue());
		}

		if (includeDocBinderTemplates != null) {
			request.addBodyParam("include_doc_binder_templates", includeDocBinderTemplates);
		}

		if (includeVaultSettings != null) {
			request.addBodyParam("include_vault_settings", includeVaultSettings);
		}

		if (componentTypes != null && !componentTypes.isEmpty()) {
			request.addBodyParam("component_types", String.join(",", componentTypes));
		}

		if (generateOutboundPackages != null) {
			request.addBodyParam("generate_outbound_packages", generateOutboundPackages);
		}

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Vault Configuration Report
	 *
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/vault/actions/configreport</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#vault-configuration-report' target='_blank'>https://developer.veevavault.com/api/23.1/#vault-configuration-report</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setIncludeVaultSettings(true)
	 * 				.setIncludeInactiveComponents(true)
	 * 				.setIncludeComponentsModifiedSince(ZonedDateTime.now().minusDays(14))
	 * 				.setSuppressEmptyResults(true)
	 * 				.setComponentsType(componentTypes)
	 * 				.setOutputFormat(ConfigurationMigrationRequest.OutputFormat.EXCEL)
	 * 				.vaultConfigurationReport();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse vaultConfigurationReport() {
		String url = vaultClient.getAPIEndpoint(URL_VAULT_CONFIG);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (includeVaultSettings != null) {
			request.addBodyParam("include_vault_settings", includeVaultSettings);
		}

		if (includeInactiveComponents != null) {
			request.addBodyParam("include_inactive_components", includeInactiveComponents);
		}

		if (includeComponentsModifiedSince != null) {
			String modifiedDate = includeComponentsModifiedSince.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			request.addBodyParam("include_components_modified_since", modifiedDate);
		}

		if (suppressEmptyResults != null) {
			request.addBodyParam("suppress_empty_results", suppressEmptyResults);
		}

		if (componentTypes != null && !componentTypes.isEmpty()) {
			request.addBodyParam("component_types", String.join(",", componentTypes));
		}

		if (outputFormat != null) {
			request.addBodyParam("output_format", outputFormat.getValue());
		}

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Validate Package</b>
	 *
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/package/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#validate-package' target='_blank'>https://developer.veevavault.com/api/23.1/#validate-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.validatePackage();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setBinaryFile("file", Files.readAllBytes(new File(filePath).toPath()))
	 * 				.validatePackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Author = " + resp.getResponseDetails().getAuthor());
	 *   System.out.println("Status = " + resp.getResponseDetails().getPackageStatus());
	 * }</pre>
	 */
	public ValidatePackageResponse validatePackage() {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE);

		HttpRequestConnector request = new HttpRequestConnector(url);
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
	 * <b>Validate Inbound Package</b>
	 *
	 * @param packageId The id field value of the vault_package__v object record to validate.
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/vobject/vault_package__v/{package_id}/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#validate-inbound-package' target='_blank'>https://developer.veevavault.com/api/23.1/#validate-inbound-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.validateInboundPackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Author = " + resp.getResponseDetails().getAuthor());
	 *   System.out.println("Status = " + resp.getResponseDetails().getPackageStatus());
	 * }</pre>
	 */
	public ValidatePackageResponse validateInboundPackage(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE_INBOUND)
				.replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, ValidatePackageResponse.class);
	}

	/*
	 *
	 * Request constants
	 *
	 */

	/**
	 * Optional: To show component level details only, set to none.
	 * To include simple attribute-level details, set to simple.
	 * To show all attribute-level details, set to complex.
	 * If omitted, this defaults to simple.
	 */
	public enum DetailsType {
		COMPLEX("complex"),
		NONE("none"),
		SIMPLE("SIMPLE"); //default

		private final String value;

		DetailsType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Optional: Output report as either an Excel (XSLX) or Excel_Macro_Enabled (XLSM) file.
	 * If omitted, defaults to Excel_Macro_Enabled.
	 */
	public enum OutputFormat {
		EXCEL("Excel"),
		EXCEL_MACRO_ENABLED("Excel_Macro_Enabled"); //default

		private final String value;

		OutputFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Optional: To include all configuration values, set this to complete.
	 * To only see the differences between vaults, set to differences.
	 * If omitted, this defaults to differences.
	 */
	public enum ResultsType {
		COMPLETE("complete"),
		DIFFERENCES("differences"); //default

		private final String value;

		ResultsType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
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
	public ConfigurationMigrationRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set list of component types for compare or config report
	 *
	 * @param componentTypes component types
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setComponentsType(List<String> componentTypes) {
		this.componentTypes = componentTypes;
		return this;
	}

	/**
	 * Set generate outbound packages baed on compare report differences
	 *
	 * @param generateOutboundPackages boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setGenerateOutboundPackages(Boolean generateOutboundPackages) {
		this.generateOutboundPackages = generateOutboundPackages;
		return this;
	}

	/**
	 * Set details type for compare report
	 *
	 * @param detailsType details type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setDetailsType(DetailsType detailsType) {
		this.detailsType = detailsType;
		return this;
	}

	/**
	 * Set components modified since date in compare report
	 *
	 * @param includeComponentsModifiedSince results type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeComponentsModifiedSince(ZonedDateTime includeComponentsModifiedSince) {
		this.includeComponentsModifiedSince = includeComponentsModifiedSince;
		return this;
	}

	/**
	 * Set include binder doc templates in compare report
	 *
	 * @param includeDocBinderTemplates boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeDocBinderTemplates(Boolean includeDocBinderTemplates) {
		this.includeDocBinderTemplates = includeDocBinderTemplates;
		return this;
	}

	/**
	 * Set include inactive components in config report
	 *
	 * @param includeInactiveComponents boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeInactiveComponents(Boolean includeInactiveComponents) {
		this.includeInactiveComponents = includeInactiveComponents;
		return this;
	}


	/**
	 * Set include vault settings in compare or config report
	 *
	 * @param includeVaultSettings boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeVaultSettings(Boolean includeVaultSettings) {
		this.includeVaultSettings = includeVaultSettings;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Set output format for config report
	 *
	 * @param outputFormat boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
		return this;
	}

	/**
	 * Set results type for compare report
	 *
	 * @param resultsType results type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setResultsType(ResultsType resultsType) {
		this.resultsType = resultsType;
		return this;
	}

	/**
	 * Set suppress empty results for config report
	 *
	 * @param suppressEmptyResults boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setSuppressEmptyResults(Boolean suppressEmptyResults) {
		this.suppressEmptyResults = suppressEmptyResults;
		return this;
	}

}
