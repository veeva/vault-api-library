/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.PackageComponent;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET https://login.veevavault.com/auth/discovery
 * <p>
 * Discover the authentication type of a user. With this API,
 * applications can dynamically adjust the the login requirements per user,
 * and support either username/password or OAuth2.0 / OpenID Connect authentication schemes.
 */
public class PackageResultsResponse extends VaultResponse {

	@JsonProperty("package_components")
	public List<PackageComponent> getPackageComponents() {
		return (List<PackageComponent>) this.get("package_components");
	}

	public void setPackageComponents(List<PackageComponent> packageComponents) {
		this.set("package_components", packageComponents);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("deployed_components")
		public Integer getDeployedComponents() {
			return this.getInteger("deployed_components");
		}

		public void setDeployedComponents(Integer deployedComponents) {
			this.set("deployed_components", deployedComponents);
		}

		@JsonProperty("deployment_log")
		public DeploymentLog getDeploymentLog() {
			return (DeploymentLog) this.get("deployment_log");
		}

		public void setDeploymentLog(DeploymentLog deploymentLog) {
			this.set("deployment_log", deploymentLog);
		}

		@JsonProperty("deployment_status__v")
		public String getDeploymentStatus() {
			return this.getString("deployment_status__v");
		}

		public void setDeploymentStatus(String deploymentStatus) {
			this.set("deployment_status__v", deploymentStatus);
		}

		@JsonProperty("error_components")
		public Integer getErrorComponents() {
			return this.getInteger("error_components");
		}

		public void setErrorComponents(Integer errorComponents) {
			this.set("error_components", errorComponents);
		}

		@JsonProperty("skipped_components")
		public Integer getSkippedComponents() {
			return this.getInteger("skipped_components");
		}

		public void setSkippedComponents(Integer skippedComponents) {
			this.set("skipped_components", skippedComponents);
		}

		@JsonProperty("total_components")
		public Integer getTotalComponents() {
			return this.getInteger("total_components");
		}

		public void setTotalComponents(Integer totalComponents) {
			this.set("total_components", totalComponents);
		}

		@JsonProperty("warnings")
		public Integer getWarnings() {
			return this.getInteger("warnings");
		}

		public void setWarnings(Integer warnings) {
			this.set("warnings", warnings);
		}

		public static class DeploymentLog extends VaultModel {

			@JsonProperty("created_date__v")
			public String getCreatedDate() {
				return this.getString("created_date__v");
			}

			public void setCreatedDate(String createdDate) {
				this.set("created_date__v", createdDate);
			}

			@JsonProperty("filename")
			public String getFilename() {
				return this.getString("filename");
			}

			public void setFilename(String filename) {
				this.set("filename", filename);
			}

			@JsonProperty("url")
			public String getUrl() {
				return this.getString("url");
			}

			public void setUrl(String url) {
				this.set("url", url);
			}
		}
	}
}