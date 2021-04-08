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
 * For use when {audit_trail_type} is domain_audit_trail
 */
public class DomainAuditResponse extends AuditDetailsResponse {

	@JsonProperty("data")
	public List<DomainAuditData> getData() {
		return (List<DomainAuditData>) this.get("data");
	}

	public void setData(List<DomainAuditData> data) {
		this.set("data", data);
	}

	public static class DomainAuditData extends VaultModel {

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

		@JsonProperty("full_name")
		public String getFullName() {
			return this.getString("full_name");
		}

		public void setFullName(String fullName) {
			this.set("full_name", fullName);
		}

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
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

		@JsonProperty("timestamp")
		public String getTimestamp() {
			return this.getString("timestamp");
		}

		public void setTimestamp(String timestamp) {
			this.set("timestamp", timestamp);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("user_id")
		public String getUserId() {
			return this.getString("user_id");
		}

		public void setUserId(String userId) {
			this.set("user_id", userId);
		}

		@JsonProperty("user_name")
		public String getUserName() {
			return this.getString("user_name");
		}

		public void setUserName(String userName) {
			this.set("user_name", userName);
		}
	}
}