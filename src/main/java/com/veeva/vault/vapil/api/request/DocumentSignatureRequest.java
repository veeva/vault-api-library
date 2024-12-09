/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.DocumentSignatureMetadataResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Events
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.3/#document-signatures">https://developer.veevavault.com/api/24.3/#document-signatures</a>
 */
public class DocumentSignatureRequest extends VaultRequest<DocumentSignatureRequest> {
    // API Endpoints
    private static final String URL_DOCUMENT_SIGNATURE_METADATA = "/metadata/query/documents/relationships/document_signature__sysr";
    private static final String URL_ARCHIVED_DOCUMENT_SIGNATURE_METADATA = "/metadata/query/archived_documents/relationships/document_signature__sysr";

    private DocumentSignatureRequest() {
    }

    /**
     * <b>Retrieve Document Signature Metadata</b>
     * <p>
     * Retrieve all metadata for signatures on documents.
     *
     * @return DocumentSignatureMetadataResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/metadata/query/documents/relationships/document_signature__sysr</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-document-event-types-and-subtypes' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-document-event-types-and-subtypes</a>
     * @vapil.request <pre>
     * DocumentSignatureMetadataResponse response = vaultClient.newRequest(DocumentSignatureRequest.class)
     *      .retrieveDocumentSignatureMetadata();
     * </pre>
     * @vapil.response <pre>
     * for (Field field : response.getProperties().getFields()) {
     *      System.out.println("--------Field---------");
     *      System.out.println("Name: " + field.getName());
     *      System.out.println("Type: " + field.getType());
     * }
     * </pre>
     */

    public DocumentSignatureMetadataResponse retrieveDocumentSignatureMetadata() {
        String url = vaultClient.getAPIEndpoint(URL_DOCUMENT_SIGNATURE_METADATA);

        HttpRequestConnector request = new HttpRequestConnector(url);
        return send(HttpMethod.GET, request, DocumentSignatureMetadataResponse.class);
    }

    /**
     * <b>Retrieve Archived Document Signature Metadata</b>
     * <p>
     * Retrieve all metadata for signatures on archived documents.
     *
     * @return DocumentSignatureMetadataResponse
     *
     * @vapil.api <pre>
     * GET /api/{version}/metadata/query/archived_documents/relationships/document_signature__sysr</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#retrieve-archived-document-signature-metadata' target='_blank'>https://developer.veevavault.com/api/24.3/#retrieve-archived-document-signature-metadata</a>
     * @vapil.request <pre>
     * DocumentSignatureMetadataResponse response = vaultClient.newRequest(DocumentSignatureRequest.class)
     *      .retrieveArchivedDocumentSignatureMetadata();
     * </pre>
     * @vapil.response <pre>
     * for (Field field : response.getProperties().getFields()) {
     *      System.out.println("--------Field---------");
     *      System.out.println("Name: " + field.getName());
     *      System.out.println("Type: " + field.getType());
     * }
     * </pre>
     */

    public DocumentSignatureMetadataResponse retrieveArchivedDocumentSignatureMetadata() {
        String url = vaultClient.getAPIEndpoint(URL_ARCHIVED_DOCUMENT_SIGNATURE_METADATA);

        HttpRequestConnector request = new HttpRequestConnector(url);
        return send(HttpMethod.GET, request, DocumentSignatureMetadataResponse.class);
    }
}
