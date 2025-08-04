package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.builder.LoaderTaskBuilder;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("LoaderRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Loader Request should")
public class LoaderRequestTest {

    private static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;
    private static final String PATH_LOAD_OBJECT_RECORDS_CSV = LoaderRequestHelper.PATH_LOAD_OBJECT_RECORDS_CSV;
    private static final String PATH_LOAD_CREATE_ATTACHMENTS_CSV = LoaderRequestHelper.PATH_LOAD_CREATE_ATTACHMENTS_CSV;
    private static final String PATH_LOAD_DELETE_ATTACHMENTS_CSV = LoaderRequestHelper.PATH_LOAD_DELETE_ATTACHMENTS_CSV;
    private static final String PATH_LOAD_ASSIGN_ROLES_CSV = LoaderRequestHelper.PATH_LOAD_ASSIGN_ROLES_CSV;
    private static final String PATH_LOAD_REMOVE_ROLES_CSV = LoaderRequestHelper.PATH_LOAD_REMOVE_ROLES_CSV;

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

//    @AfterAll
//    static void teardown() {
//        DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
//        Assertions.assertTrue(response.isSuccessful());
//        for (DocumentResponse documentResponse : response.getData()) {
//            Assertions.assertTrue(documentResponse.isSuccessful());
//        }
//    }

    @Test
    @Order(1)
    @DisplayName("successfully create a loader job and load a set of data files")
    public void testLoadDataObjects() throws InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        jsonArray.addObject()
                .put("object_type", "documents__v")
                .put("action", "create")
                .put("file", PATH_FILE_STAGING_LOADER_LOAD_OBJECT_RECORDS_CSV)
                .put("order", 1);

        LoaderResponse loadResponse = vaultClient.newRequest(LoaderRequest.class)
                .setJson(jsonArray.toString())
                .loadDataObjects();

