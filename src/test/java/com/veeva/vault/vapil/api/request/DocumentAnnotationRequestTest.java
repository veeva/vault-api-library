/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentAnnotation;
import com.veeva.vault.vapil.api.model.common.DocumentAnnotationReply;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentAnnotation")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Document annotation request should")
public class DocumentAnnotationRequestTest {
    static final String DELETE_ANNOTATIONS_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "delete_annotations.csv";
    static final String ANNOTATIONS_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "VAPIL Annotations Doc (Do Not Delete).pdf";
    static final int DOC_ID = 876;
    static final int MAJOR_VERSION = 0;
    static final int MINOR_VERSION = 1;
    private static Integer docId;
    private static Integer majorVersionNumber;
    private static Integer minorVersionNumber;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

//		Retrieve "VAPIL Annotations Doc" ID
        DocumentsResponse documentBulkResponse = vaultClient.newRequest(DocumentRequest.class)
                .retrieveAllDocuments();

        assertTrue(documentBulkResponse.isSuccessful());

        for (DocumentsResponse.DocumentNode doc : documentBulkResponse.getDocuments()) {
            if (doc.getDocument().getName().contains("VAPIL Annotations Doc")) {
                docId = doc.getDocument().getId();
                majorVersionNumber = doc.getDocument().getMajorVersionNumber();
                minorVersionNumber = doc.getDocument().getMinorVersionNumber();
                break;
            }
        }

        Assertions.assertNotNull(docId);
        Assertions.assertNotNull(majorVersionNumber);
        Assertions.assertNotNull(minorVersionNumber);
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
        DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                .setInputPath(ANNOTATIONS_FILE_PATH)
                .importDocumentVersionAnnotationsFromPdf(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
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
                    .setOutputPath(ANNOTATIONS_FILE_PATH)
                    .exportDocumentAnnotationsToPdf(docId);

            assertNotNull(exportDocumentAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentAnnotationsToPdfResponse.getHeaderContentType());
            assertEquals(ANNOTATIONS_FILE_PATH, exportDocumentAnnotationsToPdfResponse.getOutputFilePath());
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
                    .setOutputPath(ANNOTATIONS_FILE_PATH)
                    .exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(exportDocumentAnnotationsToPdfResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("application/pdf;charset=UTF-8", exportDocumentAnnotationsToPdfResponse.getHeaderContentType());
            assertEquals(ANNOTATIONS_FILE_PATH, exportDocumentAnnotationsToPdfResponse.getOutputFilePath());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully import annotations from a PDF to a document")
    class TestImportDocumentAnnotationsFromPdf {

        DocumentAnnotationResponse importDocumentAnnotationsFromPdfResponse;

        @BeforeAll
        public void setup() {
            VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATIONS_FILE_PATH)
                    .exportDocumentAnnotationsToPdf(docId);

            assertNotNull(response);
            assertNotNull(response.getOutputFilePath());
        }

        @Test
        @Order(1)
        public void testRequest() {
            importDocumentAnnotationsFromPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(ANNOTATIONS_FILE_PATH)
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

        DocumentAnnotationResponse importDocumentVersionAnnotationsFromPdfResponse;

        @BeforeAll
        public void setup() {
            VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setOutputPath(ANNOTATIONS_FILE_PATH)
                    .exportDocumentVersionAnnotationsToPdf(docId, majorVersionNumber, minorVersionNumber);

            assertNotNull(response);
            assertNotNull(response.getOutputFilePath());
        }

        @Test
        @Order(1)
        public void testRequest() {
            importDocumentVersionAnnotationsFromPdfResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                    .setInputPath(ANNOTATIONS_FILE_PATH)
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
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully read annotations from a document by ID")
    class TestReadAnnotationsByDocumentVersionAndType {

        DocumentAnnotationReadResponse readAnnotationsByDocumentVersionAndTypeResponse;

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
    @Disabled("Annotations can't be created dynamically. Create annotation, update CSV file and run this test manually")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from CSV file")
    class TestDeleteAnnotationsCsv {
        DocumentAnnotationDeleteResponse deleteAnnotationsResponse;

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
            for (DocumentAnnotationDeleteResponse.DeletedAnnotation deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertNotNull(deletedAnnotation.getDocumentId());
                assertNotNull(deletedAnnotation.getMajorVersion());
                assertNotNull(deletedAnnotation.getMinorVersion());
                assertNotNull(deletedAnnotation.getAnnotationId());
            }
        }
    }

    @Nested
    @Disabled("Annotations can't be created dynamically. Create annotation, update CSV file and run this test manually")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from CSV bytes")
    class TestDeleteAnnotationsCsvBytes {
        DocumentAnnotationDeleteResponse deleteAnnotationsResponse;

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
            for (DocumentAnnotationDeleteResponse.DeletedAnnotation deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertNotNull(deletedAnnotation.getDocumentId());
                assertNotNull(deletedAnnotation.getMajorVersion());
                assertNotNull(deletedAnnotation.getMinorVersion());
                assertNotNull(deletedAnnotation.getAnnotationId());
            }
        }
    }

    @Nested
    @Disabled("Annotations can't be created dynamically. Create annotation and run this test manually")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete annotations from Json")
    class TestDeleteAnnotationsJson {
        DocumentAnnotationDeleteResponse deleteAnnotationsResponse;
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
            for (DocumentAnnotationDeleteResponse.DeletedAnnotation deletedAnnotation : deleteAnnotationsResponse.getData()) {
                assertNotNull(deletedAnnotation.getDocumentId());
                assertNotNull(deletedAnnotation.getMajorVersion());
                assertNotNull(deletedAnnotation.getMinorVersion());
                assertNotNull(deletedAnnotation.getAnnotationId());
            }
        }
    }
}
