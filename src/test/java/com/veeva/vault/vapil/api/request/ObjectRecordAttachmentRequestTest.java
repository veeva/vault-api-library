/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectRecordAttachmentResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.time.ZonedDateTime;

@Tag("ObjectRecordAttachmentRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class ObjectRecordAttachmentRequestTest {
	static final String OBJECT_NAME = "";
	static final String RECORD_ID = "";
	static final int ATTACHMENT_ID = 0;
	static final int VERSION_ID = 1;

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
		ObjectRecordAttachmentResponse response = vaultClient.newRequest(ObjectRecordAttachmentRequest.class).retrieveObjectRecordAttachmentVersions(
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

}