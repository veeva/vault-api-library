package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Object Record Attachment Field requests
 *
 * @vapil.apicoverage <a href="https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields">https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields</a>
 */
public class ObjectRecordAttachmentFieldRequest extends VaultRequest<ObjectRecordAttachmentFieldRequest>{

    // API Endpoints
    private static final String URL_OBJ_REC_ATTACHMENT_FIELD_FILE = "/vobjects/{object_name}/{object_record_id}/attachment_fields/{attachment_field_name}/file";
    private static final String URL_OBJ_REC_ATTACHMENTS = "/vobjects/{object_name}/{object_record_id}/attachment_fields/file";
    private static final String URL_OBJ_REC_ATTACHMENT_EXPORT = "/vobjects/{object_name}/attachment_fields/actions/export";
    private static final String URL_OBJ_REC_ATTACHMENT_EXPORT_RESULTS = "/vobjects/{object_name}/attachment_fields/actions/export/{job_id}/results";
    private static final String URL_OBJ_REC_ATTACHMENT_EXPORT_DOWNLOAD = "/vobjects/{object_name}/attachment_fields/files/{file_part_name}";

    // API Request Parameters
    private HttpRequestConnector.BinaryFile binaryFile;
    private String inputPath;
    private String outputPath;
    private String headerContentType;
    private String requestString; // For raw request
    private String idParam;
    private Set<String> fieldNames;

