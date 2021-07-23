/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.ObjectField;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/metadata/vobjects/{object_name}/fields/{object_field_name}
 */
public class MetaDataObjectFieldResponse extends VaultResponse {

	@JsonProperty("field")
	public ObjectField getField() {
		return (ObjectField) this.get("field");
	}

	public void setField(ObjectField field) {
		this.set("field", field);
	}
}
