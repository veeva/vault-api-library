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

import com.veeva.vault.vapil.api.model.response.InitiateMultiDocumentWorkflowResponse;
import org.apache.log4j.Logger;

import com.veeva.vault.vapil.api.model.response.MultiDocumentWorkflowDetailsResponse;
import com.veeva.vault.vapil.api.model.response.MultiDocumentWorkflowsResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * The API allows you to retrieve, manage, and initiate multi-document
 * workflows.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/21.1/#retrieve-all-multi-document-workflows">https://developer.veevavault.com/api/21.1/#retrieve-all-multi-document-workflows</a>
 */
public class MultiDocumentWorkflowsRequest extends VaultRequest {

	private static Logger log = Logger.getLogger(MultiDocumentWorkflowsRequest.class);

	// API Endpoints
	private static final String URL_MULTIDOC_WORKFLOW_ACTIONS = "/objects/documents/actions";
	private static final String URL_MULTIDOC_WORKFLOW_ACTION_DETAILS = "/objects/documents/actions/{workflow_name}";
	private static final String URL_INITIATE_MULTIDOC_WORKFLOW_ACTION = "/objects/documents/actions/{workflow_name}";

	private static final String PARAM_DOCUMENTS = "documents__sys";
	private static final String PARAM_DESCRIPTION = "description__sys";

	private static final String DELIM_COMMA = ",";

	// API Request Parameters
	private Boolean loc;
	private String headerAccept;


	private MultiDocumentWorkflowsRequest() {
		this.loc = false;
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
	}

