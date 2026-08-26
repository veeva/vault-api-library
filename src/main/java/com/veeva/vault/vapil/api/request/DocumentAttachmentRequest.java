/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.DocumentAttachmentBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentAttachmentResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Document Attachment requests
 *
 * @vapil.apicoverage <a href="https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments">https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments</a>
 */
public class DocumentAttachmentRequest extends VaultRequest<DocumentAttachmentRequest> {

	// API Endpoints
	private static final String URL_DOCUMENT_ATTACHMENTS = "/objects/documents/{doc_id}/attachments";
	private static final String URL_DOCUMENT_ATTACHMENTS_FILE = "/objects/documents/{doc_id}/attachments/file";
	private static final String URL_DOCUMENT_ATTACHMENTS_BATCH = "/objects/documents/attachments/batch";
	private static final String URL_DOCUMENT_ATTACHMENT = "/objects/documents/{doc_id}/attachments/{attachment_id}";
	private static final String URL_DOCUMENT_ATTACHMENT_FILE = "/objects/documents/{doc_id}/attachments/{attachment_id}/file";
	private static final String URL_DOCUMENT_ATTACHMENT_VERSIONS = "/objects/documents/{doc_id}/attachments/{attachment_id}/versions";
	private static final String URL_DOCUMENT_ATTACHMENT_VERSION = "/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}";
	private static final String URL_DOCUMENT_ATTACHMENT_VERSION_FILE = "/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}/file";
	private static final String URL_DOCUMENT_ATTACHMENTS_DELETIONS = "/objects/deletions/documents/attachments";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENTS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENTS_FILE = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/file";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENT = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENT_FILE = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/file";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENT_VERSIONS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENT_VERSION = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions/{attachment_version}";
	private static final String URL_DOCUMENT_VERSION_ATTACHMENT_VERSION_FILE = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions/{attachment_version}/file";

	// API Request Parameters
	private static final String PARAM_START_DATE = "start_date";
	private static final String PARAM_END_DATE = "end_date";
	private static final String PARAM_LIMIT = "limit";

	private HttpRequestConnector.BinaryFile binaryFile;
	private String headerContentType;
	private String inputPath;
	private String outputPath;
	private String requestString; // For raw request
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Integer limit;

	// Required date format for retrieve deleted document attachments request
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private DocumentAttachmentRequest() {
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	}

