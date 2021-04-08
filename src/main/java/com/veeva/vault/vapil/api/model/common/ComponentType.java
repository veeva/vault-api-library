/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the metadata component types
 */
public class ComponentType extends VaultModel {

	@JsonProperty("active")
	public Boolean getActive() {
		return getBoolean("active");
	}

	public void setActive(Boolean active) {
		this.set("active", active);
	}

	@JsonProperty("abbreviation")
	public String getAbbreviation() {
		return getString("abbreviation");
	}

	public void setAbbreviation(String abbreviation) {
		this.set("abbreviation", abbreviation);
	}

	@JsonProperty("attributes")
	public List<Attribute> getAttributes() {
		return (List) get("attributes");
	}

	public void setAttributes(List<Attribute> attributes) {
		this.set("attributes", attributes);
	}

	@JsonProperty("class")
	public String getClassName() {
		return getString("class");
	}

	public void setClassName(String className) {
		this.set("class", className);
	}

	@JsonProperty("label")
	public String getLabel() {
		return getString("label");
	}

	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("label_plural")
	public String getLabelPlural() {
		return getString("label_plural");
	}

	public void setLabelPlural(String labelPlural) {
		this.set("label_plural", labelPlural);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("sub_components")
	public List<SubComponentType> getSubComponents() {
		return (List) get("sub_components");
	}

	public void setSubComponents(List<SubComponentType> subComponents) {
		this.set("sub_components", subComponents);
	}

	@JsonProperty("url")
	public String getUrl() {
		return getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}

	static public class Attribute extends VaultModel {

		@JsonProperty("editable")
		public Boolean getEditable() {
			return getBoolean("editable");
		}

		public void setEditable(Boolean editable) {
			this.set("editable", editable);
		}

		@JsonProperty("max_length")
		public Boolean getMaxLength() {
			return getBoolean("max_length");
		}

		public void setMaxLength(Boolean maxLength) {
			this.set("max_length", maxLength);
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

		@JsonProperty("type")
		public String getType() {
			return getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	static public class SubComponentType extends VaultModel {

		@JsonProperty("attributes")
		public List<Attribute> getAttributes() {
			return (List) get("attributes");
		}

		public void setAttributes(List<Attribute> attributes) {
			this.set("attributes", attributes);
		}

		@JsonProperty("json_collection_name")
		public String getJsonCollectionName() {
			return getString("json_collection_name");
		}

		public void setJsonCollectionName(String jsonCollectionName) {
			this.set("json_collection_name", jsonCollectionName);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}
	}
}
