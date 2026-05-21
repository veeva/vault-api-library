/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.DocumentRenditionRequestHelper;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("DocumentRenditionRequest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Document rendition request should")
public class DocumentRenditionRequestTest {
    private static final int DOC_ID = 3;
    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 1;
    private static final String RENDITION_TYPE = "viewable_rendition__v";
    private static final String PATH_ADD_MULTIPLE_DOCUMENT_RENDITIONS_CSV = DocumentRenditionRequestHelper.PATH_ADD_MULTIPLE_RENDITIONS_CSV;
    private static final String PATH_UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV = DocumentRenditionRequestHelper.PATH_UPDATE_MULTIPLE_RENDITIONS_CSV;
    private static VaultClient vaultClient = null;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test // Test Manually
    public void testAddMultipleDocumentRenditions(VaultClient vaultClient) {
        String inputPath = "";
        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .setMigrationMode(true)
                .addMultipleDocumentRenditions();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }


    @Test // Test Manually
    public void testDeleteMultipleDocumentRenditions(VaultClient vaultClient) {
        String inputPath = "";
        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .deleteMultipleDocumentRenditions();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());

    }

    @Test // Test Manually
    public void testAddSingleDocumentRendition(VaultClient vaultClient) {
        String inputPath = "";

        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .addSingleDocumentRendition(DOC_ID, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test // Test Manually
    public void testUploadDocumentVersionRendition(VaultClient vaultClient) {
        String inputPath = "";

        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .uploadDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test // Test Manually
    public void testReplaceDocumentRendition(VaultClient vaultClient) {
        String inputPath = "";

        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .replaceDocumentRendition(DOC_ID, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test // Test Manually
    public void testReplaceDocumentVersionRendition(VaultClient vaultClient) {
        String inputPath = "";

        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
                .setInputPath(inputPath)
                .replaceDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testDeleteSingleDocumentRendition(VaultClient vaultClient) {
        VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class).deleteSingleDocumentRendition(DOC_ID, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testDeleteDocumentVersionRendition(VaultClient vaultClient) {
        VaultResponse response = vaultClient
                .newRequest(DocumentRenditionRequest.class)
                .deleteDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document renditions")
    class TestRetrieveDocumentRenditions {
        DocumentRenditionResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .retrieveDocumentRenditions(docId);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<String> renditionTypes = response.getRenditionTypes();
            assertNotNull(renditionTypes);
            for (String renditionType : renditionTypes) {
                assertNotNull(renditionType);
            }
            DocumentRenditionResponse.Renditions renditions = response.getRenditions();
            assertNotNull(response.getRenditions());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document version renditions")
    class TestRetrieveDocumentVersionRenditions {
        DocumentRenditionResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .retrieveDocumentVersionRenditions(docId, MAJOR_VERSION, MINOR_VERSION);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<String> renditionTypes = response.getRenditionTypes();
            assertNotNull(renditionTypes);
            for (String renditionType : renditionTypes) {
                assertNotNull(renditionType);
            }
            DocumentRenditionResponse.Renditions renditions = response.getRenditions();
            assertNotNull(response.getRenditions());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download document rendition file")
    class TestDownloadDocumentRenditionFileToBytes {
        VaultResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .downloadDocumentRenditionFile(docId, RENDITION_TYPE);

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
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download document version rendition file")
    class TestDownloadDocumentVersionRenditionFileToBytes {
        VaultResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .downloadDocumentVersionRenditionFile(docId, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);

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
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add multiple document renditions from csv")
    class TestAddMultipleDocumentRenditionsCsv {

        private DocumentRenditionBulkResponse addMultipleDocumentRenditionsResponse = null;
        private int docId = 0;

        @BeforeAll
        public void setup() throws IOException, InterruptedException {
//            Create a document to use for the test
            DocumentBulkResponse createDocsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            assertTrue(createDocsResponse.isSuccessful());
            docId = createDocsResponse.getData().get(0).getDocument().getId();

            Thread.sleep(10000);

//            Delete the viewable rendition
            VaultResponse deleteResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .deleteSingleDocumentRendition(docId, "viewable_rendition__v");
            assertTrue(deleteResponse.isSuccessful());

//            Write the CSV file with the document ID
            DocumentRenditionRequestHelper.writeToAddMultipleRenditionsFile(vaultClient, docId);
        }

        @AfterAll
        public void teardown() {
            List<Integer> docIds = new ArrayList<>();
            docIds.add(docId);
            DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            Assertions.assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            addMultipleDocumentRenditionsResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
					.setInputPath(PATH_ADD_MULTIPLE_DOCUMENT_RENDITIONS_CSV)
                    .setMigrationMode(true)
                    .addMultipleDocumentRenditions();

            assertNotNull(addMultipleDocumentRenditionsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(addMultipleDocumentRenditionsResponse.isSuccessful());
            assertNotNull(addMultipleDocumentRenditionsResponse.getData());
            for (DocumentRenditionBulkResponse.Rendition rendition : addMultipleDocumentRenditionsResponse.getData()) {
                assertTrue(rendition.getResponseStatus().equals("SUCCESS"));
                assertNotNull(rendition.getId());
                assertNotNull(rendition.getMajorVersionNumber());
                assertNotNull(rendition.getMinorVersionNumber());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update multiple document renditions from a csv file")
    class TestUpdateMultipleDocumentRenditionsCsv {

        private DocumentRenditionBulkResponse updateMultipleDocumentRenditionsResponse = null;
        private int docId = 0;
        private int majorVersion = 0;
        private int minorVersion = 1;

        @BeforeAll
        public void setup() throws IOException {
            DocumentsResponse retrieveResponse = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            assertTrue(retrieveResponse.isSuccessful());
            for (DocumentsResponse.DocumentNode documentNode : retrieveResponse.getDocuments()) {
                if (documentNode.getDocument().getName().contains("VAPIL Test Re-render (Do Not Delete)")) {
                    docId = documentNode.getDocument().getId();
                    majorVersion = documentNode.getDocument().getMajorVersionNumber();
                    minorVersion = documentNode.getDocument().getMinorVersionNumber();
                    break;
                }
            }

            VaultResponse deleteResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .deleteSingleDocumentRendition(docId, "viewable_rendition__v");

            assertTrue(deleteResponse.isSuccessful());

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"id", "major_version_number__v", "minor_version_number__v"});
            data.add(new String[]{String.valueOf(docId), String.valueOf(majorVersion), String.valueOf(minorVersion)});
            FileHelper.writeCsvFile(PATH_UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV, data);
        }

        @Test
        @Order(1)
        public void testRequest() {
            updateMultipleDocumentRenditionsResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .setInputPath(PATH_UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV)
                    .updateMultipleDocumentRenditions();

            assertNotNull(updateMultipleDocumentRenditionsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateMultipleDocumentRenditionsResponse.isSuccessful());
            assertNotNull(updateMultipleDocumentRenditionsResponse.getData());
            for (DocumentRenditionBulkResponse.Rendition rendition : updateMultipleDocumentRenditionsResponse.getData()) {
                assertTrue(rendition.getResponseStatus().equals("SUCCESS"));
                assertNotNull(rendition.getId());
                assertNotNull(rendition.getMajorVersionNumber());
                assertNotNull(rendition.getMinorVersionNumber());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update multiple document renditions from bytes")
    class TestUpdateMultipleDocumentRenditionsBytes {

        private DocumentRenditionBulkResponse updateMultipleDocumentRenditionsResponse = null;
        private int docId = 0;
        private int majorVersion = 0;
        private int minorVersion = 1;

        @BeforeAll
        public void setup() throws IOException {
            DocumentsResponse retrieveResponse = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            assertTrue(retrieveResponse.isSuccessful());
            for (DocumentsResponse.DocumentNode documentNode : retrieveResponse.getDocuments()) {
                if (documentNode.getDocument().getName().contains("VAPIL Test Re-render (Do Not Delete)")) {
                    docId = documentNode.getDocument().getId();
                    majorVersion = documentNode.getDocument().getMajorVersionNumber();
                    minorVersion = documentNode.getDocument().getMinorVersionNumber();
                    break;
                }
            }

            VaultResponse deleteResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
                    .deleteSingleDocumentRendition(docId, "viewable_rendition__v");

            assertTrue(deleteResponse.isSuccessful());

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"id", "major_version_number__v", "minor_version_number__v"});
            data.add(new String[]{String.valueOf(docId), String.valueOf(majorVersion), String.valueOf(minorVersion)});
            FileHelper.writeCsvFile(PATH_UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV, data);
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File csvFile = new File(PATH_UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV);

            updateMultipleDocumentRenditionsResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
//					.setInputPath(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH)
                    .setBinaryFile(csvFile.getName(), Files.readAllBytes(csvFile.toPath()))
                    .updateMultipleDocumentRenditions();

            assertNotNull(updateMultipleDocumentRenditionsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateMultipleDocumentRenditionsResponse.isSuccessful());
            assertNotNull(updateMultipleDocumentRenditionsResponse.getData());
            for (DocumentRenditionBulkResponse.Rendition rendition : updateMultipleDocumentRenditionsResponse.getData()) {
                assertTrue(rendition.getResponseStatus().equals("SUCCESS"));
                assertNotNull(rendition.getId());
                assertNotNull(rendition.getMajorVersionNumber());
                assertNotNull(rendition.getMinorVersionNumber());
            }
        }
    }
}

