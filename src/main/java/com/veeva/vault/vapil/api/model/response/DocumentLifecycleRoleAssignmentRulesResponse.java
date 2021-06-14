/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for lifecycle Role Assignment Rules Response API
 */
public class DocumentLifecycleRoleAssignmentRulesResponse extends VaultResponse {

	@JsonProperty("data")
	public List<VaultResponse> getData() {
		return (List<VaultResponse>) this.get("data");
	}

	public void setData(List<VaultResponse> data) {
		this.set("data", data);
	}

	@JsonIgnore
	@Override
	public boolean hasErrors() {
		if (super.hasErrors())
			return true;

		List<VaultResponse> responses = getData();
		if (responses == null || responses.size() == 0)
			return true;
		else {
			for (VaultResponse response : responses) {
				if (!response.isSuccessful())
					return true;
			}
		}

		return false;
	}
}