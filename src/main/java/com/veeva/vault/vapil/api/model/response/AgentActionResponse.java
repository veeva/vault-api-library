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
 * GET /api/{version}/services/ai/agent_action/{agent_name}/{actionName}
 */
public class AgentActionResponse extends VaultResponse {

    @JsonProperty("agentAction")
    public AgentAction getAgentAction() {
        return (AgentAction) this.get("agentAction");
    }

    public void setAgentAction(AgentAction agentAction) {
        this.set("agentAction", agentAction);
    }

    public static class AgentAction extends VaultModel {

        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("label")
        public String getLabel() {
            return this.getString("label");
        }

        public void setLabel(String label) {
            this.set("label", label);
        }

        @JsonProperty("active")
        public Boolean getActive() {
            return this.getBoolean("active");
        }

        public void setActive(Boolean active) {
            this.set("active", active);
        }

        @JsonProperty("source")
        public String getSource() {
            return this.getString("source");
        }

        public void setSource(String source) {
            this.set("source", source);
        }

        @JsonProperty("description")
        public String getDescription() {
            return this.getString("description");
        }

        public void setDescription(String description) {
            this.set("description", description);
        }

        @JsonProperty("supportChat")
        public Boolean getSupportChat() {
            return this.getBoolean("supportChat");
        }

        public void setSupportChat(Boolean supportChat) {
            this.set("supportChat", supportChat);
        }

        @JsonProperty("toolEvaluation")
        public String getToolEvaluation() {
            return this.getString("toolEvaluation");
        }

        public void setToolEvaluation(String toolEvaluation) {
            this.set("toolEvaluation", toolEvaluation);
        }
    }
}
