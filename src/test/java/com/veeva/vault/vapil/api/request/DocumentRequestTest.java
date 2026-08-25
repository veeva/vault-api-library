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
import com.veeva.vault.vapil.api.model.common.DocumentRetrieveAttachment;
import com.veeva.vault.vapil.api.model.common.Renditions;
import com.veeva.vault.vapil.api.model.metadata.DocumentField;
import com.veeva.vault.vapil.api.model.metadata.DocumentLock;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document request should")
public class DocumentRequestTest {

    private static final String DOC_LIFECYCLE_NAME = DocumentRequestHelper.VAPIL_TEST_DOC_LIFECYCLE_NAME;
    private static final String DOC_LIFECYCLE_LABEL = DocumentRequestHelper.VAPIL_TEST_DOC_LIFECYCLE_LABEL;
    private static final String DOC_TYPE_NAME = DocumentRequestHelper.VAPIL_TEST_DOC_TYPE_NAME;
    private static final String DOC_TYPE_LABEL = DocumentRequestHelper.VAPIL_TEST_DOC_TYPE_LABEL;
    private static final String DOC_SUBTYPE_NAME = DocumentRequestHelper.VAPIL_TEST_DOC_SUBTYPE_NAME;
    private static final String DOC_SUBTYPE_LABEL = DocumentRequestHelper.VAPIL_TEST_DOC_SUBTYPE_LABEL;
    private static final String DOC_CLASSIFICATION_NAME = DocumentRequestHelper.VAPIL_TEST_DOC_CLASSIFICATION_NAME;
    private static final String DOC_CLASSIFICATION_LABEL = DocumentRequestHelper.VAPIL_TEST_DOC_CLASSIFICATION_LABEL;
    private static final String DOC_RECLASSIFY_TYPE_NAME = DocumentRequestHelper.VAPIL_TEST_RECLASSIFY_TYPE_NAME;

    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 1;
    private static final String DOC_TEMPLATE = "vapil_test_doc_template__c";
    private static final String DOC_STATUS_LABEL = "Draft";
    private static final String PATH_TEST_FILE = FileHelper.PATH_LOCAL_TEST_FILE;
    private static final String PATH_CREATE_MULTIPLE_DOCUMENTS_CSV = DocumentRequestHelper.PATH_CREATE_MULTIPLE_DOCUMENTS_CSV;
    private static final String PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV = DocumentRequestHelper.PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV;
    private static final String PATH_DELETE_MULTIPLE_DOCUMENTS_CSV = DocumentRequestHelper.PATH_DELETE_MULTIPLE_DOCUMENTS_CSV;
    private static final String PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV = DocumentRequestHelper.PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV;
    private static final String PATH_RECLASSIFY_MULTIPLE_DOCUMENTS_CSV = DocumentRequestHelper.PATH_RECLASSIFY_MULTIPLE_DOCUMENTS_CSV;
    private static final String PATH_UNDO_COLLAB_CHECKOUT_CSV = DocumentRequestHelper.PATH_UNDO_COLLAB_CHECKOUT_CSV;
    private static final String PATH_EXPORT_DOCUMENTS_JSON = DocumentRequestHelper.PATH_EXPORT_DOCUMENTS_JSON;
    private static final String PATH_EXPORT_DOCUMENT_VERSIONS_JSON = DocumentRequestHelper.PATH_EXPORT_DOCUMENT_VERSIONS_JSON;
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

            assertTrue(response.isSuccessful());
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
        Set<Integer> docIds = new HashSet<>();

