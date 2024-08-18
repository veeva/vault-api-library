/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.User;

import java.util.List;

public class UserLicenseUsageResponse extends VaultResponse {

	@JsonProperty("doc_count")
	public DocCount getDocCount() {
		return (DocCount) this.get("doc_count");
	}

	public void setDocCount(DocCount docCount) {
		this.set("doc_count", docCount);
	}

	@JsonProperty("applications")
	public List<Application> getApplications() {
		return (List<Application>) this.get("applications");
	}

	public void setApplications(List<Application> applications) {
		this.set("applications", applications);
	}

	public static class DocCount extends VaultModel {
		@JsonProperty("licensed")
		public Integer getLicensed() {
			return this.getInteger("licensed");
		}

		public void setLicensed(Integer licensed) {
			this.set("licensed", licensed);
		}

		@JsonProperty("used")
		public Integer getUsed() {
			return this.getInteger("used");
		}

		public void setUsed(Integer used) {
			this.set("used", used);
		}
	}

	public static class Application extends VaultModel {
		@JsonProperty("application_name")
		public String getApplicationName() {
			return this.getString("application_name");
		}

		public void setApplicationName(String applicationName) {
			this.set("application_name", applicationName);
		}

		@JsonProperty("user_licensing")
		public UserLicensing getUserLicensing() {
			return (UserLicensing) this.get("user_licensing");
		}

		public void setUserLicensing(UserLicensing userLicensing) {
			this.set("user_licensing", userLicensing);
		}

		public static class UserLicensing extends VaultModel {
			@JsonProperty("full__v")
			public FullLicense getFullLicense() {
				return (FullLicense) this.get("full__v");
			}

			public void setFullLicense(FullLicense fullLicense) {
				this.set("full__v", fullLicense);
			}

			@JsonProperty("read_only__v")
			public ReadOnlyLicense getReadOnlyLicense() {
				return (ReadOnlyLicense) this.get("read_only__v");
			}

			public void setReadOnlyLicense(ReadOnlyLicense readOnlyLicense) {
				this.set("read_only__v", readOnlyLicense);
			}

			@JsonProperty("external__v")
			public ExternalLicense getExternalLicense() {
				return (ExternalLicense) this.get("external__v");
			}

			public void setExternalLicense(ExternalLicense externalLicense) {
				this.set("external__v", externalLicense);
			}
			public static class FullLicense extends VaultModel {
				@JsonProperty("licensed")
				public Integer getLicensed() {
					return this.getInteger("licensed");
				}

				public void setLicensed(Integer licensed) {
					this.set("licensed", licensed);
				}

				@JsonProperty("used")
				public Integer getUsed() {
					return this.getInteger("used");
				}

				public void setUsed(Integer used) {
					this.set("used", used);
				}

				@JsonProperty("shared")
				public boolean getShared() {
					return this.getBoolean("shared");
				}

				public void setShared(boolean shared) {
					this.set("shared", shared);
				}
			}

			public static class ReadOnlyLicense extends VaultModel {
				@JsonProperty("licensed")
				public Integer getLicensed() {
					return this.getInteger("licensed");
				}

				public void setLicensed(Integer licensed) {
					this.set("licensed", licensed);
				}

				@JsonProperty("used")
				public Integer getUsed() {
					return this.getInteger("used");
				}

				public void setUsed(Integer used) {
					this.set("used", used);
				}

				@JsonProperty("shared")
				public boolean getShared() {
					return this.getBoolean("shared");
				}

				public void setShared(boolean shared) {
					this.set("shared", shared);
				}
			}

			public static class ExternalLicense extends VaultModel {
				@JsonProperty("licensed")
				public Integer getLicensed() {
					return this.getInteger("licensed");
				}

				public void setLicensed(Integer licensed) {
					this.set("licensed", licensed);
				}

				@JsonProperty("used")
				public Integer getUsed() {
					return this.getInteger("used");
				}

				public void setUsed(Integer used) {
					this.set("used", used);
				}

				@JsonProperty("shared")
				public boolean getShared() {
					return this.getBoolean("shared");
				}

				public void setShared(boolean shared) {
					this.set("shared", shared);
				}
			}
		}
	}
}
