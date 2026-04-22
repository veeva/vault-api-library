/*---------------------------------------------------------------------
 *	Copyright (c) 2024 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/services/ai/agent_actions/actions/cancel
 */
public class AgentActionCancelResponse extends VaultResponse {

    @JsonProperty("data")
    public Data getData() {
        return (Data) this.get("data");
    }

    public void setData(Data data) {
        this.set("data", data);
    }

    public static class Data extends VaultModel {

        @JsonProperty("actionStatus")
        public String getActionStatus() {
            return this.getString("actionStatus");
        }

        public void setActionStatus(String actionStatus) {
            this.set("actionStatus", actionStatus);
        }

        @JsonProperty("executionId")
        public String getExecutionId() {
            return this.getString("executionId");
        }

        public void setExecutionId(String executionId) {
            this.set("executionId", executionId);
        }
    }
}
