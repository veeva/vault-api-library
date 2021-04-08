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
 * Model for the Object Record Role API responses:
 * <p>
 * /api/{version}/vobjects/{object_name}/{id}/roles{/role_name}
 */
public class ObjectRecordRoleChangeResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRoleChange> getData() {
		return (List<ObjectRoleChange>) this.get("data");
	}

	public void setData(List<ObjectRoleChange> data) {
		this.set("data", data);
	}

	static public class ObjectRoleChange extends VaultModel {

		@JsonProperty("data")
		public ObjectRole getData() {
			return (ObjectRole) this.get("data");
		}

		public void setData(ObjectRole data) {
			this.set("data", data);
		}

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		static public class ObjectRole extends VaultModel {

			@JsonProperty("id")
			public String getId() {
				return this.getString("id");
			}

			public void setId(String id) {
				this.set("id", id);
			}
		}
	}
}