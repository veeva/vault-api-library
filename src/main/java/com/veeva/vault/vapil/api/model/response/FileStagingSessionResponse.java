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
import com.veeva.vault.vapil.api.model.response.VaultResponse;

public class FileStagingSessionResponse extends VaultResponse {

	@JsonProperty("data")
	public FileStagingSession getData() {
		return (FileStagingSession) this.get("data");
	}

	public void setData(FileStagingSession data) {
		this.set("data", data);
	}

	public static class FileStagingSession extends VaultModel {

		@JsonProperty("created_date")
		public String getCreatedDate() {
			return getString("created_date");
		}

		public void setCreatedDate(String createdDate) {
			this.set("created_date", createdDate);
		}

		@JsonProperty("expiration_date")
		public String getExpirationDate() {
			return getString("expiration_date");
		}

		public void setExpirationDate(String expirationDate) {
			this.set("expiration_date", expirationDate);
		}

		@JsonProperty("id")
		public String getId() {
			return getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("kind")
		public String getKind() {
			return getString("kind");
		}

		public void setKind(String kind) {
			this.set("kind", kind);
		}

		@JsonProperty("last_uploaded_date")
		public String getLastUploadedDate() {
			return getString("last_uploaded_date");
		}

		public void setLastUploadedDate(String lastUploadedDate) {
			this.set("last_uploaded_date", lastUploadedDate);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("overwrite")
		public Boolean getOverwrite() {
			return getBoolean("overwrite");
		}

		public void setOverwrite(Boolean overwrite) {
			this.set("overwrite", overwrite);
		}

		@JsonProperty("owner")
		public void setOwner(Long owner) {
			this.set("owner", owner);
		}

		public Long getOwner() {
			return getLong("owner");
		}

		@JsonProperty("path")
		public String getPath() {
			return getString("path");
		}

		public void setPath(String path) {
			this.set("path", path);
		}

		@JsonProperty("size")
		public Long getSize() {
			return getLong("size");
		}

		public void setSize(Long size) {
			this.set("size", size);
		}

		@JsonProperty("uploaded_parts")
		public void setUploadedParts(Integer uploadedParts) {
			this.set("uploaded_parts", uploadedParts);
		}

		public Integer getUploadedParts() {
			return getInteger("uploaded_parts");
		}

		@JsonProperty("uploaded")
		public void setUploaded(Long uploaded) {
			this.set("uploaded", uploaded);
		}

		public Long getUploaded() {
			return getLong("uploaded");
		}
	}
}