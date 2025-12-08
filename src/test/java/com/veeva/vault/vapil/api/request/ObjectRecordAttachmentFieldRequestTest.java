package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ObjectRecordAttachmentFieldRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Object Record Attachment Field Request should")
public class ObjectRecordAttachmentFieldRequestTest {

    private static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;
    private static final String ATTACHMENT_FIELD_NAME = ObjectRecordAttachmentFieldRequestHelper.ATTACHMENT_FIELD_NAME;
    private static final String PATH_TEST_FILE = FileHelper.PATH_LOCAL_TEST_FILE;
    private static final String PATH_DOWNLOAD_ATTACHMENT_FIELD_FILE = ObjectRecordAttachmentFieldRequestHelper.PATH_DOWNLOAD_ATTACHMENT_FIELD_FILE;
    private static final String PATH_DOWNLOAD_ATTACHMENT_FIELD_FILES = ObjectRecordAttachmentFieldRequestHelper.PATH_DOWNLOAD_ATTACHMENT_FIELD_FILES;

    private static final String PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON = ObjectRecordAttachmentFieldRequestHelper.PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download an attachment field file binary content")
    class TestDownloadAttachmentFieldFileToBinary {

        private VaultResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .downloadAttachmentFieldFile(OBJECT_NAME, recordId, ATTACHMENT_FIELD_NAME);

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
    @DisplayName("successfully download an attachment field file to a file")
    class TestDownloadAttachmentFieldFileToFile {

        private VaultResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setOutputPath(FileHelper.PATH_RESOURCES_FOLDER + PATH_DOWNLOAD_ATTACHMENT_FIELD_FILE)
                    .downloadAttachmentFieldFile(OBJECT_NAME, recordId, ATTACHMENT_FIELD_NAME);

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
    @DisplayName("successfully download all attachment field files as binary content")
    class TestDownloadAllAttachmentFieldFilesToBinary {

        private VaultResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .downloadAllAttachmentFieldFiles(OBJECT_NAME, recordId);

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
    @DisplayName("successfully download all attachment field files to a zip file")
    class TestDownloadAllAttachmentFieldFilesToFile {

        private VaultResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setOutputPath(FileHelper.PATH_RESOURCES_FOLDER + PATH_DOWNLOAD_ATTACHMENT_FIELD_FILES)
                    .downloadAllAttachmentFieldFiles(OBJECT_NAME, recordId);

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
    @DisplayName("successfully export attachment field files using a json request string")
    class TestExportAttachmentFieldFilesFromRequestStringSuccess {

        private ObjectRecordAttachmentFieldExportResponse response = null;
        private String jsonString;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("id", recordId);
            List<Map<String, String>> jsonList = Arrays.asList(jsonObject);

