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
import com.veeva.vault.vapil.api.model.VaultModel;
import org.json.JSONObject;

/**
 * Base Vault API response message
 */
public class VaultResponse extends VaultModel {

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



	private byte[] binaryContent; // For requests that return binary data
	private List<APIResponseError> errors;
	private Map<String, List<String>> headers;
	private String outputFilePath; // For requests that wrote a file
	private String response = ""; // Response as string	
	private String responseStatus;
	private String responseMessage;

	/**
	 * @return true if there was a successful response
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
	 * Get the full response as a string
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
	 * Get the full response as a JSON Object
	 *
	 * @return The full response as a JSON Object
	 */
	@JsonIgnore
	public JSONObject getResponseJSON() {
		return new JSONObject(this.response);
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
	 * Determine if errors exist in the API Response
	 *
	 * @return True if errors exist
	 */
	@JsonIgnore
	public boolean hasErrors() {
		return errors != null && !errors.isEmpty();
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
		if (headers.keySet().contains(HTTP_HEADER_CONTENT_DISPOSITION)) {
			return headers.get(HTTP_HEADER_CONTENT_DISPOSITION).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getHeaderContentType() {
		if (headers.keySet().contains(HTTP_HEADER_CONTENT_TYPE)) {
			return headers.get(HTTP_HEADER_CONTENT_TYPE).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Integer getHeaderVaultBurstLimit() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_BURST)) {
			return Integer.valueOf(headers.get(HTTP_HEADER_VAULT_BURST).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Integer getHeaderVaultBurstLimitRemaining() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_BURST_REMAINING)) {
			return Integer.valueOf(headers.get(HTTP_HEADER_VAULT_BURST_REMAINING).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getHeaderVaultExecutionId() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_EXECUTION_ID)) {
			return headers.get(HTTP_HEADER_VAULT_EXECUTION_ID).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Integer getHeaderVaultId() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_ID)) {
			return Integer.valueOf(headers.get(HTTP_HEADER_VAULT_ID).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getHeaderVaultResponseDelay() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_RESPONSE_DELAY)) {
			return headers.get(HTTP_HEADER_VAULT_RESPONSE_DELAY).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getHeaderVaultTruncatedSessionId() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_TRUNCATED_SESSION_ID)) {
			return headers.get(HTTP_HEADER_VAULT_TRUNCATED_SESSION_ID).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getHeaderVaultUserId() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_USER_ID)) {
			return headers.get(HTTP_HEADER_VAULT_USER_ID).get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Long getHeaderVaultSdkCount() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_SDK_COUNT)) {
			return Long.valueOf(headers.get(HTTP_HEADER_VAULT_SDK_COUNT).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Long getHeaderVaultSdkCpuTime() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_SDK_CPU_TIME)) {
			return Long.valueOf(headers.get(HTTP_HEADER_VAULT_SDK_CPU_TIME).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Long getHeaderVaultSdkElapsedTime() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_SDK_ELAPSED_TIME)) {
			return Long.valueOf(headers.get(HTTP_HEADER_VAULT_SDK_ELAPSED_TIME).get(0));
		} else {
			return null;
		}
	}

	@JsonIgnore
	public Long getHeaderVaultSdkGrossMemory() {
		if (headers.keySet().contains(HTTP_HEADER_VAULT_SDK_GROSS_MEMORY)) {
			return Long.valueOf(headers.get(HTTP_HEADER_VAULT_SDK_GROSS_MEMORY).get(0));
		} else {
			return null;
		}
	}

	/**
	 * For requests returning binary content
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
	 * Return the file path For requests creating files
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
}