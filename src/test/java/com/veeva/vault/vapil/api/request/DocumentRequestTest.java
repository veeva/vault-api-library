/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.FileStagingHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document request should")
public class DocumentRequestTest {
    static final int DOC_ID = 1;
    static final int MAJOR_VERSION = 0;
    static final int MINOR_VERSION = 1;
    static final String DOC_TYPE_NAME = "vapil_test_doc_type__c";
    static final String DOC_SUBTYPE_NAME = "vapil_test_doc_subtype__c";
    static final String DOC_CLASSIFICATION_NAME = "vapil_test_doc_classification__c";
    static final String DOC_RECLASSIFY_TYPE_NAME = "vapil_test_reclassify_type__c";
    static final String DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    static final String DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    static final String DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    static final String DOC_TEMPLATE = "vapil_test_doc_template__c";
    static final String DOC_LIFECYCLE_LABEL = "VAPIL Test Doc Lifecycle";
    static final String DOC_LIFECYCLE_NAME = "vapil_test_doc_lifecycle__c";
    static final String TEST_FILE_PATH = FileHelper.getPathTestFile();
    static final String CREATE_DOCUMENTS_CSV_PATH = DocumentRequestHelper.getPathCreateMultipleDocuments();
    static final String UPDATE_DOCUMENTS_CSV_PATH = DocumentRequestHelper.getPathUpdateMultipleDocuments();
    static final String DELETE_DOCUMENTS_CSV_PATH = DocumentRequestHelper.getPathDeleteMultipleDocuments();
    static final String RECLASSIFY_DOCUMENTS_CSV_PATH = DocumentRequestHelper.getPathReclassifyMultipleDocuments();
    static final String FILE_STAGING_FILE = FileStagingHelper.getPathFileStagingTestFilePath();
    static List<Integer> docIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
        FileStagingHelper.createTestFileOnFileStaging(vaultClient);
    }

    @Test
    @Order(1)
    @DisplayName("successfully create a single document")
    public void testCreateSingleDocument() {
        Document doc = new Document();

        doc.setName("VAPIL test create single document " + ZonedDateTime.now());
        doc.setLifecycle(DOC_LIFECYCLE_LABEL);
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);
        doc.setTitle("VAPIL test create single document");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(TEST_FILE_PATH)
                .createSingleDocument(doc);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());

        docIds.add(response.getDocument().getId());
    }

    @Test
    @Order(2)
    @DisplayName("successfully create a document from a template")
    public void testCreateSingleDocumentFromTemplate() {
        Document doc = new Document();

        doc.setName("VAPIL Test Doc From Template " + ZonedDateTime.now());
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);
        doc.setLifecycle(DOC_LIFECYCLE_LABEL);

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .createDocumentFromTemplate(doc, DOC_TEMPLATE);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());

        docIds.add(response.getDocument().getId());
    }

    @Test
    @Order(3)
    @DisplayName("successfully create multiple documents from CSV and file on staging server")
    public void testCreateMultipleDocumentsFile() throws IOException {
//		Write Headers and data to CSV file
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
        for (int i = 0; i < 3; i++) {
            String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
            data.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                    DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
        }
        FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, data);

//		Create multiple documents
        DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                .createMultipleDocuments();
        Assertions.assertTrue(createResponse.isSuccessful());
        for (DocumentResponse documentResponse : createResponse.getData()) {
            Assertions.assertTrue(documentResponse.isSuccessful());
            docIds.add(documentResponse.getDocument().getId());
        }
    }

    @Test
    @Order(4)
    @DisplayName("successfully update a single document")
    public void testUpdateSingleDocument() {
//		Update Document
        int id = docIds.get(0);
        String updatedName = "VAPIL Test Update Single Document";
        Document doc = new Document();

        doc.setName(updatedName);
        doc.setLifecycle(DOC_LIFECYCLE_LABEL);
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);
        doc.setId(id);

        DocumentResponse updateDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
                .updateSingleDocument(doc);
        Assertions.assertTrue(updateDocumentResponse.isSuccessful());

