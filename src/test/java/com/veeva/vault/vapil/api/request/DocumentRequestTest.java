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
import com.veeva.vault.vapil.api.model.metadata.DocumentField;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document request should")
public class DocumentRequestTest {
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
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all document fields")
    class TestRetrieveAllDocumentFields {
        DocumentFieldResponse response = null;
        String referenceIdHeader = "test-retrieve-all-document-fields";

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setHeaderReferenceId(referenceIdHeader)
                    .retrieveAllDocumentFields();



            Assertions.assertTrue(response.isSuccessful());

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertEquals(referenceIdHeader, response.getHeaderReferenceId());
            assertNotNull(response.getProperties());
            for (DocumentField field : response.getProperties()) {
                assertNotNull(field.getDisabled());
                assertNotNull(field.getEditable());
                assertNotNull(field.getFacetable());
                assertNotNull(field.getHidden());
                assertNotNull(field.getName());
                assertNotNull(field.getQueryable());
                assertNotNull(field.getRequired());
                assertNotNull(field.getSystemAttribute());
                assertNotNull(field.getType());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all document fields")
    @Disabled
    class TestRetrieveCommonDocumentFields {
        DocumentFieldResponse response = null;

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all document types")
    class TestRetrieveAllDocumentTypes {
        DocumentTypesResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentTypes();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getLock());
            assertNotNull(response.getTypes());
            for (DocumentTypesResponse.DocumentType type : response.getTypes()) {
                assertNotNull(type.getName());
                assertNotNull(type.getLabel());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve doc type")
    class TestRetrieveDocumentType {
        DocumentTypeResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentType(DOC_TYPE_NAME);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getName());
            assertNotNull(response.getLabel());
            assertNotNull(response.getProperties());
            assertNotNull(response.getRenditions());
            assertNotNull(response.getRelationshipTypes());
            assertNotNull(response.getAvailableLifecycles());

            assertNotNull(response.getSubtypes());
            for (DocumentTypeResponse.DocumentSubtype subtype : response.getSubtypes()) {
                assertNotNull(subtype.getLabel());
                assertNotNull(subtype.getName());
                assertNotNull(subtype.getValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document subtype")
    class TestRetrieveDocumentSubtype {
        DocumentSubtypeResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentSubtype(DOC_TYPE_NAME, DOC_SUBTYPE_NAME);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getName());
            assertNotNull(response.getLabel());
            assertNotNull(response.getProperties());
            assertNotNull(response.getRenditions());
            assertNotNull(response.getRelationshipTypes());
            assertNotNull(response.getAvailableLifecycles());

            assertNotNull(response.getClassifications());
            for (DocumentSubtypeResponse.DocumentClassification classification : response.getClassifications()) {
                assertNotNull(classification.getLabel());
                assertNotNull(classification.getName());
                assertNotNull(classification.getValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document classification")
    class TestRetrieveDocumentClassification {
        DocumentClassificationResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentClassification(
                            DOC_TYPE_NAME,
                            DOC_SUBTYPE_NAME,
                            DOC_CLASSIFICATION_NAME);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getName());
            assertNotNull(response.getLabel());
            assertNotNull(response.getProperties());
            assertNotNull(response.getRenditions());
            assertNotNull(response.getRelationshipTypes());
            assertNotNull(response.getAvailableLifecycles());
            assertNotNull(response.getTemplates());
            for (DocumentClassificationResponse.Template template : response.getTemplates()) {
                assertNotNull(template.getName());
                assertNotNull(template.getLabel());
                assertNotNull(template.getKind());
                assertNotNull(template.getDefinedIn());
                assertNotNull(template.getDefinedInType());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all documents")
    class TestRetrieveAllDocuments {
        DocumentsResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getSize());
            assertNotNull(response.getLimit());
            assertNotNull(response.getStart());
            assertNotNull(response.getDocuments());
            for (DocumentsResponse.DocumentNode documentNode : response.getDocuments()) {
                Document document = documentNode.getDocument();
                assertNotNull(document);
                assertNotNull(document.getId());
                assertNotNull(document.getDocumentNumber());
                assertNotNull(document.getName());
                assertNotNull(document.getLifecycle());
                assertNotNull(document.getStatus());
                assertNotNull(document.getType());
                assertNotNull(document.getCreatedBy());
                assertNotNull(document.getDocumentCreationDate());
                assertNotNull(document.getVersionCreatedBy());
                assertNotNull(document.getVersionCreationDate());
                assertNotNull(document.getVersionId());
                assertNotNull(document.getBinder());
                assertNotNull(document.getMajorVersionNumber());
                assertNotNull(document.getMinorVersionNumber());

            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document by ID")
    class TestRetrieveDocument {
        DocumentResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            docId = response.getDocuments().get(0).getDocument().getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocument(docId);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            Document document = response.getDocument();
            assertNotNull(document);
            assertNotNull(document.getId());
            assertNotNull(document.getDocumentNumber());
            assertNotNull(document.getName());
            assertNotNull(document.getLifecycle());
            assertNotNull(document.getStatus());
            assertNotNull(document.getType());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document versions by ID")
    class TestRetrieveDocumentVersions {
        DocumentVersionResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            docId = response.getDocuments().get(0).getDocument().getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentVersions(docId);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getRenditions());
            assertNotNull(response.getRenditions().getViewableRendition());
            assertNotNull(response.getVersions());
            for (DocumentVersionResponse.Version version : response.getVersions()) {
                assertNotNull(version.getNumber());
                assertNotNull(version.getValue());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve document by ID, minor version, and major version")
    class TestRetrieveDocumentVersion {
        DocumentResponse response = null;
        int docId;
        int minorVersion;
        int majorVersion;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            Document document = response.getDocuments().get(0).getDocument();
            docId = document.getId();
            minorVersion = document.getMinorVersionNumber();
            majorVersion = document.getMajorVersionNumber();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentVersion(docId, majorVersion, minorVersion);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            Document document = response.getDocument();
            assertNotNull(document);
            assertNotNull(document.getId());
            assertNotNull(document.getDocumentNumber());
            assertNotNull(document.getName());
            assertNotNull(document.getLifecycle());
            assertNotNull(document.getStatus());
            assertNotNull(document.getType());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download latest version of the source file from the document")
    class TestDownloadDocumentFile {
        VaultResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            docId = response.getDocuments().get(0).getDocument().getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .downloadDocumentFile(docId);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/octet-stream;charset=UTF-8", response.getHeaderContentType());
            assertNotNull(response.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download source file from specific document version")
    class TestDownloadDocumentVersionFile {
        VaultResponse response = null;
        int docId;
        int minorVersion;
        int majorVersion;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            Document document = response.getDocuments().get(0).getDocument();
            docId = document.getId();
            minorVersion = document.getMinorVersionNumber();
            majorVersion = document.getMajorVersionNumber();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .downloadDocumentVersionFile(docId, majorVersion, minorVersion);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/octet-stream;charset=UTF-8", response.getHeaderContentType());
            assertNotNull(response.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download thumbnail file from specific document version")
    @Disabled
    class TestDownloadDocumentVersionThumbnailFile {


        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document")
    class TestCreateSingleDocument {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @AfterAll
        public void teardown() {
            DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            Document doc = new Document();

            doc.setName("VAPIL test create single document " + ZonedDateTime.now());
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setTitle("VAPIL test create single document");

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(TEST_FILE_PATH)
                    .createSingleDocument(doc);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument());
            assertNotNull(response.getDocument().getId());
            docIds.add(response.getDocument().getId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document from a template")
    class TestCreateSingleDocumentFromTemplate {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @AfterAll
        public void teardown() {
            DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            Document doc = new Document();

            doc.setName("VAPIL Test Doc From Template " + ZonedDateTime.now());
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .createDocumentFromTemplate(doc, DOC_TEMPLATE);

            Assertions.assertTrue(response.isSuccessful());
            Assertions.assertNotNull(response.getDocument());

            docIds.add(response.getDocument().getId());
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument());
            assertNotNull(response.getDocument().getId());
            docIds.add(response.getDocument().getId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create placeholder document")
    @Disabled
    class TestCreateContentPlaceholderDocument {


        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create an unclassified document")
    @Disabled
    class TestCreateUnclassifiedDocument {


        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a crosslink document")
    @Disabled
    class TestCreateCrosslinkDocument {


        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create multiple documents from CSV and file on staging server")
    class TestCreateMultipleDocumentsFile {
        DocumentBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            FileStagingHelper.createTestFileOnFileStaging(vaultClient);

//            Write Headers and data to CSV file
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                    "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
            for (int i = 0; i < 3; i++) {
                String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
                data.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                        DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
            }
            FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, data);
        }

        @AfterAll
        public void teardown() {
            FileHelper.createCsvFile(DELETE_DOCUMENTS_CSV_PATH);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"id"});

            for (int docId : docIds) {
                data.add(new String[]{String.valueOf(docId)});
            }

            FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, data);

            DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                    .deleteMultipleDocuments();

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                    .createMultipleDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentResponse documentResponse : response.getData()) {
                assertTrue(documentResponse.isSuccessful());
                assertNotNull(documentResponse.getDocument());
                assertNotNull(documentResponse.getDocument().getId());
                docIds.add(documentResponse.getDocument().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update a single document")
    class TestUpdateSingleDocument {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
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

            assertTrue(response.isSuccessful());
            docIds.add(response.getDocument().getId());
        }

        @AfterAll
        public void teardown() {
            DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
//		      Update Document
            int id = docIds.get(0);
            String updatedName = "VAPIL Test Update Single Document";
            Document doc = new Document();

            doc.setName(updatedName);
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setId(id);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .updateSingleDocument(doc);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument());
            assertNotNull(response.getDocument().getId());
            docIds.add(response.getDocument().getId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update multiple documents from CSV")
    class TestUpdateMultipleDocumentsFile {
        DocumentBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            FileStagingHelper.createTestFileOnFileStaging(vaultClient);

//            Write Headers and data to CSV file
            List<String[]> createData = new ArrayList<>();
            createData.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                    "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
            for (int i = 0; i < 3; i++) {
                String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
                createData.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                        DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
            }
            FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, createData);

//		      Create multiple documents
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                    .createMultipleDocuments();
            Assertions.assertTrue(response.isSuccessful());
            for (DocumentResponse documentResponse : response.getData()) {
                Assertions.assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

//            Write Headers and data to CSV file
            String updatedTitle = "VAPIL Test Update multiple documents";

            List<String[]> updateData = new ArrayList<>();
            updateData.add(new String[]{"id", "title__v"});

            for (int docId : docIds) {
                updateData.add(new String[]{String.valueOf(docId), updatedTitle});
            }

            FileHelper.writeCsvFile(UPDATE_DOCUMENTS_CSV_PATH, updateData);
        }

        @AfterAll
        public void teardown() {
            FileHelper.createCsvFile(DELETE_DOCUMENTS_CSV_PATH);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"id"});

            for (int docId : docIds) {
                data.add(new String[]{String.valueOf(docId)});
            }

            FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, data);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                    .deleteMultipleDocuments();

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(UPDATE_DOCUMENTS_CSV_PATH)
                    .updateMultipleDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentResponse documentResponse : response.getData()) {
                assertTrue(documentResponse.isSuccessful());
                assertNotNull(documentResponse.getDocument());
                assertNotNull(documentResponse.getDocument().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully reclassify a single document")
    @Disabled
    class TestReclassifySingleDocument {

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully reclassify multiple documents")
    class TestReclassifyMultipleDocuments {
        DocumentBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            FileStagingHelper.createTestFileOnFileStaging(vaultClient);

//            Write Headers and data to CSV file
            List<String[]> createData = new ArrayList<>();
            createData.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                    "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
            for (int i = 0; i < 3; i++) {
                String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
                createData.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                        DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
            }
            FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, createData);

//		      Create multiple documents
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                    .createMultipleDocuments();
            Assertions.assertTrue(response.isSuccessful());
            for (DocumentResponse documentResponse : response.getData()) {
                Assertions.assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

//            Write Headers and data to CSV file
            List<String[]> reclassifyData = new ArrayList<>();
            reclassifyData.add(new String[]{"id", "lifecycle__v", "type__v"});

            for (int docId : docIds) {
                reclassifyData.add(new String[]{String.valueOf(docId), DOC_LIFECYCLE_NAME, DOC_RECLASSIFY_TYPE_NAME});
            }

            FileHelper.writeCsvFile(RECLASSIFY_DOCUMENTS_CSV_PATH, reclassifyData);
        }

        @AfterAll
        public void teardown() {
            FileHelper.createCsvFile(DELETE_DOCUMENTS_CSV_PATH);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"id"});

            for (int docId : docIds) {
                data.add(new String[]{String.valueOf(docId)});
            }

            FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, data);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                    .deleteMultipleDocuments();

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(RECLASSIFY_DOCUMENTS_CSV_PATH)
                    .reclassifyMultipleDocuments();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentResponse documentResponse : response.getData()) {
                assertTrue(documentResponse.isSuccessful());
                assertNotNull(documentResponse.getDocument());
                assertNotNull(documentResponse.getDocument().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update a single document version")
    @Disabled
    class TestUpdateDocumentVersion {

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create multiple document versions")
    @Disabled
    class TestCreateMultipleDocumentVersions {

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document version")
    @Disabled
    class TestCreateSingleDocumentVersion {

        DocumentResponse creatSingleDocumentVersionResponse = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            Document doc = new Document();

            doc.setName("VAPIL test create single document " + ZonedDateTime.now());
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setTitle("VAPIL test create single document");

            DocumentResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(TEST_FILE_PATH)
                    .createSingleDocument(doc);

            assertTrue(createResponse.isSuccessful());
            docIds.add(createResponse.getDocument().getId());
        }

        @AfterAll
        public void teardown() {
            DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File testFile = new File(TEST_FILE_PATH);

            creatSingleDocumentVersionResponse = vaultClient.newRequest(DocumentRequest.class)
                    .setBinaryFile(testFile.getAbsolutePath(), Files.readAllBytes(testFile.toPath()))
                    .createSingleDocumentVersion(docIds.get(0), DocumentRequest.CreateDraftType.UPLOADEDCONTENT);

            assertNotNull(creatSingleDocumentVersionResponse);

            DocumentResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                    .setBinaryFile(testFile.getAbsolutePath(), Files.readAllBytes(testFile.toPath()))
                    .createSingleDocumentVersion(docIds.get(0), DocumentRequest.CreateDraftType.UPLOADEDCONTENT);

            System.out.println("Response Status: " + createResponse.getResponseStatus());
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(creatSingleDocumentVersionResponse.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete a single document")
    class TestDeleteSingleDocument {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
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

            assertTrue(response.isSuccessful());
            docIds.add(response.getDocument().getId());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument());
            assertNotNull(response.getDocument().getId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete multiple documents from CSV")
    class TestDeleteMultipleDocuments {
        DocumentBulkResponse response = null;

        @BeforeAll
        public void setup() {
            FileStagingHelper.createTestFileOnFileStaging(vaultClient);

//            Write Headers and data to CSV file
            List<String[]> createData = new ArrayList<>();
            createData.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                    "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
            for (int i = 0; i < 3; i++) {
                String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
                createData.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                        DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
            }
            FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, createData);

//		      Create multiple documents
            List<Integer> docIds = new ArrayList<>();
            DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                    .createMultipleDocuments();
            Assertions.assertTrue(response.isSuccessful());
            for (DocumentResponse documentResponse : response.getData()) {
                Assertions.assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

            FileHelper.createCsvFile(DELETE_DOCUMENTS_CSV_PATH);

//            Write Headers and data to CSV file
            List<String[]> deleteData = new ArrayList<>();
            deleteData.add(new String[]{"id"});

            for (int docId : docIds) {
                deleteData.add(new String[]{String.valueOf(docId)});
            }

            FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, deleteData);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                    .deleteMultipleDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentResponse documentResponse : response.getData()) {
                assertTrue(documentResponse.isSuccessful());
                assertNotNull(documentResponse.getDocument());
                assertNotNull(documentResponse.getDocument().getId());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete a single document version")
    @Disabled
    class TestDeleteSingleDocumentVersion {

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete multiple document versions")
    @Disabled
    class TestDeleteMultipleDocumentVersions {

        @BeforeAll
        public void setup() {

        }

        @Test
        @Order(1)
        public void testRequest() {

        }

        @Test
        @Order(2)
        public void testResponse() {

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve deleted document IDs")
    @Disabled
    class TestRetrieveDeletedDocumentIds {
        DocumentDeletionResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setLimit(1)
                    .retrieveDeletedDocumentIds();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentDeletionResponse.DeleteDocument deleteDocument : response.getData()) {
                assertNotNull(deleteDocument.getId());
                assertNotNull(deleteDocument.getDateDeleted());
            }

            if (response.isPaginated()) {
                DocumentDeletionResponse paginatedResponse = vaultClient.newRequest(DocumentRequest.class)
                        .retrieveDeletedDocumentIdsByPage(response.getResponseDetails().getNextPage());
                Assertions.assertTrue(paginatedResponse.isSuccessful());
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

