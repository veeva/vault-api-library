/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationResponse;
import com.veeva.vault.vapil.api.model.common.DocumentAnnotation;
import com.veeva.vault.vapil.api.model.common.DocumentAnnotationReply;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationPlacemarkTypeMetadataResponse.PlacemarkTypeMetadata;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationPlacemarkTypeMetadataResponse.PlacemarkTypeMetadata.PlacemarkField;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationReferenceTypeMetadataResponse.ReferenceTypeMetadata;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationReferenceTypeMetadataResponse.ReferenceTypeMetadata.ReferenceField;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationTypeMetadataResponse.AnnotationTypeMetadata;
import com.veeva.vault.vapil.extension.DocumentAnnotationRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentAnnotation")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Document annotation request should")
public class DocumentAnnotationRequestTest {
    static final String DELETE_ANNOTATIONS_FILE_PATH = DocumentAnnotationRequestHelper.getDeleteAnnotationsFilePath();
    static final String UPDATE_ANNOTATIONS_FILE_PATH = DocumentAnnotationRequestHelper.getUpdateAnnotationsFilePath();
    static final String ANNOTATED_DOC_FILE_PATH = DocumentAnnotationRequestHelper.getAnnotatedDocFilePath();
    static final String CREATE_ANNOTATIONS_FILE_PATH = DocumentAnnotationRequestHelper.getCreateAnnotationsFilePath();
    static final String ADD_REPLIES_FILE_PATH = DocumentAnnotationRequestHelper.getAddRepliesFilePath();
    private static Integer docId;
    private static Integer majorVersionNumber;
    private static Integer minorVersionNumber;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) throws IOException {
        vaultClient = client;
        assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

//		Retrieve "VAPIL Annotations Doc" ID
        QueryResponse.QueryResult queryResult = DocumentAnnotationRequestHelper.getAnnotationsDoc(vaultClient);

        docId = queryResult.getInteger("id");
        majorVersionNumber = queryResult.getInteger("major_version_number__v");
        minorVersionNumber = queryResult.getInteger("minor_version_number__v");
    }