    public static final String ID_PARAM = "idParam";
    private static final String PARAM_FIELD_NAMES = "field_names";

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
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-attachment-field-file' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-attachment-field-file</a>
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
     * <b>Export Attachment Field Files</b>
     * <p>
     * Export all Attachment field files in bulk from the specified object records. This endpoint starts a job to create a tar.gz export file for later retrieval.
     *
     * @param objectName   The object name__v field value
     * @return ObjectRecordAttachmentFieldExportResponse
     * @vapil.api <pre>
     * POST /api/{version}/vobjects/{object_name}/attachment_fields/actions/export</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/export-attachment-field-files' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/export-attachment-field-files</a>
     * @vapil.request <pre>
     * <i>Example 1 - From Request String</i>
     * ObjectRecordAttachmentFieldExportResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setContentTypeJson()
     *      .setFieldNames(fieldNames)
     *      .setRequestString(jsonString)
     *      .exportAttachmentFieldFiles(OBJECT_NAME);
     *
     * <i>Example 2 - From Input File</i>
     * ObjectRecordAttachmentFieldExportResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setContentTypeJson()
     *      .setFieldNames(fieldNames)
     *      .setInputPath(inputPath)
     *      .exportAttachmentFieldFiles(OBJECT_NAME);
     *
     * <i>Example 3 - From Binary</i>
     * ObjectRecordAttachmentFieldExportResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .setContentTypeJson()
     *      .setFieldNames(fieldNames)
     *      .setBinaryFile("export_attachment_field_files.json", bytesArray)
     *      .exportAttachmentFieldFiles(OBJECT_NAME);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Job ID: " + response.getData().getJobId());
     * for (ObjectRecordAttachmentFieldExportResponse.Data.ExportResult exportResult : response.getData().getRecords()) {
     *      System.out.println("-----Object Record-----");
     *      System.out.println("Response Status: " + exportResult.getResponseStatus());
     *      System.out.println("Record ID: " + exportResult.getData().getId());
     *      System.out.println("ID Param Value: " + exportResult.getData().getIdParamValue());
     * }
     * </pre>
     */
    public ObjectRecordAttachmentFieldExportResponse exportAttachmentFieldFiles(String objectName) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_EXPORT);
        url = url.replace("{object_name}", objectName);

        String contentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
        if (headerContentType != null)
            contentType = headerContentType;

        HttpRequestConnector request = new HttpRequestConnector(url);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, contentType);

        if (idParam != null && !idParam.isEmpty())
            request.addQueryParam(ID_PARAM, idParam);

        if (fieldNames != null && !fieldNames.isEmpty()) {
            String fieldNamesList = String.join(",", fieldNames);
            request.addQueryParam(PARAM_FIELD_NAMES, fieldNamesList);
        }

        if (inputPath != null && !inputPath.isEmpty())
            request.addFile(contentType, inputPath);

        if (requestString != null && !requestString.isEmpty())
            request.addRawString(contentType, requestString);

        if (binaryFile != null)
            request.addBinary(contentType, binaryFile.getBinaryContent());

        return send(HttpRequestConnector.HttpMethod.POST, request, ObjectRecordAttachmentFieldExportResponse.class);
    }

    /**
     * <b>Retrieve Attachment Field Files Export Results</b>
     * <p>
     * Retrieve the results of the job requested by the Export Attachment Field Files endpoint.
     *
     * @param objectName object name__v field value
     * @param jobId export job id
     * @return ObjectRecordAttachmentFieldExportResultsResponse
     * @vapil.api <pre>
     * GET /api/{version}/vobjects/{object_name}/attachment_fields/actions/export/{job_id}/results</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/retrieve-attachment-field-files-export-results' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/retrieve-attachment-field-files-export-results</a>
     * @vapil.request <pre>
     * ObjectRecordAttachmentFieldExportResultsResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .retrieveAttachmentFieldFilesExportResults(OBJECT_NAME, jobId);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("File Name: " + response.getData().getFilename());
     * System.out.println("File Size: " + response.getData().getSize());
     * System.out.println("Fileparts: " + response.getData().getFileparts());
     * for (ObjectRecordAttachmentFieldExportResultsResponse.Data.FilePart filePart : response.getData().getFilepartDetails()) {
     *      System.out.println("-----File Part-----");
     *      System.out.println("File Part Filename: " + filePart.getFilename());
     *      System.out.println("File Part Size: " + filePart.getSize());
     * }
     * </pre>
     */
    public ObjectRecordAttachmentFieldExportResultsResponse retrieveAttachmentFieldFilesExportResults(String objectName, String jobId) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_EXPORT_RESULTS)
                .replace("{object_name}", objectName)
                .replace("{job_id}", jobId);

        HttpRequestConnector request = new HttpRequestConnector(url);
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        return send(HttpRequestConnector.HttpMethod.GET, request, ObjectRecordAttachmentFieldExportResultsResponse.class);
    }

    /**
     * <b>Download Attachment Field Files Export</b>
     * <p>
     * Download the export file parts generated by Export Attachment Field Files.
     *
     * @param objectName   The object name__v field value
     * @param filePartName The name of the file part to download.
     * @return VaultResponse
     * @vapil.api <pre>
     * GET /api/{version}/vobjects/{object_name}/attachment_fields/files/{file_part_name}</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-attachment-field-files-export' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-attachment-field-files-export</a>
     * @vapil.request <pre>
     * VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
     *      .downloadAttachmentFieldFilesExport(OBJECT_NAME, filePartName);
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Content-Type: " + response.getHeaderContentType());
     * System.out.println("Content-Disposition: " + response.getHttpHeaderContentDisposition());
     * </pre>
     */
    public VaultResponse downloadAttachmentFieldFilesExport(String objectName, String filePartName) {
        String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_EXPORT_DOWNLOAD);
        url = url.replace("{object_name}", objectName);
        url = url.replace("{file_part_name}", filePartName);

        HttpRequestConnector request = new HttpRequestConnector(url);

        return sendReturnBinary(HttpRequestConnector.HttpMethod.GET, request, VaultResponse.class);
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
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/update-attachment-field-file' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/update-attachment-field-file</a>
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
     * @vapil.vaultlink <a href='https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-all-attachment-field-files' target='_blank'>https://general.veevavault.dev/vault-api/api-reference/26.2/vault-objects/object-attachment-fields/download-all-attachment-field-files</a>
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

    /**
     * Set the Header Content Type to CSV
     *
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setContentTypeCsv() {
        this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
        return this;
    }

    /**
     * Set the Header Content Type to JSON
     *
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setContentTypeJson() {
        this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
        return this;
    }

    /**
     * Specify source data in an input string, such as a JSON request
     *
     * @param requestString The source request as a string
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setRequestString(String requestString) {
        this.requestString = requestString;
        return this;
    }

    /**
     * Specify an UPSERT operation via the idParam
     *
     * @param idParam External Id field API name for the UPSERT
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setIdParam(String idParam) {
        this.idParam = idParam;
        return this;
    }

    /**
     * Specify field names to retrieve attached files from
     *
     * @param fieldNames Set of object field names
     * @return The Request
     */
    public ObjectRecordAttachmentFieldRequest setFieldNames(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }
}
