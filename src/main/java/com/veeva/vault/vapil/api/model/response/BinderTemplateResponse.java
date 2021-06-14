/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.BinderTemplate;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the Binder Template responses
 */
public class BinderTemplateResponse extends VaultResponse {

	@JsonProperty("data")
	public List<BinderTemplate> getData() {
		return (List<BinderTemplate>) get("data");
	}

	public void setData(List<BinderTemplate> data) {
		this.set("data", data);
	}
}