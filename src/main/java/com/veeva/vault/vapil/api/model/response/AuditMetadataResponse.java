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
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/metadata/audittrail/{audit_trail_type}
 */
public class AuditMetadataResponse extends VaultResponse {

	@JsonProperty("data")
	public AuditMetadata getData() {
		return (AuditMetadata) this.get("data");
	}

	@JsonProperty("data")
	public void setData(AuditMetadata data) {
		this.set("data", data);
	}

	public static class AuditMetadata extends VaultModel {

		@JsonProperty("fields")
		public List<Field> getFields() {
			return (List<Field>) this.get("fields");
		}

		public void setFields(List<Field> fields) {
			this.set("fields", fields);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		public static class Field extends VaultModel {

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			public void setLabel(String label) {
				this.set("label", label);
			}

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