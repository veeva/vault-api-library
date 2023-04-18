/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.common.DocumentRelationship;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipMetadataResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipRetrieveResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Relationship Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-relationships">https://developer.veevavault.com/api/23.1/#document-relationships</a>
 */
public class DocumentRelationshipRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_DOCUMENT_TYPE_RELATIONSHIPS = "/metadata/objects/documents/types/{type}/relationships";
	private static final String URL_DOCUMENT_RELATIONSHIPS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships";
	private static final String URL_DOCUMENT_RELATIONSHIP = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}";
	private static final String URL_DOCUMENT_RELATIONSHIPS_BATCH = "/objects/documents/relationships/batch";

	// API Request Parameters
	private String headerContentType;
	private HttpRequestConnector.BinaryFile binaryFile;
	private String inputPath;
	private String requestString; // For raw request

	private DocumentRelationshipRequest() {
		// Defaults for the request
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	}

	/**
	 * <b>Retrieve Document Type Relationships</b>
	 * <br>
	 * Retrieve all relationships from a document type.
	 *
	 * @param documentType The document type
	 * @return DocumentRelationshipMetadataResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/types/{type}/relationships</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-type-relationships' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-type-relationships</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipMetadataResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
	 * 					.retrieveDocumentTypeRelationships(documentType);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());
	 *
	 * if (response.isSuccessful()) {
	 *   // Properties
	 *   for (RelationshipProperty p : response.getProperties()) {
	 *     System.out.println("\n**** Relationship Property **** ");
	 *     System.out.println("Name = " + p.getName());
	 *     System.out.println("Type = " + p.getType());
	 *     System.out.println("Length = " + p.getLength());
	 *     System.out.println("Editable = " + p.getEditable());
	 *     System.out.println("Queryable = " + p.getQueryable());
	 *     System.out.println("Required = " + p.getRequired());
	 *     System.out.println("Multivalue = " + p.getMultivalue());
	 *     System.out.println("onCreateEditable = " + p.getOnCreateEditable());
	 *     }
	 *
	 *   // Relationship Types
	 *   for (RelationshipType t : response.getRelationshipTypes()) {
	 *     System.out.println("\n**** Relationship Type **** ");
	 *     System.out.println("Value = " + t.getValue());
	 *     System.out.println("Label = " + t.getLabel());
	 *     System.out.println("Source Doc Version Specific = " + t.getSourceDocVersionSpecific());
	 *     System.out.println("Target Doc Version Specific = " + t.getTargetDocVersionSpecific());
	 *     System.out.println("System = " + t.getSystem());
	 *     System.out.println("Single User = " + t.getSingleUse());
	 *
	 *     // Target Document Types
	 *     for (TargetDocumentType data : t.getTargetDocumentTypes()) {
	 *       System.out.println("\n**** Target Document Type **** ");
	 *       System.out.println("Label = " + data.getLabel());
	 *       System.out.println("Value = " + data.getValue());
	 *       }
	 *     }
	 *
	 *   // Relationships
	 *   for (Relationship r : response.getRelationships()) {
	 *     System.out.println("\n**** Relationship Item **** ");
	 *     System.out.println("Relationship Name = " + r.getRelationshipName());
	 *     System.out.println("Relationship Label = " + r.getRelationshipLabel());
	 *     System.out.println("Relationship Type = " + r.getRelationshipName());
	 *
	 *     // Object
	 *     System.out.println("Relationship Object Name = " + r.getRelationshipObject().getName());
	 *     }
	 * }</pre>
	 */
	public DocumentRelationshipMetadataResponse retrieveDocumentTypeRelationships(String documentType) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_TYPE_RELATIONSHIPS)
				.replace("{type}", documentType));

		return send(HttpMethod.GET, request, DocumentRelationshipMetadataResponse.class);
	}

	/**
	 * <b>Retrieve Document Relationships</b>
	 * <br>
	 * Retrieve all relationships from a document.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @return DocumentRelationshipResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-relationships' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-relationships</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipRetrieveResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
	 * 				.retrieveDocumentRelationships(docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());
	 *
	 * if (response.isSuccessful()) {
	 *   // Properties
	 *   for (DocumentRelationshipRetrieveResponse.Relationship data : response.getRelationships()) {
	 *     DocumentRelationship dr = data.getRelationship();
	 *     System.out.println("\n**** Relationship **** ");
	 *     System.out.println("Source Doc ID = " + dr.getSourceDocId());
	 *     System.out.println("Relationship Type = " + dr.getRelationshipType());
	 *     System.out.println("Created Date = " + dr.getCreatedDate());
	 *     System.out.println("Relationship Id = " + dr.getId());
	 *     System.out.println("Target Doc Id = " + dr.getTargetDocId());
	 *     System.out.println("Created By = " + dr.getCreatedBy());
	 *   }
	 * }</pre>
	 */
	public DocumentRelationshipRetrieveResponse retrieveDocumentRelationships(int docId, int majorVersion, int minorVersion) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_RELATIONSHIPS)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion)));

		return send(HttpMethod.GET, request, DocumentRelationshipRetrieveResponse.class);
	}

	/**
	 * <b>Retrieve Document Relationship</b>
	 * <br>
	 * Retrieve details of a specific document relationship.
	 *
	 * @param docId          The Document Id
	 * @param majorVersion   The document major_version_number__v field value
	 * @param minorVersion   The document minor_version_number__v field value
	 * @param relationshipId The relationship id field value
	 * @return DocumentRelationshipRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-relationship</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipRetrieveResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
	 * 				.retrieveDocumentRelationship(docId, majorVersion, minorVersion, relationshipId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());
	 *
	 * if (response.isSuccessful()) {
	 *   // Properties
	 *   for (DocumentRelationshipRetrieveResponse.Relationship data : response.getRelationships()) {
	 *     DocumentRelationship dr = data.getRelationship();
	 *     System.out.println("\n**** Relationship **** ");
	 *     System.out.println("Source Doc ID = " + dr.getSourceDocId());
	 *     System.out.println("Relationship Type = " + dr.getRelationshipType());
	 *     System.out.println("Created Date = " + dr.getCreatedDate());
	 *     System.out.println("Id = " + dr.getId());
	 *     System.out.println("Target Doc Id = " + dr.getTargetDocId());
	 *     System.out.println("Created By = " + dr.getCreatedBy());
	 *     }
	 * }</pre>
	 */
	public DocumentRelationshipRetrieveResponse retrieveDocumentRelationship(int docId, int majorVersion, int minorVersion, int relationshipId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_RELATIONSHIP)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion))
				.replace("{relationship_id}", Integer.toString(relationshipId)));

		return send(HttpMethod.GET, request, DocumentRelationshipRetrieveResponse.class);
	}

	/**
	 * <b>Create Single Document Relationship</b>
	 * <br>
	 * Create a new relationship on a document.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @param relationship The DocumentRelationship
	 * @return DocumentRelationshipResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{document_id}/versions/{major_version_number__v}/{minor_version_number__v}/relationships</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document-relationship</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
	 * 				.createSingleDocumentRelationship(docId, majorVersion, minorVersion, docRelationship);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful()) {
	 *   System.out.println("ResponseMessage = " + response.getResponseMessage());
	 *   System.out.println("Id = " + response.getId());
	 * }</pre>
	 */
	public DocumentRelationshipResponse createSingleDocumentRelationship(int docId, int majorVersion, int minorVersion, DocumentRelationship relationship) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_RELATIONSHIPS)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion)));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(relationship.toMap());

		return send(HttpMethod.POST, request, DocumentRelationshipResponse.class);
	}

	/**
	 * <b>Create Multiple Document Relationships</b>
	 * <br>
	 * Create new relationships on multiple documents.
	 * The maximum input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return DocumentRelationshipBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/relationships/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-multiple-document-relationships' target='_blank'>https://developer.veevavault.com/api/23.1/#create-multiple-document-relationships</a>
	 */
	public DocumentRelationshipBulkResponse createMultipleDocumentRelationships() {
		return bulkDocumentRelationshipOperation(HttpMethod.POST, URL_DOCUMENT_RELATIONSHIPS_BATCH);
	}

	/**
	 * <b>Delete Single Document Relationship</b>
	 * <br>
	 * Delete a relationship from a document.
	 *
	 * @param docId          The Document Id
	 * @param majorVersion   The document major_version_number__v field value
	 * @param minorVersion   The document minor_version_number__v field value
	 * @param relationshipId The relationship id field value
	 * @return DocumentRelationshipResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{document_id}/versions/{major_version_number__v}/{minor_version_number__v}/relationships/{relationship_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-single-document-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-single-document-relationship</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
	 * 				.deleteSingleDocumentRelationship(docId, majorVersion, minorVersion, relationshipId);</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful()) {
	 *   System.out.println("ResponseMessage = " + response.getResponseMessage());
	 *   System.out.println("Id = " + response.getId());
	 * }</pre>
	 */
	public DocumentRelationshipResponse deleteSingleDocumentRelationship(int docId, int majorVersion, int minorVersion, int relationshipId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_RELATIONSHIP)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion))
				.replace("{relationship_id}", Integer.toString(relationshipId)));

		return send(HttpMethod.DELETE, request, DocumentRelationshipResponse.class);
	}

	/**
	 * <b>Delete Multiple Document Relationships</b>
	 * <br>
	 * Delete a relationships from multiple documents.
	 * The maximum input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 1000.
	 *
	 * @return DocumentRelationshipBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/relationships/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-multiple-document-relationships' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-multiple-document-relationships</a>
	 */
	public DocumentRelationshipBulkResponse deleteMultipleDocumentRelationships() {
		return bulkDocumentRelationshipOperation(HttpMethod.DELETE, URL_DOCUMENT_RELATIONSHIPS_BATCH);
	}

	/**
	 * Covers Bulk requests
	 */
	private DocumentRelationshipBulkResponse bulkDocumentRelationshipOperation(HttpMethod httpMethod, String urlEndpoint) {
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

		return send(httpMethod, request, DocumentRelationshipBulkResponse.class);
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
	public DocumentRelationshipRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to application/json
	 *
	 * @return The Request
	 */
	public DocumentRelationshipRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set the Header Content Type to application/x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public DocumentRelationshipRequest setContentTypeXForm() {
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
	public DocumentRelationshipRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentRelationshipRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentRelationshipRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}
}
