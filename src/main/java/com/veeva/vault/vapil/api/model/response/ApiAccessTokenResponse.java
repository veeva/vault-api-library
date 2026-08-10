/*---------------------------------------------------------------------
 *	Copyright (c) 2026 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API call responses:
 * <p>
 * POST /api/{version}/objects/users/{user_id}/api_access_token__sys
 */
public class ApiAccessTokenResponse extends VaultResponse {

	@JsonProperty("data")
	public Data getData() {
		return (Data) this.get("data");
	}

	public void setData(Data data) {
		this.set("data", data);
	}

	public static class Data extends VaultModel {

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("token__sys")
		public String getToken() {
			return this.getString("token__sys");
		}

		public void setToken(String token) {
			this.set("token__sys", token);
		}
	}
}
