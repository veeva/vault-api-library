package com.veeva.vault.vapil.api.model.response;

import com.veeva.vault.vapil.api.request.BinderRoleRequest;

/**
 * Model for responses to bulk Document Role Requests
 * <p>
 * POST /api/{version}/objects/documents/roles/batch
 * <br>
 * DELETE /api/{version}/objects/documents/roles/batch
 * @see BinderRoleRequest#assignUsersAndGroupsToRolesOnMultipleBinders()
 * @see BinderRoleRequest#removeUsersAndGroupsFromRolesOnMultipleBinders()
 */
public class BinderRoleChangeBulkResponse extends RoleChangeBulkResponse {

}
