/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the Vault Object Field metadata
 */
public class VaultObjectField extends VaultModel {

	@JsonProperty("checkbox")
	public Boolean getCheckbox() {
		return this.getBoolean("checkbox");
	}

	public void setCheckbox(Boolean checkbox) {
		this.set("checkbox", checkbox);
	}

	@JsonProperty("component")
	public String getComponent() {
		return this.getString("component");
	}

	public void setComponent(String component) {
		this.set("component", component);
	}

	@JsonProperty("created_by")
	public Integer getCreatedBy() {
		return this.getInteger("created_by");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by", createdBy);
	}

	@JsonProperty("created_date")
	public String getCreatedDate() {
		return this.getString("created_date");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date", createdDate);
	}

	@JsonProperty("create_object_inline")
	public Boolean getCreateObjectInline() {
		return this.getBoolean("create_object_inline");
	}

	public void setCreateObjectInline(Boolean createObjectInline) {
		this.set("create_object_inline", createObjectInline);
	}

	@JsonProperty("editable")
	public Boolean getEditable() {
		return this.getBoolean("editable");
	}

	public void setEditable(Boolean editable) {
		this.set("editable", editable);
	}

	@JsonProperty("encrypted")
	public Boolean getEncrypted() {
		return this.getBoolean("encrypted");
	}

	public void setEncrypted(Boolean encrypted) {
		this.set("encrypted", encrypted);
	}

	@JsonProperty("facetable")
	public Boolean getFacetable() {
		return this.getBoolean("facetable");
	}

	public void setFacetable(Boolean facetable) {
		this.set("facetable", facetable);
	}

	@JsonProperty("help_content")
	public String getHelpContent() {
		return this.getString("help_content");
	}

	public void setHelpContent(String helpContent) {
		this.set("help_content", helpContent);
	}

	@JsonProperty("label")
	public String getLabel() {
		return this.getString("label");
	}

	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("list_column")
	public Boolean getListColumn() {
		return this.getBoolean("list_column");
	}

	public void setListColumn(Boolean listColumn) {
		this.set("list_column", listColumn);
	}

	@JsonProperty("lookup_relationship_name")
	public String getLookupRelationshipName() {
		return this.getString("lookup_relationship_name");
	}

	public void setLookupRelationshipName(String lookupRelationshipName) {
		this.set("lookup_relationship_name", lookupRelationshipName);
	}

	@JsonProperty("lookup_source_field")
	public String getLookupSourceField() {
		return this.getString("lookup_source_field");
	}

	public void setLookupSourceField(String lookupSourceField) {
		this.set("lookup_source_field", lookupSourceField);
	}

	@JsonProperty("max_length")
	public Integer getMaxLength() {
		return this.getInteger("max_length");
	}

	public void setMaxLength(Integer maxLength) {
		this.set("max_length", maxLength);
	}

	@JsonProperty("max_value")
	public Long getMaxValue() {
		return this.getLong("max_value");
	}

	public void setMaxValue(Long maxValue) {
		this.set("max_value", maxValue);
	}

	@JsonProperty("min_value")
	public Long getMinValue() {
		return this.getLong("min_value");
	}

	public void setMinValue(Long minValue) {
		this.set("min_value", minValue);
	}

	@JsonProperty("modified_by")
	public Integer getModifiedBy() {
		return this.getInteger("modified_by");
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.set("modified_by", modifiedBy);
	}

	@JsonProperty("modified_date")
	public String getModifiedDate() {
		return this.getString("modified_date");
	}

	public void setModifiedDate(String modifiedDate) {
		this.set("modified_date", modifiedDate);
	}

	@JsonProperty("multi_part_field")
	public String getMultiPartField() {
		return this.getString("multi_part_field");
	}

	public void setMultiPartField(String multiPartField) {
		this.set("multi_part_field", multiPartField);
	}

	@JsonProperty("multi_part_readonly")
	public String getMultiPartReadonly() {
		return this.getString("multi_part_readonly");
	}

	public void setMultiPartReadonly(String multiPartReadonly) {
		this.set("multi_part_readonly", multiPartReadonly);
	}

	@JsonProperty("multi_value")
	public Boolean getMultiValue() {
		return this.getBoolean("multi_value");
	}

