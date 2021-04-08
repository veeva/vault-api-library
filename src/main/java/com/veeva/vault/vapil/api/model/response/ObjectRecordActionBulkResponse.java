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
 * Model for the following API calls responses for the Object Workflow Action
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions <br>
 * GET
 * /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}
 */
public class ObjectRecordActionBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRecordAttachmentResponse> getData() {
		return (List<ObjectRecordAttachmentResponse>) this.get("data");
	}

	public void setData(List<ObjectRecordAttachmentResponse> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public boolean hasErrors() {
		if (super.hasErrors()) return true;

		List<ObjectRecordAttachmentResponse> responses = getData();
		if (responses == null || responses.size() == 0)
			return true;
		else {
			for (ObjectRecordAttachmentResponse objectRecordResponse : responses) {
				if (!objectRecordResponse.isSuccessful())
					return true;
			}
		}

		return false;
	}
}
