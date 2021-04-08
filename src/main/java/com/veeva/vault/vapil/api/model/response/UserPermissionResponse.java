/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

public class UserPermissionResponse extends VaultResponse {

	@JsonProperty("data")
	public List<UserPermissions> getData() {
		return (List<UserPermissions>) this.get("data");
	}

	public void setData(List<UserPermissions> data) {
		this.set("data", data);
	}

	public static class UserPermissions extends VaultModel {

		@JsonProperty("name__v")
		public String getName() {
			return this.getString("name__v");
		}

		public void setName(String name) {
			this.set("name__v", name);
		}

		@JsonProperty("permissions")
		public PermissionSet getPermissions() {
			return (PermissionSet) this.get("permissions");
		}

		public void setPermissions(PermissionSet permissions) {
			this.set("permissions", permissions);
		}

		public static class PermissionSet extends VaultModel {

			@JsonProperty("create")
			public Boolean getCreate() {
				return this.getBoolean("create");
			}

			public void setCreate(Boolean create) {
				this.set("create", create);
			}

			@JsonProperty("delete")
			public Boolean getDelete() {
				return this.getBoolean("delete");
			}

			public void setDelete(Boolean delete) {
				this.set("delete", delete);
			}

			@JsonProperty("edit")
			public Boolean getEdit() {
				return this.getBoolean("edit");
			}

			public void setEdit(Boolean edit) {
				this.set("edit", edit);
			}

			@JsonProperty("read")
			public Boolean getRead() {
				return this.getBoolean("read");
			}

			public void setRead(Boolean read) {
				this.set("read", read);
			}
		}
	}

}
