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

import java.util.List;

public class LoaderTask extends VaultModel {
	@JsonProperty("action")
	public String getAction() {
		return getString("action");
	}

	public void setAction(String action) {
		this.set("action", action);
	}

	@JsonProperty("documentmigrationmode")
	public Boolean getDocumentMigrationMode() {
		return getBoolean("documentmigrationmode");
	}

	public void setDocumentMigrationMode(Boolean documentMigrationMode) {
		this.set("documentmigrationmode", documentMigrationMode);
	}

	@JsonProperty("extract_options")
	public String getExtractOptions() {
		return getString("extract_options");
	}

	public void setExtractOptions(String extractOptions) {
		this.set("extract_options", extractOptions);
	}

	@JsonProperty("file")
	public String getFile() {
		return getString("file");
	}

	public void setFile(String file) {
		this.set("file", file);
	}

	@JsonProperty("fields")
	public List<String> getFields() {
		return (List<String>) get("fields");
	}

	public void setFields(List<String> fields) {
		this.set("fields", fields);
	}

	@JsonProperty("idparam")
	public String getIdParam() {
		return getString("idparam");
	}

	public void setIdParam(String idparam) {
		this.set("idparam", idparam);
	}

	@JsonProperty("object")
	public String getObject() {
		return getString("object");
	}

	public void setObject(String object) {
		this.set("object", object);
	}

	@JsonProperty("object_type")
	public String getObjectType() {
		return getString("object_type");
	}

	public void setObjectType(String objectType) {
		this.set("object_type", objectType);
	}

	@JsonProperty("order")
	public Integer getOrder() {
		return getInteger("order");
	}

	public void setOrder(Integer order) {
		this.set("order", order);
	}

	@JsonProperty("recordmigrationmode")
	public Boolean getRecordMigrationMode() {
		return getBoolean("recordmigrationmode");
	}

	public void setRecordMigrationMode(Boolean recordMigrationMode) {
		this.set("recordmigrationmode", recordMigrationMode);
	}

	@JsonProperty("task_id")
	public Integer getTaskId() {
		return getInteger("task_id");
	}

	public void setTaskId(Integer taskId) {
		this.set("task_id", taskId);
	}

	@JsonProperty("vql_criteria__v")
	public String getVqlCriteria() {
		return getString("vql_criteria__v");
	}

	public void setVqlCriteria(String vqlCriteria) {
		this.set("vql_criteria__v", vqlCriteria);
	}
}
