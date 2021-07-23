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

public class FileStagingItemResponse extends VaultResponse {

	@JsonProperty("data")
	public FileStagingItem getData() {
		return (FileStagingItem) this.get("data");
	}

	public void setData(FileStagingItem data) {
		this.set("data", data);
	}

	public static class FileStagingItem extends VaultModel {

		@JsonProperty("kind")
		public String getKind() {
			return getString("kind");
		}

		public void setKind(String kind) {
			this.set("kind", kind);
		}

		@JsonProperty("path")
		public String getPath() {
			return getString("path");
		}

		public void setPath(String path) {
			this.set("path", path);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("size")
		public Long getSize() {
			return getLong("size");
		}

		public void setSize(Long size) {
			this.set("size", size);
		}

		@JsonProperty("file_content_md5")
		public String getFileContentMd5() {
			return getString("file_content_md5");
		}

		public void setFileContentMd5(String fileContentMd5) {
			this.set("file_content_md5", fileContentMd5);
		}
	}
}