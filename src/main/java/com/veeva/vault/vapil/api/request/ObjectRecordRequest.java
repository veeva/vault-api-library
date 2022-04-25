/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Object Record Requests
 * <ul>
 * <li>CRUD operations (single and bulk)</li>
 * <li>Object Types</li>
 * <li>Object Roles</li>
 * <li>Cascade Deletes</li>
 * <li>Deep Copy</li>
 * <li>Other Object Record requests</li>
 * </ul>
 * <p>
 * The following properties can be set for a variety of input/output
 * options to the request, leveraging builder pattern setters.
 * <ul>
 * <li>Header Content-Type (JSON, CSV)</li>
 * <li>Header Accept Return (JSON, CSV)</li>
 * <li>Request content (JSON/CSV File or String, XFORM Body)</li>
 * <li>Migration mode parameter</li>
 * <li>Upsert parameter (idParam)</li>
 * <li>The execute method to hit the API endpoint</li>
 * </ul>
 * <p>
 * Example create request with input CSV file, receiving JSON response:<pre>
 * ObjectRecordResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
 * 			.setContentTypeCsv()
 * 			.setInputPath(inputPath)
 * 			.createObjectRecords(objectName);</pre>
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/22.1/#vault-objects">https://developer.veevavault.com/api/22.1/#vault-objects</a>
 * <p>
 * <i>The following endpoints are covered in other classes for ease of use</i>
 * <ul>
 * <li>Object Record Attachments - {@link ObjectRecordAttachmentRequest}</li>
 * <li>Retrieve Object Metadata - {@link MetaDataRequest}</li>
 * <li>Retrieve Object Field Metadata - {@link MetaDataRequest}</li>
 * </ul>
 */
