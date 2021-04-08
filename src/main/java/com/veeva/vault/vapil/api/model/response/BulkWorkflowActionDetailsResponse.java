package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/object/workflow/actions/{action}
 */
public class BulkWorkflowActionDetailsResponse extends VaultResponse {

	@JsonProperty("data")
	public BulkWorkflowActionDetails getData() {
		return (BulkWorkflowActionDetails) get("data");
	}

	public void setData(BulkWorkflowActionDetails data) {
		this.set("data", data);
	}

	public class BulkWorkflowActionDetails extends VaultModel {

		@JsonProperty("controls")
		public List<Control> getControls() {
			return (List<Control>) get("controls");
		}

		public void setControls(List<Control> controls) {
			this.set("controls", controls);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}
	}

	public static class Control extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("prompts")
		public List<BulkWorkflowActionDetailsResponse.Prompt> getPrompts() {
			return (List<Prompt>) get("prompts");
		}

		public void setPrompts(List<Prompt> prompts) {
			this.set("prompts", prompts);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class Prompt extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
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

		@JsonProperty("required")
		public Boolean getRequired() {
			return this.getBoolean("required");
		}

		public void setRequired(Boolean required) {
			this.set("required", required);
		}
	}
}
