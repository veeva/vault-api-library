/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
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

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/documents/actions
 */
public class DocumentWorkflowResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DocumentWorkflow> getData() {
		return (List<DocumentWorkflow>) this.get("data");
	}

	public void setData(List<DocumentWorkflow> data) {
		this.set("data", data);
	}

	public static class DocumentWorkflow extends VaultModel {

		@JsonProperty("cardinality")
		public String getCardinality() {
			return this.getString("cardinality");
		}

		public void setCardinality(String cardinality) {
			this.set("cardinality", cardinality);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}
}