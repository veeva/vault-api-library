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

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataDocumentLockResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.apache.log4j.Logger;

/**
 * Document Requests
 * <ul>
 * <li>CRUD operations (single and bulk)</li>
 * <li>Create from Template</li>
 * <li>Placeholders</li>
 * <li>Versions</li>
 * <li>Locks</li>
 * <li>Reclassify</li>
 * <li>Document Meta-Data</li>
 * <li>Document Types</li>
 * </ul>
 * <p>
 * Many documents requests, including single document requests, leverage the {@link Document} model
 * for inbound/outbound API calls. This includes setters/getters for standard Vault fields and
 * a setter/getter for custom fields. Example:
 * <pre>
 * Document doc = new Document();
 *
 * doc.setName("VAPIL Single Document");
 * doc.setLifecycle("General Lifecycle");
 * doc.setType("General");
 * doc.setTitle("Test Upload VAPIL");
 * doc.setProperty("custom_field__c", "value");
 *
 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
 * 					.setInputPath(filePath)
 * 					.createSingleDocument(doc);</pre>
 * <p>
 * Bulk endpoints should be used for multiple document transactions. Example with input CSV file:
 * <pre>
 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
 * 					.setInputPath(csvFilePath)
 * 					.createMultipleDocuments();</pre>
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#documents">https://developer.veevavault.com/api/23.1/#documents</a>
 * <p>
 * <i>The following endpoints are covered in other classes for ease of use</i>
 * <ul>
 * <li>Document Annotations - {@link DocumentAnnotationRequest}</li>
 * <li>Document Attachments - {@link DocumentAttachmentRequest}</li>
 * <li>Document Events - {@link DocumentEventRequest}</li>
 * <li>Document Relationships - {@link DocumentRelationshipRequest}</li>
 * <li>Document Roles - {@link DocumentRoleRequest}</li>
 * <li>Document Renditions - {@link DocumentRenditionRequest}</li>
 * <li>Document Templates - {@link DocumentTemplateRequest}</li>
 * </ul>
 */
