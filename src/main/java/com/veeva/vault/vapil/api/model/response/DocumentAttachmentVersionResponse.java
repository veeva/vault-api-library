/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.AttachmentVersion;
import com.veeva.vault.vapil.api.model.common.DocumentAttachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions
 */
public class DocumentAttachmentVersionResponse extends VaultResponse {

    @JsonProperty("data")
    public List<AttachmentVersion> getData() {
        return (List<AttachmentVersion>) this.get("data");
    }

    public void setData(List<AttachmentVersion> data) {
        this.set("data", data);
    }

}