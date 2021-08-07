/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.VaultObject;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/metadata/vobjects/{object_name}
 */
public class MetaDataObjectResponse extends VaultResponse {

	@JsonProperty("object")
	public VaultObject getObject() {
		return (VaultObject) this.get("object");
	}

	public void setObject(VaultObject objectMetaData) {
		this.set("object", objectMetaData);
	}
}