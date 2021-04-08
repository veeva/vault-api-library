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

public class OauthTokenResponse extends VaultModel {

	@JsonProperty("access_token")
	public String getAccessToken() {
		return this.getString("access_token");
	}

	public void setAccessToken(String accessToken) {
		this.set("access_token", accessToken);
	}

	@JsonProperty("id_token")
	public String getIdToken() {
		return this.getString("id_token");
	}

	public void setIdToken(String idToken) {
		this.set("id_token", idToken);
	}

	@JsonProperty("scope")
	public String getScope() {
		return this.getString("scope");
	}

	public void setScope(String scope) {
		this.set("scope", scope);
	}
}