	/**
	 * <b>Retrieve All Multi-Document Workflows</b> <br>
	 * Retrieve all available multi-document workflows that can be initiated on a
	 * set of documents
	 *
	 * @return MultiDocumentWorkflowsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#retrieve-all-multi-document-workflows' target='_blank'>https://developer.veevavault.com/api/21.1/#retrieve-all-multi-document-workflows</a>
	 * @vapil.request <pre>
	 * MultiDocumentWorkflowsResponse resp = vaultClient.newRequest(MultiDocumentWorkflowsRequest.class)
	 * 				.retrieveAllMultiDocumentWorkflows();</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   for (MultiDocWorkflow workflow : resp.getData()) {
	 *     System.out.println("------------------------------");
	 *     System.out.println("Name " + workflow.getName());
	 *     System.out.println("Label " + workflow.getLabel());
	 *     System.out.println("Type " + workflow.getType());
	 *     }
	 * }</pre>
	 */
	public MultiDocumentWorkflowsResponse retrieveAllMultiDocumentWorkflows() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_MULTIDOC_WORKFLOW_ACTIONS));

		if (loc != null && loc) {
			request.addQueryParam("loc", Boolean.toString(loc));
		}

		return send(HttpMethod.GET, request, MultiDocumentWorkflowsResponse.class);
	}

	/**
	 * <b>Retrieve Multi-Document Workflow Details</b> <br>
	 * Retrieves the details for a specific multi-document workflow.
	 *
	 * @param workflowName The multi-document workflow name value. Obtained from the
	 *                     Retrieve All MultiDocument Workflows request
	 *                     {@link #retrieveAllMultiDocumentWorkflows}
	 * @return MultiDocumentWorkflowDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#retrieve-multi-document-workflow-details' target='_blank'>https://developer.veevavault.com/api/21.1/#retrieve-multi-document-workflow-details</a>
	 * @vapil.request <pre>
	 * MultiDocumentWorkflowDetailsResponse resp = vaultClient.newRequest(MultiDocumentWorkflowsRequest.class)
	 * 				.retrieveMultiDocumentWorkflowDetails(workflowName);</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   MultiDocumentWorkflowDetailsResponse.MultiDocumentWorkflow workflow = resp.getData();
	 *   System.out.println("Workflow Name " + workflow.getName());
	 *   System.out.println("------------------------------");
	 *   for (Control con : workflow.getControls()) {
	 *     System.out.println("Control Exist\n");
	 *     System.out.println("------------------------------");
	 *     System.out.println("Control Instruction " + con.getInstructions());
	 *     System.out.println("Control Label " + con.getLabel());
	 *     System.out.println("Control Type " + con.getType());
	 *
	 *     for (Prompt prompts : con.getPrompts()) {
	 *       System.out.println("\tPrompts Exist\n");
	 *       System.out.println("------------------------------");
	 *       System.out.println("\tPrompt Lable " + prompts.getLabel());
	 *       System.out.println("\tPrompt Name " + prompts.getName());
	 *       System.out.println("\tPrompt Multivalue " + prompts.getMultiValue());
	 *
	 *     }
	 *   }
	 * }
	 * </pre>
	 */
	public MultiDocumentWorkflowDetailsResponse retrieveMultiDocumentWorkflowDetails(String workflowName) {
		String url = vaultClient.getAPIEndpoint(URL_MULTIDOC_WORKFLOW_ACTION_DETAILS);
		url = url.replace("{workflow_name}", workflowName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (loc != null && loc) {
			request.addQueryParam("loc", Boolean.toString(loc));
		}
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);
		return send(HttpMethod.GET, request, MultiDocumentWorkflowDetailsResponse.class);
	}

	/**
	 * <b>Initiate Multi-Document Workflow</b> <br>
	 * Initiate a multi-document workflow on a set of documents. If any document is
	 * not in the relevant state or does not meet configured field conditions, the
	 * API returns INVALID_DATA for the invalid documents and the workflow does not
	 * start.
	 *
	 * @param workflowName    The multi-document workflow name value. Obtained from the
	 *                        Retrieve All MultiDocument Workflows request
	 *                        {@link #retrieveAllMultiDocumentWorkflows}
	 * @param documents       Input a comma-separated list of document id field values.
	 * @param participantName Enter the participant name and input the user or group id value. For example, approvers__c: user:123,group:234.
	 * @param description     Description of the workflow. Maximum 1,000 characters.
	 * @return InitiateMultiDocumentWorkflowsResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/21.1/#initiate-multi-document-workflow' target='_blank'>https://developer.veevavault.com/api/21.1/#initiate-multi-document-workflow</a>
	 * @vapil.request <pre>
	 * InitiateMultiDocumentWorkflowResponse resp = vaultClient.newRequest(MultiDocumentWorkflowsRequest.class)
	 * 						.initiateMultiDocumentWorkflow(workflowName, documents,participantName,description);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Record ID " + resp.getData().getRecordId());
	 *   System.out.println("Record URL " + resp.getData().getRecordUrl());
	 *   System.out.println("Workflow ID " + resp.getData().getWorkflowId());
	 * }</pre>
	 */
	public InitiateMultiDocumentWorkflowResponse initiateMultiDocumentWorkflow(String workflowName,
																			   List<String> documents,
																			   Map<String, String> participantName,
																			   String description) {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_MULTIDOC_WORKFLOW_ACTION);
		url = url.replace("{workflow_name}", workflowName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return sendInitiateMultiDocumentWorkflowRequest(HttpMethod.POST, documents, participantName, description, request);
	}

	/**
	 * Private method to combine build, validation and send of create/update methods
	 * to a single call
	 *
	 * @param method          The HTTP Method to perform
	 * @param documents       Input a comma-separated list of document id field values.
	 * @param participantName Enter the participant name and input the user or group id value. For example, approvers__c: user:123,group:234.
	 * @param description     Description of the workflow. Maximum 128 characters.
	 * @param request         The HTTP request
	 * @return InitiateMultiDocumentWorkflowsResponse
	 */
	private InitiateMultiDocumentWorkflowResponse sendInitiateMultiDocumentWorkflowRequest(HttpMethod method, List<String> documents,
																						   Map<String, String> participantName, String description, HttpRequestConnector request) {

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE,
				HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (documents != null && !documents.isEmpty()) {
			request.addBodyParam(PARAM_DOCUMENTS, String.join(DELIM_COMMA, documents));
		}
		if (participantName != null && !participantName.isEmpty()) {
			for (Map.Entry mapElement : participantName.entrySet()) {
				String key = (String) mapElement.getKey();
				String value = (String) mapElement.getValue();
				request.addBodyParam(key, value);
			}
		}
		// Allow empty string
		if (description != null) {
			request.addBodyParam(PARAM_DESCRIPTION, description);
		}
		return (isValidCRUDRequest(request)) ? send(method, request, InitiateMultiDocumentWorkflowResponse.class)
				: null;
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * When localized (translated) strings are available, retrieve them by setting
	 * loc to true
	 *
	 * @param loc Set to true to retrieve localized (translated) strings (if
	 *            available)
	 * @return The request
	 */
	public MultiDocumentWorkflowsRequest setLoc(Boolean loc) {
		this.loc = loc;
		return this;
	}

	/**
	 * Set the Header Accept to return JSON
	 *
	 * @return The Request
	 */
	public MultiDocumentWorkflowsRequest setAcceptJSON() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}


	/**
	 * Determine if the Create-Read-Update-Delete request is properly formed before
	 * sending
	 *
	 * @param request
	 * @return True if the request is properly formed
	 */
	private boolean isValidCRUDRequest(HttpRequestConnector request) {

		// Verify there is data name/value pairs
		if (request.getBodyParams() == null || request.getBodyParams().isEmpty()) {
			log.error("Invalid request - no source data");
			return false;
		}
		return true;
	}
}
