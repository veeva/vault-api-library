package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageCode extends PackageModel{

    @JsonProperty("deployment_option")
    public String getDeploymentOption() { return this.getString("deployment_option"); }

    public void setDeploymentOption(String deploymentOption) { this.set("deployment_option", deploymentOption); }
}
