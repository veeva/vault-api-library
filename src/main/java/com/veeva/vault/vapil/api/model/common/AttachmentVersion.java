/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the Document / Object attachment versions endpoints
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentVersion extends VaultModel {
    @JsonProperty("version__v")
    @JsonAlias({"version"})
    public Integer getVersion() {
        if (this.getFieldNames().contains("version"))
            return getInteger("version");
        else
            return getInteger("version__v");
    }

    public void setVersion(Integer version) {
        this.set("version__v", version);
    }

    @JsonProperty("url")
    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        this.set("url", url);
    }
}

