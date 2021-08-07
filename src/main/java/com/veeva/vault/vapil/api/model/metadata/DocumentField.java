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

public class DocumentField extends VaultModel {

	@JsonProperty("definedIn")
	public String getDefinedIn() {
		return this.getString("definedIn");
	}

	public void setDefinedIn(String definedIn) {
		this.set("definedIn", definedIn);
	}

	@JsonProperty("definedInType")
	public String getDefinedInType() {
		return this.getString("definedInType");
	}

	public void setDefinedInType(String definedInType) {
		this.set("definedInType", definedInType);
	}

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

	@JsonProperty("facetable")
	public Boolean getFacetable() {
		return this.getBoolean("facetable");
	}

	public void setFacetable(Boolean facetable) {
		this.set("facetable", facetable);
	}

	@JsonProperty("helpContent")
	public String getHelpContent() {
		return this.getString("helpContent");
	}

	public void setHelpContent(String helpContent) {
		this.set("helpContent", helpContent);
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

	@JsonProperty("maxLength")
	public Integer getMaxLength() {
		return this.getInteger("maxLength");
	}

	public void setMaxLength(Integer maxLength) {
		this.set("maxLength", maxLength);
	}

	@JsonProperty("maxValue")
	public Long getMaxValue() {
		return this.getLong("maxValue");
	}

	public void setMaxValue(Long maxValue) {
		this.set("maxValue", maxValue);
	}

	@JsonProperty("minValue")
	public Long getMinValue() {
		return this.getLong("minValue");
	}

	public void setMinValue(Long minValue) {
		this.set("minValue", minValue);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("queryable")
	public Boolean getQueryable() {
		return this.getBoolean("queryable");
	}

	public void setQueryable(Boolean queryable) {
		this.set("queryable", queryable);
	}

	@JsonProperty("repeating")
	public Boolean getRepeating() {
		return this.getBoolean("repeating");
	}

	public void setRepeating(Boolean repeating) {
		this.set("repeating", repeating);
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

	@JsonProperty("section")
	public String getSection() {
		return this.getString("section");
	}

	public void setSection(String section) {
		this.set("section", section);
	}

	@JsonProperty("sectionPosition")
	public Integer getSectionPosition() {
		return this.getInteger("sectionPosition");
	}

	public void setSectionPosition(Integer sectionPosition) {
		this.set("sectionPosition", sectionPosition);
	}

	@JsonProperty("setOnCreateOnly")
	public Boolean getSetOnCreateOnly() {
		return this.getBoolean("setOnCreateOnly");
	}

	public void setSetOnCreateOnly(Boolean setOnCreateOnly) {
		this.set("setOnCreateOnly", setOnCreateOnly);
	}

	@JsonProperty("shared")
	public Boolean getShared() {
		return this.getBoolean("shared");
	}

	public void setShared(Boolean shared) {
		this.set("shared", shared);
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

	@JsonProperty("usedIn")
	public List<UsedIn> getUsedIn() {
		return (List<UsedIn>) this.get("usedIn");
	}

	public void setUsedIn(List<UsedIn> usedIn) {
		this.set("usedIn", usedIn);
	}

	public static class UsedIn extends VaultModel {
		@JsonProperty("key")
		public String getKey() {
			return this.getString("key");
		}

		public void setKey(String key) {
			this.set("key", key);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}
}
