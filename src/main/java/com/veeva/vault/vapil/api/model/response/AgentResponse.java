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
 * GET /api/{version}/services/ai/agents/{agent_name}
 */
public class AgentResponse extends VaultResponse {

    @JsonProperty("agent")
    public Agent getAgent() {
        return (Agent) this.get("agent");
    }

    public void setAgent(Agent agent) {
        this.set("agent", agent);
    }

    public static class Agent extends VaultModel {

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

        @JsonProperty("status")
        public Boolean getStatus() {
            return this.getBoolean("status");
        }

        public void setStatus(Boolean status) {
            this.set("status", status);
        }

        @JsonProperty("agentClass")
        public String getAgentClass() {
            return this.getString("agentClass");
        }

        public void setAgentClass(String agentClass) {
            this.set("agentClass", agentClass);
        }

        @JsonProperty("documentType")
        public String getDocumentType() {
            return this.getString("documentType");
        }

        public void setDocumentType(String documentType) {
            this.set("documentType", documentType);
        }

        @JsonProperty("description")
        public String getDescription() {
            return this.getString("description");
        }

        public void setDescription(String description) {
            this.set("description", description);
        }

        @JsonProperty("source")
        public String getSource() {
            return this.getString("source");
        }

        public void setSource(String source) {
            this.set("source", source);
        }

        @JsonProperty("agentUser")
        public String getAgentUser() {
            return this.getString("agentUser");
        }

        public void setAgentUser(String agentUser) {
            this.set("agentUser", agentUser);
        }

        @JsonProperty("agentModelLevel")
        public String getAgentModelLevel() {
            return this.getString("agentModelLevel");
        }

        public void setAgentModelLevel(String agentModelLevel) {
            this.set("agentModelLevel", agentModelLevel);
        }

        @JsonProperty("actions")
        public List<AgentAction> getActions() {
            return (List<AgentAction>) this.get("actions");
        }

        public void setActions(List<AgentAction> actions) {
            this.set("actions", actions);
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

            @JsonProperty("url")
            public String getUrl() {
                return this.getString("url");
            }

            public void setUrl(String url) {
                this.set("url", url);
            }
        }
    }
}