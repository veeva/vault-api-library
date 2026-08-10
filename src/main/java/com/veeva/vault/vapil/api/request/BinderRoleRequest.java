package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Binder Role Requests
 * @vapil.apicoverage <a href="https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles">https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles</a>
 */
public class BinderRoleRequest extends VaultRequest<BinderRoleRequest> {
	private static Logger log = LoggerFactory.getLogger(BinderRoleRequest.class);

	//API Endpoints
	private static final String URL_BINDER_ROLES_RETRIEVE = "/objects/binders/{id}/roles";
	private static final String URL_BINDER_ROLE_RETRIEVE = "/objects/binders/{id}/roles/{role_name}";
	private static final String URL_BINDER_ROLE_ASSIGN_SINGLE = "/objects/binders/{id}/roles";
	private static final String URL_DOC_ROLE_BATCH = "/objects/documents/roles/batch";
	private static final String URL_BINDER_ROLE_REMOVE_SINGLE = "/objects/binders/{binder_id}/roles/{role_name}.{user_or_group}/{id}";

	//API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private Map<String, Object> bodyParams = null;
	private String headerContentType;
	private String inputPath;
	private String requestString; // For raw request

	private BinderRoleRequest() {
	}

	/**
	 * <b>Retrieve All Binder Roles</b>
	 * <p>
	 * Retrieve all available roles on a binder and the users and groups assigned to them.
	 *
	 * @param id                  The Document id
	 * @return BinderRoleRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}/roles</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/retrieve-all-binder-roles' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/retrieve-all-binder-roles</a>
	 * @vapil.request <pre>
	 * BinderRoleRetrieveResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.retrieveAllBinderRoles(binderId);
	 * </pre>
	 * @vapil.response <pre>
	 * for (RoleRetrieveResponse.Role role : response.getRoles()) {
	 * 		System.out.println("---------Binder Role---------");
	 * 		System.out.println("Name: " + role.getName());
	 * 		System.out.println("Label: " + role.getLabel());
	 * 		System.out.println("Available Users: " + role.getAvailableUsers());
	 * 		System.out.println("Available Groups: " + role.getAvailableGroups());
	 * }
	 * </pre>
	 */
	public BinderRoleRetrieveResponse retrieveAllBinderRoles(int id) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ROLES_RETRIEVE);
		url = url.replace("{id}", Integer.toString(id));

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, BinderRoleRetrieveResponse.class);
	}

	/**
	 * <b>Retrieve Binder Role</b>
	 * <p>
	 * Retrieve a specific role on a binder and the users and groups assigned to it.
	 *
	 * @param id                  The Binder id
	 * @param roleName            Name of the Role to retrieve
	 * @return BinderRoleRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}/roles/{role_name}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/retrieve-binder-role' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/retrieve-binder-role</a>
	 * @vapil.request <pre>
	 * BinderRoleRetrieveResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.retrieveBinderRole(binderId, "owner__v");
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Name: " + response.getRoles().get(0).getName());
	 * System.out.println("Label: " + response.getRoles().get(0).getLabel());
	 * System.out.println("Available Users: " + response.getRoles().get(0).getAvailableUsers());
	 * System.out.println("Available Groups: " + response.getRoles().get(0).getAvailableGroups());
	 * </pre>
	 */
	public BinderRoleRetrieveResponse retrieveBinderRole(int id, String roleName) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ROLE_RETRIEVE);
		url = url.replace("{id}", Integer.toString(id));
		url = url.replace("{role_name}", roleName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, BinderRoleRetrieveResponse.class);
	}

	/**
	 * <b>Assign Users and Groups to Roles on a Single Binder</b>
	 *
	 * @param id                  The Binder id
	 * @return BinderRoleChangeResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/{binder_id}/roles</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/assign-users-groups-to-roles-on-a-single-binder' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/assign-users-groups-to-roles-on-a-single-binder</a>
	 * @vapil.request <pre>
	 * BinderRoleChangeResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.setBodyParams(Collections.singletonMap("editor__v.users", userId))
	 * 		.assignUsersAndGroupsToRolesOnASingleBinder(binderId);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("-----Users added to Editor role-----");
	 * for (Long userId : response.getUpdatedRoles().get("editor__v").get("users")) {
	 * 		System.out.println("User ID: " + userId);
	 * }
	 * </pre>
	 */
	public BinderRoleChangeResponse assignUsersAndGroupsToRolesOnASingleBinder(int id) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ROLE_ASSIGN_SINGLE);
		url = url.replace("{id}", Integer.toString(id));
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(this.bodyParams);
		return send(HttpMethod.POST, request, BinderRoleChangeResponse.class);
	}

	/**
	 * <b>Assign Users &amp; Groups to Roles on Multiple Binders</b>
	 * <p>
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return BinderRoleChangeBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/roles/batch</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/document-roles/assign-users-groups-to-roles-on-multiple-documents-binders' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/document-roles/assign-users-groups-to-roles-on-multiple-documents-binders</a>
	 * @vapil.request <pre>
	 * BinderRoleChangeBulkResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.setContentTypeCsv()
	 * 		.setInputPath(inputPath)
	 * 		.assignUsersAndGroupsToRolesOnMultipleBinders();
	 * </pre>
	 * @vapil.response <pre>
	 * for (RoleChangeBulkResponse.RoleChange binderResponse : response.getData()) {
	 * 		System.out.println("Binder ID: " + binderResponse.getId());
	 * 		System.out.println("Response Status: " + binderResponse.getResponseStatus());
	 * 		System.out.println("-----Users added to Editor role-----");
	 * 		for (int userId : binderResponse.getListInteger("editor__v.users")) {
	 * 			System.out.println("User ID: " + userId);
	 * 		}
	 * 		System.out.println();
	 * }
	 * </pre>
	 * @see <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>
	 */
	public BinderRoleChangeBulkResponse assignUsersAndGroupsToRolesOnMultipleBinders() {
		return assignOrRemoveUsersAndGroupsToRolesOnMultipleBinders(HttpMethod.POST);
	}

	/**
	 * <b>Remove Users and Groups from Roles on Multiple Binders</b>
	 * <p>
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return BinderRoleChangeBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/roles/batch</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/document-roles/remove-users-groups-from-roles-on-multiple-documents-binders' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/document-roles/remove-users-groups-from-roles-on-multiple-documents-binders</a>
	 * @vapil.request <pre>
	 * BinderRoleChangeBulkResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.setContentTypeCsv()
	 * 		.setInputPath(inputPath)
	 * 		.removeUsersAndGroupsFromRolesOnMultipleBinders();
	 * </pre>
	 * @vapil.response <pre>
	 * for (RoleChangeBulkResponse.RoleChange binderResponse : response.getData()) {
	 * 		System.out.println("Binder ID: " + binderResponse.getId());
	 * 		System.out.println("Response Status: " + binderResponse.getResponseStatus());
	 * 		System.out.println("-----Users removed from Editor role-----");
	 * 		for (int userId : binderResponse.getListInteger("editor__v.users")) {
	 * 			System.out.println("User ID: " + userId);
	 * 		}
	 * 		System.out.println();
	 * }
	 * </pre>
	 * @see <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>
	 */
	public BinderRoleChangeBulkResponse removeUsersAndGroupsFromRolesOnMultipleBinders() {
		return assignOrRemoveUsersAndGroupsToRolesOnMultipleBinders(HttpMethod.DELETE);
	}

	/**
	 * <b>Remove Users and Groups from Roles on a Single Binder</b>
	 * <p>
	 * <b>Use bulk API for multiple binders</b>
	 *
	 * @param binderId               The Binder id
	 * @param roleName            Name of the Role to modify
	 * @param memberType          User or Group
	 * @param id                  User or Group id to remove
	 * @return BinderRoleChangeResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/binders/{binder_id}/roles/{role_name_and_user_or_group}/{id}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/remove-users-groups-from-roles-on-a-single-binder' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/document-binder-roles/binder-roles/remove-users-groups-from-roles-on-a-single-binder</a>
	 * @vapil.request <pre>
	 * BinderRoleChangeResponse response = vaultClient.newRequest(BinderRoleRequest.class)
	 * 		.removeUsersAndGroupsFromRolesOnASingleBinder(binderId,
	 * 			"editor__v",
	 * 			BinderRoleRequest.MemberType.USER,
	 * 			userIdToRemove);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("-----Users added to Editor role-----");
	 * 		for (Long userId : response.getUpdatedRoles().get("editor__v").get("users")) {
	 * 			System.out.println("User ID: " + userId);
	 * 		}
	 * </pre>
	 */
	public BinderRoleChangeResponse removeUsersAndGroupsFromRolesOnASingleBinder(int binderId, String roleName, MemberType memberType, long id) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ROLE_REMOVE_SINGLE);
		url = url.replace("{binder_id}", Integer.toString(binderId));
		url = url.replace("{role_name}", roleName);
		url = url.replace("{user_or_group}", memberType.getValue());
		url = url.replace("{id}", Long.toString(id));
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.DELETE, request, BinderRoleChangeResponse.class);
	}

	/**
	 * Private method for add or remove requests to the bulk Document Role endpoint
	 *
	 * @param method The http method for the request
	 * @return BinderRoleChangeBulkResponse
	 */
	private BinderRoleChangeBulkResponse assignOrRemoveUsersAndGroupsToRolesOnMultipleBinders(HttpMethod method) {
		if (!isValidCRUDRequest()) {
			return null;
		}

		String url = vaultClient.getAPIEndpoint(URL_DOC_ROLE_BATCH);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
		}

		if (inputPath != null && !inputPath.isEmpty()) {
			request.addFile(headerContentType, inputPath);
		}

		if (binaryFile != null) {
			request.addBinary(headerContentType, binaryFile.getBinaryContent());
		}

		if (requestString != null && !requestString.isEmpty()) {
			request.addRawString(headerContentType, requestString);
		}

		return send(method, request, BinderRoleChangeBulkResponse.class);
	}

	/**
	 * Enum for User or Group request on single binder remove endpoint
	 *
	 * @see #removeUsersAndGroupsFromRolesOnASingleBinder(int, String, MemberType, long)
	 */
	public enum MemberType {
		USER("user"),
		GROUP("group");

		private String value;

		MemberType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public BinderRoleRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public BinderRoleRequest setContentTypeXForm() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public BinderRoleRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Single form add/remove request with name/value body parameters
	 * <br>
	 * Can be used on single Document endpoint, or for performing single
	 * Document operations, using the bulk endpoint.
	 *
	 * @param bodyParams map of body params
	 * @return The Request
	 */
	public BinderRoleRequest setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public BinderRoleRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public BinderRoleRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * Determine if the Create-Read-Update-Delete request
	 * is properly formed before sending
	 *
	 * @return True if the request is properly formed
	 */
	private boolean isValidCRUDRequest() {
		if (headerContentType == null) {
			log.error("Invalid request - no content type is set");
			return false;
		}

		// Verify there is data - file, binary, or name/value pairs
		if (inputPath == null || inputPath.isEmpty()) {
			if (bodyParams == null || bodyParams.isEmpty()) {
				if (requestString == null || requestString.isEmpty()) {
					if (binaryFile == null || binaryFile.getBinaryContent() == null) {
						log.error("Invalid request - no source data");
						return false;
					}
				}
			}
		}
		return true;
	}

}
