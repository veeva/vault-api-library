/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the BinderTemplate and DocumentTemplate Metadata
 */
public class Template extends VaultModel {

	@JsonProperty("component")
	public String getComponent() {
		return getString("component");
	}

	public void setComponent(String component) {
		this.set("component", component);
	}

	@JsonProperty("editable")
	public Boolean getEditable() {
		return getBoolean("editable");
	}

	public void setEditable(Boolean editable) {
		this.set("editable", editable);
	}

	@JsonProperty("enums")
	public List<String> getEnums() {
		return (List<String>) get("enums");
	}

	public void setEnums(List<String> enums) {
		this.set("enums", enums);
	}

	@JsonProperty("max_value")
	public Long getMaxValue() {
		return getLong("max_value");
	}

	public void setMaxValue(Long maxValue) {
		this.set("max_value", maxValue);
	}

	@JsonProperty("min_value")
	public Integer getMinValue() {
		return getInteger("min_value");
	}

	public void setMinValue(Integer minValue) {
		this.set("min_value", minValue);
	}

	@JsonProperty("multi_value")
	public Boolean getMultiValue() {
		return getBoolean("multi_value");
	}

	public void setMultiValue(Boolean multiValue) {
		this.set("multi_value", multiValue);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("ordered")
	public Boolean getOrdered() {
		return getBoolean("ordered");
	}

	public void setOrdered(Boolean ordered) {
		this.set("ordered", ordered);
	}

	@JsonProperty("type")
	public String getType() {
		return getString("type");
	}

	public void setType(String type) {
		this.set("type", type);
	}

	@JsonProperty("requiredness")
	public String getRequiredness() {
		return getString("requiredness");
	}

	public void setRequiredness(String requiredness) {
		this.set("requiredness", requiredness);
	}

	@JsonProperty("scale")
	public Integer getScale() {
		return getInteger("scale");
	}

	public void setScale(Integer scale) {
		this.set("scale", scale);
	}
}