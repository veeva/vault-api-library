/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for the following API calls responses for the Object Record
 * CUD operations:
 * <p>
 * POST/PUT/DELETE /api/{version}/vobjects/{object_name}
 * <br>
 * POST/PUT/DELETE /api/{version}/vobjects/{object_name}/{object_record_id}
 * <p>
 * GET call responses are available in ObjectRecordResponse
 */
public class ObjectRecordBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRecordResponse> getData() {
		return (List<ObjectRecordResponse>) this.get("data");
	}

	public void setData(List<ObjectRecordResponse> data) {
		this.set("data", data);
	}

	@JsonIgnore
	@Override
	public boolean hasErrors() {
		if (super.hasErrors()) return true;

		List<ObjectRecordResponse> responses = getData();
		if (responses == null || responses.size() == 0)
			return true;
		else {
			for (ObjectRecordResponse objectRecordResponse : responses) {
				if (!objectRecordResponse.isSuccessful())
					return true;
			}
		}

		return false;
	}
}