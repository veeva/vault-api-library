/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.DocumentField;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the document classification response
 */
public class DocumentClassificationResponse extends VaultResponse {

	@JsonProperty("availableLifecycles")
	public List<AvailableLifecycle> getAvailableLifecycles() {
		return (List<AvailableLifecycle>) this.get("availableLifecycles");
	}

	@JsonProperty("availableLifecycles")
	public void setAvailableLifecycles(List<AvailableLifecycle> availableLifecycles) {
		this.set("availableLifecycles", availableLifecycles);
	}

	@JsonProperty("defaultWorkflows")
	public List<Object> getDefaultWorkflows() {
		return (List<Object>) this.get("defaultWorkflows");
	}

	@JsonProperty("defaultWorkflows")
	public void setDefaultWorkflows(List<Object> defaultWorkflows) {
		this.set("defaultWorkflows", defaultWorkflows);
	}

	@JsonProperty("label")
	public String getLabel() {
		return this.getString("label");
	}

	@JsonProperty("label")
	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("properties")
	public List<DocumentField> getProperties() {
		return (List<DocumentField>) this.get("properties");
	}

	@JsonProperty("properties")
	public void setProperties(List<DocumentField> properties) {
		this.set("properties", properties);
	}

	@JsonProperty("renditions")
	public List<String> getRenditions() {
		return (List<String>) this.get("renditions");
	}

	@JsonProperty("renditions")
	public void setRenditions(List<String> renditions) {
		this.set("renditions", renditions);
	}

	@JsonProperty("relationshipTypes")
	public List<RelationshipType> getRelationshipTypes() {
		return (List<RelationshipType>) this.get("relationshipTypes");
	}

	@JsonProperty("relationshipTypes")
	public void setRelationshipTypes(List<RelationshipType> relationshipTypes) {
		this.set("relationshipTypes", relationshipTypes);
	}

	@JsonProperty("templates")
	public List<Template> getTemplates() {
		return (List<Template>) this.get("templates");
	}

	@JsonProperty("templates")
	public void setTemplates(List<Template> templates) {
		this.set("templates", templates);
	}

	public static class AvailableLifecycle extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}
	}

	public static class RelationshipType extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("value")
		public String getValue() {
			return this.getString("value");
		}

		@JsonProperty("value")
		public void setValue(String value) {
			this.set("value", value);
		}
	}

	public static class Template extends VaultModel {

		@JsonProperty("definedIn")
		public String getDefinedIn() {
			return this.getString("definedIn");
		}

		@JsonProperty("definedIn")
		public void setDefinedIn(String definedIn) {
			this.set("definedIn", definedIn);
		}

		@JsonProperty("definedInType")
		public String getDefinedInType() {
			return this.getString("definedInType");
		}

		@JsonProperty("definedInType")
		public void setDefinedInType(String definedInType) {
			this.set("definedInType", definedInType);
		}

		@JsonProperty("kind")
		public String getKind() {
			return this.getString("kind");
		}

		@JsonProperty("kind")
		public void setKind(String kind) {
			this.set("kind", kind);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		@JsonProperty("label")
		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		@JsonProperty("name")
		public void setName(String name) {
			this.set("name", name);
		}
	}
}