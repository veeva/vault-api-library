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
 * Response model for an EDL API response
 */
public class EDLResponse extends VaultResponse {

	@JsonProperty("data")
	public List<EDLNode> getData() {
		return (List<EDLNode>) this.get("data");
	}

	public void setData(List<EDLNode> data) {
		this.set("data", data);
	}

	public static class EDLNode extends VaultModel {

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("order__v")
		public String getOrder() {
			return this.getString("order__v");
		}

		public void setOrder(String order) {
			this.set("order__v", order);
		}

		@JsonProperty("parent_id__v")
		public String getParentId() {
			return this.getString("parent_id__v");
		}

		public void setParentId(String parentId) {
			this.set("parent_id__v", parentId);
		}

		@JsonProperty("ref_id__v")
		public String getRefId() {
			return this.getString("ref_id__v");
		}

		public void setRefId(String refId) {
			this.set("ref_id__v", refId);
		}

		@JsonProperty("ref_name__v")
		public String getRefName() {
			return this.getString("ref_name__v");
		}

		public void setRefName(String refName) {
			this.set("ref_name__v", refName);
		}

		@JsonProperty("ref_type__v")
		public String getRefType() {
			return this.getString("ref_type__v");
		}

		public void setRefType(String refType) {
			this.set("ref_type__v", refType);
		}

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
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