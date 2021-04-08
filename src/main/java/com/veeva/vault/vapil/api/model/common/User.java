/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for User data
 */
public class User extends VaultModel {

	@JsonProperty("active__v")
	public Boolean getActive() {
		return getBoolean("active__v");
	}

	public void setActive(Boolean active) {
		this.set("active__v", active);
	}

	@JsonProperty("company__v")
	public String getCompany() {
		return getString("company__v");
	}

	public void setCompany(String company) {
		this.set("company__v", company);
	}

	@JsonProperty("created_by__v")
	public Integer getCreatedBy() {
		return getInteger("created_by__v");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by__v", createdBy);
	}

	@JsonProperty("created_date__v")
	public String getCreatedDate() {
		return getString("created_date__v");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date__v", createdDate);
	}

	@JsonProperty("domain_active__v")
	public Boolean getDomainActive() {
		return getBoolean("domain_active__v");
	}

	public void setDomainActive(Boolean domainActive) {
		this.set("domain_active__v", domainActive);
	}

	@JsonProperty("domain_id__v")
	public Integer getDomainId() {
		return getInteger("domain_id__v");
	}

	public void setDomainId(Integer domainId) {
		this.set("domain_id__v", domainId);
	}

	@JsonProperty("domain_name__v")
	public String getDomainName() {
		return getString("domain_name__v");
	}

	public void setDomainName(String domainName) {
		this.set("domain_name__v", domainName);
	}

	@JsonProperty("fax__v")
	public String getFax() {
		return getString("fax__v");
	}

	public void setFax(String fax) {
		this.set("fax__v", fax);
	}

	@JsonProperty("federated_id__v")
	public String getFederatedId() {
		return getString("federated_id__v");
	}

	public void setFederatedId(String federatedId) {
		this.set("federated_id__v", federatedId);
	}

	@JsonProperty("group_id__v")
	public List<Long> getGroupId() {
		return (List) get("group_id__v");
	}

	public void setGroupId(List<Long> groupId) {
		this.set("group_id__v", groupId);
	}

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("is_domain_admin__v")
	public Boolean getIsDomainAdmin() {
		return getBoolean("is_domain_admin__v");
	}

	public void setIsDomainAdmin(Boolean isDomainAdmin) {
		this.set("is_domain_admin__v", isDomainAdmin);
	}

	@JsonProperty("mobile_phone__v")
	public String getMobilePhone() {
		return getString("mobile_phone__v");
	}

	public void setMobilePhone(String mobilePhone) {
		this.set("mobile_phone__v", mobilePhone);
	}

	@JsonProperty("last_login__v")
	public String getLastLogin() {
		return getString("last_login__v");
	}

	public void setLastLogin(String lastLogin) {
		this.set("last_login__v", lastLogin);
	}

	@JsonProperty("license_type__v")
	public String getLicenseType() {
		return getString("license_type__v");
	}

	public void setLicenseType(String licenseType) {
		this.set("license_type__v", licenseType);
	}

	@JsonProperty("modified_by__v")
	public Integer getModifiedBy() {
		return getInteger("modified_by__v");
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.set("modified_by__v", modifiedBy);
	}

	@JsonProperty("modified_date__v")
	public String getModifiedDate() {
		return getString("modified_date__v");
	}

	public void setModifiedDate(String modifiedDate) {
		this.set("modified_date__v", modifiedDate);
	}

	@JsonProperty("office_phone__v")
	public String getOfficePhone() {
		return getString("office_phone__v");
	}

	public void setOfficePhone(String officePhone) {
		this.set("office_phone__v", officePhone);
	}

	@JsonProperty("salesforce_user_name__v")
	public String getSalesforceUserName() {
		return getString("salesforce_user_name__v");
	}

	public void setSalesforceUserName(String salesforceUserName) {
		this.set("salesforce_user_name__v", salesforceUserName);
	}

	@JsonProperty("security_policy_id__v")
	public Integer getSecurityPolicyId() {
		return getInteger("security_policy_id__v");
	}

	public void setSecurityPolicyId(Integer securityPolicyId) {
		this.set("security_policy_id__v", securityPolicyId);
	}

	@JsonProperty("security_profile__v")
	public String getSecurityProfile() {
		return getString("security_profile__v");
	}

	public void setSecurityProfile(String securityProfile) {
		this.set("security_profile__v", securityProfile);
	}

	@JsonProperty("site__v")
	public String getSite() {
		return getString("site__v");
	}

	public void setSite(String site) {
		this.set("site__v", site);
	}

	@JsonProperty("user_email__v")
	public String getUserEmail() {
		return getString("user_email__v");
	}

	public void setUserEmail(String userEmail) {
		this.set("user_email__v", userEmail);
	}

	@JsonProperty("user_first_name__v")
	public String getUserFirstName() {
		return getString("user_first_name__v");
	}

	public void setUserFirstName(String userFirstName) {
		this.set("user_first_name__v", userFirstName);
	}

	@JsonProperty("user_language__v")
	public String getUserLanguage() {
		return getString("user_language__v");
	}

	public void setUserLanguage(String userLanguage) {
		this.set("user_language__v", userLanguage);
	}

	@JsonProperty("user_last_name__v")
	public String getUserLastName() {
		return getString("user_last_name__v");
	}

	public void setUserLastName(String userLastName) {
		this.set("user_last_name__v", userLastName);
	}

	@JsonProperty("user_locale__v")
	public String getUserLocale() {
		return getString("user_locale__v");
	}

	public void setUserLocale(String userLocale) {
		this.set("user_locale__v", userLocale);
	}

	@JsonProperty("user_name__v")
	public String getUserName() {
		return getString("user_name__v");
	}

	public void setUserName(String userName) {
		this.set("user_name__v", userName);
	}

	@JsonProperty("user_needs_to_change_password__v")
	public Boolean getUserNeedsToChangePassword() {
		return getBoolean("user_needs_to_change_password__v");
	}

	public void setUserNeedsToChangePassword(Boolean userNeedsToChangePassword) {
		this.set("user_needs_to_change_password__v", userNeedsToChangePassword);
	}

	@JsonProperty("user_timezone__v")
	public String getUserTimezone() {
		return getString("user_timezone__v");
	}

	public void setUserTimezone(String userTimezone) {
		this.set("user_timezone__v", userTimezone);
	}

	@JsonProperty("user_title__v")
	public String getUserTitle() {
		return getString("user_title__v");
	}

	public void setUserTitle(String userTitle) {
		this.set("user_title__v", userTitle);
	}

	@JsonProperty("vault_id__v")
	public List<Integer> getVaultId() {
		return (List) get("vault_id__v");
	}

	public void setVaultId(List<Integer> vaultId) {
		this.set("vault_id__v", vaultId);
	}
}
