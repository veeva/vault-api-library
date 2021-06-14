/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for document export API response
 */
public class DocumentExportResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ExportedDocument> getData() {
		return (List<ExportedDocument>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<ExportedDocument> data) {
		this.set("data", data);
	}

	public static class ExportedDocument extends VaultResponse {

		@JsonProperty("file")
		public String getFile() {
			return this.getString("file");
		}

		@JsonProperty("file")
		public void setFile(String file) {
			this.set("file", file);
		}

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		@JsonProperty("id")
		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("major_version_number__v")
		public Integer getMajorVersionNumber() {
			return this.getInteger("major_version_number__v");
		}

		@JsonProperty("major_version_number__v")
		public void setMajorVersionNumber(Integer majorVersionNumber) {
			this.set("major_version_number__v", majorVersionNumber);
		}

		@JsonProperty("minor_version_number__v")
		public Integer getMinorVersionNumber() {
			return this.getInteger("minor_version_number__v");
		}

		@JsonProperty("minor_version_number__v")
		public void setMinorVersionNumber(Integer minorVersionNumber) {
			this.set("minor_version_number__v", minorVersionNumber);
		}

		@JsonProperty("user_id__v")
		public Integer getUserId() {
			return this.getInteger("user_id__v");
		}

		@JsonProperty("user_id__v")
		public void setUserId(Integer userId) {
			this.set("user_id__v", userId);
		}
	}
}