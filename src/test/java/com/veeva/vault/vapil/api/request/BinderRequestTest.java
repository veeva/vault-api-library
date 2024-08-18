package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Binder.Node.BinderSection;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.common.DocumentRelationship;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("BinderRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Binder request should")
public class BinderRequestTest {
    static final boolean DEPTH_ALL = true;
    static final int MAJOR_VERSION = 0;
    static final String DOC_LIFECYCLE = "VAPIL Test Doc Lifecycle";
    static final String DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    static final String DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    static final String DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    static final String BINDER_TEMPLATE_NAME = "vapil_test_binder_template__c";
    static final String BINDER_SECTION_LABEL = "VAPIL Test Binder Section";
    static int docId;
    static int minorVersion = 1;
    static List<Integer> binderIds = new ArrayList<>();
    static String binderSectionId;
    static String docNodeId;
    static String binderRelationshipId;
    static int jobId;
    private static VaultClient vaultClient;

    @BeforeAll
    public static void setup(VaultClient client) throws IOException {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

        DocumentBulkResponse response = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
        docId = response.getData().get(0).getDocument().getId();
    }

    @AfterAll
    public static void teardown() {
        List<Integer> docIds = new ArrayList<>();
        docIds.add(docId);
        DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(1)
    @DisplayName("successfully create a binder")
    public void testCreateBinder() {

        Document doc = new Document();

        doc.setName("VAPIL test create binder " + ZonedDateTime.now());
        doc.setLifecycle(DOC_LIFECYCLE);
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinder(doc);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument().getId());
        binderIds.add(response.getDocument().getId());
    }

