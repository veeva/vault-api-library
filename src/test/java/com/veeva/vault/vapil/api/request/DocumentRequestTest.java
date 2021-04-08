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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag("DocumentRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentRequestTest {
	static final int DOC_ID = 5;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;

	// Test Manually
	@Test
	public void testExportDocuments(VaultClient vaultClient) {
		String filePath = "";
		JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
			.exportDocuments(true, true, true);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testExportDocumentVersions(VaultClient vaultClient) {
		String filePath = "";

		JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
				.exportDocumentVersions(true, true);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testRetrieveExportResults(VaultClient vaultClient) {
		int jobId = 0;
		DocumentExportResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentExportResults(jobId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testGetDocumentDeletions(VaultClient vaultClient) {
		DocumentDeletionResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setOffset(3)
				.retrieveDeletedDocumentIds();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponseDetails().getSize());
	}

	@Test
	public void testDownloadDocumentFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class).downloadDocumentFile(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadDocumentVersionFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
				.downloadDocumentVersionFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testDownloadDocumentVersionThumbnailFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
				.downloadDocumentVersionThumbnailFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testCreateSingleDocumentVersionLatestContent(VaultClient vaultClient) {
		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.createSingleDocumentVersion(DOC_ID, DocumentRequest.CreateDraftType.LATESTCONTENT);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	// Test Manually
	@Test
	public void testCreateSingleDocumentVersionUploadedContent(VaultClient vaultClient) {
		String filePath = "";

		DocumentResponse response =vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
				.createSingleDocumentVersion(DOC_ID, DocumentRequest.CreateDraftType.UPLOADEDCONTENT);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	@Test
	public void testRetrieveAllDocuments(VaultClient vaultClient) {
		DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocuments();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocuments());
	}

	@Test
	public void testRetrieveAllDocumentFields(VaultClient vaultClient) {
		DocumentFieldResponse response  = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentFields();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveCommonDocumentFields(VaultClient vaultClient) {
		Set<Integer> docIds = new HashSet<>();
		docIds.add(5);
		docIds.add(12);

		DocumentFieldResponse response  = vaultClient.newRequest(DocumentRequest.class).retrieveCommonDocumentFields(docIds);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveAllDocumentTypes(VaultClient vaultClient) {
		DocumentTypesResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentTypes();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getTypes());
	}

	@Test
	public void testRetrieveDocument(VaultClient vaultClient) {
		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveDocument(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
		Assertions.assertNotNull(response.getRenditions());
		Assertions.assertNotNull(response.getVersions());
	}

	@Test
	public void testRetrieveDocumentVersion(VaultClient vaultClient) {
		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentVersion(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
		Assertions.assertNotNull(response.getRenditions());
		Assertions.assertNotNull(response.getVersions());
	}

	@Test
	public void testUpdateSingleDocument(VaultClient vaultClient) {
		Document doc = new Document();
		doc.setId(DOC_ID);
		doc.setTitle("VAPIL - Updated");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.updateDocumentVersion(doc, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getVersions());
	}

	// Run Manually
	@Test
	public void testCreateSingleDocument(VaultClient vaultClient) {
		String filePath = "";
		Document doc = new Document();

		doc.setName("VAPIL Single Document");
		doc.setLifecycle("General Lifecycle");
		doc.setType("General");
		doc.setTitle("Test Upload VAPIL");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
					.setInputPath(filePath)
					.createSingleDocument(doc);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	// Run Manually
	@Test
	public void testCreateSingleDocumentFromTemplate(VaultClient vaultClient) {
		String filePath = "";
		Document doc = new Document();

		doc.setName("VAPIL Document From Template");
		doc.setLifecycle("General Lifecycle");
		doc.setType("General");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
				.createDocumentFromTemplate(doc, "general_template__c");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	// Run Manually
	@Test
	public void testCreateContentPlaceholderDocument(VaultClient vaultClient) {
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
	@Test
	public void testCreateCrossLinkDocument(VaultClient vaultClient) {
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
	@Test
	public void testCreateCrossLinkDocumentLatestVersion(VaultClient vaultClient) {
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
	@Test
	public void testCreateCrossLinkDocumentSpecificVersion(VaultClient vaultClient) {
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

	@Test
	public void testRetrieveDocumentLockMetadata(VaultClient vaultClient) {
		MetaDataDocumentLockResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLockMetadata();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());
	}

	@Test
	public void testCreateDocumentLock(VaultClient vaultClient) {
		VaultResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.createDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveDocumentLock(VaultClient vaultClient) {
		DocumentLockResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLock());
	}

	@Test
	public void testDeleteDocumentLock(VaultClient vaultClient) {
		VaultResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testCreateMultipleDocumentsFile(VaultClient vaultClient) {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(csvFilePath)
				.createMultipleDocuments();
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testCreateMultipleDocumentsBytes(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.createMultipleDocuments();
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testUpdateMultipleDocuments(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.updateMultipleDocuments();
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Test
	public void testCreateMultipleDocumentVersions(VaultClient vaultClient) {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(csvFilePath)
				.setMigrationMode(true)
				.setIdParam("id")
				.createMultipleDocumentVersions();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testDocumentToken(VaultClient vaultClient) {
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

}
