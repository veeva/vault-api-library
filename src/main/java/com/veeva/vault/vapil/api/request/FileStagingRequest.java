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


/**
 * File Staging Requests
 * <ul>
 * <li>List Items at a Path</li>
 * <li>Download Item Content</li>
 * <li>Create Folder or File</li>
 * <li>Update Folder or File</li>
 * <li>Update File or Folder</li>
 * <li>Resumable Uploads</li>
 * </ul>
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#file-staging">https://developer.veevavault.com/api/23.1/#file-staging</a>
 */
public class FileStagingRequest extends VaultRequest {

	public static final String HTTP_HEADER_FILE_PART_NUMBER = "X-VaultAPI-FilePartNumber";

	// API Endpoints
	private static final String URL_FILE_STAGING_LIST_ITEMS_IN_PATH = "/services/file_staging/items/{item}";
	private static final String URL_FILE_STAGING_GET_ITEM_CONTENT = "/services/file_staging/items/content/{item}";
	private static final String URL_FILE_STAGING_CREATE_FILE_OR_FOLDER = "/services/file_staging/items";
	private static final String URL_FILE_STAGING_UPDATE_OR_DELETE_FILE_OR_FOLDER = "/services/file_staging/items/{item}";
	private static final String URL_FILE_STAGING_CREATE_RESUMABLE_UPLOAD_SESSION = "/services/file_staging/upload";
	private static final String URL_FILE_STAGING_RESUMABLE_SESSION = "/services/file_staging/upload/{upload_session_id}";
	private static final String URL_FILE_STAGING_RESUMABLE_SESSION_PARTS = "/services/file_staging/upload/{upload_session_id}/parts";

	// API Request Parameters
	private Boolean recursive;
	private Integer limit;
	private ResultsFormat formatResult;
	private HttpRequestConnector.BinaryFile file;
	private String contentMD5;
	private String range;
	private Integer size;
	private Boolean overwrite;
	private String inputPath;
	private String parent;
	private String name;
	private String outputPath;

	// API Request Parameter Constants
	public static final String RECURSIVE_PARAMETER = "recursive";
	public static final String LIMIT_PARAMETER = "limit";
	public static final String FORMAT_RESULT_PARAMETER = "format_result";
	public static final String KIND_PARAMETER = "kind";
	public static final String PATH_PARAMETER = "path";
	public static final String SIZE_PARAMETER = "size";
	public static final String OVERWRITE_PARAMETER = "overwrite";
	public static final String FILE_PARAMETER = "file";
	public static final String PARENT_PARAMETER = "parent";
	public static final String NAME_PARAMETER = "name";
	public static final String CHUNK_PARAMETER = "@/chunk-ab.";

	/*
	 *
	 * List Items In A Path
	 *
	 */

