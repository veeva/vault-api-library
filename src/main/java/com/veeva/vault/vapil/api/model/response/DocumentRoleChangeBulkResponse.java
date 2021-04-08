package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.List;

/**
 * Model for responses to bulk Document Role Requests
 *
 * @see DocumentRoleRequest#assignUsersAndGroupsToRolesOnMultipleDocuments()
 * @see DocumentRoleRequest#removeUsersAndGroupsFromRolesOnMultipleDocuments()
 */
public class DocumentRoleChangeBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DocumentRoleChange> getData() {
		return (List<DocumentRoleChange>) this.get(("data"));
	}

	public void setData(List<DocumentRoleChange> data) {
		this.set("data", data);
	}

	public static class DocumentRoleChange extends VaultResponse {

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		public void setId(Integer id) {
			this.set("id", id);
		}

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}
	}
}
