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
 * Model for the Object Record Role API responses:
 * <p>
 * /api/{version}/vobjects/{object_name}/{id}/roles{/role_name}
 */
public class ObjectRecordRoleResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRole> getData() {
		return (List<ObjectRole>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<ObjectRole> data) {
		this.set("data", data);
	}

	static public class ObjectRole extends VaultModel {

		@JsonProperty("assignment_type")
		public String getAssignmentType() {
			return this.getString("assignment_type");
		}

		public void setAssignmentType(String assignmentType) {
			this.set("assignment_type", assignmentType);
		}

		@JsonProperty("groups")
		public List<Integer> getGroups() {
			return (List<Integer>) this.get("groups");
		}

		public void setGroups(List<Integer> groups) {
			this.set("groups", groups);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("users")
		public List<Integer> getUsers() {
			return (List<Integer>) this.get("users");
		}

		public void setUsers(List<Integer> users) {
			this.set("users", users);
		}
	}
}