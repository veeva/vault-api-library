/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentRelationship;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag("DocumentRelationshipRequest")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class DocumentRelationshipRequestTest {
	static final int DOC_ID = 12;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final int RELATIONSHIP_ID = 5;
	static final int RELATED_DOC_ID = 4;
	static final int RELATED_DOC_ID_2 = 8;

	@Test
	public void testRetrieveDocumentTypeRelationships(VaultClient vaultClient) {
		String documentType = "general__c";

		DocumentRelationshipMetadataResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
					.retrieveDocumentTypeRelationships(documentType);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());
		Assertions.assertNotNull(response.getRelationships());
	}

	@Test
	public void testRetrieveDocumentRelationships(VaultClient vaultClient) {
	DocumentRelationshipRetrieveResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
			.retrieveDocumentRelationships(DOC_ID, MAJOR_VERSION, MINOR_VERSION);

	Assertions.assertTrue(response.isSuccessful());
	Assertions.assertNotNull(response.getRelationships());

	}

	@Test
	public void testRetrieveDocumentRelationship(VaultClient vaultClient) {

		DocumentRelationshipRetrieveResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
				.retrieveDocumentRelationship(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RELATIONSHIP_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getRelationships());
	}

	@Test
	public void testCreateSingleDocumentRelationship(VaultClient vaultClient) {
		String relationshipType = "supporting_documents__c";

		DocumentRelationship docRelationship = new DocumentRelationship();
		docRelationship.setTargetDocId(RELATED_DOC_ID);
		docRelationship.setRelationshipType(relationshipType);

		DocumentRelationshipResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
				.createSingleDocumentRelationship(DOC_ID, MAJOR_VERSION, MINOR_VERSION, docRelationship);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());

	}

	@Test // Run Manually
	public void testCreateMultipleDocumentRelationshipsCSVFile(VaultClient vaultClient) {
		String fileName = "document_relationships_sample.csv";
		String relationshipType = "supporting_documents__c";

		// CSV
		StringBuilder csvText = new StringBuilder("source_doc_id__v,source_major_version__v,source_minor_version__v,target_doc_id__v,relationship_type__v\n");
		csvText.append(DOC_ID + "," + MAJOR_VERSION + "," + MINOR_VERSION + "," + RELATED_DOC_ID + "," + relationshipType + "\n");
		csvText.append(DOC_ID + "," + MAJOR_VERSION + "," + MINOR_VERSION + "," + RELATED_DOC_ID_2 + "," + relationshipType + "\n");
		String filePath = saveToDesktop(fileName, csvText.toString());

		DocumentRelationshipRequest request = vaultClient.newRequest(DocumentRelationshipRequest.class);
		request.setInputPath(filePath);
		DocumentRelationshipBulkResponse response = request.createMultipleDocumentRelationships();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testCreateMultipleDocumentRelationshipsCSVBytes(VaultClient vaultClient) {
		String fileName = "document_relationships_sample.csv";
		String relationshipType = "supporting_documents__c";

		StringBuilder csvText = new StringBuilder("source_doc_id__v,source_major_version__v,source_minor_version__v,target_doc_id__v,relationship_type__v\n");
		csvText.append(DOC_ID + "," + MAJOR_VERSION + "," + MINOR_VERSION + "," + RELATED_DOC_ID + "," + relationshipType + "\n");
		csvText.append(DOC_ID + "," + MAJOR_VERSION + "," + MINOR_VERSION + "," + RELATED_DOC_ID_2 + "," + relationshipType + "\n");

		DocumentRelationshipRequest request = vaultClient.newRequest(DocumentRelationshipRequest.class);
		request.setBinaryFile(fileName, csvText.toString().getBytes());
		DocumentRelationshipBulkResponse response = request.createMultipleDocumentRelationships();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testCreateMultipleDocumentRelationshipsJSON(VaultClient vaultClient) {
		String relationshipType = "supporting_documents__c";

		// JSON
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"source_doc_id__v\": \"" + DOC_ID + "\",\"source_major_version__v\": \"" + MAJOR_VERSION + "\",\"source_minor_version__v\": \"" + MINOR_VERSION + "\",\"target_doc_id__v\": \"" + RELATED_DOC_ID + "\",\"relationship_type__v\": \"" + relationshipType + "\"},");
		jsonString.append("{\"source_doc_id__v\": \"" + DOC_ID + "\",\"source_major_version__v\": \"" + MAJOR_VERSION + "\",\"source_minor_version__v\": \"" + MINOR_VERSION + "\",\"target_doc_id__v\": \"" + RELATED_DOC_ID_2 + "\",\"relationship_type__v\": \"" + relationshipType + "\"}");
		jsonString.append("]");

		DocumentRelationshipRequest request = vaultClient.newRequest(DocumentRelationshipRequest.class);
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		DocumentRelationshipBulkResponse response = request.createMultipleDocumentRelationships();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testDeleteSingleDocumentRelationship(VaultClient vaultClient) {
		DocumentRelationshipResponse response = vaultClient.newRequest(DocumentRelationshipRequest.class)
				.deleteSingleDocumentRelationship(DOC_ID, MAJOR_VERSION, MINOR_VERSION, RELATIONSHIP_ID);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());
	}

	@Test
	public void testDeleteMultipleDocumentRelationshipsCSVFile(VaultClient vaultClient) {
		String fileName = "document_relationships_sample.csv";

		int relationshipId1 = 4;
		int relationshipId2 = 6;

		// CSV
		StringBuilder csvText = new StringBuilder("id\n");
		csvText.append(relationshipId1 + "\n");
		csvText.append(relationshipId2 + "\n");

		DocumentRelationshipRequest request = vaultClient.newRequest(DocumentRelationshipRequest.class);

		// Test the CSV as a file
		String filePath = saveToDesktop(fileName, csvText.toString());
		request.setInputPath(filePath);
		DocumentRelationshipBulkResponse response = request.deleteMultipleDocumentRelationships();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testDeleteMultipleDocumentRelationshipsJSON(VaultClient vaultClient) {
		int relationshipId1 = 5;
		int relationshipId2 = 6;

		// JSON
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"id\": \"" + relationshipId1 + "\"},");
		jsonString.append("{\"id\": \"" + relationshipId2 + "\"}");
		jsonString.append("]");

		DocumentRelationshipRequest request = vaultClient.newRequest(DocumentRelationshipRequest.class);
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		DocumentRelationshipBulkResponse response = request.deleteMultipleDocumentRelationships();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	public String saveToDesktop(String filename, String text) {
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", filename);

		try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
			os.write(text.getBytes());
			return outputFilePath.toString();
		}
		catch (IOException ignored) {}

		return null;
	}

}
