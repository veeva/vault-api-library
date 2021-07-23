/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.User;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

public class UserRetrieveResponse extends VaultResponse {

	@JsonProperty("users")
	public List<UserNode> getUsers() {
		return (List<UserNode>) this.get("users");
	}

	public void setUsers(List<UserNode> users) {
		this.set("users", users);
	}

	public static class UserNode extends VaultModel {

		@JsonProperty("user")
		public User getUser() {
			return (User) this.get("user");
		}

		public void setUser(User user) {
			this.set("user", user);
		}
	}
}
