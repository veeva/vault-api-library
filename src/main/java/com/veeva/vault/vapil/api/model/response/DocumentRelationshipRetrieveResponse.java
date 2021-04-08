/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.DocumentRelationship;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the Document/Binder relationship response endpoints:<br>
 * GET
 * /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships
 * <br>
 * GET
 * /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}
 * <br>
 * GET
 * /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships
 * <br>
 * GET
 * /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}
 */
public class DocumentRelationshipRetrieveResponse extends VaultResponse {

	@JsonProperty("errorCodes")
	public Object getErrorCodes() {
		return this.get("errorCodes");
	}

	public void setErrorCodes(String errorCodes) {
		this.set("errorCodes", errorCodes);
	}

	@JsonProperty("errorType")
	public String getErrorType() {
		return this.getString("errorType");
	}

	public void setErrorType(String errorType) {
		this.set("errorType", errorType);
	}

	@JsonProperty("relationships")
	public List<Relationship> getRelationships() {
		return (List<Relationship>) this.get("relationships");
	}

	public void setRelationships(List<Relationship> relationships) {
		this.set("relationships", relationships);
	}

	static public class Relationship extends VaultModel {

		@JsonProperty("relationship")
		public DocumentRelationship getRelationship() {
			return (DocumentRelationship) this.get("relationship");
		}

		public void setRelationship(DocumentRelationship relationship) {
			this.set("relationship", relationship);
		}
	}
}
