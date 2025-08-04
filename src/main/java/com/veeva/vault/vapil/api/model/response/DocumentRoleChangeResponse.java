package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.*;

/**
 * Model for responses to single Document Role Update and Delete requests
 * <p>
 * POST /api/{version}/objects/documents/{doc_id}/roles
 * <br>
 * DELETE /api/{version}/objects/documents/{doc_id}/roles/{role_name_and_user_or_group}/{id}
 */
public class DocumentRoleChangeResponse extends RoleChangeResponse {


}
