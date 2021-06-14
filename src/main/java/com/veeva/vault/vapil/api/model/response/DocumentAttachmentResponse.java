/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.common.DocumentAttachment;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the document response
 */
public class DocumentAttachmentResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DocumentAttachment> getData() {
		return (List<DocumentAttachment>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<DocumentAttachment> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public DocumentAttachment getDocumentAttachment() {
		List<DocumentAttachment> documentAttachments = getData();
		if (documentAttachments != null && documentAttachments.size() == 1) {
			return documentAttachments.get(0);
		}
		return null;
	}

	@JsonIgnore
	public void setDocumentAttachment(DocumentAttachment attachment) {
		List<DocumentAttachment> documentAttachments = getData();
		if (documentAttachments == null) {
			documentAttachments = new ArrayList<>();
		}
		if (documentAttachments.size() == 0) {
			documentAttachments.add(attachment);
		}
	}

	//----------------------------------------------------------------------
	//special case, when creating, deleting, and updating docs attachments,
	//they help says it foes not return a document node, the id, major/minor
	// and external_id__v are at the root

	@JsonProperty("id")
	public void setId(Integer id) {
		DocumentAttachment documentAttachment = getDocumentAttachment();
		if (documentAttachment == null) {
			documentAttachment = new DocumentAttachment();
			setDocumentAttachment(documentAttachment);
		}
		documentAttachment.setId(id);
	}

	@JsonProperty("version__v")
	@JsonAlias({"version"})
	public void setVersion(Integer version) {
		DocumentAttachment documentAttachment = getDocumentAttachment();
		if (documentAttachment == null) {
			documentAttachment = new DocumentAttachment();
			setDocumentAttachment(documentAttachment);
		}
		documentAttachment.setVersion(version);
	}
}