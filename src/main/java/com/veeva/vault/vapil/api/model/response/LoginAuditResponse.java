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

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/audittrail/{audit_trail_type}
 * <br>
 * For use when {audit_trail_type} is login_audit_trail
 */
public class LoginAuditResponse extends AuditDetailsResponse {

	@JsonProperty("data")
	public List<LoginAuditData> getData() {
		return (List<LoginAuditData>) this.get("data");
	}

	public void setData(List<LoginAuditData> data) {
		this.set("data", data);
	}

	public static class LoginAuditData extends Audit {

		@JsonProperty("browser")
		public String getBrowser() {
			return this.getString("browser");
		}

		public void setBrowser(String browser) {
			this.set("browser", browser);
		}

		@JsonProperty("platform")
		public String getPlatform() {
			return this.getString("platform");
		}

		public void setPlatform(String platform) {
			this.set("platform", platform);
		}

		@JsonProperty("source_ip")
		public String getSourceIp() {
			return this.getString("source_ip");
		}

		public void setSourceIp(String sourceIp) {
			this.set("source_ip", sourceIp);
		}

		@JsonProperty("status")
		public String getStatus() {
			return this.getString("status");
		}

		public void setStatus(String status) {
			this.set("status", status);
		}

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("vault_id")
		public String getVaultId() {
			return this.getString("vault_id");
		}

		public void setVaultId(String vaultId) {
			this.set("vault_id", vaultId);
		}
	}
}