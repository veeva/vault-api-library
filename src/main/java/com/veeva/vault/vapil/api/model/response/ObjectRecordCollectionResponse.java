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
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.metadata.Object;

import java.util.List;
import java.util.Map;

/**
 * Model for the following API calls responses for the Object Record CRUD
 * operations:
 * <p>
 * GET /api/{version}/vobjects/{object_name} <br>
 */
public class ObjectRecordCollectionResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRecord> getData() {
		return (List<ObjectRecord> ) this.get("data");
	}

	public void setData(List<ObjectRecord> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("limit")
		public Integer getLimit() {
			return this.getInteger("limit");
		}

		@JsonProperty("limit")
		public void setLimit(Integer limit) {
			this.set("limit", limit);
		}

		@JsonProperty("object")
		public Object getObject() {
			return (Object) this.get("object");
		}

		public void setObject(Object object) {
			this.set("object", object);
		}

		@JsonProperty("offset")
		public Integer getOffset() {
			return this.getInteger("offset");
		}

		@JsonProperty("offset")
		public void setOffset(Integer offset) {
			this.set("offset", offset);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		@JsonProperty("total")
		public void setTotal(Integer total) {
			this.set("total", total);
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