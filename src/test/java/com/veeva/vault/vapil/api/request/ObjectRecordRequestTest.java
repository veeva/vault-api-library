/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.ObjectRecordRequestHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@Tag("ObjectRecordRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Object record request should")
public class ObjectRecordRequestTest {

    static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;
    static final String OBJECT_NAME_PARENT = ObjectRecordRequestHelper.OBJECT_NAME_PARENT;
    static final String PATH_CREATE_OBJECT_RECORDS_CSV = ObjectRecordRequestHelper.PATH_CREATE_OBJECT_RECORDS_CSV;
    static final String PATH_UPDATE_OBJECT_RECORDS_CSV = ObjectRecordRequestHelper.PATH_UPDATE_OBJECT_RECORDS_CSV;
    static final String PATH_DELETE_OBJECT_RECORDS_CSV = ObjectRecordRequestHelper.PATH_DELETE_OBJECT_RECORDS_CSV;
    static final String PATH_MERGE_OBJECT_RECORDS_CSV = ObjectRecordRequestHelper.PATH_MERGE_OBJECT_RECORDS_CSV;

    static List<String> recordIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    // Create: Source - CSV file, Response - JSON
    @Test
    @Order(1)
    @DisplayName("successfully create object records and return json response")
    public void testCreateCSV_JSON() {

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"name__v"});
        for (int i = 0; i < 3; i++) {
            String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
            data.add(new String[]{name});
        }

        FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);

//		Create Objects
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                .createAndUpsertObjectRecords(OBJECT_NAME);

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
    public void testCreateCSV_CSV() {

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"name__v"});
        for (int i = 0; i < 3; i++) {
            String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
            data.add(new String[]{name});
        }

        FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);

//		Create object records
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setAcceptCSV()
                .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                .createAndUpsertObjectRecords(OBJECT_NAME);

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
    public void testRetrieveObjectRecord() {
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
    public void testUpdateCSV() {

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "name__v"});

        for (int i = 0; i < recordIds.size(); i++) {
            String updateName = "VAPIL Test Updated Object " + ZonedDateTime.now() + " " + i;
            String id = recordIds.get(i);
            updateData.add(new String[]{id, updateName});
        }

        FileHelper.writeCsvFile(PATH_UPDATE_OBJECT_RECORDS_CSV, updateData);

