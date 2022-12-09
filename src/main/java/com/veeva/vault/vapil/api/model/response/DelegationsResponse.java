/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for responses to Retrieve Delegations API:
 * <p>
 * GET https://{vaultDNS}/api/{version}/delegation/vaults
 */
public class DelegationsResponse extends VaultResponse {

    @JsonProperty("delegated_vaults")
    public List<DelegatedVault> getDelegatedVaults() {
        return (List<DelegatedVault>) this.get("delegated_vaults");
    }

    public void setDelegatedVaults(List<DelegatedVault> delegatedVaults) {
        this.set("delegated_vaults", delegatedVaults);
    }

    public static class DelegatedVault extends VaultModel {

        @JsonProperty("id")
        public Integer getId() {
            return this.getInteger("id");
        }

        public void setId(Integer id) {
            this.set("id", id);
        }

        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("dns")
        public String getDns() {
            return this.getString("dns");
        }

        public void setDns(String dns) {
            this.set("dns", dns);
        }

        @JsonProperty("delegator_userid")
        public String getDelegatorUserId() { return this.getString("delegator_userid");}

        public void setDelegatorUserId(String delegatorUserId) { this.set("delegator_userid", delegatorUserId);}
    }
}
