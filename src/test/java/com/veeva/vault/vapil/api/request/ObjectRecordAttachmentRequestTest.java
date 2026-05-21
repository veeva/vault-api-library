/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.AttachmentVersion;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.ObjectRecordRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ObjectRecordAttachmentRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class ObjectRecordAttachmentRequestTest {
    private static VaultClient vaultClient;
    static final String OBJECT_NAME = "vapil_test_object__c";
//    static final String OBJECT_NAME = "";
    static final String RECORD_ID = "";
    static final int ATTACHMENT_ID = 0;
    static final int VERSION_ID = 1;

    private static String recordId;
    private static int attachmentId;
    private static int attachmentVersion;


    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

        //		Query for doc with Attachments
        String query = "SELECT id, " +
                "(SELECT attachment_id__sys, attachment_version__sys FROM attachments__sysr) " +
                "FROM vapil_test_object__c WHERE id IN (SELECT object_record_id__sys FROM attachments__sysr) " +
                "ORDER BY id ASC MAXROWS 1";
        QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class).query(query);
        assertFalse(queryResponse.isFailure());

        recordId = queryResponse.getData().get(0).getString("id");
        attachmentId = queryResponse.getData().get(0).getSubQuery("attachments__sysr").getData().get(0).getInteger("attachment_id__sys");
        attachmentVersion = queryResponse.getData().get(0).getSubQuery("attachments__sysr").getData().get(0).getInteger("attachment_version__sys");
    }

    @Test
    public void testAttachmentsEnabled(VaultClient vaultClient) {
        Boolean attachmentsEnabled = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).attachmentsEnabled(OBJECT_NAME);
        Assertions.assertNotNull(attachmentsEnabled);
    }

    @Test
    public void testRetrieveObjectRecordAttachments(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient
                .newRequest(ObjectRecordAttachmentRequest.class)
                .retrieveObjectRecordAttachments(OBJECT_NAME, RECORD_ID);
        Assertions.assertNotNull(response.isSuccessful());
    }

    @Test
    public void testRetrieveObjectRecordAttachmentMetadata(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).retrieveObjectRecordAttachmentMetadata(
                OBJECT_NAME,
                RECORD_ID,
                ATTACHMENT_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testRetrieveObjectRecordAttachmentVersions(VaultClient vaultClient) {
        ObjectRecordAttachmentVersionResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).retrieveObjectRecordAttachmentVersions(
                OBJECT_NAME,
                RECORD_ID,
                ATTACHMENT_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testRetrieveObjectRecordAttachmentVersionMetadata(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).retrieveObjectRecordAttachmentVersionMetadata(
                OBJECT_NAME,
                RECORD_ID,
                ATTACHMENT_ID,
                VERSION_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testDownloadObjectRecordAttachmentFileBytes(VaultClient vaultClient) {
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).downloadObjectRecordAttachmentFile(
                OBJECT_NAME,
                RECORD_ID,
                ATTACHMENT_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    // Test Manually
    @Test
    public void testDownloadObjectRecordAttachmentFile(VaultClient vaultClient) {
        String filePath = "";
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .setOutputPath(filePath)
                .downloadObjectRecordAttachmentFile(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Test
    public void testDownloadObjectRecordAttachmentVersionFileBytes(VaultClient vaultClient) {
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).downloadObjectRecordAttachmentVersionFile(
                OBJECT_NAME,
                RECORD_ID,
                ATTACHMENT_ID,
                VERSION_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    // Test Manually
    @Test
    public void testDownloadObjectRecordAttachmentVersionFile(VaultClient vaultClient) {
        String filePath = "";
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .setOutputPath(filePath)
                .downloadObjectRecordAttachmentVersionFile(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID, VERSION_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Test
    public void testDownloadAllObjectRecordAttachmentFilesBytes(VaultClient vaultClient) {
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).downloadAllObjectRecordAttachmentFiles(
                OBJECT_NAME,
                RECORD_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    // Run Manually
    @Test
    public void testDownloadAllObjectRecordAttachmentFiles(VaultClient vaultClient) {
        String filePath = "";
        VaultResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .setOutputPath(filePath)
                .downloadAllObjectRecordAttachmentFiles(OBJECT_NAME, RECORD_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }


    // Run Manually
    @Test
    public void testCreateObjectRecordAttachment(VaultClient vaultClient) {
        String filePath = "";
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .setInputPath(filePath)
                .createObjectRecordAttachment(OBJECT_NAME, RECORD_ID);
        Assertions.assertNotNull(response.isSuccessful());
        Assertions.assertNotNull(response.getBinaryContent());
    }

    @Test
    public void testRestoreObjectRecordAttachmentVersion(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .restoreObjectRecordAttachmentVersion(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID, VERSION_ID);
        Assertions.assertNotNull(response.isSuccessful());
    }

    @Test
    public void testUpdateObjectRecordAttachmentDescription(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .updateObjectRecordAttachmentDescription(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID, "VAPIL Test - " + ZonedDateTime.now().toString());
        Assertions.assertNotNull(response.isSuccessful());
    }

    @Test
    public void testDeleteObjectRecordAttachmentDescription(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .deleteObjectRecordAttachment(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID);
        Assertions.assertNotNull(response.isSuccessful());
    }

    @Test
    public void testDeleteObjectRecordAttachmentVersion(VaultClient vaultClient) {
        ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                .deleteObjectRecordAttachmentVersion(OBJECT_NAME, RECORD_ID, ATTACHMENT_ID, VERSION_ID);
        Assertions.assertNotNull(response.isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve Deleted Object Record Attachments")
    @Tag("SmokeTest")
    class TestRetrieveDeletedObjectRecordAttachments {
        ObjectRecordAttachmentDeletionResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            ZonedDateTime startDate = ZonedDateTime.now().minusDays(28);
            ZonedDateTime endDate = ZonedDateTime.now();
            response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                    .setStartDate(startDate)
                    .setEndDate(endDate)
                    .retrieveDeletedObjectRecordAttachments(OBJECT_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());

            assertNotNull(response.getResponseDetails());
            assertNotNull(response.getResponseDetails().getLimit());
            assertNotNull(response.getResponseDetails().getSize());
            assertNotNull(response.getResponseDetails().getTotal());
            assertNotNull(response.getResponseDetails().getOffset());

            assertNotNull(response.getData());
            for (ObjectRecordAttachmentDeletionResponse.DeleteRecordAttachment attachment : response.getData()) {
                assertNotNull(attachment.getDateDeleted());
                assertNotNull(attachment.getDeletionType());
                assertNotNull(attachment.getId());
                assertNotNull(attachment.getRecordId());
//                assertNotNull(attachment.getExternalId());
                assertNotNull(attachment.getVersion());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve object record attachment versions")
    class TestRetrieveObjectRecordAttachmentVersions {
        ObjectRecordAttachmentVersionResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                    .retrieveObjectRecordAttachmentVersions(OBJECT_NAME, recordId, attachmentId);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<AttachmentVersion> data = response.getData();
            assertNotNull(data);

            for (AttachmentVersion attachment : data) {
                assertNotNull(attachment.getVersion());
                assertNotNull(attachment.getUrl());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete an object record attachment")
    class TestDeleteObjectRecordAttachment {
        ObjectRecordAttachmentResponse response = null;
        String recordId;
        int attachmentId;

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//            Query for a record ID
            QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
            recordId = queryResponse.getData().get(0).getString("id");

//            Create Record Attachment
            ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                    .setInputPath(FileHelper.PATH_LOCAL_TEST_FILE)
                    .createObjectRecordAttachment(OBJECT_NAME, recordId);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            attachmentId = response.getData().get(0).getId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class)
                    .deleteObjectRecordAttachment(OBJECT_NAME, recordId, attachmentId);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }
}
