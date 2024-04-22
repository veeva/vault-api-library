/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Base Vault API response message
 */
public class VaultResponse extends VaultModel {

	/**
	 * responseStatus = FAILURE
	 */
	public static final String HTTP_RESPONSE_WARNING = "WARNING";

	/**
	 * responseStatus = FAILURE
	 */
	public static final String HTTP_RESPONSE_FAILURE = "FAILURE";

	/**
	 * responseStatus = SUCCESS
	 */
	public static final String HTTP_RESPONSE_SUCCESS = "SUCCESS";

	/**
	 * <b>Content-Disposition</b>
	 */
	public static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * <b>Content-Type</b>
	 */
	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

	/**
	 * <b>X-VaultAPI-BurstLimit</b>
	 */
	public static final String HTTP_HEADER_VAULT_BURST = "X-VaultAPI-BurstLimit";

	/**
	 * <b>X-VaultAPI-BurstLimitRemaining</b>
	 */
	public static final String HTTP_HEADER_VAULT_BURST_REMAINING = "X-VaultAPI-BurstLimitRemaining";

	/**
	 * <b>X-VaultAPI-ExecutionId</b>
	 */
	public static final String HTTP_HEADER_VAULT_EXECUTION_ID = "X-VaultAPI-ExecutionId";

	/**
	 * <b>X-VaultAPI-ResponseDelay</b>
	 */
	public static final String HTTP_HEADER_VAULT_RESPONSE_DELAY = "X-VaultAPI-ResponseDelay";

	/**
	 * <b>X-VaultAPI-TruncatedSessionId</b>
	 */
	public static final String HTTP_HEADER_VAULT_TRUNCATED_SESSION_ID = "X-VaultAPI-TruncatedSessionId";

	/**
	 * <b>X-VaultAPI-UserId</b>
	 */
	public static final String HTTP_HEADER_VAULT_USER_ID = "X-VaultAPI-UserId";

	/**
	 * <b>X-VaultAPI-VaultId</b>
	 */
	public static final String HTTP_HEADER_VAULT_ID = "X-VaultAPI-VaultId";

	/**
	 * <b>X-VaultAPI-SdkCount</b>
	 */
	public static final String HTTP_HEADER_VAULT_SDK_COUNT = "X-VaultAPI-SdkCount";

	/**
	 * <b>X-VaultAPI-SdkCpuTime</b>
	 */
	public static final String HTTP_HEADER_VAULT_SDK_CPU_TIME = "X-VaultAPI-SdkCpuTime";

	/**
	 * <b>X-VaultAPI-SdkElapsedTime</b>
	 */
	public static final String HTTP_HEADER_VAULT_SDK_ELAPSED_TIME = "X-VaultAPI-SdkElapsedTime";

	/**
	 * <b>X-VaultAPI-SdkGrossMemory</b>
	 */
	public static final String HTTP_HEADER_VAULT_SDK_GROSS_MEMORY = "X-VaultAPI-SdkGrossMemory";

	/**
	 * <b>X-VaultAPI-DowntimeExpectedDurationMinutes</b>
	 */
	public static final String HTTP_HEADER_DOWNTIME_EXPECTED_DURATION_MINUTES = "X-VaultAPI-DowntimeExpectedDurationMinutes";

	/**
	 * <b>X-VaultAPI-Status</b>
	 */
	public static final String HTTP_HEADER_STATUS = "X-VaultAPI-Status";

	/**
	 * <b>X-VaultAPI-Status</b>
	 */
	public static final String HTTP_REFERENCE_ID = "X-VaultAPI-ReferenceId";


	private byte[] binaryContent; // For requests that return binary data
	private List<APIResponseError> errors;
	private List<APIResponseWarning> warnings;
	private Map<String, List<String>> headers;
	private String outputFilePath; // For requests that wrote a file
	private String response = ""; // Response as string	
	private String responseStatus;
	private String responseMessage;

