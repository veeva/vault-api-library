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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.veeva.vault.vapil.api.model.response.InitiateDocumentWorkflowResponse;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * The API allows you to retrieve, manage, and initiate document
 * workflows.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.3/#retrieve-all-document-workflows">https://developer.veevavault.com/api/24.3/#retrieve-all-document-workflows</a>
 */
public class DocumentWorkflowRequest extends VaultRequest<DocumentWorkflowRequest> {

	private static Logger log = LoggerFactory.getLogger(DocumentWorkflowRequest.class);

	// API Endpoints
	private static final String URL_DOCUMENT_WORKFLOW_ACTIONS = "/objects/documents/actions";
	private static final String URL_DOCUMENT_WORKFLOW_ACTION_DETAILS = "/objects/documents/actions/{workflow_name}";
	private static final String URL_INITIATE_DOCUMENT_WORKFLOW_ACTION = "/objects/documents/actions/{workflow_name}";

	private static final String PARAM_DOCUMENTS = "documents__sys";
	private static final String PARAM_DESCRIPTION = "description__sys";

	private static final String DELIM_COMMA = ",";

	// API Request Parameters
	private Boolean loc;
	private String headerAccept;


	private DocumentWorkflowRequest() {
		this.loc = false;
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
	}

	/**
	 * <b>Retrieve All Document Workflows</b> <br>
	 * Retrieve all available document workflows that can be initiated on a
	 * set of documents
	 *
	 * @return DocumentWorkflowResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-all-document-workflows' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-all-document-workflows</a>
	 * @vapil.request <pre>
	 * DocumentWorkflowResponse resp = vaultClient.newRequest(DocumentWorkflowRequest.class)
	 * 				.retrieveAllDocumentWorkflows();</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   for (DocumentWorkflow workflow : resp.getData()) {
	 *     System.out.println("------------------------------");
	 *     System.out.println("Name " + workflow.getName());
	 *     System.out.println("Label " + workflow.getLabel());
	 *     System.out.println("Type " + workflow.getType());
	 *     }
	 * }</pre>
	 */
	public DocumentWorkflowResponse retrieveAllDocumentWorkflows() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_WORKFLOW_ACTIONS));

		if (loc != null && loc) {
			request.addQueryParam("loc", Boolean.toString(loc));
		}

		return send(HttpMethod.GET, request, DocumentWorkflowResponse.class);
	}

	/**
	 * <b>Retrieve Document Workflow Details</b> <br>
	 * Retrieves the details for a specific document workflow.
	 *
	 * @param workflowName The document workflow name value. Obtained from the
	 *                     Retrieve All Document Workflows request
	 *                     {@link #retrieveAllDocumentWorkflows}
	 * @return DocumentWorkflowDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-document-workflow-details' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-document-workflow-details</a>
	 * @vapil.request <pre>
	 * DocumentWorkflowDetailsResponse resp = vaultClient.newRequest(DocumentWorkflowRequest.class)
	 * 				.retrieveDocumentWorkflowDetails(workflowName);</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   DocumentWorkflowDetailsResponse.DocumentWorkflow workflow = resp.getData();
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
	public DocumentWorkflowDetailsResponse retrieveDocumentWorkflowDetails(String workflowName) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_WORKFLOW_ACTION_DETAILS);
		url = url.replace("{workflow_name}", workflowName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (loc != null && loc) {
			request.addQueryParam("loc", Boolean.toString(loc));
		}
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);
		return send(HttpMethod.GET, request, DocumentWorkflowDetailsResponse.class);
	}

	/**
	 * <b>Initiate Document Workflow</b> <br>
	 * Initiate a document workflow on a set of documents. If any document is
	 * not in the relevant state or does not meet configured field conditions, the
	 * API returns INVALID_DATA for the invalid documents and the workflow does not
	 * start.
	 *
	 * @param workflowName    The document workflow name value. Obtained from the
	 *                        Retrieve All Document Workflows request
	 *                        {@link #retrieveAllDocumentWorkflows}
	 * @param documents       Input a comma-separated list of document id field values.
	 * @param participantName Enter the participant name and input the user or group id value. For example, approvers__c: user:123,group:234.
	 * @param description     Description of the workflow. Maximum 1,000 characters.
	 * @return InitiateDocumentWorkflowResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/actions/{workflow_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#initiate-document-workflow' target='_blank'>https://developer.veevavault.com/api/24.3/#initiate-document-workflow</a>
	 * @vapil.request <pre>
	 * InitiateDocumentWorkflowResponse resp = vaultClient.newRequest(DocumentWorkflowRequest.class)
	 * 						.initiateDocumentWorkflow(workflowName, documents,participantName,description);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Record ID " + resp.getData().getRecordId());
	 *   System.out.println("Record URL " + resp.getData().getRecordUrl());
	 *   System.out.println("Workflow ID " + resp.getData().getWorkflowId());
	 * }</pre>
	 */
	public InitiateDocumentWorkflowResponse initiateDocumentWorkflow(String workflowName,
																		  List<String> documents,
																		  Map<String, String> participantName,
																		  String description) {
		String url = vaultClient.getAPIEndpoint(URL_INITIATE_DOCUMENT_WORKFLOW_ACTION);
		url = url.replace("{workflow_name}", workflowName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return sendInitiateDocumentWorkflowRequest(HttpMethod.POST, documents, participantName, description, request);
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
	 * @return InitiateDocumentWorkflowResponse
	 */
	private InitiateDocumentWorkflowResponse sendInitiateDocumentWorkflowRequest(HttpMethod method, List<String> documents,
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
		return (isValidCRUDRequest(request)) ? send(method, request, InitiateDocumentWorkflowResponse.class)
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
	public DocumentWorkflowRequest setLoc(Boolean loc) {
		this.loc = loc;
		return this;
	}

	/**
	 * Set the Header Accept to return JSON
	 *
	 * @return The Request
	 */
	public DocumentWorkflowRequest setAcceptJSON() {
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