            try {
                jsonString = objectMapper.writeValueAsString(jsonList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            Set<String> fieldNames = new HashSet<>();
            fieldNames.add(ATTACHMENT_FIELD_NAME);

            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setContentTypeJson()
                    .setFieldNames(fieldNames)
                    .setRequestString(jsonString)
                    .exportAttachmentFieldFiles(OBJECT_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getJobId());
            assertNotNull(response.getData().getRecords());
            for (ObjectRecordAttachmentFieldExportResponse.Data.ExportResult exportResult : response.getData().getRecords()) {
                assertEquals("SUCCESS", exportResult.getResponseStatus());
                assertNotNull(exportResult.getData().getId());
                assertNotNull(exportResult.getData().getIdParamValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("unsuccessfully export attachment field files using a json request string")
    class TestExportAttachmentFieldFilesFromRequestStringFailure {

        private ObjectRecordAttachmentFieldExportResponse response = null;
        private String jsonString;

        @BeforeAll
        void setup() {
            String query = """
                    SELECT id,
                    FILENAME(%s)
                    FROM %s
                    WHERE %s = null
                    MAXROWS 1
                    """.formatted(ATTACHMENT_FIELD_NAME, OBJECT_NAME, ATTACHMENT_FIELD_NAME);
            QueryRequest queryRequest = vaultClient.newRequest(QueryRequest.class);
            QueryResponse queryResponse = queryRequest.query(query);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("id", recordId);
            List<Map<String, String>> jsonList = Arrays.asList(jsonObject);

            try {
                jsonString = objectMapper.writeValueAsString(jsonList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            Set<String> fieldNames = new HashSet<>();
            fieldNames.add(ATTACHMENT_FIELD_NAME);

            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setContentTypeJson()
                    .setFieldNames(fieldNames)
                    .setRequestString(jsonString)
                    .exportAttachmentFieldFiles(OBJECT_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertEquals("FAILURE", response.getData().getResponseStatus());
            assertNotNull(response.getData().getRecords());
            for (ObjectRecordAttachmentFieldExportResponse.Data.ExportResult exportResult : response.getData().getRecords()) {
                assertEquals("WARNING", exportResult.getResponseStatus());
                assertNotNull(exportResult.getWarnings());
                assertNotNull(exportResult.getWarnings().get(0).getWarningType());
                assertNotNull(exportResult.getWarnings().get(0).getMessage());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export attachment field files using a json file")
    class TestExportAttachmentFieldFilesFromInputPath {

        private ObjectRecordAttachmentFieldExportResponse response = null;

        @BeforeAll
        void setup() throws IOException {
            List<String> recordIds = new ArrayList<>();
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();
            recordIds.add(recordId);

            ObjectRecordAttachmentFieldRequestHelper.writeToExportAttachmentFieldFilesJson(recordIds);
        }

        @Test
        @Order(1)
        public void testRequest() {
            Set<String> fieldNames = new HashSet<>();
            fieldNames.add(ATTACHMENT_FIELD_NAME);

            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setContentTypeJson()
                    .setFieldNames(fieldNames)
                    .setInputPath(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON)
                    .exportAttachmentFieldFiles(OBJECT_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getJobId());
            assertNotNull(response.getData().getRecords());
            for (ObjectRecordAttachmentFieldExportResponse.Data.ExportResult exportResult : response.getData().getRecords()) {
                assertEquals("SUCCESS", exportResult.getResponseStatus());
                assertNotNull(exportResult.getData().getId());
                assertNotNull(exportResult.getData().getIdParamValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export attachment field files using a binary file")
    class TestExportAttachmentFieldFilesFromBinary {

        private ObjectRecordAttachmentFieldExportResponse response = null;

        @BeforeAll
        void setup() throws IOException {
            List<String> recordIds = new ArrayList<>();
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();
            recordIds.add(recordId);

            ObjectRecordAttachmentFieldRequestHelper.writeToExportAttachmentFieldFilesJson(recordIds);
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            Set<String> fieldNames = new HashSet<>();
            fieldNames.add(ATTACHMENT_FIELD_NAME);

            File file = new File(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON);
            byte[] bytesArray = Files.readAllBytes(file.toPath());

            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setContentTypeJson()
                    .setFieldNames(fieldNames)
                    .setBinaryFile("export_attachment_field_files.json", bytesArray)
                    .exportAttachmentFieldFiles(OBJECT_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getJobId());
            assertNotNull(response.getData().getRecords());
            for (ObjectRecordAttachmentFieldExportResponse.Data.ExportResult exportResult : response.getData().getRecords()) {
                assertEquals("SUCCESS", exportResult.getResponseStatus());
                assertNotNull(exportResult.getData().getId());
                assertNotNull(exportResult.getData().getIdParamValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve results from export attachment field files job")
    class TestRetrieveAttachmentFieldFilesExportResults {

        private ObjectRecordAttachmentFieldExportResultsResponse response = null;
        private String jobId = "";

        @BeforeAll
        void setup() throws IOException {
//            Get record ID with attachment field and write to json file
            List<String> recordIds = new ArrayList<>();
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();
            recordIds.add(recordId);
            ObjectRecordAttachmentFieldRequestHelper.writeToExportAttachmentFieldFilesJson(recordIds);

//            Export attachment field files and get job ID
            ObjectRecordAttachmentFieldExportResponse exportresponse = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setInputPath(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON)
                    .exportAttachmentFieldFiles(OBJECT_NAME);
            assertTrue(exportresponse.isSuccessful());
            jobId = exportresponse.getData().getJobId();

            boolean jobCompletion = JobStatusHelper.checkJobCompletion(vaultClient, Integer.parseInt(jobId));
            assertTrue(jobCompletion);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .retrieveAttachmentFieldFilesExportResults(OBJECT_NAME, jobId);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getFilename());
            assertNotNull(response.getData().getSize());
            assertNotNull(response.getData().getFileparts());
            assertNotNull(response.getData().getFilepartDetails());
            for (ObjectRecordAttachmentFieldExportResultsResponse.Data.FilePart filePart : response.getData().getFilepartDetails()) {
                assertNotNull(filePart.getName());
                assertNotNull(filePart.getFilename());
                assertNotNull(filePart.getFilepart());
                assertNotNull(filePart.getSize());
                assertNotNull(filePart.getUrl());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download an attachment field files export as binary content")
    class TestDownloadAttachmentFieldFilesExport {

        private VaultResponse response = null;
        private String filePartName;

        @BeforeAll
        void setup() throws IOException {
//            Get record ID with attachment field and write to json file
            List<String> recordIds = new ArrayList<>();
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            String recordId = queryResponse.getData().get(0).get("id").toString();
            recordIds.add(recordId);
            ObjectRecordAttachmentFieldRequestHelper.writeToExportAttachmentFieldFilesJson(recordIds);

//            Export attachment field files and get job ID
            ObjectRecordAttachmentFieldExportResponse exportResponse = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setInputPath(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON)
                    .exportAttachmentFieldFiles(OBJECT_NAME);
            assertTrue(exportResponse.isSuccessful());
            String jobId = exportResponse.getData().getJobId();

            boolean jobCompletion = JobStatusHelper.checkJobCompletion(vaultClient, Integer.parseInt(jobId));
            assertTrue(jobCompletion);

//            Get File Part Name
            ObjectRecordAttachmentFieldExportResultsResponse exportResultsResponse = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .retrieveAttachmentFieldFilesExportResults(OBJECT_NAME, jobId);
            assertTrue(exportResultsResponse.isSuccessful());

            filePartName = exportResultsResponse.getData().getFilepartDetails().get(0).getName();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .downloadAttachmentFieldFilesExport(OBJECT_NAME, filePartName);

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
    @DisplayName("successfully update an attachment field file on a record from input path")
    class TestUpdateAttachmentFieldFileFromInputPath {

        private ObjectRecordBulkResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setInputPath(PATH_TEST_FILE)
                    .updateAttachmentFieldFile(OBJECT_NAME, recordId, ATTACHMENT_FIELD_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() throws InterruptedException {
            assertFalse(response.isFailure());
            assertNotNull(response.getData());
            for (ObjectRecordResponse objectRecordResponse : response.getData()) {
                assertFalse(objectRecordResponse.isFailure());
                assertNotNull(objectRecordResponse.getData().getId());
                assertNotNull(objectRecordResponse.getData().getUrl());
            }
            Thread.sleep(3000);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update an attachment field file on a record from binary file")
    class TestUpdateAttachmentFieldFileFromBinary {

        private ObjectRecordBulkResponse response = null;
        private String recordId;

        @BeforeAll
        void setup() {
            QueryResponse queryResponse = ObjectRecordAttachmentFieldRequestHelper.queryForRecordIdWithAttachmentField(vaultClient);
            assertFalse(queryResponse.isFailure());
            recordId = queryResponse.getData().get(0).get("id").toString();
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File file = new File(PATH_TEST_FILE);
            byte[] bytesArray = Files.readAllBytes(file.toPath());
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setBinaryFile("test_binary_file.txt", bytesArray)
                    .updateAttachmentFieldFile(OBJECT_NAME, recordId, ATTACHMENT_FIELD_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() throws InterruptedException {
            assertFalse(response.isFailure());
            assertNotNull(response.getData());
            for (ObjectRecordResponse objectRecordResponse : response.getData()) {
                assertFalse(objectRecordResponse.isFailure());
                assertNotNull(objectRecordResponse.getData().getId());
                assertNotNull(objectRecordResponse.getData().getUrl());
            }
            Thread.sleep(3000);
        }
    }
}