	/**
	 * Determine if the response status equals WARNING.
	 *
	 * @return true if the response status equals WARNING
	 */
	public boolean isWarning() {
		if (this.responseStatus != null) {
			return this.responseStatus.equalsIgnoreCase(VaultResponse.HTTP_RESPONSE_WARNING);
		}
		return false;
	}

	/**
	 * Determine if the response status equals FAILURE.
	 *
	 * @return true if the response status equals FAILURE
	 */
	public boolean isFailure() {
		if (this.responseStatus != null) {
			return this.responseStatus.equalsIgnoreCase(VaultResponse.HTTP_RESPONSE_FAILURE);
		}
		return false;
	}

	/**
	 * Determine if the response status equals SUCCESS.
	 *
	 * @return true if the response status equals SUCCESS
	 */
	public boolean isSuccessful() {
		if (this.responseStatus != null) {
			return this.responseStatus.equalsIgnoreCase(VaultResponse.HTTP_RESPONSE_SUCCESS);
		}
		return false;
	}

	@JsonProperty("responseMessage")
	public String getResponseMessage() {
		return responseMessage;
	}

	@JsonProperty("responseMessage")
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@JsonProperty("responseStatus")
	public String getResponseStatus() {
		return responseStatus;
	}

	@JsonProperty("responseStatus")
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * Get the full response as a string.
	 *
	 * @return The full response as a string
	 */
	@JsonIgnore
	public String getResponse() {
		return response;
	}

