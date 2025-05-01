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
 * Retrieve the entry criteria for a workflow.
 * <p>
 * GET /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}/entry_requirements
 */

public class DocumentActionEntryCriteriaResponse extends VaultResponse {

	@JsonProperty("properties")
	public List<Property> getProperties() {
		return (List<Property>) this.get("properties");
	}

	@JsonProperty("properties")
	public void setProperties(List<Property> properties) {
		this.set("properties", properties);
	}

	public static class Property extends VaultModel {

		@JsonProperty("description")
		public String getDescription() {
			return this.getString("description");
		}

		@JsonProperty("description")
		public void setDescription(String description) {
			this.set("description", description);
		}

		@JsonProperty("editable")
		public Boolean getEditable() {
			return this.getBoolean("editable");
		}

		@JsonProperty("editable")
		public void setEditable(Boolean editable) {
			this.set("editable", editable);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("objectTypeReferenced")
		public ObjectTypeReferenced getObjectTypeReferenced() {
			return (ObjectTypeReferenced) this.get("objectTypeReferenced");
		}

		@JsonProperty("objectTypeReferenced")
		public void setObjectTypeReferenced(ObjectTypeReferenced objectTypeReferenced) {
			this.set("objectTypeReferenced", objectTypeReferenced);
		}

		@JsonProperty("repeating")
		public Boolean getRepeating() {
			return this.getBoolean("repeating");
		}

		@JsonProperty("repeating")
		public void setRepeating(Boolean repeating) {
			this.set("repeating", repeating);
		}

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		@JsonProperty("required")
		public void setRequired(Boolean required) {
			this.set("required", required);
		}

		@JsonProperty("scope")
		public String getScope() {
			return this.getString("scope");
		}

		@JsonProperty("scope")
		public void setScope(String scope) {
			this.set("scope", scope);
		}

		@JsonProperty("type")
		public List<String> getType() {
			return (List<String>) get("type");
		}

		@JsonProperty("type")
		public void setType(List<String> type) {
			this.set("type", type);
		}

		public static class ObjectTypeReferenced extends VaultModel {

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			@JsonProperty("label")
			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			@JsonProperty("name")
			public void setName(String name) {
				this.set("name", name);
			}
		}
	}
}
