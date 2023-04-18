/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.DocumentRenditionResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Rendition requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-renditions">https://developer.veevavault.com/api/23.1/#document-renditions</a>
 */
public class DocumentRenditionRequest extends VaultRequest {
	/**
	 * <b>X-VaultAPI-MigrationMode</b> When set to true, Vault applies Document Migration Mode limitations only to documents created with the request.
	 */
	public static final String HTTP_HEADER_VAULT_MIGRATION_MODE = "X-VaultAPI-MigrationMode";

	/**
	 * If you’re identifying documents in your input by a unique field, add idParam={fieldname} to the request endpoint.
	 */
	public static final String ID_PARAM = "idParam";


	// API Endpoints
	private static final String URL_DOC_RENDITION = "/objects/documents/{doc_id}/renditions";
	private static final String URL_DOC_RENDITION_VERSION = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions";
	private static final String URL_DOC_RENDITION_TYPE = "/objects/documents/{doc_id}/renditions/{rendition_type}";
	private static final String URL_DOC_RENDITION_TYPE_VERSION = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions/{rendition_type}";
	private static final String URL_DOC_RENDITION_BATCH = "/objects/documents/renditions/batch";


	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private String idParam;
	private String inputPath;
	private String outputPath;
	private String requestString; // For raw request
	private Boolean largeSizeAsset;
	private Boolean migrationMode;

	private DocumentRenditionRequest() {
		this.migrationMode = false;
	}

	/**
	 * Retrieve Document Renditions
	 *
	 * @param docId The document id
	 * @return DocumentRenditionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/renditions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-renditions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-renditions</a>
	 * @vapil.request <pre>
	 * DocumentRenditionResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class).retrieveDocumentRenditions(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 *
	 * for (String s : resp.getRenditionTypes())
	 * System.out.println(s);
	 *
	 * System.out.println(" ");
	 * for (String s : resp.getRenditions().getProperties().keySet())
	 * System.out.println(s + " = " + resp.getRenditions().getProperties().get(s));
	 *
	 * System.out.println("\n------ Retrieve Document Renditions Major/Minor Version ------");
	 * resp = vaultClient.newRequest(DocumentRenditionRequest.class).retrieveDocumentVersionRenditions(docId, majorVersion, minorVersion);
	 *
	 * System.out.println(resp.getResponse());
	 *
	 * for (String s : resp.getRenditionTypes())
	 * System.out.println(s);
	 *
	 * System.out.println(" ");
	 * for (String s : resp.getRenditions().getProperties().keySet())
	 * System.out.println(s + " = " + resp.getRenditions().getProperties().get(s));
	 * </pre>
	 */
	public DocumentRenditionResponse retrieveDocumentRenditions(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION);
		url = url.replace("{doc_id}", String.valueOf(docId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentRenditionResponse.class);
	}

