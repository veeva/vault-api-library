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

public class SandboxEntitlementResponse extends VaultResponse {

	@JsonProperty("data")
	public SandboxEntitlement getData() {
		return (SandboxEntitlement) this.get("data");
	}

	public void setData(SandboxEntitlement data) {
		this.set("data", data);
	}

	public static class SandboxEntitlement extends VaultModel {

		@JsonProperty("entitlements")
		public List<Entitlement> getEntitlements() {
			return (List<Entitlement>) this.get("entitlements");
		}

		public void setEntitlements(List<Entitlement> entitlements) {
			this.set("entitlements", entitlements);
		}

		@JsonProperty("parent_vault_id")
		public Integer getParentVaultId() {
			return this.getInteger("parent_vault_id");
		}

		public void setParentVaultId(Integer parentVaultId) {
			this.set("parent_vault_id", parentVaultId);
		}

		public static class Entitlement extends VaultModel {

			@JsonProperty("in_use")
			public Integer getInUse() {
				return this.getInteger("in_use");
			}

			public void setInUse(Integer inUse) {
				this.set("in_use", inUse);
			}

			@JsonProperty("total")
			public Integer getTotal() {
				return this.getInteger("total");
			}

			public void setTotal(Integer total) {
				this.set("total", total);
			}

			@JsonProperty("type")
			public String getType() {
				return this.getString("type");
			}

			public void setType(String type) {
				this.set("type", type);
			}
		}

	}
}
