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
import com.veeva.vault.vapil.api.model.metadata.VaultObject;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses for the Object WorkflowTask
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows/tasks
 */
public class ObjectWorkflowTaskResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectWorkflowTask> getData() {
		return (List<ObjectWorkflowTask>) this.get("data");
	}

	public void setData(List<ObjectWorkflowTask> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ObjectWorkflowTask extends VaultModel {

		@JsonProperty("assigned_date__v")
		public String getAssignedDate() {
			return this.getString("assigned_date__v");
		}

		public void setAssignedDate(String assignedDate) {
			this.set("assigned_date__v", assignedDate);
		}

		@JsonProperty("assignee__v")
		public String getAssignee() {
			return this.getString("assignee__v");
		}

		public void setAssignee(String assignee) {
			this.set("assignee__v", assignee);
		}

		@JsonProperty("created_date__v")
		public String getCreatedDate() {
			return this.getString("created_date__v");
		}

		public void setCreatedDate(String createdDate) {
			this.set("created_date__v", createdDate);
		}

		@JsonProperty("due_date__v")
		public String getDueDate() {
			return this.getString("due_date__v");
		}

		public void setDueDate(String dueDate) {
			this.set("due_date__v", dueDate);
		}

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("instructions__v")
		public String getInstructions() {
			return this.getString("instructions__v");
		}

		public void setInstructions(String instructions) {
			this.set("instructions__v", instructions);
		}

		@JsonProperty("label__v")
		public String getLabel() {
			return this.getString("label__v");
		}

		public void setLabel(String label) {
			this.set("label__v", label);
		}

		@JsonProperty("object__v")
		public String getObject() {
			return this.getString("object__v");
		}

		public void setObject(String object) {
			this.set("object__v", object);
		}

		@JsonProperty("record_id__v")
		public String getRecordId() {
			return this.getString("record_id__v");
		}

		public void setRecordId(String recordId) {
			this.set("record_id__v", recordId);
		}

		@JsonProperty("status__v")
		public List<String> getStatus() {
			return (List<String>) this.get("status__v");
		}

		public void setStatus(List<String> status) {
			this.set("status__v", status);
		}

		@JsonProperty("workflow__v")
		public String getWorkflow() {
			return this.getString("workflow__v");
		}

		public void setWorkflow(String workflow) {
			this.set("workflow__v", workflow);
		}

		@JsonProperty("workflow_class__sys")
		public String getWorkflowClass() {
			return this.getString("workflow_class__sys");
		}

		public void setWorkflowClass(String workflowClass) {
			this.set("workflow_class__sys", workflowClass);
		}
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("object")
		public VaultObject.ObjectReference getObject() {
			return (VaultObject.ObjectReference) this.get("object");
		}

		public void setObject(VaultObject.ObjectReference object) {
			this.set("object", object);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		public void setTotal(Integer total) {
			this.set("total", total);
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