public class DocumentRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(DocumentRequest.class);

	/**
	 * <b>X-VaultAPI-MigrationMode</b> When set to true, Vault applies Document Migration Mode limitations only to documents created with the request.
	 */
	public static final String HTTP_HEADER_VAULT_MIGRATION_MODE = "X-VaultAPI-MigrationMode";

	/**
	 * If you’re identifying documents in your input by a unique field, add idParam={fieldname} to the request endpoint.
	 */
	public static final String ID_PARAM = "idParam";

	// API Endpoints
	private static final String URL_DOCS = "/objects/documents";
	private static final String URL_DOC = "/objects/documents/{doc_id}";
	private static final String URL_DOC_DELETIONS = "/objects/deletions/documents";
	private static final String URL_DOC_EXTRACT = "/objects/documents/batch/actions/fileextract";
	private static final String URL_DOC_EXTRACT_VERSIONS = "/objects/documents/versions/batch/actions/fileextract";
	private static final String URL_DOC_EXTRACT_RESULTS = "/objects/documents/batch/actions/fileextract/{jobid}/results";
	private static final String URL_DOC_RECLASSIFY = "/objects/documents/batch/actions/reclassify";
	private static final String URL_DOC_FILE = "/objects/documents/{doc_id}/file";
	private static final String URL_DOC_ALL_FIELDS = "/metadata/objects/documents/properties";
	private static final String URL_DOCS_BATCH = "/objects/documents/batch";
	private static final String URL_DOCS_BATCH_VERSIONS = "/objects/documents/versions/batch";
	private static final String URL_DOC_COMMON_FIELDS = "/metadata/objects/documents/properties/find_common";
	private static final String URL_DOC_TYPES = "/metadata/objects/documents/types";
	private static final String URL_DOC_TYPE = "/metadata/objects/documents/types/{type}";
	private static final String URL_DOC_SUBTYPE = "/metadata/objects/documents/types/{type}/subtypes/{subtype}";
	private static final String URL_DOC_CLASSIFICATION = "/metadata/objects/documents/types/{type}/subtypes/{subtype}/classifications/{classification}";
	private static final String URL_DOC_LOCK = "/objects/documents/{doc_id}/lock";
	private static final String URL_DOC_LOCK_METADATA = "/metadata/objects/documents/lock";
	private static final String URL_DOC_TOKENS = "/objects/documents/tokens";
	private static final String URL_DOC_VERSION = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}";
	private static final String URL_DOC_VERSION_FILE = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/file";
	private static final String URL_DOC_VERSION_THUMBNAIL = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/thumbnail";
	private static final String URL_DOC_VERSIONS = "/objects/documents/{doc_id}/versions";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private Integer boundSourceMajorVersion;
	private Integer boundSourceMinorVersion;
	private String channel;
	private String description;
	private DownloadOption downloadOption;
	private String headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
	private Integer expiryDateOffset;
	private String idParam;
	private Integer limit;
	private String inputPath;
	private Boolean lockDocument;
	private NamedFilter namedFilter;
	private String outputPath;
	private String requestString; // For raw request
	private Scope scope;
	private String searchKeyword;
	private String sort;
	private Integer start;
	private Boolean steadyState;
	private Boolean suppressRendition;
	private CrosslinkBinding sourceBindingRule;
	private String tokenGroup;
	private VersionsScope versionsScope;
	private Boolean migrationMode;
	private Integer offset;
	private String startDate;
	private String endDate;


	private DocumentRequest() {
		this.migrationMode = false;
	}

	/*
	 *
	 * Retrieve Document Fields
	 *
	 */

	/**
	 * <b>Retrieve All Document Fields</b>
	 * <p>
	 * Retrieve all standard and custom document fields and field properties.
	 *
	 * @return DocumentFieldResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/properties</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-document-fields' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-document-fields</a>
	 * @vapil.request <pre>
	 * DocumentFieldResponse resp  = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveAllDocumentFields();</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public DocumentFieldResponse retrieveAllDocumentFields() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_ALL_FIELDS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentFieldResponse.class);
	}

	/**
	 * <b>Retrieve Common Document Fields</b>
	 * <p>
	 * Retrieve all document fields and field properties which are common to (shared by)
	 * a specified set of documents. This allows you to determine which document fields
	 * are eligible for bulk update.
	 *
	 * @param docIds List of Document Ids
	 * @return DocumentFieldResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/metadata/objects/documents/properties/find_common</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-common-document-fields' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-common-document-fields</a>
	 * @vapil.request <pre>
	 * DocumentFieldResponse resp  = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveCommonDocumentFields();</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public DocumentFieldResponse retrieveCommonDocumentFields(Set<Integer> docIds) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_COMMON_FIELDS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		String docIdList = docIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		request.addBodyParam("docIds", docIdList);

		return send(HttpMethod.POST, request, DocumentFieldResponse.class);
	}

	/*
	 *
	 * Retrieve Document Types
	 *
	 */

	/**
	 * <b>Retrieve All Document Types</b>
	 * <p>
	 * Retrieve all document types. These are the top-level of the document
	 * type/subtype/classification hierarchy.
	 *
	 * @return DocumentTypesResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/types</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-document-types' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-document-types</a>
	 * @vapil.request <pre>
	 * DocumentTypesResponse allTypesResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveAllDocumentTypes();</pre>
	 * @vapil.response <pre>System.out.println(allTypesResponse.getResponse());
	 *
	 * for (DocumentTypesResponse.DocumentType documentType : allTypesResponse.getTypes()) {
	 *   DocumentTypeResponse typeResponse = vaultClient.newRequest(DocumentRequest.class).retrieveDocumentType(documentType.getName());
	 *   System.out.println(typeResponse.getResponse());
	 *
	 *   if (typeResponse.getSubtypes() != null) {
	 *     for (DocumentTypeResponse.DocumentSubtype documentSubtype : typeResponse.getSubtypes()) {
	 *       DocumentSubtypeResponse subtypeResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 					.retrieveDocumentSubtype(documentType.getName(), documentSubtype.getName());
	 *       System.out.println(subtypeResponse.getResponse());
	 *
	 *       if (subtypeResponse.getClassifications() != null) {
	 *         for (DocumentSubtypeResponse.DocumentClassification documentClassification : subtypeResponse.getClassifications()) {
	 *           DocumentClassificationResponse classificationResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 					.retrieveDocumentClassification(
	 *           documentType.getName(),
	 *           documentSubtype.getName(),
	 *           documentClassification.getName());
	 *           System.out.println(classificationResponse.getResponse());
	 *           }
	 *         }
	 *       }
	 *     }
	 *   }
	 * }</pre>
	 */
	public DocumentTypesResponse retrieveAllDocumentTypes() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_TYPES);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentTypesResponse.class);
	}

	/**
	 * <b>Retrieve Document Type</b>
	 * <p>
	 * Retrieve all metadata from a document type, including all of its subtypes (when available).
	 *
	 * @param type document type (api name)
	 * @return DocumentTypeResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/types/{type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-type' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-type</a>
	 * @vapil.request <pre>
	 * DocumentTypeResponse resp  = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveCommonDocumentFields();</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public DocumentTypeResponse retrieveDocumentType(String type) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_TYPE).replace("{type}", type);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentTypeResponse.class);
	}

	/**
	 * <b>Retrieve Document Subtype</b>
	 * <p>
	 * Retrieve all metadata from a document subtype, including all of its classifications (when available).
	 *
	 * @param type    document type (api name)
	 * @param subType document subType (api name)
	 * @return DocumentSubtypeResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/types/{type}/subtypes/{subtype}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-subtype' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-subtype</a>
	 * @vapil.request <pre>
	 * DocumentSubtypeResponse resp = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentSubtype(documentType.getName(), documentSubtype.getName());</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public DocumentSubtypeResponse retrieveDocumentSubtype(String type, String subType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_SUBTYPE)
				.replace("{type}", type)
				.replace("{subtype}", subType);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentSubtypeResponse.class);
	}

	/**
	 * <b>Retrieve Document Classification</b>
	 *
	 * @param type           document type (api name)
	 * @param subType        document subType (api name)
	 * @param classification document classification (api name)
	 * @return DocumentClassificationResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/types/{type}/subtypes/{subtype}/classifications/{classification}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-classification' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-classification</a>
	 * @vapil.request <pre>
	 * DocumentClassificationResponse resp = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentClassification(type,subtype,classification);</pre>
	 * @vapil.response <pre>
	 * System.out.println(classificationResponse.getResponse());</pre>
	 */
	public DocumentClassificationResponse retrieveDocumentClassification(String type, String subType, String classification) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_CLASSIFICATION)
				.replace("{type}", type)
				.replace("{subtype}", subType)
				.replace("{classification}", classification);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentClassificationResponse.class);
	}

	/*
	 *
	 * Retrieve Document
	 *
	 */

	/**
	 * <b>Retrieve All Documents</b>
	 * <p>
	 * Retrieve the latest version of all documents and binders
	 * <p>
	 * Various properties can filter the list of returned documents
	 * <ul>
	 * <li>namedFilter=My Documents	Retrieves only documents which you have created.</li>
	 * <li>namedFilter=Favorites	Retrieves only documents which you have marked as favorites in the library.</li>
	 * <li>namedFilter=Recent Documents	Retrieves only documents which you have recently accessed.</li>
	 * <li>namedFilter=Cart	Retrieves only documents in your cart.</li>
	 * <li>versionsScope=contents	Searches only within the document content.</li>
	 * <li>versionsScope=all	Searches both within the document content and searchable document fields.</li>
	 * <li>versionsScope=all	Retrieves all document versions, rather than only the latest version.</li>
	 * <li>searchKeyword={keyword}	Search for documents based on a {keyword} in searchable document fields.</li>
	 * <li>limit</li>
	 * <li>sort</li>
	 * <li>start</li>
	 * </ul>
	 *
	 * @return DocumentsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-documents</a>
	 * @vapil.request <pre>
	 * DocumentsResponse resp = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveAllDocuments();</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public DocumentsResponse retrieveAllDocuments() {
		String url = vaultClient.getAPIEndpoint(URL_DOCS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (namedFilter != null) {
			request.addQueryParam("named_filter", namedFilter.toString());
		}

		if (scope != null) {
			request.addQueryParam("scope", scope.toString());
		}

		if (versionsScope != null && versionsScope.getValue().equals(VersionsScope.ALL)) {
			request.addQueryParam("versionscope", versionsScope.toString());
		}

		if (searchKeyword != null) {
			request.addQueryParam("search", searchKeyword);
		}

		if (limit != null) {
			request.addQueryParam("limit", limit);
		}

		if (sort != null) {
			request.addQueryParam("sort", sort);
		}

		if (start != null) {
			request.addQueryParam("start", start);
		}


		return send(HttpMethod.GET, request, DocumentsResponse.class);
	}

	/**
	 * <b>Retrieve Document</b>
	 * <p>
	 * Retrieve all metadata from a single document
	 *
	 * @param docId The Document Id
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document</a>
	 * @vapil.request <pre>
	 * DocumentResponse resp = vaultClient.newRequest(DocumentRequest.class).retrieveDocument(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * Document responseDoc = resp.getDocument();
	 * System.out.println("Name = " + responseDoc.getName());
	 * System.out.println("Title = " + responseDoc.getTitle());
	 *
	 * // Get a custom property
	 * System.out.println("Custom property custom__c = " + responseDoc.get("custom__c"));
	 *
	 * System.out.println("Renditions " + documentResponse.getRenditions().getViewableRendition());
	 *
	 * for (DocumentResponse.Version v : documentResponse.getVersions())
	 * System.out.println("Version = " + v.getNumber() + " " + v.getValue());
	 *
	 * for (DocumentResponse.Version v : resp.getVersions())
	 * System.out.println("Version = " + v.getNumber() + " " + v.getValue());</pre>
	 */
	public DocumentResponse retrieveDocument(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC)
				.replace("{doc_id}", Integer.valueOf(docId).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentResponse.class);
	}

	/**
	 * <b>Retrieve Document Versions</b>
	 * <p>
	 * Retrieve all versions of a document.
	 *
	 * @param docId The Document Id
	 * @return DocumentVersionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-versions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-versions</a>
	 * @vapil.request <pre>
	 * DocumentResponse resp = vaultClient.newRequest(DocumentRequest.class).retrieveDocumentVersion(docId, 0, 1);</pre>
	 * @vapil.response <pre>
	 * See {@link #retrieveDocument} for example responses</pre>
	 */
	public DocumentVersionResponse retrieveDocumentVersions(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSIONS)
				.replace("{doc_id}", Integer.valueOf(docId).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentVersionResponse.class);
	}

	/**
	 * <b>Retrieve Document Version</b>
	 * <p>
	 * Retrieve all fields and values configured on a document version.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion document major version number
	 * @param minorVersion document minor version number
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-version' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-version</a>
	 * @vapil.request <pre>
	 * DocumentResponse versionResponse  = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentVersion(docId, 0, 1);</pre>
	 * @vapil.response <pre>
	 * See {@link #retrieveDocument} for example responses</pre>
	 */
	public DocumentResponse retrieveDocumentVersion(int docId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSION)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion));
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentResponse.class);
	}

	/**
	 * <b>Download Document File</b>
	 *
	 * @param docId The Document Id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-file</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentRequest.class).downloadDocumentFile(docId);</pre>
	 * @vapil.response <pre>if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Doc content length: " + response.getBinaryContent().length);</pre>
	 * }
	 */
	public VaultResponse downloadDocumentFile(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_FILE)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (lockDocument != null && lockDocument) {
			request.addQueryParam("lockDocument", "true");
		}

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download Document Version File</b>
	 *
	 * @param docId        The Document Id
	 * @param majorVersion document major version number
	 * @param minorVersion document minor version number
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-version-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-version-file</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.downloadDocumentVersionFile(docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Created doc content length: " + response.getBinaryContent().length);
	 * }</pre>
	 */
	public VaultResponse downloadDocumentVersionFile(int docId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSION_FILE)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download Document Version Thumbnail File</b>
	 *
	 * @param docId        The Document Id
	 * @param majorVersion document major version number
	 * @param minorVersion document minor version number
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/thumbnail</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-document-version-thumbnail-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-document-version-thumbnail-file</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.downloadDocumentVersionThumbnailFile(docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Created doc content length: " + response.getBinaryContent().length);
	 * }</pre>
	 */
	public VaultResponse downloadDocumentVersionThumbnailFile(int docId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSION_THUMBNAIL)
				.replace("{doc_id}", Integer.valueOf(docId).toString())
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/*
	 *
	 * Create Documents
	 *
	 */

	/**
	 * <b>Create Single Document</b>
	 * <p>
	 * Create a single document by uploading the specified file.<p>
	 * Use either the local path or binaryfile setters.
	 * <p>
	 * <b>Use bulk for multiple documents! See {@link #createMultipleDocuments()}. </b>
	 *
	 * @param doc Document metadata to create
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * Document doc = new Document();
	 *
	 * doc.setName("VAPIL Single Document");
	 * doc.setLifecycle("General Lifecycle");
	 * doc.setType("General");
	 * doc.setTitle("Test Upload VAPIL");
	 *
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setInputPath(filePath)
	 * 					.createSingleDocument(doc);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Created doc id: " + response.getDocument().getId());</pre>
	 */
	public DocumentResponse createSingleDocument(Document doc) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
		request.setBodyParams(doc.toMap());

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}

	/**
	 * <b>Create Documents from Templates</b>
	 *
	 * @param doc          Document to create
	 * @param fromTemplate The Template to create from
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class).createDocumentFromTemplate(doc, "template_name__c");</pre>
	 * @vapil.response <pre>System.out.println("Created doc id: " + response);</pre>
	 */
	public DocumentResponse createDocumentFromTemplate(Document doc, String fromTemplate) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());
		request.addBodyParam("fromTemplate", fromTemplate);

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}

	/**
	 * <b>Create Content Placeholder Documents</b>
	 *
	 * @param doc Document to create
	 * @return DocumentCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class).createContentPlaceholderDocument(doc);</pre>
	 * @vapil.response <pre>System.out.println("Created doc id: " + response.getDocument().getId());</pre>
	 */
	public DocumentResponse createContentPlaceholderDocument(Document doc) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}

	/**
	 * <b>Create Unclassified Documents</b>
	 *
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setInputPath(filePath)
	 * 					.createUnclassifiedDocument();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Created doc id: " + response.getDocument().getId());</pre>
	 */
	public DocumentResponse createUnclassifiedDocument() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		// Add the file, which leverages java.nio.file.Paths for later setting HTTP multi-form boundary
		request.addBodyParam("type__v", "Undefined");
		request.addBodyParam("lifecycle__v", "Unclassified");

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}

	/**
	 * <b>Create Crosslink Document</b>
	 * <p>
	 * When creating a CrossLink document, you must include all document fields that are
	 * required for the specified document type/subtype/classification and no file is uploaded.
	 * You must also specify the vault ID and document ID for the source document which will be
	 * bound to the new CrossLink document.
	 * <p>
	 * Source binding rules define which version of the source document will be
	 * bound to the CrossLink document. If not specified, this defaults to
	 * the Latest Steady State version.
	 *
	 * @param doc                   Document to create
	 * @param source_vault_id__v    The source Vault Id
	 * @param source_document_id__v The source document id
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * Document doc = new Document();
	 * doc.setName("VAPIL CrossLink");
	 * doc.setLifecycle("General Lifecycle");
	 * doc.setType("General");
	 * doc.setTitle("VAPIL CrossLink - latest version");
	 *
	 * <i>Example 1 - Create Crosslink</i>
	 * response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setSourceBindingRule(DocumentRequest.CrosslinkBinding.LATEST)
	 * 				.createCrossLinkDocument(doc, vaultId, crosslinkDocId);
	 *
	 * <i>Example 2 - Create Crosslink Bound to Latest Version</i>
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.createCrossLinkDocument(doc, vaultId, crosslinkDocId);
	 *
	 * <i>Example 3 - Create Crosslink Bound to Specific Document Version</i>
	 * DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setSourceBindingRule(DocumentRequest.CrosslinkBinding.SPECIFIC)
	 * 				.setBoundSourceMajorVersion(1)
	 * 				.setBoundSourceMinorVersion(0)
	 * 				.createCrossLinkDocument(doc, vaultId, crosslinkDocId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Created doc id: " + response.getDocument().getId());</pre>
	 */
	public DocumentResponse createCrossLinkDocument(Document doc, int source_vault_id__v, int source_document_id__v) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());
		request.addBodyParam("source_vault_id__v", source_vault_id__v);
		request.addBodyParam("source_document_id__v", source_document_id__v);

		if (this.sourceBindingRule != null) {
			request.addBodyParam("source_binding_rule__v", sourceBindingRule.getValue());
		} else {
			request.addBodyParam("source_binding_rule__v", CrosslinkBinding.LATESTSTEADYSTATE.getValue());

			log.info("defaulting source_binding_rule__v=" + CrosslinkBinding.LATESTSTEADYSTATE.getValue());
		}


		if (this.boundSourceMajorVersion != null) {
			request.addBodyParam("bound_source_major_version__v", boundSourceMajorVersion.toString());
		} else if (CrosslinkBinding.SPECIFIC.equals(sourceBindingRule)) {
			log.error("bound_source_major_version__v is required");
		}

		if (this.boundSourceMinorVersion != null) {
			request.addBodyParam("bound_source_minor_version__v", boundSourceMinorVersion.toString());
		} else if (CrosslinkBinding.SPECIFIC.equals(sourceBindingRule)) {
			log.error("bound_source_minor_version__v is required");
		}

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}


	/**
	 * <b>Create Multiple Documents</b>
	 * <p>
	 * This endpoint allows you to create multiple documents at once with a CSV input file.
	 * <p>
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 500.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#create-multiple-documents</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setInputPath(csvFilePath)
	 * 					.createMultipleDocuments();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
	 * 				.createMultipleDocuments();</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());</pre>
	 */
	public DocumentBulkResponse createMultipleDocuments() {
		return bulkDocument(HttpMethod.POST, URL_DOCS_BATCH);
	}


	/*
	 *
	 * Update Documents
	 *
	 */

	/**
	 * <b>Update Single Document</b>
	 * <p>
	 * Update editable field values on the latest version of a single document.
	 * To update past document versions, see Update Document Version.
	 * Note that this endpoint does not allow you to update the archive__v field.
	 * <p>
	 * <b>Use bulk for multiple documents! See {@link #updateMultipleDocuments()}. </b>
	 *
	 * @param doc Document metadata to update
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#update-single-document</a>
	 */
	public DocumentResponse updateSingleDocument(Document doc) {
		String url = vaultClient.getAPIEndpoint(URL_DOC)
				.replace("{doc_id}", Integer.toString(doc.getId()));
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		Map<String, Object> bodyParams = doc.toMap();
		if (bodyParams.containsKey("id")) bodyParams.remove("id");
		request.setBodyParams(bodyParams);

		if (migrationMode != null) {
			request.addHeaderParam(HTTP_HEADER_VAULT_MIGRATION_MODE, migrationMode);
		}

		return send(HttpMethod.PUT, request, DocumentResponse.class);
	}

	/**
	 * <b>Update Multiple Documents</b>
	 * <p>
	 * Bulk update editable field values on multiple documents.
	 * You can only update the latest version of each document.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#update-multiple-documents</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setInputPath(csvFilePath)
	 * 					.updateMultipleDocuments();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
	 * 				.updateMultipleDocuments();</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());</pre>
	 */
	public DocumentBulkResponse updateMultipleDocuments() {
		return bulkDocument(HttpMethod.PUT, URL_DOCS_BATCH);
	}

	/**
	 * <b>Reclassify Document</b>
	 * <p>
	 * Reclassify allows you to change the document type of an existing document
	 * or assign a document type to an unclassified document.
	 * A document is the combination of the type__v, subtype__v, and classification__v
	 * fields on a document. When you reclassify, Vault may add or remove certain fields
	 * on the document. You can only reclassify the latest version of a document
	 * and only one document at a time. The API does not currently support bulk reclassify.
	 *
	 * @param doc Document metadata to reclassify
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#reclassify-document' target='_blank'>https://developer.veevavault.com/api/23.1/#reclassify-document</a>
	 */
	public DocumentResponse reclassifySingleDocument(Document doc) {
		doc.set("reclassify", "true");
		return updateSingleDocument(doc);
	}

	/**
	 * <b>Reclassify Multiple Documents</b>
	 * <p>
	 * Reclassify documents in bulk. Reclassify allows you to change the document type of existing documents
	 * or assign document types to unclassified documents.
	 * A document is the combination of the type__v, subtype__v, and classification__v
	 * fields on a document. When you reclassify, Vault may add or remove certain fields
	 * on the document. You can only reclassify the latest version of a document
	 * you cannot reclassify a checked out document.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/batch/actions/reclassify</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.2/#reclassify-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.2/#reclassify-multiple-documents</a>
	 */
	public DocumentBulkResponse reclassifyMultipleDocuments() {

		return bulkDocument(HttpMethod.PUT, URL_DOC_RECLASSIFY);
	}

	/**
	 * <b>Update Document Version</b>
	 * <p>
	 * Update editable field values on a specific version of a document.
	 *
	 * @param doc          Document metadata to update
	 * @param majorVersion document major version number
	 * @param minorVersion document minor version number
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-document-version' target='_blank'>https://developer.veevavault.com/api/23.1/#update-document-version</a>
	 */
	public DocumentResponse updateDocumentVersion(Document doc, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSION)
				.replace("{doc_id}", Integer.toString(doc.getId()))
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion));
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		Map<String, Object> bodyParams = doc.toMap();
		if (bodyParams.containsKey("id")) bodyParams.remove("id");
		request.setBodyParams(bodyParams);

		return send(HttpMethod.PUT, request, DocumentResponse.class);
	}

	/**
	 * <b>Create Multiple Document Versions</b>
	 * <p>
	 * Your vault must be in Migration Mode.
	 * The maximum CSV input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 500.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/versions/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#create-multiple-documents</a>
	 */
	public DocumentBulkResponse createMultipleDocumentVersions() {
		return bulkDocument(HttpMethod.POST, URL_DOCS_BATCH_VERSIONS);
	}

	/**
	 * <b>Create Single Document Version</b>
	 * <p>
	 * Add a new draft version of an existing document.
	 * You can choose to either use the existing source file,
	 * or a new source file. These actions will increase
	 * the document’s minor version number. This is analogous
	 * to using the Create Draft action in the UI.
	 * <b>Use bulk for multiple documents!</b>
	 *
	 * @param docId           Document id
	 * @param createDraftType latestContent or uploadedContent
	 * @return DocumentCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#create-single-document</a>
	 * @vapil.request <pre>
	 * DocumentResponse response =vaultClient.newRequest(DocumentRequest.class)
	 * 				.createSingleDocumentVersion(docId, DocumentRequest.CreateDraftType.LATESTCONTENT);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 * 		System.out.println("Created doc id: " + response.getDocument().getId());
	 * }</pre>
	 */
	public DocumentResponse createSingleDocumentVersion(int docId, CreateDraftType createDraftType) {
		String url = vaultClient.getAPIEndpoint(URL_DOC)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
		if (this.suppressRendition != null) {
			request.getHeaderParams().put("suppressRendition=", suppressRendition.toString().toUpperCase());
		}
		request.addBodyParamMultiPart("createDraft", createDraftType.getValue());

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		if (this.description != null) {
			request.addBodyParamMultiPart("description__v", description);
		}

		return send(HttpMethod.POST, request, DocumentResponse.class);
	}

	/*
	 *
	 * Delete Documents
	 *
	 */

	/**
	 * <b>Delete Single Document</b>
	 * <p>
	 * Delete all versions of a document, including all source files and viewable renditions.
	 *
	 * @param docid The Document Id
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{document_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-single-document' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-single-document</a>
	 */
	public DocumentResponse deleteSingleDocument(int docid) {
		String url = vaultClient.getAPIEndpoint(URL_DOC)
				.replace("{doc_id}", Integer.valueOf(docid).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, DocumentResponse.class);
	}

	/**
	 * <b>Delete Multiple Documents</b>
	 * <p>
	 * Delete all versions of multiple documents, including all source files and viewable renditions.
	 * <p>
	 * The maximum input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 500.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-multiple-documents</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setInputPath(csvFilePath)
	 * 					.deleteMultipleDocuments();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary input</i>
	 * DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
	 * 				.deleteMultipleDocuments();</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());</pre>
	 */
	public DocumentBulkResponse deleteMultipleDocuments() {
		return bulkDocument(HttpMethod.DELETE, URL_DOCS_BATCH);
	}

	/**
	 * <b>Delete Single Document Version</b>
	 * <p>
	 * Delete a specific version of a document, including the version’s source file
	 * and viewable rendition. Other versions of the document remain unchanged.
	 *
	 * @param docId        Document metadata to update
	 * @param majorVersion document major version number
	 * @param minorVersion document minor version number
	 * @return DocumentResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-single-document-version' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-single-document-version</a>
	 */
	public DocumentResponse deleteSingleDocumentVersion(int docId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_VERSION)
				.replace("{doc_id}", Integer.toString(docId))
				.replace("{major_version}", Integer.toString(majorVersion))
				.replace("{minor_version}", Integer.toString(minorVersion));
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, DocumentResponse.class);
	}

	/**
	 * <b>Delete Multiple Document Versions</b>
	 * <p>
	 * Delete a specific version of multiple documents,
	 * including the version’s source file and viewable rendition.
	 * <p>
	 * The maximum input file size is 1GB.
	 * The values in the input must be UTF-8 encoded.
	 * CSVs must follow the standard format.
	 * The maximum batch size is 500.
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/versions/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-multiple-documents' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-multiple-documents</a>
	 */
	public DocumentBulkResponse deleteMultipleDocumentVersions() {
		return bulkDocument(HttpMethod.DELETE, URL_DOCS_BATCH_VERSIONS);
	}

	/**
	 * <b>Retrieve Deleted Document IDs</b>
	 * <p>
	 * Retrieve IDs of documents deleted within the past 30 days.
	 * <p>
	 * After documents and document versions are deleted, their IDs remain available for retrieval for 30 days.
	 * After that, they cannot be retrieved. This request supports optional parameters to narrow the results
	 * to a specific date and time range within the past 30 days.
	 * <p>
	 * To completely restore a document deleted within the last 30 days, contact Veeva support.
	 *
	 * @return DocumentDeletionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/deletions/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-deleted-document-ids' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-deleted-document-ids</a>
	 * @vapil.request <pre>
	 * DocumentDeletionResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveDeletedDocumentIds();</pre>
	 * @vapil.response <pre>if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("# deleted docs: " + response.getResponseDetails().getTotal());
	 * }</pre>
	 */
	public DocumentDeletionResponse retrieveDeletedDocumentIds() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_DELETIONS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		if(this.limit != null) {
			request.addQueryParam("limit", this.limit);
		}
		if(this.offset != null) {
			request.addQueryParam("offset", this.offset);
		}
		if(this.startDate != null) {
			request.addQueryParam("start_date", this.startDate);
		}
		if(this.endDate != null) {
			request.addQueryParam("end_date", this.endDate);
		}
		return send(HttpMethod.GET, request, DocumentDeletionResponse.class);
	}

	/**
	 * <b>Retrieve Deleted Document IDs (By Page)</b>
	 * <p>
	 * Retrieve IDs of documents deleted using the previous_page or next_page parameter of a previous request
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return DocumentDeletionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/deletions/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-deleted-document-ids' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-deleted-document-ids</a>
	 * @vapil.request <pre>
	 * DocumentDeletionResponse paginatedResponse = paginatedResponse = vaultClient.newRequest(JobRequest.class)
	 * 		.retrieveDeletedDocumentIdsByPage(response.getResponseDetails().getNextPage());</pre>
	 * @vapil.response <pre>System.out.println(paginatedResponse.getResponseStatus())</pre>;
	 */
	public DocumentDeletionResponse retrieveDeletedDocumentIdsByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, DocumentDeletionResponse.class);
	}

	/*
	 *
	 * Document Locks
	 *
	 */

	/**
	 * <b>Retrieve Document Lock Metadata</b>
	 *
	 * @return MetaDataDocumentLockResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/documents/lock</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-lock-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-lock-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataDocumentLockResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentLockMetadata();</pre>
	 * @vapil.response <pre>
	 * if (response.isSuccessful()) {
	 *   for (DocumentLock documentLock : response.getProperties()) {
	 *     System.out.println("Document Lock Properties: " + documentLock.getName());
	 *   }
	 * }</pre>
	 */
	public MetaDataDocumentLockResponse retrieveDocumentLockMetadata() {
		String url = vaultClient.getAPIEndpoint(URL_DOC_LOCK_METADATA);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, MetaDataDocumentLockResponse.class);
	}

	/**
	 * <b>Create Document Lock</b>
	 * <p>
	 * A document lock is analogous to checking out a document
	 * but without the file attached in the response for download.
	 *
	 * @param docId document id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/lock</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-document-lock' target='_blank'>https://developer.veevavault.com/api/23.1/#create-document-lock</a>
	 * @vapil.request <pre>
	 * VaultResponse lockCreateResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 				.createDocumentLock(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Document Lock Created: " + lockCreateResponse.getResponseStatus());</pre>
	 */
	public VaultResponse createDocumentLock(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_LOCK)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Retrieve Document Lock</b>
	 *
	 * @param docId document id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/lock</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-lock' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-lock</a>
	 * @vapil.request <pre>
	 * DocumentLockResponse lockResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentLock(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Document Lock By: " + lockResponse.getLock().getLockedBy());
	 * System.out.println("Document Lock Date: " + lockResponse.getLock().getLockedDate());</pre>
	 */
	public DocumentLockResponse retrieveDocumentLock(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_LOCK)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentLockResponse.class);
	}

	/**
	 * <b>Deleting Document Lock</b>
	 * <p>
	 * Deleting a document lock is analogous to undoing check out of a document.
	 *
	 * @param docId document id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/documents/{doc_id}/lock</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-document-lock' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-document-lock</a>
	 * @vapil.request <pre>
	 * VaultResponse lockDeleteResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 				.deleteDocumentLock(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Document Lock Deleted: " + lockDeleteResponse.getResponseStatus());</pre>
	 */
	public VaultResponse deleteDocumentLock(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_LOCK)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/*
	 *
	 * Export documents
	 *
	 */

	/**
	 * <b>Export Documents</b>
	 * <p>
	 * Use this request to export a set of document to your vault’s FTP staging server.
	 *
	 * @param includeSource      indicates to include source content files
	 * @param includeRenditions  indicates to include renditions
	 * @param includeAllVersions indicates to include all versions
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/batch/actions/fileextract</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-documents-1' target='_blank'>https://developer.veevavault.com/api/23.1/#export-documents-1</a>
	 * @vapil.request <pre>
	 * JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setInputPath(filePath)
	 * 			.exportDocuments(true, true, true);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Job Id: " + response.getJobId());
	 * }</pre>
	 */
	public JobCreateResponse exportDocuments(boolean includeSource, boolean includeRenditions, boolean includeAllVersions) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_EXTRACT);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		request.addQueryParam("source", includeSource);
		request.addQueryParam("renditions", includeRenditions);
		request.addQueryParam("allversions", includeAllVersions);


		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestString);

		if (binaryFile != null)
			request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, binaryFile.getBinaryContent());

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Export Document Versions</b>
	 * <p>
	 * Export a specific set of document versions to your vault’s FTP staging server.
	 * The files you export go to the u{userID} folder, regardless of your security profile.
	 *
	 * @param includeSource     indicates to include source content files
	 * @param includeRenditions indicates to include renditions
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/versions/batch/actions/fileextract</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-document-versions' target='_blank'>https://developer.veevavault.com/api/23.1/#export-document-versions</a>
	 * @vapil.request <pre>
	 * JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.exportDocumentVersions(true, true);</pre>
	 * @vapil.response <pre>
	 * if (response != null &amp;&amp; response.isSuccessful()) {
	 *   System.out.println("Job Id: " + response.getJobId());
	 * }</pre>
	 */
	public JobCreateResponse exportDocumentVersions(boolean includeSource, boolean includeRenditions) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_EXTRACT_VERSIONS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		request.addQueryParam("source", includeSource);
		request.addQueryParam("renditions", includeRenditions);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestString);

		if (binaryFile != null)
			request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, binaryFile.getBinaryContent());

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Retrieve Document Export Results</b>
	 * <p>
	 * After submitting a request to export documents from your vault, you can query your vault
	 * to determine the results of the request.
	 * <p>
	 * Before submitting this request:
	 * <p>
	 * You must have previously requested a document export job (via the API) which is no longer active.<br>
	 * You must have a valid job_id value (retrieved from the document export binder request above).<br>
	 * You must be a Vault Owner, System Admin or the user who initiated the job.<br>
	 *
	 * @param jobId job id
	 * @return DocumentExportResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/batch/actions/fileextract/{jobid}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-export-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-export-results</a>
	 * @vapil.request <pre>
	 * DocumentExportResponse exportResponse = vaultClient.newRequest(DocumentRequest.class)
	 * 				.retrieveDocumentExportResults(jobId);</pre>
	 * @vapil.response <pre>
	 * if (exportResponse != null &amp;&amp; exportResponse.isSuccessful()) {
	 *   List&lt;DocumentExportResponse.ExportedDocument&gt; exportedDocumentList = exportResponse.getData();
	 *   if (exportedDocumentList != null) {
	 *     for (DocumentExportResponse.ExportedDocument exportedDocument : exportedDocumentList) {
	 *       System.out.println("id: " + exportedDocument.getId());
	 *       System.out.println("major version: " + exportedDocument.getMajorVersionNumber());
	 *       System.out.println("minor version: " + exportedDocument.getMinorVersionNumber());
	 *       System.out.println("File: " + exportedDocument.getFile());
	 *     }
	 *   }
	 * }</pre>
	 */
	public DocumentExportResponse retrieveDocumentExportResults(int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_EXTRACT_RESULTS)
				.replace("{jobid}", Integer.valueOf(jobId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, DocumentExportResponse.class);
	}

	/**
	 * <b>Document Tokens</b>
	 * <p>
	 * The Vault Document Tokens API allows you to generates document access tokens
	 * needed by the external viewer to view documents outside of Vault.
	 *
	 * @param docIds list of document ids
	 * @return DocumentTokenResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/tokens</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#document-tokens' target='_blank'>https://developer.veevavault.com/api/23.1/#document-tokens</a>
	 * @vapil.request <pre>
	 * DocumentTokenResponse response = vaultClient.newRequest(DocumentRequest.class)
	 * 					.setExpiryDateOffset(10)
	 * 					.setDownloadOption(DocumentRequest.DownloadOption.PDF)
	 * 					.setTokenGroup("example")
	 * 					.documentTokens(docIds);</pre>
	 * @vapil.response <pre>System.out.println(response.getResponse());</pre>
	 */
	public DocumentTokenResponse documentTokens(List<Integer> docIds) {
		String url = vaultClient.getAPIEndpoint(URL_DOC_TOKENS);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addBodyParam("docIds", docIds.stream().map(String::valueOf)
				.collect(Collectors.joining(",")));

		if (expiryDateOffset != null) {
			request.addBodyParam("expiryDateOffset", expiryDateOffset);
		}

		if (downloadOption != null) {
			request.addBodyParam("downloadOption", downloadOption.getValue());
		}

		if (channel != null) {
			request.addBodyParam("channel", channel);
		}

		if (tokenGroup != null) {
			request.addBodyParam("tokenGroup", tokenGroup);
		}

		if (steadyState != null) {
			request.addBodyParam("steadyState", steadyState);
		}

		return send(HttpMethod.POST, request, DocumentTokenResponse.class);
	}


	/*
	 *
	 * private bulk document method
	 *
	 */

	/**
	 * handles all CREATE, UPDATE, AND DELETE batch methods
	 *
	 * @return DocumentBulkResponse
	 * @vapil.api <pre>
	 * /api/{version}/objects/documents/batch</pre>
	 */
	private DocumentBulkResponse bulkDocument(HttpMethod httpMethod, String url) {
		url = url == null ? vaultClient.getAPIEndpoint(URL_DOCS_BATCH) : vaultClient.getAPIEndpoint(url);

		String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		if (headerContentType != null)
			contentType = headerContentType;

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

		if (idParam != null && !idParam.isEmpty())
			request.addQueryParam(ID_PARAM, idParam);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(contentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(contentType, requestString);

		if (binaryFile != null)
			request.addBinary(contentType, binaryFile.getBinaryContent());

		if (migrationMode)
			request.addHeaderParam(HTTP_HEADER_VAULT_MIGRATION_MODE, "true");

		return send(httpMethod, request, DocumentBulkResponse.class);
	}

	/*
	 *
	 * Request constants
	 *
	 */


	/**
	 * Crosslink binding rules enum for doc field picklist source_binding_rule__v
	 */
	public enum CrosslinkBinding {
		LATEST("Latest version"),
		LATESTSTEADYSTATE("Latest Steady State version"), //default
		SPECIFIC("Specific document version");

		private String value;

		CrosslinkBinding(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Create document from latestcontent or uploadedcontent
	 */
	public enum CreateDraftType {
		LATESTCONTENT("latestContent"),
		UPLOADEDCONTENT("uploadedContent");

		private String value;

		CreateDraftType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Download option for Document Token
	 */
	public enum DownloadOption {
		BOTH("both"),
		NONE("none"),
		PDF("PDF"),
		SOURCE("source");

		private String value;

		DownloadOption(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Retrieve all document named filter
	 */
	public enum NamedFilter {
		CART("Cart"),
		FAVORITES("Favorites"),
		MYDOCUMENTS("My Documents"),
		RECENTDOCUMENTS("Recent Documents");

		private String value;

		NamedFilter(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Retrieve all document scope
	 */
	public enum Scope {
		ALL("all"),
		CONTENTS("contents"),
		MYDOCUMENTS("My Documents"),
		RECENTDOCUMENTS("Recent Documents");

		private String value;

		Scope(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Retrieve all document versions scope
	 */
	public enum VersionsScope {
		ALL("all"),
		LATESTVERSION("latestversion");

		private String value;

		VersionsScope(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
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
	public DocumentRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify the Cross-Link Bound Source Major Version
	 *
	 * @param boundSourceMajorVersion Required when the source_binding_rule__v is set to Specific Document version
	 * @return The Request
	 */
	public DocumentRequest setBoundSourceMajorVersion(Integer boundSourceMajorVersion) {
		this.boundSourceMajorVersion = boundSourceMajorVersion;
		return this;
	}

	/**
	 * Specify the Cross-Link Bound Source Minor Version
	 *
	 * @param boundSourceMinorVersion Required when the source_binding_rule__v is set to Specific Document version
	 * @return The Request
	 */
	public DocumentRequest setBoundSourceMinorVersion(Integer boundSourceMinorVersion) {
		this.boundSourceMinorVersion = boundSourceMinorVersion;
		return this;
	}

	/**
	 * Include the channel request parameter set to the website object record id value
	 * that corresponds to the distribution channel where the document is being made available.
	 * If no website record is specified, Vault will assume the request is for Approved Email.
	 *
	 * @param channel channel
	 * @return The Request
	 */
	public DocumentRequest setChannel(String channel) {
		this.channel = channel;
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public DocumentRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public DocumentRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Specify description
	 *
	 * @param description description__v
	 * @return The Request
	 */
	public DocumentRequest setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Include the downloadOption request parameter set to PDF, source, both, or none.
	 * These allow users viewing the document to be able to download a PDF version
	 * or source version (Word™, PowerPoint™, etc.) of the document. If not specified,
	 * the download options default to those set on each document.
	 *
	 * @param downloadOption downloadOption for document tokens
	 * @return The Request
	 */
	public DocumentRequest setDownloadOption(DownloadOption downloadOption) {
		this.downloadOption = downloadOption;
		return this;
	}

	/**
	 * Specify number of days after which the tokens will expire
	 * and the documents will no longer be available in the viewer.
	 * If not specified, the tokens will expire after 10 years by default.
	 *
	 * @param expiryDateOffset description__v
	 * @return The Request
	 */
	public DocumentRequest setExpiryDateOffset(Integer expiryDateOffset) {
		this.expiryDateOffset = expiryDateOffset;
		return this;
	}

	/**
	 * Specify an UPSERT operation via the idParam
	 *
	 * @param idParam External Id field API name for the UPSERT
	 * @return The Request
	 */
	public DocumentRequest setIdParam(String idParam) {
		this.idParam = idParam;
		return this;
	}

	/**
	 * Set the limit of documents returned by retrieve all documents
	 *
	 * @param limit max number of documents returned
	 * @return The Request
	 */
	public DocumentRequest setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify to check out the document before retrieval
	 *
	 * @param lockDocument whether or not to lock the document
	 * @return The Request
	 */
	public DocumentRequest setLockDocument(Boolean lockDocument) {
		this.lockDocument = lockDocument;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public DocumentRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Set named filter of documents returned by retrieve all documents
	 *
	 * @param namedFilter named filter for documents
	 * @return The Request
	 */
	public DocumentRequest setScopeFilter(NamedFilter namedFilter) {
		this.namedFilter = namedFilter;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public DocumentRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

	/**
	 * Set scope of documents returned by retrieve all documents
	 *
	 * @param scope scope of documents
	 * @return The Request
	 */
	public DocumentRequest setScopeFilter(Scope scope) {
		this.scope = scope;
		return this;
	}

	/**
	 * Search keyword for documents returned by retrieve all documents
	 *
	 * @param keyword keyword to search for
	 * @return The Request
	 */
	public DocumentRequest setSearchKeyword(String keyword) {
		this.searchKeyword = keyword;
		return this;
	}

	/**
	 * Sort for documents returned by retrieve all documents
	 *
	 * @param sort order by value
	 * @return The Request
	 */
	public DocumentRequest setSort(String sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * Specify the Cross-Link Source Binding Rule
	 *
	 * @param sourceBindingRule Possible values are Latest version, Latest Steady State version, or Specific Document version
	 * @return The Request
	 */
	public DocumentRequest setSourceBindingRule(CrosslinkBinding sourceBindingRule) {
		this.sourceBindingRule = sourceBindingRule;
		return this;
	}

	/**
	 * Set the starting position of documents returned by retrieve all documents
	 *
	 * @param start starting position
	 * @return The Request
	 */
	public DocumentRequest setStart(Integer start) {
		this.start = start;
		return this;
	}

	/**
	 * Document Tokens:
	 *
	 * If set to true, Vault generates a token for the latest steady state version of a document.
	 * If you do not have view permission, or if a steady-state version does not exist,
	 * Vault returns an INVALID_STATE error. If omitted, the default value is false,
	 * and Vault generates a token for the latest version, regardless of state.
	 *
	 * @param steadyState true/false
	 * @return DocumentRequest
	 */
	public DocumentRequest setSteadyState(Boolean steadyState) {
		this.steadyState = steadyState;
		return this;
	}

	/**
	 * Specify whether to suppress rendition
	 *
	 * @param suppressRendition Required when the source_binding_rule__v is set to Specific Document version
	 * @return DocumentRequest
	 */
	public DocumentRequest setSuppressRendition(Boolean suppressRendition) {
		this.suppressRendition = suppressRendition;
		return this;
	}

	/**
	 * This only required if you want to group together generated tokens for multiple documents
	 * in order to display the documents being referenced in the same viewer. This value accepts
	 * strings of alphanumeric characters (a-z, A-Z, 0-9, and single consecutive underscores)
	 * up to 255 characters in length.
	 *
	 * @param tokenGroup name of group
	 * @return The Request
	 */
	public DocumentRequest setTokenGroup(String tokenGroup) {
		this.tokenGroup = tokenGroup;
		return this;
	}

	/**
	 * Set all versions or latest version of documents returned by retrieve all documents
	 *
	 * @param versionsScope scope for versions
	 * @return The Request
	 */
	public DocumentRequest setVersionScope(VersionsScope versionsScope) {
		this.versionsScope = versionsScope;
		return this;
	}

	/**
	 * Enable migration mode to create multiple document versions in bulk
	 * and perform manual assignment of documents numbers.
	 * If not specified, the create multiple document versions endpoint will fail.
	 * API user must have the Document Migration permission to use this header.
	 *
	 * @param migrationMode The source request as a string
	 * @return The Request
	 */
	public DocumentRequest setMigrationMode(boolean migrationMode) {
		this.migrationMode = migrationMode;
		return this;
	}

	/**
	 * Set the offset, used to paginate Retrieve Deleted Document IDs API
	 *
	 * @param offset The offset as an int
	 * @return The Request
	 */
	public DocumentRequest setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Set the start date, used to set start date for Retrieve Deleted Document IDs API
	 *
	 * @param startDate The start date as a string in the YYYY-MM-DDTHH:MM:SSZ format
	 * @return The Request
	 */
	public DocumentRequest setStartData(String startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * Set the end date, used to set end date Retrieve Deleted Document IDs API
	 *
	 * @param endDate The end date as a string in the YYYY-MM-DDTHH:MM:SSZ format
	 * @return The Request
	 */
	public DocumentRequest setEndData(String endDate) {
		this.endDate = endDate;
		return this;
	}
}
