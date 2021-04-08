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
import com.veeva.vault.vapil.api.model.common.ObjectAttachment;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments
 */
public class ObjectRecordAttachmentResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectAttachment> getData() {
		return (List<ObjectAttachment>) this.get("data");
	}

	public void setData(List<ObjectAttachment> data) {
		this.set("data", data);
	}
}