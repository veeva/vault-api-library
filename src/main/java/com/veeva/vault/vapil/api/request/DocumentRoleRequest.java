package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.DocumentRoleChangeBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRoleRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRoleChangeResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Document Role Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.3/#document-roles">https://developer.veevavault.com/api/24.3/#document-roles</a>
 */
public class DocumentRoleRequest extends VaultRequest<DocumentRoleRequest> {
	private static Logger log = LoggerFactory.getLogger(DocumentRoleRequest.class);

	//API Endpoints
	private static final String URL_DOC_ROLE_RETRIEVE = "/objects/{documents_or_binders}/{id}/roles";
	private static final String URL_DOC_ROLE_RETRIEVE_SINGLE = "/objects/{documents_or_binders}/{id}/roles/{role_name}";
	private static final String URL_DOC_ROLE_ASSIGN_SINGLE = "/objects/{documents_or_binders}/{id}/roles";
	private static final String URL_DOC_ROLE_BATCH = "/objects/documents/roles/batch";
	private static final String URL_DOC_ROLE_REMOVE_SINGLE = "/objects/{documents_or_binders}/{doc_id}/roles/{role_name}.{user_or_group}/{id}";

	//API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private Map<String, Object> bodyParams = null;
	private String headerContentType;
	private String inputPath;
	private String requestString; // For raw request

	private DocumentRoleRequest() {
	}