	/**
	 * Retrieve Document Version Rendition
	 *
	 * @param docId        The document id
	 * @param majorVersion The major version
	 * @param minorVersion The minor version
	 * @return DocumentRenditionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-version-renditions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-version-renditions</a>
	 * @vapil.request <pre>
	 * DocumentRenditionResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class).retrieveDocumentVersionRenditions(docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 *
	 * for (String s : resp.getRenditionTypes())
	 *   System.out.println(s);
	 *
	 * for (String s : resp.getRenditions().getProperties().keySet())
	 *   System.out.println(s + " = " + resp.getRenditions().getProperties().get(s));</pre>
	 */
	public DocumentRenditionResponse retrieveDocumentVersionRenditions(int docId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_VERSION);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{major_version}", String.valueOf(majorVersion));
		url = url.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentRenditionResponse.class);
	}

	/**
	 * Download Document Rendition File
	 * <p>
	 * Download a rendition (file) from the latest version of a document.
	 * This method will retrieve non-steady state versions. Use the
	 * {@link #downloadDocumentRenditionFile(int, String, Boolean)}
	 * method to retrieve steady state (pass in steadyState = true).
	 *
	 * @param docId         The document id
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-rendition-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-rendition-file</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setOutputPath(inputPath.replace(".pdf", "1.pdf"))
	 * 				.downloadDocumentRenditionFile(docId, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Latest version status = " + resp.isSuccessful());
	 *
	 * resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * .setOutputPath(inputPath.replace(".pdf", "2.pdf"))
	 * .downloadDocumentRenditionFile(docId, renditionType, true);
	 * System.out.println("Steady state status = " + resp.isSuccessful());
	 *
	 * resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * .setOutputPath(inputPath.replace(".pdf", "3.pdf"))
	 * .downloadDocumentVersionRenditionFile(docId, majorVersion, minorVersion,renditionType);
	 * System.out.println("Specific version status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse downloadDocumentRenditionFile(int docId, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Download Document Rendition File
	 * <p>
	 * Download a rendition (file) from either the steady state or latest
	 * version of the document, as specified by the input steadyState param.
	 *
	 * @param docId         The document id
	 * @param renditionType The rendition type
	 * @param steadyState   True to retrieve the steady state; false for the latest version
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-rendition-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-rendition-file</a>
	 */
	public VaultResponse downloadDocumentRenditionFile(int docId, String renditionType, Boolean steadyState) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);
		if (steadyState != null && steadyState)
			request.addQueryParam("steadyState", "true");

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Download Document Version Rendition File
	 * <p>
	 * Download a specific document version rendition (file).
	 *
	 * @param docId         The document id
	 * @param majorVersion  The major version
	 * @param minorVersion  The minor version
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-version-rendition-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-version-rendition-file</a>
	 */
	public VaultResponse downloadDocumentVersionRenditionFile(int docId, int majorVersion, int minorVersion, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE_VERSION);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{major_version}", String.valueOf(majorVersion));
		url = url.replace("{minor_version}", String.valueOf(minorVersion));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Add Multiple Document Renditions<br>
	 * Add document renditions in bulk. Note that the Vault must
	 * be in migration mode for this integration call.
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/renditions/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-multiple-document-renditions' target='_blank'>https://developer.veevavault.com/api/23.1/#add-multiple-document-renditions</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setInputPath(inputPath)
	 * 				.addMultipleDocumentRenditions();</pre>
	 * @vapil.response <pre>System.out.println(new String(resp.getBinaryContent()));
	 * </pre>
	 */
	public VaultResponse addMultipleDocumentRenditions() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_BATCH);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);

		if (largeSizeAsset != null)
			request.addQueryParam("largeSizeAsset", largeSizeAsset);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, inputPath);

		if (binaryFile != null)
			request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, binaryFile.getBinaryContent());

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, requestString);

		if (migrationMode != null)
			request.addHeaderParam(HTTP_HEADER_VAULT_MIGRATION_MODE, migrationMode);

		if (outputPath != null) {
			return sendToFile(HttpMethod.POST, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.POST, request, VaultResponse.class);
		}
	}

	/**
	 * Add Single Document Rendition<br>
	 * If you need to add more than one document rendition, it is best practice to use
	 * {@link #addMultipleDocumentRenditions()}
	 *
	 * @param docId         The document id
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-single-document-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#add-single-document-rendition</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setInputPath(inputPath)
	 * 				.addSingleDocumentRendition(docId, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Add single status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse addSingleDocumentRendition(int docId, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFileMultiPart("file", inputPath);

		if (binaryFile != null)
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * Upload Document Version Rendition
	 *
	 * @param docId         The document id
	 * @param majorVersion  The major version
	 * @param minorVersion  The minor version
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upload-document-version-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#upload-document-version-rendition</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setInputPath(inputPath)
	 * 				.uploadDocumentVersionRendition(docId, majorVersion, minorVersion, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Upload status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse uploadDocumentVersionRendition(int docId, int majorVersion, int minorVersion, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE_VERSION);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{major_version}", String.valueOf(majorVersion));
		url = url.replace("{minor_version}", String.valueOf(minorVersion));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFileMultiPart("file", inputPath);

		if (binaryFile != null)
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * Replace Document Rendition
	 *
	 * @param docId         The document id
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#replace-document-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#replace-document-rendition</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setInputPath(inputPath)
	 * 				.replaceDocumentRendition(docId, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Upload status = " + resp.isSuccessful());
	 *
	 * resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * .setInputPath(inputPath)
	 * .replaceDocumentVersionRendition(docId, majorVersion, minorVersion, renditionType);
	 * System.out.println("Upload specific version status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse replaceDocumentRendition(int docId, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFileMultiPart("file", inputPath);

		if (binaryFile != null)
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * Replace Document Version Rendition
	 *
	 * @param docId         The document id
	 * @param majorVersion  The major version
	 * @param minorVersion  The minor version
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#replace-document-version-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#replace-document-version-rendition</a>
	 */
	public VaultResponse replaceDocumentVersionRendition(int docId, int majorVersion, int minorVersion, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE_VERSION);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{major_version}", String.valueOf(majorVersion));
		url = url.replace("{minor_version}", String.valueOf(minorVersion));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFileMultiPart("file", inputPath);

		if (binaryFile != null)
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * Delete Multiple Document Renditions
	 * <p>
	 * Delete document renditions in bulk.
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/renditions/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-multiple-document-renditions' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-multiple-document-renditions</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class)
	 * 				.setInputPath(inputPath)
	 * 				.deleteMultipleDocumentRenditions();</pre>
	 * @vapil.response <pre>System.out.println(new String(resp.getBinaryContent()));
	 * </pre>
	 */
	public VaultResponse deleteMultipleDocumentRenditions() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_BATCH);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);

		if (idParam != null && !idParam.isEmpty())
			request.addQueryParam(ID_PARAM, idParam);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, inputPath);

		if (binaryFile != null)
			request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, binaryFile.getBinaryContent());

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, requestString);

		if (outputPath != null) {
			return sendToFile(HttpMethod.DELETE, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.DELETE, request, VaultResponse.class);
		}
	}

	/**
	 * Delete Single Document Rendition
	 * <p>
	 * If you need to delete more than one document rendition, it is best practice to use bulk
	 * {@link #deleteMultipleDocumentRenditions()}
	 *
	 * @param docId         The document id
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{document_id}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-single-document-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-single-document-rendition</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class).deleteSingleDocumentRendition(docId, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse deleteSingleDocumentRendition(int docId, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * Delete Document Version Rendition
	 *
	 * @param docId         The document id
	 * @param majorVersion  The major version
	 * @param minorVersion  The minor version
	 * @param renditionType The rendition type
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions/{rendition_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-document-version-rendition' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-document-version-rendition</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(DocumentRenditionRequest.class).deleteDocumentVersionRendition(docId, majorVersion, minorVersion, renditionType);</pre>
	 * @vapil.response <pre>System.out.println("Status = " + resp.isSuccessful());
	 * </pre>
	 */
	public VaultResponse deleteDocumentVersionRendition(int docId, int majorVersion, int minorVersion, String renditionType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_RENDITION_TYPE_VERSION);
		url = url.replace("{doc_id}", String.valueOf(docId));
		url = url.replace("{major_version}", String.valueOf(majorVersion));
		url = url.replace("{minor_version}", String.valueOf(minorVersion));
		url = url.replace("{rendition_type}", renditionType);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public DocumentRenditionRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Indicates that the renditions to add are of the Large Size Asset (large_size_asset__v) rendition type
	 *
	 * @param largeSizeAsset The source request as a string
	 * @return The Request
	 */
	public DocumentRenditionRequest setLargeSizeAsset(Boolean largeSizeAsset) {
		this.largeSizeAsset = largeSizeAsset;
		return this;
	}

	/**
	 * Specify a value for the idParam (if not using id)
	 *
	 * @param idParam External Id field API name for the UPSERT
	 * @return The Request
	 */
	public DocumentRenditionRequest setIdParam(String idParam) {
		this.idParam = idParam;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentRenditionRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public DocumentRenditionRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a CSV request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentRenditionRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * Enable migration mode to add document renditions in bulk.
	 * If not specified, the Add Multiple Document Renditions will fail.
	 * API user must have the Document Migration permission to use this header.
	 *
	 * @param migrationMode The source request as a string
	 * @return The Request
	 */
	public DocumentRenditionRequest setMigrationMode(Boolean migrationMode) {
		this.migrationMode = migrationMode;
		return this;
	}
}
