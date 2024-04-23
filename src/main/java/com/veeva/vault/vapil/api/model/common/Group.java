/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import org.apache.commons.lang3.BooleanUtils;

public class Group extends VaultModel {
	@JsonProperty("id")
	public Long getId() {
		return getLong("id");
	}

	public void setId(Long id) {
		this.set("id", id);
	}

	@JsonProperty("active__v")
	public Boolean getActive() {
		return getBoolean("active__v");
	}

	public void setActive(Boolean active) {
		this.set("active__v", active);
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

	@JsonProperty("editable__v")
	public Boolean getEditable() {
		return getBoolean("editable__v");
	}

	public void setEditable(Boolean editable) {
		this.set("editable__v", editable);
	}

	@JsonIgnore
	public boolean isEditable() {
		return BooleanUtils.isTrue(getEditable());
	}

	@JsonProperty("group_description__v")
	public String getGroupDescription() {
		return getString("group_description__v");
	}

	public void setGroupDescription(String groupDescription) {
		this.set("group_description__v", groupDescription);
	}

	@JsonProperty("implied_members__v")
	public List<ImpliedMember> getImpliedMembers() {
		return (List<ImpliedMember>) get("implied_members__v");
	}

	public void setImpliedMembers(List<ImpliedMember> impliedMembers) {
		this.set("implied_members__v", impliedMembers);
	}

	@JsonProperty("label__v")
	public String getLabel() {
		return getString("label__v");
	}

	public void setLabel(String label) {
		this.set("label__v", label);
	}

	@JsonProperty("members__v")
	public List<Integer> getMembers() {
		return (List<Integer>) get("members__v");
	}

	public void setMembers(List<Integer> members) {
		this.set("members__v", members);
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

	@JsonProperty("name__v")
	public String getName() {
		return getString("name__v");
	}

	public void setName(String name) {
		this.set("name__v", name);
	}

	@JsonProperty("security_profiles__v")
	public List<String> getSecurityProfiles() {
		return (List<String>) get("security_profiles__v");
	}

	public void setSecurityProfiles(List<String> securityProfiles) {
		this.set("security_profiles__v", securityProfiles);
	}

	@JsonProperty("system_group__v")
	public Boolean getSystemGroup() {
		return getBoolean("system_group__v");
	}

	public void setSystemGroup(Boolean systemGroup) {
		this.set("system_group__v", systemGroup);
	}

	@JsonIgnore
	public boolean isSystemGroup() {
		return BooleanUtils.isTrue(getSystemGroup());
	}

	@JsonProperty("type__v")
	public String getType() {
		return getString("type__v");
	}

	public void setType(String type) {
		this.set("type__v", type);
	}

	@JsonProperty("allow_delegation_among_members__v")
	public Boolean getAllowDelegationAmongMembers() {
		return getBoolean("allow_delegation_among_members__v");
	}

	public void setAllowDelegationAmongMembers(Boolean allow) {
		this.set("allow_delegation_among_members__v", allow);
	}

	public static class ImpliedMember extends VaultModel {

		@JsonProperty("id")
		public Integer getId() {
			return getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("source_name")
		public String getSourceName() {
			return getString("source_name");
		}

		public void setSourceName(String sourceName) {
			this.set("source_name", sourceName);
		}

		@JsonProperty("source_type")
		public String getSourceType() {
			return getString("source_type");
		}

		public void setSourceType(String sourceType) {
			this.set("source_type", sourceType);
		}
	}
}