//		Update Object
        ObjectRecordBulkResponse updateResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_UPDATE_OBJECT_RECORDS_CSV)
                .updateObjectRecords(OBJECT_NAME);

        Assertions.assertTrue(updateResponse.isSuccessful());
        for (ObjectRecordResponse response : updateResponse.getData()) {
            Assertions.assertTrue(response.isSuccessful());
        }
    }

    // Delete: Source -  CSV, Response - JSON
    @Test
    @Order(6)
    @DisplayName("successfully delete object records from CSV")
    public void testDeleteObjectRecordsCsv() {
        List<String[]> deleteData = new ArrayList<>();
        deleteData.add(new String[]{"id"});
        for (int i = 0; i < recordIds.size(); i++) {
            deleteData.add(new String[]{recordIds.get(i)});
        }

        FileHelper.writeCsvFile(PATH_DELETE_OBJECT_RECORDS_CSV, deleteData);

//		Delete Object
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_DELETE_OBJECT_RECORDS_CSV)
                .deleteObjectRecords(OBJECT_NAME);

        Assertions.assertTrue(response.isSuccessful());
        for (ObjectRecordResponse recordResponse : response.getData()) {
            Assertions.assertTrue(recordResponse.isSuccessful());
        }
    }

    @Disabled
    @Test
    public void testInvalidRequest() {

        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .createAndUpsertObjectRecords("person__sys");

        Assertions.assertNull(response);
    }

    // Create: Source - CSV file, Response - JSON, Upsert operation with idParam
    @Disabled
    @Test
    public void testUpsertCSV() {
        String objectName = "person__sys";
        String inputPath = "";
        String idParam = "external_id__c";
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setIdParam(idParam)
                .setInputPath(inputPath)
                .setUnchangedFieldBehavior(ObjectRecordRequest.UnchangedFieldBehaviorType.IGNORESETONCREATEONLY)
                .createAndUpsertObjectRecords(objectName);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponse());
    }

    // Create: Source - CSV file, Response - JSON, Migration Mode
    @Disabled
    @Test
    public void testCreateMigrationMode() {
        String objectName = "tt_claim__c";
        String inputPath = "";

        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setMigrationMode(true)
                .setInputPath(inputPath)
                .createAndUpsertObjectRecords(objectName);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponse());
    }

    // Create: Source - JSON string, Response - JSON
    @Disabled
    @Test
    public void testCreateJSONString() {
        String objectName = "person__sys";
        String requestString = "[{\"first_name__sys\":\"Leah2\",\"last_name__sys\":\"Allison\",\"status__v\":\"active__v\",\"email__sys\":\"test@veeva.com\","
                + "\"external_id__c\":1003,\"language__sys\":\"0LU000000000101\",\"locale__sys\":\"0LO000000000104\",\"locale__sys.name__v\":\"United States\","
                + "\"timezone__sys\":\"america_indianapolis__sys\"},{\"first_name__sys\":\"Angelo2\",\"last_name__sys\":\"Marquez\",\"status__v\":\"active__v\","
                + "\"email__sys\":\"test@veeva.com\",\"external_id__c\":1004,\"language__sys\":\"0LU000000000101\",\"locale__sys\":\"0LO000000000104\","
                + "\"locale__sys.name__v\":\"United States\",\"timezone__sys\":\"america_indianapolis__sys\"}]";


        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeJson()
                .setRequestString(requestString)
                .createAndUpsertObjectRecords(objectName);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponse());
    }

    // Create: Single Record: Source - Body Params, Response - JSON
    @Disabled
    @Test
    public void testCreateBodyParams() {

        String objectName = "person__sys";

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("first_name__sys", "Waylon");
        bodyParams.put("last_name__sys", "Jennings");
        bodyParams.put("email__sys", "test@veeva.com");
        bodyParams.put("language__sys", "0LU000000000101");
        bodyParams.put("locale__sys", "0LO000000000104");
        bodyParams.put("timezone__sys", "america_indianapolis__sys");

        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setBodyParams(bodyParams)
                .createAndUpsertObjectRecords(objectName);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponse());
    }

    // Cascade Delete
    @Disabled
    @Test
    public void testCascadeDelete() {
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
    public void testResultsOfCascadeDeleteJobStatus() {
        String objectName = "person__sys";
        Integer jobId = 123;

        VaultResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .getResultsOfCascadeDeleteJob(objectName, "success", jobId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Disabled
    @Test
    public void testRetrieveDeletedObjectRecordId() {
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
    public void testDeepCopyObjectRecord() {

        String objectName = "tt_claim__c";
        String objectRecordId = "OOW000000000803";

        JobCreateResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .deepCopyObjectRecord(objectName, objectRecordId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getJobId());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate a record merge job with content-type csv")
    class TestInitiateRecordMergeCsv {

        ObjectRecordMergeJobResponse initiateRecordMergeResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create 2 records to merge
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"duplicate_record_id", "main_record_id"});
            data.add(new String[]{String.valueOf(recordIds.get(0)), String.valueOf(recordIds.get(1))});
            FileHelper.writeCsvFile(PATH_MERGE_OBJECT_RECORDS_CSV, data);
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            initiateRecordMergeResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_MERGE_OBJECT_RECORDS_CSV)
                    .initiateRecordMerge(OBJECT_NAME);

            assertNotNull(initiateRecordMergeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(initiateRecordMergeResponse.isSuccessful());
            assertNotNull(initiateRecordMergeResponse.getData());
            assertNotNull(initiateRecordMergeResponse.getData().getJobId());

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, initiateRecordMergeResponse.getData().getJobId()));
        }
    }

    @Disabled
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate a record merge job with content-type json")
    class TestInitiateRecordMergeJson {

        ObjectRecordMergeJobResponse initiateRecordMergeResponse = null;
        List<String> recordIds = new ArrayList<>();
        String requestString = "";

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create 2 records to merge
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
            jsonObject.put("duplicate_record_id", recordIds.get(0));
            jsonObject.put("main_record_id", recordIds.get(1));
            arrayNode.add(jsonObject);
            requestString = arrayNode.toString();
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            initiateRecordMergeResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeJson()
                    .setRequestString(requestString)
                    .initiateRecordMerge(OBJECT_NAME);

            assertNotNull(initiateRecordMergeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(initiateRecordMergeResponse.isSuccessful());
            assertNotNull(initiateRecordMergeResponse.getData());
            assertNotNull(initiateRecordMergeResponse.getData().getJobId());

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, initiateRecordMergeResponse.getData().getJobId()));
        }
    }

    @Disabled
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate a record merge job with csv bytes")
    class TestInitiateRecordMergeBytes {

        ObjectRecordMergeJobResponse initiateRecordMergeResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create 2 records to merge
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"duplicate_record_id", "main_record_id"});
            data.add(new String[]{String.valueOf(recordIds.get(0)), String.valueOf(recordIds.get(1))});
            FileHelper.writeCsvFile(PATH_MERGE_OBJECT_RECORDS_CSV, data);
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File csvFile = new File(PATH_MERGE_OBJECT_RECORDS_CSV);
            byte[] csvBytes = Files.readAllBytes(csvFile.toPath());

            initiateRecordMergeResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setBinaryFile(csvFile.getName(), csvBytes)
                    .initiateRecordMerge(OBJECT_NAME);

            assertNotNull(initiateRecordMergeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(initiateRecordMergeResponse.isSuccessful());
            assertNotNull(initiateRecordMergeResponse.getData());
            assertNotNull(initiateRecordMergeResponse.getData().getJobId());

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, initiateRecordMergeResponse.getData().getJobId()));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve status of record merge job")
    class TestRetrieveRecordMergeStatus {

        JobStatusResponse retrieveRecordMergeStatusResponse = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create 2 records to merge
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"duplicate_record_id", "main_record_id"});
            data.add(new String[]{String.valueOf(recordIds.get(0)), String.valueOf(recordIds.get(1))});
            FileHelper.writeCsvFile(PATH_MERGE_OBJECT_RECORDS_CSV, data);

