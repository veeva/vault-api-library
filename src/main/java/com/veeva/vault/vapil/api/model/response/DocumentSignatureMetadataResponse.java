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
import com.veeva.vault.vapil.api.model.metadata.Template;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/metadata/query/documents/relationships/document_signature__sysr
 * <br>
 * GET /api/{version}/metadata/query/archived_documents/relationships/document_signature__sysr
 */
public class DocumentSignatureMetadataResponse extends VaultResponse {

	@JsonProperty("properties")
	public Properties getProperties() {
		return (Properties) this.get("properties");
	}

	public void setProperties(Properties properties) {
		this.set("properties", properties);
	}

	public static class Properties extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("fields")
		public List<Field> getFields() {
			return (List<Field>) this.get("fields");
		}

		public void setFields(List<Field> fields) {
			this.set("fields", fields);
		}

		public static class Field extends VaultModel {
			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			public void setName(String name) {
				this.set("name", name);
			}

			@JsonProperty("type")
			public String getType() {
				return this.getString("type");
			}

			public void setType(String type) {
				this.set("type", type);
			}
		}
	}
}