	@JsonIgnore
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * Get the full response as a JSON Object.
	 *
	 * @return The full response as a JSON Object
	 */
	@JsonIgnore
	public JsonNode getResponseJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.valueToTree(this.response);
	}

	@JsonProperty("errors")
	public List<APIResponseError> getErrors() {
		return errors;
	}

	@JsonProperty("errors")
	public void setErrors(List<APIResponseError> errors) {
		this.errors = errors;
	}

	/**
	 * Determine if the response has errors.
	 *
	 * @return true if the response has errors
	 */
	@JsonIgnore
	public boolean hasErrors() {
		return errors != null && !errors.isEmpty();
	}

	@JsonProperty("warnings")
	public List<APIResponseWarning> getWarnings() {
		return warnings;
	}

	@JsonProperty("warnings")
	public void setWarnings(List<APIResponseWarning> warnings) {
		this.warnings = warnings;
	}

	/**
	 * Determine if the response has warnings.
	 *
	 * @return true if the response has warnings
	 */
	@JsonIgnore
	public boolean hasWarnings() {
		return warnings != null && !warnings.isEmpty();
	}

	@JsonIgnore
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	@JsonIgnore
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	@JsonIgnore
	public String getHttpHeaderContentDisposition() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_CONTENT_DISPOSITION);
	}

	@JsonIgnore
	public String getHeaderContentType() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_CONTENT_TYPE);
	}

	@JsonIgnore
	public Integer getHeaderVaultBurstLimit() {
		return  getHeaderAsIntegerIgnoreCase(HTTP_HEADER_VAULT_BURST);
	}

	@JsonIgnore
	public Integer getHeaderVaultBurstLimitRemaining() {
		return  getHeaderAsIntegerIgnoreCase(HTTP_HEADER_VAULT_BURST_REMAINING);
	}

	@JsonIgnore
	public String getHeaderVaultExecutionId() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_VAULT_EXECUTION_ID);
	}

	@JsonIgnore
	public Integer getHeaderVaultId() {
		return getHeaderAsIntegerIgnoreCase(HTTP_HEADER_VAULT_ID);
	}

	@JsonIgnore
	public Integer getHeaderVaultResponseDelay() {
		return getHeaderAsIntegerIgnoreCase(HTTP_HEADER_VAULT_RESPONSE_DELAY);
	}

	@JsonIgnore
	public String getHeaderVaultTruncatedSessionId() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_VAULT_TRUNCATED_SESSION_ID);
	}

	@JsonIgnore
	public String getHeaderVaultUserId() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_VAULT_USER_ID);
	}

	@JsonIgnore
	public Integer getHeaderVaultSdkCount() {
		return getHeaderAsIntegerIgnoreCase(HTTP_HEADER_VAULT_SDK_COUNT);
	}

	@JsonIgnore
	public Long getHeaderVaultSdkCpuTime() {
		return getHeaderAsLongIgnoreCase(HTTP_HEADER_VAULT_SDK_CPU_TIME);
	}

	@JsonIgnore
	public Long getHeaderVaultSdkElapsedTime() {
		return getHeaderAsLongIgnoreCase(HTTP_HEADER_VAULT_SDK_ELAPSED_TIME);
	}

	@JsonIgnore
	public Long getHeaderVaultSdkGrossMemory() {
		return getHeaderAsLongIgnoreCase(HTTP_HEADER_VAULT_SDK_GROSS_MEMORY);
	}

	@JsonIgnore
	public Integer getHeaderDowntimeExpectedDurationMinutes() { return getHeaderAsIntegerIgnoreCase(HTTP_HEADER_DOWNTIME_EXPECTED_DURATION_MINUTES); }

	@JsonIgnore
	public String getHeaderStatus() {
		return getHeaderAsStringIgnoreCase(HTTP_HEADER_STATUS);
	}

	@JsonIgnore
	public String getHeaderReferenceId() {
		return getHeaderAsStringIgnoreCase(HTTP_REFERENCE_ID);
	}

	/**
	 * For requests returning binary content.
	 *
	 * @return Byte array for the returned binary content
	 */
	@JsonIgnore
	public byte[] getBinaryContent() {
		return binaryContent;
	}

	@JsonIgnore
	public void setBinaryContent(byte[] binaryContent) {
		this.binaryContent = binaryContent;
	}

	/**
	 * Return the file path For requests creating files.
	 *
	 * @return The file path of the created file
	 */
	@JsonIgnore
	public String getOutputFilePath() {
		return outputFilePath;
	}

	@JsonIgnore
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public Object getHeaderIgnoreCase(String header) {
		return headers.entrySet().stream()
				.filter(headers -> headers.getKey().equalsIgnoreCase(header))
				.map(headers -> headers.getValue().get(0))
				.findFirst()
				.orElse(null);
	}

	private Integer getHeaderAsIntegerIgnoreCase(String header) {
		Object headerValue = getHeaderIgnoreCase(header);
		if (headerValue != null) {
			return Integer.valueOf(headerValue.toString());
		}
		return null;
	}

	private Long getHeaderAsLongIgnoreCase(String header) {
		Object headerValue = getHeaderIgnoreCase(header);
		if (headerValue != null) {
			return Long.valueOf(headerValue.toString());
		}
		return null;
	}

	private String getHeaderAsStringIgnoreCase(String header) {
		Object headerValue = getHeaderIgnoreCase(header);
		if (headerValue != null) {
			return String.valueOf(headerValue.toString());
		}
		return null;
	}

	public static class APIResponseError extends VaultModel {

		@JsonProperty("message")
		public String getMessage() {
			return this.getString("message");
		}

		@JsonProperty("message")
		public void setMessage(String message) {
			this.set("message", message);
		}

		@JsonProperty("type")
		public String getType() {
			return getString("type");
		}

		@JsonProperty("type")
		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class APIResponseWarning extends VaultModel {

		@JsonProperty("message")
		public String getMessage() {
			return this.getString("message");
		}

		@JsonProperty("message")
		public void setMessage(String message) {
			this.set("message", message);
		}

		@JsonProperty("type")
		public String getType() {
			return getString("type");
		}

		@JsonProperty("type")
		public void setType(String type) {
			this.set("type", type);
		}
	}

}