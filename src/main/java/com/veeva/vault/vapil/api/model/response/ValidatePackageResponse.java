/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;


/**
 * Response model for the following API calls:
 * <p>
 * POST /api/{version}/services/package/actions/validate
 * <br>
 * POST /api/{version}/services/vobject/vault_package__v/{package_id}/actions/validate
 */
public class ValidatePackageResponse extends VaultResponse {

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("author")
		public String getAuthor() {
			return this.getString("author");
		}

		public void setAuthor(String author) {
			this.set("author", author);
		}

		@JsonProperty("end_time")
		public String getEndTime() {
			return this.getString("end_time");
		}

		public void setEndTime(String endTime) {
			this.set("end_time", endTime);
		}

		@JsonProperty("package_error")
		public String getPackageError() {
			return this.getString("package_error");
		}

		public void setPackageError(String packageError) {
			this.set("package_error", packageError);
		}

		@JsonProperty("package_name")
		public String getPackageName() {
			return this.getString("package_name");
		}

		public void setPackageName(String packageName) {
			this.set("package_name", packageName);
		}

		@JsonProperty("package_status")
		public String getPackageStatus() {
			return this.getString("package_status");
		}

		public void setPackageStatus(String packageStatus) {
			this.set("package_status", packageStatus);
		}

		@JsonProperty("package_steps")
		public List<PackageStep> getPackageSteps() {
			return (List<PackageStep>) this.get("package_steps");
		}

		public void setPackageSteps(List<PackageStep> packageSteps) {
			this.set("package_steps", packageSteps);
		}

		@JsonProperty("source_vault")
		public String getSourceVault() {
			return this.getString("source_vault");
		}

		public void setSourceVault(String sourceVault) {
			this.set("source_vault", sourceVault);
		}

		@JsonProperty("start_time")
		public String getStartTime() {
			return this.getString("start_time");
		}

		@JsonProperty("start_time")
		public void setStartTime(String startTime) {
			this.set("start_time", startTime);
		}

		@JsonProperty("summary")
		public String getSummary() {
			return this.getString("summary");
		}

		public void setSummary(String summary) {
			this.set("summary", summary);
		}

		@JsonProperty("total_steps")
		public Integer getTotalSteps() {
			return this.getInteger("total_steps");
		}

		public void setTotalSteps(Integer totalSteps) {
			this.set("total_steps", totalSteps);
		}

		public static class PackageStep extends VaultModel {

			@JsonProperty("deployment_action")
			public String getDeploymentAction() {
				return this.getString("deployment_action");
			}

			public void setDeploymentAction(String deploymentAction) {
				this.set("deployment_action", deploymentAction);
			}

			@JsonProperty("deployment_status__v")
			public String getDeploymentStatus() {
				return this.getString("deployment_status__v");
			}

			public void setDeploymentStatus(String deploymentStatus) {
				this.set("deployment_status__v", deploymentStatus);
			}

			@JsonProperty("name__v")
			public String getName() {
				return this.getString("name__v");
			}

			public void setName(String name) {
				this.set("name__v", name);
			}

			@JsonProperty("step_label__v")
			public String getStepLabel() {
				return this.getString("step_label__v");
			}

			public void setStepLabel(String stepLabel) {
				this.set("step_label__v", stepLabel);
			}

			@JsonProperty("step_name__v")
			public String getStepName() {
				return this.getString("step_name__v");
			}

			public void setStepName(String stepName) {
				this.set("step_name__v", stepName);
			}

			@JsonProperty("step_type__v")
			public String getStepType() {
				return this.getString("step_type__v");
			}

			public void setStepType(String stepType) {
				this.set("step_type__v", stepType);
			}

			@JsonProperty("type__v")
			public String getType() {
				return this.getString("type__v");
			}

			public void setType(String type) {
				this.set("type__v", type);
			}

			@JsonProperty("validation_message")
			public String getValidationMessage() {
				return this.getString("validation_message");
			}

			public void setValidationMessage(String validationMessage) {
				this.set("validation_message", validationMessage);
			}

			@JsonProperty("validation_response")
			public String getValidationResponse() {
				return this.getString("validation_response");
			}

			public void setValidationResponse(String validationResponse) {
				this.set("validation_response", validationResponse);
			}
		}
	}
}