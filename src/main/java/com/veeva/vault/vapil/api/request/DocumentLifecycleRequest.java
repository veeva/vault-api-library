/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import java.util.Map;

import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.DocumentActionEntryCriteriaResponse;
import com.veeva.vault.vapil.api.model.response.DocumentActionInitiateResponse;
import com.veeva.vault.vapil.api.model.response.DocumentActionResponse;
import com.veeva.vault.vapil.api.model.response.DocumentLifecycleDeleteOverrideRulesResponse;
import com.veeva.vault.vapil.api.model.response.DocumentLifecycleRoleAssignmentResponse;
import com.veeva.vault.vapil.api.model.response.DocumentLifecycleRoleAssignmentRulesResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Lifecycle requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-lifecycle">https://developer.veevavault.com/api/23.1/#document-lifecycle</a>
 */
public class DocumentLifecycleRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_ACTION = "/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions";
	private static final String URL_ACTION_INITIATE = "/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}";
	private static final String URL_ACTION_MULTIPLE_DOCS_BINDERS = "/objects/{documents_or_binders}/lifecycle_actions";
	private static final String URL_ACTION_RETRIEVE_ENTRY_CRITERIA = "/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}/entry_requirements";
	private static final String URL_ACTION_DOWNLOAD_CONTROLLED_COPY_JOB_RESULT = "/objects/documents/actions/{lifecycle.state.action}/{job_id}/results";
	private static final String URL_ACTION_BULK_DOCUMENT_STATE_CHANGE = "/objects/{documents_or_binders}/lifecycle_actions/{user_action_name}";
	private static final String URL_CREATE_OVERRIDE_RULES = "/configuration/role_assignment_rule";
	private static final String URL_UPDATE_OVERRIDE_RULES = "/configuration/role_assignment_rule";
	private static final String URL_DELETE_OVERRIDE_RULES = "/configuration/role_assignment_rule";
	private static final String URL_LIFECYCLE_ROLE_ASSIGNMENT_RULES = "/configuration/role_assignment_rule";

	// API Request Param
	private static final String PARAM_DOCIDS = "docIds";
	private static final String PARAM_MAJORVERSION = "major_version";
	private static final String PARAM_MINORVERSION = "minor_version";
	private static final String PARAM_LIFECYCLE_NAME = "lifecycle";
	private static final String PARAM_LIFECYCLE_STATE = "state";
	private static final String PARAM_LIFECYCLE__V = "lifecycle__v";
	private static final String PARAM_ROLE__V = "role__v";

	private String documentState;
	private String lifecycleName;
	private String inputPath;
	private HttpRequestConnector.BinaryFile binaryFile;
	private String headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	private String requestString; // For raw request
	private String role;
	private String productName;
	private String countryName;
	private String countryID;

	// API Request Parameters
	private Map<String, Object> bodyParams = null;
	private Map<String, Object> queryParams = null;

	private DocumentLifecycleRequest() {
	}

	/**
	 * <b>Retrieve User Actions</b>
	 *
	 * @param requestType        documents or binders
	 * @param id                 document or binder id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber major version number
	 * @return DocumentActionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-user-actions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-user-actions</a>
	 * @vapil.request <pre>
	 * DocumentActionResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class).retrieveUserActions(
	 * 				DocumentRequestType.DOCUMENTS, docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   for (DocumentActionResponse.LifecycleAction action : resp.getLifecycleActions()) {
	 *     System.out.println("Label = " + action.getLabel());
	 *     System.out.println("Name = " + action.getName());
	 *     }
	 *   }
	 * </pre>
	 */
	public DocumentActionResponse retrieveUserActions(DocumentRequestType requestType, int id, int majorVersionNumber, int minorVersionNumber) {
		String url = vaultClient.getAPIEndpoint(URL_ACTION).replace("{documents_or_binders}", requestType.getValue())
				.replace("{id}", Integer.valueOf(id).toString())
				.replace("{major_version}", Integer.valueOf(majorVersionNumber).toString())
				.replace("{minor_version}", Integer.valueOf(minorVersionNumber).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.GET, request, DocumentActionResponse.class);
	}

	/**
	 * <b>Initiate User Action</b>
	 *
	 * @param requestType        documents or binders
	 * @param id                 document or binder id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber major version number
	 * @param userActionName     api name of the useraction
	 * @return DocumentActionInitiateResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-user-action' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-user-action</a>
	 * @vapil.request <pre>
	 * DocumentActionInitiateResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class).initiateUserAction(
	 * 				DocumentRequestType.DOCUMENTS, docId, majorVersion, minorVersion, useraction);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("id = " + resp.getId());
	 *   }
	 * </pre>
	 */
	public DocumentActionInitiateResponse initiateUserAction(DocumentRequestType requestType, int id, int majorVersionNumber, int minorVersionNumber, String userActionName) {
		String url = vaultClient.getAPIEndpoint(URL_ACTION_INITIATE).replace("{documents_or_binders}", requestType.getValue())
				.replace("{id}", Integer.valueOf(id).toString())
				.replace("{major_version}", Integer.valueOf(majorVersionNumber).toString())
				.replace("{minor_version}", Integer.valueOf(minorVersionNumber).toString())
				.replace("{name__v}", userActionName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.PUT, request, DocumentActionInitiateResponse.class);
	}

	/**
	 * <b>Retrieve user action multiple documents or binders</b>
	 *
	 * @param requestType  documents or binders
	 * @param docIds       Include a comma-separated list of document or binder IDs,
	 *                     major and minor version numbers. The document or binder
	 *                     docIds field values from which to retrieve the available
	 *                     user actions. e.g. "docIds=22:0:1,21:1:0,20:1:0
	 * @param majorVersion The major version number of the document or binder.
	 * @param minorVersion The minor version number of the document or binder.
	 * @return DocumentActionResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/{documents_or_binders}/lifecycle_actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-user-actions-on-multiple-documents-or-binders' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-user-actions-on-multiple-documents-or-binders</a>
	 * @vapil.request <pre>
	 * DocumentActionResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class)
	 * 				.retrieveUserActionsOnMultipleDocumentsBinders(DocumentRequestType.DOCUMENTS,
	 * 						docIds, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   List&lt;LifecycleAction&gt; list = resp.getLifecycleActions();
	 *
	 *   for (LifecycleAction lcAction : list) {
	 *     System.out.println("Get Name:" + lcAction.getName());
	 *     System.out.println("Get Label:" + lcAction.getLabel());
	 *     System.out.println("Get Lifecycle:" + lcAction.getLifecycle());
	 *     System.out.println("Get Lifecycle:" + lcAction.getLifecycleActionType());
	 *     System.out.println("Get Lifecycle:" + lcAction.getState());
	 *     System.out.println("Get Lifecycle:" + lcAction.getEntryRequirements());
	 *     }
	 *   }
	 * }
	 * </pre>
	 */
	public DocumentActionResponse retrieveUserActionsOnMultipleDocumentsBinders(DocumentRequestType requestType, String docIds, String majorVersion, String minorVersion) {

		String url = vaultClient.getAPIEndpoint(URL_ACTION_MULTIPLE_DOCS_BINDERS).replace("{documents_or_binders}",
				requestType.getValue());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (docIds != null && docIds != "") {
			request.addBodyParam(PARAM_DOCIDS, docIds);
		}
		if (majorVersion != null) {
			request.addBodyParam(PARAM_MAJORVERSION, majorVersion);
		}
		if (minorVersion != null) {
			request.addBodyParam(PARAM_MINORVERSION, minorVersion);
		}

		return send(HttpMethod.POST, request, DocumentActionResponse.class);
	}

	/**
	 * <b>Retrieve the entry criteria for a user action.</b>
	 *
	 * @param requestType   Choose to retrieve values for documents or binders.
	 * @param id            The document or binder id field value from which to
	 *                      retrieve available user actions.
	 * @param majorVersion  The major version number of the document or binder.
	 * @param minorVersion  The minor version number of the document or binder.
	 * @param lifecycleName The lifecycle name__v field value from which to retrieve
	 *                      entry criteria. This is retrieved from the Retrieve User
	 *                      Actions request above.
	 * @return DocumentActionEntryCriteriaResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}/entry_requirements</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-entry-criteria' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-entry-criteria</a>
	 * @vapil.request <pre>
	 * DocumentActionEntryCriteriaResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class)
	 * 				.retrieveEntryCriteria(DocumentRequestType.DOCUMENTS, id, majorVersion,
	 * 						minorVersion, lifecycleName);</pre>
	 * @vapil.response <pre>System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   List&lt;Property&gt; properties = resp.getProperties();
	 *
	 *   for (Property property : properties) {
	 *     System.out.println("Get Name:" + property.getName());
	 *     System.out.println("Get Label:" + property.getDescription());
	 *     System.out.println("Get Lifecycle:" + property.getScope());
	 *     }
	 *   }
	 * }
	 * </pre>
	 */
	public DocumentActionEntryCriteriaResponse retrieveEntryCriteria(DocumentRequestType requestType, int id, int majorVersion, int minorVersion, String lifecycleName) {

		String url = vaultClient.getAPIEndpoint(URL_ACTION_RETRIEVE_ENTRY_CRITERIA)
				.replace("{documents_or_binders}", requestType.getValue())
				.replace("{id}", Integer.valueOf(id).toString())
				.replace("{major_version}", Integer.valueOf(majorVersion).toString())
				.replace("{minor_version}", Integer.valueOf(minorVersion).toString())
				.replace("{name__v}", lifecycleName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		return send(HttpMethod.GET, request, DocumentActionEntryCriteriaResponse.class);
	}

	/**
	 * <b>Download Controlled Copy Job Results.</b>
	 *
	 * @param lifecycleStateAction The name__v values for the lifecycle, state, and
	 *                             action. We recommend you Retrieve the Job Status
	 *                             and use the href under "rel": "artifacts" to
	 *                             obtain this value.
	 * @param jobId                The ID of the job, returned from the original job
	 *                             request. For controlled copy, you can find this
	 *                             ID in the Initiate User Action response.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/actions/{lifecycle.state.action}/{job_id}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-controlled-copy-job-results' target='_blank'>https://developer.veevavault.com/api/23.1/#download-controlled-copy-job-results</a>
	 */
	public VaultResponse downloadControlledCopyJobResult(String lifecycleStateAction, int jobId) {

		String url = vaultClient.getAPIEndpoint(URL_ACTION_DOWNLOAD_CONTROLLED_COPY_JOB_RESULT)
				.replace("{lifecycle.state.action}", lifecycleStateAction).replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
	}

	/**
	 * <b>Bulk Document State Change. </b>
	 *
	 * @param requestType    documents or binders
	 * @param docIds         Include a comma-separated list of document or binder
	 *                       IDs, major and minor version numbers. The document or
	 *                       binder docIds field values from which to retrieve the
	 *                       available user actions. e.g.
	 *                       "docIds=22:0:1,21:1:0,20:1:0
	 * @param userActionName The user action name__v field value. Find this value
	 *                       with the Retrieve User Actions on Multiple Documents or
	 *                       Binders endpoint.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/{documents_or_binders}/lifecycle_actions/{user_action_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#bulk-document-state-change' target='_blank'>https://developer.veevavault.com/api/23.1/#bulk-document-state-change</a>
	 */
	public VaultResponse initiateBulkUserActions(DocumentRequestType requestType,
												 String userActionName,
												 String docIds) {

		String url = vaultClient.getAPIEndpoint(URL_ACTION_BULK_DOCUMENT_STATE_CHANGE)
				.replace("{documents_or_binders}", requestType.getValue())
				.replace("{user_action_name}", userActionName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (docIds != null && docIds != "") {
			request.addBodyParam(PARAM_DOCIDS, docIds);
		}
		if (lifecycleName != null && lifecycleName != null) {
			request.addBodyParam(PARAM_LIFECYCLE_NAME, lifecycleName);
		}
		if (documentState != null && documentState != null) {
			request.addBodyParam(PARAM_LIFECYCLE_STATE, documentState);
		}

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * <b>Retrieve Lifecycle Role Assignment Rules (Default &amp; Override)</b>
	 *
	 * @return DocumentLifecycleRoleAssignmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/configuration/role_assignment_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-lifecycle-role-assignment-rules-default-amp-override' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-lifecycle-role-assignment-rules-default-amp-override</a>
	 * @vapil.request <pre>
	 * DocumentLifecycleRoleAssignmentResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class)
	 * 				.setLifecycleName("general_lifecycle1__c").setRoleName("all_users__v")
	 * 				.retrieveLifecycleRoleAssignmentRules();</pre>
	 * @vapil.response <pre>System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   List&lt;RoleAssignmentResponse&gt; roleAssigmentRes = resp.getRoleAssignmentResponse();
	 *
	 *   for (RoleAssignmentResponse roleAssigment : roleAssigmentRes) {
	 *     System.out.println("Get Lifecycle Name:" + roleAssigment.getLifecycle());
	 *     System.out.println("Get Role Name:" + roleAssigment.getRole());
	 *     List&lt;String&gt; allowedGroups = roleAssigment.getAllowedGroups();
	 *     for (String groups : allowedGroups) {
	 *       System.out.println("Get allowed Groups:" + groups);
	 *     }
	 *   }
	 * }</pre>
	 * @vapil.request <pre>
	 * DocumentLifecycleRoleAssignmentResponse resp = vaultClient.newRequest(DocumentLifecycleRequest.class)
	 * 				.setQueryParams(hm)
	 * 				.retrieveLifecycleRoleAssignmentRules(withOverride);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   List&lt;RoleAssignmentResponse&gt; roleAssigmentRes = resp.getRoleAssignmentResponse();
	 *
	 *   for (RoleAssignmentResponse roleAssigment : roleAssigmentRes) {
	 *     System.out.println("Get Lifecycle Name:" + roleAssigment.getLifecycle());
	 *     System.out.println("Get Role Name:" + roleAssigment.getRole());
	 *     List&lt;String&gt; allowedGroups = roleAssigment.getAllowedGroups();
	 *     for (String groups : allowedGroups) {
	 *       System.out.println("Get allowed Groups:" + groups);
	 *       }
	 *   }
	 * }</pre>
	 */
	public DocumentLifecycleRoleAssignmentResponse retrieveLifecycleRoleAssignmentRules() {
		String url = vaultClient.getAPIEndpoint(URL_LIFECYCLE_ROLE_ASSIGNMENT_RULES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (lifecycleName != null && !lifecycleName.equals("")) {
			request.addQueryParam(PARAM_LIFECYCLE__V, lifecycleName);
		}

		if (role != null && !role.equals("")) {
			request.addQueryParam(PARAM_ROLE__V, role);
		}

		if (productName != null && !productName.equals("")) {
			request.addQueryParam("PARAM_PRODUCT__V", productName);
		}

		if (countryID != null && !countryID.equals("")) {
			request.addQueryParam("PARAM_COUNTRY__V", countryName);
		} else if (countryName != null && !countryName.equals("")) {
			request.addQueryParam("PARAM_COUNTRY__V_NAME__V", countryName);
		}

		return send(HttpMethod.GET, request, DocumentLifecycleRoleAssignmentResponse.class);
	}

	/**
	 * <b>Create Override Rules</b>
	 *
	 * @return LifecycleRoleAssignmentRulesResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/configuration/role_assignment_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-override-rules' target='_blank'>https://developer.veevavault.com/api/23.1/#create-override-rules</a>
	 * @vapil.request <pre>
	 * .newRequest(DocumentLifecycleRequest.class).setInputPath(csvFilePath).createOverrideRules();</pre>
	 * @vapil.response <pre>System.out.println(bulkPathResponse.getResponse());
	 *
	 * if (bulkPathResponse.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   }
	 *
	 * DocumentLifecycleRoleAssignmentRulesResponse bulkByteResponse = vaultClient
	 * .newRequest(DocumentLifecycleRequest.class)
	 * .setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath())).createOverrideRules();
	 * System.out.println(bulkByteResponse.getResponse());
	 *
	 * if (bulkByteResponse.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   }
	 *
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }</pre>
	 */
	public DocumentLifecycleRoleAssignmentRulesResponse createOverrideRules() {
		String url = vaultClient.getAPIEndpoint(URL_CREATE_OVERRIDE_RULES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(contentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(contentType, requestString);

		if (binaryFile != null)
			request.addBinary(contentType, binaryFile.getBinaryContent());

		return send(HttpMethod.POST, request, DocumentLifecycleRoleAssignmentRulesResponse.class);

	}

	/**
	 * <b>Update Override Rules</b>
	 *
	 * @return LifecycleRoleAssignmentRulesResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/configuration/role_assignment_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-override-rules' target='_blank'>https://developer.veevavault.com/api/23.1/#update-override-rules</a>
	 * @vapil.request <pre>
	 * .newRequest(DocumentLifecycleRequest.class).setInputPath(csvFilePath).updateOverrideRules();</pre>
	 * @vapil.response <pre>System.out.println(updateOverrideRulesResponse.getResponse());
	 *
	 * if (updateOverrideRulesResponse.isSuccessful()) {
	 *   System.out.println("Successful ");
	 *   }
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }</pre>
	 */
	public DocumentLifecycleRoleAssignmentRulesResponse updateOverrideRules() {
		String url = vaultClient.getAPIEndpoint(URL_UPDATE_OVERRIDE_RULES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(contentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(contentType, requestString);

		if (binaryFile != null)
			request.addBinary(contentType, binaryFile.getBinaryContent());

		return send(HttpMethod.PUT, request, DocumentLifecycleRoleAssignmentRulesResponse.class);

	}

	/**
	 * <b>Delete Override Rules</b>
	 *
	 * @param lifecycleName Include the name of the lifecycle from which to delete
	 *                      override rules. For example:
	 *                      lifecycle__v=general_lifecycle__c
	 * @param role          Include the name of the role from which to delete
	 *                      override rules. For example: role__v=editor__c
	 * @return DocumentLifecycleDeleteOverrideRulesResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/configuration/role_assignment_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-override-rules' target='_blank'>https://developer.veevavault.com/api/23.1/#update-override-rules</a>
	 * @vapil.request <pre>
	 * .newRequest(DocumentLifecycleRequest.class)
	 * 					.deleteOverrideRules(lifecycle, role);</pre>
	 * @vapil.response <pre>System.out.println(deleteOverrideRulesResponse.getResponse());
	 * if (deleteOverrideRulesResponse != null) {
	 *   if (deleteOverrideRulesResponse.getData() != null) {
	 *     System.out.println("Rules Deleted:" + deleteOverrideRulesResponse.getData().getRulesDeleted());
	 *     }else {
	 *     System.out.println("INVALID_DATA");
	 *     }
	 *   }
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * .newRequest(DocumentLifecycleRequest.class)
	 * 					.setQueryParams(hm).deleteOverrideRules();</pre>
	 * @vapil.response <pre>System.out.println(deleteOverrideRulesResponse.getResponse());
	 * if (deleteOverrideRulesResponse != null) {
	 *   if (deleteOverrideRulesResponse.getData() != null) {
	 *     System.out.println("Rules Deleted:" + deleteOverrideRulesResponse.getData().getRulesDeleted());
	 *     }else {
	 *     System.out.println("INVALID_DATA");
	 *     }
	 *   }
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }</pre>
	 */
	public DocumentLifecycleDeleteOverrideRulesResponse deleteOverrideRules(String lifecycleName, String role) {
		String url = vaultClient.getAPIEndpoint(URL_DELETE_OVERRIDE_RULES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (lifecycleName != null && !lifecycleName.equals("")) {
			request.addQueryParam("lifecycle__v", lifecycleName);
		}
		if (role != null && !role.equals("")) {
			request.addQueryParam("role__v", role);
		}

		return send(HttpMethod.DELETE, request, DocumentLifecycleDeleteOverrideRulesResponse.class);

	}

	/**
	 * <b>Delete Override Rules</b>
	 *
	 * @return DocumentLifecycleDeleteOverrideRulesResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/configuration/role_assignment_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-override-rules' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-override-rules</a>
	 */
	public DocumentLifecycleDeleteOverrideRulesResponse deleteOverrideRules() {
		String url = vaultClient.getAPIEndpoint(URL_DELETE_OVERRIDE_RULES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		if (queryParams != null && !queryParams.isEmpty()) {
			request.setQueryParams(queryParams);
		}

		return send(HttpMethod.DELETE, request, DocumentLifecycleDeleteOverrideRulesResponse.class);

	}

	/*
	 *
	 * Request constants
	 *
	 */

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Single record form ObjectRecordRequest with name/value body parameters
	 *
	 * @param bodyParams map of body params
	 * @return The Request
	 */
	public DocumentLifecycleRequest setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
		return this;
	}

	/**
	 * Single record form ObjectRecordRequest with name/value body parameters
	 *
	 * @param queryParams map of query params
	 * @return The Request
	 */
	public DocumentLifecycleRequest setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public DocumentLifecycleRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public DocumentLifecycleRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentLifecycleRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public DocumentLifecycleRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set document state name to the request
	 *
	 * @param documentState name of document state
	 * @return The Request
	 */
	public DocumentLifecycleRequest setDocumentState(String documentState) {
		this.documentState = documentState;
		return this;
	}

	/**
	 * Set lifecycle name to the request
	 *
	 * @param lifecycleName name of lifecycle
	 * @return The Request
	 */
	public DocumentLifecycleRequest setLifecycleName(String lifecycleName) {
		this.lifecycleName = lifecycleName;
		return this;
	}

	/**
	 * Set role name (role__v) to the request
	 *
	 * @param roleName name of role
	 * @return The Request
	 */
	public DocumentLifecycleRequest setRoleName(String roleName) {
		this.role = roleName;
		return this;
	}

	/**
	 * Set product name to the request
	 *
	 * @param productName name of product
	 * @return The Request
	 */
	public DocumentLifecycleRequest setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	/**
	 * Set country name to the request
	 *
	 * @param countryName name of country
	 * @return The Request
	 */
	public DocumentLifecycleRequest setCountryName(String countryName) {
		this.countryName = countryName;
		return this;
	}

	/**
	 * Set country id to the request
	 *
	 * @param countryID id of country
	 * @return The Request
	 */
	public DocumentLifecycleRequest setCountryID(String countryID) {
		this.countryID = countryID;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentLifecycleRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}
}
