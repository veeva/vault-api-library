package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.request.DocumentRoleRequest;

import java.util.List;

/**
 * Model for responses to Document Role GET requests
 * <p>
 * GET /api/{version}/objects/documents/{doc_id}/roles
 * <br>
 * GET /api/{version}/objects/documents/{doc_id}/roles/{role_name}
 */
public class DocumentRoleRetrieveResponse extends RoleRetrieveResponse {

}
