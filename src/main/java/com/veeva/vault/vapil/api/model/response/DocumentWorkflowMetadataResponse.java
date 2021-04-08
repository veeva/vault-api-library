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
 * Model for the document response
 */
public class DocumentWorkflowMetadataResponse extends VaultResponse {

	@JsonProperty("properties")
	public List<Property> getProperties() {
		return (List<Property>) this.get("properties");
	}

	public void setProperties(List<Property> properties) {
		this.set("properties", properties);
	}

	public static class Property extends VaultModel {

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("multivalue")
		public Boolean getMultivalue() {
			return this.getBoolean("multivalue");
		}

		@JsonProperty("multivalue")
		public void setMultivalue(Boolean multivalue) {
			this.set("multivalue", multivalue);
		}

		@JsonProperty("length")
		public Integer getLength() {
			return this.getInteger("length");
		}

		public void setLength(Integer length) {
			this.set("length", length);
		}

		@JsonProperty("values")
		public List<Value> getValues() {
			return (List<Value>) this.get("values");
		}

		public void setValues(List<Value> values) {
			this.set("values", values);
		}

		@JsonProperty("object")
		public String getObject() {
			return this.getString("object");
		}

		public void setObject(String object) {
			this.set("object", object);
		}
	}

	public static class Value extends VaultModel {

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