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
 * Model for the Vault Object metadata
 */
public class VaultObject extends VaultModel {

	@JsonProperty("allow_attachments")
	public Boolean getAllowAttachments() {
		return getBoolean("allow_attachments");
	}

	public void setAllowAttachments(Boolean allowAttachments) {
		this.set("allow_attachments", allowAttachments);
	}

	@JsonProperty("allow_types")
	public Boolean getAllowTypes() {
		return getBoolean("allow_types");
	}

	public void setAllowTypes(Boolean allowTypes) {
		this.set("allow_types", allowTypes);
	}

	@JsonProperty("available_lifecycles")
	public List<String> getAvailableLifecycles() {
		return (List) get("available_lifecycles");
	}

	public void setAvailableLifecycles(List<String> availableLifecycles) {
		this.set("available_lifecycles", availableLifecycles);
	}

	@JsonProperty("auditable")
	public Boolean getAuditable() {
		return getBoolean("auditable");
	}

	public void setAuditable(Boolean auditable) {
		this.set("auditable", auditable);
	}

	@JsonProperty("created_by")
	public Integer getCreatedBy() {
		return getInteger("created_by");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by", createdBy);
	}

	@JsonProperty("created_date")
	public String getCreatedDate() {
		return getString("created_date");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date", createdDate);
	}

	@JsonProperty("default_obj_type")
	public String getDefaultObjType() {
		return getString("default_obj_type");
	}

	public void setDefaultObjType(String defaultObjType) {
		this.set("default_obj_type", defaultObjType);
	}

