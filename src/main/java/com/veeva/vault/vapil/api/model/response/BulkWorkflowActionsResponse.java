/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;


/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/object/workflow/actions
 */
public class BulkWorkflowActionsResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ActionItem> getData() {
		return (List<ActionItem>) get("data");
	}

	@JsonProperty("data")
	public void setData(List<ActionItem> data) {
		this.set("data", data);
	}

	public static class ActionItem extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}
	}
}
