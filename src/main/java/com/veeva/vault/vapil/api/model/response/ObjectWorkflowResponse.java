/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.VaultObject;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses for the Object workflow
 * operations:
 * <p>
 * GET /api/{version}/objects/objectworkflows
 * GET /api/{version}/objects/objectworkflows/{workflow_id} <br>
 */
public class ObjectWorkflowResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectWorkflow> getData() {
		return (List<ObjectWorkflow>) this.get("data");
	}

	public void setData(List<ObjectWorkflow> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ObjectWorkflow extends VaultModel {

		@JsonProperty("completed_date__v")
		public String getCompletedDate() {
			return this.getString("completed_date__v");
		}

		public void setCompletedDate(String completedDate) {
			this.set("completed_date__v", completedDate);
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

		@JsonProperty("initiator__v")
		public Integer getInitiator() {
			return this.getInteger("initiator__v");
		}

		public void setInitiator(Integer initiator) {
			this.set("initiator__v", initiator);
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

		@JsonProperty("started_date__v")
		public String getStartedDate() {
			return this.getString("started_date__v");
		}

		public void setStartedDate(String startedDate) {
			this.set("started_date__v", startedDate);
		}

		@JsonProperty("status__v")
		public List<String> getStatus() {
			return (List<String>) this.get("status__v");
		}

		public void setStatus(List<String> status) {
			this.set("status__v", status);
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