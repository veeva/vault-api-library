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
 * GET /api/{version}/objects/domain
 * <p>
 * There are two different response message formats, depending on whether the
 * API user is a domain admin.
 */
public class DomainResponse extends VaultResponse {

	@JsonProperty("domain__v")
	public Domain getDomain() {
		return (Domain) this.get("domain__v");
	}

	@JsonProperty("domain__v")
	public void setDomain(Domain domain) {
		this.set("domain__v", domain);
	}

	public static class Domain extends VaultModel {

		@JsonProperty("domain_name__v")
		public String getDomainName() {
			return this.getString("domain_name__v");
		}

		public void setDomainName(String domainName) {
			this.set("domain_name__v", domainName);
		}

		@JsonProperty("domain_type__v")
		public String getDomainType() {
			return this.getString("domain_type__v");
		}

		public void setDomainType(String domainType) {
			this.set("domain_type__v", domainType);
		}

		@JsonProperty("vaults__v")
		public List<DomainVault> getVaults() {
			return (List<DomainVault>) this.get("vaults__v");
		}

		public void setVaults(List<DomainVault> vaults) {
			this.set("vaults__v", vaults);
		}

		public static class DomainVault extends VaultModel {

			@JsonProperty("id")
			public String getId() {
				return this.getString("id");
			}

			public void setId(String id) {
				this.set("id", id);
			}

			@JsonProperty("vault_name__v")
			public String getVaultName() {
				return this.getString("vault_name__v");
			}

			public void setVaultName(String vaultName) {
				this.set("vault_name__v", vaultName);
			}

			@JsonProperty("vault_status__v")
			public String getVaultStatus() {
				return this.getString("vault_status__v");
			}

			public void setVaultStatus(String vaultStatus) {
				this.set("vault_status__v", vaultStatus);
			}

			@JsonProperty("vault_application__v")
			public String getVaultApplication() {
				return this.getString("vault_application__v");
			}

			public void setVaultApplication(String vaultApplication) {
				this.set("vault_application__v", vaultApplication);
			}

			@JsonProperty("vault_family__v")
			public VaultFamily getVaultFamily() {
				return (VaultFamily) this.get("vault_family__v");
			}

			public void setVaultFamily(VaultFamily vaultFamily) {
				this.set("vault_family__v", vaultFamily);
			}

			public static class VaultFamily extends VaultModel {

				@JsonProperty("name__v")
				public String getName() {
					return this.getString("name__v");
				}

				public void setName(String name) {
					this.set("name__v", name);
				}

				@JsonProperty("label__v")
				public String getLabel() {
					return this.getString("label__v");
				}

				public void setLabel(String label) {
					this.set("label__v", label);
				}
			}
		}
	}
}