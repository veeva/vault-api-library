package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for Package Steps data
 */
public class PackageStep extends PackageModel {
    @JsonProperty("deployment_action")
    public String getDeploymentAction() {
        return this.getString("deployment_action");
    }

    public void setDeploymentAction(String deploymentAction) {
        this.set("deployment_action", deploymentAction);
    }

    @JsonProperty("deployment_status__v")
    public String getDeploymentStatus() {
        return this.getString("deployment_status__v");
    }

    public void setDeploymentStatus(String deploymentStatus) {
        this.set("deployment_status__v", deploymentStatus);
    }

    @JsonProperty("step_label__v")
    public String getStepLabel() {
        return this.getString("step_label__v");
    }

    public void setStepLabel(String stepLabel) {
        this.set("step_label__v", stepLabel);
    }

    @JsonProperty("step_name__v")
    public String getStepName() {
        return this.getString("step_name__v");
    }

    public void setStepName(String stepName) {
        this.set("step_name__v", stepName);
    }

    @JsonProperty("step_type__v")
    public String getStepType() {
        return this.getString("step_type__v");
    }

    public void setStepType(String stepType) {
        this.set("step_type__v", stepType);
    }

    @JsonProperty("type__v")
    public String getType() {
        return this.getString("type__v");
    }

    public void setType(String type) {
        this.set("type__v", type);
    }

    @JsonProperty("validation_message")
    public String getValidationMessage() {
        return this.getString("validation_message");
    }

    public void setValidationMessage(String validationMessage) {
        this.set("validation_message", validationMessage);
    }

    @JsonProperty("validation_response")
    public String getValidationResponse() {
        return this.getString("validation_response");
    }

    public void setValidationResponse(String validationResponse) {
        this.set("validation_response", validationResponse);
    }

    @JsonProperty("package_components")
    public List<PackageComponent> getPackageComponents() { return (List<PackageComponent>) this.get("package_components"); }

    public void setPackageComponents(List<PackageComponent> packageComponents) { this.set("package_components", packageComponents);}



    public static class PackageComponent extends PackageModel {

        @JsonProperty("component_name__v")
        @JsonAlias({"componentName"})
        public String getComponentName() {
            if (this.getFieldNames().contains("componentName"))
                return getString("componentName");
            else
                return getString("component_name__v");
        }

        public void setComponentName(String componentName) {
            this.set("component_name__v", componentName);
        }

        @JsonProperty("component_type__v")
        @JsonAlias({"componentType"})
        public String getComponentType() {
            if (this.getFieldNames().contains("componentType"))
                return getString("componentType");
            else
                return getString("component_type__v");
        }

        public void setComponentType(String componentType) {
            this.set("component_type__v", componentType);
        }


        @JsonProperty("deployment_status__v")
        public String getDeploymentStatus() {
            return getString("deployment_status__v");
        }

        public void setDeploymentStatus(String deploymentStatus) {
            this.set("deployment_status__v", deploymentStatus);
        }

        @JsonProperty("external_id__v")
        public String getExternalId() {
            return getString("external_id__v");
        }

        public void setExternalId(String externalId) {
            this.set("external_id__v", externalId);
        }

        @JsonProperty("status")
        public String getStatus() {
            return getString("status");
        }

        public void setStatus(String status) {
            this.set("status", status);
        }

        @JsonProperty("step")
        public String getStep() {
            return getString("step");
        }

        public void setStep(String step) {
            this.set("step", step);
        }

        @JsonProperty("stepId")
        public String getStepId() {
            return getString("stepId");
        }

        public void setStepId(String stepId) {
            this.set("stepId", stepId);
        }
    }

    public static class PackageData extends PackageModel {

        @JsonProperty("stepId")
        public String getStepId() {
            return this.getString("stepId");
        }

        public void setStepId(String stepId) {
            this.set("stepId", stepId);
        }

        @JsonProperty("object__v")
        public String getObject() {
            return this.getString("object__v");
        }

        public void setObject(String object) {
            this.set("object__v", object);
        }

        @JsonProperty("data_type__v")
        public String getDataType() {
            return this.getString("data_type__v");
        }

        public void setDataType(String dataType) {
            this.set("data_type__v", dataType);
        }

        @JsonProperty("data_action__v")
        public String getDataAction() {
            return this.getString("data_action__v");
        }

        public void setDataAction(String dataAction) {
            this.set("data_action__v", dataAction);
        }

        @JsonProperty("key_field__sys")
        public String getKeyField() {
            return this.getString("key_field__v");
        }

        public void setKeyField(String keyField) {
            this.set("key_field__sys", keyField);
        }

        @JsonProperty("record_migration_mode__sys")
        public Boolean getRecordMigrationMode() {
            return this.getBoolean("record_migration_mode__sys");
        }

        public void setRecordMigrationMode(Boolean recordMigrationMode) {
            this.get("record_migration_mode__sys");
        }

        @JsonProperty("record_count__sys")
        public String getRecordCount() {
            return this.getString("record_count__sys");
        }

        public void setRecordCount(String recordCount) {
            this.set("record_count__sys", recordCount);
        }
    }

    public static class PackageCode extends PackageModel {

        @JsonProperty("deployment_option__sys")
        public String getDeploymentOption() { return this.getString("deployment_option__sys"); }

        public void setDeploymentOption(String deploymentOption) { this.set("deployment_option__sys", deploymentOption); }
    }
}