	/**
	 * <b>Retrieve Roles</b>
	 * <p>
	 * Retrieve all available roles on a document or binder and the users and groups assigned to them.
	 *
	 * @param documentRequestType Documents or Binders
	 * @param id                  The Document or Binder id
	 * @return DocumentRoleRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/{documents_or_binders}/{id}/roles</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-roles' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-roles</a>
	 * @vapil.request <pre>
	 * DocumentRoleRetrieveResponse resp = vaultClient.newRequest(DocumentRoleRequest.class)
	 * 				.retrieveRoles(DocumentRequestType.DOCUMENTS, docId);</pre>
	 * @vapil.response <pre>
	 * if (resp != null &amp;&amp; resp.isSuccessful()) {
	 *   System.out.println("Document Roles:");
	 *   resp.getDocumentRoles().forEach(role -&gt; {
	 *     System.out.println("\tName:  " + role.getName());
	 *     System.out.println("\tLabel:  " + role.getLabel());
	 *     System.out.println("\tAssigned Users:  " + role.getAssignedUsers());
	 *     System.out.println("\tAssigned Groups:  " + role.getAssignedGroups());
	 *     System.out.println("\tAvailable Users:  " + role.getAvailableUsers());
	 *     System.out.println("\tAvailable Groups:  " + role.getAvailableGroups());
	 *     System.out.println("\tDefault Users:  " + role.getDefaultUsers());
	 *     System.out.println("\tDefault Groups:  " + role.getDefaultGroups());
	 *   });
	 *   System.out.println();
	 * }</pre>
	 */
	public DocumentRoleRetrieveResponse retrieveRoles(DocumentRequestType documentRequestType, int id) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_ROLE_RETRIEVE);
		url = url.replace("{documents_or_binders}", documentRequestType.getValue());
		url = url.replace("{id}", Integer.toString(id));

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentRoleRetrieveResponse.class);
	}

	/**
	 * <b>Retrieve Roles - Filter by Role</b>
	 * <p>
	 * Include a role name to filter for a specific role. For example, owner__v.
	 *
	 * @param documentRequestType Documents or Binders
	 * @param id                  The Document or Binder id
	 * @param roleName            The Role name to filter results by
	 * @return DocumentRoleRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/{documents_or_binders}/{id}/roles{/role_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-roles' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-roles</a>
	 * @vapil.request <pre>
	 * DocumentRoleRetrieveResponse resp = documentRoleRequest
	 * 				.retrieveRoles(DocumentRequestType.DOCUMENTS, docId, "owner__v");</pre>
	 * @vapil.response <pre>See {@link #retrieveRoles(DocumentRequestType, int)} for example responses</pre>
	 */
	public DocumentRoleRetrieveResponse retrieveRoles(DocumentRequestType documentRequestType, int id, String roleName) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_ROLE_RETRIEVE_SINGLE);
		url = url.replace("{documents_or_binders}", documentRequestType.getValue());
		url = url.replace("{id}", Integer.toString(id));
		url = url.replace("{role_name}", roleName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentRoleRetrieveResponse.class);
	}

	/**
	 * <b>Assign Users and Groups to Roles on a Single Document</b>
	 *
	 * @param documentRequestType Documents or Binders
	 * @param id                  The Document or Binder id
	 * @return DocumentRoleChangeResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{id}/roles</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#assign-users-amp-groups-to-roles-on-a-single-document' target='_blank'>https://developer.veevavault.com/api/24.3/#assign-users-amp-groups-to-roles-on-a-single-document</a>
	 * @vapil.request <pre>
	 * DocumentRoleChangeResponse response = vaultClient.newRequest(DocumentRoleRequest.class).
	 * 				.assignUsersAndGroupsToRolesOnASingleDocument(docId);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Updated Roles:");
	 *   response.getUpdatedRoles().forEach((k,v) -&gt; {
	 *     System.out.println("\tRole:  " + k);
	 *     v.forEach((k1, v1) -&gt; {
	 *       System.out.println("\t\t" + k1 + ":  " + v1);
	 *       });
	 *   });
	 * }</pre>
	 */
	public DocumentRoleChangeResponse assignUsersAndGroupsToRolesOnASingleDocument(DocumentRequestType documentRequestType, int id) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_ROLE_ASSIGN_SINGLE);
		url = url.replace("{documents_or_binders}", documentRequestType.getValue());
		url = url.replace("{id}", Integer.toString(id));
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(this.bodyParams);
		return send(HttpMethod.POST, request, DocumentRoleChangeResponse.class);
	}

	/**
	 * <b>Assign users and groups to roles on a document or binder in bulk.</b>
	 * <p>
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return DocumentRoleUpdateBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/roles/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#assign-users-amp-groups-to-roles-on-multiple-documents' target='_blank'>https://developer.veevavault.com/api/24.3/#assign-users-amp-groups-to-roles-on-multiple-documents</a>
	 * @vapil.request <pre>
	 * DocumentRoleUpdateBulkResponse response = vaultClient.newRequest(DocumentRoleRequest.class)
	 * 				.setInputPath(TEST_CSV)
	 * 				.setContentTypeCsv()
	 * 				documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Updated Documents:");
	 *   response.getData().forEach(entry -&gt; {
	 *     System.out.println("\tDocument id:  " + entry.getId());
	 *     System.out.println("\tUpdates made:  " + entry.getUpdates());
	 *     entry.getUpdates().forEach((k,v) -&gt; {
	 *       System.out.println("\t\t" + k + ":  " + v);
	 *       });
	 *   });
	 * }</pre>
	 * @see <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>
	 */
	public DocumentRoleChangeBulkResponse assignUsersAndGroupsToRolesOnMultipleDocuments() {
		return assignOrRemoveUsersAndGroupsToRolesOnMultipleDocuments(HttpMethod.POST);
	}

	/**
	 * <b>Remove Users and Groups from Roles on Multiple Documents</b>
	 * <p>
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return DocumentRoleUpdateBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/roles/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#remove-users-and-groups-from-roles-on-multiple-documents' target='_blank'>https://developer.veevavault.com/api/24.3/#remove-users-and-groups-from-roles-on-multiple-documents</a>
	 * @vapil.request <pre>
	 * DocumentRoleUpdateBulkResponse response = vaultClient.newRequest(DocumentRoleRequest.class)
	 * 				.setInputPath(TEST_CSV)
	 * 				.setContentTypeCsv()
	 * 				documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Updated Documents:");
	 *   response.getData().forEach(entry -&gt; {
	 *     System.out.println("\tDocument id:  " + entry.getId());
	 *     System.out.println("\tUpdates made:  " + entry.getUpdates());
	 *     entry.getUpdates().forEach((k,v) -&gt; {
	 *       System.out.println("\t\t" + k + ":  " + v);
	 *     });
	 *   });
	 * }</pre>
	 * @see <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>
	 */
	public DocumentRoleChangeBulkResponse removeUsersAndGroupsFromRolesOnMultipleDocuments() {
		return assignOrRemoveUsersAndGroupsToRolesOnMultipleDocuments(HttpMethod.DELETE);
	}

	/**
	 * <b>Remove Users and Groups from Roles on a Single Document</b>
	 * <p>
	 * <b>Use bulk API for multiple documents</b>
	 *
	 * @param documentRequestType Documents or Binders
	 * @param docId               The Document or Binder id
	 * @param roleName            Name of the Role to modify
	 * @param memberType          User or Group
	 * @param id                  User or Group id to remove
	 * @return DocumentRoleChangeResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/roles/{role_name}.{user_or_group}/{id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#remove-users-amp-groups-from-roles-on-a-single-document' target='_blank'>https://developer.veevavault.com/api/24.3/#remove-users-amp-groups-from-roles-on-a-single-document</a>
	 * @vapil.request <pre>
	 * DocumentRoleChangeResponse response = request
	 * 			.removeUsersAndGroupsFromRolesOnASingleDocument(docId, "viewer__v", MemberType.GROUP, id);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Updated Roles:");
	 *   response.getUpdatedRoles().forEach((k,v) -&gt; {
	 *     System.out.println("\tRole:  " + k);
	 *     v.forEach((v1, k1) -&gt; {
	 *       System.out.println("\t\t" + v1 + ":  " + k1);
	 *
	 *       });
	 *     });
	 * }</pre>
	 */
	public DocumentRoleChangeResponse removeUsersAndGroupsFromRolesOnASingleDocument(DocumentRequestType documentRequestType, int docId, String roleName, MemberType memberType, long id) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_ROLE_REMOVE_SINGLE);
		url = url.replace("{documents_or_binders}", documentRequestType.getValue());
		url = url.replace("{doc_id}", Integer.toString(docId));
		url = url.replace("{role_name}", roleName);
		url = url.replace("{user_or_group}", memberType.getValue());
		url = url.replace("{id}", Long.toString(id));
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.DELETE, request, DocumentRoleChangeResponse.class);
	}

	/**
	 * Private method for add or remove requests to the bulk Document Role endpoint
	 *
	 * @param method The http method for the request
	 * @return DocumentRoleUpdateBulkResponse
	 */
	private DocumentRoleChangeBulkResponse assignOrRemoveUsersAndGroupsToRolesOnMultipleDocuments(HttpMethod method) {
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

		return send(method, request, DocumentRoleChangeBulkResponse.class);
	}

	/**
	 * Enum for User or Group request on single document remove endpoint
	 *
	 * @see #removeUsersAndGroupsFromRolesOnASingleDocument(DocumentRequestType documentRequestType, int, String, MemberType, long)
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
	public DocumentRoleRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public DocumentRoleRequest setContentTypeXForm() {
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
	public DocumentRoleRequest setBinaryFile(String filename, byte[] binaryContent) {
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
	public DocumentRoleRequest setBodyParams(Map<String, Object> bodyParams) {
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
	public DocumentRoleRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentRoleRequest setRequestString(String requestString) {
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