	/**
	 * <b>Retrieve Document Attachments</b>
	 *
	 * @param docId document id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachments</a>
	 */
	public DocumentAttachmentResponse retrieveDocumentAttachments(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENTS)
				.replace("{doc_id}", String.valueOf(docId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Version Attachments</b>
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachments</a>
	 * @vapil.request <pre>
	 * DocumentAttachmentDeletionResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
	 * 		.setStartDate(startDate)
	 * 		.setEndDate(endDate)
	 * 		.retrieveDeletedDocumentAttachments();
	 * </pre>
	 * @vapil.response <pre>
	 * for (DocumentAttachmentDeletionResponse.DeleteDocumentAttachment attachment : response.getData()) {
	 * 		System.out.println("--------Attachment--------");
	 * 		System.out.println("Date Deleted: " + attachment.getDateDeleted());
	 * 		System.out.println("Deletion Type: " + attachment.getDeletionType());
	 * 		System.out.println("Id: " + attachment.getId());
	 * 		System.out.println("Id: " + attachment.getVersion());
	 * }
	 * </pre>
	 */
	public DocumentAttachmentResponse retrieveDocumentVersionAttachments(int docId,
																		 int majorVersionNumber,
																		 int minorVersionNumber) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENTS)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Attachment Versions</b>
	 *
	 * @param docId        document id
	 * @param attachmentId attachment id
	 * @return DocumentAttachmentVersionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-versions' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-versions</a>
	 */
	public DocumentAttachmentVersionResponse retrieveDocumentAttachmentVersions(int docId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_VERSIONS)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentVersionResponse.class);
	}

	/**
	 * <b>Retrieve Document Version Attachment Versions</b>
	 * <p>
	 * Retrieve attachment versions on a specific version of a document.
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @param attachmentId       attachment id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachment-versions' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachment-versions</a>
	 */
	public DocumentAttachmentResponse retrieveDocumentVersionAttachmentVersions(int docId,
																				int majorVersionNumber,
																				int minorVersionNumber,
																				int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENT_VERSIONS)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Attachment Metadata</b>
	 *
	 * @param docId        document id
	 * @param attachmentId attachment id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-metadata' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-metadata</a>
	 */
	public DocumentAttachmentResponse retrieveDocumentAttachmentMetadata(int docId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Version Attachment Metadata</b>
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @param attachmentId       attachment id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink (Undocumented Endpoint)
	 */
	public DocumentAttachmentResponse retrieveDocumentVersionAttachmentMetadata(int docId,
																				int majorVersionNumber,
																				int minorVersionNumber,
																				int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENT)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Attachment Version Metadata</b>
	 *
	 * @param docId           document id
	 * @param attachmentId    attachment id
	 * @param attachVersionId attachment version id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-version-metadata' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-attachment-version-metadata</a>
	 */
	public DocumentAttachmentResponse retrieveDocumentAttachmentVersionMetadata(int docId,
																				int attachmentId,
																				int attachVersionId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_VERSION)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachVersionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Retrieve Document Version Attachment Version Metadata</b>
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @param attachmentId       attachment id
	 * @param attachVersionId    attachment version id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachment-version-metadata' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-document-version-attachment-version-metadata</a>
	 */
	public DocumentAttachmentResponse retrieveDocumentVersionAttachmentVersionMetadata(int docId,
																					   int majorVersionNumber,
																					   int minorVersionNumber,
																					   int attachmentId,
																					   int attachVersionId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENT_VERSION)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachVersionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentAttachmentResponse.class);
	}


	/**
	 * <b>Download Document Attachment</b>
	 * <p>
	 * Downloads the latest version of the specified attachment from the document.
	 *
	 * @param docId        document id
	 * @param attachmentId attachment id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/file</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-attachment' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-attachment</a>
	 */
	public VaultResponse downloadDocumentAttachment(int docId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_FILE)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download Document Version Attachment</b>
	 * <p>
	 * Downloads the latest version of the specified attachment from the document version
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @param attachmentId       attachment id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/file</pre>
	 * @vapil.vaultlink (Undocumented Endpoint)
	 */
	public VaultResponse downloadDocumentVersionAttachment(int docId,
														   int majorVersionNumber,
														   int minorVersionNumber,
														   int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENT_FILE)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download Document Attachment Version</b>
	 *
	 * @param docId           document id
	 * @param attachmentId    attachment id
	 * @param attachVersionId attachment version id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}/file</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-attachment-version' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-attachment-version</a>
	 */
	public VaultResponse downloadDocumentAttachmentVersion(int docId, int attachmentId, int attachVersionId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_VERSION_FILE)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachVersionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download Document Version Attachment Version</b>
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @param attachmentId       attachment id
	 * @param attachVersionId    attachment version id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/attachments/{attachment_id}/versions/{attachment_version}/file</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-version-attachment-version' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-document-version-attachment-version</a>
	 */
	public VaultResponse downloadDocumentVersionAttachmentVersion(int docId,
																  int majorVersionNumber,
																  int minorVersionNumber,
																  int attachmentId,
																  int attachVersionId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENT_VERSION_FILE)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachVersionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download All Document Attachments</b>
	 *
	 * @param docId document id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/attachments/file</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-all-document-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-all-document-attachments</a>
	 */
	public VaultResponse downloadAllDocumentAttachments(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENTS_FILE)
				.replace("{doc_id}", String.valueOf(docId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download All Document Version Attachments</b>
	 *
	 * @param docId              document id
	 * @param majorVersionNumber major version number
	 * @param minorVersionNumber minor version number
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/{major_version}/{minor_version}/attachments/file</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-all-document-version-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/download-all-document-version-attachments</a>
	 */
	public VaultResponse downloadAllDocumentVersionAttachments(int docId,
															   int majorVersionNumber,
															   int minorVersionNumber) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ATTACHMENTS_FILE)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{major_version}", String.valueOf(majorVersionNumber))
				.replace("{minor_version}", String.valueOf(minorVersionNumber));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Delete Single Document Attachment</b>
	 *
	 * @param docId        document id
	 * @param attachmentId attachment id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-single-document-attachment' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-single-document-attachment</a>
	 */
	public VaultResponse deleteSingleDocumentAttachment(int docId,
														int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * <b>Delete Single Document Attachment Version</b>
	 *
	 * @param docId             document id
	 * @param attachmentId      attachment id
	 * @param attachmentVersion version of the attachment
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-single-document-attachment-version' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-single-document-attachment-version</a>
	 */
	public VaultResponse deleteSingleDocumentAttachmentVersion(int docId,
															   int attachmentId,
															   int attachmentVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_VERSION)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachmentVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * <b>Delete Multiple Document Attachments</b>
	 * <p>
	 * This works for version-specific attachments and attachments at the document level.
	 *
	 * @return DocumentAttachmentBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-multiple-document-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/delete-multiple-document-attachments</a>
	 */
	public DocumentAttachmentBulkResponse deleteMultipleDocumentAttachments() {
		return bulkAttachments(HttpMethod.DELETE);
	}

	/**
	 * <b>Create Document Attachment</b>
	 * <p>
	 * Create an attachment on the latest version of a document. If the attachment already exists,
	 * Vault uploads the attachment as a new version of the existing attachment.
	 *
	 * @param docId document id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/attachments</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/create-document-attachment' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/create-document-attachment</a>
	 */
	public DocumentAttachmentResponse createDocumentAttachment(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENTS)
				.replace("{doc_id}", String.valueOf(docId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.POST, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Create Multiple Document Attachments</b>
	 * <p>
	 * Create multiple document attachments in bulk with a JSON or CSV input file.
	 * You must first load the attachments to the FTP staging server.
	 * This works for version-specific attachments and attachments at the document level.
	 * If the attachment already exists, Vault uploads the attachment as a new version
	 * of the existing attachment. Learn more about attachment versioning in Vault Help.
	 *
	 * @return DocumentAttachmentBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/create-multiple-document-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/create-multiple-document-attachments</a>
	 */
	public DocumentAttachmentBulkResponse createMultipleDocumentAttachments() {
		return bulkAttachments(HttpMethod.POST);
	}

	/**
	 * <b>Restore Document Attachment Version</b>
	 * Restores the specific version of an existing attachment to make it the latest version
	 *
	 * @param docId           document id
	 * @param attachmentId    attachment id
	 * @param attachVersionId attachment version id
	 * @return DocumentAttachmentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}/versions/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/restore-document-attachment-version' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/restore-document-attachment-version</a>
	 */
	public DocumentAttachmentResponse restoreDocumentAttachmentVersion(int docId,
																	   int attachmentId,
																	   int attachVersionId) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT_VERSION)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId))
				.replace("{attachment_version}", String.valueOf(attachVersionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addQueryParam("restore", true);

		return send(HttpMethod.POST, request, DocumentAttachmentResponse.class);
	}

	/**
	 * <b>Update Document Attachment Description</b>
	 *
	 * @param docId        document id
	 * @param attachmentId attachment id
	 * @param description  description of the attachment
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/update-document-attachment-description' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/update-document-attachment-description</a>
	 */
	public VaultResponse updateDocumentAttachmentDescription(int docId,
															 int attachmentId,
															 String description) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENT)
				.replace("{doc_id}", String.valueOf(docId))
				.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
		request.addBodyParam("description__v", description);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * <b>Update Multiple Document Attachment Descriptions</b>
	 * <p>
	 * Update multiple document attachments in bulk with a JSON or CSV input file.
	 * This works for version-specific attachments and attachments at the document level.
	 * You can only update the latest version of an attachment.
	 *
	 * @return DocumentAttachmentBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/update-multiple-document-attachment-descriptions' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/update-multiple-document-attachment-descriptions</a>
	 */
	public DocumentAttachmentBulkResponse updateMultipleDocumentAttachmentDescriptions() {
		return bulkAttachments(HttpMethod.PUT);
	}

	/**
	 * <b>Retrieve Deleted Document Attachments</b>
	 *
	 * @return DocumentAttachmentDeletionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/deletions/documents/attachments</pre>
	 * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-deleted-document-attachments' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.1/documents/document-attachments/retrieve-deleted-document-attachments</a>
	 */
	public DocumentAttachmentDeletionResponse retrieveDeletedDocumentAttachments() {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENTS_DELETIONS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (startDate != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDate));
		}

		if (endDate != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDate));
		}

		if (limit != null) {
			request.addQueryParam(PARAM_LIMIT, limit);
		}

		return send(HttpMethod.GET, request, DocumentAttachmentDeletionResponse.class);
	}


	/**
	 * handles all CREATE, UPDATE, AND DELETE batch methods
	 * <p>
	 * /api/{version}/objects/documents/attachments/batch
	 *
	 * @return DocumentBulkResponse
	 */
	private DocumentAttachmentBulkResponse bulkAttachments(HttpMethod httpMethod) {
		String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_ATTACHMENTS_BATCH);

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

		return send(httpMethod, request, DocumentAttachmentBulkResponse.class);
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
	public DocumentAttachmentRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public DocumentAttachmentRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public DocumentAttachmentRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}


	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentAttachmentRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public DocumentAttachmentRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentAttachmentRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * Specify a date (no more than 30 days past) after which Vault will look for deleted document attachments.
	 *
	 * @param startDate start date
	 * @return DocumentAttachmentRequest
	 */
	public DocumentAttachmentRequest setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * Specify a date (no more than 30 days past) before which Vault will look for deleted document attachments.
	 *
	 * @param endDate end date
	 * @return DocumentAttachmentRequest
	 */
	public DocumentAttachmentRequest setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * Paginate the results by specifying the maximum number of deleted attachments to display per page in the response.
	 * <p>
	 *
	 * @param limit the size of the result set in the page
	 * @return DocumentAttachmentRequest
	 */
	public DocumentAttachmentRequest setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Converts the date to the proper string format expected by the API
	 *
	 * @param date        The date to convert
	 * @return Formatted date as string
	 */
	private String getFormattedDate(ZonedDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
		return date.format(formatter);
	}
}
