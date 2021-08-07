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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/objects/documents/actions/{workflow_name}
 */
public class InitiateDocumentWorkflowResponse extends VaultResponse {

	@JsonProperty("data")
	public DocumentWorkflowData getData() {
		return (DocumentWorkflowData) this.get("data");
	}

	public void setData(DocumentWorkflowData data) {
		this.set("data", data);
	}

	public static class DocumentWorkflowData extends VaultModel {

		@JsonProperty("record_id__v")
		public String getRecordId() {
			return this.getString("record_id__v");
		}

		public void setRecordId(String recordId) {
			this.set("record_id__v", recordId);
		}

		@JsonProperty("record_url")
		public String getRecordUrl() {
			return this.getString("record_url");
		}

		public void setRecordUrl(String recordUrl) {
			this.set("record_url", recordUrl);
		}

		@JsonProperty("workflow_id")
		public String getWorkflowId() {
			return this.getString("workflow_id");
		}

		public void setWorkflowId(String workflowId) {
			this.set("workflow_id", workflowId);
		}
	}
}