/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

import java.util.Set;

/**
 * Document Annotation Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.2/#document-annotations">https://developer.veevavault.com/api/24.2/#document-annotations</a>
 */
public class DocumentAnnotationRequest extends VaultRequest<DocumentAnnotationRequest> {
    // API Endpoints
    private static final String URL_DOCUMENT_ANNOTATIONS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations";
    private static final String URL_DOCUMENT_ANNOTATION_ID = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/{annotation_id}";
    private static final String URL_DOCUMENT_ANNOTATIONS_FILE = "/objects/documents/{doc_id}/annotations/file";
    private static final String URL_DOCUMENT_VERSION_ANNOTATIONS_FILE = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/file";
    private static final String URL_DOCUMENT_ANNOTATION_REPLIES = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/{annotation_id}/replies";
    private static final String URL_DOCUMENT_ANNOTATIONS_BULK = "/objects/documents/annotations/batch";
    private static final String URL_DOCUMENT_DOCUMENT_ANCHORS = "/objects/documents/{doc_id}/anchors";
    private static final String URL_DOCUMENT_VERSION_NOTES = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/doc-export-annotations-to-csv";
    private static final String URL_DOCUMENT_VIDEO_ANNOTATIONS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/export-video-annotations";
    private static final String URL_DOCUMENT_ANNOTATION_TYPE_METADATA = "/metadata/objects/documents/annotations/types/{annotation_type}";
    private static final String URL_DOCUMENT_ANNOTATION_PLACEMARK_TYPE_METADATA = "/metadata/objects/documents/annotations/placemarks/types/{placemark_type}";
    private static final String URL_DOCUMENT_ANNOTATION_REFERENCE_TYPE_METADATA = "/metadata/objects/documents/annotations/references/types/{reference_type}";
    private static final String URL_DOCUMENT_ANNOTATION_REPLIES_ADD = "/objects/documents/annotations/replies/batch";

    // API Request Parameters
    private HttpRequestConnector.BinaryFile binaryFile;
    private String inputPath;
    private String requestString;
    private String headerContentType;
    private String outputPath;
    private Integer offset;
    private Integer limit;
    private String paginationId;
    private Set<String> annotationTypes;
    private String json;

    private DocumentAnnotationRequest() {
    }

