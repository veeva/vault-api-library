/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.BinderTemplate;
import com.veeva.vault.vapil.api.model.response.BinderTemplateBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateMetadataResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateNodeBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

import java.util.Map;

/**
 * The Binder Template APIs retrieve information about Binder Templates
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#binder-templates">https://developer.veevavault.com/api/23.1/#binder-templates</a>
 */
public class BinderTemplateRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_BINDER_TEMPLATE_METADATA = "/metadata/objects/binders/templates";
	private static final String URL_BINDER_TEMPLATE_NODE_METADATA = "/metadata/objects/binders/templates/bindernodes";
	private static final String URL_BINDER_TEMPLATE = "/objects/binders/templates/{template_name}";
	private static final String URL_BINDER_TEMPLATES = "/objects/binders/templates";
	private static final String URL_BINDER_TEMPLATE_NODES = "/objects/binders/templates/{template_name}/bindernodes";

	// API Request Parameters
	private String headerContentType;
	private HttpRequestConnector.BinaryFile binaryFile;
	private String inputPath;
	private String requestString; // For raw request

	private BinderTemplateRequest() {
		// Defaults for the request
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	}

	/**
	 * <b>Retrieve Binder Template Metadata</b>
	 * <p>
	 * Retrieve the metadata which defines the shape of binder templates in your vault.
	 *
	 * @return BinderTemplateMetadataResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/binders/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-template-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-template-metadata</a>
	 * @vapil.request <pre>
	 * BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.retrieveBinderTemplateMetadata();</pre>
	 * @vapil.response <pre>
	 * for (Template data : response.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Name = " + data.getName());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Requiredness = " + data.getRequiredness());
	 *   System.out.println("Editable = " + data.getEditable());
	 *   System.out.println("Multi Value = " + data.getMultiValue());
	 *   System.out.println("Component = " + data.getComponent());
	 *   System.out.println("Ordered = " + data.getOrdered());
	 * }</pre>
	 */
	public BinderTemplateMetadataResponse retrieveBinderTemplateMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATE_METADATA));

		return send(HttpMethod.GET, request, BinderTemplateMetadataResponse.class);
	}

	/**
	 * <b>Retrieve Binder Template Node Metadata</b>
	 * <p>
	 * Retrieve the metadata which defines the shape of binder template nodes in your vault.
	 *
	 * @return BinderTemplateMetadataResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/binders/templates/bindernodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-template-node-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-template-node-metadata</a>
	 * @vapil.request <pre>
	 * BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.retrieveBinderTemplateNodeMetadata();</pre>
	 * @vapil.response <pre>
	 * for (Template data : response.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Name = " + data.getName());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Requiredness = " + data.getRequiredness());
	 *   System.out.println("Editable = " + data.getEditable());
	 *   System.out.println("Multi Value = " + data.getMultiValue());
	 *   System.out.println("Enums = " + data.getEnums());
	 *   System.out.println("Component = " + data.getComponent());
	 *   System.out.println("Max Value = " + data.getMaxValue());
	 *   System.out.println("Min Value = " + data.getMinValue());
	 *   System.out.println("Scale = " + data.getScale());
	 * }</pre>
	 */
	public BinderTemplateMetadataResponse retrieveBinderTemplateNodeMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATE_NODE_METADATA));

		return send(HttpMethod.GET, request, BinderTemplateMetadataResponse.class);
	}

	/**
	 * <b>Retrieve Binder Template Collection</b>
	 * <p>
	 * Retrieve the collection of all binder templates in your vault.
	 *
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-template-collection' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-template-collection</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.retrieveBinderTemplateCollection();</pre>
	 * @vapil.response <pre>
	 * for (BinderTemplate data : response.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Name = " + data.getName());
	 *   System.out.println("Label = " + data.getLabel());
	 *   System.out.println("Active = " + data.getActive());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Subtype = " + data.getSubType());
	 *   System.out.println("Classification = " + data.getClassification());
	 *   System.out.println("Filing Model = " + data.getFilingModel());
	 * }</pre>
	 */
	public BinderTemplateResponse retrieveBinderTemplateCollection() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATES));

		return send(HttpMethod.GET, request, BinderTemplateResponse.class);
	}

	/**
	 * <b>Retrieve Binder Template Attributes</b>
	 * <p>
	 * Retrieve the attributes of a specific binder template in your vault.
	 *
	 * @param templateName The binder template name__v field value.
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/templates/{template_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-template-attributes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-template-attributes</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse templates = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.retrieveBinderTemplateCollection();</pre>
	 * @vapil.response <pre>
	 * String templateName = templates.getData().get(0).getName();
	 *
	 * BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * .retrieveBinderTemplateAttributes(templateName);
	 *
	 * for (BinderTemplate data : response.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Name = " + data.getName());
	 *   System.out.println("Label = " + data.getLabel());
	 *   System.out.println("Active = " + data.getActive());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Subtype = " + data.getSubType());
	 *   System.out.println("Classification = " + data.getClassification());
	 *   System.out.println("Filing Model = " + data.getFilingModel());
	 * }</pre>
	 */
	public BinderTemplateResponse retrieveBinderTemplateAttributes(String templateName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATE)
				.replace("{template_name}", templateName));

		return send(HttpMethod.GET, request, BinderTemplateResponse.class);
	}

	/**
	 * <b>Retrieve Binder Template Node Attributes</b>
	 * <p>
	 * Retrieve the attributes of each node (folder/section) of a specific binder template in your vault.
	 *
	 * @param templateName The binder template name__v field value.
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/templates/{template_name}/bindernodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-template-node-attributes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-template-node-attributes</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse resp = vaultClient.newRequest(BinderTemplateRequest.class)
	 * .retrieveBinderTemplateNodeAttributes(binderTemplateName);</pre>
	 * @vapil.response <pre>
	 * for (BinderTemplate data : resp.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Id = " + data.getId());
	 *   System.out.println("Parent Id = " + data.getParentId());
	 *   System.out.println("Node Type = " + data.getNodeType());
	 *   System.out.println("Label = " + data.getLabel());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Subtype = " + data.getSubType());
	 *   System.out.println("Classification = " + data.getClassification());
	 *   System.out.println("Lifecycle = " + data.getLifecycle());
	 *   System.out.println("Number = " + data.getNumber());
	 *   System.out.println("Order = " + data.getOrder());
	 *   System.out.println("Hierarchy Mapping = " + data.getHierarchyMapping());
	 * }</pre>
	 */
	public BinderTemplateResponse retrieveBinderTemplateNodeAttributes(String templateName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATE_NODES)
				.replace("{template_name}", templateName));

		return send(HttpMethod.GET, request, BinderTemplateResponse.class);
	}

	/**
	 * <b>Create Binder Template</b>
	 * <p>
	 * Create a new binder template in your vault.
	 *
	 * @param binderTemplate Template metadata to create
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-template' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-template</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 			.createBinderTemplate(template);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());</pre>
	 */
	public BinderTemplateResponse createBinderTemplate(BinderTemplate binderTemplate) {
		return singleBinderOperation(HttpMethod.POST, URL_BINDER_TEMPLATES, binderTemplate.toMap());
	}

	/**
	 * <b>Bulk Create Binder Templates</b>
	 * <p>
	 * Create from 1-500 new binder templates in your vault.
	 *
	 * @return BinderTemplateBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#bulk-create-binder-templates' target='_blank'>https://developer.veevavault.com/api/23.1/#bulk-create-binder-templates</a>
	 * @vapil.request <pre>
	 * <i>Prep a CSV string for testing</i>
	 * StringBuilder csvText = new StringBuilder("name__v,label__v,type__v,subtype__v,classification__v,active__v\n");
	 * csvText.append("binder_template_1__c,Binder Template 1," + DOCUMENT_TYPE + ",,,true\n");
	 * csvText.append(",Binder Template 2," + DOCUMENT_TYPE + ",,,true\n");</pre>
	 * @vapil.request <pre>
	 * <i>Prep a JSON string for testing</i>
	 * StringBuilder jsonString = new StringBuilder();
	 * jsonString.append("[");
	 * jsonString.append("{\"name__v\": \"binder_template_1__c\",\"label__v\": \"Binder Template 1\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\"},");
	 * jsonString.append("{\"name__v\": \"\",\"label__v\": \"Binder Template 2\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\"}");
	 * jsonString.append("]");</pre>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV as a File</i>
	 * BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);
	 * String filePath = saveToDesktop(fileName, csvText.toString());
	 * request.setInputPath(filePath);
	 * BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - CSV as Bytes</i>
	 * BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);
	 * request.setBinaryFile(fileName, csvText.toString().getBytes());
	 * BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();</pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - JSON</i>
	 * BinderTemplateBulkResponse request = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.setContentTypeJson();
	 * 				.setRequestString(jsonString.toString());
	 *
	 * BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();</pre>
	 * @vapil.response <pre>
	 * for (BinderTemplateBulkResponse.TemplateResult data : response.getData()) {
	 * 		System.out.println("Status: " + data.getResponseStatus());
	 * 		System.out.println("Name: " + data.getName());
	 * }</pre>
	 */
	public BinderTemplateBulkResponse bulkCreateBinderTemplates() {
		return bulkBinderOperation(HttpMethod.POST, URL_BINDER_TEMPLATES);
	}

	/**
	 * <b>Update Binder Template</b>
	 * <p>
	 * Update an existing binder template in your vault.<br>
	 * By changing the document type/subtype/classification, you can move an existing binder
	 * template to a different level of the document type hierarchy,
	 * effectively reclassifying the template.
	 *
	 * @param templateName   The binder template name__v field value.
	 * @param binderTemplate Template metadata to update.
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/binders/templates/{template_name}</pre>
	 * @vapil.request <pre>
	 * BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.updateBinderTemplate(binderTemplateName, template);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());</pre>
	 */
	public BinderTemplateResponse updateBinderTemplate(String templateName, BinderTemplate binderTemplate) {
		String url = URL_BINDER_TEMPLATE
				.replace("{template_name}", templateName);

		return singleBinderOperation(HttpMethod.PUT, url, binderTemplate.toMap());
	}

	/**
	 * <b>Bulk Update Binder Templates</b>
	 * <p>
	 * Update from 1-500 binder templates in your vault.<br>
	 * By changing the document type/subtype/classification, you can move an existing
	 * binder template to a different level of the document type hierarchy,
	 * effectively reclassifying the template.
	 * <p>
	 * See {@link #bulkCreateBinderTemplates()} for example request and response
	 *
	 * @return BinderTemplateBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/binders/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#bulk-update-binder-templates' target='_blank'>https://developer.veevavault.com/api/23.1/#bulk-update-binder-templates</a>
	 */
	public BinderTemplateBulkResponse bulkUpdateBinderTemplates() {
		return bulkBinderOperation(HttpMethod.PUT, URL_BINDER_TEMPLATES);
	}

	/**
	 * <b>Delete Binder Template</b>
	 * <p>
	 * Delete an existing binder template from your vault.
	 *
	 * @param templateName The binder template name__v field value.
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/binders/templates/{template_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-binder-template' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-binder-template</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse resp = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.deleteBinderTemplate(binderTemplateName);</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Successfully deleted the binder.");
	 * }</pre>
	 */
	public BinderTemplateResponse deleteBinderTemplate(String templateName) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_TEMPLATE)
				.replace("{template_name}", templateName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, BinderTemplateResponse.class);
	}

	/**
	 * <b>Create Binder Template Node</b>
	 * <p>
	 * Create nodes in an existing binder template.
	 *
	 * @param templateName   The binder template name__v field value.
	 * @param binderTemplate Template metadata to create
	 * @return BinderTemplateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/templates/{template_name}/bindernodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-template-node' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-template-node</a>
	 * @vapil.request <pre>
	 * BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.createBinderTemplateNode(binderTemplateName, templateNode);</pre>
	 * @vapil.response <pre>System.out.println(response.getResponse());
	 *
	 * BinderTemplate templateSubNode = new BinderTemplate();
	 * templateSubNode.setId("anything"); // Doesn't seem to matter what this is, but it's required
	 * templateSubNode.setLabel("Sub Node 1");
	 * templateSubNode.setNumber("01.01");
	 * templateSubNode.setParentId(response.getData().get(0).getId()); // Generated id from above
	 * templateSubNode.setNodeType(BinderTemplate.NodeType.SECTION);
	 *
	 * response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * .createBinderTemplateNode(binderTemplateName, templateSubNode);
	 * System.out.println(response.getResponse());</pre>
	 */
	public BinderTemplateResponse createBinderTemplateNode(String templateName, BinderTemplate binderTemplate) {
		String url = URL_BINDER_TEMPLATE_NODES
				.replace("{template_name}", templateName);

		return singleBinderOperation(HttpMethod.POST, url, binderTemplate.toMap());
	}

	/**
	 * <b>Bulk Create Binder Template Nodes</b>
	 * <p>
	 * Update nodes in an existing binder template.
	 *
	 * @param templateName The binder template name__v field value.
	 * @return BinderTemplateNodeBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/templates/{template_name}/bindernodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-template-node' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-template-node</a>
	 * @vapil.request <pre>
	 *  <i>Example 1 - CSV as File</i>
	 * BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.bulkCreateBinderTemplateNodes(binderTemplateName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV as Bytes</i>
	 * BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.setBinaryFile(fileName, csvText.toString().getBytes())
	 * 				.bulkCreateBinderTemplateNodes(binderTemplateName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - JSON</i>
	 *
	 * BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.setContentTypeJson()
	 * 				.setRequestString(jsonText.toString())
	 * 				.bulkCreateBinderTemplateNodes(binderTemplateName);</pre>
	 * @vapil.request <pre>
	 * for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
	 *   System.out.println("Status: " + result.getResponseStatus());
	 * }</pre>
	 */
	public BinderTemplateNodeBulkResponse bulkCreateBinderTemplateNodes(String templateName) {
		String url = URL_BINDER_TEMPLATE_NODES
				.replace("{template_name}", templateName);

		return bulkBinderNodeOperation(HttpMethod.POST, url);
	}

	/**
	 * <b>Replace Binder Template Nodes</b>
	 * <p>
	 * Replace all binder nodes in an existing binder template. This action removes
	 * all existing nodes and replaces them with those specified in the input.
	 * <p>
	 * See {@link #bulkCreateBinderTemplateNodes(String)} for example request and response
	 *
	 * @param templateName The binder template name__v field value.
	 * @return BinderTemplateNodeBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/binders/templates/{template_name}/bindernodes</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#replace-binder-template-nodes' target='_blank'>https://developer.veevavault.com/api/23.1/#replace-binder-template-nodes</a>
	 * @vapil.request <pre>
	 * BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.replaceBinderTemplateNodes(binderTemplateName);</pre>
	 */
	public BinderTemplateNodeBulkResponse replaceBinderTemplateNodes(String templateName) {
		String url = URL_BINDER_TEMPLATE_NODES
				.replace("{template_name}", templateName);

		return bulkBinderNodeOperation(HttpMethod.PUT, url);
	}

	/**
	 * Private helper method for BinderTemplate single requests
	 */
	private BinderTemplateResponse singleBinderOperation(HttpMethod httpMethod, String urlEndpoint, Map<String, Object> bodyParams) {
		String url = vaultClient.getAPIEndpoint(urlEndpoint);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(bodyParams);

		return send(httpMethod, request, BinderTemplateResponse.class);
	}

	/**
	 * Private helper method for BinderTemplate bulk requests
	 */
	private BinderTemplateBulkResponse bulkBinderOperation(HttpMethod httpMethod, String urlEndpoint) {
		String url = vaultClient.getAPIEndpoint(urlEndpoint);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(contentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(contentType, requestString);

		if (binaryFile != null)
			request.addBinary(contentType, binaryFile.getBinaryContent());

		return send(httpMethod, request, BinderTemplateBulkResponse.class);
	}

	/**
	 * Private helper method for BinderTemplateNode bulk requests
	 */
	private BinderTemplateNodeBulkResponse bulkBinderNodeOperation(HttpMethod httpMethod, String urlEndpoint) {
		String url = vaultClient.getAPIEndpoint(urlEndpoint);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(contentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(contentType, requestString);

		if (binaryFile != null)
			request.addBinary(contentType, binaryFile.getBinaryContent());

		return send(httpMethod, request, BinderTemplateNodeBulkResponse.class);
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the Header Content Type to text/csv
	 *
	 * @return The Request
	 */
	public BinderTemplateRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to application/json
	 *
	 * @return The Request
	 */
	public BinderTemplateRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set the Header Content Type to application/x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public BinderTemplateRequest setContentTypeXForm() {
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
	public BinderTemplateRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public BinderTemplateRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public BinderTemplateRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}
}