    @Test
    @Order(2)
    @DisplayName("successfully create a binder version")
    public void testCreateBinderVersion() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderVersion(binderIds.get(0));
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument().getMinorVersionNumber());
        minorVersion = response.getDocument().getMinorVersionNumber();
    }

    @Test
    @Order(3)
    @DisplayName("successfully retrieve a binder")
    public void testRetrieveBinder() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .setDepth(DEPTH_ALL)
                .retrieveBinder(binderIds.get(0));
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument());
    }

    @Test
    @Order(4)
    @DisplayName("successfully retrieve all versions of a binder")
    public void testRetrieveAllBinderVersions() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveAllBinderVersions(binderIds.get(0));
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(5)
    @DisplayName("successfully retrieve a version of a binder")
    public void testRetrieveBinderVersion() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                        .retrieveBinderVersion(binderIds.get(0), MAJOR_VERSION, minorVersion);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(6)
    @DisplayName("successfully update a binder")
    public void testUpdateBinder() {
        Document doc = new Document();

        doc.setName("VAPIL test update binder " + ZonedDateTime.now());

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinder(binderIds.get(0), doc);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(7)
    @DisplayName("successfully reclassify a binder")
    public void testReclassifyBinder() {
        Document doc = new Document();

        doc.setLifecycle(DOC_LIFECYCLE);
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .reclassifyBinder(binderIds.get(0), doc);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(8)
    @DisplayName("successfully update a binder version")
    public void testUpdateBinderVersion() {
        Document doc = new Document();

        doc.setName("VAPIL test update binder version " + ZonedDateTime.now());

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinderVersion(binderIds.get(0), MAJOR_VERSION, minorVersion, doc);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(9)
    @DisplayName("successfully create a binder section")
    public void testCreateBinderSection() {

        BinderSection binderSection = new BinderSection();
        binderSection.setName(BINDER_SECTION_LABEL);

        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderSection(binderIds.get(0), binderSection);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
        binderSectionId = response.getId();
    }

    @Test
    @Order(10)
    @DisplayName("successfully retrieve all binder sections")
    public void testRetrieveBinderSections() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderSections(binderIds.get(0));
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(11)
    @DisplayName("successfully retrieve all binder sections from a sub-level node")
    public void testRetrieveBinderSectionsSub() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderSections(binderIds.get(0), binderSectionId);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(12)
    @DisplayName("successfully retrieve binder sections")
    public void testRetrieveBinderVersionSection() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderVersionSections(binderIds.get(0), MAJOR_VERSION, minorVersion, binderSectionId);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(13)
    @DisplayName("successfully update a binder section")
    public void testUpdateBinderSection() {
        BinderSection binderSection = new BinderSection();

        binderSection.setName("VAPIL test update binder section" + ZonedDateTime.now());

        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinderSection(binderIds.get(0), binderSectionId, binderSection);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(14)
    @DisplayName("successfully add a document to a binder")
    public void testAddDocumentToBinder() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .addDocumentToBinder(binderIds.get(0), docId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
        docNodeId = response.getId();
    }

    @Test
    @Order(15)
    @DisplayName("successfully move a document in a binder")
    public void testMoveDocumentInBinder() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .moveDocumentInBinder(binderIds.get(0), docNodeId, binderSectionId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
        docNodeId = response.getId();
    }

    @Test
    @Order(16)
    @DisplayName("successfully create a binder relationship")
    public void testCreateBinderRelationship() {
        DocumentRelationship docRelationship = new DocumentRelationship();
        docRelationship.setRelationshipType("supporting_documents__c");
        docRelationship.setTargetDocId(docId);
        docRelationship.setTargetMajorVersion(0);
        docRelationship.setTargetMinorVersion(1);

        DocumentRelationshipResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderRelationship(binderIds.get(0), MAJOR_VERSION, minorVersion, docRelationship);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
        binderRelationshipId = response.getId();
    }

    @Test
    @Order(17)
    @DisplayName("successfully retrieve a binder relationship")
    public void testRetrieveBinderRelationship() {
        DocumentRelationshipRetrieveResponse response = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderRelationship(binderIds.get(0), MAJOR_VERSION, minorVersion, Integer.valueOf(binderRelationshipId));

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNull(response.getErrorCodes());
        Assertions.assertNull(response.getErrorType());
        Assertions.assertNotNull(response.getRelationships());
        for (DocumentRelationshipRetrieveResponse.Relationship relationship : response.getRelationships()) {
            Assertions.assertNotNull(relationship.getRelationship().getId());
            Assertions.assertNotNull(relationship.getRelationship().getRelationshipType());
            Assertions.assertNotNull(relationship.getRelationship().getSourceDocId());
            Assertions.assertNotNull(relationship.getRelationship().getTargetDocId());
            Assertions.assertNotNull(relationship.getRelationship().getCreatedBy());
            Assertions.assertNotNull(relationship.getRelationship().getCreatedDate());
        }
    }

    @Test
    @Order(18)
    @DisplayName("successfully create a binder from a template")
    public void testCreateBinderFromTemplate() {
        Document doc = new Document();

        doc.setName("VAPIL test create binder from template " + ZonedDateTime.now());
        doc.setLifecycle(DOC_LIFECYCLE);
        doc.setType(DOC_TYPE_LABEL);
        doc.setSubtype(DOC_SUBTYPE_LABEL);
        doc.setClassification(DOC_CLASSIFICATION_LABEL);

        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .createBinderFromTemplate(doc, BINDER_TEMPLATE_NAME);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument().getId());
        binderIds.add(response.getDocument().getId());
    }

    @Test
    @Order(20)
    @DisplayName("successfully update a binding rule")
    public void testUpdateBindingRule() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBindingRule(binderIds.get(0), "default", false);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocument().getId());
    }

    @Test
    @Order(21)
    @DisplayName("successfully update a binder section binding rule")
    public void testUpdateBinderSectionBindingRule() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinderSectionBindingRule(binderIds.get(0), binderSectionId, "default", false);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(22)
    @DisplayName("successfully update a binder document binding rule")
    public void testUpdateBinderDocumentBindingRule() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .updateBinderDocumentBindingRule(binderIds.get(0), docNodeId, "default");

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(23)
    @DisplayName("successfully delete a binder relationship")
    public void testDeleteBinderRelationship() {
        DocumentRelationshipResponse response = vaultClient.newRequest(BinderRequest.class)
                .deleteBinderRelationship(binderIds.get(0), MAJOR_VERSION, minorVersion, Integer.valueOf(binderRelationshipId));

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(24)
    @DisplayName("successfully remove a document from a binder")
    public void testRemoveDocumentFromBinder() {
        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .removeDocumentFromBinder(binderIds.get(0), docNodeId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(25)
    @DisplayName("successfully export a binder")
    public void testExportBinder() {
        JobCreateResponse jobCreateResponse = vaultClient.newRequest(BinderRequest.class)
                .setExportAudit(true)
                .setExportAuditFormatType(BinderRequest.AuditFormatType.CSV)
                .setExportAttachmentType(BinderRequest.AttachmentType.ALL)
                .setExportDocumentVersionType(BinderRequest.DocumentVersionType.MAJOR)
                .exportBinder(binderIds.get(0));

        Assertions.assertTrue(jobCreateResponse.isSuccessful());
        Assertions.assertNotNull(jobCreateResponse.getJobId());
        jobId = jobCreateResponse.getJobId();

        boolean jobStatus = JobStatusHelper.checkJobCompletion(vaultClient, jobId);
        Assertions.assertTrue(jobStatus);
    }

    @Test
    @Order(26)
    @DisplayName("successfully retrieve binder export results")
    public void testRetrieveBinderExportResults() {
        BinderExportResponse exportResponse = vaultClient.newRequest(BinderRequest.class)
                .retrieveBinderExportResults(jobId);

        Assertions.assertTrue(exportResponse.isSuccessful());
        Assertions.assertNotNull(exportResponse.getJobId());
        Assertions.assertNotNull(exportResponse.getId());
        Assertions.assertNotNull(exportResponse.getFile());
    }

    @Test
    @Order(27)
    @DisplayName("successfully export sections of a binder")
    public void testExportBinderSections() {
        HashSet<String> binderSectionIds = new HashSet<>();
        binderSectionIds.add(binderSectionId);

        JobCreateResponse jobCreateResponse = vaultClient.newRequest(BinderRequest.class)
                .setExportAudit(true)
                .setExportAuditFormatType(BinderRequest.AuditFormatType.CSV)
                .setExportAttachmentType(BinderRequest.AttachmentType.ALL)
                .setExportDocumentVersionType(BinderRequest.DocumentVersionType.MAJOR)
                .exportBinderSections(binderIds.get(0), binderSectionIds);

        Assertions.assertTrue(jobCreateResponse.isSuccessful());
        Assertions.assertNotNull(jobCreateResponse.getJobId());
        int jobId = jobCreateResponse.getJobId();

        boolean jobStatus = JobStatusHelper.checkJobCompletion(vaultClient, jobId);
        Assertions.assertTrue(jobStatus);
    }

    @Test
    @Order(28)
    @DisplayName("successfully export sections of a specific binder version")
    public void testExportBinderVersionSections() {
        HashSet<String> binderSectionIds = new HashSet<>();
        binderSectionIds.add(binderSectionId);

        JobCreateResponse jobCreateResponse = vaultClient.newRequest(BinderRequest.class)
                .setExportAudit(true)
                .setExportAuditFormatType(BinderRequest.AuditFormatType.CSV)
                .setExportAttachmentType(BinderRequest.AttachmentType.ALL)
                .setExportDocumentVersionType(BinderRequest.DocumentVersionType.MAJOR)
                .exportBinderSections(binderIds.get(0), MAJOR_VERSION, minorVersion, binderSectionIds);

        Assertions.assertTrue(jobCreateResponse.isSuccessful());
        Assertions.assertNotNull(jobCreateResponse.getJobId());
        int jobId = jobCreateResponse.getJobId();

        boolean jobStatus = JobStatusHelper.checkJobCompletion(vaultClient, jobId);
        Assertions.assertTrue(jobStatus);
    }

    @Test
    @Order(29)
    @DisplayName("successfully delete a binder section")
    public void testDeleteBinderSection() {

        BinderSection binderSection = new BinderSection();
        binderSection.setName(BINDER_SECTION_LABEL);

        BinderSectionResponse response = vaultClient.newRequest(BinderRequest.class)
                .deleteBinderSection(binderIds.get(0), binderSectionId);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(30)
    @DisplayName("successfully delete a binder version")
    public void testDeleteBinderVersion() {
        BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                .deleteBinderVersion(binderIds.get(0), MAJOR_VERSION, minorVersion);
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(31)
    @DisplayName("successfully delete a binder")
    public void testDeleteBinder() {
        for (Integer id : binderIds) {
            BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                    .deleteBinder(id);

            Assertions.assertTrue(response.isSuccessful());
            Assertions.assertNotNull(response.getDocument().getId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add a document to a binder")
    class TestAddDocumentToBinder {
        BinderSectionResponse response = null;
        List<Integer> docIds = new ArrayList<>();
        List<Integer> binderIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws IOException {
//            Create Document
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            docId = createResponse.getData().get(0).getDocument().getId();

//            Create Binder
            Document doc = new Document();

            doc.setName("VAPIL test create binder " + ZonedDateTime.now());
            doc.setLifecycle(DOC_LIFECYCLE);
            doc.setType(DOC_TYPE_LABEL);
            doc.setSubtype(DOC_SUBTYPE_LABEL);
            doc.setClassification(DOC_CLASSIFICATION_LABEL);

            BinderResponse response = vaultClient.newRequest(BinderRequest.class)
                    .createBinder(doc);

            Assertions.assertTrue(response.isSuccessful());
            Assertions.assertNotNull(response.getDocument().getId());
            binderIds.add(response.getDocument().getId());
        }

        @AfterAll
        public void teardown() {
            DocumentResponse deleteDocResponse = vaultClient.newRequest(DocumentRequest.class)
                    .deleteSingleDocument(docIds.get(0));

            assertTrue(deleteDocResponse.isSuccessful());

            BinderResponse deleteBinderResponse = vaultClient.newRequest(BinderRequest.class)
                    .deleteBinder(binderIds.get(0));

            assertTrue(deleteBinderResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRequest.class)
                    .setOrder(1)
                    .setBindingRule(BinderRequest.BindingRule.DEFAULT)
                    .setParentId("rootNode")
                    .setMajorVersion(0)
                    .setMinorVersion(1)
                    .addDocumentToBinder(binderIds.get(0), docIds.get(0));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getId());
        }
    }

}
