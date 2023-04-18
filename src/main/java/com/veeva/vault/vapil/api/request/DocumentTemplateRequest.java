/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.DocumentTemplate;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.DocumentTemplateBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentTemplateMetadataResponse;
import com.veeva.vault.vapil.api.model.response.DocumentTemplateResponse;
import com.veeva.vault.vapil.api.model.response.DocumentTemplatesResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Template Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-templates">https://developer.veevavault.com/api/23.1/#document-templates</a>
 */
public class DocumentTemplateRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_DOCUMENT_TEMPLATE_METADATA = "/metadata/objects/documents/templates";
	private static final String URL_DOCUMENT_TEMPLATE = "/objects/documents/templates/{template_name}";
	private static final String URL_DOCUMENT_TEMPLATES = "/objects/documents/templates";
	private static final String URL_DOCUMENT_TEMPLATE_FILE = "/objects/documents/templates/{template_name}/file";

	// API Request Parameters
	private String headerContentType;
	private HttpRequestConnector.BinaryFile binaryFile;
	private String inputPath;
	private String outputPath;
	private String requestString; // For raw request

	private DocumentTemplateRequest() {
		// Defaults for the request
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	}

	/**
	 * <b>Retrieve Document Template Metadata</b>
	 * <p>
	 * Retrieve the metadata which defines the shape of document templates in your vault.
	 *
	 * @return DocumentTemplateMetadataResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-template-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-template-metadata</a>
	 */
	public DocumentTemplateMetadataResponse retrieveDocumentTemplateMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATE_METADATA));

		return send(HttpMethod.GET, request, DocumentTemplateMetadataResponse.class);
	}

	/**
	 * <b>Retrieve Document Template Collection</b>
	 * <p>
	 * Retrieve all document templates.
	 *
	 * @return DocumentTemplatesResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-template-collection' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-template-collection</a>
	 * @vapil.request <pre>
	 * DocumentTemplateMetadataResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.retrieveDocumentTemplateMetadata();</pre>
	 * @vapil.response <pre>
	 * for (Template data : resp.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("Name = " + data.getName());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Requiredness = " + data.getRequiredness());
	 *   System.out.println("Editable = " + data.getEditable());
	 *   System.out.println("Multi Value = " + data.getMultiValue());
	 *   System.out.println("Component = " + data.getComponent());
	 *   System.out.println("Ordered = " + data.getOrdered());
	 *   System.out.println("MaxValue = " + data.getMaxValue());
	 * }
	 * </pre>
	 */
	public DocumentTemplatesResponse retrieveDocumentTemplateCollection() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATES));

		return send(HttpMethod.GET, request, DocumentTemplatesResponse.class);
	}

	/**
	 * <b>Retrieve Document Template Attributes</b>
	 * <p>
	 * Retrieve the attributes from a document template.
	 *
	 * @param templateName The document template name__v field value.
	 * @return DocumentTemplateResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/templates/{template_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-template-attributes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-template-attributes</a>
	 * @vapil.request <pre>
	 * DocumentTemplatesResponse respTemplates = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.retrieveDocumentTemplateCollection();
	 *
	 * String templateName = respTemplates.getData().get(0).getName();
	 * DocumentTemplateResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.retrieveDocumentTemplateAttributes(templateName);</pre>
	 * @vapil.response <pre>
	 * DocumentTemplate data = resp.getData();
	 * System.out.println("\n**** Data Item **** ");
	 * System.out.println("Name = " + data.getName());
	 * System.out.println("Label = " + data.getLabel());
	 * System.out.println("Active = " + data.getActive());
	 * System.out.println("Type = " + data.getType());
	 * System.out.println("SubType = " + data.getSubType());
	 * System.out.println("Classification = " + data.getClassification());
	 * System.out.println("Format = " + data.getFormat());
	 * System.out.println("Size = " + data.getSize());
	 * System.out.println("Created By = " + data.getCreatedBy());
	 * System.out.println("File Uploaded By = " + data.getFileUploadedBy());
	 * System.out.println("MD5 Checksum = " + data.getMd5checksum());
	 * System.out.println("Is Controlled = " + data.getIsControlled());
	 * System.out.println("Template Doc ID = " + data.getTemplateDocId());
	 * System.out.println("Template Doc Selected By = " + data.getTemplateDocSelectedBy());</pre>
	 */
	public DocumentTemplateResponse retrieveDocumentTemplateAttributes(String templateName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATE)
				.replace("{template_name}", templateName));

		return send(HttpMethod.GET, request, DocumentTemplateResponse.class);
	}

	/**
	 * <b>Download Document Template File</b>
	 * <p>
	 * Download the file of a specific document template.
	 *
	 * @param templateName The document template name__v field value.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/templates/{template_name}/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-template-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-template-file</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Download directly to file</i>
	 * VaultResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.downloadDocumentTemplateFile(templateName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Stream and Download</i>
	 * VaultResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setOutputPath(null)
	 * 				.downloadDocumentTemplateFile(templateName);
	 *
	 * if (resp.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   try (OutputStream os = new FileOutputStream(outputPath.toString())) {
	 *     os.write(resp.getBinaryContent());
	 *   }
	 *   catch (IOException ignored){}
	 * }</pre>
	 * @vapil.response <pre>
	 * System.out.println("File was saved to: " + outputPath.toString());</pre>
	 */
	public VaultResponse downloadDocumentTemplateFile(String templateName) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATE_FILE)
				.replace("{template_name}", templateName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Create Single Document Template</b>
	 * <p>
	 * Create one document template. To create multiple document templates, see Bulk Create Document Templates.
	 *
	 * @param documentTemplate Template metadata to create
	 * @return DocumentTemplateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document-template' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document-template</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - From file</i>
	 * DocumentTemplateResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setInputPath(inputPath.toString())
	 * 				.createSingleDocumentTemplate(documentTemplate);</pre>
	 * @vapil.request <pre>
	 * <i>Example 1 - From binary</i>
	 * DocumentTemplateResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setBinaryFile("DocTemplate.rtf", getRTFContent().getBytes())
	 * 				.createSingleDocumentTemplate(documentTemplate);</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   System.out.println("The document template was created: " + resp.getName());
	 * }</pre>
	 */
	public DocumentTemplateResponse createSingleDocumentTemplate(DocumentTemplate documentTemplate) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATES));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
		request.setBodyParams(documentTemplate.toMap());

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.POST, request, DocumentTemplateResponse.class);
	}

	/**
	 * <b>Create Multiple Document Templates</b>
	 * <p>
	 * Create up to 500 document templates.
	 *
	 * @return DocumentTemplateBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-multiple-document-templates' target='_blank'>https://developer.veevavault.com/api/23.1/#create-multiple-document-templates</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV as File</i>
	 * DocumentTemplateBulkResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.createMultipleDocumentTemplates();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - CSV as Bytes</i>
	 * DocumentTemplateBulkResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setBinaryFile(fileName, csvText.toString().getBytes())
	 * 				.createMultipleDocumentTemplates();</pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - JSON</i>
	 * DocumentTemplateBulkResponse resp = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.setContentTypeJson()
	 * 				.setRequestString(jsonString.toString())
	 * 				.createMultipleDocumentTemplates();</pre>
	 * @vapil.response <pre>
	 * for (DocumentTemplateBulkResponse.TemplateResult data : resp.getData()) {
	 *   System.out.println("Status: " + data.getResponseStatus());
	 *
	 *   if (!data.isSuccessful()) {
	 *     for (VaultResponse.APIResponseError e : data.getErrors()){
	 *       System.out.println(e.getType() + " " + e.getMessage());
	 *     }
	 *   }
	 *   else {
	 *     System.out.println("Name: " + data.getName());
	 *   }
	 * }</pre>
	 */
	public DocumentTemplateBulkResponse createMultipleDocumentTemplates() {
		return bulkDocumentTemplateOperation(HttpMethod.POST, URL_DOCUMENT_TEMPLATES);
	}

	/**
	 * <b>Update Single Document Template</b>
	 * <p>
	 * Update a single document template in your vault.
	 *
	 * @param templateName     The document template name__v field value.
	 * @param documentTemplate Template metadata to update.
	 * @return DocumentTemplateResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/templates/{template_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-single-document-template' target='_blank'>https://developer.veevavault.com/api/23.1/#update-single-document-template</a>
	 * @vapil.request <pre>
	 * DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.updateSingleDocumentTemplate(DOCUMENT_TEMPLATE_NAME1, documentTemplate);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful()) {
	 *   System.out.println("The template was updated: " + response.getName());
	 * }
	 * else {
	 *   System.out.println(response.getResponse());
	 * }</pre>
	 */
	public DocumentTemplateResponse updateSingleDocumentTemplate(String templateName, DocumentTemplate documentTemplate) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATE)
				.replace("{template_name}", templateName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(documentTemplate.toMap());

		return send(HttpMethod.PUT, request, DocumentTemplateResponse.class);
	}

	/**
	 * <b>Update Multiple Document Templates</b>
	 * <p>
	 * Update uo to 500 document templates in your vault.
	 *
	 * @return DocumentTemplateBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/templates</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-multiple-document-templates' target='_blank'>https://developer.veevavault.com/api/23.1/#update-multiple-document-templates</a>
	 * @vapil.request <pre>See {@link #createMultipleDocumentTemplates()} for example requests (replace "createMultipleDocumentTemplates" with "updateMultipleDocumentTemplates")</pre>
	 * @vapil.response <pre>See {@link #createMultipleDocumentTemplates()} for example responses (replace "createMultipleDocumentTemplates" with "updateMultipleDocumentTemplates")</pre>
	 */
	public DocumentTemplateBulkResponse updateMultipleDocumentTemplates() {
		return bulkDocumentTemplateOperation(HttpMethod.PUT, URL_DOCUMENT_TEMPLATES);
	}

	/**
	 * <b>Delete Basic Document Template</b>
	 * <p>
	 * Delete a basic document template from your vault. You cannot delete controlled document templates. Learn more about controlled template deletion in Vault Help.
	 *
	 * @param templateName The document template name__v field value.
	 * @return DocumentTemplateResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/templates/{template_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-basic-document-template' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-basic-document-template</a>
	 * @vapil.request <pre>
	 * DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
	 * 				.deleteBasicDocumentTemplate(DOCUMENT_TEMPLATE_NAME1);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful()) {
	 *   System.out.println("The document template was deleted");
	 * }</pre>
	 */
	public DocumentTemplateResponse deleteBasicDocumentTemplate(String templateName) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_TEMPLATE)
				.replace("{template_name}", templateName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, DocumentTemplateResponse.class);
	}

	/**
	 * Helper method for DocumentTemplate bulk requests
	 */
	private DocumentTemplateBulkResponse bulkDocumentTemplateOperation(HttpMethod httpMethod, String urlEndpoint) {
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

		return send(httpMethod, request, DocumentTemplateBulkResponse.class);
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
	public DocumentTemplateRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to application/json
	 *
	 * @return The Request
	 */
	public DocumentTemplateRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set the Header Content Type to application/x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public DocumentTemplateRequest setContentTypeXForm() {
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
	public DocumentTemplateRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentTemplateRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public DocumentTemplateRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentTemplateRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}
}
