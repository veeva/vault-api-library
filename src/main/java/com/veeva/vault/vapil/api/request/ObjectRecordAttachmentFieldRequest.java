package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;

/**
 * Object Record Attachment Field requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.3/#attachment-fields">https://developer.veevavault.com/api/24.3/#attachment-fields</a>
 */
public class ObjectRecordAttachmentFieldRequest extends VaultRequest<ObjectRecordAttachmentFieldRequest>{

    // API Endpoints
    private static final String URL_OBJ_REC_ATTACHMENT_FIELD_FILE = "/vobjects/{object_name}/{object_record_id}/attachment_fields/{attachment_field_name}/file";
    private static final String URL_OBJ_REC_ATTACHMENTS = "/vobjects/{object_name}/{object_record_id}/attachment_fields/file";

    private HttpRequestConnector.BinaryFile binaryFile;
    private String inputPath;
    private String outputPath;

    /**
     * <b>Download Attachment Field File</b>
     * <p>
     * Download the specified Attachment field file from an object record.
     *
     * @param objectName   The object name__v field value
     * @param recordId     The object record id field value
     * @param attachmentFieldName    The name of the Attachment field from which to retrieve the file
     * @return VaultResponse
     * @vapil.api <pre>
     * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachment_fields/{attachment_field_name}/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#download-attachment-field-file' target='_blank'>https://developer.veevavault.com/api/24.3/#download-attachment-field-file</a>
     * @vapil.request <pre>
     * <i>Example 1 - Download Binary Content</i>
     * VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .downloadAttachmentFieldFile(objectName, recordId, attachmentFieldName);
     *
     * <i>Example 2 - Download to File</i>
     * VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setOutputPath(outputPath)
     *      .downloadAttachmentFieldFile(objectName, recordId, attachmentFieldName);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Content-Type: " + response.getHeaderContentType());
     * System.out.println("Content-Disposition: " + response.getHttpHeaderContentDisposition());
     * </pre>
     */
    public VaultResponse downloadAttachmentFieldFile(String objectName, String recordId, String attachmentFieldName) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_FIELD_FILE);
        url = url.replace("{object_name}", objectName);
        url = url.replace("{object_record_id}", recordId);
        url = url.replace("{attachment_field_name}", attachmentFieldName);

        HttpRequestConnector request = new HttpRequestConnector(url);

        if (outputPath != null) {
            return sendToFile(HttpRequestConnector.HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * <b>Update Attachment Field File</b>
     * <br>
     * <p>
     * Update an Attachment field by uploading a file. If you need to update more
     * than one Attachment field, it is best practice to update in bulk with Update Object Records.
     *
     * @param objectName The object name__v field value
     * @param recordId   The object record id field value
     * @param attachmentFieldName The name of the Attachment field to update
     * @return ObjectRecordBulkResponse
     * @vapil.api <pre>
     * POST /api/{version}/vobjects/{object_name}/{object_record_id}/attachment_fields/{attachment_field_name}/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#update-attachment-field-file' target='_blank'>https://developer.veevavault.com/api/24.3/#update-attachment-field-file</a>
     * @vapil.request <pre>
     * <i>Example 1 - Upload from input path</i>
     * ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setInputPath(inputPath)
     *      .updateAttachmentFieldFile(objectName, recordId, attachmentFieldName);
     *
     * <i>Example 2 - Upload bytes</i>
     * ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setBinaryFile("test_binary_file.txt", bytesArray)
     *      .updateAttachmentFieldFile(objectName, recordId, attachmentFieldName);
     * </pre>
     * @vapil.response <pre>
     * for (ObjectRecordResponse objectRecordResponse : response.getData()) {
     *      System.out.println("Response Status: " + objectRecordResponse.getResponseStatus());
     *      System.out.println("Record ID: " + objectRecordResponse.getData().getId());
     *      System.out.println("Record URL: " + objectRecordResponse.getData().getUrl());
     * }
     * </pre>
     */
    public ObjectRecordBulkResponse updateAttachmentFieldFile(String objectName, String recordId, String attachmentFieldName) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_FIELD_FILE);
        url = url.replace("{object_name}", objectName);
        url = url.replace("{object_record_id}", recordId);
        url = url.replace("{attachment_field_name}", attachmentFieldName);

        HttpRequestConnector request = new HttpRequestConnector(url);

        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

        if (inputPath != null && !inputPath.isEmpty())
            request.addFileMultiPart("file", inputPath);

        if (binaryFile != null)
            request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

        return send(HttpRequestConnector.HttpMethod.POST, request, ObjectRecordBulkResponse.class);
    }

    /**
     * <b>Download All Attachment Field Files</b>
     * <p>
     * Download all Attachment field files from the specified object record.
     *
     * @param objectName   The object name__v field value
     * @param recordId     The object record id field value
     * @return VaultResponse
     * @vapil.api <pre>
     * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachment_fields/file</pre>
     * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.3/#download-all-attachment-field-files' target='_blank'>https://developer.veevavault.com/api/24.3/#download-all-attachment-field-files</a>
     * @vapil.request <pre>
     * <i>Example 1 - Download Binary Content</i>
     * VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .downloadAllAttachmentFieldFiles(objectName, recordId);
     *
     * <i>Example 2 - Download to File</i>
     * VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setOutputPath(outputPath)
     *      .downloadAllAttachmentFieldFiles(objectName, recordId);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Content-Type: " + response.getHeaderContentType());
     * System.out.println("Content-Disposition: " + response.getHttpHeaderContentDisposition());
     * </pre>
     */
    public VaultResponse downloadAllAttachmentFieldFiles(String objectName, String recordId) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS);
        url = url.replace("{object_name}", objectName);
        url = url.replace("{object_record_id}", recordId);

        HttpRequestConnector request = new HttpRequestConnector(url);

        if (outputPath != null) {
            return sendToFile(HttpRequestConnector.HttpMethod.GET, request, outputPath, VaultResponse.class);
        } else {
            return sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, VaultResponse.class);
        }
    }

    /**
     * Specify source data in an input file
     *
     * @param filename      file name (no path)
     * @param binaryContent byte array of the file content
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setBinaryFile(String filename, byte[] binaryContent) {
        this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
        return this;
    }

    /**
     * Specify source data in an input file
     *
     * @param inputPath Absolute path to the file for the request
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setInputPath(String inputPath) {
        this.inputPath = inputPath;
        return this;
    }

    /**
     * Specify source data in an output file
     *
     * @param outputPath Absolute path to the file for the response
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }
}
