/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.EDLResponse;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

import java.util.ArrayList;
import java.util.List;


/**
 * Expected Document Lists (EDLs) help you to measure the completeness of projects.
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#expected-document-lists">https://developer.veevavault.com/api/23.1/#expected-document-lists</a>
 */
public class EDLRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_EDL_CREATE_PLACEHOLDER = "/vobjects/edl_item__v/actions/createplaceholder";
	private static final String URL_EDL_NODES = "/composites/trees/{edl_hierarchy_or_template}";
	private static final String URL_EDL_SPECIFIC_NODES = "/composites/trees/{edl_hierarchy_or_template}/actions/listnodes";
	private static final String URL_EDL_CHILD_NODES = "/composites/trees/{edl_hierarchy_or_template}/{parent_node_id}/children";
	private static final String URL_EDL_MATCHED_DOCUMENTS_ADD = "/objects/edl_matched_documents/batch/actions/add";
	private static final String URL_EDL_MATCHED_DOCUMENTS_REMOVE = "/objects/edl_matched_documents/batch/actions/remove";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private String headerAccept;
	private String headerContentType;
	private String inputPath;
	private String requestString; // For raw request

	/**
	 * Constructor - Default the Accept header to application/json
	 */
	private EDLRequest() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON; // default on all requests per spec
	}

	/**
	 * Create a placeholder from an EDL item.
	 *
	 * @param edlItemIds list of edl item ids on which to initiate the action
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/edl_item__v/actions/createplaceholder</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-a-placeholder-from-an-edl-item' target='_blank'>https://developer.veevavault.com/api/23.1/#create-a-placeholder-from-an-edl-item</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(EDLRequest.class)
	 *                 .createPlaceholderFromEDLItem(edlItemIds);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   if (resp.getWarnings() != null) {
	 *     for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 * 	      System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *     }
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse createPlaceholderFromEDLItem(List<String> edlItemIds) {
		String url = vaultClient.getAPIEndpoint(URL_EDL_CREATE_PLACEHOLDER);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("edlItemIds", String.join(",", edlItemIds));
		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}


	/**
	 * Retrieves all root EDL nodes and node metadata
	 *
	 * @param nodeType The tree object type to retrieve
	 * @return EDLResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/composites/trees/{edl_hierarchy_or_template}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-root-nodes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-root-nodes</a>
	 * @vapil.request <pre>
	 * List&lt;EDLNode&gt; resp = vaultClient.newRequest(EDLRequest.class)
	 * .retrieveAllRootNodes(EDLRequest.NodeType.TEMPLATE)
	 * .getData();</pre>
	 * @vapil.response <pre>
	 * resp.forEach(node-&gt;System.out.println(node));</pre>
	 */
	public EDLResponse retrieveAllRootNodes(NodeType nodeType) {
		String url = vaultClient.getAPIEndpoint(URL_EDL_NODES)
				.replace("{edl_hierarchy_or_template}", nodeType.getValue());
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);
		return send(HttpMethod.GET, request, EDLResponse.class);
	}

	/**
	 * Retrieves the root node ID for the given EDL record IDs
	 *
	 * @param nodeType   The tree object type to retrieve
	 * @param nodeRefIds list of EDL reference IDs to select root node IDs
	 * @return EDLResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/composites/trees/{edl_hierarchy_or_template}/actions/listnodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-specific-root-nodes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-specific-root-nodes</a>
	 * @vapil.request <pre>
	 * List&lt;EDLNode&gt; resp = vaultClient.newRequest(EDLRequest.class)
	 * .retrieveSpecificRootNodes(EDLRequest.NodeType.TEMPLATE)
	 * .getData();</pre>
	 * @vapil.response <pre>
	 * resp.forEach(node-&gt;System.out.println(node));</pre>
	 */
	public EDLResponse retrieveSpecificRootNodes(NodeType nodeType, List<String> nodeRefIds) {
		List<EDLReferenceID> edlReferences = new ArrayList<>();
		nodeRefIds.forEach(id -> edlReferences.add(new EDLReferenceID(id)));
		ObjectMapper objectMapper = new ObjectMapper();
		String bodyData = "";
		try {
			bodyData = objectMapper.writeValueAsString(edlReferences);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
		}

		String url = vaultClient.getAPIEndpoint(URL_EDL_SPECIFIC_NODES)
				.replace("{edl_hierarchy_or_template}", nodeType.getValue());
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);
		request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, bodyData.getBytes());
		request.addBodyParam("raw-data", bodyData);
		return send(HttpMethod.POST, request, EDLResponse.class);
	}

	/**
	 * Given an EDL node ID, retrieves immediate children (not grandchildren) of that node.
	 *
	 * @param nodeType     The tree object type to retrieve
	 * @param parentNodeId The Id of the Parent Node
	 * @return EDLResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/composites/trees/edl_hierarchy__v/{parent_node_id}/children</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-a-node-s-children' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-a-node-s-children</a>
	 * @vapil.request <pre>
	 * List&lt;EDLNode&gt; resp = vaultClient.newRequest(EDLRequest.class)
	 * .retrieveNodeChildren(EDLRequest.NodeType.HIERARCHY, parent_node_id)
	 * .getData();</pre>
	 * @vapil.response <pre>
	 * resp.forEach(node-&gt;System.out.println(node));</pre>
	 */
	public EDLResponse retrieveNodeChildren(NodeType nodeType, String parentNodeId) {
		String url = vaultClient.getAPIEndpoint(URL_EDL_CHILD_NODES)
				.replace("{edl_hierarchy_or_template}", nodeType.getValue())
				.replace("{parent_node_id}", parentNodeId);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);
		return send(HttpMethod.GET, request, EDLResponse.class);
	}


	/**
	 * Given an EDL node ID, retrieves immediate children (not grandchildren) of that node.
	 * <b>Content-Type</b> application/json (default) or text/csv
	 *
	 * @param nodeType     The tree object type to retrieve
	 * @param parentNodeId The ID of a parent node in the hierarchy
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/composites/trees/edl_hierarchy__v/{parent_node_id}/children</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-node-order' target='_blank'>https://developer.veevavault.com/api/23.1/#update-node-order</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(EDLRequest.class)
	 *     .setContentTypeJson() or .setContentTypeCSV()
	 *     .setInputPath(inputFilePath)
	 *     .updateNodeOrder(EDLRequest.NodeType.TEMPLATE, parentNodeId);</pre>
	 */
	public VaultResponse updateNodeOrder(NodeType nodeType, String parentNodeId) {
		String url = vaultClient.getAPIEndpoint(URL_EDL_CHILD_NODES)
				.replace("{edl_hierarchy_or_template}", nodeType.getValue())
				.replace("{parent_node_id}", parentNodeId);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, requestString);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * Add matched documents to EDL Items. You must have a security profile that grants
	 * Application: EDL Matching: Edit Document Matches permission,
	 * and EDL Matched Document APIs must be enabled in your Vault.
	 *
	 * @return EDLMatchedDocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/edl_matched_documents/batch/actions/add</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-edl-matched-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#add-edl-matched-documents</a>
	 * @vapil.request <pre>
	 * EDLMatchedDocumentResponse resp = vaultClient.newRequest(EDLRequest.class)
	 *     .setContentTypeJson() or .setContentTypeCSV()
	 *     .setInputPath(inputFilePath)
	 *     .addEdlMatchedDocuments();</pre>
	 */
	public EDLMatchedDocumentResponse addEdlMatchedDocuments() {
		String url = vaultClient.getAPIEndpoint(URL_EDL_MATCHED_DOCUMENTS_ADD);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(headerContentType, requestString);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		return send(HttpMethod.POST, request, EDLMatchedDocumentResponse.class);
	}

	/**
	 * Remove manually matched documents from EDL Items. You must have a security profile that grants
	 * Application: EDL Matching: Edit Document Matches permission,
	 * and EDL Matched Document APIs must be enabled in your Vault.
	 *
	 * @return EDLMatchedDocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/edl_matched_documents/batch/actions/remove</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#remove-edl-matched-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#remove-edl-matched-documents</a>
	 * @vapil.request <pre>
	 * EDLMatchedDocumentResponse resp = vaultClient.newRequest(EDLRequest.class)
	 *     .setContentTypeJson() or .setContentTypeCSV()
	 *     .setInputPath(inputFilePath)
	 *     .removeEdlMatchedDocuments();</pre>
	 */
	public EDLMatchedDocumentResponse removeEdlMatchedDocuments() {
		String url = vaultClient.getAPIEndpoint(URL_EDL_MATCHED_DOCUMENTS_REMOVE);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, requestString);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		return send(HttpMethod.POST, request, EDLMatchedDocumentResponse.class);
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public EDLRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public EDLRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public EDLRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public EDLRequest setAcceptCSV() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public EDLRequest setAcceptJson() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public EDLRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public EDLRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * The tree object types available for selection
	 */
	public enum NodeType {
		/**
		 * Use the edl_hierarchy__v parameter
		 */
		HIERARCHY("edl_hierarchy__v"),
		/**
		 * Use the edl_template__v parameter
		 */
		TEMPLATE("edl_template__v");

		private String value;

		NodeType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private class EDLReferenceID {
		@JsonProperty("ref_id__v")
		private String refId;

		EDLReferenceID(String value) {
			this.refId = value;
		}

	}
}
