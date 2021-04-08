package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SCIMAttribute extends VaultModel {

	@JsonProperty("canonicalValues")
	public List<String> getCanonicalValues() {
		return (List<String>) this.get("canonicalValues");
	}

	public void setCanonicalValues(List<String> canonicalValues) {
		this.set("canonicalValues", canonicalValues);
	}

	@JsonProperty("caseExact")
	public Boolean getCaseExact() {
		return this.getBoolean("caseExact");
	}

	public void setCaseExact(Boolean caseExact) {
		this.set("caseExact", caseExact);
	}

	@JsonProperty("description")
	public String getDescription() {
		return this.getString("description");
	}

	public void setDescription(String description) {
		this.set("description", description);
	}

	@JsonProperty("mutability")
	public String getMutability() {
		return this.getString("mutability");
	}

	public void setMutability(String mutability) {
		this.set("mutability", mutability);
	}

	@JsonProperty("multiValued")
	public Boolean getMultiValued() {
		return this.getBoolean("multiValued");
	}

	public void setMultiValued(Boolean multiValued) {
		this.set("multiValued", multiValued);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("referenceTypes")
	public List<String> getReferenceTypes() {
		return (List<String>) this.get("referenceTypes");
	}

	public void setReferenceTypes(List<String> referenceTypes) {
		this.set("referenceTypes", referenceTypes);
	}

	@JsonProperty("required")
	public Boolean getRequired() {
		return this.getBoolean("required");
	}

	public void setRequired(Boolean required) {
		this.set("required", required);
	}

	@JsonProperty("returned")
	public String getReturned() {
		return this.getString("returned");
	}

	public void setReturned(String returned) {
		this.set("returned", returned);
	}

	@JsonProperty("subAttributes")
	public List<SCIMAttribute> getSubAttributes() {
		return (List<SCIMAttribute>) this.get("subAttributes");
	}

	public void setSubAttributes(List<SCIMAttribute> subAttributes) {
		this.set("subAttributes", subAttributes);
	}

	@JsonProperty("type")
	public String getType() {
		return this.getString("type");
	}

	public void setType(String type) {
		this.set("type", type);
	}

	@JsonProperty("value")
	public String getValue() {
		return this.getString("value");
	}

	public void setValue(String value) {
		this.set("value", value);
	}

	@JsonProperty("uniqueness")
	public String getUniqueness() {
		return this.getString("uniqueness");
	}

	public void setUniqueness(String uniqueness) {
		this.set("uniqueness", uniqueness);
	}

	public boolean hasSubAttributeErrors() {
		if (getSubAttributes() == null || getSubAttributes().size() == 0) return true;
		return false;
	}
}
