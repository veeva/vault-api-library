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
 * Model for the following API calls responses:
 * <p>
 * POST https://{vaultDNS}/api/{version}/auth
 */
public class AuthenticationResponse extends VaultResponse {

	@JsonProperty("sessionId")
	public String getSessionId() {
		return this.getString("sessionId");
	}

	public void setSessionId(String sessionId) {
		this.set("sessionId", sessionId);
	}

	@JsonProperty("userId")
	public String getUserId() {
		return this.getString("userId");
	}

	public void setUserId(String userId) {
		this.set("userId", userId);
	}

	@JsonProperty("vaultId")
	public Integer getVaultId() {
		return this.getInteger("vaultId");
	}

	public void setVaultId(Integer vaultId) {
		this.set("vaultId", vaultId);
	}

	@JsonProperty("vaultIds")
	public List<Vault> getVaultIds() {
		return (List<Vault>) this.get("vaultIds");
	}

	public void setVaultIds(List<Vault> vaultIds) {
		this.set("vaultIds", vaultIds);
	}

	public static class Vault extends VaultModel {

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("url")
		public String getUrl() {
			return this.getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}
	}
}
