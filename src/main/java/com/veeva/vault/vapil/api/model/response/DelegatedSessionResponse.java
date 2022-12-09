/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for responses to Initiate Delegated Session API:
 * <p>
 * GET https://{vaultDNS}/api/{version}/delegation/login
 */
public class DelegatedSessionResponse extends VaultResponse {

    @JsonProperty("delegated_sessionid")
    public String getDelegatedSessionId() {
        return this.getString("delegated_sessionid");
    }

    public void setDelegatedSessionId(String delegatedSessionId) {
        this.set("delegated_sessionid", delegatedSessionId);
    }

}
