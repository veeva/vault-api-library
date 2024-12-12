package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ObjectRecordAttachmentFieldRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Object Record Attachment Field Request should")
public class ObjectRecordAttachmentFieldRequestTest {

    static final String OBJECT_NAME = "vapil_test_object__c";
    static final String ATTACHMENT_FIELD_NAME = "vapil_attachment_field__c";
    static final String TEST_DOWNLOAD_FILE_NAME = "test_download_attachment_field_file.txt";
    static final String TEST_DOWNLOAD_FILES_NAME = "test_download_attachment_field_files.zip";
    private static VaultClient vaultClient;
    static String recordId;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

//        Find record with populated attachment field
        QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                .query(String.format("SELECT id FROM %s WHERE (%s != null)", OBJECT_NAME, ATTACHMENT_FIELD_NAME));

        recordId = queryResponse.getData().get(0).get("id").toString();
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download an attachment field file binary content")
    class TestDownloadAttachmentFieldFileToBinary {

        private VaultResponse response = null;

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

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setOutputPath(FileHelper.getPathResourcesFolder() + TEST_DOWNLOAD_FILE_NAME)
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

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setOutputPath(FileHelper.getPathResourcesFolder() + TEST_DOWNLOAD_FILES_NAME)
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
    @DisplayName("successfully update an attachment field file on a record from input path")
    class TestUpdateAttachmentFieldFileFromInputPath {

        private ObjectRecordBulkResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentFieldRequest.class)
                    .setInputPath(FileHelper.getPathResourcesFolder() + TEST_DOWNLOAD_FILE_NAME)
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

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File file = new File(FileHelper.getPathResourcesFolder() + TEST_DOWNLOAD_FILE_NAME);
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
