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
 * Response model for Bulk Update Node Order
 */
public class EDLBulkUpdateNodeOrderResponse extends VaultResponse {

	@JsonProperty("data")
	public List<NodeOrderResult> getData() {
		return (List<NodeOrderResult>) this.get("data");
	}

	public void setData(List<NodeOrderResult> data) {
		this.set("data", data);
	}

	public static class NodeOrderResult extends VaultModel {

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		@JsonProperty("data")
		public NodeOrderData getData() {
			return (NodeOrderData) this.get("data");
		}

		public void setData(NodeOrderData data) {
			this.set("data", data);
		}

		public static class NodeOrderData extends VaultModel {

			@JsonProperty("parent_id")
			public String getParentId() {
				return this.getString("parent_id");
			}

			public void setParentId(String parentId) {
				this.set("parent_id", parentId);
			}

			@JsonProperty("id")
			public String getId() {
				return this.getString("id");
			}

			public void setId(String id) {
				this.set("id", id);
			}

			@JsonProperty("order__v")
			public Integer getOrder() {
				return this.getInteger("order__v");
			}

			public void setOrder(Integer order) {
				this.set("order__v", order);
			}
		}
	}
}
