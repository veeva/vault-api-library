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

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/audittrail/{audit_trail_type}
 * <br>
 * For use when {audit_trail_type} is object_audit_trail
 */
public class ObjectAuditResponse extends AuditDetailsResponse {

	@JsonProperty("data")
	public List<ObjectAuditData> getData() {
		return (List<ObjectAuditData>) this.get("data");
	}

	public void setData(List<ObjectAuditData> data) {
		this.set("data", data);
	}

	public static class ObjectAuditData extends Audit {

		@JsonProperty("action")
		public String getAction() {
			return this.getString("action");
		}

		public void setAction(String action) {
			this.set("action", action);
		}

		@JsonProperty("capacity")
		public String getCapacity() {
			return this.getString("capacity");
		}

		public void setCapacity(String capacity) {
			this.set("capacity", capacity);
		}

		@JsonProperty("event_description")
		public String getEventDescription() {
			return this.getString("event_description");
		}

		public void setEventDescription(String eventDescription) {
			this.set("event_description", eventDescription);
		}

		@JsonProperty("field_name")
		public String getFieldName() {
			return this.getString("field_name");
		}

		public void setFieldName(String fieldName) {
			this.set("field_name", fieldName);
		}

		@JsonProperty("item")
		public String getItem() {
			return this.getString("item");
		}

		public void setItem(String item) {
			this.set("item", item);
		}

		@JsonProperty("new_value")
		public String getNewValue() {
			return this.getString("new_value");
		}

		public void setNewValue(String newValue) {
			this.set("new_value", newValue);
		}

		@JsonProperty("object_label")
		public String getObjectLabel() {
			return this.getString("object_label");
		}

		public void setObjectLabel(String objectLabel) {
			this.set("object_label", objectLabel);
		}

		@JsonProperty("old_value")
		public String getOldValue() {
			return this.getString("old_value");
		}

		public void setOldValue(String oldValue) {
			this.set("old_value", oldValue);
		}

		@JsonProperty("reason")
		public String getReason() {
			return this.getString("reason");
		}

		public void setReason(String reason) {
			this.set("reason", reason);
		}

		@JsonProperty("record_id")
		public String getRecordId() {
			return this.getString("record_id");
		}

		public void setRecordId(String recordId) {
			this.set("record_id", recordId);
		}

		@JsonProperty("task_name")
		public String getTaskName() {
			return this.getString("task_name");
		}

		public void setTaskName(String taskName) {
			this.set("task_name", taskName);
		}

		@JsonProperty("verdict")
		public String getVerdict() {
			return this.getString("verdict");
		}

		public void setVerdict(String verdict) {
			this.set("verdict", verdict);
		}

		@JsonProperty("workflow_name")
		public String getWorkflowName() {
			return this.getString("workflow_name");
		}

		public void setWorkflowName(String workflowName) {
			this.set("workflow_name", workflowName);
		}
	}
}