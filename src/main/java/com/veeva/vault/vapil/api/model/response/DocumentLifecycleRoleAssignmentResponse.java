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
 * Model for the Document Lifecycle Role Assignment response
 */
public class DocumentLifecycleRoleAssignmentResponse extends VaultResponse {

	@JsonProperty("data")
	public List<RoleAssignment> getData() {
		return (List<RoleAssignment>) this.get("data");
	}

	public void setData(List<RoleAssignment> data) {
		this.set("data", data);
	}

	public static class RoleAssignment extends VaultModel {

		@JsonProperty("allowed_default_groups__v")
		public List<String> getAllowedDefaultGroups() {
			return (List<String>) this.get("allowed_default_groups__v");
		}

		public void setAllowedDefaultGroups(List<String> allowedDefaultGroups) {
			this.set("allowed_default_groups__v", allowedDefaultGroups);
		}

		@JsonProperty("allowed_default_users__v")
		public List<String> getAllowedDefaultUsers() {
			return (List<String>) this.get("allowed_default_users__v");
		}

		public void setAllowedDefaultUsers(List<String> allowedDefaultUsers) {
			this.set("allowed_default_users__v", allowedDefaultUsers);
		}

		@JsonProperty("allowed_groups__v")
		public List<String> getAllowedGroups() {
			return (List<String>) this.get("allowed_groups__v");
		}

		public void setAllowedGroups(List<String> allowedGroups) {
			this.set("allowed_groups__v", allowedGroups);
		}

		@JsonProperty("allowed_users__v")
		public List<String> getAllowedUsers() {
			return (List<String>) this.get("allowed_users__v");
		}

		public void setAllowedUsers(List<String> allowedUsers) {
			this.set("allowed_users__v", allowedUsers);
		}

		@JsonProperty("country__v")
		public String getCountry() {
			return this.getString("country__v");
		}

		public void setCountry(String country) {
			this.set("country__v", country);
		}

		@JsonProperty("lifecycle__v")
		public String getLifecycle() {
			return this.getString("lifecycle__v");
		}

		@JsonProperty("lifecycle__v")
		public void setLifecycle(String lifecycle) {
			this.set("lifecycle__v", lifecycle);
		}

		@JsonProperty("role__v")
		public String getRole() {
			return this.getString("role__v");
		}

		@JsonProperty("role__v")
		public void setRole(String role) {
			this.set("role__v", role);
		}

		@JsonProperty("product__v")
		public String getProduct() {
			return this.getString("product__v");
		}

		@JsonProperty("product__v")
		public void setProduct(String product) {
			this.set("product__v", product);
		}
	}
}