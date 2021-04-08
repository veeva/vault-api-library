/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


@Tag("DocumentLifecycleWorkflow")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentLifecycleWorkflowRequestTest {
	static final int DOC_ID = 9;
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;

	@Test
	public void testRetrieveUserActions(VaultClient vaultClient) {
		DocumentActionResponse response = vaultClient.newRequest(DocumentLifecycleWorkflowRequest.class).retrieveUserActions(
				DocumentRequestType.DOCUMENTS, DOC_ID, MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLifecycleActions());
	}


	@Test
	public void testRetrieveUserActionsOnMultipleDocumentsOrBinders(VaultClient vaultClient) {
		String docIds = "5:0:1, 6:0:1";
		DocumentActionResponse response = vaultClient.newRequest(DocumentLifecycleWorkflowRequest.class)
				.retrieveUserActionsOnMultiDocumentsBinders(DocumentRequestType.DOCUMENTS,
						docIds, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION));
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLifecycleActions());
	}

	@Test
	public void testRetrieveEntryCriteria(VaultClient vaultClient) {
		String lifecycleName = "start_approval__c";

		DocumentActionEntryCriteriaResponse response = vaultClient.newRequest(DocumentLifecycleWorkflowRequest.class)
				.retrieveEntryCriteria(DocumentRequestType.DOCUMENTS, DOC_ID, MAJOR_VERSION, MINOR_VERSION, lifecycleName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());

	}

	@Test
	public void testInitiateUserAction(VaultClient vaultClient) {
		String useraction = "";
		System.out.println("\n****** Initiate User Action ******");
		DocumentActionInitiateResponse response = vaultClient.newRequest(DocumentLifecycleWorkflowRequest.class).initiateUserAction(
				DocumentRequestType.DOCUMENTS, DOC_ID, MAJOR_VERSION, MINOR_VERSION, useraction);
		Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getId());
	}


	@Test
	public void testRetrieveLifecycleRoleAssignmentRules(VaultClient vaultClient) {
		DocumentLifecycleRoleAssignmentResponse response = vaultClient.newRequest(DocumentLifecycleWorkflowRequest.class)
				.setLifecycleName("general_lifecycle__c").setRoleName("editor__c")
				.retrieveLifecycleRoleAssignmentRules();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

}
