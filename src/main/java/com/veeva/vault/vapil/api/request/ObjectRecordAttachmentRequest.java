/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordAttachmentResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.apache.log4j.Logger;

/**
 * Object Record Attachments requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#object-record-attachments">https://developer.veevavault.com/api/23.1/#object-record-attachments</a>
 */
public class ObjectRecordAttachmentRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(ObjectRecordAttachmentRequest.class);

	// API Endpoints
	private static final String URL_OBJ_REC_ATTACHMENT = "/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}";
	private static final String URL_OBJ_REC_ATTACHMENT_FILE = "/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/file";
	private static final String URL_OBJ_REC_ATTACHMENT_FILES = "/vobjects/{object_name}/{object_record_id}/attachments/file";
	private static final String URL_OBJ_REC_ATTACHMENTS = "/vobjects/{object_name}/{object_record_id}/attachments";
	private static final String URL_OBJ_REC_ATTACHMENTS_BATCH = "/vobjects/{object_name}/attachments/batch";
	private static final String URL_OBJ_REC_ATTACHMENT_VERSION = "/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions/{attachment_version}";
	private static final String URL_OBJ_REC_ATTACHMENT_VERSION_FILE = "/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions/{attachment_version}/file";
	private static final String URL_OBJ_REC_ATTACHMENT_VERSIONS = "/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions";

	/**
	 * If you’re identifying object records in your input by a unique field, add idParam={fieldname} to the request endpoint.
	 */
	public static final String ID_PARAM = "idParam";

	private HttpRequestConnector.BinaryFile binaryFile;
	private String headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
	;
	private String headerContentType;
	private String idParam;
	private String inputPath;
	private String outputPath;
	private String requestString; // For raw request

	private ObjectRecordAttachmentRequest() {
	}

	/**
	 * Determine if Attachments are Enabled on an Object
	 *
	 * @param objectName Object name for the attachment
	 * @return Boolean indicating if the Object allows attachments. Null if error occurs
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#determine-if-attachments-are-enabled-on-an-object' target='_blank'>https://developer.veevavault.com/api/23.1/#determine-if-attachments-are-enabled-on-an-object</a>
	 * @vapil.request <pre>
	 * Boolean attachmentsEnabled = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).attachmentsEnabled(objectName);</pre>
	 * @vapil.response <pre>System.out.println("attachmentsEnabled = " + attachmentsEnabled);
	 * }</pre>
	 */
	public Boolean attachmentsEnabled(String objectName) {
		MetaDataObjectResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveObjectMetadata(objectName);
		if (response != null && response.isSuccessful()) {
			return response.getObject().getAllowAttachments();
		} else {
			return null;
		}
	}

	/**
	 * Retrieve Object Record Attachments
	 *
	 * @param objectName Object name for the attachment
	 * @param recordId   Object record id for the attachment
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachments' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachments</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse attachmentResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.retrieveObjectRecordAttachments(objectName,recordId);</pre>
	 * @vapil.response <pre>
	 * if (attachmentResponse.getData() != null) {
	 *     for (ObjectAttachment attachment : attachmentResponse.getData()) {
	 *
	 *       System.out.println("------------------------------");
	 *       System.out.println("CreatedDate " + attachment.getCreatedBy());
	 *       System.out.println("Filename " + attachment.getFilename());
	 *       System.out.println("Format " + attachment.getFormat());
	 *       System.out.println("Id " + attachment.getId());
	 *       System.out.println("Md5checksum " + attachment.getMd5checksum());
	 *       System.out.println("Size " + attachment.getSize());
	 *       System.out.println("Version " + attachment.getVersion());
	 *
	 *       if (attachment.getVersions() != null) {
	 *         for (ObjectAttachment.Version version : attachment.getVersions()) {
	 *           System.out.println("Url " + version.getUrl());
	 *           System.out.println("Version " + version.getVersion());
	 *           System.out.println("");
	 *         }
	 *       }
	 *     }
	 *   }</pre>
	 */
	public ObjectRecordAttachmentResponse retrieveObjectRecordAttachments(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Retrieve Object Record Attachment Metadata
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId attachment id
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-metadata</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse metadataResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 				.retrieveObjectRecordAttachmentMetadata(objectName,recordId,attachmentId);</pre>
	 * @vapil.response <pre>
	 * if (metadataResponse.getData() != null) {
	 *     for (ObjectAttachment attachment : metadataResponse.getData()) {
	 *
	 *       System.out.println("------------------------------");
	 *       System.out.println("CreatedDate " + attachment.getCreatedBy());
	 *       System.out.println("Filename " + attachment.getFilename());
	 *       System.out.println("Format " + attachment.getFormat());
	 *       System.out.println("Id " + attachment.getId());
	 *       System.out.println("Md5checksum " + attachment.getMd5checksum());
	 *       System.out.println("Size " + attachment.getSize());
	 *       System.out.println("Version " + attachment.getVersion());
	 *
	 *       if (attachment.getVersions() != null) {
	 *         for (ObjectAttachment.Version version : attachment.getVersions()) {
	 *           System.out.println("Url " + version.getUrl());
	 *           System.out.println("Version " + version.getVersion());
	 *           System.out.println("");
	 *         }
	 *       }
	 *     }
	 *  }</pre>
	 */
	public ObjectRecordAttachmentResponse retrieveObjectRecordAttachmentMetadata(String objectName, String recordId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Retrieve Object Record Attachment Versions
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId attachment id
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-versions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-versions</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse versionsResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.retrieveObjectRecordAttachmentVersions(objectName,recordId,attachmentId);</pre>
	 * @vapil.response <pre>
	 * if (versionsResponse.getData() != null) {
	 *     for (ObjectAttachment attachment : versionsResponse.getData()) {
	 *
	 *       System.out.println("------------------------------");
	 *       System.out.println("CreatedDate " + attachment.getCreatedBy());
	 *       System.out.println("Filename " + attachment.getFilename());
	 *       System.out.println("Format " + attachment.getFormat());
	 *       System.out.println("Id " + attachment.getId());
	 *       System.out.println("Md5checksum " + attachment.getMd5checksum());
	 *       System.out.println("Size " + attachment.getSize());
	 *       System.out.println("Version " + attachment.getVersion());
	 *
	 *       if (attachment.getVersions() != null) {
	 *         for (ObjectAttachment.Version version : attachment.getVersions()) {
	 *           System.out.println("Url " + version.getUrl());
	 *           System.out.println("Version " + version.getVersion());
	 *           System.out.println("");
	 *         }
	 *       }
	 *     }
	 *  } </pre>
	 */
	public ObjectRecordAttachmentResponse retrieveObjectRecordAttachmentVersions(String objectName, String recordId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_VERSIONS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Retrieve Object Record Attachment Version Metadata
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId attachment id
	 * @param versionId    version of the attachment
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/version/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-version-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-record-attachment-version-metadata</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse versionMetadataResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 				.retrieveObjectRecordAttachmentVersionMetadata(objectName,recordId,attachmentId,versionId);</pre>
	 * @vapil.response <pre>
	 * if (versionMetadataResponse.getData() != null) {
	 *     for (ObjectAttachment attachment : versionMetadataResponse.getData()) {
	 *
	 *       System.out.println("------------------------------");
	 *       System.out.println("CreatedDate " + attachment.getCreatedBy());
	 *       System.out.println("Filename " + attachment.getFilename());
	 *       System.out.println("Format " + attachment.getFormat());
	 *       System.out.println("Id " + attachment.getId());
	 *       System.out.println("Md5checksum " + attachment.getMd5checksum());
	 *       System.out.println("Size " + attachment.getSize());
	 *       System.out.println("Version " + attachment.getVersion());
	 *
	 *       if (attachment.getVersions() != null) {
	 *         for (ObjectAttachment.Version version : attachment.getVersions()) {
	 *           System.out.println("Url " + version.getUrl());
	 *           System.out.println("Version " + version.getVersion());
	 *           System.out.println("");
	 *         }
	 *       }
	 *     }
	 *  }</pre>
	 */
	public ObjectRecordAttachmentResponse retrieveObjectRecordAttachmentVersionMetadata(String objectName, String recordId, int attachmentId, int versionId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_VERSION);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));
		url = url.replace("{attachment_version}", String.valueOf(versionId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Download Object Record Attachment File
	 * <p>
	 * Download the attachment and write the output to the buffer,
	 * which can be retrieved via the VaultResponse.
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-object-record-attachment-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-object-record-attachment-file</a>
	 * @vapil.request <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).downloadObjectRecordAttachmentFile(
	 * 					objectName,
	 * 					recordId,
	 * 					attachmentId);</pre>
	 * @vapil.response <pre>System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   OutputStream os = new FileOutputStream(testDir + "retrieveObjectRecordAttachmentFile_bytearray.pdf");
	 *   os.write(VaultResponse.getBinaryContent());
	 *   os.close();
	 * }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 * @vapil.request <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.setOutputPath(testDir + "retrieveObjectRecordAttachmentFile_localfile.pdf")
	 * 					.downloadObjectRecordAttachmentFile(objectName,recordId,attachmentId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   String filePath = VaultResponse.getOutputFilePath();
	 *   System.out.println(filePath);
	 * }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 */
	public VaultResponse downloadObjectRecordAttachmentFile(String objectName, String recordId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_FILE);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Download Object Record Attachment Version File
	 * <p>
	 * Download the attachment and write the output to the buffer,
	 * which can be retrieved via the VaultResponse.
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @param versionId    version id of the attachment
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions/{attachment_version}/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-object-record-attachment-version-file' target='_blank'>https://developer.veevavault.com/api/23.1/#download-object-record-attachment-version-file</a>
	 * @vapil.request <i>Example 1</i>
	 * <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 				.downloadObjectRecordAttachmentVersionFile(objectName,recordId,attachmentId,versionId);</pre>
	 * @vapil.response <pre>
	 * <i>Example 1</i>
	 * System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   OutputStream os = new FileOutputStream(testDir + "retrieveObjectRecordAttachmentVersionFile_bytearray.pdf");
	 *   os.write(VaultResponse.getBinaryContent());
	 *   os.close();
	 * }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 * @vapil.request <i>Example 2</i>
	 * <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.setOutputPath(testDir + "retrieveObjectRecordAttachmentVersionFile_localfile.pdf")
	 * 					.downloadObjectRecordAttachmentVersionFile(
	 * 							objectName,
	 * 							recordId,
	 * 							attachmentId,
	 * 							versionId);</pre>
	 * @vapil.response <pre>
	 * <i>Example 2</i>
	 * System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   String filePath = VaultResponse.getOutputFilePath();
	 *   System.out.println(filePath);
	 * }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 */
	public VaultResponse downloadObjectRecordAttachmentVersionFile(String objectName, String recordId, int attachmentId, int versionId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_VERSION_FILE);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));
		url = url.replace("{attachment_version}", String.valueOf(versionId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Download All Object Record Attachment Files
	 *
	 * @param objectName Object name for the attachment
	 * @param recordId   Object record id for the attachment
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/file</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#download-all-object-record-attachment-files' target='_blank'>https://developer.veevavault.com/api/23.1/#download-all-object-record-attachment-files</a>
	 * @vapil.request <i>Example 1</i>
	 * <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).downloadAllObjectRecordAttachmentFiles(
	 * 					objectName,
	 * 					recordId);</pre>
	 * @vapil.response <pre>System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   OutputStream os = new FileOutputStream(testDir + "downloadAllObjectRecordAttachmentFiles_bytearray.zip");
	 *   os.write(VaultResponse.getBinaryContent());
	 *   os.close();
	 * }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 * @vapil.request <i>Example 2</i>
	 * <pre>
	 * VaultResponse VaultResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.setOutputPath(testDir + "downloadAllObjectRecordAttachmentFiles_localfile.zip")
	 * 					.downloadAllObjectRecordAttachmentFiles(
	 * 							objectName,
	 * 							recordId);</pre>
	 * @vapil.response <pre>System.out.println(VaultResponse.getResponseStatus());
	 *
	 * if (VaultResponse.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   String filePath = VaultResponse.getOutputFilePath();
	 *   System.out.println(filePath);
	 *   }
	 * else {
	 *   System.out.println(VaultResponse.getResponseJSON());
	 * }</pre>
	 */
	public VaultResponse downloadAllObjectRecordAttachmentFiles(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_FILES);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * Create Object Record Attachment
	 * <br>
	 * Create a single object record attachment. If the attachment already exists,
	 * Vault uploads the attachment as a new version of the existing attachment.
	 * <p>
	 * This method sends a local file for the attachment.
	 *
	 * @param objectName Object name for the attachment
	 * @param recordId   Object record id for the attachment Object record id for the attachment
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/{object_record_id}/attachments</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-object-record-attachment' target='_blank'>https://developer.veevavault.com/api/23.1/#create-object-record-attachment</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse createResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 						.setInputPath(testFile.getAbsolutePath())
	 * 						.createObjectRecordAttachment(
	 * 								objectName,
	 * 								recordId);</pre>
	 * @vapil.response <pre>System.out.println(createResponse.toString());</pre>
	 */
	public ObjectRecordAttachmentResponse createObjectRecordAttachment(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFileMultiPart("file", inputPath);

		if (binaryFile != null)
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());

		return send(HttpMethod.POST, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * You can create object record attachments in bulk with a JSON or CSV input file.
	 * You must first load the attachments to the FTP staging server.
	 * If the attachment already exists in your vault, Vault uploads it as a new version of the existing attachment.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-multiple-object-record-attachments' target='_blank'>https://developer.veevavault.com/api/23.1/#create-multiple-object-record-attachments</a>
	 */
	public ObjectRecordAttachmentResponse createMultipleObjectRecordAttachments(String objectName) {
		return sendObjectRecordAttachmentRequest(objectName,
				HttpMethod.POST,
				vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS_BATCH));
	}

	/**
	 * Restore Object Record Attachment Version
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @param versionId    Document version id
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}/versions/{attachment_version}?restore=true</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#restore-object-record-attachment-version' target='_blank'>https://developer.veevavault.com/api/23.1/#restore-object-record-attachment-version</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse restoreResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 					.restoreObjectRecordAttachmentVersion(objectName,recordId,attachmentId,versionId);</pre>
	 * @vapil.response <pre>System.out.println(restoreResponse.getResponseJSON());</pre>
	 */
	public ObjectRecordAttachmentResponse restoreObjectRecordAttachmentVersion(String objectName, String recordId, int attachmentId, int versionId) {

		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_VERSION);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));
		url = url.replace("{attachment_version}", String.valueOf(versionId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addQueryParam("restore", "true");

		return send(HttpMethod.POST, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Update Object Record Attachment Description
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @param description  Attachment description
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-object-record-attachment-description' target='_blank'>https://developer.veevavault.com/api/23.1/#update-object-record-attachment-description</a>
	 */
	public ObjectRecordAttachmentResponse updateObjectRecordAttachmentDescription(String objectName, String recordId, int attachmentId, String description) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("description__v", description);

		return send(HttpMethod.PUT, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Update object record attachments in bulk with a JSON or CSV input file.
	 * You can only update the latest version of an attachment.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/vobjects/{object_name}/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-multiple-object-record-attachment-descriptions' target='_blank'>https://developer.veevavault.com/api/23.1/#update-multiple-object-record-attachment-descriptions</a>
	 */
	public ObjectRecordAttachmentResponse updateMultipleObjectRecordAttachments(String objectName) {
		return sendObjectRecordAttachmentRequest(objectName,
				HttpMethod.PUT,
				vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS_BATCH));
	}

	/**
	 * Delete Object Record Attachment
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-object-record-attachment' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-object-record-attachment</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse deleteResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 				.deleteObjectRecordAttachment(objectName,recordId,attachmentId);</pre>
	 * @vapil.response <pre>System.out.println(deleteResponse.getResponseJSON());</pre>
	 */
	public ObjectRecordAttachmentResponse deleteObjectRecordAttachment(String objectName, String recordId, int attachmentId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Delete object record attachments in bulk with a JSON or CSV input file.
	 * You can only delete the latest version of an attachment.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/vobjects/{object_name}/attachments/batch</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-multiple-object-record-attachments' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-multiple-object-record-attachments</a>
	 */
	public ObjectRecordAttachmentResponse deleteMultipleObjectRecordAttachments(String objectName) {
		return sendObjectRecordAttachmentRequest(objectName,
				HttpMethod.DELETE,
				vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENTS_BATCH));
	}

	/**
	 * Delete Object Record Attachment Version
	 *
	 * @param objectName   Object name for the attachment
	 * @param recordId     Object record id for the attachment
	 * @param attachmentId The attachment id
	 * @param versionId    Attachment version
	 * @return ObjectRecordAttachmentResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/vobjects/{object_name}/{object_record_id}/attachments/{attachment_id}versions/{attachment_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-object-record-attachment-version' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-object-record-attachment-version</a>
	 * @vapil.request <pre>
	 * ObjectRecordAttachmentResponse deleteResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
	 * 				.deleteObjectRecordAttachmentVersion(objectName,recordId,attachmentId,versionId);</pre>
	 * @vapil.response <pre>System.out.println(deleteResponse.getResponseJSON());</pre>
	 */
	public ObjectRecordAttachmentResponse deleteObjectRecordAttachmentVersion(String objectName, String recordId, int attachmentId, int versionId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_REC_ATTACHMENT_VERSION);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);
		url = url.replace("{attachment_id}", String.valueOf(attachmentId));
		url = url.replace("{attachment_version}", String.valueOf(versionId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Private method to combine create/update/delete methods
	 * to a single call
	 *
	 * @param objectName The object name for the operation
	 * @param method     The HTTP Method to perform
	 * @return ObjectRecordAttachmentResponse
	 */
	private ObjectRecordAttachmentResponse sendObjectRecordAttachmentRequest(
			String objectName,
			HttpMethod method,
			String url) {
		if (!isValidCRUDRequest()) return null;

		url = url.replace("{object_name}", objectName);

		ObjectMapper objectMapper = getBaseObjectMapper();

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (idParam != null && !idParam.isEmpty())
			request.addQueryParam(ID_PARAM, idParam);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(headerContentType, requestString);

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV)) {
			if (outputPath != null) {
				return sendToFile(method, request, outputPath, ObjectRecordAttachmentResponse.class);
			} else {
				return sendReturnBinary(method, request, ObjectRecordAttachmentResponse.class);
			}
		} else
			return send(method, request, objectMapper, ObjectRecordAttachmentResponse.class);
	}

	/**
	 * Determine if the Create-Read-Update-Delete request
	 * is properly formed before sending
	 *
	 * @return True if the request is properly formed
	 */
	private boolean isValidCRUDRequest() {
		if (headerContentType == null) {
			log.error("Invalid request - no content type is set");
			return false;
		}

		// Verify there is data - file, binary, or name/value pairs
		if (inputPath == null || inputPath.isEmpty()) {
			if (requestString == null || requestString.isEmpty()) {
				if (binaryFile == null || binaryFile.getBinaryContent() == null) {
					log.error("Invalid request - no source data");
					return false;
				}
			}
		}
		return true;
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setAcceptCSV() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Specify a value for the idParam (if not using id)
	 *
	 * @param idParam External Id field API name for the UPDATE and DELETE
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setIdParam(String idParam) {
		this.idParam = idParam;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public ObjectRecordAttachmentRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}

}
