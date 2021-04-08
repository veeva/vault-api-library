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
 * Model for the following API calls responses:
 * <p>
 * GET https://login.veevavault.com/auth/discovery
 * <p>
 * Discover the authentication type of a user. With this API,
 * applications can dynamically adjust the the login requirements per user,
 * and support either username/password or OAuth2.0 / OpenID Connect authentication schemes.
 */
public class DiscoveryResponse extends VaultResponse {

	@JsonProperty("data")
	public DiscoveryData getData() {
		return (DiscoveryData) get("data");
	}

	@JsonProperty("data")
	public void setData(DiscoveryData data) {
		this.set("data", data);
	}

	public static class DiscoveryData extends VaultModel {

		@JsonProperty("auth_profiles")
		public List<AuthProfile> getAuthProfiles() {
			return (List<AuthProfile>) this.get("auth_profiles");
		}

		@JsonProperty("auth_profiles")
		public void setAuthProfiles(List<AuthProfile> authProfiles) {
			this.set("auth_profiles", authProfiles);
		}

		@JsonProperty("auth_type")
		public String getAuthType() {
			return this.getString("auth_type");
		}

		@JsonProperty("auth_type")
		public void setAuthType(String authType) {
			this.set("auth_type", authType);
		}

		public static class AuthProfile extends VaultModel {

			@JsonProperty("as_client_id")
			public String getAsClientId() {
				return this.getString("as_client_id");
			}

			@JsonProperty("as_client_id")
			public void setAsClientId(String asClientId) {
				this.set("as_client_id", asClientId);
			}

			@JsonProperty("as_metadata")
			public AsMetadata getAsMetadata() {
				return (AsMetadata) this.get("as_metadata");
			}

			@JsonProperty("as_metadata")
			public void setAsMetadata(AsMetadata asMetadata) {
				this.set("as_metadata", asMetadata);
			}

			@JsonProperty("description")
			public String getDescription() {
				return this.getString("description");
			}

			@JsonProperty("description")
			public void setDescription(String description) {
				this.set("description", description);
			}

			@JsonProperty("id")
			public String getId() {
				return this.getString("id");
			}

			@JsonProperty("id")
			public void setId(String id) {
				this.set("id", id);
			}

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			@JsonProperty("label")
			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("use_adal")
			public Boolean getUseAdal() {
				return this.getBoolean("use_adal");
			}

			@JsonProperty("use_adal")
			public void setUseAdal(Boolean useAdal) {
				this.set("use_adal", useAdal);
			}

			@JsonProperty("vault_session_endpoint")
			public String getVaultSessionEndpoint() {
				return this.getString("vault_session_endpoint");
			}

			@JsonProperty("vault_session_endpoint")
			public void setVaultSessionEndpoint(String vaultSessionEndpoint) {
				this.set("vault_session_endpoint", vaultSessionEndpoint);
			}

			public static class AsMetadata extends VaultModel {
				@JsonProperty("token_endpoint")
				public String getTokenEndpoint() {
					return this.getString("token_endpoint");
				}

				@JsonProperty("token_endpoint")
				public void setTokenEndpoint(String tokenEndpoint) {
					this.set("token_endpoint", tokenEndpoint);
				}
			}
		}
	}
}