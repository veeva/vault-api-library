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
 * Response model for the following API calls:
 * <p>
 * PUT /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}
 * POST /api/{version}/objects/{documents_or_binders}/lifecycle_actions
 */
public class DocumentActionResponse extends VaultResponse {

	@JsonProperty("lifecycle_actions__v")
	public List<LifecycleAction> getLifecycleActions() {
		return (List<LifecycleAction>) this.get("lifecycle_actions__v");
	}

	@JsonProperty("lifecycle_actions__v")
	public void setLifecycleActions(List<LifecycleAction> lifecycleActions) {
		this.set("lifecycle_actions__v", lifecycleActions);
	}

	public static class LifecycleAction extends VaultModel {

		@JsonProperty("entry_requirements__v")
		public String getEntryRequirements() {
			return this.getString("entry_requirements__v");
		}

		@JsonProperty("entry_requirements__v")
		public void setEntryRequirements(String entryRequirements) {
			this.set("entry_requirements__v", entryRequirements);
		}

		@JsonProperty("label__v")
		public String getLabel() {
			return this.getString("label__v");
		}

		@JsonProperty("label__v")
		public void setLabel(String label) {
			this.set("label__v", label);
		}

		@JsonProperty("lifecycle__v")
		public String getLifecycle() {
			return this.getString("lifecycle__v");
		}

		@JsonProperty("lifecycle__v")
		public void setLifecycle(String lifecycle) {
			this.set("lifecycle__v", lifecycle);
		}

		@JsonProperty("lifecycle_action_type__v")
		public String getLifecycleActionType() {
			return this.getString("lifecycle_action_type__v");
		}

		@JsonProperty("lifecycle_action_type__v")
		public void setLifecycleActionType(String lifecycleActionType) {
			this.set("lifecycle_action_type__v", lifecycleActionType);
		}

		@JsonProperty("name__v")
		public String getName() {
			return this.getString("name__v");
		}

		@JsonProperty("name__v")
		public void setName(String name) {
			this.set("name__v", name);
		}

		@JsonProperty("state__v")
		public String getState() {
			return this.getString("state__v");
		}

		@JsonProperty("state__v")
		public void setState(String state) {
			this.set("state__v", state);
		}
	}
}