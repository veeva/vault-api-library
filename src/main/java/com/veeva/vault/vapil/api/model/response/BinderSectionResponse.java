/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.Binder.Node;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the binder section response, needed to handle:
 * <br>
 * 1. The binder section id is a string (not an integer).
 * <br>
 * 2. Handling the response differences between other binder retrieves for this API endpoint:
 * <br>
 * /objects/binders/{binder_id}/sections/{section_id}
 */
public class BinderSectionResponse extends VaultResponse {

	@JsonProperty("id")
	public String getId() {
		return this.getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("node")
	public Node getNode() {
		return (Node) this.get("node");
	}

	public void setNode(Node node) {
		this.set("node", node);
	}
}