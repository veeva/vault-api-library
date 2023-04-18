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

import java.util.List;

public class SandboxSnapshotResponse extends VaultResponse {

	@JsonProperty("data")
	public Sandbox getData() {
		return (Sandbox) this.get("data");
	}

	public void setData(Sandbox data) {
		this.set("data", data);
	}

	public static class Sandbox extends VaultModel {

		@JsonProperty("available")
		public Integer getAvailable() {
			return this.getInteger("available");
		}

		public void setAvailable(int available) {
			this.set("available", available);
		}

		@JsonProperty("snapshots")
		public List<Snapshot> getSnapshots() {return (List<Snapshot>) this.get("snapshots");}

		public void setSnapshots(List<Snapshot> snapshots) {
			this.set("snapshots", snapshots);
		}

	}

	public static class Snapshot extends VaultModel {

		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("api_name")
		public String getApiName() {
			return this.getString("api_name");
		}

		public void setApiName(String apiName) {this.set("api_name", apiName);}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("description")
		public String getDescription() {
			return this.getString("description");
		}

		public void setDescription(String description) {this.set("description", description);}

		@JsonProperty("status")
		public String getStatus() {
			return this.getString("status");
		}

		public void setStatus(String status) {
			this.set("status", status);
		}

		@JsonProperty("upgrade_status")
		public String getUpgradeStatus() {
			return this.getString("upgrade_status");
		}

		public void setUpgradeStatus(String upgradeStatus) {
			this.set("upgrade_status", upgradeStatus);
		}

		@JsonProperty("source_sandbox")
		public String getSourceSandbox() {
			return this.getString("source_sandbox");
		}

		public void setSourceSandbox(String sourceSandbox) {
			this.set("source_sandbox", sourceSandbox);
		}

		@JsonProperty("total_object_records")
		public Integer getTotalObjectRecords() {
			return this.getInteger("total_object_records");
		}

		public void setTotalObjectRecords(int totalObjectRecords) {
			this.set("total_object_records", totalObjectRecords);
		}

		@JsonProperty("document_versions")
		public Integer getDocumentVersions() {
			return this.getInteger("document_versions");
		}

		public void setDocumentVersions(int documentVersions) {
			this.set("document_versions", documentVersions);
		}

		@JsonProperty("vault_version")
		public String getVaultVersion() {
			return this.getString("vault_version");
		}

		public void setVaultVersion(String vaultVersion) {
			this.set("vault_version", vaultVersion);
		}

		@JsonProperty("update_available")
		public String getUpdateAvailable() {
			return this.getString("update_available");
		}

		public void setUpdateAvailable(String updateAvailable) {
			this.set("update_available", updateAvailable);
		}

		@JsonProperty("created_date")
		public String getCreatedDate() {
			return this.getString("created_date");
		}

		public void setCreatedDate(String createdDate) {
			this.set("created_date", createdDate);
		}

		@JsonProperty("expiration_date")
		public String getExpirationDate() {
			return this.getString("expiration_date");
		}

		public void setExpirationDate(String expirationDate) {
			this.set("expiration_date", expirationDate);
		}

		@JsonProperty("domain")
		public String getDomain() {
			return this.getString("domain");
		}

		public void setDomain(String domain) {
			this.set("domain", domain);
		}

		@JsonProperty("created_by")
		public Integer getCreatedBy() {
			return this.getInteger("created_by");
		}

		public void setCreatedBy(int createdBy) {
			this.set("created_by", createdBy);
		}

	}

}

