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

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/objects/documents/batch/actions/rerender
 */
public class DocumentRenditionBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<Rendition> getData() {
		return (List<Rendition>) this.get("data");
	}

	public void setData(List<Rendition> data) {
		this.set("data", data);
	}

	public static class Rendition extends VaultModel {
		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("major_version_number__v")
		public Integer getMajorVersionNumber() {
			return this.getInteger("major_version_number__v");
		}

		public void setMajorVersionNumber(Integer majorVersionNumber) {
			this.set("major_version_number__v", majorVersionNumber);
		}

		@JsonProperty("minor_version_number__v")
		public Integer getMinorVersionNumber() {
			return this.getInteger("minor_version_number__v");
		}

		public void setMinorVersionNumber(Integer minorVersionNumber) {
			this.set("minor_version_number__v", minorVersionNumber);
		}
	}
}
