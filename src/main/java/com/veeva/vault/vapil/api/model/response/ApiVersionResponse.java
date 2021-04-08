/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.Set;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api
 */
public class ApiVersionResponse extends VaultResponse {

	@JsonProperty("values")
	public ApiVersion getValues() {
		return (ApiVersion) this.get("values");
	}

	public void setValues(ApiVersion values) {
		this.set("values", values);
	}

	public static class ApiVersion extends VaultModel {

		@JsonIgnore
		public Set<String> getVersions() {
			return this.getVaultModelData().keySet();
		}

		@JsonIgnore
		public String getVersionUrl(String version) {
			return (String) this.getVaultModelData().get(version);
		}
	}
}