//		Retrieve Document and verify update
        DocumentResponse retrieveDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocument(id);
        Assertions.assertTrue(retrieveDocumentResponse.isSuccessful());
        Assertions.assertEquals(updatedName, retrieveDocumentResponse.getDocument().getName());
    }

    @Test
    @Order(5)
    @DisplayName("successfully update multiple documents from a CSV")
    public void testUpdateMultipleDocuments() throws IOException {
        String updatedTitle = "VAPIL Test Update multiple documents";

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id", "title__v"});

        for (int docId : docIds) {
            data.add(new String[]{String.valueOf(docId), updatedTitle});
        }

        FileHelper.writeCsvFile(UPDATE_DOCUMENTS_CSV_PATH, data);

//		Update multiple documents
        DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(UPDATE_DOCUMENTS_CSV_PATH)
                .updateMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());

//		Retrieve documents and verify updates
        for (DocumentResponse documentResponse : response.getData()) {
            DocumentResponse retrieveDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocument(documentResponse.getDocument().getId());
            Assertions.assertEquals(updatedTitle, retrieveDocumentResponse.getDocument().getTitle());
        }
    }

    @Test
    @Order(5)
    @DisplayName("successfully reclassify multiple documents from a CSV")
    public void testReclassifyMultipleDocuments() throws IOException {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id", "lifecycle__v", "type__v"});

        for (int docId : docIds) {
            data.add(new String[]{String.valueOf(docId), DOC_LIFECYCLE_NAME, DOC_RECLASSIFY_TYPE_NAME});
        }

        FileHelper.writeCsvFile(RECLASSIFY_DOCUMENTS_CSV_PATH, data);

//		Reclassify multiple documents
        DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(RECLASSIFY_DOCUMENTS_CSV_PATH)
                .reclassifyMultipleDocuments();

        Assertions.assertTrue(response.isSuccessful());

        for (DocumentResponse documentResponse : response.getData()) {
            Assertions.assertTrue(documentResponse.isSuccessful());
            Assertions.assertNotNull(documentResponse.getDocument().getId());
        }
    }

    @Test
    @Order(6)
    @DisplayName("successfully retrieve document by ID")
    public void testRetrieveDocument() {
        DocumentResponse documentResponse = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocument(docIds.get(0));

        Assertions.assertTrue(documentResponse.isSuccessful());
        Assertions.assertNotNull(documentResponse.getDocument());
        Assertions.assertNotNull(documentResponse.getRenditions());
        Assertions.assertNotNull(documentResponse.getVersions());
    }

    @Test
    @Order(7)
    @DisplayName("successfully retrieve document versions by ID")
    public void testRetrieveDocumentVersions() {
        DocumentVersionResponse documentVersionResponse = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentVersions(docIds.get(0));

        Assertions.assertTrue(documentVersionResponse.isSuccessful());
        Assertions.assertNotNull(documentVersionResponse.getVersions());
    }

    @Test
    @Order(8)
    @DisplayName("successfully retrieve document by ID, minor version, and major version")
    public void testRetrieveDocumentVersion() {
        int id = docIds.get(0);
        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentVersion(id, MAJOR_VERSION, MINOR_VERSION);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
        Assertions.assertNotNull(response.getRenditions());
        Assertions.assertNotNull(response.getVersions());
    }

    @Test
    @Order(9)
    @DisplayName("successfully retrieve all documents")
    public void testRetrieveAllDocuments() {
        DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocuments());
    }

    @Test
    @Order(10)
    @DisplayName("successfully retrieve all document fields")
    public void testRetrieveAllDocumentFields() {
        DocumentFieldResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentFields();
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(11)
    @DisplayName("successfully retrieve all document types")
    public void testRetrieveAllDocumentTypes() {
        DocumentTypesResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentTypes();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getTypes());
    }

    @Test
    @Order(12)
    @DisplayName("successfully retrieve doc type")
    public void testRetrieveDocumentType() {
        DocumentTypeResponse response = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentType(DOC_TYPE_NAME);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getLabel());
    }

    @Test
    @Order(13)
    @DisplayName("successfully retrieve document subtype")
    public void testRetrieveDocumentSubtype() {
        DocumentSubtypeResponse response = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentSubtype(DOC_TYPE_NAME, DOC_SUBTYPE_NAME);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getLabel());
    }

    @Test
    @Order(14)
    @DisplayName("successfully retrieve document classification")
    public void testRetrieveDocumentClassification() {
        DocumentClassificationResponse response = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentClassification(
                        DOC_TYPE_NAME,
                        DOC_SUBTYPE_NAME,
                        DOC_CLASSIFICATION_NAME);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getLabel());
        System.out.println(response.getLabel());
    }

    @Test
    @Order(15)
    @DisplayName("successfully delete a single document by ID")
    public void testDeleteSingleDocument() {
        int id = docIds.get(docIds.size() - 1);
        DocumentResponse deleteDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
                .deleteSingleDocument(id);

        docIds.remove(docIds.size() - 1);

        Assertions.assertTrue(deleteDocumentResponse.isSuccessful());
    }

    @Test
    @Order(16)
    @DisplayName("successfully delete multiple documents from a CSV")
    public void testDeleteMultipleDocuments() throws IOException {
        FileHelper.createCsvFile(DELETE_DOCUMENTS_CSV_PATH);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id"});

        for (int docId : docIds) {
            data.add(new String[]{String.valueOf(docId)});
        }

        FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, data);

