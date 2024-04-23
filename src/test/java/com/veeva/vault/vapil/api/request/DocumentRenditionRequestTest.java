/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DocumentRenditionResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("DocumentRenditionRequest")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class DocumentRenditionRequestTest {
	static final int DOC_ID = 3;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final String RENDITION_TYPE = "viewable_rendition__v";

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
	
}

