/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the Object Record data
 */
public class ObjectRecord extends VaultModel {
	@JsonProperty("id")
	public String getId() {
		return getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

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

	@JsonProperty("global_id__sys")
	public String getGlobalId() {
		return getString("global_id__sys");
	}

	public void setGlobalId(String globalId) {
		this.set("global_id__sys", globalId);
	}

	@JsonProperty("lifecycle__v")
	public List<String> getLifecycle() {
		return (List<String>) get("lifecycle__v");
	}

	public void setLifecycle(List<String> lifecycle) {
		this.set("lifecycle__v", lifecycle);
	}

	@JsonProperty("link__sys")
	public String getLinkSys() {
		return getString("link__sys");
	}

	public void setLinkSys(String linkSys) {
		this.set("link__sys", linkSys);
	}

	@JsonProperty("modified_by__v")
	public Integer getModifiedBy() {
		return getInteger("modified_by__v");
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.set("modified_by__v", modifiedBy);
	}

	@JsonProperty("modified_date__v")
	public String getModifiedDate() {
		return getString("modified_date__v");
	}

	public void setModifiedDate(String modifiedDate) {
		this.set("modified_date__v", modifiedDate);
	}

	@JsonProperty("name__v")
	public String getName() {
		return getString("name__v");
	}

	public void setName(String name) {
		this.set("name__v", name);
	}

	@JsonProperty("object_type__v")
	public String getObjectType() {
		return getString("object_type__v");
	}

	public void setObjectType(String objectType) {
		this.set("object_type__v", objectType);
	}

	@JsonProperty("state__v")
	public List<String> getState() {
		return (List<String>) get("state__v");
	}

	public void setState(List<String> state) {
		this.set("state__v", state);
	}

	@JsonProperty("status__v")
	public List<String> getStatus() {
		return (List<String>) get("status__v");
	}

	public void setStatus(List<String> status) {
		this.set("status__v", status);
	}

	@JsonProperty("url")
	public String getUrl() {
		return getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}
}