//		Delete multiple documents
        DocumentBulkResponse deleteDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                .deleteMultipleDocuments();

        Assertions.assertTrue(deleteDocumentsResponse.isSuccessful());
        for (DocumentResponse documentResponse : deleteDocumentsResponse.getData()) {
            Assertions.assertTrue(documentResponse.isSuccessful());
        }

    }

    @Test
    @Order(17)
    @DisplayName("successfully retrieve deleted document IDs")
    public void testGetDocumentDeletions() {
        DocumentDeletionResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setLimit(1)
                .retrieveDeletedDocumentIds();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails().getSize());

        if (response.isPaginated()) {
            DocumentDeletionResponse paginatedResponse = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDeletedDocumentIdsByPage(response.getResponseDetails().getNextPage());
            Assertions.assertTrue(paginatedResponse.isSuccessful());
            Assertions.assertNotNull(paginatedResponse.getResponseDetails().getSize());
        }
    }

    // Test Manually
    @Disabled
    @Test
    public void testExportDocuments() {
        String filePath = "";
        JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(filePath)
                .exportDocuments(true, true, true);
        Assertions.assertTrue(response.isSuccessful());
    }

    // Test Manually
    @Disabled
    @Test
    public void testExportDocumentVersions() {
        String filePath = "";

        JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(filePath)
                .exportDocumentVersions(true, true);
        Assertions.assertTrue(response.isSuccessful());
    }

    // Test Manually
    @Disabled
    @Test
    public void testRetrieveExportResults() {
        int jobId = 0;
        DocumentExportResponse response = vaultClient.newRequest(DocumentRequest.class)
                .retrieveDocumentExportResults(jobId);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Disabled
    @Test
    public void testDownloadDocumentFile() {
        VaultResponse response = vaultClient.newRequest(DocumentRequest.class).downloadDocumentFile(DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Disabled
    @Test
    public void testDownloadDocumentVersionFile() {
        VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
                .downloadDocumentVersionFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Disabled
    @Test
    public void testDownloadDocumentVersionThumbnailFile() {
        VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
                .downloadDocumentVersionThumbnailFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Disabled
    @Test
    public void testCreateSingleDocumentVersionLatestContent() {
        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .createSingleDocumentVersion(DOC_ID, DocumentRequest.CreateDraftType.LATESTCONTENT);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    // Test Manually
    @Disabled
    @Test
    public void testCreateSingleDocumentVersionUploadedContent() {
        String filePath = "";

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(filePath)
                .createSingleDocumentVersion(DOC_ID, DocumentRequest.CreateDraftType.UPLOADEDCONTENT);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    @Disabled
    @Test
    public void testRetrieveCommonDocumentFields() {
        Set<Integer> docIds = new HashSet<>();
        docIds.add(5);
        docIds.add(12);

        DocumentFieldResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveCommonDocumentFields(docIds);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Disabled
    @Test
    public void testUpdateDocumentVersion() {
        Document doc = new Document();
        doc.setId(DOC_ID);
        doc.setTitle("VAPIL - Updated");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .updateDocumentVersion(doc, MAJOR_VERSION, MINOR_VERSION);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getVersions());
    }

    // Run Manually
    @Disabled
    @Test
    public void testCreateContentPlaceholderDocument() {
        String filePath = "";
        Document doc = new Document();

        doc.setName("VAPIL Placeholder");
        doc.setLifecycle("General Lifecycle");
        doc.setType("General");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(filePath)
                .createContentPlaceholderDocument(doc);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    // Run Manually
    @Disabled
    @Test
    public void testCreateCrossLinkDocument() {
        String filePath = "";
        int vaultId = 0; // Set Correct Values
        int crossLinkDocId = 0; // Set correct value

        Document doc = new Document();
        doc = new Document();
        doc.setName("VAPIL CrossLink");
        doc.setLifecycle("General Lifecycle");
        doc.setType("General");
        doc.setTitle("VAPIL CrossLink - default values");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .createCrossLinkDocument(doc, vaultId, crossLinkDocId);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    // Run Manually
    @Disabled
    @Test
    public void testCreateCrossLinkDocumentLatestVersion() {
        String filePath = "";
        int vaultId = 0; // Set Correct Values
        int crossLinkDocId = 0; // Set correct value

        Document doc = new Document();
        doc = new Document();
        doc.setName("VAPIL CrossLink");
        doc.setLifecycle("General Lifecycle");
        doc.setType("General");
        doc.setTitle("VAPIL CrossLink - latest version");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setSourceBindingRule(DocumentRequest.CrosslinkBinding.LATEST)
                .createCrossLinkDocument(doc, vaultId, crossLinkDocId);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }


    // Run Manually
    @Disabled
    @Test
    public void testCreateCrossLinkDocumentSpecificVersion() {
        String filePath = "";
        int vaultId = 0; // Set Correct Values
        int crossLinkDocId = 0; // Set correct value

        Document doc = new Document();
        doc = new Document();
        doc.setName("VAPIL CrossLink");
        doc.setLifecycle("General Lifecycle");
        doc.setType("General");
        doc.setTitle("VAPIL CrossLink - latest version");

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setSourceBindingRule(DocumentRequest.CrosslinkBinding.SPECIFIC)
                .setBoundSourceMajorVersion(1)
                .setBoundSourceMinorVersion(0)
                .createCrossLinkDocument(doc, vaultId, crossLinkDocId);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    @Disabled
    @Test
    public void testRetrieveDocumentLockMetadata() {
        MetaDataDocumentLockResponse response = vaultClient
                .newRequest(DocumentRequest.class)
                .retrieveDocumentLockMetadata();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getProperties());
    }

    @Disabled
    @Test
    public void testCreateDocumentLock() {
        VaultResponse response = vaultClient
                .newRequest(DocumentRequest.class)
                .createDocumentLock(DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Disabled
    @Test
    public void testRetrieveDocumentLock() {
        DocumentLockResponse response = vaultClient
                .newRequest(DocumentRequest.class)
                .retrieveDocumentLock(DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getLock());
    }

    @Disabled
    @Test
    public void testDeleteDocumentLock() {
        VaultResponse response = vaultClient
                .newRequest(DocumentRequest.class)
                .retrieveDocumentLock(DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
    }

    // Test Manually
    @Disabled
    @Test
    public void testCreateMultipleDocumentsBytes() throws IOException {
        String csvFilePath = "";
        File csv = new File(csvFilePath);

        DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
                .createMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
    }

    // Test Manually
    @Disabled
    @Test
    public void testCreateMultipleDocumentVersions() {
        String csvFilePath = "";
        File csv = new File(csvFilePath);

        DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(csvFilePath)
                .setMigrationMode(true)
                .setIdParam("id")
                .createMultipleDocumentVersions();
        Assertions.assertTrue(response.isSuccessful());
    }

    @Disabled
    @Test
    public void testDocumentToken() {
        List<Integer> docIds = new ArrayList<>();
        docIds.add(5);
        docIds.add(12);

        DocumentTokenResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setExpiryDateOffset(10)
                .setDownloadOption(DocumentRequest.DownloadOption.PDF)
                .setTokenGroup("example")
                .documentTokens(docIds);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Disabled
    @Test
    public void testReclassifyDocumentWithMigrationMode() {
        Document doc = new Document();

        doc.setName("VAPIL Single Document");
        doc.setLifecycle("General Lifecycle");
        doc.setType("General");
        doc.setTitle("Test Reclassify VAPIL");
        doc.setDocumentNumber("4");
        doc.setStatus("Approved");
        doc.setId(DOC_ID);

        DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                .setMigrationMode(true)
                .reclassifySingleDocument(doc);

        Assertions.assertTrue(response.isSuccessful());

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all documents")
    class TestRetrieveAllDocuments {

        private DocumentsResponse retrieveAllDocumentsResponse = null;

        @Test
        @Order(1)
        public void testRequest() {
            retrieveAllDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            assertNotNull(retrieveAllDocumentsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveAllDocumentsResponse.isSuccessful());
            assertNotNull(retrieveAllDocumentsResponse.getSize());
            assertNotNull(retrieveAllDocumentsResponse.getStart());
            assertNotNull(retrieveAllDocumentsResponse.getLimit());

            List<DocumentsResponse.DocumentNode> documents = retrieveAllDocumentsResponse.getDocuments();
            assertNotNull(retrieveAllDocumentsResponse.getDocuments());
            for (DocumentsResponse.DocumentNode documentNode : documents) {
                Document document = documentNode.getDocument();
                assertNotNull(document);
                assertNotNull(document.getName());
                assertNotNull(document.getId());
                assertNotNull(document.getMajorVersionNumber());
                assertNotNull(document.getMinorVersionNumber());
                assertNotNull(document.getSize());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("have no errors and return isSuccessful() = true when response type is WARNING")
    class TestUpdateMultipleDocumentsWarning {

        DocumentBulkResponse updateMultipleDocumentsResponse = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create objects
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient);
            assertTrue(createResponse.isSuccessful());

            for (DocumentResponse documentResponse : createResponse.getData()) {
                assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

            List<String[]> updateData = new ArrayList<>();
            updateData.add(new String[]{"id", "description__c"});

            for (int i = 0; i < docIds.size(); i++) {
                int id = docIds.get(i);
                String description = "VAPIL Test";
                updateData.add(new String[]{String.valueOf(id), description});
            }

            FileHelper.writeCsvFile(UPDATE_DOCUMENTS_CSV_PATH, updateData);
        }

        @AfterAll
        public void teardown() throws IOException {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            updateMultipleDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(UPDATE_DOCUMENTS_CSV_PATH)
                    .updateMultipleDocuments();

            assertNotNull(updateMultipleDocumentsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertFalse(updateMultipleDocumentsResponse.isSuccessful());
            assertTrue(updateMultipleDocumentsResponse.hasWarnings());
            assertFalse(updateMultipleDocumentsResponse.hasErrors());
            assertNull(updateMultipleDocumentsResponse.getErrors());
        }
    }
}
