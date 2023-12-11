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
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@Tag("DocumentLifecycleRequestTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Document lifecycle request should")
public class DocumentLifecycleRequestTest {
	static final int MAJOR_VERSION = 0;
	static final int MINOR_VERSION = 1;
	static final String DOC_LIFECYCLE = "vapil_test_doc_lifecycle__c";
	static final String DRAFT_STATE = "draft__c";
	static String lifecycleActionName;
	static List<Integer> docIds = new ArrayList<>();
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) throws IOException {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

		DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient);

		Assertions.assertTrue(createResponse.isSuccessful());

		for (DocumentResponse documentResponse : createResponse.getData()) {
			Assertions.assertTrue(documentResponse.isSuccessful());
			docIds.add(documentResponse.getDocument().getId());
		}
	}

	@AfterAll
	static void teardown() {
		DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);

		Assertions.assertTrue(response.isSuccessful());
		for (DocumentResponse documentResponse : response.getData()) {
			Assertions.assertTrue(documentResponse.isSuccessful());
		}
	}

	@Test
	@Order(1)
	@DisplayName("successfully retrieve all available user actions on a specific version of a document")
	public void testRetrieveUserActions() {
		DocumentActionResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class).retrieveUserActions(
				DocumentRequestType.DOCUMENTS, Integer.valueOf(docIds.get(0)), MAJOR_VERSION, MINOR_VERSION);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getLifecycleActions());
		lifecycleActionName = response.getLifecycleActions().get(0).getName();
	}


	@Test
	@Order(2)
	@DisplayName("successfully retrieve all available user actions on specific versions of multiple documents")
	public void testRetrieveUserActionsOnMultipleDocumentsOrBinders() {
		StringJoiner documents = new StringJoiner(",");
		for (int docId : docIds) {
			documents.add(String.format("%s:%s:%s",docId, MAJOR_VERSION, MINOR_VERSION));
		}
		String joinedDocuments = documents.toString();

		DocumentActionResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class)
				.retrieveUserActionsOnMultipleDocumentsBinders(DocumentRequestType.DOCUMENTS,
						joinedDocuments, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION));
		Assertions.assertTrue(response.isSuccessful());
		for (DocumentActionResponse.LifecycleAction lifecycleAction : response.getLifecycleActions()) {
			Assertions.assertNotNull(lifecycleAction.getName());
			Assertions.assertNotNull(lifecycleAction.getLifecycleActionType());
			Assertions.assertNotNull(lifecycleAction.getLifecycle());
		}
	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve the entry criteria for a user action")
	public void testRetrieveEntryCriteria() {
		DocumentActionEntryCriteriaResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class)
				.retrieveEntryCriteria(DocumentRequestType.DOCUMENTS, docIds.get(0), MAJOR_VERSION, MINOR_VERSION, lifecycleActionName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());
		for (DocumentActionEntryCriteriaResponse.Property property : response.getProperties()) {
			Assertions.assertNotNull(property.getName());
			Assertions.assertNotNull(property.getType());
		}

	}

	@Test
	@Order(4)
	@DisplayName("successfully initiate a user action on a record")
	public void testInitiateUserAction() {
		DocumentActionInitiateResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class).initiateUserAction(
				DocumentRequestType.DOCUMENTS, docIds.get(0), MAJOR_VERSION, MINOR_VERSION, lifecycleActionName);

		Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getId());
	}

	@Test
	@Order(5)
	@DisplayName("successfully initiate a bulk user action on multiple documents")
	public void testInitiateBulkUserActions() {
		StringJoiner documents = new StringJoiner(",");
		for (int i = 1; i < docIds.size(); i++) {
			documents.add(String.format("%s:%s:%s", docIds.get(i), MAJOR_VERSION, MINOR_VERSION));
		}
		String joinedDocuments = documents.toString();

		VaultResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class)
				.setDocumentState(DRAFT_STATE)
				.setLifecycleName(DOC_LIFECYCLE)
				.initiateBulkUserActions(DocumentRequestType.DOCUMENTS, lifecycleActionName, joinedDocuments);

		Assertions.assertTrue(response.isSuccessful());
	}

	@Disabled
	@Test
	public void testRetrieveLifecycleRoleAssignmentRules() {
		DocumentLifecycleRoleAssignmentResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class)
				.setLifecycleName("general_lifecycle__c").setRoleName("editor__c")
				.retrieveLifecycleRoleAssignmentRules();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

}
