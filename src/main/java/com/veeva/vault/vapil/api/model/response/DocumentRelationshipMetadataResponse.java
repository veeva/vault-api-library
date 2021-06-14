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
 * GET /api/{version}/metadata/objects/documents/types/{type}/relationships
 */
public class DocumentRelationshipMetadataResponse extends VaultResponse {

	@JsonProperty("properties")
	public List<RelationshipProperty> getProperties() {
		return (List<RelationshipProperty>) this.get("properties");
	}

	public void setProperties(List<RelationshipProperty> properties) {
		this.set("properties", properties);
	}

	@JsonProperty("relationships")
	public List<Relationship> getRelationships() {
		return (List<Relationship>) this.get("relationships");
	}

	public void setRelationships(List<Relationship> relationships) {
		this.set("relationships", relationships);
	}

	@JsonProperty("relationshipTypes")
	public List<RelationshipType> getRelationshipTypes() {
		return (List<RelationshipType>) this.get("relationshipTypes");
	}

	public void setRelationshipTypes(List<RelationshipType> relationshipTypes) {
		this.set("relationshipTypes", relationshipTypes);
	}

	public static class Relationship extends VaultModel {

		@JsonProperty("object")
		public RelationshipObject getRelationshipObject() {
			return (RelationshipObject) this.get("object");
		}

		public void setRelationshipObject(RelationshipObject object) {
			this.set("object", object);
		}

		@JsonProperty("relationship_label")
		public String getRelationshipLabel() {
			return this.getString("relationship_label");
		}

		public void setRelationshipLabel(String relationshipLabel) {
			this.set("relationship_label", relationshipLabel);
		}

		@JsonProperty("relationship_name")
		public String getRelationshipName() {
			return this.getString("relationship_name");
		}

		public void setRelationshipName(String relationshipName) {
			this.set("relationship_name", relationshipName);
		}

		@JsonProperty("relationship_type")
		public String getRelationshipType() {
			return this.getString("relationship_type");
		}

		public void setRelationshipType(String relationshipType) {
			this.set("relationship_type", relationshipType);
		}

		public static class RelationshipObject extends VaultModel {

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			public void setName(String name) {
				this.set("name", name);
			}
		}
	}

	public static class RelationshipProperty extends VaultModel {

		@JsonProperty("editable")
		public Boolean getEditable() {
			return this.getBoolean("editable");
		}

		public void setEditable(Boolean editable) {
			this.set("editable", editable);
		}

		@JsonProperty("length")
		public Integer getLength() {
			return this.getInteger("length");
		}

		public void setLength(Integer length) {
			this.set("length", length);
		}

		@JsonProperty("multivalue")
		public Boolean getMultivalue() {
			return this.getBoolean("multivalue");
		}

		public void setMultivalue(Boolean multivalue) {
			this.set("multivalue", multivalue);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("onCreateEditable")
		public Boolean getOnCreateEditable() {
			return this.getBoolean("onCreateEditable");
		}

		public void setOnCreateEditable(Boolean onCreateEditable) {
			this.set("onCreateEditable", onCreateEditable);
		}

		@JsonProperty("queryable")
		public Boolean getQueryable() {
			return this.getBoolean("queryable");
		}

		public void setQueryable(Boolean queryable) {
			this.set("queryable", queryable);
		}

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		public void setRequired(Boolean required) {
			this.set("required", required);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class RelationshipType extends VaultModel {
		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("singleUse")
		public Boolean getSingleUse() {
			return this.getBoolean("singleUse");
		}

		public void setSingleUse(Boolean singleUse) {
			this.set("singleUse", singleUse);
		}

		@JsonProperty("sourceDocVersionSpecific")
		public Boolean getSourceDocVersionSpecific() {
			return this.getBoolean("sourceDocVersionSpecific");
		}

		public void setSourceDocVersionSpecific(Boolean sourceDocVersionSpecific) {
			this.set("sourceDocVersionSpecific", sourceDocVersionSpecific);
		}

		@JsonProperty("system")
		public Boolean getSystem() {
			return this.getBoolean("system");
		}

		public void setSystem(Boolean system) {
			this.set("system", system);
		}

		@JsonProperty("targetDocumentTypes")
		public List<TargetDocumentType> getTargetDocumentTypes() {
			return (List<TargetDocumentType>) this.get("targetDocumentTypes");
		}

		public void setTargetDocumentTypes(List<TargetDocumentType> targetDocumentTypes) {
			this.set("targetDocumentTypes", targetDocumentTypes);
		}

		@JsonProperty("targetDocVersionSpecific")
		public Boolean getTargetDocVersionSpecific() {
			return this.getBoolean("targetDocVersionSpecific");
		}

		public void setTargetDocVersionSpecific(Boolean targetDocVersionSpecific) {
			this.set("targetDocVersionSpecific", targetDocVersionSpecific);
		}

		@JsonProperty("value")
		public String getValue() {
			return this.getString("value");
		}

		public void setValue(String value) {
			this.set("value", value);
		}

		public static class TargetDocumentType extends VaultModel {
			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("value")
			public String getValue() {
				return this.getString("value");
			}

			public void setValue(String value) {
				this.set("value", value);
			}
		}
	}
}