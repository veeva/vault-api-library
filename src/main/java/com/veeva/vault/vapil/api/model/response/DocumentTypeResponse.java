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

import java.util.List;

/**
 * Model for the document type response
 */
public class DocumentTypeResponse extends DocumentClassificationResponse {

	@JsonProperty("subtypes")
	public List<DocumentSubtype> getSubtypes() {
		return (List<DocumentSubtype>) this.get("subtypes");
	}

	public void setSubtypes(List<DocumentSubtype> subtypes) {
		this.set("subtypes", subtypes);
	}

	public static class DocumentSubtype extends VaultModel {

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return getValue().substring(getValue().lastIndexOf("/") + 1);
		}

		@JsonProperty("value")
		public String getValue() {
			return this.getString("value");
		}

		public void setValue(String value) {
			this.set("value", value);
		}

	}
}