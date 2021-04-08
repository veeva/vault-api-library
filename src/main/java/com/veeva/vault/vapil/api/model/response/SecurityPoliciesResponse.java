package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.SecurityPolicyRequest;

import java.util.List;

/**
 * Model for response to requests to the Retrieve Security Policies endpoint
 *
 * @see SecurityPolicyRequest#retrieveAllSecurityPolicies()
 */
public class SecurityPoliciesResponse extends VaultResponse {

	@JsonProperty("security_policies__v")
	public List<SecurityPolicy> getSecurityPolicies() {
		return (List<SecurityPolicy>) this.get("security_policies__v");
	}

	public void setSecurityPolicies(List<SecurityPolicy> securityPolicies) {
		this.set("security_policies__v", securityPolicies);
	}

	public static class SecurityPolicy extends VaultModel {

		@JsonProperty("name__v")
		public String getName() {
			return this.getString("name__v");
		}

		public void setName(String name) {
			this.set("name__v", name);
		}

		@JsonProperty("label__v")
		public String getLabel() {
			return this.getString("label__v");
		}

		public void setLabel(String label) {
			this.set("label__v", label);
		}

		@JsonProperty("value__v")
		public String getValue() {
			return this.getString("value__v");
		}

		public void setValue(String value) {
			this.set("value__v", value);
		}
	}
}
