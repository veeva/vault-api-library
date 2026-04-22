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

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/services/ai/agent_actions/actions/execute
 */
public class AgentActionExecutionResponse extends VaultResponse {

    @JsonProperty("actionResponse")
    public ActionResponse getActionResponse() {
        return (ActionResponse) this.get("actionResponse");
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.set("actionResponse", actionResponse);
    }

    public static class ActionResponse extends VaultModel {

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

        @JsonProperty("urls")
        public List<String> getUrls() {
            return (List<String>) this.get("urls");
        }

        public void setUrls(List<String> urls) {
            this.set("urls", urls);
        }
    }
}
