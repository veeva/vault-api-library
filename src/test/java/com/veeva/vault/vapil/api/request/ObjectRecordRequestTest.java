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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.HashMap;
import java.util.Map;


@Tag("ObjectRecord")
@ExtendWith(VaultClientParameterResolver.class)
public class ObjectRecordRequestTest {

	
	@Test
	public void testInvalidRequest(VaultClient vaultClient) {
		
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()				
				.createObjectRecords("person__sys");

		Assertions.assertNull(response);
	}	
	
	// Retrieve Object Record
	@Test
	public void testRetrieveObjectRecord(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String recordId = "OOW000000000102";
		ObjectRecordResponse response = vaultClient.newRequest(ObjectRecordRequest.class).retrieveObjectRecord(objectName, recordId);

		Assertions.assertTrue(response.isSuccessful());

		ObjectRecord objectRecord = response.getData();
		Assertions.assertNotNull(objectRecord);

		Map<String,Object> additionalFields = objectRecord.getVaultModelData();
		Assertions.assertNotNull(additionalFields);
		Assertions.assertNotNull(additionalFields.keySet());
	}
	
	// Create: Source - CSV file, Response - JSON	
	@Test
	public void testCreateCSV_JSON(VaultClient vaultClient) {
		String objectName = "person__sys";
		String inputPath = "";
		
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setInputPath(inputPath)
				.createObjectRecords(objectName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}	
	
	// Create: Source - CSV file, Response - CSV
	@Test
	public void testCreateCSV_CSV(VaultClient vaultClient) {
		String objectName = "person__sys";
		String inputPath = "";

		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()
				.setAcceptCSV()
				.setInputPath(inputPath)
				.createObjectRecords(objectName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}
	
	// Create: Source - CSV file, Response - JSON, Upsert operation with idParam
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
	public void testUpdateCSV(VaultClient vaultClient) {
		String objectName = "person__sys";
		String inputPath = "";
				
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()				
				.setInputPath(inputPath)
				.updateObjectRecords(objectName);
			
		Assertions.assertTrue(response.isSuccessful());
	    Assertions.assertNotNull(response.getResponse());
	}	
	
	// Delete: Source -  CSV, Response - JSON
	@Test
	public void testDeleteCSV(VaultClient vaultClient) {
		String objectName = "person__sys";
		String inputPath = "";
	
		ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setContentTypeCsv()				
				.setInputPath(inputPath)
				.deleteObjectRecords(objectName);
			
		Assertions.assertTrue(response.isSuccessful());
	    Assertions.assertNotNull(response.getResponse());
	}	
		
	// Cascade Delete
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
	@Test
	public void testResultsOfCascadeDeleteJobStatus(VaultClient vaultClient) {
		String objectName = "person__sys";
		Integer jobId = 123;

		VaultResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.getResultsOfCascadeDeleteJob(objectName, "success", jobId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	
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
	public void testRetrieveObjectCollection(VaultClient vaultClient) {

		ObjectRecordCollectionResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.retrieveObjectRecordCollection("vapil_test_import_validate__c");

		if (response.isPaginated()) {
			ObjectRecordCollectionResponse paginatedResponse = vaultClient.newRequest(ObjectRecordRequest.class)
					.retrieveObjectRecordCollectionByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}
}

