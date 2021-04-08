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
 * Model for the all document types response
 */
public class DocumentTypesResponse extends VaultResponse {

	@JsonProperty("lock")
	public String getLock() {
		return this.getString("lock");
	}

	public void setLock(String lock) {
		this.set("lock", lock);
	}

	@JsonProperty("types")
	public List<DocumentType> getTypes() {
		return (List<DocumentType>) this.get("types");
	}

	public void setTypes(List<DocumentType> types) {
		this.set("types", types);
	}

	public static class DocumentType extends VaultModel {

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
