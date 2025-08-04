package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.List;

/**
 * Model for responses to bulk Document Role Requests
 * <p>
 * POST /api/{version}/objects/documents/roles/batch
 * <br>
 * DELETE /api/{version}/objects/documents/roles/batch
 * @see DocumentRoleRequest#assignUsersAndGroupsToRolesOnMultipleDocuments()
 * @see DocumentRoleRequest#removeUsersAndGroupsFromRolesOnMultipleDocuments()
 */
public class DocumentRoleChangeBulkResponse extends RoleChangeBulkResponse {

}
