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

/**
 * Model for Document Role Check for Document Change Control response
 */
public class DocumentRoleCheckForDCCResponse extends VaultResponse {

	@JsonProperty("data")
	public DocumentRoleCheckForDCC getData() {
		return (DocumentRoleCheckForDCC) this.get("data");
	}

	public void setData(DocumentRoleCheckForDCC data) {
		this.set("data", data);
	}

	public class DocumentRoleCheckForDCC extends VaultModel {

		@JsonProperty("check_result")
		public Boolean getCheckResult() {
			return this.getBoolean("check_result");
		}

		public void setCheckResult(Boolean checkResult) {
			this.set("check_result", checkResult);
		}
	}
}