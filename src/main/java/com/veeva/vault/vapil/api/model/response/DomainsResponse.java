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

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/domain
 * <p>
 * There are two different response message formats, depending on whether the
 * API user is a domain admin.
 */
public class DomainsResponse extends VaultResponse {

	@JsonProperty("domains")
	public List<Domain> getDomains() {
		return (List<Domain>) this.get("domains");
	}

	public void setDomains(List<Domain> domains) {
		this.set("domains", domains);
	}

	public static class Domain extends VaultModel {

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
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