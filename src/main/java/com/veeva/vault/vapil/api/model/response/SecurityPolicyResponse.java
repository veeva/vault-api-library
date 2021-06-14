package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.SecurityPolicyRequest;

/**
 * Model for response to requests to the Retrieve Security Policy endpoint
 *
 * @see SecurityPolicyRequest#retrieveSecurityPolicy(String)
 */
public class SecurityPolicyResponse extends VaultResponse {

	@JsonProperty("security_policy__v")
	public SecurityPolicy getSecurityPolicy() {
		return (SecurityPolicy) this.get("security_policy__v");
	}

	public void setSecurityPolicy(SecurityPolicy securityPolicy) {
		this.set("security_policy__v", securityPolicy);
	}

	public static class SecurityPolicy extends VaultModel {

		@JsonProperty("policy_details__v")
		public PolicyDetails getPolicyDetails() {
			return (PolicyDetails) this.get("policy_details__v");
		}

		public void setPolicyDetails(SecurityPolicy policyDetails) {
			this.set("policy_details__v", policyDetails);
		}

		@JsonProperty("policy_security_settings__v")
		public PolicySecuritySettings getPolicySecuritySettings() {
			return (PolicySecuritySettings) this.get("policy_security_settings__v");
		}

		public void setPolicySecuritySettings(SecurityPolicy policySecuritySettings) {
			this.set("policy_security_settings__v", policySecuritySettings);
		}

		public static class PolicyDetails extends VaultModel {

			@JsonProperty("description__v")
			public String getDescription() {
				return this.getString("description__v");
			}

			public void setDescription(String description) {
				this.set("description__v", description);
			}

			@JsonProperty("is_active__v")
			public Boolean getIsActive() {
				return this.getBoolean("is_active__v");
			}

			public void setIsActive(String isActive) {
				this.set("is_active__v", isActive);
			}

			@JsonProperty("label__v")
			public String getLabel() {
				return this.getString("label__v");
			}

			public void setLabel(String label) {
				this.set("label__v", label);
			}

			@JsonProperty("name__v")
			public String getName() {
				return this.getString("name__v");
			}

			public void setName(String name) {
				this.set("name__v", name);
			}
		}

		public static class PolicySecuritySettings extends VaultModel {

			@JsonProperty("allow_delegated_auth_sfdc__v")
			public Boolean getAllowDelegatedAuthSFDC() {
				return this.getBoolean("allow_delegated_auth_sfdc__v");
			}

			public void setAllowDelegatedAuthSfdc(String allowDelegatedAuthSfdc) {
				this.set("allow_delegated_auth_sfdc__v", allowDelegatedAuthSfdc);
			}

			@JsonProperty("authentication_type__v")
			public AuthenticationType getAuthenticationType() {
				return (AuthenticationType) this.get("authentication_type__v");
			}

			public void setAuthenticationType(String authenticationType) {
				this.set("authentication_type__v", authenticationType);
			}

			@JsonProperty("min_password_length__v")
			public Integer getMinPasswordLength() {
				return this.getInteger("min_password_length__v");
			}

			public void setMinPasswordLength(String minPasswordLength) {
				this.set("min_password_length__v", minPasswordLength);
			}

			@JsonProperty("password_expiration__v")
			public Integer getPasswordExpiration() {
				return this.getInteger("password_expiration__v");
			}

			public void setPasswordExpiration(String passwordExpiration) {
				this.set("password_expiration__v", passwordExpiration);
			}

			@JsonProperty("password_history_reuse__v")
			public Integer getPasswordHistoryReuse() {
				return this.getInteger("password_history_reuse__v");
			}

			public void setPasswordHistoryReuse(String passwordHistoryReuse) {
				this.set("password_history_reuse__v", passwordHistoryReuse);
			}

			@JsonProperty("passwords_require_number__v")
			public Boolean getPasswordsRequireNumber() {
				return this.getBoolean("passwords_require_number__v");
			}

			public void setPasswordsRequireNumber(String passwordsRequireNumber) {
				this.set("passwords_require_number__v", passwordsRequireNumber);
			}

			@JsonProperty("passwords_require_uppercase_letter__v")
			public Boolean getPasswordsRequireUpperCaseLetter() {
				return this.getBoolean("passwords_require_uppercase_letter__v");
			}

			public void setPasswordsRequireUpperCaseLetter(String passwordsRequireUppercaseLetter) {
				this.set("passwords_require_uppercase_letter__v", passwordsRequireUppercaseLetter);
			}

			@JsonProperty("passwords_require_nonalpha_char__v")
			public Boolean getPasswordsRequireNonAlphaChar() {
				return this.getBoolean("passwords_require_nonalpha_char__v");
			}

			public void setPasswordsRequireNonAlphaChar(String passwordsRequireNonAlphaChar) {
				this.set("passwords_require_nonalpha_char__v", passwordsRequireNonAlphaChar);
			}

			@JsonProperty("require_question_on_password_reset__v")
			public Boolean getRequireQuestionOnPasswordReset() {
				return this.getBoolean("require_question_on_password_reset__v");
			}

			public void setRequireQuestionOnPasswordReset(String requireQuestionOnPasswordReset) {
				this.set("require_question_on_password_reset__v", requireQuestionOnPasswordReset);
			}

			@JsonProperty("sfdc_org_id__v")
			public String getSfdcOrgId() {
				return this.getString("sfdc_org_id__v");
			}

			public void setSfdcOrgId(String sfdcOrgId) {
				this.set("sfdc_org_id__v", sfdcOrgId);
			}

			public static class AuthenticationType extends VaultModel {

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
			}
		}
	}

}
