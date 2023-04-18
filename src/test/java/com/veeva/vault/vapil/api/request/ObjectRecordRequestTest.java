/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.opencsv.CSVWriter;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag("ObjectRecord")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Object record request")
public class ObjectRecordRequestTest {

	static final String OBJECT_NAME = "vapil_test_object__c";

	static final String RESOURCES_FOLDER_PATH = "src\\test\\resources";

	static final List<String> recordIds = new ArrayList<>();

	@BeforeAll
	static void createObjects(VaultClient vaultClient) {
//		Write to CSV file
		String csvPath = RESOURCES_FOLDER_PATH + "\\test_create_objects.csv";
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"name__v", "status__v"});
		for (int i = 1; i < 4; i++) {
			String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
			data.add(new String[]{name, "active__v"});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
			writer.writeAll(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Create Objects
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(csvPath)
				.createObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
		for (ObjectRecordResponse objectRecordResponse : response.getData()) {
			recordIds.add(objectRecordResponse.getData().getId());
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
	
	// Retrieve Object Record
	@Test
	@Order(0)
	@DisplayName("Should successfully retrieve object record by Id")
	public void testRetrieveObjectRecord(VaultClient vaultClient) {
//		Retrieve created Record
		String recordId = recordIds.get(0);
		ObjectRecordResponse retrieveResponse = vaultClient
				.newRequest(ObjectRecordRequest.class)
				.retrieveObjectRecord(OBJECT_NAME, recordId);

		Assertions.assertTrue(retrieveResponse.isSuccessful());
		Assertions.assertNotNull(retrieveResponse.getData().getId());

	}
	
	// Create: Source - CSV file, Response - JSON
    @Test
	@DisplayName("Should successfully create object records and return json response")
	public void testCreateCSV_JSON(VaultClient vaultClient) {
//		Write to CSV file
		String csvPath = RESOURCES_FOLDER_PATH + "\\test_create_objects.csv";
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"name__v", "status__v"});
		for (int i = 1; i < 4; i++) {
			String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
			data.add(new String[]{name, "active__v"});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
			writer.writeAll(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Create Objects
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(csvPath)
				.createObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
		for (ObjectRecordResponse objectRecordResponse : response.getData()) {
			Assertions.assertNotNull(objectRecordResponse.getData().getId());
		}
	}	
	
	// Create: Source - CSV file, Response - CSV
    @Test
	@DisplayName("Should successfully create object records and return CSV response")
	public void testCreateCSV_CSV(VaultClient vaultClient) {
//		Write to CSV file
		String csvPath = RESOURCES_FOLDER_PATH + "\\test_create_objects.csv";
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"name__v", "status__v"});
		for (int i = 1; i < 4; i++) {
			String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
			data.add(new String[]{name, "active__v"});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
			writer.writeAll(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Create object records
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setAcceptCSV()
				.setInputPath(csvPath)
				.createObjectRecords(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());
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


	// Update: Source - CSV, Response - JSON
    @Test
	@Order(1)
	@DisplayName("Should successfully update object records from CSV")
	public void testUpdateCSV(VaultClient vaultClient) {
//		Write IDs to CSV file
		String updateCsvPath = RESOURCES_FOLDER_PATH + "\\test_update_objects.csv";
		List<String[]> updateData = new ArrayList<>();
		updateData.add(new String[]{"id", "name__v"});

		for (int i = 0; i < recordIds.size(); i++) {
			String updateName = "VAPIL Test Updated Object " + ZonedDateTime.now() + " " + i;
			String id = recordIds.get(i);
			updateData.add(new String[]{id, updateName});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(updateCsvPath))) {
			writer.writeAll(updateData);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Update Object
		ObjectRecordBulkResponse updateResponse = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()				
				.setInputPath(updateCsvPath)
				.updateObjectRecords(OBJECT_NAME);
			
		Assertions.assertTrue(updateResponse.isSuccessful());
	    for (ObjectRecordResponse response : updateResponse.getData()) {
			Assertions.assertTrue(response.isSuccessful());
		}
	}	
	
	// Delete: Source -  CSV, Response - JSON
    @Test
	@Order(3)
	@DisplayName("Should successfully delete object records from CSV")
	public void testDeleteObjectRecordsCsv(VaultClient vaultClient) {
//		Write IDs to CSV file
		String deleteCsvPath = RESOURCES_FOLDER_PATH + "\\test_delete_objects.csv";
		List<String[]> deleteData = new ArrayList<>();
		deleteData.add(new String[]{"id"});
		for (int i = 0; i < recordIds.size(); i++) {
			deleteData.add(new String[]{recordIds.get(i)});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(deleteCsvPath))) {
			writer.writeAll(deleteData);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Delete Object
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(deleteCsvPath)
				.deleteObjectRecords(OBJECT_NAME);
			
		Assertions.assertTrue(response.isSuccessful());
	    for (ObjectRecordResponse recordResponse : response.getData()) {
			Assertions.assertTrue(recordResponse.isSuccessful());
		}
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

    @Test
	@Order(2)
	@DisplayName("Should successfully retrieve object record collection")
	public void testRetrieveObjectRecordCollection(VaultClient vaultClient) {

		ObjectRecordCollectionResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.retrieveObjectRecordCollection(OBJECT_NAME);

		if (response.isPaginated()) {
			ObjectRecordCollectionResponse paginatedResponse = vaultClient.newRequest(ObjectRecordRequest.class)
					.retrieveObjectRecordCollectionByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}
}

