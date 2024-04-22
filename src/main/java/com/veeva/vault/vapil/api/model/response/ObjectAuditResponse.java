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
		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("timestamp")
		public String getTimestamp() {
			return this.getString("timestamp");
		}

		public void setTimestamp(String timestamp) {
			this.set("timestamp", timestamp);
		}

		@JsonProperty("user_name")
		public String getUserName() {
			return this.getString("user_name");
		}

		public void setUserName(String userName) {
			this.set("user_name", userName);
		}

		@JsonProperty("full_name")
		public String getFullName() {
			return this.getString("full_name");
		}

		public void setFullName(String fullName) {
			this.set("full_name", fullName);
		}

		@JsonProperty("action")
		public String getAction() {
			return this.getString("action");
		}

		public void setAction(String action) {
			this.set("action", action);
		}

		@JsonProperty("item")
		public String getItem() {
			return this.getString("item");
		}

		public void setItem(String item) {
			this.set("item", item);
		}

		@JsonProperty("field_name")
		public String getFieldName() {
			return this.getString("field_name");
		}

		public void setFieldName(String fieldName) {
			this.set("field_name", fieldName);
		}

		@JsonProperty("field_label")
		public String getFieldLabel() {
			return this.getString("field_label");
		}

		public void setFieldLabel(String fieldLabel) {
			this.set("field_label", fieldLabel);
		}

		@JsonProperty("old_value")
		public String getOldValue() {
			return this.getString("old_value");
		}

		public void setOldValue(String oldValue) {
			this.set("old_value", oldValue);
		}

		@JsonProperty("old_display_value")
		public String getOldDisplayValue() {
			return this.getString("old_display_value");
		}

		public void setOldDisplayValue(String oldDisplayValue) {
			this.set("old_display_value", oldDisplayValue);
		}

		@JsonProperty("new_value")
		public String getNewValue() {
			return this.getString("new_value");
		}

		public void setNewValue(String newValue) {
			this.set("new_value", newValue);
		}

		@JsonProperty("new_display_value")
		public String getNewDisplayValue() {
			return this.getString("new_display_value");
		}

		public void setNewDisplayValue(String newDisplayValue) {
			this.set("new_display_value", newDisplayValue);
		}

		@JsonProperty("record_id")
		public String getRecordId() {
			return this.getString("record_id");
		}

		public void setRecordId(String recordId) {
			this.set("record_id", recordId);
		}

		@JsonProperty("object_name")
		public String getObjectName() {
			return this.getString("object_name");
		}

		public void setObjectName(String objectName) {
			this.set("object_name", objectName);
		}

		@JsonProperty("object_label")
		public String getObjectLabel() {
			return this.getString("object_label");
		}

		public void setObjectLabel(String objectLabel) {
			this.set("object_label", objectLabel);
		}

		@JsonProperty("workflow_name")
		public String getWorkflowName() {
			return this.getString("workflow_name");
		}

		public void setWorkflowName(String workflowName) {
			this.set("workflow_name", workflowName);
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

		@JsonProperty("reason")
		public String getReason() {
			return this.getString("reason");
		}

		public void setReason(String reason) {
			this.set("reason", reason);
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

		@JsonProperty("on_behalf_of")
		public String getOnBehalfOf() {
			return this.getString("on_behalf_of");
		}

		public void setOnBehalfOf(String onBehalfOf) {
			this.set("on_behalf_of", onBehalfOf);
		}
	}
}