	@JsonProperty("description")
	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String description) {
		this.set("description", description);
	}

	@JsonProperty("dynamic_security")
	public Boolean getDynamicSecurity() {
		return getBoolean("dynamic_security");
	}

	public void setDynamicSecurity(Boolean dynamicSecurity) {
		this.set("dynamic_security", dynamicSecurity);
	}

	@JsonProperty("enable_esignatures")
	public Boolean getEnableEsignatures() {
		return getBoolean("enable_esignatures");
	}

	public void setEnableEsignatures(Boolean enableEsignatures) {
		this.set("enable_esignatures", enableEsignatures);
	}

	@JsonProperty("fields")
	public List<VaultObjectField> getFields() {
		return (List) get("fields");
	}

	public void setFields(List<VaultObjectField> fields) {
		this.set("fields", fields);
	}

	@JsonProperty("help_content")
	public String getHelpContent() {
		return getString("help_content");
	}

	public void setHelpContent(String helpContent) {
		this.set("help_content", helpContent);
	}

	@JsonProperty("in_menu")
	public Boolean getInMenu() {
		return getBoolean("in_menu");
	}

	public void setInMenu(Boolean inMenu) {
		this.set("in_menu", inMenu);
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

	@JsonProperty("modified_by")
	public Integer getModifiedBy() {
		return getInteger("modified_by");
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.set("modified_by", modifiedBy);
	}

	@JsonProperty("modified_date")
	public String getModifiedDate() {
		return getString("modified_date");
	}

	public void setModifiedDate(String modifiedDate) {
		this.set("modified_date", modifiedDate);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("object_class")
	public String getObjectClass() {
		return getString("object_class");
	}

	public void setObjectClass(String objectClass) {
		this.set("object_class", objectClass);
	}

	@JsonProperty("object_types")
	public List<ObjectType> getObjectTypes() {
		return (List) get("object_types");
	}

	public void setObjectTypes(List<ObjectType> objectTypes) {
		this.set("object_types", objectTypes);
	}

	@JsonProperty("order")
	public Integer getOrder() {
		return getInteger("order");
	}

	public void setOrder(Integer order) {
		this.set("order", order);
	}

	@JsonProperty("prefix")
	public String getPrefix() {
		return getString("prefix");
	}

	public void setPrefix(String prefix) {
		this.set("prefix", prefix);
	}

	@JsonProperty("prevent_record_overwrite")
	public Boolean getPreventRecordOverwrite() {
		return getBoolean("prevent_record_overwrite");
	}

	public void setPreventRecordOverwrite(Boolean preventRecordOverwrite) {
		this.set("prevent_record_overwrite", preventRecordOverwrite);
	}

	@JsonProperty("relationships")
	public List<Relationship> getRelationships() {
		return (List) get("relationships");
	}

	public void setRelationships(List<Relationship> relationships) {
		this.set("relationships", relationships);
	}

	@JsonProperty("role_overrides")
	public Boolean getRoleOverrides() {
		return getBoolean("role_overrides");
	}

	public void setRoleOverrides(Boolean roleOverrides) {
		this.set("role_overrides", roleOverrides);
	}

	@JsonProperty("secure_attachments")
	public Boolean getSecureAttachments() {
		return getBoolean("secure_attachments");
	}

	public void setSecureAttachments(Boolean secureAttachments) {
		this.set("secure_attachments", secureAttachments);
	}

	@JsonProperty("secure_sharing_settings")
	public Boolean getSecureSharingSettings() {
		return getBoolean("secure_sharing_settings");
	}

	public void setSecureSharingSettings(Boolean secureSharingSettings) {
		this.set("secure_sharing_settings", secureSharingSettings);
	}

	@JsonProperty("source")
	public String getSource() {
		return getString("source");
	}

	public void setSource(String source) {
		this.set("source", source);
	}

	@JsonProperty("status")
	public List<String> getStatus() {
		return (List) get("status");
	}

	public void setStatus(List<String> status) {
		this.set("status", status);
	}

	@JsonProperty("system_managed")
	public Boolean getSystemManaged() {
		return getBoolean("system_managed");
	}

	public void setSystemManaged(Boolean systemManaged) {
		this.set("system_managed", systemManaged);
	}

	@JsonProperty("url")
	public String getUrl() {
		return getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}

	@JsonProperty("urls")
	public Urls getUrls() {
		return (Urls) get("urls");
	}

	public void setUrls(Urls urls) {
		this.set("urls", urls);
	}

	@JsonProperty("user_role_setup_object")
	public ObjectReference getUserRoleSetupObject() {
		return (ObjectReference) get("user_role_setup_object");
	}

	public void setUserRoleSetupObject(ObjectReference userRoleSetupObject) {
		this.set("user_role_setup_object", userRoleSetupObject);
	}

	public static class ObjectReference extends VaultModel {

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

		@JsonProperty("prefix")
		public String getPrefix() {
			return getString("prefix");
		}

		public void setPrefix(String prefix) {
			this.set("prefix", prefix);
		}

		@JsonProperty("url")
		public String getUrl() {
			return getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}
	}

	public static class ObjectType extends VaultModel {
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

		@JsonProperty("status")
		public List<String> getStatus() {
			return (List) get("status");
		}

		public void setStatus(List<String> status) {
			this.set("status", status);
		}

		@JsonProperty("url")
		public String getUrl() {
			return getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}
	}

	public static class Relationship extends VaultModel {

		@JsonProperty("field")
		public String getField() {
			return getString("field");
		}

		public void setField(String field) {
			this.set("field", field);
		}

		@JsonProperty("object")
		public ObjectReference getObjectReference() {
			return (ObjectReference) get("object");
		}

		public void setObjectReference(ObjectReference objectReference) {
			this.set("object", objectReference);
		}

		@JsonProperty("relationship_deletion")
		public String getRelationshipDeletion() {
			return getString("relationship_deletion");
		}

		public void setRelationshipDeletion(String relationshipDeletion) {
			this.set("relationship_deletion", relationshipDeletion);
		}

		@JsonProperty("relationship_label")
		public String getRelationshipLabel() {
			return getString("relationship_label");
		}

		public void setRelationshipLabel(String relationshipLabel) {
			this.set("relationship_label", relationshipLabel);
		}

		@JsonProperty("relationship_name")
		public String getRelationshipName() {
			return getString("relationship_name");
		}

		public void setRelationshipName(String relationshipName) {
			this.set("relationship_name", relationshipName);
		}

		@JsonProperty("relationship_type")
		public String getRelationshipType() {
			return getString("relationship_type");
		}

		public void setRelationshipType(String relationshipType) {
			this.set("relationship_type", relationshipType);
		}
	}

	public static class Urls extends VaultModel {
		@JsonProperty("field")
		public String getField() {
			return getString("field");
		}

		public void setField(String field) {
			this.set("field", field);
		}

		@JsonProperty("list")
		public String getList() {
			return getString("list");
		}

		public void setList(String list) {
			this.set("list", list);
		}

		@JsonProperty("metadata")
		public String getMetadata() {
			return getString("metadata");
		}

		public void setMetadata(String metadata) {
			this.set("metadata", metadata);
		}

		@JsonProperty("record")
		public String getRecord() {
			return getString("record");
		}

		public void setRecord(String record) {
			this.set("record", record);
		}
	}
}
