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
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses for the Object Workflow Action
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions <br>
 * GET
 * /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}
 */
public class ObjectWorkflowActionResponse extends VaultResponse {

	@JsonProperty("data")
	public List<WorkflowAction> getData() {
		return (List<WorkflowAction>) this.get("data");
	}

	public void setData(List<WorkflowAction> data) {
		this.set("data", data);
	}

	public static class WorkflowAction extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}
	}
}
