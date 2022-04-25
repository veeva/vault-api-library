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

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/objects/documents/{doc_id}/lock
 */
public class DocumentVersionResponse extends VaultResponse {

	@JsonProperty("renditions")
	public Renditions getRenditions() {
		return (Renditions) this.get("renditions");
	}

	public void setRenditions(DocumentResponse.Renditions renditions) {
		this.set("renditions", renditions);
	}

	@JsonProperty("versions")
	public List<Version> getVersions() {
		return (List<Version>) this.get("versions");
	}

	public void setVersions(List<Version> versions) {
		this.set("versions", versions);
	}

	public static class Version extends VaultModel {

		@JsonProperty("number")
		public String getNumber() {
			return this.getString("number");
		}

		public void setNumber(String number) {
			this.set("number", number);
		}

		@JsonProperty("value")
		public String getValue() {
			return this.getString("value");
		}

		public void setValue(String value) {
			this.set("value", value);
		}
	}

	static public class Renditions extends VaultModel {

		@JsonProperty("viewable_rendition__v")
		public String getViewableRendition() {
			return this.getString("viewable_rendition__v");
		}

		public void setViewableRendition(String viewableRendition) {
			this.set("viewable_rendition__v", viewableRendition);
		}
	}
}