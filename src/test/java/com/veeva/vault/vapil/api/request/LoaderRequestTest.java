package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.builder.LoaderTaskBuilder;
import com.veeva.vault.vapil.api.model.builder.LoaderTaskBuilder.ExtractOption;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("LoaderRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Loader Request should")
public class LoaderRequestTest {

    private static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;
    private static final String VAPIL_OBJECT_TYPE_NAME = "vapil_test_type_1_object__c";
    private static final String PATH_LOAD_OBJECT_RECORDS_CSV = LoaderRequestHelper.PATH_LOAD_OBJECT_RECORDS_CSV;
    private static final String PATH_LOAD_CREATE_ATTACHMENTS_CSV = LoaderRequestHelper.PATH_LOAD_CREATE_ATTACHMENTS_CSV;
    private static final String PATH_LOAD_DELETE_ATTACHMENTS_CSV = LoaderRequestHelper.PATH_LOAD_DELETE_ATTACHMENTS_CSV;
    private static final String PATH_LOAD_ASSIGN_ROLES_CSV = LoaderRequestHelper.PATH_LOAD_ASSIGN_ROLES_CSV;
    private static final String PATH_LOAD_REMOVE_ROLES_CSV = LoaderRequestHelper.PATH_LOAD_REMOVE_ROLES_CSV;
    private static final String PATH_LOAD_CHANGE_OBJECT_TYPE_CSV = LoaderRequestHelper.PATH_LOAD_CHANGE_OBJECT_TYPE_CSV;
    private static final String PATH_RETRIEVE_EXTRACT_TEXT_RESULTS_FILE = LoaderRequestHelper.PATH_RETRIEVE_EXTRACT_TEXT_RESULTS_FILE;

