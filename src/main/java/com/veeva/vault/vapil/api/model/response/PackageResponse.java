/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.PackageComponent;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET https://login.veevavault.com/auth/discovery
 * <p>
 * Discover the authentication type of a user. With this API,
 * applications can dynamically adjust the the login requirements per user,
 * and support either username/password or OAuth2.0 / OpenID Connect authentication schemes.
 */
public class PackageResponse extends VaultResponse {

	@JsonProperty("vaultPackage")
	public VaultPackage getVaultPackage() {
		return (VaultPackage) this.get("vaultPackage");
	}

	public void setVaultPackage(VaultPackage vaultPackage) {
		this.set("vaultPackage", vaultPackage);
	}

	public static class VaultPackage extends VaultModel {

		@JsonProperty("components")
		public List<PackageComponent> getComponents() {
			return (List<PackageComponent>) this.get("components");
		}

		public void setComponents(List<PackageComponent> components) {
			this.set("components", components);
		}

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("oldName")
		public String getOldName() {
			return this.getString("oldName");
		}

		public void setOldName(String oldName) {
			this.set("oldName", oldName);
		}

		@JsonProperty("renamed")
		public Boolean getRenamed() {
			return this.getBoolean("renamed");
		}

		public void setRenamed(Boolean renamed) {
			this.set("renamed", renamed);
		}

		@JsonProperty("status")
		public String getStatus() {
			return this.getString("status");
		}

		public void setStatus(String status) {
			this.set("status", status);
		}
	}
}