        @BeforeAll
        public void setup() {
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docIds.add(queryResponse.getData().get(0).getInteger("id"));
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveCommonDocumentFields(docIds);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<DocumentField> properties = response.getProperties();
            for (DocumentField property : properties) {
                assertNotNull(property.getName());
                assertNotNull(property.getDisabled());
                assertNotNull(property.getEditable());
                assertNotNull(property.getHidden());
                assertNotNull(property.getQueryable());
                assertNotNull(property.getRequired());
                assertNotNull(property.getSystemAttribute());
                assertNotNull(property.getType());
            }
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

            List<DocumentField> properties = response.getProperties();
            assertNotNull(properties);
            for (DocumentField property : properties) {
                assertNotNull(property.getName());
                assertNotNull(property.getType());
                assertNotNull(property.getRequired());
                assertNotNull(property.getEditable());
            }

            List<String> renditions = response.getRenditions();
            assertNotNull(renditions);
            for (String rendition : renditions) {
                assertNotNull(rendition);
            }

            List<DocumentClassificationResponse.RelationshipType> relationshipTypes = response.getRelationshipTypes();
            assertNotNull(relationshipTypes);
            for (DocumentClassificationResponse.RelationshipType relationshipType : relationshipTypes) {
                assertNotNull(relationshipType.getLabel());
                assertNotNull(relationshipType.getValue());
            }

            List<DocumentClassificationResponse.AvailableLifecycle> availableLifecycles = response.getAvailableLifecycles();
            assertNotNull(availableLifecycles);
            for (DocumentClassificationResponse.AvailableLifecycle availableLifecycle : availableLifecycles) {
                assertNotNull(availableLifecycle.getLabel());
                assertNotNull(availableLifecycle.getLabel());
            }

            List<DocumentTypeResponse.DocumentSubtype> subtypes = response.getSubtypes();
            assertNotNull(subtypes);
            for (DocumentTypeResponse.DocumentSubtype subtype : subtypes) {
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

            List<DocumentField> properties = response.getProperties();
            assertNotNull(properties);
            for (DocumentField property : properties) {
                assertNotNull(property.getName());
                assertNotNull(property.getType());
                assertNotNull(property.getRequired());
                assertNotNull(property.getEditable());
            }

            List<String> renditions = response.getRenditions();
            assertNotNull(renditions);
            for (String rendition : renditions) {
                assertNotNull(rendition);
            }

            List<DocumentClassificationResponse.RelationshipType> relationshipTypes = response.getRelationshipTypes();
            assertNotNull(relationshipTypes);
            for (DocumentClassificationResponse.RelationshipType relationshipType : relationshipTypes) {
                assertNotNull(relationshipType.getLabel());
                assertNotNull(relationshipType.getValue());
            }

            List<DocumentClassificationResponse.AvailableLifecycle> availableLifecycles = response.getAvailableLifecycles();
            assertNotNull(availableLifecycles);
            for (DocumentClassificationResponse.AvailableLifecycle availableLifecycle : availableLifecycles) {
                assertNotNull(availableLifecycle.getLabel());
                assertNotNull(availableLifecycle.getLabel());
            }

            List<DocumentSubtypeResponse.DocumentClassification> classifications = response.getClassifications();
            assertNotNull(classifications);
            for (DocumentSubtypeResponse.DocumentClassification classification : classifications) {
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

            List<DocumentField> properties = response.getProperties();
            assertNotNull(properties);
            for (DocumentField property : properties) {
                assertNotNull(property.getName());
                assertNotNull(property.getType());
                assertNotNull(property.getRequired());
                assertNotNull(property.getEditable());
            }

            List<String> renditions = response.getRenditions();
            assertNotNull(renditions);
            for (String rendition : renditions) {
                assertNotNull(rendition);
            }

            List<DocumentClassificationResponse.RelationshipType> relationshipTypes = response.getRelationshipTypes();
            assertNotNull(relationshipTypes);
            for (DocumentClassificationResponse.RelationshipType relationshipType : relationshipTypes) {
                assertNotNull(relationshipType.getLabel());
                assertNotNull(relationshipType.getValue());
            }

            List<DocumentClassificationResponse.AvailableLifecycle> availableLifecycles = response.getAvailableLifecycles();
            assertNotNull(availableLifecycles);
            for (DocumentClassificationResponse.AvailableLifecycle availableLifecycle : availableLifecycles) {
                assertNotNull(availableLifecycle.getLabel());
                assertNotNull(availableLifecycle.getLabel());
            }

            List<DocumentClassificationResponse.Template> templates = response.getTemplates();
            assertNotNull(templates);
            for (DocumentClassificationResponse.Template template : templates) {
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
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
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
            Renditions renditions = response.getRenditions();
            assertNotNull(renditions);

            List<DocumentResponse.Version> versions = response.getVersions();
            assertNotNull(versions);
            for (DocumentResponse.Version version : versions) {
                assertNotNull(version.getNumber());
                assertNotNull(version.getValue());
            }

            List<DocumentRetrieveAttachment> attachments = response.getAttachments();
            assertNotNull(attachments);

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
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
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
            List<DocumentVersionResponse.Version> versions = response.getVersions();
            assertNotNull(versions);
            for (DocumentVersionResponse.Version version : versions) {
                assertNotNull(version.getNumber());
                assertNotNull(version.getValue());
            }

            Renditions renditions = response.getRenditions();
            assertNotNull(renditions);
            assertNotNull(renditions.getViewableRendition());
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

            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
            minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
            majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
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
            Renditions renditions = response.getRenditions();
            assertNotNull(renditions);

            List<DocumentResponse.Version> versions = response.getVersions();
            assertNotNull(versions);
            for (DocumentResponse.Version version : versions) {
                assertNotNull(version.getNumber());
                assertNotNull(version.getValue());
            }

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
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
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
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
            minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
            majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
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
    @DisplayName("successfully download the plain text of a specific document version")
    class TestRetrieveDocumentVersionText {
        VaultResponse response = null;
        int docId;
        int minorVersion;
        int majorVersion;

        @BeforeAll
        public void setup() {
            DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveAllDocuments();

            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
            minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
            majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentVersionText(docId, majorVersion, minorVersion);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("text/plain;charset=UTF-8", response.getHeaderContentType());
            assertNotNull(response.getBinaryContent());
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
                    .setInputPath(PATH_TEST_FILE)
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
    class TestCreateSingleDocumentContentPlaceholder {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            Document doc = new Document();

            doc.setName("VAPIL test create single document: placeholder " + ZonedDateTime.now());
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setTitle("VAPIL test create single document: placeholder");

            response = vaultClient.newRequest(DocumentRequest.class)
                    .createSingleDocument(doc);

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
    @DisplayName("successfully create an unclassified document")
    @Disabled
    class TestCreateSingleDocumentUnclassified {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            Document doc = new Document();

            doc.setName("VAPIL test create single document: unclassified " + ZonedDateTime.now());
            doc.setLifecycle("unclassified__v");
            doc.setType("undefined__v");
            doc.setTitle("VAPIL test create single document: unclassified");

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_TEST_FILE)
                    .createSingleDocument(doc);

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

//    @Nested
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @DisplayName("successfully create a crosslink document")
//    @Disabled
//    class TestCreateCrosslinkDocument {
//
//
//        @BeforeAll
//        public void setup() {
//
//        }
//
//        @Test
//        @Order(1)
//        public void testRequest() {
//
//        }
//
//        @Test
//        @Order(2)
//        public void testResponse() {
//
//        }
//    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create multiple documents from CSV")
    class TestCreateMultipleDocumentsFile {
        DocumentBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            FileStagingHelper.createTestFileOnFileStaging(vaultClient);
            DocumentRequestHelper.writeToCreateMultipleDocumentsFile(3);
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocumentsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);

            assertTrue(deleteDocumentsResponse.isSuccessful());
            for (DocumentResponse documentResponse : deleteDocumentsResponse.getData()) {
                assertTrue(documentResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_CREATE_MULTIPLE_DOCUMENTS_CSV)
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
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
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
//            Create a document to update
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }

//            Write Update Multiple Documents CSV
            DocumentRequestHelper.writeToUpdateMultipleDocumentsFile(docIds);
        }

        @AfterAll
        public void teardown() {
            DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV)
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
    @DisplayName("return status of WARNING when updating records with no changes")
    class TestUpdateMultipleDocumentsWarning {

        DocumentBulkResponse updateMultipleDocumentsResponse = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//			Create documents
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 3);
            assertTrue(createResponse.isSuccessful());

            for (DocumentResponse documentResponse : createResponse.getData()) {
                assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

//			Write Update Multiple Documents CSV
            DocumentRequestHelper.writeToUpdateMultipleDocumentsFile(docIds);

//            Create first update
            DocumentBulkResponse updateDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV)
                    .updateMultipleDocuments();

            assertTrue(updateDocumentsResponse.isSuccessful());
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
                    .setInputPath(PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV)
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

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully reclassify a single document")
    @Disabled
    class TestReclassifySingleDocument {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            int id = docIds.get(0);
            Document doc = new Document();

            doc.setId(id);
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            doc.setType(DOC_RECLASSIFY_TYPE_NAME);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .reclassifySingleDocument(doc);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.get("id"));
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
//            Create documents
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }

//            Write Headers and data to CSV file
            DocumentRequestHelper.writeToReclassifyMultipleDocumentsFile(docIds);
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteDocsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_RECLASSIFY_MULTIPLE_DOCUMENTS_CSV)
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
    class TestCreateMultipleDocumentVersions {
        DocumentBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
//            Create a document
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            assertTrue(createDocumentsResponse.isSuccessful());
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }

            DocumentRequestHelper.writeToUpdateMultipleDocumentVersionsFile(docIds.get(0));
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocumentsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteDocumentsResponse.isSuccessful());
            for (DocumentResponse documentResponse : deleteDocumentsResponse.getData()) {
                assertTrue(documentResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setIdParam("id")
                    .setMigrationMode(true)
                    .setInputPath(PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV)
                    .createMultipleDocumentVersions();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (DocumentResponse documentResponse : response.getData()) {
                assertTrue(documentResponse.isSuccessful());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document version from uploaded content")
    class TestCreateSingleDocumentVersionFromUploadedContent {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);

            assertTrue(deleteDocsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File testFile = new File(PATH_TEST_FILE);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setBinaryFile(testFile.getAbsolutePath(), Files.readAllBytes(testFile.toPath()))
                    .setCreateDraft(DocumentRequest.CreateDraftType.UPLOADEDCONTENT)
                    .setDescription("VAPIL test create single document version from uploaded content")
                    .createSingleDocumentVersion(docIds.get(0));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument().getMajorVersionNumber());
            assertNotNull(response.getDocument().getMinorVersionNumber());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document version from latest content")
    class TestCreateSingleDocumentVersionFromLatestContent {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);

            assertTrue(deleteDocsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setCreateDraft(DocumentRequest.CreateDraftType.LATESTCONTENT)
                    .setDescription("VAPIL test create single document version from latest content")
                    .createSingleDocumentVersion(docIds.get(0));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDocument().getMajorVersionNumber());
            assertNotNull(response.getDocument().getMinorVersionNumber());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a single document version from latest content")
    class TestCreateSingleDocumentVersionFromPlaceholder {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
//            Create a Placeholder document
            Document doc = new Document();
            doc.setName("VAPIL test create single document: placeholder " + ZonedDateTime.now());
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);
            doc.setLifecycle(DOC_LIFECYCLE_LABEL);
            DocumentResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                    .createSingleDocument(doc);
            assertTrue(createResponse.isSuccessful());
            docIds.add(createResponse.getDocument().getId());
        }

        @AfterAll
        public void teardown() {
            DocumentBulkResponse deleteDocsResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);

            assertTrue(deleteDocsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File testFile = new File(PATH_TEST_FILE);

            response = vaultClient.newRequest(DocumentRequest.class)
                    .setBinaryFile(testFile.getAbsolutePath(), Files.readAllBytes(testFile.toPath()))
                    .setDescription("VAPIL test create single document version from placeholder")
                    .createSingleDocumentVersion(docIds.get(0));

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
    @DisplayName("successfully delete a single document")
    class TestDeleteSingleDocument {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            assertTrue(createDocumentsResponse.isSuccessful());
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
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
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 3);
            assertTrue(createDocumentsResponse.isSuccessful());

            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                assertTrue(documentResponse.isSuccessful());
                docIds.add(documentResponse.getDocument().getId());
            }

            DocumentRequestHelper.writeToDeleteMultipleDocumentsFile(docIds);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_DELETE_MULTIPLE_DOCUMENTS_CSV)
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
    class TestDeleteSingleDocumentVersion {
        DocumentResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() {
            DocumentBulkResponse createDocumentsResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            assertTrue(createDocumentsResponse.isSuccessful());
            for (DocumentResponse documentResponse : createDocumentsResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocumentVersion(docIds.get(0), MAJOR_VERSION, MINOR_VERSION);

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
    @DisplayName("successfully retrieve document lock metadata")
    class TestRetrieveDocumentLockMetadata {
        MetaDataDocumentLockResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentLockMetadata();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<DocumentLock> properties = response.getProperties();
            assertNotNull(properties);
            for (DocumentLock property : properties) {
                assertNotNull(property.getName());
                assertNotNull(property.getScope());
                assertNotNull(property.getType());
                assertNotNull(property.getSystemAttribute());
                assertNotNull(property.getEditable());
                assertNotNull(property.getSetOnCreateOnly());
                assertNotNull(property.getDisabled());
                assertNotNull(property.getObjectType());
                assertNotNull(property.getLabel());
                assertNotNull(property.getHidden());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create a document lock")
    class TestCreateDocumentLock {
        VaultResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");
        }

        @AfterAll
        public void teardown() {
            VaultResponse deleteLockResponse = vaultClient.newRequest(DocumentRequest.class)
                    .deleteDocumentLock(docId);

            assertTrue(deleteLockResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .createDocumentLock(docId);

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
    @DisplayName("successfully retrieve document lock")
    class TestRetrieveDocumentLock {
        DocumentLockResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");

//            Create a Document Lock
            VaultResponse createLockResponse = vaultClient.newRequest(DocumentRequest.class)
                    .createDocumentLock(docId);

            assertNotNull(createLockResponse);
            assertTrue(createLockResponse.isSuccessful());
        }

        @AfterAll
        public void teardown() {
            VaultResponse deleteLockResponse = vaultClient.newRequest(DocumentRequest.class)
                    .deleteDocumentLock(docId);

            assertTrue(deleteLockResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .retrieveDocumentLock(docId);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getLock());
            assertNotNull(response.getLock().getLockedBy());
            assertNotNull(response.getLock().getLockedDate());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete document lock")
    class TestDeleteDocumentLock {
        VaultResponse response = null;
        int docId;

        @BeforeAll
        public void setup() {
//            Query for a document ID
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            docId = queryResponse.getData().get(0).getInteger("id");

//            Create a Document Lock
            VaultResponse createLockResponse = vaultClient.newRequest(DocumentRequest.class)
                    .createDocumentLock(docId);

            assertNotNull(createLockResponse);
            assertTrue(createLockResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .deleteDocumentLock(docId);

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
    @DisplayName("successfully retrieve deleted document IDs")
    @Disabled("Collaborative Authoring can only be started from UI. Enable first, then " +
            "test this manually")
    class TestUndoCollaborativeAuthoringCheckout {
        DocumentCollaborativeCheckoutResponse response = null;
        VaultClient vaultClient = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_UNDO_COLLAB_CHECKOUT_CSV)
                    .undoCollaborativeAuthoringCheckout();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());

            for (DocumentCollaborativeCheckoutResponse.UndoCheckoutResponse response : response.getData()) {
                assertNotNull(response.getId());
                assertNotNull(response.getResponseStatus());
                assertNotNull(response.getResponseMessage());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export documents to file staging")
    class TestExportDocuments {
        JobCreateResponse response = null;
        List<Integer> docIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            assertFalse(queryResponse.isFailure());
            docIds.add(queryResponse.getData().get(0).getInteger("id"));

            DocumentRequestHelper.writeToExportDocumentsFile(docIds);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_EXPORT_DOCUMENTS_JSON)
                    .exportDocuments(true, true, true, true);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getJobId());
            assertNotNull(response.getUrl());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export document versions to file staging")
    class TestExportDocumentVersions {
        JobCreateResponse response = null;
        List<HashMap<String, Integer>> docVersions = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
            assertFalse(queryResponse.isFailure());
            HashMap<String, Integer> docVersion = new HashMap();
            docVersion.put("id", queryResponse.getData().get(0).getInteger("id"));
            docVersion.put("major_version_number__v", queryResponse.getData().get(0).getInteger("major_version_number__v"));
            docVersion.put("minor_version_number__v", queryResponse.getData().get(0).getInteger("minor_version_number__v"));
            docVersions.add(docVersion);

            DocumentRequestHelper.writeToExportDocumentVersionsFile(docVersions);
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRequest.class)
                    .setInputPath(PATH_EXPORT_DOCUMENT_VERSIONS_JSON)
                    .exportDocumentVersions(true, true, true);
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getJobId());
            assertNotNull(response.getUrl());
        }
    }
}

