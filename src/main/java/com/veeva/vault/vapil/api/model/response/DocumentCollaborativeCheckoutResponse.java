/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model for the Undo Collaborative Authoring Checkout response:<br>
 * DELETE
 * /api/{version}/objects/documents/batch/lock
 */
public class DocumentCollaborativeCheckoutResponse extends VaultResponse {

	@JsonProperty("data")
	public List<UndoCheckoutResponse> getData() {
		return (List<UndoCheckoutResponse>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<UndoCheckoutResponse> data) {
		this.set("data", data);
	}

	public static class UndoCheckoutResponse extends VaultResponse {
		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		@JsonProperty("responseMessage")
		public String getResponseMessage() {
			return this.getString("responseMessage");
		}

		public void setResponseMessage(String responseMessage) {
			this.set("responseMessage", responseMessage);
		}

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}
	}

}
