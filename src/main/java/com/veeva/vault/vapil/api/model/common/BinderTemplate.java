/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the BinderTemplate request and responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinderTemplate extends VaultModel {

	@JsonProperty("active__v")
	public Boolean getActive() {
		return getBoolean("active__v");
	}

	public void setActive(Boolean active) {
		set("active__v", active);
	}

	@JsonProperty("classification__v")
	public String getClassification() {
		return getString("classification__v");
	}

	public void setClassification(String classification) {
		set("classification__v", classification);
	}

	@JsonProperty("filing_model__v")
	public String getFilingModel() {
		return getString("filing_model__v");
	}

	public void setFilingModel(String filingModel) {
		set("filing_model__v", filingModel);
	}

	@JsonProperty("hierarchy_mapping__v")
	public String getHierarchyMapping() {
		return getString("hierarchy_mapping__v");
	}

	public void setHierarchyMapping(String hierarchyMapping) {
		set("hierarchy_mapping__v", hierarchyMapping);
	}

	@JsonProperty("id")
	public String getId() {
		return getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("label__v")
	public String getLabel() {
		return getString("label__v");
	}

	public void setLabel(String label) {
		set("label__v", label);
	}

	@JsonProperty("lifecycle__v")
	public String getLifecycle() {
		return getString("lifecycle__v");
	}

	public void setLifecycle(String lifecycle) {
		set("lifecycle__v", lifecycle);
	}

	@JsonProperty("name__v")
	public String getName() {
		return getString("name__v");
	}

	public void setName(String name) {
		set("name__v", name);
	}

	@JsonProperty("node_type__v")
	public String getNodeType() {
		return getString("node_type__v");
	}

	public void setNodeType(String nodeType) {
		set("node_type__v", nodeType);
	}

	@JsonIgnore
	public void setNodeType(NodeType nodeType) {
		set("node_type__v", nodeType.getValue());
	}

	@JsonProperty("number__v")
	public String getNumber() {
		return getString("number__v");
	}

	public void setNumber(String number) {
		set("number__v", number);
	}

	@JsonProperty("order__v")
	public String getOrder() {
		return getString("order__v");
	}

	public void setOrder(String order) {
		set("order__v", order);
	}

	@JsonProperty("parent_id__v")
	public String getParentId() {
		return getString("parent_id__v");
	}

	public void setParentId(String parentId) {
		this.set("parent_id__v", parentId);
	}

	@JsonProperty("subtype__v")
	public String getSubType() {
		return getString("subtype__v");
	}

	public void setSubType(String subtype) {
		set("subtype__v", subtype);
	}

	@JsonProperty("type__v")
	public String getType() {
		return getString("type__v");
	}

	public void setType(String type) {
		set("type__v", type);
	}

	public enum NodeType {
		SECTION("section"),
		PLANNED_DOCUMENT("planned_document");

		private String type;

		NodeType(String type) {
			this.type = type;
		}

		public String getValue() {
			return type;
		}
	}
}
