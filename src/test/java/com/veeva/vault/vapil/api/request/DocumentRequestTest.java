/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.opencsv.CSVWriter;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag("DocumentRequest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document request")
public class DocumentRequestTest {
	static final int DOC_ID = 1;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final String VAPIL_TEST_DOC_TYPE = "vapil_test_doc_type__c";
	static final String VAPIL_TEST_DOC_SUBTYPE = "vapil_test_doc_subtype__c";
	static final String VAPIL_TEST_DOC_CLASSIFICATION = "vapil_test_doc_classification__c";
	static final String DOC_TYPE = "VAPIL Test Doc Type";
	static final String DOC_SUBTYPE = "VAPIL Test Doc Subtype";
	static final String DOC_CLASSIFICATION = "VAPIL Test Doc Classification";
	static final String RESOURCES_FOLDER_PATH = "src\\test\\resources\\";

	static List<Integer> docIds = new ArrayList<>();
	@BeforeAll
	static void createDocs(VaultClient vaultClient) throws IOException {
//		Create file on file staging server
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(FileStagingRequest.Kind.FILE, "test_create_file.txt");
		Assertions.assertTrue(fileStagingResponse.isSuccessful());

//		Create multiple documents
		String csvFilePath = RESOURCES_FOLDER_PATH + "test_create_multiple_documents.csv";
		DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(csvFilePath)
				.createMultipleDocuments();

//		Store Ids for testing
		for (DocumentResponse documentResponse : createResponse.getData()) {
			docIds.add(documentResponse.getDocument().getId());
		}
	}
	// Test Manually
	@Disabled
	@Test
	public void testExportDocuments(VaultClient vaultClient) {
		String filePath = "";
		JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
			.exportDocuments(true, true, true);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Disabled
	@Test
	public void testExportDocumentVersions(VaultClient vaultClient) {
		String filePath = "";

		JobCreateResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(filePath)
				.exportDocumentVersions(true, true);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Test Manually
	@Disabled
	@Test
	public void testRetrieveExportResults(VaultClient vaultClient) {
		int jobId = 0;
		DocumentExportResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentExportResults(jobId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@Order(15)
	@DisplayName("Should successfully retrieve deleted document IDs")
	public void testGetDocumentDeletions(VaultClient vaultClient) {
		DocumentDeletionResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setLimit(1)
				.retrieveDeletedDocumentIds();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponseDetails().getSize());

		if (response.isPaginated()) {
			DocumentDeletionResponse paginatedResponse = vaultClient.newRequest(DocumentRequest.class)
					.retrieveDeletedDocumentIdsByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
			Assertions.assertNotNull(paginatedResponse.getResponseDetails().getSize());
		}
	}

	@Disabled
	@Test
	public void testDownloadDocumentFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class).downloadDocumentFile(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Disabled
	@Test
	public void testDownloadDocumentVersionFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
				.downloadDocumentVersionFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Disabled
	@Test
	public void testDownloadDocumentVersionThumbnailFile(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRequest.class)
				.downloadDocumentVersionThumbnailFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Disabled
	@Test
	public void testCreateSingleDocumentVersionLatestContent(VaultClient vaultClient) {
		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.createSingleDocumentVersion(DOC_ID, DocumentRequest.CreateDraftType.LATESTCONTENT);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	// Test Manually
	@Disabled
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
	@DisplayName("Should successfully retrieve all documents")
	public void testRetrieveAllDocuments(VaultClient vaultClient) {
		DocumentsResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocuments();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocuments());
	}

	@Test
	@DisplayName("Should successfully retrieve all document fields")
	public void testRetrieveAllDocumentFields(VaultClient vaultClient) {
		DocumentFieldResponse response  = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentFields();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Disabled
	@Test
	public void testRetrieveCommonDocumentFields(VaultClient vaultClient) {
		Set<Integer> docIds = new HashSet<>();
		docIds.add(5);
		docIds.add(12);

		DocumentFieldResponse response  = vaultClient.newRequest(DocumentRequest.class).retrieveCommonDocumentFields(docIds);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should successfully retrieve all document types")
	public void testRetrieveAllDocumentTypes(VaultClient vaultClient) {
		DocumentTypesResponse response = vaultClient.newRequest(DocumentRequest.class).retrieveAllDocumentTypes();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getTypes());
	}

	@Test
	@DisplayName("Should successfully retrieve doc type")
	public void testRetrieveDocumentType(VaultClient vaultClient) {
		DocumentTypeResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentType(VAPIL_TEST_DOC_TYPE);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLabel());
	}

	@Test
	@DisplayName("Should successfully retrieve document subtype")
	public void testRetrieveDocumentSubtype(VaultClient vaultClient) {
		DocumentSubtypeResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentSubtype(VAPIL_TEST_DOC_TYPE,VAPIL_TEST_DOC_SUBTYPE);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLabel());
	}

	@Test
	@DisplayName("Should successfully retrieve document classification")
	public void testRetrieveDocumentClassification(VaultClient vaultClient) {
		DocumentClassificationResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentClassification(
						VAPIL_TEST_DOC_TYPE,
						VAPIL_TEST_DOC_SUBTYPE,
						VAPIL_TEST_DOC_CLASSIFICATION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLabel());
		System.out.println(response.getLabel());
	}

	@Test
	@Order(3)
	@DisplayName("Should successfully retrieve document by ID")
	public void testRetrieveDocument(VaultClient vaultClient) {
		DocumentResponse documentResponse = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocument(docIds.get(0));

		Assertions.assertTrue(documentResponse.isSuccessful());
		Assertions.assertNotNull(documentResponse.getDocument());
		Assertions.assertNotNull(documentResponse.getRenditions());
		Assertions.assertNotNull(documentResponse.getVersions());
	}

	@Test
	@Order(4)
	@DisplayName("Should successfully retrieve document versions by ID")
	public void testRetrieveDocumentVersions(VaultClient vaultClient) {
		DocumentVersionResponse documentVersionResponse = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentVersions(docIds.get(0));

		Assertions.assertTrue(documentVersionResponse.isSuccessful());
		Assertions.assertNotNull(documentVersionResponse.getVersions());
	}

	@Test
	@Order(5)
	@DisplayName("Should successfully retrieve document by ID, minor version, and major version")
	public void testRetrieveDocumentVersion(VaultClient vaultClient) {
		int id = docIds.get(0);
		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocumentVersion(id, 0, 1);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
		Assertions.assertNotNull(response.getRenditions());
		Assertions.assertNotNull(response.getVersions());
	}

	@Disabled
	@Test
	public void testUpdateDocumentVersion(VaultClient vaultClient) {
		Document doc = new Document();
		doc.setId(DOC_ID);
		doc.setTitle("VAPIL - Updated");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.updateDocumentVersion(doc, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getVersions());
	}

	@Test
	@DisplayName("Should successfully create a single document")
	public void testCreateSingleDocument(VaultClient vaultClient) {
		String filePath = "src\\test\\resources\\vapil_test_document.docx";
		Document doc = new Document();

		doc.setName("VAPIL test create single document " + ZonedDateTime.now());
		doc.setLifecycle("General Lifecycle");
		doc.setType(DOC_TYPE);
		doc.setSubtype(DOC_SUBTYPE);
		doc.setClassification(DOC_CLASSIFICATION);
		doc.setTitle("VAPIL test create single document");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
					.setInputPath(filePath)
					.createSingleDocument(doc);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	@Test
	@DisplayName("Should successfully create a document from a template")
	public void testCreateSingleDocumentFromTemplate(VaultClient vaultClient) {
		Document doc = new Document();
		
		doc.setName("VAPIL Test Doc From Template " + ZonedDateTime.now());
		doc.setType("VAPIL Test Doc Type");
		doc.setSubtype("VAPIL Test Doc Subtype");
		doc.setClassification("VAPIL Test Doc Classification");
		doc.setLifecycle("General Lifecycle");

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.createDocumentFromTemplate(doc, "vapil_test_doc_template__c");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getDocument());
	}

	// Run Manually
	@Disabled
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
	@Disabled
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
	@Disabled
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
	@Disabled
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

	@Disabled
	@Test
	public void testRetrieveDocumentLockMetadata(VaultClient vaultClient) {
		MetaDataDocumentLockResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLockMetadata();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());
	}

	@Disabled
	@Test
	public void testCreateDocumentLock(VaultClient vaultClient) {
		VaultResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.createDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Disabled
	@Test
	public void testRetrieveDocumentLock(VaultClient vaultClient) {
		DocumentLockResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLock());
	}

	@Disabled
	@Test
	public void testDeleteDocumentLock(VaultClient vaultClient) {
		VaultResponse response = vaultClient
				.newRequest(DocumentRequest.class)
				.retrieveDocumentLock(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("Should successfully create multiple documents from CSV and file on staging server")
	public void testCreateMultipleDocumentsFile(VaultClient vaultClient) throws IOException {
//		Create file on file staging server
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(FileStagingRequest.Kind.FILE, "test_create_file.txt");
		Assertions.assertTrue(fileStagingResponse.isSuccessful());

//		Create multiple documents
		String csvFilePath = RESOURCES_FOLDER_PATH + "test_create_multiple_documents.csv";
		DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(csvFilePath)
				.createMultipleDocuments();
		Assertions.assertTrue(createResponse.isSuccessful());
		for (DocumentResponse documentResponse : createResponse.getData()) {
			Assertions.assertTrue(documentResponse.isSuccessful());
		}
	}

	// Test Manually
	@Disabled
	@Test
	public void testCreateMultipleDocumentsBytes(VaultClient vaultClient) throws IOException {
		String csvFilePath = "";
		File csv = new File(csvFilePath);

		DocumentBulkResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setBinaryFile(csv.getName(), Files.readAllBytes(csv.toPath()))
				.createMultipleDocuments();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("Should successfully update a single document")
	public void testUpdateSingleDocument(VaultClient vaultClient) {
//		Update Document
		int id = docIds.get(0);
		String updatedName = "VAPIL Test Update Single Document";
		Document doc = new Document();

		doc.setName(updatedName);
		doc.setLifecycle("General Lifecycle");
		doc.setType(DOC_TYPE);
		doc.setSubtype(DOC_SUBTYPE);
		doc.setClassification(DOC_CLASSIFICATION);
		doc.setId(id);

		DocumentResponse updateDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
						.updateSingleDocument(doc);
		Assertions.assertTrue(updateDocumentResponse.isSuccessful());

//		Retrieve Document and verify update
		DocumentResponse retrieveDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
				.retrieveDocument(id);
		Assertions.assertTrue(retrieveDocumentResponse.isSuccessful());
		Assertions.assertEquals(updatedName, retrieveDocumentResponse.getDocument().getName());
	}

	@Test
	@Order(2)
	@DisplayName("Should successfully update multiple documents from a CSV")
	public void testUpdateMultipleDocuments(VaultClient vaultClient) throws IOException {
//		Write IDs to a csv file to use for updating
		String updatedTitle = "VAPIL Test Update multiple documents";
		String updateDocsCsvPath = RESOURCES_FOLDER_PATH + "test_update_multiple_documents.csv";
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"id", "title__v"});

		for (int docId : docIds) {
			data.add(new String[]{String.valueOf(docId), updatedTitle});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(updateDocsCsvPath))) {
			writer.writeAll(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Update multiple documents
		DocumentBulkResponse updateResponse = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(updateDocsCsvPath)
				.updateMultipleDocuments();
		Assertions.assertTrue(updateResponse.isSuccessful());

//		Retrieve documents and verify updates
		for (DocumentResponse documentResponse : updateResponse.getData()) {
			DocumentResponse retrieveDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
					.retrieveDocument(documentResponse.getDocument().getId());
			Assertions.assertEquals(updatedTitle, retrieveDocumentResponse.getDocument().getTitle());
		}
	}

	// Test Manually
	@Disabled
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

	@Disabled
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

	@Disabled
	@Test
	public void testReclassifyDocumentWithMigrationMode(VaultClient vaultClient) {
		Document doc = new Document();

		doc.setName("VAPIL Single Document");
		doc.setLifecycle("General Lifecycle");
		doc.setType("General");
		doc.setTitle("Test Reclassify VAPIL");
		doc.setDocumentNumber("4");
		doc.setStatus("Approved");
		doc.setId(DOC_ID);

		DocumentResponse response = vaultClient.newRequest(DocumentRequest.class)
				.setMigrationMode(true)
				.reclassifyDocument(doc);

		Assertions.assertTrue(response.isSuccessful());

	}

	@Test
	@Order(13)
	@DisplayName("Should successfully delete a single document by ID")
	public void testDeleteSingleDocument(VaultClient vaultClient) {
		int id = docIds.get(2);
		DocumentResponse deleteDocumentResponse = vaultClient.newRequest(DocumentRequest.class)
				.deleteSingleDocument(id);

		Assertions.assertTrue(deleteDocumentResponse.isSuccessful());
	}

	@Test
	@Order(14)
	@DisplayName("Should successfully delete multiple documents from a CSV")
	public void testDeleteMultipleDocuments(VaultClient vaultClient) throws IOException {
//		Write IDs to a csv file to use for deleting
		String deleteDocsCsvPath = RESOURCES_FOLDER_PATH + "test_delete_multiple_documents.csv";
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"id"});

		for (int i = 0; i < 2; i++) {
			String id = String.valueOf(docIds.get(i));
			data.add(new String[]{id});
		}

		try (CSVWriter writer = new CSVWriter(new FileWriter(deleteDocsCsvPath))) {
			writer.writeAll(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Delete multiple documents
		DocumentBulkResponse deleteDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
				.setInputPath(deleteDocsCsvPath)
				.deleteMultipleDocuments();

		Assertions.assertTrue(deleteDocumentsResponse.isSuccessful());
		for (DocumentResponse documentResponse : deleteDocumentsResponse.getData()) {
			Assertions.assertTrue(documentResponse.isSuccessful());
		}

	}

}
