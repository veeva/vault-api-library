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
 * Model for the Object Record data
 */
public class ObjectRecordType extends VaultModel {

	@JsonProperty("active")
	public Boolean getActive() {
		return this.getBoolean("active");
	}

	public void setActive(Boolean active) {
		this.set("active", active);
	}

	@JsonProperty("additional_type_validations")
	public List<String> getAdditionalTypeValidations() {
		return (List<String>) this.get("additional_type_validations");
	}

	public void setAdditionalTypeValidations(List<String> additionalTypeValidations) {
		this.set("additional_type_validations", additionalTypeValidations);
	}

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

	@JsonProperty("object")
	public String getObject() {
		return this.getString("object");
	}

	public void setObject(String object) {
		this.set("object", object);
	}

	@JsonProperty("type_actions")
	public List<ObjectRecordTypeAction> getTypeActions() {
		return (List<ObjectRecordTypeAction>) this.get("type_actions");
	}

	public void setTypeActions(List<ObjectRecordTypeAction> typeActions) {
		this.set("type_actions", typeActions);
	}

	@JsonProperty("type_fields")
	public List<ObjectRecordTypeField> getTypeFields() {
		return (List<ObjectRecordTypeField>) this.get("type_fields");
	}

	@JsonProperty("type_fields")
	public void setTypeFields(List<ObjectRecordTypeField> typeFields) {
		this.set("type_fields", typeFields);
	}

	@JsonProperty("localized_data")
	public LocalizedData getLocalizedData() {
		return (LocalizedData) this.get("localized_data");
	}

	public void setLocalizedData(LocalizedData localizedData) {
		this.set("localized_data", localizedData);
	}

	public static class LocalizedData extends VaultModel {

		@JsonProperty("label")
		public Label getLabel() {
			return (Label) this.get("label");
		}

		public void setLabel(Label label) {
			this.set("label", label);
		}

		@JsonProperty("label_plural")
		public LabelPlural getLabelPlural() {
			return (LabelPlural) this.get("label_plural");
		}

		public void setLabelPlural(LabelPlural labelPlural) {
			this.set("label_plural", labelPlural);
		}

		public static class Label extends VaultModel {

		}

		public static class LabelPlural extends VaultModel {

		}
	}

	public static class ObjectRecordTypeAction extends VaultModel {

		@JsonProperty("action")
		public String getAction() {
			return this.getString("action");
		}

		public void setAction(String action) {
			this.set("action", action);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}
	}

	public static class ObjectRecordTypeField extends VaultModel {

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		public void setRequired(Boolean required) {
			this.set("required", required);
		}

		@JsonProperty("source")
		public String getSource() {
			return this.getString("source");
		}

		public void setSource(String source) {
			this.set("source", source);
		}
	}
}
