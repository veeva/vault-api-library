/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

public class DocumentEvent extends VaultModel {

	public DocumentEvent() {
		setEventProperties(new DocumentEvent.Property());
	}

	@JsonProperty("name")
	public String getName() {
		return this.getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("label")
	public String getLabel() {
		return this.getString("label");
	}

	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("properties")
	public Property getEventProperties() {
		return (Property) this.get("properties");
	}

	public void setEventProperties(Property properties) {
		this.set("properties", properties);
	}

	public static class Property extends VaultModel {
		@JsonProperty("classification__v")
		public String getClassification() {
			return this.getString("classification__v");
		}

		public void setClassification(String classification) {
			this.set("classification__v", classification);
		}

		@JsonProperty("document_id__v")
		public Integer getDocumentId() {
			return this.getInteger("document_id__v");
		}

		public void setDocumentId(Integer documentId) {
			this.set("document_id__v", documentId);
		}

		@JsonProperty("document_number__v")
		public String getDocumentNumber() {
			return this.getString("document_number__v");
		}

		public void setDocumentNumber(String documentNumber) {
			this.set("document_number__v", documentNumber);
		}

		@JsonProperty("document_name__v")
		public String getDocumentName() {
			return this.getString("document_name__v");
		}

		public void setDocumentName(String documentName) {
			this.set("document_name__v", documentName);
		}

		@JsonProperty("event_date__v")
		public String getEventDate() {
			return this.getString("event_date__v");
		}

		public void setEventDate(String eventDate) {
			this.set("event_date__v", eventDate);
		}

		@JsonProperty("event_type__v")
		public String getEventType() {
			return this.getString("event_type__v");
		}

		public void setEventType(String eventType) {
			this.set("event_type__v", eventType);
		}

		@JsonProperty("event_subtype__v")
		public String getEventSubtype() {
			return this.getString("event_subtype__v");
		}

		public void setEventSubtype(String eventSubtype) {
			this.set("event_subtype__v", eventSubtype);
		}

		@JsonProperty("event_modified_by__v")
		public Integer getEventModifiedBy() {
			return this.getInteger("event_modified_by__v");
		}

		public void setEventModifiedBy(Integer eventModifiedBy) {
			this.set("event_modified_by__v", eventModifiedBy);
		}

		@JsonProperty("event_modified_date__v")
		public String getEventModifiedDate() {
			return this.getString("event_modified_date__v");
		}

		public void setEventModifiedDate(String eventModifiedDate) {
			this.set("event_modified_date__v", eventModifiedDate);
		}

		@JsonProperty("external_id__v")
		public String getExternalId() {
			return this.getString("external_id__v");
		}

		public void setExternalId(String externalId) {
			this.set("external_id__v", externalId);
		}

		@JsonProperty("major_version_number__v")
		public Integer getMajorVersion() {
			return this.getInteger("major_version_number__v");
		}

		public void setMajorVersion(Integer majorVersion) {
			this.set("major_version_number__v", majorVersion);
		}

		@JsonProperty("minor_version_number__v")
		public Integer getMinorVersion() {
			return this.getInteger("minor_version_number__v");
		}

		public void setMinorVersion(Integer minorVersion) {
			this.set("minor_version_number__v", minorVersion);
		}

		@JsonProperty("triggered_by__v")
		public Integer getTriggeredBy() {
			return this.getInteger("triggered_by__v");
		}

		public void setTriggeredBy(Integer triggeredBy) {
			this.set("triggered_by__v", triggeredBy);
		}

		@JsonProperty("user_email__v")
		public String getUserEmail() {
			return this.getString("user_email__v");
		}

		public void setUserEmail(String userEmail) {
			this.set("user_email__v", userEmail);
		}
	}
}
