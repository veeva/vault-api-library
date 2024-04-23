/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.Binder;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the binder response
 */
public class BinderResponse extends VaultResponse {

	@JsonProperty("binder")
	public Binder getBinder() {
		return (Binder) this.get("binder");
	}

	public void setBinder(Binder binder) {
		this.set("binder", binder);
	}

	@JsonProperty("document")
	public Document getDocument() {
		return (Document) this.get("document");
	}

	public void setDocument(Document document) {
		this.set("document", document);
	}

	@JsonProperty("renditions")
	public Renditions getRenditions() {
		return (Renditions) this.get("renditions");
	}

	public void setRenditions(Renditions renditions) {
		this.set("renditions", renditions);
	}

	@JsonProperty("versions")
	public List<Version> getVersions() {
		return (List<Version>) this.get("versions");
	}

	public void setVersions(List<Version> versions) {
		this.set("versions", versions);
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

	static public class Version extends VaultModel {

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


	//----------------------------------------------------------------------
	//special case, when creating, deleting, and updating docs,
	//they do not return a document node, the id, major/minor and external_id__v are at the root

	@JsonProperty("external_id__v")
	public void setExternalId(String externalId) {
		if (getDocument() == null) {
			setDocument(new Document());
		}
		getDocument().setExternalId(externalId);
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		if (getDocument() == null) {
			setDocument(new Document());
		}
		getDocument().setId(id);
	}

	@JsonProperty("major_version_number__v")
	public void setMajorVersionNumber(Integer majorVersionNumber) {
		if (getDocument() == null) {
			setDocument(new Document());
		}
		getDocument().setMajorVersionNumber(majorVersionNumber);
	}

	@JsonProperty("minor_version_number__v")
	public void setMinorVersionNumber(Integer minorVersionNumber) {
		if (getDocument() == null) {
			setDocument(new Document());
		}
		getDocument().setMinorVersionNumber(minorVersionNumber);
	}
}