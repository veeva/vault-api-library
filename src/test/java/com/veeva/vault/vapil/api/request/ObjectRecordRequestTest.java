/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.ObjectRecordRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag("ObjectRecordRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Object record request should")
public class ObjectRecordRequestTest {

	static final String OBJECT_NAME = "vapil_test_object__c";
	static final String CREATE_OBJECTS_CSV_PATH = ObjectRecordRequestHelper.getPathCreateObjectRecordsCsv();
	static final String UPDATE_OBJECTS_CSV_PATH = ObjectRecordRequestHelper.getPathUpdateObjectRecordsCsv();
	static final String DELETE_OBJECTS_CSV_PATH = ObjectRecordRequestHelper.getPathDeleteObjectRecordsCsv();
	static List<String> recordIds = new ArrayList<>();


	// Create: Source - CSV file, Response - JSON
	@Test
	@Order(1)
	@DisplayName("successfully create object records and return json response")
	public void testCreateCSV_JSON(VaultClient vaultClient) {

		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"name__v"});
		for (int i = 0; i < 3; i++) {
			String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
			data.add(new String[]{name});
		}

		FileHelper.writeCsvFile(CREATE_OBJECTS_CSV_PATH, data);

//		Create Objects
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(CREATE_OBJECTS_CSV_PATH)
				.createObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
		for (ObjectRecordResponse objectRecordResponse : response.getData()) {
			Assertions.assertNotNull(objectRecordResponse.getData().getId());
			recordIds.add(objectRecordResponse.getData().getId());
		}
	}


	// Create: Source - CSV file, Response - CSV
	@Disabled
	@Test
	@Order(2)
	@DisplayName("successfully create object records and return CSV response")
	public void testCreateCSV_CSV(VaultClient vaultClient) {

		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"name__v"});
		for (int i = 0; i < 3; i++) {
			String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
			data.add(new String[]{name});
		}

		FileHelper.writeCsvFile(CREATE_OBJECTS_CSV_PATH, data);