    /**
     * <b>Export Document Annotations to PDF</b>
     * <p>
     * Export the latest version of any document, along with its annotations, as an annotated PDF.
     * This is equivalent to the Export Annotations action in the Vault document viewer UI.
     *
     * @param docId The document id field value.
     *
     * @return VaultResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/annotations/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-document-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-document-annotations</a>
     * @vapil.request <pre>
     * <i>Example 1 - Bytes</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.exportDocumentAnnotationsToPdf(docId);
     *
     * <i>Example 2 - File</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.setOutputPath(outputPath)
     * 		.exportDocumentAnnotationsToPdf(docId);
     * </pre>
     * @vapil.response <pre>
     * <i>Example 1 - Bytes</i>
     * byte[] bytes = response.getBinaryContent();
     *
     * <i>Example 2 - File</i>
     * System.out.println("Output file: " + response.getOutputFilePath());
     * </pre>
     */
    public VaultResponse exportDocumentAnnotationsToPdf(int docId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS_FILE)
                .replace("{doc_id}", Integer.valueOf(docId).toString()));

        if (outputPath != null) {
            return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * <b>Export Document Version Annotations to PDF</b>
     * <p>
     * Export a specific version of any document, along with its annotations, as an annotated PDF.
     * This is equivalent to the Export Annotations action in the Vault document viewer UI.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     *
     * @return VaultResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-document-version-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-document-version-annotations</a>
     * @vapil.request <pre>
     * <i>Example 1 - Bytes</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);
     *
     * <i>Example 2 - File</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.setOutputPath(outputPath)
     * 		.exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);
     * </pre>
     * @vapil.response <pre>
     * <i>Example 1 - Bytes</i>
     * byte[] bytes = response.getBinaryContent();
     *
     * <i>Example 2 - File</i>
     * System.out.println("Output file: " + response.getOutputFilePath());
     * </pre>
     */
    public VaultResponse exportDocumentVersionAnnotationsToPdf(int docId, int majorVersion, int minorVersion) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ANNOTATIONS_FILE)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion)));

        if (outputPath != null) {
            return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * <b>Retrieve Anchor IDs</b>
     * <p>
     * Retrieve all anchor IDs from a document.
     *
     * @param docId The document id field value.
     *
     * @return DocumentAnnotationAnchorResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/anchors</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-anchor-ids' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-anchor-ids</a>
     * @vapil.request <pre>
     * DocumentAnnotationAnchorResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 				.retrieveAnchorIds(docId);</pre>
     * @vapil.response <pre>
     * System.out.println(response.getResponse());
     *
     * if (response.isSuccessful()) {
     *   for (DocumentAnnotationAnchorResponse.AnchorData data : response.wgetAnchorDataList()) {
     *     System.out.println("\n**** Data Item **** ");
     *     System.out.println("anchorId = " + data.getAnchorId());
     *     System.out.println("noteId = " + data.getNoteId());
     *     System.out.println("anchorName = " + data.getAnchorName());
     *     System.out.println("noteAuthor = " + data.getNoteAuthor());
     *     System.out.println("noteTimestamp = " + data.getNoteTimestamp());
     *     System.out.println("pageNumber = " + data.getPageNumber());
     *   }
     * }</pre>
     */
    public DocumentAnnotationAnchorResponse retrieveAnchorIds(int docId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_DOCUMENT_ANCHORS)
                .replace("{doc_id}", Integer.valueOf(docId).toString()));

        return send(HttpMethod.GET, request, DocumentAnnotationAnchorResponse.class);
    }

    /**
     * <b>Retrieve Document Version Notes as CSV</b>
     * <br>
     * Retrieve notes in CSV format for any document that has a viewable rendition and at least one annotation. You must have a Full User license type.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     *
     * @return VaultResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/doc-export-annotations-to-csv</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-document-version-notes-as-csv' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-document-version-notes-as-csv</a>
     * @vapil.request <pre>
     * <i>Example 1 - To File</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 				.setOutputPath(outputPath.toString())
     * 				.retrieveDocumentVersionNotesAsCSV(docId, majorVersion, minorVersion);
     *
     * <i>Example 2 - Get the file and manually download (retrieve the file as bytes in the response)</i>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 			.setOutputPath(null)
     * 			.retrieveDocumentVersionNotesAsCSV(docId, majorVersion, minorVersion);</pre>
     * @vapil.response <pre>
     * if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
     *   try (OutputStream os = new FileOutputStream(outputPath.toString())) {
     *     os.write(response.getBinaryContent());
     *   }
     *   catch (IOException ignored) {}
     * }
     *
     * System.out.println("File was saved to: " + response.getOutputFilePath());</pre>
     */
    public VaultResponse retrieveDocumentVersionNotesAsCSV(int docId, int majorVersion, int minorVersion) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_NOTES)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion)));

        if (outputPath != null) {
            return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * <b>Retrieve Video Annotations</b>
     * <br>
     * Retrieve annotations on a video document.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     *
     * @return VaultResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/export-video-annotations</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-video-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-video-annotations</a>
     * @vapil.request <pre>
     * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 				.setOutputPath(outputPath.toString())
     * 				.retrieveVideoAnnotations(docId, majorVersion, minorVersion);
     * </pre>
     */
    public VaultResponse retrieveVideoAnnotations(int docId, int majorVersion, int minorVersion) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VIDEO_ANNOTATIONS)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion)));

        if (outputPath != null) {
            return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * <b>Import Document Annotations from PDF</b>
     * <p>
     * Load annotations from a PDF to Vault. This is equivalent to the Import Annotations action in the Vault document viewer UI.
     *
     * @param docId The document id field value.
     *
     * @return DocumentAnnotationImportResponse
     *
     * @vapil.api <pre>
     * POST /api/{version}/objects/documents/{doc_id}/annotations/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#upload-document-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#upload-document-annotations</a>
     * @vapil.request <pre>
     * DocumentAnnotationImportResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.setInputPath(PATH_ANNOTATIONS_FILE)
     * 		.importDocumentAnnotationsFromPdf(docId);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("New Annotations: " + response.getNewCount());
     * System.out.println("Replies: " + response.getReplies());
     * System.out.println("Failures: " + response.getFailures());
     * </pre>
     */
    public DocumentAnnotationImportResponse importDocumentAnnotationsFromPdf(int docId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS_FILE)
                .replace("{doc_id}", Integer.valueOf(docId).toString()));

        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

        if (this.inputPath != null) {
            request.addFileMultiPart("file", inputPath);
        }

        if (this.binaryFile != null) {
            request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
        }

        return send(HttpMethod.POST, request, DocumentAnnotationImportResponse.class);
    }

    /**
     * <b>Import Document Version Annotations from PDF</b>
     * <br>
     * Load annotations from a PDF to Vault. This is equivalent to the Import Annotations action in the Vault document viewer UI.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     *
     * @return DocumentAnnotationImportResponse
     *
     * @vapil.api <pre>
     * POST /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#upload-document-version-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#upload-document-version-annotations</a>
     * @vapil.request <pre>
     * DocumentAnnotationImportResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.setInputPath(PATH_ANNOTATIONS_FILE)
     * 		.importDocumentVersionAnnotationsFromPdf(docId, majorVersionNumber, minorVersionNumber);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("New Annotations: " + response.getNewCount());
     * System.out.println("Replies: " + response.getReplies());
     * System.out.println("Failures: " + response.getFailures());
     * </pre>
     */
    public DocumentAnnotationImportResponse importDocumentVersionAnnotationsFromPdf(int docId, int majorVersion, int minorVersion) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ANNOTATIONS_FILE)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion)));

        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

        if (this.inputPath != null) {
            request.addFileMultiPart("file", inputPath);
        }

        if (this.binaryFile != null) {
            request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
        }

        return send(HttpMethod.POST, request, DocumentAnnotationImportResponse.class);
    }

    /**
     * <b>Retrieve Annotation Type Metadata</b>
     * <br>
     * Retrieves the metadata of an annotation type, including metadata and value sets for all supported fields on the annotation type.
     *
     * @param annotationType    The annotation type
     *
     * @return DocumentAnnotationTypeMetadataResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/metadata/objects/documents/annotations/types/{annotation_type}</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-annotation-type-metadata' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-annotation-type-metadata</a>
     * @vapil.request <pre>
     * DocumentAnnotationTypeMetadataResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .retrieveAnnotationTypeMetadata(AnnotationType.NOTE);
     * </pre>
     * @vapil.response <pre>
     * AnnotationTypeMetadata data = response.getData();
     * System.out.println("Annotation Type Name: " + data.getName());
     * for (AnnotationTypeMetadata.AnnotationField field : data.getFields()) {
     *      System.out.println("Annotation Type Field");
     *      System.out.println("Field Name: " + field.getName());
     *      System.out.println("Field Type: " + field.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationTypeMetadataResponse retrieveAnnotationTypeMetadata(AnnotationType annotationType) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_TYPE_METADATA)
                .replace("{annotation_type}", annotationType.getValue()));

        return send(HttpMethod.GET, request, DocumentAnnotationTypeMetadataResponse.class);
    }

    /**
     * <b>Retrieve Annotation Placemark Type Metadata</b>
     * <br>
     * Retrieves the metadata of a specified annotation placemark type.
     *
     * @param placemarkType    The placemark type
     *
     * @return DocumentAnnotationPlacemarkTypeMetadataResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/metadata/objects/documents/annotations/placemarks/types/{placemark_type}</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-annotation-placemark-type-metadata' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-annotation-placemark-type-metadata</a>
     * @vapil.request <pre>
     * DocumentAnnotationPlacemarkTypeMetadataResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .retrieveAnnotationPlacemarkTypeMetadata(DocumentAnnotationRequest.PlacemarkType.TEXT);
     * </pre>
     * @vapil.response <pre>
     * PlacemarkTypeMetadata data = response.getData();
     * System.out.println("Placemark Type Name: " + data.getName());
     * for (PlacemarkTypeMetadata.PlacemarkField field : data.getFields()) {
     *      System.out.println("Placemark Type Field");
     *      System.out.println("Field Name: " + field.getName());
     * }
     * </pre>
     */
    public DocumentAnnotationPlacemarkTypeMetadataResponse retrieveAnnotationPlacemarkTypeMetadata(PlacemarkType placemarkType) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_PLACEMARK_TYPE_METADATA)
                .replace("{placemark_type}", placemarkType.getValue()));

        return send(HttpMethod.GET, request, DocumentAnnotationPlacemarkTypeMetadataResponse.class);
    }

    /**
     * <b>Retrieve Annotation Reference Type Metadata</b>
     * <br>
     * Retrieves the metadata of a specified annotation reference type.
     *
     * @param referenceType    The reference type
     *
     * @return DocumentAnnotationReferenceTypeMetadataResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/metadata/objects/documents/annotations/references/types/{reference_type}</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-annotation-reference-type-metadata' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-annotation-reference-type-metadata</a>
     * @vapil.request <pre>
     * DocumentAnnotationReferenceTypeMetadataResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .retrieveAnnotationReferenceTypeMetadata(DocumentAnnotationRequest.ReferenceType.DOCUMENT);
     * </pre>
     * @vapil.response <pre>
     * ReferenceTypeMetadata data = response.getData();
     * System.out.println("Reference Type Name: " + data.getName());
     * for (ReferenceTypeMetadata.ReferenceField field : data.getFields()) {
     *      System.out.println("Reference Type Field");
     *      System.out.println("Field Name: " + field.getName());
     * }
     * </pre>
     */
    public DocumentAnnotationReferenceTypeMetadataResponse retrieveAnnotationReferenceTypeMetadata(ReferenceType referenceType) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_REFERENCE_TYPE_METADATA)
                .replace("{reference_type}", referenceType.getValue()));

        return send(HttpMethod.GET, request, DocumentAnnotationReferenceTypeMetadataResponse.class);
    }

    /**
     * <b>Create Multiple Annotations</b>
     * <br>
     * Create up to 500 annotations.
     *
     * @return DocumentAnnotationBulkResponse
     *
     * @vapil.api <pre>
     * POST /api/{version}/objects/documents/annotations/batch</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-annotation-reference-type-metadata' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-annotation-reference-type-metadata</a>
     * @vapil.request <pre>
     * <i>Example 1 - Json String</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setJson(jsonString)
     *      .createMultipleAnnotations();
     *
     * <i>Example 2 - Json File</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setInputPath(filePath)
     *      .createMultipleAnnotations();
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationResponse annotation : response.getData()) {
     *      System.out.println("Response Status: " + annotation.getResponseStatus());
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Document Version ID: " + annotation.getDocumentVersionId());
     * }
     * </pre>
     */
    public DocumentAnnotationBulkResponse createMultipleAnnotations() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS_BULK));
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        if (json != null) {
            request.addRawString("application/json", json);
        }

        if (inputPath != null && !inputPath.isEmpty())
            request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);

        return send(HttpMethod.POST, request, DocumentAnnotationBulkResponse.class);
    }

    /**
     * <b>Add Annotation Replies</b>
     * <br>
     * Create up to 500 annotation replies.
     *
     * @return DocumentAnnotationBulkResponse
     *
     * @vapil.api <pre>
     * POST /api/{version}/objects/documents/annotations/replies/batch</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#add-annotation-replies' target='_blank'>https://developer.veevavault.com/api/24.2/#add-annotation-replies</a>
     * @vapil.request <pre>
     * <i>Example 1 - Json String</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setJson(jsonString)
     *      .addAnnotationReplies();
     *
     * <i>Example 2 - Json File</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setInputPath(filePath)
     *      .addAnnotationReplies();
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationResponse annotation : response.getData()) {
     *      System.out.println("Response Status: " + annotation.getResponseStatus());
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Document Version ID: " + annotation.getDocumentVersionId());
     * }
     * </pre>
     */
    public DocumentAnnotationBulkResponse addAnnotationReplies() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_REPLIES_ADD));
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        if (json != null) {
            request.addRawString("application/json", json);
        }

        if (inputPath != null && !inputPath.isEmpty())
            request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);

        return send(HttpMethod.POST, request, DocumentAnnotationBulkResponse.class);
    }

    /**
     * <b>Update Annotations</b>
     * <br>
     * Update up to 500 existing annotations.
     *
     * @return DocumentAnnotationBulkResponse
     *
     * @vapil.api <pre>
     * PUT /api/{version}/objects/documents/annotations/batch</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#update-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#update-annotations</a>
     * @vapil.request <pre>
     * <i>Example 1 - Json String</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setJson(jsonString)
     *      .updateAnnotations();
     *
     * <i>Example 2 - Json File</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setInputPath(filePath)
     *      .updateAnnotations();
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationResponse annotation : response.getData()) {
     *      System.out.println("Response Status: " + annotation.getResponseStatus());
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Document Version ID: " + annotation.getDocumentVersionId());
     * }
     * </pre>
     */
    public DocumentAnnotationBulkResponse updateAnnotations() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS_BULK));
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        if (json != null) {
            request.addRawString("application/json", json);
        }

        if (inputPath != null && !inputPath.isEmpty())
            request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);

        return send(HttpMethod.PUT, request, DocumentAnnotationBulkResponse.class);
    }

    /**
     * <b>Read Annotations by Document Version and Type</b>
     * <br>
     * Retrieve annotations from a specific document version. You can retrieve all annotations or choose to retrieve only certain annotation types.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     *
     * @return DocumentAnnotationReadResponse
     *
     * @vapil.api <pre>
     * POST GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#read-annotations-by-document-version-and-type' target='_blank'>https://developer.veevavault.com/api/24.2/#read-annotations-by-document-version-and-type</a>
     * @vapil.request <pre>
     * DocumentAnnotationReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     * 		.readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotation annotation : response.getData()) {
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Comment: " + annotation.getComment());
     *      System.out.println("Type: " + annotation.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationReadResponse readAnnotationsByDocumentVersionAndType(int docId, int majorVersion, int minorVersion) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion)));

        if (offset != null) {
            request.addQueryParam("offset", offset);
        }

        if (limit != null) {
            request.addQueryParam("limit", limit);
        }

        if (paginationId != null) {
            request.addQueryParam("pagination_id", paginationId);
        }

        if (annotationTypes != null) {
            request.addQueryParam("annotation_types", String.join(",", annotationTypes));
        }

        return send(HttpMethod.GET, request, DocumentAnnotationReadResponse.class);
    }

    /**
     * <b>Read Annotations by Document Version and Type (By Page)</b>
     * <p>
     * Retrieve annotations from a specific document version using the previous_page or next_page parameter of a previous request
     *
     * @param pageUrl The URL from the previous_page or next_page parameter
     *
     * @return DocumentAnnotationReadResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#read-annotations-by-document-version-and-type' target='_blank'>https://developer.veevavault.com/api/24.2/#read-annotations-by-document-version-and-type</a>
     * @vapil.request <pre>
     * if (response.isPaginated()) {
     *      String nextPageUrl = response.getResponseDetails().getNextPage();
     *      DocumentAnnotationReadResponse response_page_2 = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *          .readAnnotationsByDocumentVersionAndTypeByPage(nextPageUrl);
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotation annotation : response_page_2.getData()) {
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Comment: " + annotation.getComment());
     *      System.out.println("Type: " + annotation.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationReadResponse readAnnotationsByDocumentVersionAndTypeByPage(String pageUrl) {
        String url = vaultClient.getPaginationEndpoint(pageUrl);
        HttpRequestConnector request = new HttpRequestConnector(url);
        return send(HttpMethod.GET, request, DocumentAnnotationReadResponse.class);
    }

    /**
     * <b>Read Annotations by ID</b>
     * <p>
     * Retrieve a specific annotation by the annotation ID.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     * @param annotationId The annotation id
     *
     * @return DocumentAnnotationReadResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/{annotation_id}</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#read-annotations-by-id' target='_blank'>https://developer.veevavault.com/api/24.2/#read-annotations-by-id</a>
     * @vapil.request <pre>
     * DocumentAnnotationReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .readAnnotationsById(docId, majorVersionNumber, minorVersionNumber, annotationId);
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotation annotation : response.getData()) {
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Comment: " + annotation.getComment());
     *      System.out.println("Type: " + annotation.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationReadResponse readAnnotationsById(int docId, int majorVersion, int minorVersion, int annotationId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_ID)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion))
                .replace("{annotation_id}", Integer.toString(annotationId)));

        return send(HttpMethod.GET, request, DocumentAnnotationReadResponse.class);
    }

    /**
     * <b>Read Replies of Parent Annotation</b>
     * <br>
     * Given a parent annotation ID, retrieves all replies to the annotation.
     *
     * @param docId        The Document Id
     * @param majorVersion The document major_version_number__v field value
     * @param minorVersion The document minor_version_number__v field value
     * @param annotationId The annotation id
     *
     * @return DocumentAnnotationReplyReadResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/{annotation_id}/replies</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#read-replies-of-parent-annotation' target='_blank'>https://developer.veevavault.com/api/24.2/#read-replies-of-parent-annotation</a>
     * @vapil.request <pre>
     * DocumentAnnotationReplyReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .readRepliesOfParentAnnotation(docId, majorVersionNumber, minorVersionNumber, annotationId);
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationReply reply : response.getData()) {
     *      System.out.println("ID: " + reply.getId());
     *      System.out.println("Comment: " + reply.getComment());
     *      System.out.println("Type: " + reply.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationReplyReadResponse readRepliesOfParentAnnotation(int docId, int majorVersion, int minorVersion, int annotationId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATION_REPLIES)
                .replace("{doc_id}", Integer.valueOf(docId).toString())
                .replace("{major_version}", Integer.toString(majorVersion))
                .replace("{minor_version}", Integer.toString(minorVersion))
                .replace("{annotation_id}", Integer.toString(annotationId)));

        if (offset != null) {
            request.addQueryParam("offset", offset);
        }

        if (limit != null) {
            request.addQueryParam("limit", limit);
        }

        if (paginationId != null) {
            request.addQueryParam("pagination_id", paginationId);
        }

        return send(HttpMethod.GET, request, DocumentAnnotationReplyReadResponse.class);
    }

    /**
     * <b>Read Replies of Parent Annotation (By Page)</b>
     * <p>
     * Retrieve replies from a specific annotation using the previous_page or next_page parameter of a previous request
     *
     * @param pageUrl The URL from the previous_page or next_page parameter
     *
     * @return DocumentAnnotationReplyReadResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations/{annotation_id}/replies</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#read-replies-of-parent-annotation' target='_blank'>https://developer.veevavault.com/api/24.2/#read-replies-of-parent-annotation</a>
     * @vapil.request <pre>
     * if (response.isPaginated()) {
     *      String nextPageUrl = response.getResponseDetails().getNextPage();
     *      DocumentAnnotationReplyReadResponse response_page_2 = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *          .readRepliesOfParentAnnotationByPage(nextPageUrl);
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationReply reply : response_page_2.getData()) {
     *      System.out.println("ID: " + reply.getId());
     *      System.out.println("Comment: " + reply.getComment());
     *      System.out.println("Type: " + reply.getType());
     * }
     * </pre>
     */
    public DocumentAnnotationReplyReadResponse readRepliesOfParentAnnotationByPage(String pageUrl) {
        String url = vaultClient.getPaginationEndpoint(pageUrl);
        HttpRequestConnector request = new HttpRequestConnector(url);
        return send(HttpMethod.GET, request, DocumentAnnotationReplyReadResponse.class);
    }

    /**
     * <b>Delete Annotations</b>
     * <p>
     * Delete multiple annotations. You can delete up to 500 annotations per batch.
     *
     * @return DocumentAnnotationBulkResponse
     *
     * @vapil.api <pre>
     * DELETE/api/{version}/objects/documents/annotations/batch</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#delete-annotations' target='_blank'>https://developer.veevavault.com/api/24.2/#delete-annotations</a>
     * @vapil.request <pre>
     * <i>Example 1 - CSV input</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setContentTypeCsv()
     *      .setInputPath(csvFilePath)
     *      .deleteAnnotations();
     *
     * <i>Example 2 - Binary input</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setContentTypeCsv()
     *      .setBinaryFile(csvFile.getName(), Files.readAllBytes(csvFile.toPath()))
     *      .deleteAnnotations();
     *
     * <i>Example 3 - Json input</i>
     * DocumentAnnotationBulkResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
     *      .setContentTypeJson()
     *      .setRequestString(requestString)
     *      .deleteAnnotations();
     * </pre>
     * @vapil.response <pre>
     * for (DocumentAnnotationResponse annotation : response.getData()) {
     *      System.out.println("Response Status: " + annotation.getResponseStatus());
     *      System.out.println("Annotation ID: " + annotation.getId());
     *      System.out.println("Document Version ID: " + annotation.getDocumentVersionId());
     * }
     * </pre>
     */
    public DocumentAnnotationBulkResponse deleteAnnotations() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS_BULK));

        String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
        if (headerContentType != null)
            contentType = headerContentType;

        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

        if (inputPath != null && !inputPath.isEmpty())
            request.addFile(contentType, inputPath);

        if (requestString != null && !requestString.isEmpty())
            request.addRawString(contentType, requestString);

        if (binaryFile != null)
            request.addBinary(contentType, binaryFile.getBinaryContent());

        return send(HttpMethod.DELETE, request, DocumentAnnotationBulkResponse.class);
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
     *
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setBinaryFile(String filename, byte[] binaryContent) {
        this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
        return this;
    }

    /**
     * Specify source data in an input file
     *
     * @param inputPath Absolute path to the file for the request
     *
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setInputPath(String inputPath) {
        this.inputPath = inputPath;
        return this;
    }

    /**
     * Specify source data in an output file
     *
     * @param outputPath Absolute path to the file for the response
     *
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    /**
     * This parameter is used to paginate the results. It specifies the amount of offset from the first record returned.
     *
     * @param offset Amount of offset from the first record returned
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * This parameter paginates the results by specifying the maximum number of records per page in the response.
     *
     * @param limit Max number of results
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * A unique identifier used to load requests with paginated results.
     *
     * @param paginationId The pagination id
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setPaginationId(String paginationId) {
        this.paginationId = paginationId;
        return this;
    }

    /**
     * The type(s) of annotations to retrieve. For example, note__sys,anchor__sys. If omitted, Vault returns all annotations.
     *
     * @param annotationTypes A set of annotation types
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setAnnotationTypes(Set<String> annotationTypes) {
        this.annotationTypes = annotationTypes;
        return this;
    }

    /**
     * Set the Header Content Type to CSV
     *
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setContentTypeCsv() {
        this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
        return this;
    }

    /**
     * Set the Header Content Type to JSON
     *
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setContentTypeJson() {
        this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
        return this;
    }

    /**
     * Specify source data in an input string, such as a JSON request
     *
     * @param requestString The source request as a string
     * @return DocumentAnnotationRequest
     */
    public DocumentAnnotationRequest setRequestString(String requestString) {
        this.requestString = requestString;
        return this;
    }

    /**
     * Set a raw JSON string to the Annotation request
     *
     * @param json Fully formed Annotation request
     * @return The Request
     */
    public DocumentAnnotationRequest setJson(String json) {
        this.json = json;
        return this;
    }

    /**
     * Enum AnnotationType represents the valid annotation types.
     */
    public enum AnnotationType {
        NOTE("note__sys"),
        LINE("line__sys"),
        DOCUMENT_LINK("document_link__sys"),
        PERMALINK("permalink_link__sys"),
        ANCHOR("anchor__sys"),
        REPLY("reply__sys"),
        EXTERNAL_LINK("external_link__sys");

        private final String annotationType;

        AnnotationType(String annotationType) {
            this.annotationType = annotationType;
        }

        public String getValue() {
            return annotationType;
        }
    }

    /**
     * Enum PlacemarkType represents the valid placemark types.
     */
    public enum PlacemarkType {
        ARROW("arrow__sys"),
        ELLIPSE("ellipse__sys"),
        RECTANGLE("rectangle__sys"),
        LINE("line__sys"),
        PAGE_LEVEL("page_level__sys"),
        REPLY("reply__sys"),
        STICKY("sticky__sys"),
        TEXT("text__sys");

        private final String placemarkType;

        PlacemarkType(String placemarkType) {
            this.placemarkType = placemarkType;
        }

        public String getValue() {
            return placemarkType;
        }
    }

    /**
     * Enum ReferenceType represents the valid reference types.
     */
    public enum ReferenceType {
        DOCUMENT("document__sys"),
        EXTERNAL("external__sys"),
        PERMALINK("permalink__sys");

        private final String referenceType;

        ReferenceType(String referenceType) {
            this.referenceType = referenceType;
        }

        public String getValue() {
            return referenceType;
        }
    }
}
