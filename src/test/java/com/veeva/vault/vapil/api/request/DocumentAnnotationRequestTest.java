/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DocumentAnnotationResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.FileHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Tag("DocumentAnnotation")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentAnnotationRequestTest {
	static final int DOC_ID = 876;
	static final int VIDEO_ID = 8;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final String ANNOTATIONS_IMPORT_FILE_PATH = FileHelper.getPathAnnotationsImportFile();
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	public void testExportDocumentAnnotationsToPdf() {
		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.exportDocumentAnnotationsToPdf(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());

	}

	@Test
	public void testExportDocumentVersionAnnotationsToPdf() {
		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.exportDocumentVersionAnnotationsToPdf(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Disabled
	@Test
	public void testRetrieveAnchorIds() {
		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.retrieveAnchorIds(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getAnchorDataList());
	}

	@Disabled
	@Test
	public void testRetrieveDocumentVersionNotesAsCSV() {
		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveDocumentVersionNotesAsCSV(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Disabled
	@Test
	public void testRetrieveVideoAnnotations() {
		int majorVersion = 0;
		int minorVersion = 1;

		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveVideoAnnotations(VIDEO_ID, majorVersion, minorVersion);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}


	@Test
	public void testImportDocumentAnnotationsFromPdf() {
		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setInputPath(ANNOTATIONS_IMPORT_FILE_PATH)
				.importDocumentAnnotationsFromPdf(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Need to be manually run to specify file
	@Disabled
	@Test
	public void testUploadDocumentAnnotationsBytes() throws IOException {
		String fileName = "";
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
					.setBinaryFile(fileName, Files.readAllBytes(new File(inputPath).toPath()))
					.importDocumentAnnotationsFromPdf(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testImportDocumentVersionAnnotationsFromPdf() {
		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setInputPath(ANNOTATIONS_IMPORT_FILE_PATH)
				.importDocumentVersionAnnotationsFromPdf(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Need to be manually run to specify file
	@Disabled
	@Test
	public void testUploadDocumentVersionAnnotationsBytes() throws IOException {
		String fileName = "";
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setBinaryFile(fileName, Files.readAllBytes(new File(inputPath).toPath()))
				.importDocumentVersionAnnotationsFromPdf(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
	}
}