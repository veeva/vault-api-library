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
 * GET /api/{version}/services/ai/agent_actions/status
 */
public class AgentActionExecutionStatusResponse extends VaultResponse {

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

        @JsonProperty("agentAction")
        public String getAgentAction() {
            return this.getString("agentAction");
        }

        public void setAgentAction(String agentAction) {
            this.set("agentAction", agentAction);
        }

        @JsonProperty("actionOutputs")
        public List<ActionOutput> getActionOutputs() {
            return (List<ActionOutput>) this.get("actionOutputs");
        }

        public void setActionOutputs(List<ActionOutput> actionOutputs) {
            this.set("actionOutputs", actionOutputs);
        }

        public static class ActionOutput extends VaultModel {

            @JsonProperty("type")
            public String getType() {
                return this.getString("type");
            }

            public void setType(String type) {
                this.set("type", type);
            }

            @JsonProperty("value")
            public String getValue() {
                return this.getString("value");
            }

            public void setValue(String value) {
                this.set("value", value);
            }
        }
    }
}
