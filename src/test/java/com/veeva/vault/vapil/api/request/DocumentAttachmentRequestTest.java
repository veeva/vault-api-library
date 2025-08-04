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
import com.veeva.vault.vapil.api.model.common.DocumentAttachment;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentAttachmentRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document attachment request should")
public class DocumentAttachmentRequestTest {

	private static VaultClient vaultClient;
	private static int docId;
	private static int majorVersion;
	private static int minorVersion;
	private static int attachmentId;
	private static int attachmentVersion;
	
	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

//		Query for doc with Attachments
		String query = "SELECT id, major_version_number__v, minor_version_number__v, " +
				"(SELECT attachment_id__sys, attachment_version__sys FROM attachments__sysr) " +
				"FROM documents WHERE id IN (SELECT document_id__sys FROM attachments__sysr) ORDER BY id ASC MAXROWS 1";
		QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class).query(query);
		assertFalse(queryResponse.isFailure());

		docId = queryResponse.getData().get(0).getInteger("id");
		majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
		minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
		attachmentId = queryResponse.getData().get(0).getSubQuery("attachments__sysr").getData().get(0).getInteger("attachment_id__sys");
		attachmentVersion = queryResponse.getData().get(0).getSubQuery("attachments__sysr").getData().get(0).getInteger("attachment_version__sys");
	}

	@Nested
	@Tag("SmokeTest")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve document attachments")
	class TestRetrieveDocumentAttachments {
		DocumentAttachmentResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.retrieveDocumentAttachments(docId);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<DocumentAttachment> data = response.getData();
			assertNotNull(data);

			for (DocumentAttachment attachment : data) {
				assertNotNull(attachment.getId());
				assertNotNull(attachment.getFilename());
				assertNotNull(attachment.getFormat());
				assertNotNull(attachment.getSize());
				assertNotNull(attachment.getMd5checksum());
				assertNotNull(attachment.getVersion());
				assertNotNull(attachment.getCreatedBy());
//				assertNotNull(attachment.getCreatedDate());

				List<DocumentAttachment.Version> versions = attachment.getVersions();
				assertNotNull(versions);
				for (DocumentAttachment.Version version : versions) {
					assertNotNull(version.getVersion());
					assertNotNull(version.getUrl());
				}
			}
		}
	}

	@Nested
	@Tag("SmokeTest")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve document version attachments")
	class TestRetrieveDocumentVersionAttachments {
		DocumentAttachmentResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.retrieveDocumentVersionAttachments(docId, majorVersion, minorVersion);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<DocumentAttachment> data = response.getData();
			assertNotNull(data);

			for (DocumentAttachment attachment : data) {
				assertNotNull(attachment.getId());
				assertNotNull(attachment.getFilename());
				assertNotNull(attachment.getFormat());
				assertNotNull(attachment.getSize());
				assertNotNull(attachment.getMd5checksum());
				assertNotNull(attachment.getVersion());
				assertNotNull(attachment.getCreatedBy());
//				assertNotNull(attachment.getCreatedDate());

				List<DocumentAttachment.Version> versions = attachment.getVersions();
				assertNotNull(versions);
				for (DocumentAttachment.Version version : versions) {
					assertNotNull(version.getVersion());
					assertNotNull(version.getUrl());
				}
			}
		}
	}

	@Nested
	@Tag("SmokeTest")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve document attachment versions")
	class TestRetrieveDocumentAttachmentVersions {
		DocumentAttachmentVersionResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.retrieveDocumentAttachmentVersions(docId, attachmentId);

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

//	TODO: This endpoint has an optional parameter for attachment_version. Response differs if parameter is included or not.
	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve document version attachment versions")
	class TestRetrieveDocumentVersionAttachmentVersions {
		DocumentAttachmentResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.retrieveDocumentVersionAttachmentVersions(docId, majorVersion, minorVersion, attachmentId);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<DocumentAttachment> data = response.getData();
			assertNotNull(data);
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve Deleted document Attachments")
	@Tag("SmokeTest")
	class TestRetrieveDeletedDocumentAttachments {
		DocumentAttachmentDeletionResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			ZonedDateTime startDate = ZonedDateTime.now().minusDays(28);
			ZonedDateTime endDate = ZonedDateTime.now();
			response = vaultClient.newRequest(DocumentAttachmentRequest.class)
					.setStartDate(startDate)
					.setEndDate(endDate)
					.retrieveDeletedDocumentAttachments();

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
			for (DocumentAttachmentDeletionResponse.DeleteDocumentAttachment attachment : response.getData()) {
				assertNotNull(attachment.getDateDeleted());
				assertNotNull(attachment.getDeletionType());
				assertNotNull(attachment.getId());
				assertNotNull(attachment.getGlobalId());
//				assertNotNull(attachment.getExternalId());
//				assertNotNull(attachment.getGlobalVersionId());
				assertNotNull(attachment.getVersion());
			}
		}
	}

	@Test
	public void testRetrieveDocumentVersionAttachmentVersions(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachmentVersions(
						docId,
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
				.retrieveDocumentAttachmentMetadata(docId, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testRetrieveDocumentVersionAttachmentMetadata(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		int attachmentId = 10;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentVersionAttachmentMetadata(docId, majorVersion, minorVersion, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocumentAttachment());
	}

	@Test
	public void testRetrieveDocumentAttachmentVersionMetadata(VaultClient vaultClient) {
		int attachmentId = 10;
		int versionId = 1;
		DocumentAttachmentResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.retrieveDocumentAttachmentVersionMetadata(docId, attachmentId, versionId);
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
						docId,
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
				.downloadDocumentAttachment(docId, attachmentId);
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
				.downloadDocumentVersionAttachment(docId, majorVersion, minorVersion, attachmentId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
public void testDownloadDocumentAttachmentVersion(VaultClient vaultClient) {
		int attachmentId = 10;
		int versionId = 1;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadDocumentAttachmentVersion(docId, attachmentId, versionId);
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
				.downloadDocumentVersionAttachmentVersion(docId, majorVersion, minorVersion, attachmentId, versionId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadAllDocumentAttachment(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadAllDocumentAttachments(docId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadAllDocumentVersionAttachment(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;
		VaultResponse response = vaultClient.newRequest(DocumentAttachmentRequest.class)
				.setOutputPath(null)
				.downloadAllDocumentVersionAttachments(docId, majorVersion, minorVersion);
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
					.createDocumentAttachment(docId);
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
