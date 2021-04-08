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
public class BinderTemplateNodeBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<TemplateNodeResult> getData() {
		return (List<TemplateNodeResult>) this.get("data");
	}

	public void setData(List<TemplateNodeResult> data) {
		this.set("data", data);
	}

	public static class TemplateNodeResult extends VaultResponse {

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}
	}
}