	public void setMultiValue(Boolean multiValue) {
		this.set("multi_value", multiValue);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("no_copy")
	public Boolean getNoCopy() {
		return this.getBoolean("no_copy");
	}

	public void setNoCopy(Boolean noCopy) {
		this.set("no_copy", noCopy);
	}

	@JsonProperty("object")
	public ObjectReference getObjectReference() {
		return (ObjectReference) this.get("object");
	}

	public void setObjectReference(ObjectReference objectReference) {
		this.set("object", objectReference);
	}

	@JsonProperty("order")
	public Integer getOrder() {
		return this.getInteger("order");
	}

	public void setOrder(Integer order) {
		this.set("order", order);
	}

	@JsonProperty("picklist")
	public String getPicklist() {
		return this.getString("picklist");
	}

	public void setPicklist(String picklist) {
		this.set("picklist", picklist);
	}

	@JsonProperty("relationship_criteria")
	public String getRelationshipCriteria() {
		return this.getString("relationship_criteria");
	}

	public void setRelationshipCriteria(String relationshipCriteria) {
		this.set("relationship_criteria", relationshipCriteria);
	}

	@JsonProperty("relationship_deletion")
	public String getRelationshipDeletion() {
		return this.getString("relationship_deletion");
	}

	public void setRelationshipDeletion(String relationshipDeletion) {
		this.set("relationship_deletion", relationshipDeletion);
	}

	@JsonProperty("relationship_inbound_label")
	public String getRelationshipInboundLabel() {
		return this.getString("relationship_inbound_label");
	}

	public void setRelationshipInboundLabel(String relationshipInboundLabel) {
		this.set("relationship_inbound_label", relationshipInboundLabel);
	}

	@JsonProperty("relationship_inbound_name")
	public String getRelationshipInboundName() {
		return this.getString("relationship_inbound_name");
	}

	public void setRelationshipInboundName(String relationshipInboundName) {
		this.set("relationship_inbound_name", relationshipInboundName);
	}

	@JsonProperty("relationship_outbound_name")
	public String getRelationshipOutboundName() {
		return this.getString("relationship_outbound_name");
	}

	public void setRelationshipOutboundName(String relationshipOutboundName) {
		this.set("relationship_outbound_name", relationshipOutboundName);
	}

	@JsonProperty("relationship_type")
	public String getRelationshipType() {
		return this.getString("relationship_type");
	}

	public void setRelationshipType(String relationshipType) {
		this.set("relationship_type", relationshipType);
	}

	@JsonProperty("required")
	public Boolean getRequired() {
		return this.getBoolean("required");
	}

	public void setRequired(Boolean required) {
		this.set("required", required);
	}

	@JsonProperty("scale")
	public Integer getScale() {
		return this.getInteger("scale");
	}

	public void setScale(Integer scale) {
		this.set("scale", scale);
	}

	@JsonProperty("secure_relationship")
	public Boolean getSecureRelationship() {
		return this.getBoolean("secure_relationship");
	}

	public void setSecureRelationship(Boolean secureRelationship) {
		this.set("secure_relationship", secureRelationship);
	}

	@JsonProperty("sequential_naming")
	public Boolean getSequentialNaming() {
		return this.getBoolean("sequential_naming");
	}

	public void setSequentialNaming(Boolean sequentialNaming) {
		this.set("sequential_naming", sequentialNaming);
	}

	@JsonProperty("source")
	public String getSource() {
		return this.getString("source");
	}

	public void setSource(String source) {
		this.set("source", source);
	}

	@JsonProperty("start_number")
	public Integer getStartNumber() {
		return this.getInteger("start_number");
	}

	public void setStartNumber(Integer startNumber) {
		this.set("start_number", startNumber);
	}

	@JsonProperty("status")
	public List<String> getStatus() {
		return (List) this.get("status");
	}

	public void setStatus(List<String> status) {
		this.set("status", status);
	}

	@JsonProperty("system_managed_name")
	public Boolean getSystemManagedName() {
		return this.getBoolean("system_managed_name");
	}

	public void setSystemManagedName(Boolean systemManagedName) {
		this.set("system_managed_name", systemManagedName);
	}

	@JsonProperty("type")
	public String getType() {
		return this.getString("type");
	}

	public void setType(String type) {
		this.set("type", type);
	}

	@JsonProperty("value_format")
	public String getValueFormat() {
		return this.getString("value_format");
	}

	public void setValueFormat(String valueFormat) {
		this.set("value_format", valueFormat);
	}

	@JsonProperty("unique")
	public Boolean getUnique() {
		return this.getBoolean("unique");
	}

	public void setUnique(Boolean unique) {
		this.set("unique", unique);
	}

	public static class ObjectReference extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("label_plural")
		public String getLabelPlural() {
			return this.getString("label_plural");
		}

		public void setLabelPlural(String labelPlural) {
			this.set("label_plural", labelPlural);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("prefix")
		public String getPrefix() {
			return this.getString("prefix");
		}

		public void setPrefix(String prefix) {
			this.set("prefix", prefix);
		}

		@JsonProperty("url")
		public String getUrl() {
			return this.getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}
	}

}
