/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model for the document annotation bulk responses
 */
public class DocumentAnnotationBulkResponse extends VaultResponse {

    @JsonProperty("data")
    public List<DocumentAnnotationResponse> getData() {
        return (List<DocumentAnnotationResponse>) this.get("data");
    }

    @JsonProperty("data")
    public void setData(List<DocumentAnnotationResponse> data) {
        this.set("data", data);
    }

}