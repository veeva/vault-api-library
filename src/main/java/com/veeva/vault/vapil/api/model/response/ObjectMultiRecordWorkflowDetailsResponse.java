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

/**
 * Retrieve Multi-Record Workflow Details
 * <p>
 * GET /api/{version}/objects/objectworkflows/actions/{workflow_name} <br>
 */
public class ObjectMultiRecordWorkflowDetailsResponse extends VaultResponse {

    @JsonProperty("data")
    public MultiRecordWorkflow getData() {
        return (MultiRecordWorkflow) this.get("data");
    }

    public void setData(MultiRecordWorkflow data) {
        this.set("data", data);
    }

    public static class MultiRecordWorkflow extends VaultModel {

        @JsonProperty("name")
        public String getName() {
            return this.getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("controls")
        public List<Control> getControls() {
            return (List<Control>) this.get("controls");
        }

        public void setControls(List<Control> controls) {
            this.set("controls", controls);
        }

        @JsonProperty("label")
        public String getLabel() {
            return this.getString("label");
        }

        public void setLabel(String label) {
            this.set("label", label);
        }

        @JsonProperty("type")
        public String getType() {
            return this.getString("type");
        }

        public void setType(String type) {
            this.set("type", type);
        }

        @JsonProperty("cardinality")
        public String getCardinality() {
            return this.getString("cardinality");
        }

        public void setCardinality(String cardinality) {
            this.set("cardinality", cardinality);
        }
    }


    public static class Control extends VaultModel {

        @JsonProperty("label")
        public String getLabel() {
            return this.getString("label");
        }

        public void setLabel(String label) {
            this.set("label", label);
        }

        @JsonProperty("type")
        public String getType() {
            return this.getString("type");
        }

        public void setType(String type) {
            this.set("type", type);
        }

        @JsonProperty("prompts")
        public List<Prompt> getPrompts() {
            return (List<Prompt>) this.get("prompts");
        }

        public void setPrompts(List<Prompt> prompts) {
            this.set("prompts", prompts);
        }

        public static class Prompt extends VaultModel {

            @JsonProperty("label")
            public String getLabel() {
                return this.getString("label");
            }

            public void setLabel(String label) {
                this.set("label", label);
            }

            @JsonProperty("name")
            public String getName() {
                return this.getString("name");
            }

            public void setName(String name) {
                this.set("name", name);
            }

            @JsonProperty("multi_value")
            public Boolean getMultiValue() {
                return this.getBoolean("multi_value");
            }

            public void setMultiValue(Boolean multiValue) {
                this.set("multi_value", multiValue);
            }
        }
    }

}