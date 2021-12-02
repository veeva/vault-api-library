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
 * For use when {audit_trail_type} is system_audit_trail
 */
public class SystemAuditResponse extends AuditDetailsResponse {

	@JsonProperty("data")
	public List<SystemAuditData> getData() {
		return (List<SystemAuditData>) this.get("data");
	}

	public void setData(List<SystemAuditData> data) {
		this.set("data", data);
	}

	public static class SystemAuditData extends Audit {

		@JsonProperty("action")
		public String getAction() {
			return this.getString("action");
		}

		public void setAction(String action) {
			this.set("action", action);
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

		@JsonProperty("user_id")
		public String getUserId() {
			return this.getString("user_id");
		}

		public void setUserId(String userId) {
			this.set("user_id", userId);
		}
	}
}