	/**
	 * <b>List Items At A Path</b>
	 * <p>
	 * Return a list of files and folders for the specified path.
	 * Paths are different for Admin users (Vault Owners and System Admins) and non-Admin users.
	 *
	 * @param item, the file path of the item
	 * @return FileStagingItemBulkResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/items/{item} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#list-items-at-a-path' target='_blank'>https://developer.veevavault.com/api/23.1/#list-items-at-a-path</a>
	 * @vapil.request <pre>FileStagingItemBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class).listItemsAtAPath("Documents");</pre>
	 * @vapil.response <pre>
	 *     for(FileStagingItem fileStagingItem : resp.getData()) {
	 *         System.out.println(fileStagingItem);
	 *     }
	 * </pre>
	 */
	public FileStagingItemBulkResponse listItemsAtAPath(String item) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_LIST_ITEMS_IN_PATH).replace("{item}", getItemAsUrl(item));
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (this.recursive != null) {
			request.addQueryParam(RECURSIVE_PARAMETER, this.recursive);
		}

		if (this.limit != null) {
			request.addQueryParam(LIMIT_PARAMETER, this.limit);
		}
		if (this.formatResult != null) {
			request.addQueryParam(FORMAT_RESULT_PARAMETER, this.formatResult.getValue());
		}
		return send(HttpMethod.GET, request, FileStagingItemBulkResponse.class);
	}

	/**
	 * <b>List Items By Page</b>
	 * <p>
	 * Return a list of files and folders for the specified page url.
	 *
	 * @param pageUrl, full path to the page (including https://{vaultDNS}/api/{version}/)
	 * @return FileStagingItemBulkResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/items/{item} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#list-items-at-a-path' target='_blank'>https://developer.veevavault.com/api/23.1/#list-items-at-a-path</a>
	 * @vapil.request <pre>FileStagingItemBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class)
	 * 		.listItemsAtPathByPage(pageUrl);</pre>
	 * @vapil.response <pre>
	 *     for(FileStagingItem fileStagingItem : resp.getData()) {
	 *         System.out.println(fileStagingItem);
	 *     }
	 * </pre>
	 */
	public FileStagingItemBulkResponse listItemsAtPathByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, FileStagingItemBulkResponse.class);
	}

	/*
	 *
	 * Download Item Content
	 *
	 */

	/**
	 * <b>Download Item Content</b>
	 * <p>
	 * Retrieve the content of a specified file from the file staging server.
	 * Use the Range header to create resumable downloads for large files, or to continue downloading a file if your session is interrupted.
	 *
	 * @param item, the file path of the item
	 * @return VaultResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/items/content/{item} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#get-item-content' target='_blank'>https://developer.veevavault.com/api/23.1/#get-item-content</a>
	 * @vapil.request <pre>VaultResponse resp = vaultClient.newRequest(FileStagingRequest.class).downloadItemContent("Documents/New/file.txt");</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * </pre>
	 */
	public VaultResponse downloadItemContent(String item) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_GET_ITEM_CONTENT).replace("{item}", getItemAsUrl(item));
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (this.range != null) {
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_RANGE, this.range);
		}

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}


	/*
	 *
	 * Create Folder or File
	 *
	 */

	/**
	 * <b>Create Folder or File</b>
	 * <p>
	 * Upload files or folders up to 50MB to the File Staging Server.
	 *
	 * @param kind, a Kind enum value representing the type of the item. Can be either FILE or FOLDER type
	 * @param path, a String value indicating the path of the item
	 * @return FileStagingItemResponse
	 * @vapil.api <pre> POST /api/{version}/services/file_staging/items </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-folder-or-file' target='_blank'>https://developer.veevavault.com/api/23.1/#create-folder-or-file</a>
	 * @vapil.request <pre>FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);</pre>
	 * @vapil.response <pre>FileStagingItemResponse resp = fileStagingRequest.createFolderOrFile(fileStagingResumableUploadSession);</pre>
	 */
	public FileStagingItemResponse createFolderOrFile(Kind kind, String path) {

		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_FILE_STAGING_CREATE_FILE_OR_FOLDER));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.contentMD5 != null) {
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_MD5, contentMD5);
		}

		request.addBodyParam(KIND_PARAMETER, kind.getValue());
		request.addBodyParam(PATH_PARAMETER, path);

		if (this.overwrite != null) {
			request.addBodyParam(OVERWRITE_PARAMETER, overwrite);
		}

		if (this.inputPath != null) {
			request.addFileMultiPart(FILE_PARAMETER, inputPath);
		}
		if (this.file != null) {
			request.addFileBinary(FILE_PARAMETER, file.getBinaryContent(), file.getFileName());
		}

		return send(HttpMethod.POST, request, FileStagingItemResponse.class);
	}

	/*
	 *
	 * Update Folder or File
	 *
	 */

	/**
	 * <b>Update Folder or File</b>
	 * <p>
	 * Move or rename a folder or file on the file staging server. You can move and rename an item in the same request.
	 *
	 * @param item, The absolute path to a file or folder. This path is specific to the authenticated user.
	 * @return FileStagingJobResponse
	 * @vapil.api <pre> PUT /api/{version}/services/file_staging/items/{item} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-folder-or-file' target='_blank'>https://developer.veevavault.com/api/23.1/#update-folder-or-file</a>
	 * @vapil.request <pre>FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);</pre>
	 * @vapil.response <pre>
	 *     FileStagingJobResponse resp = fileStagingRequest.updateFolderOrFile("test.txt", fileStagingResumableUploadSession);
	 *     Integer jobId = resp.getData().getJobId();
	 * </pre>
	 */
	public FileStagingJobResponse updateFolderOrFile(String item) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_UPDATE_OR_DELETE_FILE_OR_FOLDER).replace("{item}", getItemAsUrl(item));
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.parent != null) {
			request.addBodyParam(PARENT_PARAMETER, parent);
		}

		if (this.name != null) {
			request.addBodyParam(NAME_PARAMETER, name);
		}

		return send(HttpMethod.PUT, request, FileStagingJobResponse.class);
	}

	/*
	 *
	 * Delete File or Folder
	 *
	 */

	/**
	 * <b>Delete File or Folder</b>
	 * <p>
	 * Delete an individual file or folder from the file staging server.
	 *
	 * @param item, The absolute path to a file or folder. This path is specific to the authenticated user.
	 * @return FileStagingJobResponse
	 * @vapil.api <pre> DELETE /api/{version}/services/file_staging/items/{item} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-folder-or-file' target='_blank'>https://developer.veevavault.com/api/23.1/#update-folder-or-file</a>
	 * @vapil.request <pre>FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);</pre>
	 * @vapil.response <pre>
	 *     FileStagingJobResponse resp = fileStagingRequest.deleteFolderOrFile("text.txt");
	 *     Integer jobId = resp.getData().getJobId();
	 * </pre>
	 */
	public FileStagingJobResponse deleteFolderOrFile(String item) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_UPDATE_OR_DELETE_FILE_OR_FOLDER).replace("{item}", getItemAsUrl(item));
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (this.recursive != null) {
			request.addQueryParam("recursive", recursive);
		}

		return send(HttpMethod.DELETE, request, FileStagingJobResponse.class);
	}

	/*
	 *
	 * Create Resumable Upload Session
	 *
	 */

	/**
	 * <b>Create Resumable Upload Session</b>
	 * <p>
	 * Initiate a multipart upload session and return an upload session ID.
	 * The upload_session_id, can use to upload file parts, resume an interrupted session, retrieve information about a session, and, if necessary, abort a session.
	 *
	 * @param path, the path to the item in the file staging area
	 * @param size, the total size in bytes of the file
	 * @return FileStagingSessionResponse
	 * @vapil.api <pre> POST /api/{version}/services/file_staging/upload </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-resumable-upload-session' target='_blank'>https://developer.veevavault.com/api/23.1/#create-resumable-upload-session</a>
	 * @vapil.request <pre>FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);</pre>
	 * @vapil.response <pre>
	 *     FileStagingSessionResponse resp = fileStagingRequest.createResumableUploadSession(fileStagingResumableUploadSession);
	 *     String uploadSessionId = resp.getData().getUploadSessionID();
	 * </pre>
	 */
	public FileStagingSessionResponse createResumableUploadSession(String path, int size) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_FILE_STAGING_CREATE_RESUMABLE_UPLOAD_SESSION));

		request.addBodyParam(PATH_PARAMETER, path);
		request.addBodyParam(SIZE_PARAMETER, size);

		if (this.overwrite != null) {
			request.addBodyParam(OVERWRITE_PARAMETER, overwrite);
		}

		return send(HttpMethod.POST, request, FileStagingSessionResponse.class);
	}

	/*
	 *
	 * Upload to a Session
	 *
	 */

	/**
	 * <b>Upload to a Session</b>
	 * <p>
	 * The session owner can upload parts of a file to an active upload session.
	 * By default, you can upload up to 2000 parts per upload session, and each part can be up to 50MB.
	 * Use the Range header to specify the range of bytes for each upload, or split files into parts and add each part as a separate file.
	 * Each part must be the same size, except for the last part in the upload session.
	 *
	 * @param uploadSessionId, the session id of the resumable upload session
	 * @param partNumber       The part number, which uniquely identifies a file part and defines its position within the file as a whole.
	 * @return FileStagingSessionResponse
	 * @vapil.api <pre> PUT /api/{version}/services/file_staging/upload/{upload_session_id} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upload-to-a-session' target='_blank'>https://developer.veevavault.com/api/23.1/#upload-to-a-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>
	 *     FileStagingSessionResponse resp = fileStagingRequest.uploadToASession(uploadSessionId, 1);
	 * </pre>
	 */
	public FileStagingSessionResponse uploadToASession(String uploadSessionId, int partNumber) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_RESUMABLE_SESSION).replace("{upload_session_id}", uploadSessionId);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_OCTET);
		request.addHeaderParam(HTTP_HEADER_FILE_PART_NUMBER, partNumber);

		if (this.contentMD5 != null) {
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_MD5, this.contentMD5);
		}

		if (this.file != null) {
			byte[] binaryContent = file.getBinaryContent();
			request.addBinary(CHUNK_PARAMETER, binaryContent);
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_LENGTH, binaryContent.length);
		}

		return send(HttpMethod.PUT, request, FileStagingSessionResponse.class);
	}

	/*
	 *
	 * Commit Upload Session
	 *
	 */

	/**
	 * <b>Commit Upload Session</b>
	 * <p>
	 * Mark an upload session as complete and assemble all previously uploaded parts to create a file.
	 *
	 * @param uploadSessionId, the session id of the resumable upload session
	 * @return FileStagingJobResponse
	 * @vapil.api <pre> POST /api/{version}/services/file_staging/upload/{upload_session_id} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#commit-upload-session' target='_blank'>https://developer.veevavault.com/api/23.1/#commit-upload-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>FileStagingJobResponse resp = fileStagingRequest.uploadToASession(uploadSessionId);</pre>
	 */
	public FileStagingJobResponse commitUploadSession(String uploadSessionId) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_RESUMABLE_SESSION).replace("{upload_session_id}", uploadSessionId);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, FileStagingJobResponse.class);
	}

	/*
	 *
	 * List Upload Sessions
	 *
	 */

	/**
	 * <b>List Upload Sessions</b>
	 * <p>
	 * Return a list of active upload sessions
	 *
	 * @return FileStagingSessionBulkResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/upload </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#list-upload-sessions' target='_blank'>https://developer.veevavault.com/api/23.1/#commit-upload-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>
	 *     FileStagingSessionBulkResponse resp =  fileStagingRequest.listUploadSessions();
	 *     for(FileStagingSession fileStagingSession : resp.getData()) {
	 * 			System.out.println(fileStagingSession);
	 *     }
	 * </pre>
	 */
	public FileStagingSessionBulkResponse listUploadSessions() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_FILE_STAGING_CREATE_RESUMABLE_UPLOAD_SESSION));
		return send(HttpMethod.GET, request, FileStagingSessionBulkResponse.class);
	}

	/**
	 * <b>List Upload Sessions</b>
	 * <p>
	 * Return a list of active upload sessions using the previous_page or next_page parameter of a previous request
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return FileStagingSessionBulkResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/upload </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#list-upload-sessions' target='_blank'>https://developer.veevavault.com/api/23.1/#commit-upload-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest paginatedResponse = vaultClient.newRequest(JobRequest.class)
	 *			.retrieveJobTasksByPage(response.getResponseDetails().getNextPage());</pre>
	 * @vapil.response <pre>System.out.println(paginatedResponse.getResponseStatus())</pre>;
	 */
	public FileStagingSessionBulkResponse listUploadSessionsByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, FileStagingSessionBulkResponse.class);
	}

	/*
	 *
	 * Get Session Details
	 *
	 */

	/**
	 * <b>Get Session Details</b>
	 * <p>
	 * Retrieve the details of an active upload session.
	 * Admin users can get details for all sessions, while non-Admin users can only get details for sessions if they are the owner.
	 *
	 * @param uploadSessionId, the session id of the resumable upload session
	 * @return FileStagingSessionResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/upload/{upload_session_id} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#get-upload-session-details' target='_blank'>https://developer.veevavault.com/api/23.1/#get-upload-session-details</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>
	 *    FileStagingSessionResponse resp = fileStagingRequest.getUploadSessionDetails(uploadSessionId);
	 *    System.out.println(resp.getSession().getCreatedDate());
	 *
	 * </pre>
	 */
	public FileStagingSessionResponse getUploadSessionDetails(String uploadSessionId) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_RESUMABLE_SESSION).replace("{upload_session_id}", uploadSessionId);
		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, FileStagingSessionResponse.class);
	}

	/*
	 *
	 * List File Parts In A Session
	 *
	 */

	/**
	 * <b>List File Parts In A Session</b>
	 * <p>
	 * Return a list of parts uploaded in a session. You must be an Admin user or the session owner.
	 *
	 * @param uploadSessionId, the session id of the resumable upload session
	 * @return FileStagingSessionBulkResponse
	 * @vapil.api <pre> GET /api/{version}/services/file_staging/upload/{upload_session_id}/parts </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#list-file-parts-uploaded-to-session' target='_blank'>https://developer.veevavault.com/api/23.1/#list-file-parts-uploaded-to-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>
	 *     FileStagingSessionBulkResponse resp =  fileStagingRequest.listFilePartsUploadedToASession();
	 *     for(FileStagingSession fileStagingSession : resp.getData()) {
	 * 			System.out.println(fileStagingSession.getSize());
	 *     }
	 * </pre>
	 */
	public FileStagingSessionBulkResponse listFilePartsUploadedToASession(String uploadSessionId) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_RESUMABLE_SESSION_PARTS).replace("{upload_session_id}", uploadSessionId);
		HttpRequestConnector request = new HttpRequestConnector(url);
		if (limit != null) {
			request.addQueryParam("limit", limit);
		}

		return send(HttpMethod.GET, request, FileStagingSessionBulkResponse.class);
	}

	/*
	 *
	 * Abort Upload Session
	 *
	 */

	/**
	 * <b>Abort Upload Session</b>
	 * <p>
	 * Abort an active upload session and purge all uploaded file parts.
	 * Admin users can see and abort all upload sessions, while non-Admin users can only see and abort sessions where they are the owner.
	 *
	 * @param uploadSessionId, the session id of the resumable upload session
	 * @return VaultResponse
	 * @vapil.api <pre> DELETE /api/{version}/services/file_staging/upload/{upload_session_id} </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#abort-upload-session' target='_blank'>https://developer.veevavault.com/api/23.1/#abort-upload-session</a>
	 * @vapil.request <pre>
	 *     FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
	 * </pre>
	 * @vapil.response <pre>
	 *     VaultResponse resp =  fileStagingRequest.abortUploadSession(uploadSessionId);
	 * </pre>
	 */
	public VaultResponse abortUploadSession(String uploadSessionId) {
		String url = vaultClient.getAPIEndpoint(URL_FILE_STAGING_RESUMABLE_SESSION).replace("{upload_session_id}", uploadSessionId);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * Gets the item value as a url path. If null or forward slash, an empty string is returned
	 *
	 * @param item The absolute path to a file or folder.
	 * @return The Request
	 */
	private String getItemAsUrl(String item) {
		if (item == null || item.equals("/"))
			return org.apache.commons.lang.StringUtils.EMPTY;
		else if (item.startsWith("/"))
			return item.substring(1);
		else
			return item;
	}

	/**
	 * Request constants
	 */

	/**
	 * Enum ResultsFormat represents the format that the results can be in
	 * Option(s): CSV
	 */
	public enum ResultsFormat {
		CSV("CSV");

		private final String value;

		ResultsFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Enum Kind represents the kind of item to create.
	 * Option(s): file, folder
	 */
	public enum Kind {
		FILE("file"),
		FOLDER("folder");

		private final String kind;

		Kind(String kind) {
			this.kind = kind;
		}

		public String getValue() {
			return kind;
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set recursive flag
	 *
	 * @param recursive a Boolean to set the recursive value
	 * @return The Request
	 */
	public FileStagingRequest setRecursive(Boolean recursive) {
		this.recursive = recursive;
		return this;
	}

	/**
	 * Set the limit of items return by list items in a path
	 *
	 * @param limit max number of items per page in the response
	 * @return The Request
	 */
	public FileStagingRequest setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Set the results format
	 *
	 * @param formatResult, a ResultsFormat enum value
	 * @return The Request
	 */
	public FileStagingRequest setResultsFormat(ResultsFormat formatResult) {
		this.formatResult = formatResult;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename    file name (no path)
	 * @param fileContent byte array of the file content
	 * @return The Request
	 */
	public FileStagingRequest setFile(String filename, byte[] fileContent) {
		this.file = new HttpRequestConnector.BinaryFile(filename, fileContent);
		return this;
	}

	/**
	 * Specify the MD5 Hash of the file or file part
	 *
	 * @param contentMD5, the md5 hash of the file
	 * @return The Request
	 */
	public FileStagingRequest setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
		return this;
	}

	/**
	 * Specify the partial range of bytes to be used for the upload/download
	 *
	 * @param range, the range of bytes
	 * @return The Request
	 */
	public FileStagingRequest setRange(String range) {
		this.range = range;
		return this;
	}

	/**
	 * Specify the size of the file in bytes. Not applicable to folders.
	 *
	 * @param size, the range of bytes
	 * @return The Request
	 */
	public FileStagingRequest setSize(Integer size) {
		this.size = size;
		return this;
	}

	/**
	 * Set overwrite flag; does not apply to folders
	 *
	 * @param overwrite a Boolean to set the overwrite value
	 * @return The Request
	 */
	public FileStagingRequest setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public FileStagingRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify the absolute path to the parent directory in which to place the file.
	 *
	 * @param parent Absolute path to the file for the request
	 * @return The Request
	 */
	public FileStagingRequest setParent(String parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Specify the new name when renaming a file or folder
	 *
	 * @param name Absolute path to the file for the request
	 * @return The Request
	 */
	public FileStagingRequest setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public FileStagingRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}
}