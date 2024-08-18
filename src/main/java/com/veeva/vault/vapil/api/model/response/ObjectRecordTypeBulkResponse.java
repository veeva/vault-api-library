/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ObjectRecordType;

/**
 * Model for the Object Record Type or Object describe API responses:
 * <p>
 * /api/{version}/configuration/Objecttype
 */
public class ObjectRecordTypeBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRecordType> getData() {
		return (List<ObjectRecordType>) this.get("data");
	}

	public void setData(List<ObjectRecordType> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultResponse {
		@JsonProperty("size")
		public Integer getSize() {
			return getInteger("size");
		}

		public void setSize(Integer size) {
			this.set("size", size);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return getInteger("total");
		}

		public void setTotal(Integer total) {
			this.set("total", total);
		}
	}

}