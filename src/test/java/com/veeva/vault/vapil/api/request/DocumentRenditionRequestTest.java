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
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentRenditionRequest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Document rendition request should")
public class DocumentRenditionRequestTest {
	static final int DOC_ID = 3;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final String RENDITION_TYPE = "viewable_rendition__v";
	static final String UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator
			+ "document_renditions" + File.separator + "update_multiple_document_renditions.csv";
	private static VaultClient vaultClient = null;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test // Test Manually
	public void testAddMultipleDocumentRenditions(VaultClient vaultClient) {
		String inputPath = "";
		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.setMigrationMode(true)
				.addMultipleDocumentRenditions();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}


	@Test // Test Manually
	public void testDeleteMultipleDocumentRenditions(VaultClient vaultClient) {
		String inputPath = "";
		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.deleteMultipleDocumentRenditions();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());

	}

	@Test
	public void testRetrieveDocumentRenditions(VaultClient vaultClient) {
		DocumentRenditionResponse response = vaultClient.newRequest(DocumentRenditionRequest.class).retrieveDocumentRenditions(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getRenditionTypes());
	}

	@Test
	public void testRetrieveDocumentVersionRenditions(VaultClient vaultClient) {
		DocumentRenditionResponse response = vaultClient
				.newRequest(DocumentRenditionRequest.class)
				.retrieveDocumentVersionRenditions(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getRenditionTypes());
	}

	@Test // Test Manually
	public  void testDownloadDocumentRenditionFileLatestVersion(VaultClient vaultClient) {
		String outputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setOutputPath(outputPath)
				.downloadDocumentRenditionFile(DOC_ID, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}


	@Test // Test Manually
	public  void testDownloadDocumentRenditionFileSteadyState(VaultClient vaultClient) {
		String outputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setOutputPath(outputPath)
				.downloadDocumentRenditionFile(DOC_ID, RENDITION_TYPE, true);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test // Test Manually
	public  void testDownloadDocumentVersionRenditionFile(VaultClient vaultClient) {
		String outputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setOutputPath(outputPath)
				.downloadDocumentVersionRenditionFile(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test // Test Manually
	public void testAddSingleDocumentRendition(VaultClient vaultClient) {
		String inputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.addSingleDocumentRendition(DOC_ID, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test // Test Manually
	public  void testUploadDocumentVersionRendition(VaultClient vaultClient) {
		String inputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.uploadDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test // Test Manually
	public void testReplaceDocumentRendition(VaultClient vaultClient) {
		String inputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.replaceDocumentRendition(DOC_ID, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test // Test Manually
	public void testReplaceDocumentVersionRendition(VaultClient vaultClient) {
		String inputPath = "";
		
		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class)
				.setInputPath(inputPath)
				.replaceDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testDeleteSingleDocumentRendition(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentRenditionRequest.class).deleteSingleDocumentRendition(DOC_ID, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testDeleteDocumentVersionRendition(VaultClient vaultClient) {
		VaultResponse response = vaultClient
				.newRequest(DocumentRenditionRequest.class)
				.deleteDocumentVersionRendition(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RENDITION_TYPE);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully update multiple document renditions from a csv file")
	class TestUpdateMultipleDocumentRenditionsCsv {

		private DocumentRenditionBulkResponse updateMultipleDocumentRenditionsResponse = null;
		private int docId = 0;
		private int majorVersion = 0;
		private int minorVersion = 1;

		@BeforeAll
		public void setup() throws IOException {
			DocumentsResponse retrieveResponse = vaultClient.newRequest(DocumentRequest.class)
					.retrieveAllDocuments();

			assertTrue(retrieveResponse.isSuccessful());
			for (DocumentsResponse.DocumentNode documentNode : retrieveResponse.getDocuments()) {
				if (documentNode.getDocument().getName().contains("VAPIL Test Re-render (Do Not Delete)")) {
					docId = documentNode.getDocument().getId();
					majorVersion = documentNode.getDocument().getMajorVersionNumber();
					minorVersion = documentNode.getDocument().getMinorVersionNumber();
					break;
				}
			}

			VaultResponse deleteResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
					.deleteSingleDocumentRendition(docId, "viewable_rendition__v");

			assertTrue(deleteResponse.isSuccessful());

			List<String[]> data = new ArrayList<>();
			data.add(new String[]{"id", "major_version_number__v", "minor_version_number__v"});
			data.add(new String[]{String.valueOf(docId), String.valueOf(majorVersion), String.valueOf(minorVersion)});
			FileHelper.writeCsvFile(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH, data);
		}

		@Test
		@Order(1)
		public void testRequest() {
			updateMultipleDocumentRenditionsResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
					.setInputPath(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH)
					.updateMultipleDocumentRenditions();

			assertNotNull(updateMultipleDocumentRenditionsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(updateMultipleDocumentRenditionsResponse.isSuccessful());
			assertNotNull(updateMultipleDocumentRenditionsResponse.getData());
			for (DocumentRenditionBulkResponse.Rendition rendition : updateMultipleDocumentRenditionsResponse.getData()) {
				assertTrue(rendition.getResponseStatus().equals("SUCCESS"));
				assertNotNull(rendition.getId());
				assertNotNull(rendition.getMajorVersionNumber());
				assertNotNull(rendition.getMinorVersionNumber());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully update multiple document renditions from bytes")
	class TestUpdateMultipleDocumentRenditionsBytes {

		private DocumentRenditionBulkResponse updateMultipleDocumentRenditionsResponse = null;
		private int docId = 0;
		private int majorVersion = 0;
		private int minorVersion = 1;

		@BeforeAll
		public void setup() throws IOException {
			DocumentsResponse retrieveResponse = vaultClient.newRequest(DocumentRequest.class)
					.retrieveAllDocuments();

			assertTrue(retrieveResponse.isSuccessful());
			for (DocumentsResponse.DocumentNode documentNode : retrieveResponse.getDocuments()) {
				if (documentNode.getDocument().getName().contains("VAPIL Test Re-render (Do Not Delete)")) {
					docId = documentNode.getDocument().getId();
					majorVersion = documentNode.getDocument().getMajorVersionNumber();
					minorVersion = documentNode.getDocument().getMinorVersionNumber();
					break;
				}
			}

			VaultResponse deleteResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
					.deleteSingleDocumentRendition(docId, "viewable_rendition__v");

			assertTrue(deleteResponse.isSuccessful());

			List<String[]> data = new ArrayList<>();
			data.add(new String[]{"id", "major_version_number__v", "minor_version_number__v"});
			data.add(new String[]{String.valueOf(docId), String.valueOf(majorVersion), String.valueOf(minorVersion)});
			FileHelper.writeCsvFile(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH, data);
		}

		@Test
		@Order(1)
		public void testRequest() throws IOException {
			File csvFile = new File(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH);

			updateMultipleDocumentRenditionsResponse = vaultClient.newRequest(DocumentRenditionRequest.class)
//					.setInputPath(UPDATE_MULTIPLE_DOCUMENT_RENDITIONS_CSV_PATH)
					.setBinaryFile(csvFile.getName(), Files.readAllBytes(csvFile.toPath()))
					.updateMultipleDocumentRenditions();

			assertNotNull(updateMultipleDocumentRenditionsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(updateMultipleDocumentRenditionsResponse.isSuccessful());
			assertNotNull(updateMultipleDocumentRenditionsResponse.getData());
			for (DocumentRenditionBulkResponse.Rendition rendition : updateMultipleDocumentRenditionsResponse.getData()) {
				assertTrue(rendition.getResponseStatus().equals("SUCCESS"));
				assertNotNull(rendition.getId());
				assertNotNull(rendition.getMajorVersionNumber());
				assertNotNull(rendition.getMinorVersionNumber());
			}
		}
	}
}