//            Initiate record merge
            ObjectRecordMergeJobResponse mergeResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_MERGE_OBJECT_RECORDS_CSV)
                    .initiateRecordMerge(OBJECT_NAME);
            assertTrue(mergeResponse.isSuccessful());
            jobId = mergeResponse.getData().getJobId();
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            retrieveRecordMergeStatusResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .retrieveRecordMergeStatus(jobId);

            assertNotNull(retrieveRecordMergeStatusResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveRecordMergeStatusResponse.isSuccessful());
            assertNotNull(retrieveRecordMergeStatusResponse.getData());
            assertNotNull(retrieveRecordMergeStatusResponse.getData().getStatus());

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve results of record merge job")
    class TestRetrieveRecordMergeResults {

        ObjectRecordMergeResultsResponse retrieveRecordMergeResultsResponse = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create 2 records to merge
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"duplicate_record_id", "main_record_id"});
            data.add(new String[]{String.valueOf(recordIds.get(0)), String.valueOf(recordIds.get(1))});
            FileHelper.writeCsvFile(PATH_MERGE_OBJECT_RECORDS_CSV, data);

//            Initiate record merge
            ObjectRecordMergeJobResponse mergeResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_MERGE_OBJECT_RECORDS_CSV)
                    .initiateRecordMerge(OBJECT_NAME);
            assertTrue(mergeResponse.isSuccessful());
            jobId = mergeResponse.getData().getJobId();

