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
 * Model for the following API calls
 * <br>
 * GET /api/{version}/objects/picklists/{picklist_name}
 */
public class PicklistValueResponse extends VaultResponse {

	@JsonProperty("picklistValues")
	public List<PicklistValue> getPicklistValues() {
		return (List<PicklistValue>) this.get("picklistValues");
	}

	public void setPicklistValues(List<PicklistValue> picklistValues) {
		this.set("picklistValues", picklistValues);
	}

	public static class PicklistValue extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("label")
		public String getLabel() {
			return getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}
	}
}