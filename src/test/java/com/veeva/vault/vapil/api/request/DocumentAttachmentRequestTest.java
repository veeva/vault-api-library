/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentAttachment;
import com.veeva.vault.vapil.api.model.response.DocumentAttachmentBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentAttachmentResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Tag("DocumentAttachment")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentAttachmentRequestTest {
	final int DOC_ID = 9;

	@Test
	public void testRetrieveDocumentAttachments(VaultClient vaultClient) {
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentAttachments(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentVersionAttachments(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;

		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachments(DOC_ID, majorVersion, minorVersion);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentAttachmentVersions(VaultClient vaultClient) {
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentAttachmentVersions(DOC_ID, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentVersionAttachmentVersions(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachmentVersions(
						DOC_ID,
						majorVersion,
						minorVersion,
						attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentAttachmentMetadata(VaultClient vaultClient) {
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentAttachmentMetadata(DOC_ID, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testRetrieveDocumentVersionAttachmentMetadata(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachmentMetadata(DOC_ID, majorVersion, minorVersion, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testRetrieveDocumentAttachmentVersionMetadata(VaultClient vaultClient) {
		int attachmentId = 10;
		int versionId = 1;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentAttachmentVersionMetadata(DOC_ID, attachmentId, versionId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testRetrieveDocumentVersionAttachmentVersionMetadata(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		int versionId = 1;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachmentVersionMetadata(
						DOC_ID,
						majorVersion,
						minorVersion,
						attachmentId,
						versionId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testDownloadDocumentAttachment(VaultClient vaultClient) {
		int attachmentId = 10;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadDocumentAttachment(DOC_ID, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadDocumentVersionAttachment(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadDocumentVersionAttachment(DOC_ID, majorVersion, minorVersion, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
public void testDownloadDocumentAttachmentVersion(VaultClient vaultClient) {
		int attachmentId = 10;
		int versionId = 1;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadDocumentAttachmentVersion(DOC_ID, attachmentId, versionId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadDocumentVersionAttachmentVersion(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		int versionId = 1;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadDocumentVersionAttachmentVersion(DOC_ID, majorVersion, minorVersion, attachmentId, versionId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadAllDocumentAttachment(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadAllDocumentAttachments(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadAllDocumentVersionAttachment(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadAllDocumentVersionAttachments(DOC_ID, majorVersion, minorVersion);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	// Test manually
	@Test
	public void testCreate(VaultClient vaultClient, String csvFilePath) {
		try {
			System.out.println("\n****** Single Document Attachment ******");


			DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.setInputPath(csvFilePath)
					.createDocumentAttachment(DOC_ID);
			System.out.println(response.getResponse());
			if (response.isSuccessful()) {
				DocumentAttachment attachment = response.getDocumentAttachment();
				System.out.println("id = " + attachment.getId());
				System.out.println("version = " + attachment.getVersion());
			}


			System.out.println("Test Complete...");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Test manually
	@Test
	public void testcreateMultipleDocumentAttachmentsFile(VaultClient vaultClient) {
		String csvFilePath = "";
		DocumentAttachmentBulkResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setInputPath(csvFilePath)
				.createMultipleDocumentAttachments();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	// Test manually
	@Test
	public void testcreateMultipleDocumentAttachmentsBytes(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentAttachmentBulkResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.createMultipleDocumentAttachments();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	// Test manually
	@Test
	public void testUpdateMultipleDocumentAttachmentDescription(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentAttachmentBulkResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.updateMultipleDocumentAttachmentDescriptions();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	// Test manually
	@Test
	public void testDeleteMultipleDocumentAttachments(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentAttachmentBulkResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.updateMultipleDocumentAttachmentDescriptions();
		Assertions.assertTrue(response.isSuccessful());
	}
}
