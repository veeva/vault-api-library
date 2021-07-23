/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses for the Object Workflow Action Details
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions <br>
 * GET
 * /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}
 */
public class ObjectWorkflowActionDetailsResponse extends VaultResponse {

	@JsonProperty("data")
	public WorkflowAction getData() {
		return (WorkflowAction) this.get("data");
	}

	public void setData(WorkflowAction data) {
		this.set("data", data);
	}

	public class WorkflowAction extends VaultModel {

		@JsonProperty("controls")
		public List<Control> getControls() {
			return (List<Control>) this.get("controls");
		}

		public void setControls(List<Control> controls) {
			this.set("controls", controls);
		}

		@JsonProperty("label")
		public String getLabel() {
			return getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}
	}

	public class Control extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("prompts")
		public List<Prompt> getPrompts() {
			return (List<Prompt>) this.get("prompts");
		}

		public void setPrompts(List<Prompt> prompts) {
			this.set("prompts", prompts);
		}

		@JsonProperty("current_values")
		public List<CurrentValue> getCurrentValues() {
			return (List<CurrentValue>) this.get("current_values");
		}

		public void setCurrentValues(List<CurrentValue> currentValues) {
			this.set("current_values", currentValues);
		}
	}

	public class Prompt extends VaultModel {

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

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		public void setRequired(Boolean required) {
			this.set("required", required);
		}
	}

	public class CurrentValue extends VaultModel {

		@JsonProperty("document_id__v")
		public String getDocumentId() {
			return getString("document_id__v");
		}

		public void setDocumentId(String documentId) {
			this.set("document_id__v", documentId);
		}
	}
}
