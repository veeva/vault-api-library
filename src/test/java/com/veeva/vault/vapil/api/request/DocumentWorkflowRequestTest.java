/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.InitiateDocumentWorkflowResponse;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse.Control;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowDetailsResponse.Prompt;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowResponse;
import com.veeva.vault.vapil.api.model.response.DocumentWorkflowResponse.DocumentWorkflow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Tag("DocumentWorkflowRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentWorkflowRequestTest {

	@Test
	public void testDocumentWorkflows(VaultClient vaultClient) {
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
	public void testDocumentWorkflowDetails(VaultClient vaultClient, boolean loc, String workflowName) {
		System.out.println("\n------ Retrieve all available document workflow details ------");

		DocumentWorkflowDetailsResponse response = vaultClient.newRequest(DocumentWorkflowRequest.class)
				.retrieveDocumentWorkflowDetails(workflowName);

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
					System.out.println("\tPrompt Lable " + prompts.getLabel());
					System.out.println("\tPrompt Name " + prompts.getName());
					System.out.println("\tPrompt Multivalue " + prompts.getMultiValue());

				}
			}
		}
		System.out.println("Test Complete...");
	}

	/**
	 * 
	 * @param vaultClient
	 * @param loc
	 * @param workflowName
	 */
	public void testInitiateDocumentWorkflow(VaultClient vaultClient, boolean loc, String workflowName) {
		System.out.println("\n------ Initiate Document Workflow ------");

		// Document Values
		List<String> documents = new ArrayList<String>();
		documents.add("29");
		// Participant Name Values pair
		HashMap<String, String> participantName = new HashMap<String, String>();
		participantName.put("sme__c","user:2929873");
		participantName.put("executive_approvers__c","user:2929873");
		// Description value
		String description = "Workflow Description: Multi Document Workflow";


		InitiateDocumentWorkflowResponse response = vaultClient.newRequest(DocumentWorkflowRequest.class)
						.initiateDocumentWorkflow(workflowName, documents,participantName,description);

		System.out.println("Status = " + response.getResponseStatus());

		if (response.getResponseStatus().equalsIgnoreCase("SUCCESS")) {
			System.out.println("Record ID " + response.getData().getRecordId());
			System.out.println("Record URL " + response.getData().getRecordUrl());
			System.out.println("Workflow ID " + response.getData().getWorkflowId());
		}
		System.out.println("Test Complete...");
	}

	

}
