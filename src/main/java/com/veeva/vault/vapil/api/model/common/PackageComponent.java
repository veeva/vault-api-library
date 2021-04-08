/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the VPK Components
 */
public class PackageComponent extends VaultModel {

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

	@JsonProperty("checksum__v")
	public String getChecksum() {
		return getString("checksum__v");
	}

	public void setChecksum(String checksum) {
		this.set("checksum__v", checksum);
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

	@JsonProperty("id")
	public String getId() {
		return getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("name__v")
	@JsonAlias({"name"})
	public String getName() {
		if (this.getFieldNames().contains("name"))
			return getString("name");
		else
			return getString("name__v");
	}

	public void setName(String name) {
		this.set("name__v", name);
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
