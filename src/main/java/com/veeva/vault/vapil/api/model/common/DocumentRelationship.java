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

/**
 * Model for the Document/Binder relationship, used in:<br>
 * - response of the retrieve/get<br>
 * - inputs to create a Document/Binder relationship
 */
public class DocumentRelationship extends VaultModel {

	@JsonProperty("created_by__v")
	public Integer getCreatedBy() {
		return getInteger("created_by__v");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by__v", createdBy);
	}

	@JsonProperty("created_date__v")
	public String getCreatedDate() {
		return getString("created_date__v");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date__v", createdDate);
	}

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("source_doc_id__v")
	public Integer getSourceDocId() {
		return getInteger("source_doc_id__v");
	}

	public void setSourceDocId(Integer sourceDocId) {
		this.set("source_doc_id__v", sourceDocId);
	}

	@JsonProperty("relationship_type__v")
	public String getRelationshipType() {
		return getString("relationship_type__v");
	}

	public void setRelationshipType(String relationshipType) {
		this.set("relationship_type__v", relationshipType);
	}

	@JsonProperty("target_doc_id__v")
	public Integer getTargetDocId() {
		return getInteger("target_doc_id__v");
	}

	public void setTargetDocId(Integer targetDocId) {
		this.set("target_doc_id__v", targetDocId);
	}

	@JsonProperty("target_major_version__v")
	public Integer getTargetMajorVersion() {
		return getInteger("target_major_version__v");
	}

	public void setTargetMajorVersion(Integer targetMajorVersion) {
		this.set("target_major_version__v", targetMajorVersion);
	}

	@JsonProperty("target_minor_version__v")
	public Integer getTargetMinorVersion() {
		return getInteger("target_minor_version__v");
	}

	public void setTargetMinorVersion(Integer targetMinorVersion) {
		this.set("target_minor_version__v", targetMinorVersion);
	}
}
