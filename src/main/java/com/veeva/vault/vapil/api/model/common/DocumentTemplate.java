/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the Document Template request and responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentTemplate extends VaultModel {

	@JsonProperty("active__v")
	public Boolean getActive() {
		return getBoolean("active__v");
	}

	public void setActive(Boolean active) {
		set("active__v", active);
	}

	@JsonProperty("classification__v")
	public String getClassification() {
		return getString("classification__v");
	}

	public void setClassification(String classification) {
		set("classification__v", classification);
	}

	@JsonProperty("created_by__v")
	public Long getCreatedBy() {
		return getLong("created_by__v");
	}

	public void setCreatedBy(Long createdBy) {
		this.set("created_by__v", createdBy);
	}

	@JsonProperty("file_uploaded_by__v")
	public Long getFileUploadedBy() {
		return getLong("file_uploaded_by__v");
	}

	public void setFileUploadedBy(Long fileUploadedBy) {
		this.set("file_uploaded_by__v", fileUploadedBy);
	}

	@JsonProperty("format__v")
	public String getFormat() {
		return getString("format__v");
	}

	public void setFormat(String format) {
		this.set("format__v", format);
	}

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("is_controlled__v")
	public Boolean getIsControlled() {
		return getBoolean("is_controlled__v");
	}

	public void setIsControlled(Boolean isControlled) {
		set("is_controlled__v", isControlled);
	}

	@JsonProperty("name__v")
	@JsonAlias("name")
	public String getName() {
		return getString("name__v");
	}

	public void setName(String name) {
		set("name__v", name);
	}

	@JsonProperty("new_name")
	public String getNewName() {
		return getString("new_name");
	}

	public void setNewName(String newName) {
		set("new_name", newName);
	}

	@JsonProperty("label__v")
	public String getLabel() {
		return getString("label__v");
	}

	public void setLabel(String label) {
		set("label__v", label);
	}

	@JsonProperty("md5checksum__v")
	public String getMd5checksum() {
		return getString("md5checksum__v");
	}

	public void setMd5checksum(String md5checksum) {
		this.set("md5checksum__v", md5checksum);
	}

	@JsonProperty("size__v")
	public Long getSize() {
		return getLong("size__v");
	}

	public void setSize(Long size) {
		this.set("size__v", size);
	}

	@JsonProperty("subtype__v")
	public String getSubType() {
		return getString("subtype__v");
	}

	public void setSubType(String subtype) {
		set("subtype__v", subtype);
	}

	@JsonProperty("template_doc_id__v")
	public Integer getTemplateDocId() {
		return getInteger("template_doc_id__v");
	}

	public void setTemplateDocId(Integer templateDocId) {
		this.set("template_doc_id__v", templateDocId);
	}

	@JsonProperty("template_doc_selected_by__v")
	public Integer getTemplateDocSelectedBy() {
		return getInteger("template_doc_selected_by__v");
	}

	public void setTemplateDocSelectedBy(Integer templateDocSelectedBy) {
		this.set("template_doc_selected_by__v", templateDocSelectedBy);
	}

	@JsonProperty("type__v")
	public String getType() {
		return getString("type__v");
	}

	public void setType(String type) {
		set("type__v", type);
	}

}