//		Create object records
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setAcceptCSV()
				.setInputPath(CREATE_OBJECTS_CSV_PATH)
				.createObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
		for (ObjectRecordResponse objectRecordResponse : response.getData()) {
			Assertions.assertNotNull(objectRecordResponse.getData().getId());
			recordIds.add(objectRecordResponse.getData().getId());
		}
	}

	// Retrieve Object Record
	@Test
	@Order(3)
	@DisplayName("successfully retrieve object record by Id")
	public void testRetrieveObjectRecord(VaultClient vaultClient) {
//		Retrieve created Record
		String recordId = recordIds.get(0);
		ObjectRecordResponse retrieveResponse = vaultClient
				.newRequest(ObjectRecordRequest.class)
				.retrieveObjectRecord(OBJECT_NAME, recordId);

		Assertions.assertTrue(retrieveResponse.isSuccessful());
		Assertions.assertNotNull(retrieveResponse.getData().getId());

	}

	// Update: Source - CSV, Response - JSON
	@Test
	@Order(4)
	@DisplayName("successfully update object records from CSV")
	public void testUpdateCSV(VaultClient vaultClient) {

		List<String[]> updateData = new ArrayList<>();
		updateData.add(new String[]{"id", "name__v"});

		for (int i = 0; i < recordIds.size(); i++) {
			String updateName = "VAPIL Test Updated Object " + ZonedDateTime.now() + " " + i;
			String id = recordIds.get(i);
			updateData.add(new String[]{id, updateName});
		}

		FileHelper.writeCsvFile(UPDATE_OBJECTS_CSV_PATH, updateData);

//		Update Object
		ObjectRecordBulkResponse updateResponse = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(UPDATE_OBJECTS_CSV_PATH)
				.updateObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(updateResponse.isSuccessful());
		for (ObjectRecordResponse response : updateResponse.getData()) {
			Assertions.assertTrue(response.isSuccessful());
		}
	}

	@Test
	@Order(5)
	@DisplayName("successfully retrieve object record collection")
	public void testRetrieveObjectRecordCollection(VaultClient vaultClient) {

		ObjectRecordCollectionResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.retrieveObjectRecordCollection(OBJECT_NAME);

		if (response.isPaginated()) {
			ObjectRecordCollectionResponse paginatedResponse = vaultClient.newRequest(ObjectRecordRequest.class)
					.retrieveObjectRecordCollectionByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	// Delete: Source -  CSV, Response - JSON
	@Test
	@Order(6)
	@DisplayName("successfully delete object records from CSV")
	public void testDeleteObjectRecordsCsv(VaultClient vaultClient) {
		List<String[]> deleteData = new ArrayList<>();
		deleteData.add(new String[]{"id"});
		for (int i = 0; i < recordIds.size(); i++) {
			deleteData.add(new String[]{recordIds.get(i)});
		}

		FileHelper.writeCsvFile(DELETE_OBJECTS_CSV_PATH, deleteData);

//		Delete Object
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(DELETE_OBJECTS_CSV_PATH)
				.deleteObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
		for (ObjectRecordResponse recordResponse : response.getData()) {
			Assertions.assertTrue(recordResponse.isSuccessful());
		}
	}

	@Disabled
    @Test
	public void testInvalidRequest(VaultClient vaultClient) {
		
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()				
				.createObjectRecords("person__sys");

		Assertions.assertNull(response);
	}
	
	// Create: Source - CSV file, Response - JSON, Upsert operation with idParam
	@Disabled
    @Test
	public void testUpsertCSV(VaultClient vaultClient) {
		String objectName = "person__sys";
		String inputPath = "";
		String idParam = "external_id__c";
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setIdParam(idParam)
				.setInputPath(inputPath)
				.setUnchangedFieldBehavior(ObjectRecordRequest.UnchangedFieldBehaviorType.IGNORESETONCREATEONLY)
				.createObjectRecords(objectName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}	
	
	// Create: Source - CSV file, Response - JSON, Migration Mode
	@Disabled
    @Test
	public void testCreateMigrationMode(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String inputPath = "";
				
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setMigrationMode(true)
				.setInputPath(inputPath)
				.createObjectRecords(objectName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}	
	
	// Create: Source - JSON string, Response - JSON
	@Disabled
    @Test
	public void testCreateJSONString(VaultClient vaultClient) {
		String objectName = "person__sys";
		String requestString  = "[{\"first_name__sys\":\"Leah2\",\"last_name__sys\":\"Allison\",\"status__v\":\"active__v\",\"email__sys\":\"test@veeva.com\","
				+ "\"external_id__c\":1003,\"language__sys\":\"0LU000000000101\",\"locale__sys\":\"0LO000000000104\",\"locale__sys.name__v\":\"United States\","
				+ "\"timezone__sys\":\"america_indianapolis__sys\"},{\"first_name__sys\":\"Angelo2\",\"last_name__sys\":\"Marquez\",\"status__v\":\"active__v\","
				+ "\"email__sys\":\"test@veeva.com\",\"external_id__c\":1004,\"language__sys\":\"0LU000000000101\",\"locale__sys\":\"0LO000000000104\","
				+ "\"locale__sys.name__v\":\"United States\",\"timezone__sys\":\"america_indianapolis__sys\"}]";

				
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeJson()
				.setRequestString(requestString)				
				.createObjectRecords(objectName);
			
		Assertions.assertTrue(response.isSuccessful());
	    Assertions.assertNotNull(response.getResponse());
	}
		
	// Create: Single Record: Source - Body Params, Response - JSON
	@Disabled
    @Test
	public void testCreateBodyParams(VaultClient vaultClient) {

		String objectName = "person__sys";

		Map<String,Object> bodyParams = new HashMap<>();
		bodyParams.put("first_name__sys", "Waylon");
		bodyParams.put("last_name__sys", "Jennings");
		bodyParams.put("email__sys", "test@veeva.com");
		bodyParams.put("language__sys", "0LU000000000101");
		bodyParams.put("locale__sys", "0LO000000000104");
		bodyParams.put("timezone__sys", "america_indianapolis__sys");
				
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setBodyParams(bodyParams)
				.createObjectRecords(objectName);
			
		Assertions.assertTrue(response.isSuccessful());
	    Assertions.assertNotNull(response.getResponse());
	}
		
	// Cascade Delete
	@Disabled
    @Test
	public void testCascadeDelete(VaultClient vaultClient) {
		String objectName = "person__sys";
		String id = "V0E000000000382";
		
		JobCreateResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.cascadeDeleteObjectRecord(objectName, id);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}
	
	// Cascade Delete Job Status
	@Disabled
    @Test
	public void testResultsOfCascadeDeleteJobStatus(VaultClient vaultClient) {
		String objectName = "person__sys";
		Integer jobId = 123;

		VaultResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.getResultsOfCascadeDeleteJob(objectName, "success", jobId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	
	@Disabled
    @Test
	public void testRetrieveDeletedObjectRecordId(VaultClient vaultClient) {
		String objectName = "person__sys";

		ObjectRecordDeletedResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.retrieveDeletedObjectRecordId(objectName);

		if (response.isPaginated()) {
			ObjectRecordDeletedResponse paginatedResponse = vaultClient.newRequest(ObjectRecordRequest.class)
					.retrieveDeletedObjectRecordIdByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
			Assertions.assertNotNull(paginatedResponse.getResponseDetails().getSize());
		}
	}
	
	@Disabled
    @Test
	public void testDeepCopyObjectRecord(VaultClient vaultClient) {

		String objectName = "tt_claim__c";
		String objectRecordId = "OOW000000000803";
		
		JobCreateResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.deepCopyObjectRecord(objectName, objectRecordId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}
}

