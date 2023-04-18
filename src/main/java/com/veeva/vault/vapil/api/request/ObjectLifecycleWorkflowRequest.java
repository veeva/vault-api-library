/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.ObjectRecordActionBulkResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordActionResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Object Lifecycle and Workflow requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#object-lifecycle-amp-workflows">https://developer.veevavault.com/api/23.1/#object-lifecycle-amp-workflows</a>
 */
public class ObjectLifecycleWorkflowRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_OBJ_REC_ACTIONS = "/vobjects/{object_name}/{object_record_id}/actions";
	private static final String URL_OBJ_REC_ACTIONS_ACTIONNAME = "/vobjects/{object_name}/{object_record_id}/actions/{action_name}";
	private static final String URL_OBJ_ACTIONS = "/vobjects/{object_name}/actions/{action_name}";
	private static final String URL_OBJ_ACTIONS_WORKFLOWS = "/objects/objectworkflows";
	private static final String URL_OBJ_ACTIONS_WORKFLOW = "/objects/objectworkflows/{workflow_id}";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_ACTIONS = "/objects/objectworkflows/{workflow_id}/actions";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_ACTION = "/objects/objectworkflows/{workflow_id}/actions/{workflow_action}";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_TASKS = "/objects/objectworkflows/tasks";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_TASK = "/objects/objectworkflows/tasks/{task_id}";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_TASK_ACTIONS = "/objects/objectworkflows/tasks/{task_id}/actions";
	private static final String URL_OBJ_ACTIONS_WORKFLOW_TASK_ACTION = "/objects/objectworkflows/tasks/{task_id}/actions/{task_action}";
	private static final String URL_OBJ_ACTIONS_MULTI_RECORD_WORKFLOWS = "/objects/objectworkflows/actions";
	private static final String URL_OBJ_ACTIONS_MULTI_RECORD_WORKFLOW = "/objects/objectworkflows/actions/{workflow_name}";
	private static final String PARAM_OBJECT__V = "object__v";
	private static final String PARAM_RECORD_ID__V = "record_id__v";
	private static final String PARAM_PARTICIPANT = "participant";

	private List<Integer> documentIdList;
	private boolean loc;
	private int offset;
	private int pageSize;
	private String status;

	// API Request Parameters
	private Map<String, Object> bodyParams = null;

	private ObjectLifecycleWorkflowRequest() {
	}


	/*
	 *
	 * Object Record User Actions
	 *
	 */

	/**
	 * <b>Retrieve Object Record User Actions</b>
	 * <p>
	 * Retrieve all available user actions that can be initiated on a specific
	 * object record. Returned data can be retrieved via the "getActions" method.
	 *
	 * @param objectName The object name The object name
	 * @param recordId   The object record id
	 * @return ObjectRecordActionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-record-user-actions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-record-user-actions</a>
	 * @vapil.request <pre>
	 * ObjectRecordActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.retrieveObjectRecordUserActions("tt_claim__c", "OOW000000000501");</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   System.out.println("Actions Exist\n");
	 *
	 *   for (Action action : resp.getData()) {
	 *
	 *     System.out.println("------------------------------");
	 *     System.out.println("Name " + action.getName());
	 *     System.out.println("Label " + action.getLabel());
	 *     System.out.println("Type " + action.getType());
	 *
	 *     for (Link link : action.getLinks()) {
	 *       System.out.println("Rel " + link.getRel());
	 *       System.out.println("Accept " + link.getAccept());
	 *       System.out.println("Href " + link.getHref());
	 *       System.out.println("Method " + link.getMethod());
	 *       System.out.println("");
	 *     }
	 *   }
	 * } else
	 *   System.out.println("No Actions Exist\n");
	 * }</pre>
	 */
	public ObjectRecordActionResponse retrieveObjectRecordUserActions(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ACTIONS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordActionResponse.class);
	}

	/**
	 * <b>Retrieve Object User Action Details</b>
	 * <p>
	 * Retrieve the details for a specific user action.
	 * <p>
	 * Returned data can be retrieved via the "getActions" method. Note that the an
	 * array is returned instead of a single entity for a consistent response class.
	 * Responses with data will have an array of size one.
	 *
	 * @param objectName The object name
	 * @param recordId   The object record id
	 * @param actionName Obtained from the Retrieve User Actions request
	 *                   {@link #retrieveObjectRecordUserActions}
	 * @return ObjectRecordActionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/actions/{action_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-user-action-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-user-action-details</a>
	 * @vapil.request <pre>
	 * ObjectRecordActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.retrieveObjectRecordUserActionDetails(objectName, recordId, actionName);</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   System.out.println("Actions Exist\n");
	 *   for (Action action : resp.getData()) {
	 *
	 *     System.out.println("------------------------------");
	 *     System.out.println("Name " + action.getName());
	 *     System.out.println("Label " + action.getLabel());
	 *     System.out.println("Type " + action.getType());
	 *
	 *     for (Link link : action.getLinks()) {
	 *       System.out.println("Rel " + link.getRel());
	 *       System.out.println("Accept " + link.getAccept());
	 *       System.out.println("Href " + link.getHref());
	 *       System.out.println("Method " + link.getMethod());
	 *       System.out.println("");
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectRecordActionResponse retrieveObjectRecordUserActionDetails(String objectName, String recordId, String actionName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ACTIONS_ACTIONNAME);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{action_name}", actionName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Single value is returned by the API so use custom object mapper for re-using
		// the response (which is an array)
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, ObjectRecordActionResponse.class);
	}

	/**
	 * <b>Initiate Object Action on a Single Record</b>
	 *
	 * @param objectName The object name
	 * @param recordId   The object record id
	 * @param actionName Obtained from the Retrieve User Actions request
	 *                   {@link #retrieveObjectRecordUserActions}
	 * @return ObjectRecordActionResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/{object_record_id}/actions/{action_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-object-action-on-a-single-record' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-object-action-on-a-single-record</a>
	 * @vapil.request <pre>
	 * ObjectRecordActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.initiateObjectActionOnASingleRecord(objectName, recordId, actionName);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());</pre>
	 */
	public ObjectRecordActionResponse initiateObjectActionOnASingleRecord(String objectName, String recordId, String actionName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ACTIONS_ACTIONNAME);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{action_name}", actionName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.POST, request, ObjectRecordActionResponse.class);
	}

	/**
	 * <b>Initiate Object Action on Multiple Records</b>
	 * <p>
	 * Initiate an object user action on multiple records. Maximum 500 records per
	 * batch.
	 *
	 * @param objectName The object name
	 * @param recordIds  Set of record ids to perform the action upon. Up to 500
	 *                   entries per call
	 * @param actionName Obtained from the Retrieve User Actions request
	 *                   {@link #retrieveObjectRecordUserActions}
	 * @return ObjectRecordActionBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/actions/{action_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-object-action-on-multiple-records' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-object-action-on-multiple-records</a>
	 * @vapil.request <pre>
	 * ObjectRecordActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.initiateObjectActionOnMultipleRecords(objectName, ids, actionName);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());</pre>
	 * @vapil.request <pre>
	 * ObjectRecordActionBulkResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.initiateObjectActionOnMultipleRecords(objectName, ids, actionName);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public ObjectRecordActionBulkResponse initiateObjectActionOnMultipleRecords(String objectName, Set<String> recordIds, String actionName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{action_name}", actionName);

		String ids = String.join(",", recordIds);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("ids", ids);

		return send(HttpMethod.POST, request, ObjectRecordActionBulkResponse.class);
	}

	/*
	 *
	 * Workflows
	 *
	 */

	/**
	 * <b>Retrieve Workflows</b>
	 * <p>
	 * Retrieve all workflows for a specific object and object record or from a
	 * specific workflow participant.
	 *
	 * @param object      To retrieve all workflows configured on an object.
	 * @param recordId    Object record id field values e.g
	 *                    object__v={name__v}&amp;record_id__v={id}.
	 *                    object__v=product__v&amp;record_id__v=00P07551
	 * @param participant To retrieve all workflows available to a particular user,
	 *                    include the user id field value as ?participant={id}. To
	 *                    retrieve your own workflows, set this value to
	 *                    participant=me. This parameter is required when the
	 *                    object__v and record_id__v parameters are not used.
	 * @return ObjectWorkflowResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflows</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setPageSize(0).setOffset(0)
	 * 				.retrieveWorkflows(object, recordId, participant);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 *
	 * Object.ObjectReference objInfo = resp.getResponseDetails().getObject();
	 *
	 * System.out.println("-----------Response Details -------------------");
	 * System.out.println("Total: " + resp.getResponseDetails().getTotal());
	 * System.out.println("Offset: " + resp.getResponseDetails().getOffset());
	 * System.out.println("PageSize: " + resp.getResponseDetails().getPageSize());
	 * resp.getResponseDetails().getOffset();
	 * System.out.println("-----------Object Info -------------------");
	 * System.out.println("Name: " + objInfo.getName());
	 * System.out.println("Label: " + objInfo.getLabel());
	 * System.out.println("URL: " + objInfo.getUrl());
	 * System.out.println("Label Plural: " + objInfo.getLabelPlural());
	 *
	 * List&lt;ObjectWorkflow&gt; objectWorkflowDetail = resp.getData();
	 * for (ObjectWorkflow workflowDetail : objectWorkflowDetail) {
	 *   System.out.println("-----------Data -------------------");
	 *   System.out.println("Get ID: " + workflowDetail.getId());
	 *   System.out.println("Get Label: " + workflowDetail.getLabel());
	 *   System.out.println("Get StartDate: " + workflowDetail.getStartedDate());
	 *   System.out.println("Get Object: " + workflowDetail.getObject());
	 *   System.out.println("Get RecordID: " + workflowDetail.getRecordId());
	 *   System.out.println("Get Initiator: " + workflowDetail.getInitiator());
	 *   System.out.println("Get Due Date: " + workflowDetail.getDueDate());
	 * }</pre>
	 */
	public ObjectWorkflowResponse retrieveWorkflows(String object, String recordId, String participant) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOWS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (object != null && !object.equals("")) {
			request.addQueryParam(PARAM_OBJECT__V, object);
		}
		if (recordId != null && !recordId.equals("")) {
			request.addQueryParam(PARAM_RECORD_ID__V, recordId);
		}
		if (participant != null && !participant.equals("")) {
			request.addQueryParam(PARAM_PARTICIPANT, participant);
		}
		if (status != null && !status.equals("")) {
			request.addQueryParam(status, status);
		}
		if (offset > 0) {
			request.addQueryParam("offset", offset);
		}
		if (pageSize > 0) {
			request.addQueryParam("page_size", pageSize);
		}
		request.addQueryParam("loc", loc);
		return send(HttpMethod.GET, request, ObjectWorkflowResponse.class);
	}

	/**
	 * <b>Retrieve Workflows (By Page)</b>
	 * <p>
	 * Retrieve all workflows using the previous_page or next_page parameter of a previous request
	 *
	 * @param pageUrl  The URL from the previous_page or next_page parameter
	 * @return ObjectWorkflowResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflows</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowResponse paginatedResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 		.retrieveWorkflowsByPage(response.getResponseDetails().getNextPage());</pre>
	 * @vapil.response <pre>System.out.println(paginatedResponse.getResponseStatus());</pre>
	 */
	public ObjectWorkflowResponse retrieveWorkflowsByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectWorkflowResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Details</b> <br>
	 * Retrieve the details for a specific object workflow.
	 *
	 * @param workflowId The workflow id.
	 * @return ObjectWorkflowResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/{workflow_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-details</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setLoc(true)
	 * 				.retrieveWorkflowDetails(workflowId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 *
	 * Object.ObjectReference objInfo = resp.getResponseDetails().getObject();
	 *
	 * System.out.println("-----------Response Details -------------------");
	 * System.out.println("URL: " + resp.getResponseDetails().getUrl());
	 * resp.getResponseDetails().getOffset();
	 * System.out.println("-----------Object Info -------------------");
	 * System.out.println("Name: " + objInfo.getName());
	 * System.out.println("Label: " + objInfo.getLabel());
	 * System.out.println("URL: " + objInfo.getUrl());
	 * System.out.println("Label Plural: " + objInfo.getLabelPlural());
	 *
	 * List&lt;ObjectWorkflow&gt; objectWorkflowDetail = resp.getData();
	 * for (ObjectWorkflow workflowDetail : objectWorkflowDetail) {
	 *   System.out.println("-----------Data -------------------");
	 *   System.out.println("Get ID: " + workflowDetail.getId());
	 *   System.out.println("Get Label: " + workflowDetail.getLabel());
	 *   System.out.println("Get StartDate: " + workflowDetail.getStartedDate());
	 *   System.out.println("Get Object: " + workflowDetail.getObject());
	 *   System.out.println("Get RecordID: " + workflowDetail.getRecordId());
	 *   System.out.println("Get Initiator: " + workflowDetail.getInitiator());
	 *   System.out.println("Get Due Date: " + workflowDetail.getDueDate());
	 * }</pre>
	 */
	public ObjectWorkflowResponse retrieveWorkflowDetails(int workflowId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW);
		url = url.replace("{workflow_id}", String.valueOf(workflowId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		request.addQueryParam("loc", loc);

		return send(HttpMethod.GET, request, ObjectWorkflowResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Action </b> <br>
	 * Retrieve the details for a specific object workflow.
	 *
	 * @param workflowId The workflow id.
	 * @return ObjectWorkflowActionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-actions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-actions</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setLoc(false)
	 * 				.retrieveWorkflowActions(workflowId);</pre>
	 * @vapil.response <pre>System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 *
	 * List&lt;WorkflowAction&gt; objectWorkflowDetail = resp.getData();
	 * if (resp.getData() != null) {
	 *   for (WorkflowAction workflowDetail : objectWorkflowDetail) {
	 *     System.out.println("Name: " + workflowDetail.getName());
	 *     System.out.println("Label: " + workflowDetail.getLabel());
	 *   }
	 * }</pre>
	 */
	public ObjectWorkflowActionResponse retrieveWorkflowActions(int workflowId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_ACTIONS);
		url = url.replace("{workflow_id}", String.valueOf(workflowId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		request.addQueryParam("loc", loc);

		return send(HttpMethod.GET, request, ObjectWorkflowActionResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Action Details </b> <br>
	 * Retrieve details about a workflow action. For example, the prompts needed to
	 * complete a workflow action.
	 *
	 * @param workflowId     The workflow id.
	 * @param workflowAction The name of the workflow action. To get this value,
	 *                       <b>You can Retrieve Workflow Actions.</b>
	 * @return ObjectWorkflowActionDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-action-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-action-details</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowActionDetailsResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setLoc(false)
	 * 				.retrieveWorkflowActionDetails(workflowId, workflowName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 *
	 * List&lt;Control&gt; controls = resp.getData().getControls();
	 * if (controls != null) {
	 *   for (Control control : controls) {
	 *     System.out.println("Type: " + control.getType());
	 *     System.out.println("Class: " + control.getClass());
	 *     List&lt;CurrentValue&gt; currentValue = control.getCurrentValues();
	 *     for (CurrentValue cValue : currentValue) {
	 *       System.out.println("Class: " + cValue.getDocumentId());
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectWorkflowActionDetailsResponse retrieveWorkflowActionDetails(int workflowId, String workflowAction) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_ACTION);
		url = url.replace("{workflow_id}", String.valueOf(workflowId));
		url = url.replace("{workflow_action}", workflowAction);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, ObjectWorkflowActionDetailsResponse.class);
	}

	/**
	 * <b>Initiate Workflow Action </b> <br>
	 * Initiate a workflow action on a specific object workflow.
	 *
	 * @param workflowId     The workflow id.
	 * @param workflowAction The name of the workflow action.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/objectworkflows/{workflow_id}/actions/{workflow_action}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-action' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-action</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.setBodyParams(bodyParams)
	 * 				.initiateWorkflowAction(workflowId, workflowName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 * </pre>
	 */
	public VaultResponse initiateWorkflowAction(int workflowId, String workflowAction) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_ACTION);
		url = url.replace("{workflow_id}", String.valueOf(workflowId));
		url = url.replace("{workflow_action}", workflowAction);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (documentIdList != null) {
			String documentIds = documentIdList.stream().map(String::valueOf)
					.collect(Collectors.joining(","));
			request.addQueryParam("documents__sys", documentIds);
		}

		if (bodyParams != null) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.POST, request, VaultResponse.class);
	}


	/**
	 * <b>Retrieve Multi-Record Workflows </b> <br>
	 * Retrieve all available multi-record workflows which:
	 * The authenticated user has permissions to view or initiate
	 * Can be initiated through the API
	 *
	 * @return ObjectMultiRecordWorkflowsResponse
	 * @vapil.api <pre>
	 * GET /objects/objectworkflows/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#multi-record-workflows' target='_blank'>https://developer.veevavault.com/api/23.1/#multi-record-workflows</a>
	 * @vapil.request <pre>
	 * ObjectMultiRecordWorkflowsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 *                 .retrieveMultiRecordWorkflows();</pre>
	 * @vapil.response <pre>System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Response: " + response.getResponse());
	 *
	 * for (ObjectMultiRecordWorkflowsResponse.MultiRecordWorkflow workflow : response.getData()) {
	 *   System.out.println("Name: " + workflow.getName());
	 *   System.out.println("Label: " + workflow.getLabel());
	 *   System.out.println("Type: " + workflow.getType());
	 *   System.out.println("Cardinality: " + workflow.getCardinality());
	 * }</pre>
	 */
	public ObjectMultiRecordWorkflowsResponse retrieveMultiRecordWorkflows() {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_MULTI_RECORD_WORKFLOWS);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, ObjectMultiRecordWorkflowsResponse.class);
	}

	/**
	 * <b>Retrieve Multi-Record Workflow Details </b> <br>
	 * Retrieves the fields required to initiate a specific multi-record workflow.
	 *
	 * @param workflowName     The workflow name.
	 * @return ObjectMultiRecordWorkflowDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-multi-record-workflow-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-multi-record-workflow-details</a>
	 * @vapil.request <pre>
	 * ObjectMultiRecordWorkflowDetailsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 *                 .retrieveMultiRecordWorkflowDetails(workflowName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 *
	 * System.out.println("Name: " + response.getData().getName());
	 * System.out.println("Label: " + response.getData().getLabel());
	 * System.out.println("Type: " + response.getData().getType());
	 * System.out.println("Cardinality: " + response.getData().getCardinality());
	 * if (response.getData().getControls() != null) {
	 *   System.out.println("Controls: ");
	 *   for (Control control : response.getData().getControls()) {
	 *     System.out.println("    Control Label: " + control.getLabel());
	 *     System.out.println("    Control Type: " + control.getType());
	 *     System.out.println("    Prompts: ");
	 *     for (Prompt prompt : control.getPrompts()) {
	 *       System.out.println("        Prompt Label: " + prompt.getLabel());
	 *       System.out.println("        Prompt Name: " + prompt.getName());
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectMultiRecordWorkflowDetailsResponse retrieveMultiRecordWorkflowDetails(String workflowName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_MULTI_RECORD_WORKFLOW);
		url = url.replace("{workflow_name}", workflowName);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, ObjectMultiRecordWorkflowDetailsResponse.class);
	}

	/**
	 * <b>Initiate Multi-Record Workflow </b> <br>
	 * Initiate a multi-record workflow on a set of records.
	 *
	 * @param workflowName     The workflow name.
	 * @return InitiateMultiRecordWorkflowResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/objectworkflows/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-multi-record-workflow' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-multi-record-workflow</a>
	 * @vapil.request <pre>
	 * InitiateMultiRecordWorkflowResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 *                 .setBodyParams(bodyParams)
	 *                 .initiateMultiRecordWorkflow(workflowName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 * </pre>
	 */

	public ObjectMultiRecordWorkflowInitiateResponse initiateMultiRecordWorkflow(String workflowName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_MULTI_RECORD_WORKFLOW);
		url = url.replace("{workflow_name}", workflowName);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (bodyParams != null) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.POST, request, ObjectMultiRecordWorkflowInitiateResponse.class);
	}


	/*
	 *
	 * Workflow Tasks
	 *
	 */

	/**
	 * <b>Retrieve Workflow Tasks, Retrieve Object Workflow Tasks </b>
	 * <p>
	 * Note: Tasks associated with document workflows will not appear in API
	 * versions earlier than v18.3.
	 *
	 * @param object   To retrieve all workflows configured on an object.
	 * @param recordId Object record id field values e.g
	 *                 object__v={name__v}&amp;record_id__v={id}.
	 *                 object__v=product__v&amp;record_id__v=00P07551
	 * @param assignee To retrieve all workflow tasks available to a particular
	 *                 user, include the user id field value as ?assignee__v={id}.
	 *                 To retrieve your own workflow tasks, set this value to
	 *                 ?assignee__v=me. This parameter is required when the
	 *                 object__v and record_id__v parameters are not used.
	 * @return ObjectWorkflowTaskResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/tasks</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-workflow-tasks' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-workflow-tasks</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowTaskResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setPageSize(0)
	 * 				.setOffset(0).retrieveObjectWorkflowTasks(object, recordId, assignee);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 *
	 * Object.ObjectReference objInfo = resp.getResponseDetails().getObject();
	 *
	 * System.out.println("-----------Response Details -------------------");
	 * System.out.println("Total: " + resp.getResponseDetails().getTotal());
	 * System.out.println("Offset: " + resp.getResponseDetails().getOffset());
	 * System.out.println("PageSize: " + resp.getResponseDetails().getPageSize());
	 * resp.getResponseDetails().getOffset();
	 * System.out.println("-----------Object Info -------------------");
	 * System.out.println("Name: " + objInfo.getName());
	 * System.out.println("Label: " + objInfo.getLabel());
	 * System.out.println("URL: " + objInfo.getUrl());
	 * System.out.println("Label Plural: " + objInfo.getLabelPlural());
	 *
	 * List&lt;ObjectWorkflowTask&gt; tasks = resp.getData();
	 *
	 * for (ObjectWorkflowTask workflowTask : tasks) {
	 *   System.out.println("-----------Data -------------------");
	 *   System.out.println("Get ID: " + workflowTask.getId());
	 *   System.out.println("Get Label: " + workflowTask.getLabel());
	 *   System.out.println("Get CreatedDate: " + workflowTask.getCreatedDate());
	 *   System.out.println("Get Object: " + workflowTask.getObject());
	 *   System.out.println("Get RecordID: " + workflowTask.getRecordId());
	 *   System.out.println("Get Instructions: " + workflowTask.getInstructions());
	 *   System.out.println("Get Due Date: " + workflowTask.getDueDate());
	 * }</pre>
	 */
	public ObjectWorkflowTaskResponse retrieveObjectWorkflowTasks(String object, String recordId, String assignee) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_TASKS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (object != null && !object.equals("")) {
			request.addQueryParam(PARAM_OBJECT__V, object);
		}
		if (recordId != null && !recordId.equals("")) {
			request.addQueryParam(PARAM_RECORD_ID__V, recordId);
		}
		if (assignee != null && !assignee.equals("")) {
			request.addQueryParam(PARAM_PARTICIPANT, assignee);
		}
		if (status != null && !status.equals("")) {
			request.addQueryParam("status__v", status);
		}
		if (offset > 0) {
			request.addQueryParam("offset", offset);
		}
		if (pageSize > 0) {
			request.addQueryParam("page_size", pageSize);
		}
		request.addQueryParam("loc", loc);
		return send(HttpMethod.GET, request, ObjectWorkflowTaskResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Tasks Details</b>
	 *
	 * @param taskId id of task
	 * @return ObjectWorkflowTaskResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/tasks/{task_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-details</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowTaskResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.retrieveObjectWorkflowTaskDetails(taskId);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * System.out.println(resp.getResponse());
	 *
	 * Object.ObjectReference objInfo = resp.getResponseDetails().getObject();
	 *
	 * System.out.println("-----------Response Details -------------------");
	 * System.out.println("URL: " + resp.getResponseDetails().getUrl());
	 *
	 * resp.getResponseDetails().getOffset();
	 * System.out.println("-----------Object Info -------------------");
	 * System.out.println("Name: " + objInfo.getName());
	 * System.out.println("Label: " + objInfo.getLabel());
	 * System.out.println("URL: " + objInfo.getUrl());
	 * System.out.println("Label Plural: " + objInfo.getLabelPlural());
	 *
	 * List&lt;ObjectWorkflowTask&gt; tasks = resp.getData();
	 *
	 * for (ObjectWorkflowTask workflowTask : tasks) {
	 *   System.out.println("-----------Data -------------------");
	 *   System.out.println("Get ID: " + workflowTask.getId());
	 *   System.out.println("Get Label: " + workflowTask.getLabel());
	 *   System.out.println("Get CreatedDate: " + workflowTask.getCreatedDate());
	 *   System.out.println("Get Object: " + workflowTask.getObject());
	 *   System.out.println("Get RecordID: " + workflowTask.getRecordId());
	 *   System.out.println("Get Instructions: " + workflowTask.getInstructions());
	 *   System.out.println("Get Assigned Date: " + workflowTask.getAssignedDate());
	 * }</pre>
	 */
	public ObjectWorkflowTaskResponse retrieveObjectWorkflowTaskDetails(int taskId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_TASK);
		url = url.replace("{task_id}", String.valueOf(taskId));
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addQueryParam("loc", loc);
		return send(HttpMethod.GET, request, ObjectWorkflowTaskResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Tasks Actions</b>
	 *
	 * @param taskId id of task
	 * @return ObjectWorkflowActionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/tasks/{task_id}/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-actions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-actions</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowActionResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setLoc(false)
	 * 				.retrieveObjectWorkflowTaskActions(taskId);</pre>
	 * @vapil.response <pre>System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 *
	 * List&lt;WorkflowAction&gt; objectWorkflowTask = resp.getData();
	 * if (resp.getData() != null) {
	 *   for (WorkflowAction workflowDetail : objectWorkflowTask) {
	 *     System.out.println("Name: " + workflowDetail.getName());
	 *     System.out.println("Label: " + workflowDetail.getLabel());
	 *   }
	 * }</pre>
	 */
	public ObjectWorkflowActionResponse retrieveObjectWorkflowTaskActions(int taskId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_TASK_ACTIONS);
		url = url.replace("{task_id}", String.valueOf(taskId));
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addQueryParam("loc", loc);
		return send(HttpMethod.GET, request, ObjectWorkflowActionResponse.class);
	}

	/**
	 * <b>Retrieve Workflow Tasks Actions Details</b>
	 *
	 * @param taskId     The task id field value.
	 * @param taskAction The name of the task action retrieved from Retrieve
	 *                   Workflow Task Actions.
	 * @return ObjectWorkflowTaskActionDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/objectworkflows/tasks/{task_id}/actions/{task_action}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-action-details' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-workflow-task-action-details</a>
	 * @vapil.request <pre>
	 * ObjectWorkflowTaskActionDetailsResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class).setLoc(false)
	 * 				.retrieveObjectWorkflowTaskActionDetails(taskId, taskAction);</pre>
	 * @vapil.response <pre>System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());
	 *
	 * List&lt;ObjectWorkflowTaskActionDetails&gt; actionDetails = resp.getData();
	 * if (actionDetails != null) {
	 *   for (ObjectWorkflowTaskActionDetails workflowTaskActionDetails : actionDetails) {
	 *     System.out.println("-----------Data -------------------");
	 *     System.out.println("Get ID: " + workflowTaskActionDetails.getName());
	 *     System.out.println("Get Label: " + workflowTaskActionDetails.getLabel());
	 *     System.out.println("Get Type: " + workflowTaskActionDetails.getType());
	 *     System.out.println("Get Description: " + workflowTaskActionDetails.getDescription());
	 *     if (workflowTaskActionDetails.getControls() != null) {
	 *       List&lt;Control&gt; controls = workflowTaskActionDetails.getControls();
	 *       if (controls != null) {
	 *         for (Control taskControl : controls) {
	 *           System.out.println("-----------Controls -------------------");
	 *           System.out.println("Get Label: " + taskControl.getLabel());
	 *           System.out.println("Get Type: " + taskControl.getType());
	 *           System.out.println("Get Instructions " + taskControl.getInstructions());
	 *           List&lt;Verdict&gt; verdicts = taskControl.getVerdicts();
	 *           if (verdicts != null) {
	 *             for (Verdict verdict : verdicts) {
	 *               System.out.println("-----------Verdict -------------------");
	 *               System.out.println("Get Name: " + verdict.getName());
	 *               System.out.println("Get Label: " + verdict.getLabel());
	 *
	 *               List&lt;Prompt&gt; prompts = verdict.getPrompts();
	 *               if (prompts != null) {
	 *                 for (Prompt prompt : prompts) {
	 *                   System.out.println("-----------Prompts -------------------");
	 *                   System.out.println("Get Prompt Name: " + prompt.getName());
	 *                   System.out.println("Get Prompt Label: " + prompt.getLabel());
	 *                   System.out.println("Get Prompt Required: " + prompt.getRequired());
	 *                   System.out.println("Get Prompt MultiValue: " + prompt.getMultiValue());
	 *                   System.out.println("Get Prompt Type: " + prompt.getType());
	 *                 }
	 *               }
	 *             }
	 *           }
	 *         }
	 *       }
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectWorkflowTaskActionDetailsResponse retrieveObjectWorkflowTaskActionDetails(int taskId, String taskAction) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_TASK_ACTION);
		url = url.replace("{task_id}", String.valueOf(taskId));
		url = url.replace("{task_action}", taskAction);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addQueryParam("loc", loc);

		// Single value is returned by the API so use custom object mapper for re-using
		// the response (which is an array)
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, ObjectWorkflowTaskActionDetailsResponse.class);
	}

	/**
	 * <b>Initiate Workflow Task Action</b>
	 * <p>
	 * Initiate an object workflow task action on a specific object record. On
	 * SUCCESS, the specified object workflow task action is initiated on the object
	 * record.
	 *
	 * @param taskId     The task id field value.
	 * @param taskAction The name of the task action retrieved from Retrieve
	 *                   Workflow Task Actions.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/objectworkflows/tasks/{task_id}/actions/{task_action}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#initiate-workflow-task-action' target='_blank'>https://developer.veevavault.com/api/23.1/#initiate-workflow-task-action</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
	 * 				.setBodyParams(bodyParams)
	 * 				.initiateWorkflowTaskAction(taskId, taskAction);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status: " + resp.getResponseStatus());
	 * System.out.println("Response :" + resp.getResponse());</pre>
	 */
	public VaultResponse initiateWorkflowTaskAction(int taskId, String taskAction) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ACTIONS_WORKFLOW_TASK_ACTION);
		url = url.replace("{task_id}", String.valueOf(taskId));
		url = url.replace("{task_action}", taskAction);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (bodyParams != null) {
			request.setBodyParams(bodyParams);
		}

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

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
	public ObjectLifecycleWorkflowRequest setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
		return this;
	}

	/**
	 * Set the documents__sys body parameter to initiate actions on specific documents in a document workflow
	 *
	 * @param documentIdList List of document IDs
	 * @return The request
	 */
	public ObjectLifecycleWorkflowRequest setDocumentIds(List<Integer> documentIdList) {
		this.documentIdList = documentIdList;
		return this;
	}

	/**
	 * Set the loc
	 *
	 * @param loc loc
	 * @return The Request
	 */
	public ObjectLifecycleWorkflowRequest setLoc(boolean loc) {
		this.loc = loc;
		return this;
	}

	/**
	 * Set the offset
	 *
	 * @param offset offset
	 * @return The Request
	 */
	public ObjectLifecycleWorkflowRequest setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Set the page size
	 *
	 * @param pageSize page size
	 * @return The Request
	 */
	public ObjectLifecycleWorkflowRequest setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * Set the status
	 *
	 * @param status status
	 * @return The Request
	 */
	public ObjectLifecycleWorkflowRequest setStatus(String status) {
		this.status = status;
		this.loc = false;
		return this;
	}
}