//    @Test
//    public void testImportDocumentAnnotationsFromPdf() {
//        DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
//                .setInputPath(PATH_ANNOTATIONS_FILE)
//                .importDocumentAnnotationsFromPdf(DOC_ID);
//        Assertions.assertTrue(response.isSuccessful());
//    }

    @Test
    public void testImportDocumentVersionAnnotationsFromPdf() {
        DocumentAnnotationImportResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                .setInputPath(ANNOTATED_DOC_FILE_PATH)
                .importDocumentVersionAnnotationsFromPdf(docId, majorVersionNumber, minorVersionNumber);
        assertTrue(response.isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export annotations from a document to bytes")
    class TestExportDocumentAnnotationsToPdfBytes {
        VaultResponse exportDocumentAnnotationsToPdfResponse;

        @Test
        @Order(1)
        public void testRequest() {
            exportDocumentAnnotationsToPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .exportDocumentAnnotationsToPdf(docId);

            assertNotNull(exportDocumentAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentAnnotationsToPdfResponse.getHeaderContentType());
            assertNotNull(exportDocumentAnnotationsToPdfResponse.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export annotations from a document to a PDF file")
    class TestExportDocumentAnnotationsToPdfFile {
        VaultResponse exportDocumentAnnotationsToPdfResponse;

        @Test
        @Order(1)
        public void testRequest() {
            exportDocumentAnnotationsToPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATED_DOC_FILE_PATH)
                    .exportDocumentAnnotationsToPdf(docId);

            assertNotNull(exportDocumentAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentAnnotationsToPdfResponse.getHeaderContentType());
            assertEquals(ANNOTATED_DOC_FILE_PATH, exportDocumentAnnotationsToPdfResponse.getOutputFilePath());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export annotations from a document version to bytes")
    class TestExportDocumentVersionAnnotationsToPdfBytes {
        VaultResponse exportDocumentVersionAnnotationsToPdfResponse;

        @Test
        @Order(1)
        public void testRequest() {
            exportDocumentVersionAnnotationsToPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(exportDocumentVersionAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentVersionAnnotationsToPdfResponse.getHeaderContentType());
            assertNotNull(exportDocumentVersionAnnotationsToPdfResponse.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully export annotations from a document version to a PDF file")
    class TestExportDocumentVersionAnnotationsToPdfFile {
        VaultResponse exportDocumentAnnotationsToPdfResponse;

        @Test
        @Order(1)
        public void testRequest() {
            exportDocumentAnnotationsToPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATED_DOC_FILE_PATH)
                    .exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(exportDocumentAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentAnnotationsToPdfResponse.getHeaderContentType());
            assertEquals(ANNOTATED_DOC_FILE_PATH, exportDocumentAnnotationsToPdfResponse.getOutputFilePath());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully import annotations from a PDF to a document")
    class TestImportDocumentAnnotationsFromPdf {

        DocumentAnnotationImportResponse importDocumentAnnotationsFromPdfResponse;

        @BeforeAll
        public void setup() {
            VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATED_DOC_FILE_PATH)
                    .exportDocumentAnnotationsToPdf(docId);

            assertNotNull(response);
            assertNotNull(response.getOutputFilePath());
        }

        @Test
        @Order(1)
        public void testRequest() {
            importDocumentAnnotationsFromPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(ANNOTATED_DOC_FILE_PATH)
                    .importDocumentAnnotationsFromPdf(docId);

            assertNotNull(importDocumentAnnotationsFromPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(importDocumentAnnotationsFromPdfResponse.isSuccessful());
            assertNotNull(importDocumentAnnotationsFromPdfResponse.getReplies());
            assertNotNull(importDocumentAnnotationsFromPdfResponse.getNewCount());
            assertNotNull(importDocumentAnnotationsFromPdfResponse.getFailures());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully import annotations from a PDF to a document version")
    class TestImportDocumentVersionAnnotationsFromPdf {

        DocumentAnnotationImportResponse importDocumentVersionAnnotationsFromPdfResponse;

        @BeforeAll
        public void setup() {
            VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATED_DOC_FILE_PATH)
                    .exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(response);
            assertNotNull(response.getOutputFilePath());
        }

        @Test
        @Order(1)
        public void testRequest() {
            importDocumentVersionAnnotationsFromPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(ANNOTATED_DOC_FILE_PATH)
                    .importDocumentVersionAnnotationsFromPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(importDocumentVersionAnnotationsFromPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(importDocumentVersionAnnotationsFromPdfResponse.isSuccessful());
            assertNotNull(importDocumentVersionAnnotationsFromPdfResponse.getReplies());
            assertNotNull(importDocumentVersionAnnotationsFromPdfResponse.getNewCount());
            assertNotNull(importDocumentVersionAnnotationsFromPdfResponse.getFailures());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve annotation type metadata")
    class TestRetrieveAnnotationTypeMetadata {

        DocumentAnnotationTypeMetadataResponse retrieveAnnotationTypeMetadataResponse;

        @Test
        @Order(1)
        public void testRequest() {
            retrieveAnnotationTypeMetadataResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .retrieveAnnotationTypeMetadata(DocumentAnnotationRequest.AnnotationType.NOTE);

            assertNotNull(retrieveAnnotationTypeMetadataResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveAnnotationTypeMetadataResponse.isSuccessful());
            DocumentAnnotationTypeMetadataResponse.AnnotationTypeMetadata data = retrieveAnnotationTypeMetadataResponse.getData();
            assertNotNull(data);
            assertNotNull(data.getName());
            assertNotNull(data.getAllowsReplies());
            assertNotNull(data.getAllowedPlacemarkTypes());
//            assertNotNull(data.getAllowedReferenceTypes());
            List<AnnotationTypeMetadata.AnnotationField> fieldList = data.getFields();
            assertNotNull(fieldList);
            for (AnnotationTypeMetadata.AnnotationField field : fieldList) {
                assertNotNull(field.getName());
                assertNotNull(field.getType());
                assertNotNull(field.getSystemManaged());
//                assertNotNull(field.getValueSet());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve annotation placemark type metadata")
    class TestRetrieveAnnotationPlacemarkTypeMetadata {

        DocumentAnnotationPlacemarkTypeMetadataResponse retrieveAnnotationPlacemarkTypeMetadataResponse;

        @Test
        @Order(1)
        public void testRequest() {
            retrieveAnnotationPlacemarkTypeMetadataResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .retrieveAnnotationPlacemarkTypeMetadata(DocumentAnnotationRequest.PlacemarkType.TEXT);

            assertNotNull(retrieveAnnotationPlacemarkTypeMetadataResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveAnnotationPlacemarkTypeMetadataResponse.isSuccessful());
            PlacemarkTypeMetadata data = retrieveAnnotationPlacemarkTypeMetadataResponse.getData();
            assertNotNull(data);
            assertNotNull(data.getName());
            List<PlacemarkField> fieldList = data.getFields();
            assertNotNull(fieldList);
            for (PlacemarkField field : fieldList) {
                assertNotNull(field.getName());
                assertNotNull(field.getType());
                assertNotNull(field.getSystemManaged());
//                assertNotNull(field.getValueSet());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve annotation reference type metadata")
    class TestRetrieveAnnotationReferenceTypeMetadata {

        DocumentAnnotationReferenceTypeMetadataResponse retrieveAnnotationReferenceTypeMetadata;

        @Test
        @Order(1)
        public void testRequest() {
            retrieveAnnotationReferenceTypeMetadata = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .retrieveAnnotationReferenceTypeMetadata(DocumentAnnotationRequest.ReferenceType.DOCUMENT);

            assertNotNull(retrieveAnnotationReferenceTypeMetadata);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveAnnotationReferenceTypeMetadata.isSuccessful());
            ReferenceTypeMetadata data = retrieveAnnotationReferenceTypeMetadata.getData();
            assertNotNull(data);
            assertNotNull(data.getName());
            List<ReferenceField> fieldList = data.getFields();
            assertNotNull(fieldList);
            for (ReferenceField field : fieldList) {
                assertNotNull(field.getName());
                assertNotNull(field.getType());
                assertNotNull(field.getSystemManaged());
//                assertNotNull(field.getValueSet());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create multiple annotations on a document, from a json string")
    class TestCreateMultipleAnnotationsString {

        DocumentAnnotationBulkResponse createMultipleAnnotationsResponse;
        String jsonString;

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationRequestHelper.writeCreateAnnotationsFile(vaultClient);
            File jsonFile = new File(CREATE_ANNOTATIONS_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonFile);
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        }

        @AfterAll
        public void teardown() {
            List<String> docIds = new ArrayList<>();
            List<String> annotationIds = new ArrayList<>();
            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }

            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            createMultipleAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setJson(jsonString)
                    .createMultipleAnnotations();

            assertNotNull(createMultipleAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = createMultipleAnnotationsResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully create multiple annotations on a document, from a json file")
    class TestCreateMultipleAnnotationsFile {

        DocumentAnnotationBulkResponse createMultipleAnnotationsResponse;

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationRequestHelper.writeCreateAnnotationsFile(vaultClient);
        }

        @AfterAll
        public void teardown() {
            List<String> docIds = new ArrayList<>();
            List<String> annotationIds = new ArrayList<>();
            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }
            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            createMultipleAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(CREATE_ANNOTATIONS_FILE_PATH)
                    .createMultipleAnnotations();

            assertNotNull(createMultipleAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = createMultipleAnnotationsResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add multiple replies to annotations on a document, from a json string")
    class TestAddAnnotationRepliesString {

        DocumentAnnotationBulkResponse addAnnotationRepliesResponse;
        String jsonString;
        List<String> docIds = new ArrayList<>();
        List<String> annotationIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = DocumentAnnotationRequestHelper.createMultipleAnnotations(vaultClient);
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());

            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }

            DocumentAnnotationRequestHelper.writeAddRepliesFile(vaultClient, docIds, annotationIds);
            File jsonFile = new File(ADD_REPLIES_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonFile);
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        }

        @AfterAll
        public void teardown() {
            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            addAnnotationRepliesResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setJson(jsonString)
                    .addAnnotationReplies();

            assertNotNull(addAnnotationRepliesResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(addAnnotationRepliesResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = addAnnotationRepliesResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add multiple replies to annotations on a document, from a json file")
    class TestAddAnnotationRepliesFile {

        DocumentAnnotationBulkResponse addAnnotationRepliesResponse;
        List<String> docIds = new ArrayList<>();
        List<String> annotationIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = DocumentAnnotationRequestHelper.createMultipleAnnotations(vaultClient);
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());

            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }

            DocumentAnnotationRequestHelper.writeAddRepliesFile(vaultClient, docIds, annotationIds);
        }

        @AfterAll
        public void teardown() {
            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            addAnnotationRepliesResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(ADD_REPLIES_FILE_PATH)
                    .addAnnotationReplies();

            assertNotNull(addAnnotationRepliesResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(addAnnotationRepliesResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = addAnnotationRepliesResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update multiple annotations on a document, from a json string")
    class TestUpdateAnnotationsString {

        DocumentAnnotationBulkResponse updateAnnotationsResponse;
        String jsonString;
        List<String> docIds = new ArrayList<>();
        List<String> annotationIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = DocumentAnnotationRequestHelper.createMultipleAnnotations(vaultClient);
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());

            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }

            DocumentAnnotationRequestHelper.writeUpdateAnnotationsFile(vaultClient, docIds, annotationIds);
            File jsonFile = new File(UPDATE_ANNOTATIONS_FILE_PATH);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonFile);
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        }

        @AfterAll
        public void teardown() {
            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            updateAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setJson(jsonString)
                    .updateAnnotations();

            assertNotNull(updateAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateAnnotationsResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = updateAnnotationsResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully update multiple annotations on a document, from a json file")
    class TestUpdateAnnotationsFile {

        DocumentAnnotationBulkResponse updateAnnotationsResponse;
        List<String> docIds = new ArrayList<>();
        List<String> annotationIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = DocumentAnnotationRequestHelper.createMultipleAnnotations(vaultClient);
            assertTrue(createMultipleAnnotationsResponse.isSuccessful());

            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                docIds.add(annotation.getDocumentVersionId());
                annotationIds.add(annotation.getId());
            }

            DocumentAnnotationRequestHelper.writeUpdateAnnotationsFile(vaultClient, docIds, annotationIds);
        }

        @AfterAll
        public void teardown() {
            DocumentAnnotationBulkResponse deleteAnnotationsResponse = DocumentAnnotationRequestHelper.deleteAnnotations(vaultClient, docIds, annotationIds);
            assertTrue(deleteAnnotationsResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            updateAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(UPDATE_ANNOTATIONS_FILE_PATH)
                    .updateAnnotations();

            assertNotNull(updateAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(updateAnnotationsResponse.isSuccessful());
            List<DocumentAnnotationResponse> data = updateAnnotationsResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationResponse annotation : data) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getGlobalVersionId());
                assertNotNull(annotation.getId());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read annotations from a document by ID")
    class TestReadAnnotationsByDocumentVersionAndType {

        DocumentAnnotationReadResponse readAnnotationsByDocumentVersionAndTypeResponse;
        Integer docId;
        Integer majorVersionNumber;
        Integer minorVersionNumber;

        @BeforeAll
        public void setup() {
            QueryResponse.QueryResult queryResult = DocumentAnnotationRequestHelper.getAnnotationsDoc(vaultClient);

            docId = queryResult.getInteger("id");
            majorVersionNumber = queryResult.getInteger("major_version_number__v");
            minorVersionNumber = queryResult.getInteger("minor_version_number__v");
        }

        @Test
        @Order(1)
        public void testRequest() {
            readAnnotationsByDocumentVersionAndTypeResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(readAnnotationsByDocumentVersionAndTypeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(readAnnotationsByDocumentVersionAndTypeResponse.isSuccessful());
            DocumentAnnotation.ResponseDetails responseDetails = readAnnotationsByDocumentVersionAndTypeResponse.getResponseDetails();
            assertNotNull(responseDetails);
            assertNotNull(responseDetails.getLimit());
            assertNotNull(responseDetails.getOffset());
            assertNotNull(responseDetails.getSize());
            assertNotNull(responseDetails.getTotal());

            List<DocumentAnnotation> data = readAnnotationsByDocumentVersionAndTypeResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotation annotation : data) {
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getModifiedByUser());
                assertNotNull(annotation.getId());
                assertNotNull(annotation.getCreatedByUser());
                assertNotNull(annotation.getComment());
                assertNotNull(annotation.getCreatedDateTime());
                assertNotNull(annotation.getModifiedDateTime());
                assertNotNull(annotation.getTitle());
                assertNotNull(annotation.getType());
                assertNotNull(annotation.getState());
                assertNotNull(annotation.getReplyCount());
                assertNotNull(annotation.getColor());

                DocumentAnnotation.Placemark placemark = annotation.getPlacemark();
                assertNotNull(placemark);
                assertNotNull(placemark.getYCoordinate());
                assertNotNull(placemark.getXCoordinate());
                assertNotNull(placemark.getTextStartIndex());
                assertNotNull(placemark.getTextEndIndex());
                assertNotNull(placemark.getType());
                assertNotNull(placemark.getHeight());
                assertNotNull(placemark.getWidth());
                assertNotNull(placemark.getPageNumber());
                assertNotNull(placemark.getStyle());
                assertNotNull(placemark.getCoordinates());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read annotations from a document by ID using paginated URL")
    class TestReadAnnotationsByDocumentVersionAndTypeByPage {
        DocumentAnnotationReadResponse readAnnotationsByDocumentVersionAndTypeByPageResponse;
        String prevPageUrl;

        @BeforeAll
        public void setup() {
            DocumentAnnotationReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setLimit(1)
                    .setOffset(1)
                    .readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);

            assertTrue(response.isSuccessful());
            DocumentAnnotation.ResponseDetails responseDetails = response.getResponseDetails();
            assertNotNull(responseDetails);
            assertEquals(1, responseDetails.getLimit());
            assertEquals(1, responseDetails.getOffset());
            assertNotNull(responseDetails.getPreviousPage());
            prevPageUrl = responseDetails.getPreviousPage();
        }

        @Test
        @Order(1)
        public void testRequest() {
            readAnnotationsByDocumentVersionAndTypeByPageResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsByDocumentVersionAndTypeByPage(prevPageUrl);

            assertNotNull(readAnnotationsByDocumentVersionAndTypeByPageResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(readAnnotationsByDocumentVersionAndTypeByPageResponse.isSuccessful());
            assertTrue(readAnnotationsByDocumentVersionAndTypeByPageResponse.isPaginated());
            DocumentAnnotation.ResponseDetails responseDetails = readAnnotationsByDocumentVersionAndTypeByPageResponse.getResponseDetails();
            assertNotNull(responseDetails);
            assertNotNull(responseDetails.getNextPage());
            assertEquals(1, responseDetails.getLimit());
            assertEquals(0, responseDetails.getOffset());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read annotations from a document by annotation ID")
    class TestReadAnnotationsById {

        DocumentAnnotationReadResponse readAnnotationsById;
        int annotationId;

        @BeforeAll
        public void setup() {
            DocumentAnnotationReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);

            assertTrue(response.isSuccessful());
            List<DocumentAnnotation> data = response.getData();
            assertNotNull(data);
            assertNotNull(data.get(0).getId());
            annotationId = data.get(0).getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            readAnnotationsById = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsById(docId, majorVersionNumber, minorVersionNumber, annotationId);

            assertNotNull(readAnnotationsById);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(readAnnotationsById.isSuccessful());

            List<DocumentAnnotation> data = readAnnotationsById.getData();
            assertNotNull(data);
            for (DocumentAnnotation annotation : data) {
                assertNotNull(annotation.getDocumentVersionId());
                assertNotNull(annotation.getModifiedByUser());
                assertNotNull(annotation.getId());
                assertNotNull(annotation.getCreatedByUser());
                assertNotNull(annotation.getComment());
                assertNotNull(annotation.getCreatedDateTime());
                assertNotNull(annotation.getModifiedDateTime());
                assertNotNull(annotation.getTitle());
                assertNotNull(annotation.getType());
                assertNotNull(annotation.getState());
                assertNotNull(annotation.getReplyCount());
                assertNotNull(annotation.getColor());

                DocumentAnnotation.Placemark placemark = annotation.getPlacemark();
                assertNotNull(placemark);
                assertNotNull(placemark.getYCoordinate());
                assertNotNull(placemark.getXCoordinate());
                assertNotNull(placemark.getTextStartIndex());
                assertNotNull(placemark.getTextEndIndex());
                assertNotNull(placemark.getType());
                assertNotNull(placemark.getHeight());
                assertNotNull(placemark.getWidth());
                assertNotNull(placemark.getPageNumber());
                assertNotNull(placemark.getStyle());
                assertNotNull(placemark.getCoordinates());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read replies from a parent annotation")
    class TestReadRepliesOfParentAnnotation {

        DocumentAnnotationReplyReadResponse readRepliesOfParentAnnotationResponse;
        int annotationId;

        @BeforeAll
        public void setup() {
            DocumentAnnotationReadResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);

            assertTrue(response.isSuccessful());
            List<DocumentAnnotation> data = response.getData();
            assertNotNull(data);
            assertNotNull(data.get(0).getId());
            annotationId = data.get(0).getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            readRepliesOfParentAnnotationResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readRepliesOfParentAnnotation(docId, majorVersionNumber, minorVersionNumber, annotationId);

            assertNotNull(readRepliesOfParentAnnotationResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(readRepliesOfParentAnnotationResponse.isSuccessful());

            List<DocumentAnnotationReply> data = readRepliesOfParentAnnotationResponse.getData();
            assertNotNull(data);
            for (DocumentAnnotationReply reply : data) {
                assertNotNull(reply.getDocumentVersionId());
                assertNotNull(reply.getModifiedByUser());
                assertNotNull(reply.getId());
                assertNotNull(reply.getCreatedByUser());
                assertNotNull(reply.getComment());
                assertNotNull(reply.getCreatedDateTime());
                assertNotNull(reply.getModifiedDateTime());
                assertNotNull(reply.getType());
                assertNotNull(reply.getState());
                assertNotNull(reply.getColor());

                DocumentAnnotationReply.Placemark placemark = reply.getPlacemark();
                assertNotNull(placemark);
                assertNotNull(placemark.getReplyPositionIndex());
                assertNotNull(placemark.getType());
                assertNotNull(placemark.getReplyParent());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read replies from a parent annotation using paginated URL")
    class TestReadRepliesOfParentAnnotationByPage {
        DocumentAnnotationReplyReadResponse readRepliesOfParentAnnotationByPageResponse;
        String prevPageUrl;

        @BeforeAll
        public void setup() {
//            Get Annotation ID
            DocumentAnnotationReadResponse annotationReadResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readAnnotationsByDocumentVersionAndType(docId, majorVersionNumber, minorVersionNumber);

            assertTrue(annotationReadResponse.isSuccessful());
            List<DocumentAnnotation> data = annotationReadResponse.getData();
            assertNotNull(data);
            assertNotNull(data.get(0).getId());
            int annotationId = data.get(0).getId();

//            Get previous page url
            DocumentAnnotationReplyReadResponse annotationReplyReadResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOffset(1)
                    .setLimit(1)
                    .readRepliesOfParentAnnotation(docId, majorVersionNumber, minorVersionNumber, annotationId);

            assertTrue(annotationReplyReadResponse.isSuccessful());
            DocumentAnnotationReply.ResponseDetails responseDetails = annotationReplyReadResponse.getResponseDetails();
            assertNotNull(responseDetails);
            assertEquals(1, responseDetails.getLimit());
            assertEquals(1, responseDetails.getOffset());
            assertNotNull(responseDetails.getPreviousPage());
            prevPageUrl = responseDetails.getPreviousPage();
        }

        @Test
        @Order(1)
        public void testRequest() {
            readRepliesOfParentAnnotationByPageResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .readRepliesOfParentAnnotationByPage(prevPageUrl);

            assertNotNull(readRepliesOfParentAnnotationByPageResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(readRepliesOfParentAnnotationByPageResponse.isSuccessful());
            assertTrue(readRepliesOfParentAnnotationByPageResponse.isPaginated());
            DocumentAnnotationReply.ResponseDetails responseDetails = readRepliesOfParentAnnotationByPageResponse.getResponseDetails();
            assertNotNull(responseDetails);
            assertNotNull(responseDetails.getNextPage());
            assertEquals(1, responseDetails.getLimit());
            assertEquals(0, responseDetails.getOffset());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from CSV file")
    class TestDeleteAnnotationsCsv {
        DocumentAnnotationBulkResponse deleteAnnotationsResponse;

        @BeforeAll
        public void setup() throws IOException {
            DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = DocumentAnnotationRequestHelper.createMultipleAnnotations(vaultClient);

//            Write Headers and data to CSV file
            List<String[]> updateData = new ArrayList<>();
            updateData.add(new String[]{"id__sys", "document_version_id__sys"});

            for (DocumentAnnotationResponse annotation : createMultipleAnnotationsResponse.getData()) {
                assertTrue(annotation.getResponseStatus().equals("SUCCESS"));
                updateData.add(new String[]{annotation.getId(), annotation.getDocumentVersionId()});
            }

            FileHelper.writeCsvFile(DELETE_ANNOTATIONS_FILE_PATH, updateData);
        }

        @Test
        @Order(1)
        public void testRequest() {
            deleteAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(DELETE_ANNOTATIONS_FILE_PATH)
                    .deleteAnnotations();

            assertNotNull(deleteAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(deleteAnnotationsResponse.isSuccessful());
            assertNotNull(deleteAnnotationsResponse.getData());
            for (DocumentAnnotationResponse deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertTrue(deletedAnnotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(deletedAnnotation.getId());
                assertNotNull(deletedAnnotation.getDocumentVersionId());
                assertNotNull(deletedAnnotation.getGlobalVersionId());
            }
        }
    }

    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from CSV bytes")
    class TestDeleteAnnotationsCsvBytes {
        DocumentAnnotationBulkResponse deleteAnnotationsResponse;

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File csvFile = new File(DELETE_ANNOTATIONS_FILE_PATH);
            deleteAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setContentTypeCsv()
                    .setBinaryFile(csvFile.getName(), Files.readAllBytes(csvFile.toPath()))
                    .deleteAnnotations();

            assertNotNull(deleteAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(deleteAnnotationsResponse.isSuccessful());
            assertNotNull(deleteAnnotationsResponse.getData());
            for (DocumentAnnotationResponse deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertTrue(deletedAnnotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(deletedAnnotation.getId());
                assertNotNull(deletedAnnotation.getDocumentVersionId());
                assertNotNull(deletedAnnotation.getGlobalVersionId());
            }
        }
    }

    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from Json")
    class TestDeleteAnnotationsJson {
        DocumentAnnotationBulkResponse deleteAnnotationsResponse;
        String requestString;

        @BeforeAll
        public void setup() {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
//            Update annotation_id__v when running manually
            jsonObject.put("annotation_id__v", 12);
            jsonObject.put("document_id__v", docId);
            jsonObject.put("major_version__v", majorVersionNumber);
            jsonObject.put("minor_version__v", minorVersionNumber);
            arrayNode.add(jsonObject);
            requestString = arrayNode.toString();
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            deleteAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setContentTypeJson()
                    .setRequestString(requestString)
                    .deleteAnnotations();

            assertNotNull(deleteAnnotationsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(deleteAnnotationsResponse.isSuccessful());
            assertNotNull(deleteAnnotationsResponse.getData());
            for (DocumentAnnotationResponse deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertTrue(deletedAnnotation.getResponseStatus().equals("SUCCESS"));
                assertNotNull(deletedAnnotation.getId());
                assertNotNull(deletedAnnotation.getDocumentVersionId());
                assertNotNull(deletedAnnotation.getGlobalVersionId());
            }
        }
    }

    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve doc version notes as CSV")
    class TestRetrieveDocumentVersionNotesAsCSV {
        VaultResponse retrieveDocumentVersionNotesAsCsvResponse;

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            retrieveDocumentVersionNotesAsCsvResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                            .retrieveDocumentVersionNotesAsCSV(1221, 0, 1);

            assertNotNull(retrieveDocumentVersionNotesAsCsvResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveDocumentVersionNotesAsCsvResponse.isSuccessful());
        }
    }
}