        Assertions.assertTrue(loadResponse.isSuccessful());
        Assertions.assertNotNull(loadResponse.getJobId());
        loadJobId = loadResponse.getJobId();
        Assertions.assertNotNull(loadResponse.getUrl());
        Assertions.assertNotNull(loadResponse.getTasks());
        for (LoaderTask task : loadResponse.getTasks()) {
            Assertions.assertNotNull(task.getTaskId());
            loadTasks.add(task.getTaskId());
        }
        Thread.sleep(5000);
    }

    @Test
    @Order(2)
    @DisplayName("successfully retrieve success logs of the loader results")
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
    public void testBuild() {
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
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
    @Order(4)
    @DisplayName("successfully create and run a loader job to extract data")
    public void testExtractDataFiles() throws Exception {
        LoaderResponse extractResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(loaderTask)
                .extractDataFiles();

        Assertions.assertTrue(extractResponse.isSuccessful());
        Assertions.assertNotNull(extractResponse.getJobId());
        extractJobId = extractResponse.getJobId();
        Assertions.assertNotNull(extractResponse.getUrl());
        Assertions.assertNotNull(extractResponse.getTasks());
        for (LoaderTask task : extractResponse.getTasks()) {
            Assertions.assertNotNull(task.getTaskId());
            extractTasks.add(task.getTaskId());
        }
        Thread.sleep(5000);
    }

    @Test
    @Order(5)
    @DisplayName("successfully retrieve results of a specified job task")
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
    public void testRetrieveLoaderExtractRenditionsResults() {
        for (Integer taskId : extractTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractRenditionsResults(extractJobId, taskId);
            Assertions.assertNotNull(resultResponse);
            Assertions.assertNotNull(resultResponse.getBinaryContent());
        }
    }

    @Test
    @Disabled("Needs Further setup/Eval")
    public void testExtract() throws Exception {
        //There are dependencies so an autonomous approach will require duplication.
        Integer jobId = null;
        List<Integer> taskIds = new ArrayList<>();

        jobId = 46809;
        taskIds.add(1);

        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'");

        System.out.println(taskBuilder.build().toMap());

        LoaderResponse extractResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(taskBuilder.build())
                .extractDataFiles();


        System.out.println(extractResponse.getResponse());

        if (extractResponse.isSuccessful()) {
            jobId = extractResponse.getJobId();
            if (extractResponse.getTasks() != null) {
                for (LoaderTask tasks : extractResponse.getTasks()) {
                    taskIds.add(tasks.getTaskId());
                }
            }
        }

        if (jobId != null) {
            boolean retry = true;
            while (retry) {


                JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);
                if ((jobStatusResponse != null)
                        && (!jobStatusResponse.hasErrors())
                        && (jobStatusResponse.getData() != null)) {

                    Job job = jobStatusResponse.getData();
                    if (job.getRunEndDate() != null) {
                        retry = false;

                        for (Integer taskId : taskIds) {
                            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoaderExtractResults(jobId, taskId);
                            System.out.println(new String(resultResponse.getBinaryContent()));

                            VaultResponse renditionResponse = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoaderExtractRenditionsResults(jobId, taskId);
                            System.out.println(new String(renditionResponse.getBinaryContent()));
                        }
                    } else {
                        //NOTE: MUST WAIT 30 SECONDS!
                        //{"responseStatus":"FAILURE","errors":[{"type":"API_LIMIT_EXCEEDED","message":"Too many polling requests"}]}
                        System.out.println("wait 30 seconds - job not complete");
                        Thread.sleep(30000);
                    }
                }
            }
        }
    }

    @Test
    @Disabled("Needs Further setup/Eval")
    public void testLoad() throws Exception {
        //There are dependencies so an autonomous approach will require duplication.
        Integer jobId = null;
        List<Integer> taskIds = new ArrayList<>();

        //NOTE: be sure to put a csv file in the FTPS folder at /vapil/products.csv
        //include all required fields
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .setAction(LoaderTaskBuilder.Action.CREATE)
                .setFile("/vapil/products.csv")
                .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
                .setObject("product__v");

        LoaderResponse loadResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(taskBuilder.build())
                .loadDataObjects();

        System.out.println(loadResponse.getResponse());

        if (loadResponse.isSuccessful()) {
            jobId = loadResponse.getJobId();
            if (loadResponse.getTasks() != null) {
                for (LoaderTask tasks : loadResponse.getTasks()) {
                    taskIds.add(tasks.getTaskId());
                }
            }
        }

        if (jobId != null) {
            boolean retry = true;
            while (retry) {
                JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);
                if ((jobStatusResponse != null)
                        && (!jobStatusResponse.hasErrors())
                        && (jobStatusResponse.getData() != null)) {

                    Job job = jobStatusResponse.getData();
                    if (job.getRunEndDate() != null) {
                        retry = false;

                        for (Integer taskId : taskIds) {
                            VaultResponse successLogResults = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoadSuccessLogResults(jobId, taskId);
                            System.out.println(new String(successLogResults.getBinaryContent()));

                            VaultResponse failureLogResults = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoadFailureLogResults(jobId, taskId);
                            System.out.println(new String(failureLogResults.getBinaryContent()));
                        }
                    }
                    else {
                        //NOTE: MUST WAIT 30 SECONDS!
                        //{"responseStatus":"FAILURE","errors":[{"type":"API_LIMIT_EXCEEDED","message":"Too many polling requests"}]}
                        System.out.println("wait 30 seconds - job not complete");
                        Thread.sleep(30000);
                    }
                }
            }
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
                    .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
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
                assertNotNull(task.getObjectType());
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
                    .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
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
                assertNotNull(task.getObjectType());
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
                    .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
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
                assertNotNull(task.getObjectType());
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
                    .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
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
                assertNotNull(task.getObjectType());
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
                    .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
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
                assertNotNull(task.getObjectType());
                assertNotNull(task.getFile());
            }

            jobId = response.getJobId();
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
