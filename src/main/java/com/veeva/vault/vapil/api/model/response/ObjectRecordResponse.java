/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.VaultObject;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses for the Object Record CRUD
 * operations:
 * <p>
 * GET /api/{version}/vobjects/{object_name} <br>
 * GET /api/{version}/vobjects/{object_name}/{object_record_id}
 */
public class ObjectRecordResponse extends VaultResponse {

	@JsonProperty("data")
	public ObjectRecord getData() {
		return (ObjectRecord) this.get("data");
	}

	public void setData(ObjectRecord data) {
		this.set("data", data);
	}

	@JsonProperty("manually_assigned_sharing_roles")
	public Map<String, java.lang.Object> getManuallyAssignedSharingRoles() {
		return (Map<String, java.lang.Object>) this.get("manually_assigned_sharing_roles");
	}

	public void setManuallyAssignedSharingRoles(Map<String, java.lang.Object> manuallyAssignedSharingRoles) {
		this.set("manually_assigned_sharing_roles", manuallyAssignedSharingRoles);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("object")
		public VaultObject getObject() {
			return (VaultObject) this.get("object");
		}

		public void setObject(VaultObject object) {
			this.set("object", object);
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