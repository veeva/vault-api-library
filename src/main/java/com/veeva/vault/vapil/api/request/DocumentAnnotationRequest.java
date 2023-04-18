/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Document Annotation Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#document-annotations">https://developer.veevavault.com/api/23.1/#document-annotations</a>
 */
public class DocumentAnnotationRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_DOCUMENT_ANNOTATIONS = "/objects/documents/{doc_id}/annotations";
	private static final String URL_DOCUMENT_VERSION_ANNOTATIONS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations";
	private static final String URL_DOCUMENT_DOCUMENT_ANCHORS = "/objects/documents/{doc_id}/anchors";
	private static final String URL_DOCUMENT_VERSION_NOTES = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/doc-export-annotations-to-csv";
	private static final String URL_DOCUMENT_VIDEO_ANNOTATIONS = "/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/export-video-annotations";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private String inputPath;
	private String outputPath;

	private DocumentAnnotationRequest() {
	}

	/**
	 * Retrieve Document Annotations
	 * <p>
	 * Retrieves the document rendition and its associated annotations.
	 *
	 * @param docId The document id field value.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/annotations</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-annotations' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-annotations</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - To File</i>
	 * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.retrieveDocumentAnnotations(docId);</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 -  Get the file and manually download (retrieve the file as bytes in the response)</i>
	 * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.retrieveDocumentAnnotations(docId);</pre>
	 * @vapil.response <pre>
	 * if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   try (OutputStream os = new FileOutputStream(outputPath.toString())) {
	 *     os.write(response.getBinaryContent());
	 *     }
	 *   catch (IOException ignored) {}
	 * }</pre>
	 */
	public VaultResponse retrieveDocumentAnnotations(int docId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS)
				.replace("{doc_id}", Integer.valueOf(docId).toString()));

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Retrieve Document Version Annotations
	 * <p>
	 * Retrieves the specified version document rendition and its associated annotations.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-version-annotations' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-version-annotations</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.retrieveDocumentVersionAnnotations(docId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   try (OutputStream os = new FileOutputStream(outputPath.toString())) {
	 *     os.write(response.getBinaryContent());
	 *     }
	 *   catch (IOException ignored) {}
	 * }
	 *
	 * System.out.println("File was saved to: " + outputPath.toString());
	 * </pre>
	 */
	public VaultResponse retrieveDocumentVersionAnnotations(int docId, int majorVersion, int minorVersion) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ANNOTATIONS)
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
	 * Retrieve Anchor IDs
	 * <p>
	 * Retrieve all anchor IDs from a document.
	 *
	 * @param docId The document id field value.
	 * @return DocumentAnnotationResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/anchors</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-anchor-ids' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-anchor-ids</a>
	 * @vapil.request <pre>
	 * DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.retrieveAnchorIds(docId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(response.getResponse());
	 *
	 * if (response.isSuccessful()) {
	 *   for (DocumentAnnotationResponse.AnchorData data : response.wgetAnchorDataList()) {
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
	public DocumentAnnotationResponse retrieveAnchorIds(int docId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_DOCUMENT_ANCHORS)
				.replace("{doc_id}", Integer.valueOf(docId).toString()));

		return send(HttpMethod.GET, request, DocumentAnnotationResponse.class);
	}

	/**
	 * Retrieve Document Version Notes as CSV
	 * <br>
	 * Retrieve notes in CSV format for any document that has a viewable rendition and at least one annotation. You must have a Full User license type.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/doc-export-annotations-to-csv</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-document-version-notes-as-csv' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-document-version-notes-as-csv</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - To File</i>
	 * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.retrieveDocumentVersionNotesAsCSV(docId, majorVersion, minorVersion);</pre>
	 * @vapil.request <pre>
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
	 * Retrieve Video Annotations
	 * <br>
	 * Retrieve annotations on a video document.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/export-video-annotations</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-video-annotations' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-video-annotations</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setOutputPath(outputPath.toString())
	 * 				.retrieveVideoAnnotations(docId, majorVersion, minorVersion);</pre>
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
	 * Upload Document Annotations
	 * <p>
	 * Uploads the file and its annotations.
	 *
	 * @param docId The document id field value.
	 * @return DocumentAnnotationResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/annotations</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upload-document-annotations' target='_blank'>https://developer.veevavault.com/api/23.1/#upload-document-annotations</a>
	 * @vapil.request <pre>
	 * DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setInputPath(inputPath.toString())
	 * 				.uploadDocumentAnnotations(docId);</pre>
	 */
	public DocumentAnnotationResponse uploadDocumentAnnotations(int docId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_ANNOTATIONS)
				.replace("{doc_id}", Integer.valueOf(docId).toString()));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}

		return send(HttpMethod.POST, request, DocumentAnnotationResponse.class);
	}

	/**
	 * Upload Document Version Annotations
	 * <br>
	 * Uploads the file and its annotations for the specified document version.
	 *
	 * @param docId        The Document Id
	 * @param majorVersion The document major_version_number__v field value
	 * @param minorVersion The document minor_version_number__v field value
	 * @return DocumentAnnotationResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/annotations</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upload-document-version-annotations' target='_blank'>https://developer.veevavault.com/api/23.1/#upload-document-version-annotations</a>
	 * @vapil.request <pre>
	 * DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
	 * 				.setInputPath(inputPath.toString())
	 * 				.uploadDocumentVersionAnnotations(docId, majorVersion, minorVersion);</pre>
	 */
	public DocumentAnnotationResponse uploadDocumentVersionAnnotations(int docId, int majorVersion, int minorVersion) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOCUMENT_VERSION_ANNOTATIONS)
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

		return send(HttpMethod.POST, request, DocumentAnnotationResponse.class);
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
	public DocumentAnnotationRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public DocumentAnnotationRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public DocumentAnnotationRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}
}
