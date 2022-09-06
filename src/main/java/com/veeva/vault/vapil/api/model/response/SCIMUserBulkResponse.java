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
import com.veeva.vault.vapil.api.model.common.SCIMUser;

import java.util.List;

public class SCIMUserBulkResponse extends VaultResponse {

	@JsonIgnore
	@Override
	public boolean isSuccessful() {
		return !hasResourceErrors();
	}

	@Override
	public String getResponseStatus() {
		if (isSuccessful()) {
			return VaultResponse.HTTP_RESPONSE_SUCCESS;
		} else {
			return VaultResponse.HTTP_RESPONSE_FAILURE;
		}
	}

	@Override
	public String getResponseMessage() {
		return this.toJsonString();
	}

	@JsonIgnore
	public boolean hasResourceErrors() {
		return (super.hasErrors() || getResources() == null || getResources().size() == 0);
	}

	@JsonProperty("itemsPerPage")
	public Integer getItemsPerPage() {
		return this.getInteger("itemsPerPage");
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.set("itemsPerPage", itemsPerPage);
	}

	@JsonProperty("Resources")
	public List<SCIMUser> getResources() {
		return (List<SCIMUser>) this.get("Resources");
	}

	public void setResources(List<SCIMUser> resources) {
		this.set("Resources", resources);
	}

	@JsonProperty("schemas")
	public List<String> getSchemas() {
		return (List<String>) this.get("schemas");
	}

	public void setSchemas(List<String> schemas) {
		this.set("schemas", schemas);
	}

	@JsonProperty("startIndex")
	public Integer getStartIndex() {
		return this.getInteger("startIndex");
	}

	public void setStartIndex(Integer startIndex) {
		this.set("startIndex", startIndex);
	}

	@JsonProperty("totalResults")
	public Integer getTotalResults() {
		return this.getInteger("totalResults");
	}

	public void setTotalResults(Integer totalResults) {
		this.set("totalResults", totalResults);
	}
}