public class ObjectRecordRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(ObjectRecordRequest.class);

	/**
	 * <b>X-VaultAPI-MigrationMode</b> Set to true to create object records in a specific state or state type.
	 */
	public static final String HTTP_HEADER_VAULT_MIGRATION_MODE = "X-VaultAPI-MigrationMode";

	/**
	 * <b>X-VaultAPI-UnchangedFieldBehavior</b>
	 * When upserting, set to IgnoreSetOnCreateOnly to ignore values given for the Object Type field and parent object fields
	 * if the record already exists and the value has not changed. Set to NeverIgnore to enforce edit permission for all fields
	 * in the payload, regardless of whether or not the value has changed. Read-only fields such as Object Type will fail even
	 * if the value has not changed. Set to AlwaysIgnore to ignore fields that match between the record and payload values during upsert.
	 */
	public static final String HTTP_HEADER_VAULT_UNCHANGED_FIELD_BEHAVIOR = "X-VaultAPI-UnchangedFieldBehavior";

	/**
	 * If you’re identifying object records in your input by a unique field, add idParam={fieldname} to the request endpoint.
	 */
	public static final String ID_PARAM = "idParam";

	// API Endpoints
	private static final String URL_OBJ = "/vobjects/{object_name}";
	private static final String URL_OBJ_ID = "/vobjects/{object_name}/{object_record_id}";
	private static final String URL_OBJ_CASCADE_DELETE = "/vobjects/{object_name}/{object_record_id}/actions/cascadedelete";
	private static final String URL_OBJ_CASCADE_DELETE_STATUS = "/vobjects/cascadedelete/results/{object_name}/{job_status}/{job_id}";
	private static final String URL_OBJ_TYPE_RETRIEVE = "/configuration/Objecttype";
	private static final String URL_OBJ_TYPE_RETRIEVE_OBJECT = "/configuration/Objecttype.{object_name}.{object_type}";
	private static final String URL_OBJ_TYPE_CHANGE = "/vobjects/{object_name}/actions/changetype";
	private static final String URL_OBJ_ROLE = "/vobjects/{object_name}/{id}/roles";
	private static final String URL_OBJ_ROLE_SINGLE = "/vobjects/{object_name}/{id}/roles/{role_name}";
	private static final String URL_OBJ_ROLE_ADD_REMOVE = "/vobjects/{object_name}/roles";
	private static final String URL_OBJ_DEEP_COPY = "/vobjects/{object_name}/{object_record_ID}/actions/deepcopy";
	private static final String URL_OBJ_DEEP_COPY_RESULTS = "/vobjects/deepcopy/results/{object_name}/{job_status}/{job_id}";
	private static final String URL_OBJ_RECORD_DELETED = "/objects/deletions/vobjects/{object_name}";
	private static final String URL_OBJ_LIMITS = "/limits";
	private static final String URL_OBJ_CURRENCY = "/vobjects/{object_name}/actions/updatecorporatecurrency";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private Map<String, Object> bodyParams = null;
	private String headerAccept;
	private String headerContentType;
	private List<String> fields;
	private String idParam;
	private String inputPath;
	private Boolean migrationMode;
	private String outputPath;
	private String requestString; // For raw request
	private ZonedDateTime startDateDeleted; // For deleted records
	private ZonedDateTime endDateDeleted; // For deleted records
	private Integer limit; // For deleted records
	private Integer offset; // For deleted records
	private UnchangedFieldBehaviorType unchangedFieldBehavior;

	private ObjectRecordRequest() {
		this.migrationMode = false;
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON; // default on all requests per spec
	}

	/**
	 * <b>Retrieve Object Record Collection</b>
	 * <p>
	 * Retrieve all records for a specific Vault Object.
	 * <p>
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordCollectionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-object-record' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-object-record</a>
	 * @vapil.request <pre>
	 * ObjectRecordResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 					.retrieveObjectRecordCollection(objectName);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * }</pre>
	 */
	public ObjectRecordCollectionResponse retrieveObjectRecordCollection(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ).replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (fields != null) {
			request.addQueryParam("fields", String.join(",", fields));
		}

		return send(HttpMethod.GET, request, ObjectRecordCollectionResponse.class);
	}

	/**
	 * <b>Retrieve Object Record Collection (By Page)</b>
	 * <p>
	 * Retrieve all records using the previous_page or next_page parameter of a previous request
	 * <p>
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return ObjectRecordCollectionResponse
	 * @vapil.api <pre>GET /api/{version}/vobjects/{object_name}/?offset={offset}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-object-record' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-object-record</a>
	 * @vapil.request <pre>
	 * ObjectRecordResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
	 *  			.retrieveObjectRecordCollectionByPage(pageUrl);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * }</pre>
	 */
	public ObjectRecordCollectionResponse retrieveObjectRecordCollectionByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectRecordCollectionResponse.class);
	}

	/**
	 * <b>Retrieve Object Record</b>
	 * <p>
	 * Single record transaction - Retrieve metadata (and values) configured on a single/specific object record.
	 * <p>
	 * Use bulk for multiple records
	 *
	 * @param objectName The object name for the operation
	 * @param recordId   The record id to get
	 * @return ObjectRecordResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-object-record' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-object-record</a>
	 * @vapil.request <pre>
	 * ObjectRecordResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 					.retrieveObjectRecord(objectName, id);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * ObjectRecord objectRecord = resp.getData();
	 *
	 * System.out.println("Id = " + objectRecord.getId());
	 * System.out.println("Name = " + objectRecord.getName());
	 *
	 * Map&lt;String,Object&gt; additionalFields = objectRecord.getProperties();
	 * for (String name : additionalFields.keySet())
	 * System.out.println(name + " = " + additionalFields.get(name));
	 *
	 * for (String roleName : resp.getManuallyAssignedSharingRoles().keySet()) {
	 *   System.out.println("\nRole " + roleName);
	 *
	 *   Map&lt;String,Object&gt; role = (Map&lt;String, Object&gt;) resp.getManuallyAssignedSharingRoles().get(roleName);
	 *
	 *   List&lt;Integer&gt; groups = (List&lt;Integer&gt; ) role.get("groups");
	 *   List&lt;Integer&gt;  users = (List&lt;Integer&gt; ) role.get("users");
	 *
	 *   if (groups != null) {
	 *     for (Integer groupId : groups)
	 *     System.out.println("Group = " + groupId);
	 *     }
	 *
	 *   if (users != null) {
	 *     for (Integer userId : users)
	 *     System.out.println("User = " + userId);
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectRecordResponse retrieveObjectRecord(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ID);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, ObjectRecordResponse.class);
	}

	/**
	 * <b>Create Object Records</b>
	 * <p>
	 * Create Object Records in bulk based on the set properties of the class.
	 * The maximum batch size is 500 records.
	 * Note that it is up to the calling code to batch the data.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#create-object-records' target='_blank'>https://developer.veevavault.com/api/22.1/#create-object-records</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Source - CSV file, Response - JSON</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeCsv()
	 * 				.setInputPath(inputPath)
	 * 				.createObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Source - CSV file, Response - CSV</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeCsv()
	 * 				.setAcceptCSV()
	 * 				.setInputPath(inputPath)
	 * 				.createObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - Source - CSV file, Response - JSON, Upsert operation with idParam</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeCsv()
	 * 				.setIdParam(idParam)
	 * 				.setInputPath(inputPath)
	 * 				.createObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 4 - Source - CSV file, Response - JSON, Migration Mode</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeCsv()
	 * 				.setMigrationMode(true)
	 * 				.setInputPath(inputPath)
	 * 				.createObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 5 - Source - JSON string, Response - JSON</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeJson()
	 * 				.setRequestString(requestString)
	 * 				.createObjectRecords(objectName);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 *
	 * if (resp.hasErrors()) {
	 *   System.out.println("Errors exist");
	 *
	 *   if (resp.getErrors() != null) {
	 *     for (VaultResponse.APIResponseError e : resp.getErrors())
	 *       System.out.println("VaultResponse Error " + e.getMessage());
	 *     }
	 * }
	 * if (resp.getErrors() == null) {
	 *   for (ObjectRecordResponse objectRecordResponse : resp.getData()) {
	 *     System.out.println("Record successful? " + objectRecordResponse.isSuccessful());
	 *
	 *     if (objectRecordResponse.isSuccessful())
	 *       System.out.println(objectRecordResponse.getData().getId() + " " + objectRecordResponse.getData().getUrl());
	 *
	 *     if (objectRecordResponse.getErrors() != null) {
	 *       for (VaultResponse.APIResponseError error : objectRecordResponse.getErrors()) {
	 *         System.out.println("Error " + error.getType() + " " + error.getMessage());
	 *       }
	 *     }
	 *   }
	 * }</pre>
	 */
	public ObjectRecordBulkResponse createObjectRecords(String objectName) {
		return sendObjectRecordRequest(objectName, HttpMethod.POST, vaultClient.getAPIEndpoint(URL_OBJ));
	}

	/**
	 * <b>Update Object Records</b>
	 * <p>
	 * Update Object Records in bulk based on the set properties of the class.
	 * The maximum batch size is 500 records.
	 * Note that it is up to the calling code to batch the data.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#update-object-records' target='_blank'>https://developer.veevavault.com/api/22.1/#update-object-records</a>
	 * @vapil.request See {@link #createObjectRecords(String)} for example requests (replace "createObjectRecords" with "updateObjectRecords")
	 * @vapil.response See {@link #createObjectRecords(String)} for example responses (replace "createObjectRecords" with "updateObjectRecords")
	 */
	public ObjectRecordBulkResponse updateObjectRecords(String objectName) {
		return sendObjectRecordRequest(objectName, HttpMethod.PUT, vaultClient.getAPIEndpoint(URL_OBJ));
	}

	/**
	 * <b>Delete Object Records</b>
	 * <p>
	 * Bulk delete object records. The maximum batch size is 500.
	 * Note that it is up to the calling code to batch the data.
	 *
	 * @param objectName The object name for the operation
	 * @return ObjectRecordResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#delete-object-records' target='_blank'>https://developer.veevavault.com/api/22.1/#delete-object-records</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Source - CSV file, Response - JSON</i>
	 * ObjectRecordBulkResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setContentTypeCsv()
	 * 				.setInputPath(inputPath)
	 * 				.deleteObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Additional Requests</i>
	 * See {@link #createObjectRecords(String)} for additional example requests (replace "createObjectRecords" with "deleteObjectRecords")</pre>
	 * @vapil.response See {@link #createObjectRecords(String)} for example responses (replace "createObjectRecords" with "deleteObjectRecords")
	 */
	public ObjectRecordBulkResponse deleteObjectRecords(String objectName) {
		return sendObjectRecordRequest(objectName, HttpMethod.DELETE, vaultClient.getAPIEndpoint(URL_OBJ));
	}

	/**
	 * Private helper method to combine create/update/delete methods
	 * to a single call.
	 *
	 * @param objectName The object name for the operation
	 * @param method     The HTTP Method to perform
	 * @return ObjectRecordResponse
	 */
	private ObjectRecordBulkResponse sendObjectRecordRequest(String objectName, HttpMethod method, String url) {
		if (!isValidCRUDRequest()) return null;

		url = url.replace("{object_name}", objectName);

		ObjectMapper objectMapper = getBaseObjectMapper();

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		}
		if (idParam != null && !idParam.isEmpty())
			request.addQueryParam(ID_PARAM, idParam);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		if (migrationMode != null)
			request.addHeaderParam(HTTP_HEADER_VAULT_MIGRATION_MODE, migrationMode);

		if (unchangedFieldBehavior != null)
			request.addHeaderParam(HTTP_HEADER_VAULT_UNCHANGED_FIELD_BEHAVIOR, unchangedFieldBehavior.getValue());

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(headerContentType, requestString);

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV)) {
			if (outputPath != null) {
				return sendToFile(method, request, outputPath, ObjectRecordBulkResponse.class);
			} else {
				return sendReturnBinary(method, request, ObjectRecordBulkResponse.class);
			}
		} else
			return send(method, request, objectMapper, ObjectRecordBulkResponse.class);
	}

	/**
	 * <b>Cascade Delete Object Record</b>
	 * <p>
	 * Submit a job to perform a cascade delete.
	 * The corresponding job id can be retrieved from
	 * the response via the "getJobId" method.
	 *
	 * @param objectName The object name for the operation
	 * @param id         The record id to delete
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/{object_record_id}/actions/cascadedelete</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#cascade-delete-object-record' target='_blank'>https://developer.veevavault.com/api/22.1/#cascade-delete-object-record</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.cascadeDeleteObjectRecord(objectName, id);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());
	 * System.out.println("Job Id = " + resp.getJobId());</pre>
	 */
	public JobCreateResponse cascadeDeleteObjectRecord(String objectName, String id) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_CASCADE_DELETE);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_id}", id);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV)) {
			if (outputPath != null) {
				return sendToFile(HttpMethod.POST, request, outputPath, JobCreateResponse.class);
			} else {
				return sendReturnBinary(HttpMethod.POST, request, JobCreateResponse.class);
			}
		} else
			return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Get Results of Cascade Delete Job</b>
	 * <p>
	 * After submitting a request to deep copy an object record,
	 * query vault to determine the results of the request.
	 *
	 * @param objectName The object name for the operation
	 * @param jobStatus  "success" or "failure"
	 * @param jobId      The ID of the job, retrieved from the response of the job request
	 * @return The VaultResponse in CSV byte array
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/cascadedelete/results/{object_name}/{job_status}/{job_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#get-results-of-cascade-delete-job' target='_blank'>https://developer.veevavault.com/api/22.1/#get-results-of-cascade-delete-job</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.getResultsOfCascadeDeleteJob(objectName, "success", jobId);</pre>
	 * @vapil.response <pre>
	 * System.out.println(new String(resp.getBinaryContent()));</pre>
	 */
	public VaultResponse getResultsOfCascadeDeleteJob(String objectName, String jobStatus, int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_CASCADE_DELETE_STATUS);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{job_status}", jobStatus);
		url = url.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}


	/**
	 * <b>Object Types - Retrieve Details from All Object Types</b>
	 *
	 * @return Describes of all object types
	 * @vapil.api <pre>
	 * GET /api/{version}/configuration/Objecttype</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-details-from-all-object-types' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-details-from-all-object-types</a>
	 * @vapil.request <pre>
	 * ObjectRecordTypeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.retrieveDetailsFromAllObjectTypes();</pre>
	 * @vapil.response <pre>
	 * for (ObjectRecordType rt : resp.getData()) {
	 *   System.out.println("Object " + rt.getObject() + " - " + rt.getName() + " - "+ rt.getLabel());
	 *
	 *   for (String s : rt.getAdditionalTypeValidations())
	 *     System.out.println("Validations " + s);
	 *
	 *   List&lt;ObjectRecordTypeAction&gt; actions = rt.getActions();
	 *   if (actions != null) {
	 *     for (ObjectRecordTypeAction action : rt.getActions())
	 *       System.out.println("Action " + action.getAction() + " - " + action.getName());
	 *   }
	 *
	 *   for (ObjectRecordTypeField fld : rt.getFields())
	 *     System.out.println("Field " + fld.getName() + " - " + fld.getSource() + " - " + fld.getRequired());
	 * }</pre>
	 */
	public ObjectRecordTypeResponse retrieveDetailsFromAllObjectTypes() {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_TYPE_RETRIEVE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectRecordTypeResponse.class);
	}

	/**
	 * <b>Object Types - Retrieve Details from a Specific Object</b>
	 * <p>
	 * List all object types and all fields configured on each object type
	 * for the specific object. Note that an array is returned with size one.
	 *
	 * @param objectName The object name
	 * @param objectType The object type name
	 * @return Describe of the single object, which will be the first element in the array
	 * @vapil.api <pre>
	 * GET /api/{version}/configuration/Objecttype.{object_name}.{object_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-details-from-a-specific-object' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-details-from-a-specific-object</a>
	 * @vapil.request <pre>
	 * ObjectRecordTypeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.retrieveDetailsFromASpecificObject(objectName, objectType);</pre>
	 * @vapil.response See {@link #retrieveDetailsFromAllObjectTypes()}
	 */
	public ObjectRecordTypeResponse retrieveDetailsFromASpecificObject(String objectName, String objectType) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_TYPE_RETRIEVE_OBJECT);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_type}", objectType);

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Single value is returned by the API so use custom object mapper for re-using the response (which is an array)
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, ObjectRecordTypeResponse.class);
	}

	/**
	 * <b>Object Types - Change Object Type</b>
	 * <p>
	 * Change the object type for the data supplied in localfile, binarydata, or raw json
	 *
	 * @param objectName The object name
	 * @return ObjectRecordBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/actions/changetype</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#change-object-type' target='_blank'>https://developer.veevavault.com/api/22.1/#change-object-type</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 					.setContentTypeCsv()
	 * 					.setBinaryFile("file",mapper.writeValueAsBytes(objectRecords))
	 * 					.changeObjectType(objectName);</pre>
	 * @vapil.response <pre>
	 * System.out.println(resp.getResponse());</pre>
	 */
	public ObjectRecordBulkResponse changeObjectType(String objectName) {
		return sendObjectRecordRequest(objectName,
				HttpMethod.POST,
				vaultClient.getAPIEndpoint(URL_OBJ_TYPE_CHANGE));
	}

	/**
	 * <b>Object Roles - Retrieve Object Record Roles</b>
	 * <p>
	 * Retrieve manually assigned roles on an object record and the users and groups assigned to them.
	 *
	 * @param objectName The object name
	 * @param id         The id of the object record
	 * @return ObjectRecordRoleResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{id}/roles</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-object-record-roles' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-object-record-roles</a>
	 * @vapil.request <pre>
	 * ObjectRecordRoleResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.retrieveObjectRecordRoles(objectName, id);</pre>
	 * @vapil.response <pre>
	 * for (ObjectRole role : resp.getData()) {
	 *   System.out.println("\nRole: " + role.getName() + ", Assignment Type = " + role.getAssignmentType());
	 *
	 *   for (Integer i : role.getGroups())
	 *   System.out.println("Group = " + i);
	 *
	 *   for (Integer i : role.getUsers())
	 *   System.out.println("User = " + i);
	 * }</pre>
	 */
	public ObjectRecordRoleResponse retrieveObjectRecordRoles(String objectName, String id) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ROLE);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{id}", id);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectRecordRoleResponse.class);
	}

	/**
	 * <b>Object Roles -  Retrieve Object Record Role (Filter to a specific role)</b>
	 * <p>
	 * Retrieve manually assigned role on an object record and the users and groups assigned to them.
	 *
	 * @param objectName The object name
	 * @param id         The id of the object record
	 * @param roleName   Role name to filter for a specific role. For example, owner__v.
	 * @return ObjectRecordRoleResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{id}/roles{/role_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-object-record-roles' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-object-record-roles</a>
	 * @vapil.request <pre>
	 * ObjectRecordRoleResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.retrieveObjectRecordRole(objectName, id, roleName);</pre>
	 * @vapil.response <pre>
	 * for (ObjectRole role : resp.getData()) {
	 *   System.out.println("\n" + role.getName() + ", Assignment Type = " + role.getAssignmentType());
	 *
	 *   for (Integer i : role.getGroups()) {
	 *     System.out.println("Group = " + i);
	 *   }
	 *
	 *   for (Integer i : role.getUsers()) {
	 *     System.out.println("User = " + i);
	 *   }
	 * }</pre>
	 */
	public ObjectRecordRoleResponse retrieveObjectRecordRole(String objectName, String id, String roleName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ROLE_SINGLE);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{id}", id);
		url = url.replace("{role_name}", roleName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectRecordRoleResponse.class);
	}

	/**
	 * <b>Object Roles - Assign Users and Groups to Roles on Object Records</b>
	 * <p>
	 * Assign users and groups to roles on an object record in bulk.
	 *
	 * @param objectName The name of the object where you want to update records.
	 * @return ObjectRecordRoleChangeResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/roles</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#assign-users-amp-groups-to-roles-on-object-records' target='_blank'>https://developer.veevavault.com/api/22.1/#assign-users-amp-groups-to-roles-on-object-records</a>
	 * @vapil.response <pre>
	 * <i>Example 1 - CSV</i>
	 * ObjectRecordRoleChangeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 			.setInputPath(inputPath)
	 * 			.setContentTypeCsv()
	 * 			.assignUsersAndGroupsToRolesOnObjectRecords(objectName);</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - JSON</i>
	 * ObjectRecordRoleChangeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 			.setRequestString(requestString)
	 * 			.setContentTypeJson()
	 * 			.assignUsersAndGroupsToRolesOnObjectRecords(objectName);</pre>
	 * @vapil.response <pre>
	 * for (ObjectRecordRoleChangeResponse.ObjectRoleChange roleChange : resp.getData())
	 *   System.out.println(roleChange.getResponseStatus() + " for " + roleChange.getData().getId());
	 * </pre>
	 */
	public ObjectRecordRoleChangeResponse assignUsersAndGroupsToRolesOnObjectRecords(String objectName) {
		return assignOrRemoveUsersAndGroupsOnObjectRecords(HttpMethod.POST, objectName);
	}

	/**
	 * <b>Object Roles - Remove Users and Groups from Roles on Object Records</b>
	 *
	 * @param objectName The name of the object where you want to remove roles.
	 * @return ObjectRecordRoleChangeResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/vobjects/{object_name}/roles</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#remove-users-amp-groups-from-roles-on-object-records' target='_blank'>https://developer.veevavault.com/api/22.1/#remove-users-amp-groups-from-roles-on-object-records</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - CSV</i>
	 * ObjectRecordRoleChangeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 			.setInputPath(inputPath)
	 * 			.setContentTypeCsv()
	 * 			.removeUsersAndGroupsFromRolesOnObjectRecords(objectName);</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - JSON</i>
	 * ObjectRecordRoleChangeResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 			.setRequestString(requestString)
	 * 			.setContentTypeJson()
	 * 			.removeUsersAndGroupsFromRolesOnObjectRecords(objectName);</pre>
	 * @vapil.response <pre>
	 * for (ObjectRecordRoleChangeResponse.ObjectRoleChange roleChange : resp.getData())
	 *   System.out.println(roleChange.getResponseStatus() + " for " + roleChange.getData().getId());</pre>
	 */
	public ObjectRecordRoleChangeResponse removeUsersAndGroupsFromRolesOnObjectRecords(String objectName) {
		return assignOrRemoveUsersAndGroupsOnObjectRecords(HttpMethod.DELETE, objectName);
	}

	/**
	 * Private/Shared method for adding/removing object record role endpoints
	 *
	 * @param httpMethod The http method for the request
	 * @param objectName The name of the object for the request
	 * @return ObjectRecordRoleChangeResponse
	 */
	private ObjectRecordRoleChangeResponse assignOrRemoveUsersAndGroupsOnObjectRecords(HttpMethod httpMethod, String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_ROLE_ADD_REMOVE);
		url = url.replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (bodyParams != null && !bodyParams.isEmpty())
			request.setBodyParams(bodyParams);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(headerContentType, requestString);

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV))
			if (outputPath != null) {
				return sendToFile(httpMethod, request, outputPath, ObjectRecordRoleChangeResponse.class);
			} else {
				return sendReturnBinary(httpMethod, request, ObjectRecordRoleChangeResponse.class);
			}
		else
			return send(httpMethod, request, ObjectRecordRoleChangeResponse.class);
	}

	/**
	 * <b>Deep Copy Object Record</b>
	 * <p>
	 * Deep Copy copies an object record, including all of the record's related child and
	 * grandchild records. Each deep (hierarchical) copy can copy a maximum of 10,000
	 * related records at a time.
	 * <p>
	 * Add raw JSON request string for overriding/ignoring field values in the request
	 * by calling the "setRequestString" in the builder.
	 *
	 * @param objectName The name of the parent object to copy
	 * @param recordId   The ID of the specific object record to copy
	 * @return JobCreateResponse, with the job id of the created response
	 * @vapil.api <pre>
	 * POST /api/{version}/vobjects/{object_name}/{object_record_ID}/actions/deepcopy</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#deep-copy-object-record' target='_blank'>https://developer.veevavault.com/api/22.1/#deep-copy-object-record</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.deepCopyObjectRecord(objectName, objectRecordId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * if (resp.isSuccessful()) {
	 *   Integer jobId = resp.getJobId();
	 *   System.out.println("Job id = " + jobId.toString());
	 *
	 *   boolean jobRunning = true;
	 *   while (jobRunning) {
	 *     try {
	 *       Thread.sleep(5000);
	 *
	 *       JobStatusResponse respJob = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);
	 *       String status = respJob.getData().getStatus();
	 *       System.out.println("Job status = " + status);
	 *       if (status.equalsIgnoreCase("ERRORS_ENCOUNTERED")) {
	 *         jobRunning = false;
	 *
	 *         VaultResponse respJobResults = vaultClient.newRequest(ObjectRecordRequest.class).getResultsOfDeepCopyJob(objectName, false, jobId);
	 *         System.out.println(new String(respJobResults.getBinaryContent()));
	 *         }
	 *       if (status.equalsIgnoreCase("SUCCESS")) {
	 *         jobRunning = false;
	 *
	 *         VaultResponse respJobResults = vaultClient.newRequest(ObjectRecordRequest.class).getResultsOfDeepCopyJob(objectName, true, jobId);
	 *         System.out.println(new String(respJobResults.getBinaryContent()));
	 *         }
	 *
	 *
	 *       } catch (InterruptedException e) {
	 *       e.printStackTrace();
	 *       }
	 *     }
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse deepCopyObjectRecord(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_DEEP_COPY);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_record_ID}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestString);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Get Results of Deep Copy Job</b>
	 * <p>
	 * Query vault to determine the results of the deep copy request.
	 *
	 * @param objectName The name of the deep copied object
	 * @param success    True for successes, false for failures
	 * @param jobId      The ID of the job, retrieved from the response of the job request.
	 * @return VaultResponse, with content retrieve via "getBinaryContent" method
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/deepcopy/results/{object_name}/{job_status}/{job_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#get-results-of-deep-copy-job' target='_blank'>https://developer.veevavault.com/api/22.1/#get-results-of-deep-copy-job</a>
	 * @vapil.request <pre>
	 * See {@link #deepCopyObjectRecord(String, String)}</pre>
	 */
	public VaultResponse getResultsOfDeepCopyJob(String objectName, boolean success, int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_DEEP_COPY_RESULTS);
		url = url.replace("{object_name}", objectName);

		if (success)
			url = url.replace("{job_status}", "success");
		else
			url = url.replace("{job_status}", "failure");

		url = url.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Retrieve Deleted Object Record ID</b>
	 * <p>
	 * Retrieve the IDs of object records that have been deleted from your vault within the past 30 days.
	 * The method automatically paginates to return all records in the response.
	 * <ul>
	 * <li>Optionally set start/end date ranges via the "setStartDateDeleted"
	 * and "setEndDateDeleted" methods. This also applies to the ""offset" and "limit" query parameters.</li>
	 * <li>Note that only JSON responses are supported at this time</li>
	 * <li>Calling code must write to CSV if CSV is needed</li>
	 * </ul>
	 *
	 * @param objectName Object name for the recycle bin
	 * @return The recycle bin for deleted records, which can be retrieved via the "getDeleteRecords" method in the response
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/deletions/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-deleted-object-record-id' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-deleted-object-record-id</a>
	 * @vapil.request <pre>
	 * ObjectRecordDeletedResponse resp = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 				.setStartDateDeleted(startDate)
	 * 				.setEndDateDeleted(endDate)
	 * 			    .set
	 * 				.retrieveDeletedObjectRecordId(objectName);</pre>
	 * @vapil.response <pre>
	 * if (resp.getData() != null) {
	 *   System.out.println("Total Deleted Records = " + resp.getData().size());
	 *   System.out.println("First record deleted " + resp.getData().get(0).getId() + ", " + resp.getData().get(0).getDateDeleted());
	 * }
	 * else
	 *   System.out.println("No deleted records");</pre>
	 */
	public ObjectRecordDeletedResponse retrieveDeletedObjectRecordId(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_RECORD_DELETED);
		url = url.replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Set JSON as header accept to ensure JSON response
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		// Determine if start/end date ranges set
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		if (startDateDeleted != null) {
			String startDate = startDateDeleted.format(formatter);
			startDate = startDate.replace(" ", "T") + "Z";
			log.debug("Deleted Record Start Date = " + startDate);
			request.addQueryParam("start_date", startDate);
		}

		if (endDateDeleted != null) {
			String endDate = endDateDeleted.format(formatter);
			endDate = endDate.replace(" ", "T") + "Z";
			log.debug("Deleted Record End Date = " + endDate);
			request.addQueryParam("end_date", endDate);
		}

		if (offset != null) {
			log.debug("Offset = " + offset);
			request.addQueryParam("offset", offset);
		}

		if (limit != null) {
			log.debug("Limit = " + limit);
			request.addQueryParam("limit", limit);
		}

		return send(HttpMethod.GET, request, ObjectRecordDeletedResponse.class);
	}

	/**
	 * <b>Retrieve Deleted Object Record ID (By Page)</b>
	 * <p>
	 * Retrieve the IDs of object records using the previous_page or next_page parameter of a previous request
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return The recycle bin for deleted records, which can be retrieved via the "getData" method in the response
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/deletions/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-deleted-object-record-id' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-deleted-object-record-id</a>
	 * @vapil.request <pre>
	 * ObjectRecordDeletedResponse paginatedResponse = vaultClient.newRequest(ObjectRecordRequest.class)
	 * 		.retrieveDeletedObjectRecordIdByPage(response.getResponseDetails().getNextPage());</pre>
	 * @vapil.response <pre>System.out.println(paginatedResponse.getResponseStatus())</pre>;
	 */
	public ObjectRecordDeletedResponse retrieveDeletedObjectRecordIdByPage(String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, ObjectRecordDeletedResponse.class);
	}

	/**
	 * <b>Retrieve Limits on Objects</b>
	 *
	 * @return VaultResponse - get raw string (no response model)
	 * @vapil.api <pre>
	 * GET /api/{version}/limits</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#retrieve-limits-on-objects' target='_blank'>https://developer.veevavault.com/api/22.1/#retrieve-limits-on-objects</a>
	 */
	public VaultResponse retrieveLimitsOnObjects() {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_LIMITS);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, VaultResponse.class);
	}

	/**
	 * <b>Update Corporate Currency Fields</b>
	 * <p>
	 * Add raw JSON request string for overriding/ignoring field values in the request
	 * by calling the "setRequestString" in the builder.
	 *
	 * @param objectName The object name__v field value, for example, product__v
	 * @return JobCreateResponse, with the job id of the created response
	 * @vapil.api <pre>
	 * PUT /api/{version}/vobjects/{object_name}/actions/updatecorporatecurrency</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.1/#update-corporate-currency-fields' target='_blank'>https://developer.veevavault.com/api/22.1/#update-corporate-currency-fields</a>
	 */
	public JobCreateResponse updateCorporateCurrencyFields(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJ_CURRENCY);
		url = url.replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestString);

		return send(HttpMethod.PUT, request, JobCreateResponse.class);
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
			if (bodyParams == null || bodyParams.isEmpty()) {
				if (requestString == null || requestString.isEmpty()) {
					if (binaryFile == null || binaryFile.getBinaryContent() == null) {
						log.error("Invalid request - no source data");
						return false;
					}
				}
			}
		}
		return true;
	}

    /*
		Enums
	*/

	public enum UnchangedFieldBehaviorType {
		ALWAYSIGNORE("AlwaysIgnore"),
		IGNORESETONCREATEONLY("IgnoreSetOnCreateOnly"),
		NEVERIGNORE("NeverIgnore");

		private String value;

		UnchangedFieldBehaviorType(String value) {
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
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public ObjectRecordRequest setAcceptCSV() {
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
	public ObjectRecordRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Single record form ObjectRecordRequest with name/value body parameters
	 *
	 * @param bodyParams map of body params
	 * @return The Request
	 */
	public ObjectRecordRequest setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM;
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public ObjectRecordRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public ObjectRecordRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Specify the start date when calling the deleted records endpoint
	 * <p>
	 * Specify a date (no more than 30 days past) after which Vault will look for
	 * deleted object records. Dates and times are in UTC. If the time is not
	 * specified, it will default to midnight (T00:00:00Z) on the specified date.
	 * Date formats will automatically be formatted.
	 *
	 * @param endDateDeleted End date for the date range
	 * @return The Request
	 */
	public ObjectRecordRequest setEndDateDeleted(ZonedDateTime endDateDeleted) {
		this.endDateDeleted = endDateDeleted;
		return this;
	}

	/**
	 * Specify an UPSERT operation via the idParam
	 *
	 * @param idParam External Id field API name for the UPSERT
	 * @return The Request
	 */
	public ObjectRecordRequest setIdParam(String idParam) {
		this.idParam = idParam;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public ObjectRecordRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Enable migration mode to set the object record state.
	 * If not specified, records will be created in their initial state.
	 * API user must have the Record Migration permission to use this header.
	 *
	 * @param migrationMode The source request as a string
	 * @return The Request
	 */
	public ObjectRecordRequest setMigrationMode(boolean migrationMode) {
		this.migrationMode = migrationMode;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public ObjectRecordRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public ObjectRecordRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}


	/**
	 * Specify the start date when calling the deleted records endpoint
	 * <p>
	 * Specify a date (no more than 30 days past) after which Vault will look for
	 * deleted object records. Dates and times are in UTC. If the time is not
	 * specified, it will default to midnight (T00:00:00Z) on the specified date.
	 * Date formats will automatically be formatted.
	 *
	 * @param startDateDeleted Start date for the date range
	 * @return The Request
	 */
	public ObjectRecordRequest setStartDateDeleted(ZonedDateTime startDateDeleted) {
		this.startDateDeleted = startDateDeleted;
		return this;
	}

	/**
	 * When upserting, set to IgnoreSetOnCreateOnly (default starting with API v21.1)
	 * to ignore values given for the Object Type field and parent object fields
	 * if the record already exists and the value has not changed.
	 * Set to NeverIgnore to enforce edit permission for all fields in the payload,
	 * regardless of whether or not the value has changed.
	 * Read-only fields such as Object Type will fail even if the value has not changed.
	 * Set to AlwaysIgnore to ignore fields that match between the record and payload values during upsert.
	 *
	 * @param unchangedFieldBehavior The source request as a string
	 * @return The Request
	 */
	public ObjectRecordRequest setUnchangedFieldBehavior(UnchangedFieldBehaviorType unchangedFieldBehavior) {
		this.unchangedFieldBehavior = unchangedFieldBehavior;
		return this;
	}

	/**
	 * Paginate the results displayed per page by specifying
	 * the amount of offset from the first job history returned.
	 *  If omitted, defaults to 0.
	 *
	 * @param offset page offset
	 * @return The Request
	 */
	public ObjectRecordRequest setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Paginate the results by specifying the maximum number of histories per page in the response.
	 * This can be any value between 1 and 200. If omitted, defaults to 50.
	 * <p>
	 * @param limit the size of the result set in the page
	 * @return The request
	 */

	public ObjectRecordRequest setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}
}
