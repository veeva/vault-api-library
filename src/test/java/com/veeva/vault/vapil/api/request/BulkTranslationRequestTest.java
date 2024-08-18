package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.request.BulkTranslationRequest.MESSAGE_TYPE;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("BulkTranslationRequest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Bulk translation request should")
public class BulkTranslationRequestTest {

    private static VaultClient vaultClient;
    private static String BULK_IMPORT_TRANSLATION_FILE_NAME = "bulk_translation_test.csv";
    private static String BULK_IMPORT_TRANSLATION_FILE_CSV_PATH = FileHelper.getPathResourcesFolder() + "bulk_translation" + File.separator + BULK_IMPORT_TRANSLATION_FILE_NAME;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export bulk translation file")
    class TestExportBulkTranslationFile {

        private BulkTranslationJobResponse response = null;

        @AfterAll
        public void teardown() throws InterruptedException {
//            Check that import job completed successfully
            boolean jobCompleted = JobStatusHelper.checkJobCompletion(vaultClient, Integer.valueOf(response.getData().getJobId()));
            assertTrue(jobCompleted);

//            Get the job status to retrieve the file URL
            Thread.sleep(10000);
            JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class)
                    .retrieveJobStatus(Integer.valueOf(response.getData().getJobId()));

            String url = "";
            for (Job.Link link : jobStatusResponse.getData().getLinks()) {
                if (link.getRel().equals("file")) {
                    url = link.getHref();
                }
            }

//            Extract the file path from the URL and delete the file
            int startIndex = url.indexOf("VAPIL");
            String result = url.substring(startIndex);

            FileStagingJobResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(result);
            assertTrue(fileStagingResponse.isSuccessful());

            JobStatusHelper.checkJobCompletion(vaultClient, fileStagingResponse.getData().getJobId());

        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BulkTranslationRequest.class)
                    .exportBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, "ja");

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getJobId());
            assertNotNull(response.getData().getUrl());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully import bulk translation file")
    class TestImportBulkTranslationFile {

        private BulkTranslationJobResponse response = null;

        @BeforeAll
        public void setup() {
            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setInputPath(BULK_IMPORT_TRANSLATION_FILE_CSV_PATH)
                    .createFolderOrFile(FileStagingRequest.Kind.FILE, BULK_IMPORT_TRANSLATION_FILE_NAME);

            assertTrue(createFileResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            String fssFilePath = "/" + BULK_IMPORT_TRANSLATION_FILE_NAME;
            response = vaultClient.newRequest(BulkTranslationRequest.class)
                    .importBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, fssFilePath);

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getJobId());
            assertNotNull(response.getData().getUrl());
        }
    }

    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve import bulk job summary")
    class TestRetrieveImportBulkTranslationFileJobSummary {

        private BulkTranslationImportSummaryResponse response = null;
        private String jobId;

        @BeforeAll
        public void setup() {
            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setInputPath(BULK_IMPORT_TRANSLATION_FILE_CSV_PATH)
                    .createFolderOrFile(FileStagingRequest.Kind.FILE, BULK_IMPORT_TRANSLATION_FILE_NAME);

            assertTrue(createFileResponse.isSuccessful());

            String fssFilePath = "/" + BULK_IMPORT_TRANSLATION_FILE_NAME;
            BulkTranslationJobResponse importResponse = vaultClient.newRequest(BulkTranslationRequest.class)
                    .importBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, fssFilePath);

            assertTrue(importResponse != null);
            assertTrue(importResponse.isSuccessful());

            jobId = importResponse.getData().getJobId();
            boolean jobComplete = JobStatusHelper.checkJobCompletion(vaultClient, Integer.valueOf(jobId));
            assertTrue(jobComplete);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BulkTranslationRequest.class)
                    .retrieveImportBulkTranslationFileJobSummary(jobId);

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getAdded());
            assertNotNull(response.getData().getFailed());
            assertNotNull(response.getData().getIgnored());
            assertNotNull(response.getData().getUpdated());
        }
    }

    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve import bulk job errors")
    class TestRetrieveImportBulkTranslationFileJobErrors {

        private VaultResponse response = null;
        private String jobId;

        @BeforeAll
        public void setup() {
            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setInputPath(BULK_IMPORT_TRANSLATION_FILE_CSV_PATH)
                    .createFolderOrFile(FileStagingRequest.Kind.FILE, BULK_IMPORT_TRANSLATION_FILE_NAME);

            assertTrue(createFileResponse.isSuccessful());

            String fssFilePath = "/" + BULK_IMPORT_TRANSLATION_FILE_NAME;
            BulkTranslationJobResponse importResponse = vaultClient.newRequest(BulkTranslationRequest.class)
                    .importBulkTranslationFile(MESSAGE_TYPE.SYSTEM_MESSAGES, fssFilePath);

            assertTrue(importResponse != null);
            assertTrue(importResponse.isSuccessful());

            jobId = importResponse.getData().getJobId();
            boolean jobComplete = JobStatusHelper.checkJobCompletion(vaultClient, Integer.valueOf(jobId));
            assertTrue(jobComplete);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BulkTranslationRequest.class)
                    .retrieveImportBulkTranslationFileJobErrors(jobId);

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getBinaryContent());
        }
    }
}
