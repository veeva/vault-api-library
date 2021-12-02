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
 * For use when {audit_trail_type} is document_audit_trail
 */
public class DocumentAuditResponse extends AuditDetailsResponse {

	@JsonProperty("data")
	public List<DocumentAudit> getData() {
		return (List<DocumentAudit>) this.get("data");
	}

	public void setData(List<DocumentAudit> data) {
		this.set("data", data);
	}

	public static class DocumentAudit extends Audit {

		@JsonProperty("action")
		public String getAction() {
			return this.getString("action");
		}

		public void setAction(String action) {
			this.set("action", action);
		}

		@JsonProperty("doc_id")
		public String getDocId() {
			return this.getString("doc_id");
		}

		public void setDocId(String docId) {
			this.set("doc_id", docId);
		}

		@JsonProperty("document_url")
		public String getDocumentUrl() {
			return this.getString("document_url");
		}

		public void setDocumentUrl(String documentUrl) {
			this.set("document_url", documentUrl);
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

		@JsonProperty("job_instance_id")
		public String getJobInstanceId() {
			return this.getString("job_instance_id");
		}

		public void setJobInstanceId(String jobInstanceId) {
			this.set("job_instance_id", jobInstanceId);
		}

		@JsonProperty("new_value")
		public String getNewValue() {
			return this.getString("new_value");
		}

		public void setNewValue(String newValue) {
			this.set("new_value", newValue);
		}

		@JsonProperty("old_value")
		public String getOldValue() {
			return this.getString("old_value");
		}

		public void setOldValue(String oldValue) {
			this.set("old_value", oldValue);
		}

		@JsonProperty("signature_meaning")
		public String getSignatureMeaning() {
			return this.getString("signature_meaning");
		}

		public void setSignatureMeaning(String signatureMeaning) {
			this.set("signature_meaning", signatureMeaning);
		}

		@JsonProperty("task_name")
		public String getTaskName() {
			return this.getString("task_name");
		}

		public void setTaskName(String taskName) {
			this.set("task_name", taskName);
		}

		@JsonProperty("version")
		public String getVersion() {
			return this.getString("version");
		}

		public void setVersion(String version) {
			this.set("version", version);
		}

		@JsonProperty("view_license")
		public String getViewLicense() {
			return this.getString("view_license");
		}

		public void setViewLicense(String viewLicense) {
			this.set("view_license", viewLicense);
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