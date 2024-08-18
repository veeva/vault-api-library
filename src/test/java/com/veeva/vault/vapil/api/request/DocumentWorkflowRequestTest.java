/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse.Control;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse.Prompt;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowResponse.DocumentWorkflow;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Tag("DocumentWorkflowRequest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document workflow request")
public class DocumentWorkflowRequestTest {

	private static final String VAPIL_TEST_DOC_WORKFLOW = "Objectworkflow.vapil_test_doc_workflow__c";
	static List<String> documents = new ArrayList<>();

	@BeforeAll
	static void setup(VaultClient vaultClient) throws IOException {
		DocumentBulkResponse response = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
		int docId = response.getData().get(0).getDocument().getId();
		documents.add(String.valueOf(docId));
	}

	@AfterAll
	static void teardown(VaultClient vaultClient) throws IOException {
		List<Integer> docIds = new ArrayList<>();
		for (String doc : documents) {
			docIds.add(Integer.valueOf(doc));
		}
		DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
		Assertions.assertTrue(response.isSuccessful());
		for (DocumentResponse doc : response.getData()) {
			Assertions.assertTrue(doc.isSuccessful());
		}
	}

	@Test
	@Order(1)
	@DisplayName("Should successfully retrieve all available doc workflows")
	public void testRetrieveAllDocumentWorkflows(VaultClient vaultClient) {
		DocumentWorkflowResponse response = vaultClient.newRequest(DocumentWorkflowRequest.class)
				.retrieveAllDocumentWorkflows();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());

		for (DocumentWorkflow workflow : response.getData()) {
			Assertions.assertNotNull(workflow.getName());
			Assertions.assertNotNull(workflow.getLabel());
			Assertions.assertNotNull(workflow.getType());
		}
	}
	
	@Test
	@Order(2)
	@DisplayName("Should successfully retrieve doc workflow details")
	public void testRetrieveDocumentWorkflowDetails(VaultClient vaultClient) {
		System.out.println("\n------ Retrieve all available document workflow details ------");

		DocumentWorkflowDetailsResponse response = vaultClient.newRequest(DocumentWorkflowRequest.class)
				.retrieveDocumentWorkflowDetails(VAPIL_TEST_DOC_WORKFLOW);

		if (response.getData() != null) {
			DocumentWorkflowDetailsResponse.DocumentWorkflow workflow = response.getData();
			System.out.println("Workflow Name " + workflow.getName());
			System.out.println("------------------------------");
			for (Control con : workflow.getControls()) {
				System.out.println("Control Exist\n");
				System.out.println("------------------------------");
				System.out.println("Control Instruction " + con.getInstructions());
				System.out.println("Control Label " + con.getLabel());
				System.out.println("Control Type " + con.getType());

				for (Prompt prompts : con.getPrompts()) {
					System.out.println("\tPrompts Exist\n");
					System.out.println("------------------------------");
					System.out.println("\tPrompt Label " + prompts.getLabel());
					System.out.println("\tPrompt Name " + prompts.getName());
					System.out.println("\tPrompt Multivalue " + prompts.getMultiValue());

				}
			}
		}
		System.out.println("Test Complete...");
	}

	@Test
	@Disabled("There is a bug in the API that is preventing this from working")
	@Order(3)
	@DisplayName("Should successfully retrieve doc workflow details")
	public void testInitiateDocumentWorkflow(VaultClient vaultClient) {
		System.out.println("\n------ Initiate Document Workflow ------");

		// Participant Name Values pair
		HashMap<String, String> participantName = new HashMap<String, String>();
		participantName.put("", "");
		// Description value
		String description = "VAPIL Test Doc Workflow";


		InitiateDocumentWorkflowResponse response = vaultClient.newRequest(DocumentWorkflowRequest.class)
				.initiateDocumentWorkflow(VAPIL_TEST_DOC_WORKFLOW, documents, participantName, description);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData().getRecordId());
		Assertions.assertNotNull(response.getData().getWorkflowId());
		Assertions.assertNotNull(response.getData().getRecordUrl());
	}
}
