/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<UserResponse> getData() {
		return (List<UserResponse>) this.get("data");
	}

	public void setData(List<UserResponse> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public boolean hasUserResultErrors() {
		if (super.hasErrors()) return true;

		List<UserResponse> results = getData();
		if (results == null || results.size() == 0)
			return true;
		else {
			for (UserResponse response : results) {
				if (!response.isSuccessful())
					return true;
			}
		}

		return false;
	}
}