//            Wait for job completion
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            retrieveRecordMergeResultsResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .retrieveRecordMergeResults(jobId);

            assertNotNull(retrieveRecordMergeResultsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveRecordMergeResultsResponse.isSuccessful());
            assertNotNull(retrieveRecordMergeResultsResponse.getData());
            assertNotNull(retrieveRecordMergeResultsResponse.getData().getMergeSets());
            List<ObjectRecordMergeResultsResponse.Data.MergeSet> mergeSets = retrieveRecordMergeResultsResponse.getData().getMergeSets();

            for (ObjectRecordMergeResultsResponse.Data.MergeSet mergeSet : mergeSets) {
                assertNotNull(mergeSet.getDuplicateRecordId());
                assertNotNull(mergeSet.getMainRecordId());
                assertNotNull(mergeSet.getStatus());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create object records from a csv file")
    class TestCreateObjectRecordsCsv {

        ObjectRecordBulkResponse createObjectRecordsResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"name__v", "description__c"});
            for (int i = 0; i < 500; i++) {
                String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
                String description = "VAPIL Test";
                data.add(new String[]{name, description});
            }

            FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            createObjectRecordsResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                    .setMigrationMode(true)
                    .setNoTriggers(true)
                    .createAndUpsertObjectRecords(OBJECT_NAME);

            assertNotNull(createObjectRecordsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createObjectRecordsResponse.isSuccessful());
            assertNotNull(createObjectRecordsResponse.getData());
            for (ObjectRecordResponse objectRecordResponse : createObjectRecordsResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                assertNotNull(objectRecordResponse.getData().getId());
                assertNotNull(objectRecordResponse.getData().getUrl());
                recordIds.add(objectRecordResponse.getData().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create object records from an input stream")
    class TestCreateObjectRecordsStream {

        ObjectRecordBulkResponse createObjectRecordsResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"name__v", "description__c"});
            for (int i = 0; i < 5; i++) {
                String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
                String description = "VAPIL Test";
                data.add(new String[]{name, description});
            }

            FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws FileNotFoundException {
            File file = new File(PATH_CREATE_OBJECT_RECORDS_CSV);
            InputStream inputStream = new FileInputStream(file);


            createObjectRecordsResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputStream(inputStream)
                    .createAndUpsertObjectRecords(OBJECT_NAME);

            assertNotNull(createObjectRecordsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createObjectRecordsResponse.isSuccessful());
            assertNotNull(createObjectRecordsResponse.getData());
            for (ObjectRecordResponse objectRecordResponse : createObjectRecordsResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                assertNotNull(objectRecordResponse.getData().getId());
                assertNotNull(objectRecordResponse.getData().getUrl());
                recordIds.add(objectRecordResponse.getData().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully send a call that fails to create object records from a csv file")
    class TestCreateObjectRecordsError {

        ObjectRecordBulkResponse createObjectRecordsResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"name__v", "description__c"});
            for (int i = 0; i < 2; i++) {
                String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
                String description = "VAPIL Test";
                data.add(new String[]{name, description});
            }

            FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);

            // Create object
            ObjectRecordBulkResponse createResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                    .createAndUpsertObjectRecords(OBJECT_NAME);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            createObjectRecordsResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                    .createAndUpsertObjectRecords(OBJECT_NAME);

            assertNotNull(createObjectRecordsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createObjectRecordsResponse.isSuccessful());
            assertNotNull(createObjectRecordsResponse.getData());
            for (ObjectRecordResponse objectRecordResponse : createObjectRecordsResponse.getData()) {
                assertTrue(objectRecordResponse.isFailure());
                assertTrue(objectRecordResponse.hasErrors());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate roll-up field recalculation")
    class TestRecalculateRollupFields {

        VaultResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordRequest.class)
                    .recalculateRollupFields(OBJECT_NAME_PARENT);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve roll-up field recalculation status")
    class TestRetrieveRollupFieldRecalculationStatus {

        JobStatusResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordRequest.class)
                    .retrieveRollupFieldRecalculationStatus(OBJECT_NAME_PARENT);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getStatus());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("have no errors and return isSuccessful() = true when response type is WARNING")
    class TestUpdateObjectRecordsWarning {

        ObjectRecordBulkResponse updateObjectRecordsResponse = null;
        List<String> recordIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create objects
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 2);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }

            List<String[]> updateData = new ArrayList<>();
            updateData.add(new String[]{"id", "description__c"});

            for (int i = 0; i < recordIds.size(); i++) {
                String id = recordIds.get(i);
                String description = "VAPIL Test";
                updateData.add(new String[]{id, description});
            }

            FileHelper.writeCsvFile(PATH_UPDATE_OBJECT_RECORDS_CSV, updateData);
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            updateObjectRecordsResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_UPDATE_OBJECT_RECORDS_CSV)
                    .updateObjectRecords(OBJECT_NAME);

            assertNotNull(updateObjectRecordsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertFalse(updateObjectRecordsResponse.isSuccessful());
            assertTrue(updateObjectRecordsResponse.hasWarnings());
            assertFalse(updateObjectRecordsResponse.hasErrors());
            assertNull(updateObjectRecordsResponse.getErrors());
        }
    }
}