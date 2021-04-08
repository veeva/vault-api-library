package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.*;

/**
 * Model for responses to single Document Role Update and Delete requests
 *
 * @see DocumentRoleRequest#assignUsersAndGroupsToRolesOnASingleDocument(DocumentRequestType, int)
 * @see DocumentRoleRequest#removeUsersAndGroupsFromRolesOnASingleDocument(DocumentRequestType, int, String, DocumentRoleRequest.MemberType, long)
 */
public class DocumentRoleChangeResponse extends VaultResponse {

	@JsonProperty("updatedRoles")
	public Map<String, Map<String, List<Long>>> getUpdatedRoles() {
		return (Map<String, Map<String, List<Long>>>) this.get("updatedRoles");
	}

	public void setUpdatedRoles(Map<String, Map<String, List<Long>>> updatedRoles) {
		this.set("updatedRoles", updatedRoles);
	}
}
