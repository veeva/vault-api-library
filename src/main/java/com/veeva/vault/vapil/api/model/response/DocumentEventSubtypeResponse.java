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
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/metadata/objects/documents/events
 */
public class DocumentEventSubtypeResponse extends VaultResponse {

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

	@JsonProperty("properties")
	public List<DocumentEventSubtype> getProperties() {
		return (List<DocumentEventSubtype>) this.get("properties");
	}

	@JsonProperty("properties")
	public void setProperties(List<DocumentEventSubtype> properties) {
		this.set("properties", properties);
	}

	public static class DocumentEventSubtype extends VaultModel {

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		@JsonProperty("type")
		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		@JsonProperty("required")
		public void setRequired(Boolean required) {
			this.set("required", required);
		}

		@JsonProperty("systemAttribute")
		public Boolean getSystemAttribute() {
			return this.getBoolean("systemAttribute");
		}

		@JsonProperty("systemAttribute")
		public void setSystemAttribute(Boolean systemAttribute) {
			this.set("systemAttribute", systemAttribute);
		}

		@JsonProperty("queryable")
		public Boolean getQueryable() {
			return this.getBoolean("queryable");
		}

		@JsonProperty("queryable")
		public void setQueryable(Boolean queryable) {
			this.set("queryable", queryable);
		}

		@JsonProperty("values")
		public List<Value> getValues() {
			return (List<Value>) this.get("values");
		}

		@JsonProperty("values")
		public void setValues(List<Value> values) {
			this.set("values", values);
		}

		public static class Value extends VaultModel {
			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			@JsonProperty("name")
			public void setName(String name) {
				this.set("name", name);
			}

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			@JsonProperty("label")
			public void setLabel(String label) {
				this.set("label", label);
			}
		}
	}
}