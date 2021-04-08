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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Tag("DocumentAnnotation")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentAnnotationRequestTest {
	static final int DOC_ID = 7;
	static final int VIDEO_ID = 8;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;

	@Test
	public void testRetrieveDocumentAnnotations(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveDocumentAnnotations(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testRetrieveDocumentVersionAnnotations(VaultClient vaultClient) {

		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveDocumentVersionAnnotations(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testRetrieveAnchorIds(VaultClient vaultClient) {
		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.retrieveAnchorIds(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getAnchorDataList());
	}

	@Test
	public void testRetrieveDocumentVersionNotesAsCSV(VaultClient vaultClient) {

		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveDocumentVersionNotesAsCSV(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testRetrieveVideoAnnotations(VaultClient vaultClient) {
		int majorVersion = 0;
		int minorVersion = 1;

		VaultResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setOutputPath(null)
				.retrieveVideoAnnotations(VIDEO_ID, majorVersion, minorVersion);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}


	// Need to be manually run to specify file
	@Test
	public void testUploadDocumentAnnotationsFile(VaultClient vaultClient) {
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setInputPath(inputPath)
				.uploadDocumentAnnotations(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Need to be manually run to specify file
	@Test
	public void testUploadDocumentAnnotationsBytes(VaultClient vaultClient) throws IOException {
		String fileName = "";
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
					.setBinaryFile(fileName, Files.readAllBytes(new File(inputPath).toPath()))
					.uploadDocumentAnnotations(DOC_ID);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Need to be manually run to specify file
	@Test
	public void testUploadDocumentVersionAnnotationsFile(VaultClient vaultClient) {
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setInputPath(inputPath)
				.uploadDocumentVersionAnnotations(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
	}

	// Need to be manually run to specify file
	@Test
	public void testUploadDocumentVersionAnnotationsBytes(VaultClient vaultClient) throws IOException {
		String fileName = "";
		String inputPath = "";

		DocumentAnnotationResponse response = vaultClient.newRequest(DocumentAnnotationRequest.class)
				.setBinaryFile(fileName, Files.readAllBytes(new File(inputPath).toPath()))
				.uploadDocumentVersionAnnotations(DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
	}
}