/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the Template responses
 */
public class BinderTemplateBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<TemplateResult> getData() {
		return (List<TemplateResult>) this.get("data");
	}

	public void setData(List<TemplateResult> data) {
		this.set("data", data);
	}

	public static class TemplateResult extends VaultResponse {

		@JsonProperty("name__v")
		@JsonAlias("name")
		public String getName() {
			return this.getString("name__v");
		}

		public void setName(String name) {
			this.set("name__v", name);
		}
	}
}