/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for User metadata
 */
public class User extends VaultModel {

	@JsonProperty("editable")
	public Boolean getEditable() {
		return getBoolean("editable");
	}

	public void setEditable(Boolean editable) {
		this.set("editable", editable);
	}

	@JsonProperty("length")
	public Integer getLength() {
		return getInteger("length");
	}

	public void setLength(Integer length) {
		this.set("length", length);
	}

	@JsonProperty("multivalue")
	public Boolean getMultivalue() {
		return getBoolean("multivalue");
	}

	public void setMultivalue(Boolean multivalue) {
		this.set("multivalue", multivalue);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("onCreateEditable")
	public Boolean getOnCreateEditable() {
		return getBoolean("onCreateEditable");
	}

	public void setOnCreateEditable(Boolean onCreateEditable) {
		this.set("onCreateEditable", onCreateEditable);
	}

	@JsonProperty("queryable")
	public Boolean getQueryable() {
		return getBoolean("queryable");
	}

	public void setQueryable(Boolean queryable) {
		this.set("queryable", queryable);
	}

	@JsonProperty("required")
	public Boolean getRequired() {
		return getBoolean("required");
	}

	public void setRequired(Boolean required) {
		this.set("required", required);
	}

	@JsonProperty("type")
	public String getType() {
		return getString("type");
	}

	public void setType(String type) {
		this.set("type", type);
	}

	@JsonProperty("values")
	public List<Values> getValues() {
		return (List) get("values");
	}

	public void setValues(List<Values> values) {
		this.set("values", values);
	}

	static public class Values extends VaultModel {
		@JsonProperty("label")
		public String getLabel() {
			return getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("value")
		public String getValue() {
			return getString("value");
		}

		public void setValue(String value) {
			this.set("value", value);
		}
	}
}

