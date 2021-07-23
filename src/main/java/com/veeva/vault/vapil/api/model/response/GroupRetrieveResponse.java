/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.Group;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/groups
 * GET /api/{version}/objects/groups/auto
 * GET /api/{version}/objects/groups/{group_id}
 */
public class GroupRetrieveResponse extends VaultResponse {
	@JsonProperty("groups")
	public List<GroupNode> getGroups() {
		return (List<GroupNode>) this.get("groups");
	}

	public void setGroups(List<GroupNode> groups) {
		this.set("groups", groups);
	}

	public static class GroupNode extends VaultModel {

		@JsonProperty("group")
		public Group getGroup() {
			return (Group) this.get("group");
		}

		public void setGroup(Group group) {
			this.set("group", group);
		}
	}
}