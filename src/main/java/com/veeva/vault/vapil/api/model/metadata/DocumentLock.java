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

public class DocumentLock extends VaultModel {

	@JsonProperty("disabled")
	public Boolean getDisabled() {
		return this.getBoolean("disabled");
	}

	public void setDisabled(Boolean disabled) {
		this.set("disabled", disabled);
	}

	@JsonProperty("editable")
	public Boolean getEditable() {
		return this.getBoolean("editable");
	}

	public void setEditable(Boolean editable) {
		this.set("editable", editable);
	}

	@JsonProperty("hidden")
	public Boolean getHidden() {
		return this.getBoolean("hidden");
	}

	public void setHidden(Boolean hidden) {
		this.set("hidden", hidden);
	}

	@JsonProperty("label")
	public String getLabel() {
		return this.getString("label");
	}

	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("objectType")
	public String getObjectType() {
		return this.getString("objectType");
	}

	public void setObjectType(String objectType) {
		this.set("objectType", objectType);
	}

	@JsonProperty("required")
	public Boolean getRequired() {
		return this.getBoolean("required");
	}

	public void setRequired(Boolean required) {
		this.set("required", required);
	}

	@JsonProperty("scope")
	public String getScope() {
		return this.getString("scope");
	}

	public void setScope(String scope) {
		this.set("scope", scope);
	}

	@JsonProperty("setOnCreateOnly")
	public Boolean getSetOnCreateOnly() {
		return this.getBoolean("setOnCreateOnly");
	}

	public void setSetOnCreateOnly(Boolean setOnCreateOnly) {
		this.set("setOnCreateOnly", setOnCreateOnly);
	}

	@JsonProperty("systemAttribute")
	public Boolean getSystemAttribute() {
		return this.getBoolean("systemAttribute");
	}

	public void setSystemAttribute(Boolean systemAttribute) {
		this.set("systemAttribute", systemAttribute);
	}

	@JsonProperty("type")
	public String getType() {
		return this.getString("type");
	}

	public void setType(String type) {
		this.set("type", type);
	}
}
