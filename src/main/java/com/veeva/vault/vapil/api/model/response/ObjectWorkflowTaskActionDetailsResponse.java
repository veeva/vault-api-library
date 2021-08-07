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
 * Model for the following API calls responses for the Object Workflow Task Action Details
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions <br>
 * GET
 * /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}
 */
public class ObjectWorkflowTaskActionDetailsResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectWorkflowTaskAction> getData() {
		return (List<ObjectWorkflowTaskAction>) this.get("data");
	}

	public void setData(List<ObjectWorkflowTaskAction> data) {
		this.set("data", data);
	}

	public static class ObjectWorkflowTaskAction extends VaultModel {

		@JsonProperty("controls")
		public List<Control> getControls() {
			return (List<Control>) this.get("controls");
		}

		public void setControls(List<Control> controls) {
			this.set("controls", controls);
		}

		@JsonProperty("description")
		public String getDescription() {
			return this.getString("description");
		}

		public void setDescription(String description) {
			this.set("description", description);
		}

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

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}
	}

	public static class Control extends VaultModel {

		@JsonProperty("capacity")
		public String getCapacity() {
			return this.getString("capacity");
		}

		public void setCapacity(String capacity) {
			this.set("capacity", capacity);
		}

		@JsonProperty("instructions")
		public String getInstructions() {
			return this.getString("instructions");
		}

		public void setInstructions(String instructions) {
			this.set("instructions", instructions);
		}

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

		@JsonProperty("reason")
		public String getReason() {
			return this.getString("reason");
		}

		public void setReason(String reason) {
			this.set("reason", reason);
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

		@JsonProperty("verdict")
		public String getVerdict() {
			return this.getString("verdict");
		}

		public void setVerdict(String verdict) {
			this.set("verdict", verdict);
		}

		@JsonProperty("verdicts")
		public List<Verdict> getVerdicts() {
			return (List<Verdict>) this.get("verdicts");
		}

		public void setVerdicts(List<Verdict> verdicts) {
			this.set("verdicts", verdicts);
		}

		public static class Verdict extends VaultModel {

			@JsonProperty("esignature")
			public Boolean getEsignature() {
				return this.getBoolean("esignature");
			}

			public void setEsignature(Boolean esignature) {
				this.set("esignature", esignature);
			}

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

			@JsonProperty("prompts")
			public List<Prompt> getPrompts() {
				return (List<Prompt>) this.get("prompts");
			}

			public void setPrompts(List<Prompt> prompts) {
				this.set("prompts", prompts);
			}

			@JsonProperty("reasons")
			public List<Reason> getReasons() {
				return (List<Reason>) this.get("reasons");
			}

			public void setReasons(List<Reason> reasons) {
				this.set("reasons", reasons);
			}
		}

		public static class Reason extends VaultModel {

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

		public static class Prompt extends VaultModel {

			@JsonProperty("current_value")
			public List<String> getCurrentValue() {
				return (List<String>) this.get("current_value");
			}

			public void setCurrentValue(List<String> currentValue) {
				this.set("current_value", currentValue);
			}

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

			@JsonProperty("multi_value")
			public Boolean getMultiValue() {
				return this.getBoolean("multi_value");
			}

			public void setMultiValue(Boolean multiValue) {
				this.set("multi_value", multiValue);
			}

			@JsonProperty("type")
			public String getType() {
				return this.getString("type");
			}

			public void setType(String type) {
				this.set("type", type);
			}
		}
	}
}
