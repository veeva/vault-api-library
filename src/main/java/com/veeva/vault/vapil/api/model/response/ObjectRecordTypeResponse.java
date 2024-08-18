/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ObjectRecordType;

import java.util.List;

/**
 * Model for the Object Record Type or Object describe API responses:
 * <p>
 * /api/{version}/configuration/Objecttype.{object_name}.{object_type}
 */
public class ObjectRecordTypeResponse extends VaultResponse {

	@JsonProperty("data")
	public ObjectRecordType getData() {
		return (ObjectRecordType) this.get("data");
	}

	public void setData(ObjectRecordType data) {
		this.set("data", data);
	}

}