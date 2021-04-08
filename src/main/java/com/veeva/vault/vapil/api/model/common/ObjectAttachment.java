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

import java.util.List;

/**
 * Model for the Attachment attachment metadata
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectAttachment extends VaultModel {
	//---------------------------------------------------------------------------------
	//id fields

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("version__v")
	@JsonAlias({"version"})
	public Integer getVersion() {
		Integer result = getInteger("version__v");
		if (result == null) {
			result = getInteger("version");
		}
		return result;
	}

	public void setVersion(Integer version) {
		this.set("version__v", version);
	}

	//---------------------------------------------------------------------------------
	//All other fields in Alpha Order

	@JsonProperty("created_by__v")
	public Integer getCreatedBy() {
		return getInteger("created_by__v");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by__v", createdBy);
	}

	@JsonProperty("external_id__v")
	public String getExternalId() {
		return getString("external_id__v");
	}

	public void setExternalId(String externalId) {
		this.set("external_id__v", externalId);
	}

	@JsonProperty("filename__v")
	public String getFilename() {
		return getString("filename__v");
	}

	public void setFilename(String filename) {
		this.set("filename__v", filename);
	}

	@JsonProperty("format__v")
	public String getFormat() {
		return getString("format__v");
	}

	public void setFormat(String format) {
		this.set("format__v", format);
	}

	@JsonProperty("md5checksum__v")
	public String getMd5checksum() {
		return getString("md5checksum__v");
	}

	public void setMd5checksum(String md5checksum) {
		this.set("md5checksum__v", md5checksum);
	}

	@JsonProperty("size__v")
	public Integer getSize() {
		return getInteger("size__v");
	}

	public void setSize(Integer size) {
		this.set("size__v", size);
	}

	@JsonProperty("versions")
	public List<Version> getVersions() {
		return (List<Version>) this.get("versions");
	}

	public void setVersions(List<Version> versions) {
		this.set("versions", versions);
	}

	public static class Version extends VaultModel {
		@JsonProperty("version__v")
		@JsonAlias({"version"})
		public Integer getVersion() {
			Integer result = getInteger("version__v");
			if (result == null) {
				result = getInteger("version");
			}
			return result;
		}

		public void setVersion(Integer version) {
			this.set("version__v", version);
		}

		@JsonProperty("url")
		public String getUrl() {
			return getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}
	}
}