    private static final String PATH_FILE_STAGING_LOADER_LOAD_OBJECT_RECORDS_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_OBJECT_RECORDS_CSV);
    private static final String PATH_FILE_STAGING_LOADER_LOAD_CREATE_ATTACHMENTS_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_CREATE_ATTACHMENTS_CSV);
    private static final String PATH_FILE_STAGING_LOADER_LOAD_DELETE_ATTACHMENTS_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_DELETE_ATTACHMENTS_CSV);
    private static final String PATH_FILE_STAGING_LOADER_LOAD_ASSIGN_ROLES_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_ASSIGN_ROLES_CSV);
    private static final String PATH_FILE_STAGING_LOADER_LOAD_REMOVE_ROLES_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_REMOVE_ROLES_CSV);
    private static final String PATH_FILE_STAGING_LOADER_LOAD_CHANGE_OBJECT_TYPE_CSV = String.format("%s/%s",
            FileStagingHelper.PATH_FILE_STAGING_LOADER_FOLDER, LoaderRequestHelper.NAME_LOAD_CHANGE_OBJECT_TYPE_CSV);

    static LoaderTask loaderTask;
    static int loadJobId;
    static int extractJobId;
    static List<Integer> loadTasks = new ArrayList<>();
    static List<Integer> extractTasks = new ArrayList<>();
    static List<Integer> docIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) throws IOException {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }


    @Test
    @Order(2)
    @DisplayName("successfully retrieve success logs of the loader results")
    @Disabled
    public void testRetrieveLoadSuccessLogResults() {
        for (Integer taskId : loadTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(loadJobId, taskId);

            Assertions.assertTrue(resultResponse.isSuccessful());
            Assertions.assertNotNull(resultResponse.getBinaryContent());
            byte[] byteArray = resultResponse.getBinaryContent();
            String responseString = new String(byteArray, StandardCharsets.UTF_8);
            extractDocIds(responseString);
        }
    }


    @Test
    @Order(3)
    @DisplayName("successfully build a loader task")
    @Disabled
    public void testBuild() {
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setEntityType(LoaderTaskBuilder.EntityType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'");
        loaderTask = taskBuilder.build();

        Assertions.assertNotNull(loaderTask);
        Assertions.assertNotNull(loaderTask.getFields());
        Assertions.assertEquals(2,loaderTask.getFields().size());
        Assertions.assertNotNull(loaderTask.getVqlCriteria());
        Assertions.assertNotEquals("",loaderTask.getVqlCriteria());
    }

    @Test
    @Order(5)
    @DisplayName("successfully retrieve results of a specified job task")
    @Disabled
    public void testRetrieveLoaderExtractResults() {
        for (Integer taskId : extractTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractResults(extractJobId, taskId);
            Assertions.assertNotNull(resultResponse);
            Assertions.assertNotNull(resultResponse.getBinaryContent());
        }
    }

    @Test
    @Order(6)
    @DisplayName("successfully retrieve results of a specified job task that includes renditions requested with documents.")
    @Disabled
    public void testRetrieveLoaderExtractRenditionsResults() {
        for (Integer taskId : extractTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractRenditionsResults(extractJobId, taskId);
            Assertions.assertNotNull(resultResponse);
            Assertions.assertNotNull(resultResponse.getBinaryContent());
        }
    }

    public void extractDocIds(String csvResponse) {
        String[] lines = csvResponse.split("\n");
        // Skip the header line
        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].split(",");

            if (columns.length >= 2) {
                String docId = columns[1];
                docIds.add(Integer.valueOf(docId));
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully load object records")
    class TestLoadDataObjectsRecords {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() {
            File file = new File(PATH_LOAD_OBJECT_RECORDS_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_OBJECT_RECORDS_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Extract record ids from the CSV response
            try {
                recordIds = extractIdsFromCSV(loadResultsResponse.getBinaryContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertNotNull(recordIds);

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.CREATE)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_OBJECT_RECORDS_CSV)
                    .setRecordMigrationMode(true)
                    .setNoTriggers(true)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
                assertNotNull(task.getRecordMigrationMode());
                assertNotNull(task.getNoTriggers());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create attachments for object records")
    class TestLoadDataCreateAttachments {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws IOException {
//            Create a record to attach the file to
            ObjectRecordBulkResponse createRecordsResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createRecordsResponse.isSuccessful());
            assertTrue(createRecordsResponse.getData().get(0).isSuccessful());
            recordIds.add(createRecordsResponse.getData().get(0).getData().getId());

//            Write to CSV File
            LoaderRequestHelper.writeToLoadCreateAttachmentsFile(recordIds);

//            Upload the CSV file to File Staging
            File file = new File(PATH_LOAD_CREATE_ATTACHMENTS_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_CREATE_ATTACHMENTS_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.CREATE_ATTACHMENTS)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_CREATE_ATTACHMENTS_CSV)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete attachments from object records")
    class TestLoadDataDeleteAttachments {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        List<Integer> attachmentIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws IOException {
//            Create a record to attach the file to
            ObjectRecordBulkResponse createRecordsResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createRecordsResponse.isSuccessful());
            assertTrue(createRecordsResponse.getData().get(0).isSuccessful());
            recordIds.add(createRecordsResponse.getData().get(0).getData().getId());

//            Create an attachment for the record
            ObjectRecordAttachmentResponse createAttachmentResponse = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                    .setInputPath(FileHelper.PATH_LOCAL_TEST_FILE)
                    .createObjectRecordAttachment(OBJECT_NAME, recordIds.get(0));
            assertTrue(createAttachmentResponse.isSuccessful());
            attachmentIds.add(createAttachmentResponse.getData().get(0).getId());

//            Write to CSV File
            LoaderRequestHelper.writeToLoadDeleteAttachmentsFile(recordIds, attachmentIds);

//            Upload the CSV file to File Staging
            File file = new File(PATH_LOAD_DELETE_ATTACHMENTS_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_DELETE_ATTACHMENTS_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.DELETE_ATTACHMENTS)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_DELETE_ATTACHMENTS_CSV)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully assign roles to object records")
    class TestLoadDataAssignRoles {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws IOException {
//            Create a record to assign roles to
            ObjectRecordBulkResponse createRecordsResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createRecordsResponse.isSuccessful());
            assertTrue(createRecordsResponse.getData().get(0).isSuccessful());
            recordIds.add(createRecordsResponse.getData().get(0).getData().getId());

//            Write to CSV File
            LoaderRequestHelper.writeToLoadAssignRolesFile(recordIds, vaultClient.getAuthenticationResponse().getUserId());

//            Upload the CSV file to File Staging
            File file = new File(PATH_LOAD_ASSIGN_ROLES_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_ASSIGN_ROLES_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.ASSIGN_ROLES)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_ASSIGN_ROLES_CSV)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully remove roles from object records")
    class TestLoadDataRemoveRoles {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws IOException {
//            Create a record to assign roles to
            ObjectRecordBulkResponse createRecordsResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createRecordsResponse.isSuccessful());
            assertTrue(createRecordsResponse.getData().get(0).isSuccessful());
            recordIds.add(createRecordsResponse.getData().get(0).getData().getId());

//            Assign a role to the record
            String csvStringTemplate = """
            id,viewer__v.users
            %s,%s
            """;
            String csvString = String.format(csvStringTemplate, recordIds.get(0), vaultClient.getAuthenticationResponse().getUserId());

            ObjectRecordRoleChangeResponse assignRoleResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .setContentTypeCsv()
                    .setRequestString(csvString)
                    .assignUsersAndGroupsToRolesOnObjectRecords(OBJECT_NAME);
            assertTrue(assignRoleResponse.isSuccessful());

//            Write to CSV File
            LoaderRequestHelper.writeToLoadRemoveRolesFile(recordIds, vaultClient.getAuthenticationResponse().getUserId());

//            Upload the CSV file to File Staging
            File file = new File(PATH_LOAD_REMOVE_ROLES_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_REMOVE_ROLES_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.REMOVE_ROLES)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_REMOVE_ROLES_CSV)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully change object type on object records")
    class TestLoadDataChangeObjectType {
        LoaderResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int jobId;

        @BeforeAll
        public void setup() throws IOException {
//            Create a record to attach the file to
            ObjectRecordBulkResponse createRecordsResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createRecordsResponse.isSuccessful());
            assertTrue(createRecordsResponse.getData().get(0).isSuccessful());
            recordIds.add(createRecordsResponse.getData().get(0).getData().getId());

//            Query for object_type__v record
            String objectTypeQuery = """
                    SELECT id
                    FROM object_type__v
                    WHERE api_name__v = '%s'
                    """.formatted(VAPIL_OBJECT_TYPE_NAME);

            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(objectTypeQuery);
            assertFalse(queryResponse.isFailure());
            String objectTypeId = queryResponse.getData().get(0).getString("id");

//            Write to CSV File
            LoaderRequestHelper.writeToLoadChangeObjectTypeFile(recordIds, objectTypeId);

//            Upload the CSV file to File Staging
            File file = new File(PATH_LOAD_CHANGE_OBJECT_TYPE_CSV);
            FileStagingHelper.createFileOnFileStaging(vaultClient, file, PATH_FILE_STAGING_LOADER_LOAD_CHANGE_OBJECT_TYPE_CSV, true);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Wait for job completion
            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
            VaultResponse loadResultsResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(jobId, 1);
            assertNotNull(loadResultsResponse);
            assertTrue(loadResultsResponse.isSuccessful());

//            Delete the records
            ObjectRecordBulkResponse deleteRecordsResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteRecordsResponse.isSuccessful());
            for (ObjectRecordResponse recordResponse : deleteRecordsResponse.getData()) {
                assertTrue(recordResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setAction(LoaderTaskBuilder.Action.UPDATE)
                    .setEntityType(LoaderTaskBuilder.EntityType.OBJECTS)
                    .setObject(OBJECT_NAME)
                    .setChangeObjectType(true)
                    .setFile(PATH_FILE_STAGING_LOADER_LOAD_CHANGE_OBJECT_TYPE_CSV)
                    .build();

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTask(loaderTask)
                    .loadDataObjects();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getAction());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFile());
                assertNotNull(task.getChangeObjectType());
            }

            jobId = response.getJobId();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully extract data files (documents) with include text option")
    class TestExtractDataFilesIncludeText {
        LoaderResponse response = null;

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            List<LoaderTask> loaderTasks = new ArrayList<>();
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setEntityType(LoaderTaskBuilder.EntityType.DOCUMENTS)
                    .setFields(List.of("id", "name__v"))
                    .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_TEXT)
                    .build();
            loaderTasks.add(loaderTask);

            response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTasks(loaderTasks)
                    .extractDataFiles();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getUrl());
            assertNotNull(response.getJobId());
            assertNotNull(response.getTasks());
            for (LoaderTask task : response.getTasks()) {
                assertNotNull(task.getTaskId());
                assertNotNull(task.getExtractOptions());
                assertNotNull(task.getEntityType());
                assertNotNull(task.getFields());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve text extract results from a loader job as binary")
    class TestRetrieveLoaderExtractTextResultsBinary {
        VaultResponse response = null;
        int jobId;

        @BeforeAll
        public void setup() throws Exception {
            List<LoaderTask> loaderTasks = new ArrayList<>();
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setEntityType(LoaderTaskBuilder.EntityType.DOCUMENTS)
                    .setFields(List.of("id", "name__v"))
                    .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_TEXT)
                    .build();
            loaderTasks.add(loaderTask);

            LoaderResponse response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTasks(loaderTasks)
                    .extractDataFiles();
            assertTrue(response.isSuccessful());
            jobId = response.getJobId();

            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            response = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractTextResults(jobId, 1);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve text extract results from a loader job as file")
    class TestRetrieveLoaderExtractTextResultsFile {
        VaultResponse response = null;
        int jobId;

        @BeforeAll
        public void setup() throws Exception {
            List<LoaderTask> loaderTasks = new ArrayList<>();
            LoaderTask loaderTask = new LoaderTaskBuilder()
                    .setEntityType(LoaderTaskBuilder.EntityType.DOCUMENTS)
                    .setFields(List.of("id", "name__v"))
                    .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_TEXT)
                    .build();
            loaderTasks.add(loaderTask);

            LoaderResponse response = vaultClient.newRequest(LoaderRequest.class)
                    .addLoaderTasks(loaderTasks)
                    .extractDataFiles();
            assertTrue(response.isSuccessful());
            jobId = response.getJobId();

            JobStatusHelper.checkJobCompletion(vaultClient, jobId);
        }

        @Test
        @Order(1)
        public void testRequest() throws Exception {
            response = vaultClient.newRequest(LoaderRequest.class)
                    .setOutputPath(PATH_RETRIEVE_EXTRACT_TEXT_RESULTS_FILE)
                    .retrieveLoaderExtractTextResults(jobId, 1);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }

    public static List<String> extractIdsFromCSV(byte[] bytes) throws IOException {
        // Create InputStream from bytes
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // Initialize CsvMapper and schema
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader(); // Assume the first row contains headers

        // Parse the CSV and extract the "id" column
        List<String> ids = new ArrayList<>();
        csvMapper.readerFor(Map.class)
                .with(schema)
                .<Map<String, String>>readValues(inputStream)
                .forEachRemaining(row -> {
                    String id = row.get("id");
                    if (id != null && !id.isEmpty()) {
                        ids.add(id);
                    }
                });

        return ids;
    }
}
