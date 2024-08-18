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

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/services/jobs/{job_id}/summary
 */
public class BulkTranslationImportSummaryResponse extends VaultResponse {

    @JsonProperty("data")
    public Data getData() {
        return (Data) this.get("data");
    }

    public void setData(Data data) {
        this.set("data", data);
    }

    public class Data extends VaultModel {
        @JsonProperty("ignored")
        public Integer getIgnored() {
            return this.getInteger("ignored");
        }

        public void setIgnored(Integer ignored) {
            this.set("ignored", ignored);
        }

        @JsonProperty("updated")
        public Integer getUpdated() {
            return this.getInteger("updated");
        }

        public void setUpdated(Integer updated) {
            this.set("updated", updated);
        }

        @JsonProperty("failed")
        public Integer getFailed() {
            return this.getInteger("failed");
        }

        public void setFailed(Integer failed) {
            this.set("failed", failed);
        }

        @JsonProperty("added")
        public Integer getAdded() {
            return this.getInteger("added");
        }

        public void setAdded(Integer added) {
            this.set("added", added);
        }
    }
}