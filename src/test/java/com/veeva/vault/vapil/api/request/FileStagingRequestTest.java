/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ResumableUploadSession;
import com.veeva.vault.vapil.api.model.common.ResumableUploadSessionPart;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.request.FileStagingRequest.Kind;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;


@Tag("FileStagingRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("File Staging request should")
public class FileStagingRequestTest {
    static final String RESOURCES_FOLDER_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    static final String TEST_FOLDER_FSS_NAME = "test_create_folder";
    static final String TEST_UPDATE_FOLDER_FSS_NAME = "test_update_folder";
    static final String TEST_FILE_FSS_NAME = "vapil_test_document.docx";
    static final String TEST_DOWNLOAD_FILE_NAME = "vapil_test_document.docx";
    static final String TEST_UPDATE_FILE_FSS_NAME = "vapil_test_document_update.docx";
    static final String TEST_RESUMABLE_UPLOAD_FILE_NAME = "test_resumable_upload.txt";
    static final String TEST_RESUMABLE_UPLOAD_FILE_PATH = RESOURCES_FOLDER_PATH + TEST_RESUMABLE_UPLOAD_FILE_NAME;
    static String resumableUploadSessionId;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve a list of files and folders for the specified path")
    class TestListItemsAtAPath {
        FileStagingItemBulkResponse listItemsAtAPathResponse = null;

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .setLimit(1)
                    .listItemsAtAPath("/");

            assertNotNull(response);
            listItemsAtAPathResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(listItemsAtAPathResponse.isSuccessful());
            assertTrue(listItemsAtAPathResponse.isPaginated());
            assertNotNull(listItemsAtAPathResponse.getData());
            for (FileStagingItemBulkResponse.FileStagingItem item : listItemsAtAPathResponse.getData()) {
                assertNotNull(item.getKind());
                assertNotNull(item.getPath());
                assertNotNull(item.getName());
                if (item.getKind().equals("file")) {
                    assertNotNull(item.getSize());
                    assertNotNull(item.getModifiedDate());
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve a list of files, folders, and a next_page url for the specified path")
    class TestListItemsByPage {
        FileStagingItemBulkResponse listItemsAtAPathResponse = null;

        @Disabled
        @Test
        @Order(1)
        public void testRequest() {
            FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setLimit(1)
                    .setRecursive(true)
                    .listItemsAtAPath("/");

            assertNotNull(response);
            assertTrue(response.isSuccessful());
            assertTrue(response.isPaginated());

            FileStagingItemBulkResponse paginatedResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .listItemsAtPathByPage(response.getResponseDetails().getNextPage());
            assertNotNull(paginatedResponse);
            assertTrue(paginatedResponse.isSuccessful());
            listItemsAtAPathResponse = paginatedResponse;
        }

        @Disabled
        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(listItemsAtAPathResponse.isSuccessful());
            assertTrue(listItemsAtAPathResponse.isPaginated());
            assertNotNull(listItemsAtAPathResponse.getData());

            for (FileStagingItemBulkResponse.FileStagingItem item : listItemsAtAPathResponse.getData()) {
                assertNotNull(item.getKind());
                assertNotNull(item.getPath());
                assertNotNull(item.getName());
                if (item.getKind().equals("file")) {
                    assertNotNull(item.getSize());
                    assertNotNull(item.getModifiedDate());
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a folder at the specified path")
    class TestCreateFolder {
        FileStagingItemResponse createFolderResponse = null;

        @BeforeAll
        public void setup() throws InterruptedException {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .deleteFolderOrFile(TEST_FOLDER_FSS_NAME);

            if (response.isSuccessful()) {
                Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getData().getJobId()));
            }
        }

        @AfterAll
        public void teardown() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .deleteFolderOrFile(TEST_FOLDER_FSS_NAME);

            Assertions.assertTrue(response.isSuccessful());
            Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() {
            createFolderResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createFolderOrFile(Kind.FOLDER, TEST_FOLDER_FSS_NAME);

            assertNotNull(createFolderResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createFolderResponse.isSuccessful());
            assertNotNull(createFolderResponse.getData().getKind());
            assertNotNull(createFolderResponse.getData().getPath());
            assertNotNull(createFolderResponse.getData().getName());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a file at the specified path")
    class TestCreateFile {
        FileStagingItemResponse createFileResponse = null;

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File testFile = new File(FileHelper.getPathTestFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setFile(testFile.getPath(), bytes)
                    .createFolderOrFile(Kind.FILE, TEST_FILE_FSS_NAME);

            assertNotNull(createFileResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createFileResponse.isSuccessful());
            assertNotNull(createFileResponse.getData().getKind());
            assertNotNull(createFileResponse.getData().getPath());
            assertNotNull(createFileResponse.getData().getName());
            assertNotNull(createFileResponse.getData().getSize());
            assertNotNull(createFileResponse.getData().getFileContentMd5());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update a folder name at the specified path")
    class TestUpdateFolder {
        FileStagingJobResponse updateFolderResponse = null;

        @BeforeAll
        public void setup() throws InterruptedException {
//			Delete folder if it exists
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .deleteFolderOrFile(TEST_FOLDER_FSS_NAME);

            if (response.isSuccessful()) {
                assertNotNull(response.getData().getJobId());
                assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getData().getJobId()));
            }

//			Create a new folder
            FileStagingItemResponse createFolderResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .createFolderOrFile(Kind.FOLDER, TEST_FOLDER_FSS_NAME);

            assertNotNull(createFolderResponse);
            assertTrue(createFolderResponse.isSuccessful());
        }

        @AfterAll
        public void teardown() {
//			Delete updated folder
            FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .deleteFolderOrFile(TEST_UPDATE_FOLDER_FSS_NAME);

            assertTrue(resp.isSuccessful());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setName(TEST_UPDATE_FOLDER_FSS_NAME)
                    .updateFolderOrFile(TEST_FOLDER_FSS_NAME);

            assertNotNull(response);
            updateFolderResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateFolderResponse.isSuccessful());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, updateFolderResponse.getData().getJobId()));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update a file name at the specified path")
    class TestUpdateFile {
        FileStagingJobResponse updateFileResponse = null;

        @BeforeAll
        public void setup() throws IOException {
//			Create a new file
            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setInputPath(FileHelper.getPathTestFile())
                    .createFolderOrFile(Kind.FILE, TEST_FILE_FSS_NAME);

            assertNotNull(createFileResponse);
            assertTrue(createFileResponse.isSuccessful());
        }

        @AfterAll
        public void teardown() {
            FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(TEST_UPDATE_FILE_FSS_NAME);

            assertTrue(resp.isSuccessful());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setName(TEST_UPDATE_FILE_FSS_NAME)
                    .updateFolderOrFile(TEST_FILE_FSS_NAME);

            assertNotNull(response);
            updateFileResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateFileResponse.isSuccessful());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, updateFileResponse.getData().getJobId()));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete a folder at the specified path")
    class TestDeleteFolder {
        FileStagingJobResponse deleteFolderResponse = null;

        @BeforeAll
        public void setup() throws InterruptedException {
//			Create a new folder
            FileStagingItemResponse createFolderResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .createFolderOrFile(Kind.FOLDER, TEST_FOLDER_FSS_NAME);

            assertNotNull(createFolderResponse);
            assertTrue(createFolderResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setRecursive(true)
                    .deleteFolderOrFile(TEST_FOLDER_FSS_NAME);

            assertNotNull(response);
            deleteFolderResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(deleteFolderResponse.isSuccessful());
            assertNotNull(deleteFolderResponse.getData());
            assertNotNull(deleteFolderResponse.getData().getJobId());
            assertNotNull(deleteFolderResponse.getData().getUrl());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, deleteFolderResponse.getData().getJobId()));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete a file at the specified path")
    class TestDeleteFile {
        FileStagingJobResponse deleteFileResponse = null;

        @BeforeAll
        public void setup() throws IOException {
//			Create a new file
            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setInputPath(FileHelper.getPathTestFile())
                    .createFolderOrFile(Kind.FILE, TEST_FILE_FSS_NAME);

            assertNotNull(createFileResponse);
            assertTrue(createFileResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(TEST_FILE_FSS_NAME);

            assertNotNull(response);
            deleteFileResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(deleteFileResponse.isSuccessful());
            assertNotNull(deleteFileResponse.getData());
            assertNotNull(deleteFileResponse.getData().getJobId());
            assertNotNull(deleteFileResponse.getData().getUrl());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, deleteFileResponse.getData().getJobId()));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download bytes from a file at the specified path")
    class TestDownloadItemContentBinary {
        VaultResponse downloadResp = null;

        @BeforeAll
        public void setup() throws IOException {
            File testFile = new File(FileHelper.getPathTestFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setFile(testFile.getPath(), bytes)
                    .createFolderOrFile(Kind.FILE, TEST_FILE_FSS_NAME);

            assertNotNull(createFileResponse);
        }

        @AfterAll
        public void teardown() {
            FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(TEST_FILE_FSS_NAME);

            Assertions.assertTrue(resp.isSuccessful());
            Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            downloadResp = vaultClient.newRequest(FileStagingRequest.class)
                    .downloadItemContent(TEST_FILE_FSS_NAME);

            assertNotNull(downloadResp);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(downloadResp.isSuccessful());
            assertNotNull(downloadResp.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download a file at the specified path to a local path")
    class TestDownloadItemContentToFile {
        VaultResponse downloadResp = null;

        @BeforeAll
        public void setup() throws IOException {
            File testFile = new File(FileHelper.getPathTestFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            FileStagingItemResponse createFileResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .setFile(testFile.getPath(), bytes)
                    .createFolderOrFile(Kind.FILE, TEST_FILE_FSS_NAME);

            assertNotNull(createFileResponse);
        }

        @AfterAll
        public void teardown() {
            FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(TEST_FILE_FSS_NAME);

            Assertions.assertTrue(resp.isSuccessful());
            Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            downloadResp = vaultClient.newRequest(FileStagingRequest.class)
                    .setOutputPath(RESOURCES_FOLDER_PATH + TEST_DOWNLOAD_FILE_NAME)
                    .downloadItemContent(TEST_FILE_FSS_NAME);

            assertNotNull(downloadResp);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(downloadResp.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a resumable upload session")
    class TestCreateResumableUploadSession {
        FileStagingSessionResponse createResumableUploadSessionResponse = null;

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(createResumableUploadSessionResponse.getData().getId());

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            String filePath = FileHelper.getPathResumableUploadFile();
            long fileSize = new File(filePath).length();
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(response);
            createResumableUploadSessionResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createResumableUploadSessionResponse.isSuccessful());
            assertNotNull(createResumableUploadSessionResponse.getData());
            assertNotNull(createResumableUploadSessionResponse.getData().getId());
            assertEquals(String.format("/%s", TEST_FILE_FSS_NAME), createResumableUploadSessionResponse.getData().getPath());
            assertEquals(TEST_FILE_FSS_NAME, createResumableUploadSessionResponse.getData().getName());
            assertNotNull(createResumableUploadSessionResponse.getData().getSize());
            assertNotNull(createResumableUploadSessionResponse.getData().getUploadedParts());
            assertNotNull(createResumableUploadSessionResponse.getData().getUploadedParts());
            assertNotNull(createResumableUploadSessionResponse.getData().getUploaded());
            assertNotNull(createResumableUploadSessionResponse.getData().getCreatedDate());
            assertNotNull(createResumableUploadSessionResponse.getData().getExpirationDate());
            assertNotNull(createResumableUploadSessionResponse.getData().getLastUploadedDate());
            assertNotNull(createResumableUploadSessionResponse.getData().getOverwrite());
            assertNotNull(createResumableUploadSessionResponse.getData().getOwner());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully abort an upload session")
    class TestAbortUploadSession {
        FileStagingSessionResponse createResumableUploadSessionResponse = null;
        VaultResponse abortUploadSessionResponse = null;

        @BeforeAll
        public void setup() {
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            createResumableUploadSessionResponse = response;
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(createResumableUploadSessionResponse.getData().getId());
            assertNotNull(response);
            abortUploadSessionResponse = response;

        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(abortUploadSessionResponse.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully list active resumable upload sessions")
    class TestListUploadSessions {
        FileStagingSessionBulkResponse listUploadSessionsResponse = null;
        String sessionId = null;

        @BeforeAll
        public void setup() {
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            sessionId = response.getData().getId();
        }

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(sessionId);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingSessionBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .listUploadSessions();
            assertNotNull(response);
            listUploadSessionsResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(listUploadSessionsResponse.isSuccessful());
            assertNotNull(listUploadSessionsResponse.getData());
            for (ResumableUploadSession session : listUploadSessionsResponse.getData()) {
                assertNotNull(session.getId());
                assertNotNull(session.getPath());
                assertNotNull(session.getName());
                assertNotNull(session.getSize());
                assertNotNull(session.getUploadedParts());
                assertNotNull(session.getUploaded());
                assertNotNull(session.getCreatedDate());
                assertNotNull(session.getExpirationDate());
                assertNotNull(session.getLastUploadedDate());
                assertNotNull(session.getOverwrite());
                assertNotNull(session.getOwner());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve details of an active upload session")
    class TestGetUploadSessionDetails {
        FileStagingSessionResponse getUploadSessionDetailsResponse = null;
        String sessionId = null;

        @BeforeAll
        public void setup() {
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getId());
            sessionId = response.getData().getId();
        }

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(sessionId);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .getUploadSessionDetails(sessionId);
            assertNotNull(response);
            getUploadSessionDetailsResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(getUploadSessionDetailsResponse.isSuccessful());
            assertNotNull(getUploadSessionDetailsResponse.getData());
            assertNotNull(getUploadSessionDetailsResponse.getData().getId());
            assertNotNull(getUploadSessionDetailsResponse.getData().getPath());
            assertNotNull(getUploadSessionDetailsResponse.getData().getName());
            assertNotNull(getUploadSessionDetailsResponse.getData().getSize());
            assertNotNull(getUploadSessionDetailsResponse.getData().getUploadedParts());
            assertNotNull(getUploadSessionDetailsResponse.getData().getUploaded());
            assertNotNull(getUploadSessionDetailsResponse.getData().getCreatedDate());
            assertNotNull(getUploadSessionDetailsResponse.getData().getExpirationDate());
            assertNotNull(getUploadSessionDetailsResponse.getData().getLastUploadedDate());
            assertNotNull(getUploadSessionDetailsResponse.getData().getOverwrite());
            assertNotNull(getUploadSessionDetailsResponse.getData().getOwner());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully upload a file to an active upload session")
    class TestUploadToASession {
        FileStagingSessionPartResponse uploadToASessionResponse = null;
        String sessionId = null;

        @BeforeAll
        public void setup() {
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getId());
            sessionId = response.getData().getId();
        }

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(sessionId);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException, InterruptedException {
            File testFile = new File(FileHelper.getPathResumableUploadFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            FileStagingSessionPartResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setFile(testFile.getName(), bytes)
                    .uploadToASession(sessionId, 1);

            assertNotNull(response);
            uploadToASessionResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(uploadToASessionResponse.isSuccessful());
            assertNotNull(uploadToASessionResponse.getData());
            assertNotNull(uploadToASessionResponse.getData().getSize());
            assertNotNull(uploadToASessionResponse.getData().getPartNumber());
            assertNotNull(uploadToASessionResponse.getData().getPartContentMd5());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully list file parts uploaded to an active upload session")
    public class TestListFilePartsUploadedToASession {
        FileStagingSessionPartBulkResponse listFilePartsUploadedToASessionResponse = null;
        String sessionId = null;

        @BeforeAll
        public void setup() throws IOException {
//            Create resumable upload session
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse sessionResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(sessionResponse);
            assertTrue(sessionResponse.isSuccessful());
            assertNotNull(sessionResponse.getData().getId());
            sessionId = sessionResponse.getData().getId();

//            Upload file to session
            File testFile = new File(FileHelper.getPathResumableUploadFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            FileStagingSessionPartResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setFile(testFile.getName(), bytes)
                    .uploadToASession(sessionId, 1);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .abortUploadSession(sessionId);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException, InterruptedException {
            FileStagingSessionPartBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setLimit(1)
                    .listFilePartsUploadedToASession(sessionId);

            assertNotNull(response);
            listFilePartsUploadedToASessionResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(listFilePartsUploadedToASessionResponse.isSuccessful());
            assertNotNull(listFilePartsUploadedToASessionResponse.getData());
            for (ResumableUploadSessionPart uploadedPart : listFilePartsUploadedToASessionResponse.getData()) {
                assertNotNull(uploadedPart.getPartNumber());
                assertNotNull(uploadedPart.getPartContentMd5());
                assertNotNull(uploadedPart.getSize());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully commit an active upload session")
    public class TestCommitUploadSession {
        FileStagingJobResponse commitUploadSessionResponse = null;
        String sessionId = null;

        @BeforeAll
        public void setup() throws IOException {
//            Create resumable upload session
            String filePath = FileHelper.getPathResumableUploadFile();
            int fileSize = (int) new File(filePath).length();
            FileStagingSessionResponse sessionResponse = vaultClient.newRequest(FileStagingRequest.class)
                    .setOverwrite(true)
                    .createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
            assertNotNull(sessionResponse);
            assertTrue(sessionResponse.isSuccessful());
            assertNotNull(sessionResponse.getData().getId());
            sessionId = sessionResponse.getData().getId();

//            Upload file to session
            File testFile = new File(FileHelper.getPathResumableUploadFile());
            byte[] bytes = Files.readAllBytes(testFile.toPath());

            FileStagingSessionPartResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .setFile(testFile.getName(), bytes)
                    .uploadToASession(sessionId, 1);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
        }

        @AfterAll
        public void teardown() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .deleteFolderOrFile(TEST_FILE_FSS_NAME);

            assertNotNull(response);
            assertTrue(response.isSuccessful());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getData().getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() {
            FileStagingJobResponse response = vaultClient.newRequest(FileStagingRequest.class)
                    .commitUploadSession(sessionId);

            assertNotNull(response);
            commitUploadSessionResponse = response;
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(commitUploadSessionResponse.isSuccessful());
            assertNotNull(commitUploadSessionResponse.getData());
            assertNotNull(commitUploadSessionResponse.getData().getJobId());
            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, commitUploadSessionResponse.getData().getJobId()));
        }
    }
}
