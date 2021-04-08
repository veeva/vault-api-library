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
 * Model for lifecycle Role Assignment Rules Response API
 */
public class DocumentLifecycleDeleteOverrideRulesResponse extends VaultResponse {

	@JsonProperty("data")
	public DeleteOverrideRule getData() {
		return (DeleteOverrideRule) this.get("data");
	}

	public void setData(DeleteOverrideRule data) {
		this.set("data", data);
	}

	public static class DeleteOverrideRule extends VaultModel {

		@JsonProperty("rules_deleted")
		public Integer getRulesDeleted() {
			return this.getInteger("rules_deleted");
		}

		public void setRulesDeleted(Integer rulesDeleted) {
			this.set("rules_deleted", rulesDeleted);
		}
	}

}