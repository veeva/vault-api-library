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
import com.veeva.vault.vapil.extension.BinderRequestHelper;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Tag("DocumentLifecycleRequestTest")
@Tag("SmokeTest")
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
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all available user actions on a specific version of a document")
	class TestRetrieveDocumentUserActions {
		DocumentActionResponse response = null;
		int docId;
		int majorVersion;
		int minorVersion;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
			docId = queryResponse.getData().get(0).getInteger("id");
			majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveDocumentUserActions(docId, majorVersion, minorVersion);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getLifecycleActions());
			for (DocumentActionResponse.LifecycleAction lifecycleAction : response.getLifecycleActions()) {
				assertNotNull(lifecycleAction.getName());
				assertNotNull(lifecycleAction.getLabel());
				assertNotNull(lifecycleAction.getLifecycleActionType());
				assertNotNull(lifecycleAction.getLifecycle());
				assertNotNull(lifecycleAction.getState());
				assertNotNull(lifecycleAction.getExecutable());
				assertNotNull(lifecycleAction.getEntryRequirements());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all available user actions on multiple document versions")
	class TestRetrieveUserActionsOnMultipleDocuments {
		DocumentActionResponse response = null;
		StringBuilder docIdsBuilder = new StringBuilder();
		String docIds;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
			int docId = queryResponse.getData().get(0).getInteger("id");
			int majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			int minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
			docIdsBuilder.append(docId).append(":").append(majorVersion).append(":").append(minorVersion);
			docIds = docIdsBuilder.toString();
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveUserActionsOnMultipleDocuments(docIds);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getLifecycleActions());
			for (DocumentActionResponse.LifecycleAction lifecycleAction : response.getLifecycleActions()) {
				assertNotNull(lifecycleAction.getName());
				assertNotNull(lifecycleAction.getLabel());
				assertNotNull(lifecycleAction.getLifecycleActionType());
				assertNotNull(lifecycleAction.getLifecycle());
				assertNotNull(lifecycleAction.getState());
				assertNotNull(lifecycleAction.getExecutable());
				assertNotNull(lifecycleAction.getEntryRequirements());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve the document entry criteria for a user action.")
	class TestRetrieveDocumentEntryCriteria {
		DocumentActionEntryCriteriaResponse response = null;
		int docId;
		int majorVersion;
		int minorVersion;
		String userActionName;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
			docId = queryResponse.getData().get(0).getInteger("id");
			majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");

			DocumentActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveDocumentUserActions(docId, majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveDocumentEntryCriteria(docId, majorVersion, minorVersion, userActionName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getProperties());
			for (EntryCriteriaResponse.Property property : response.getProperties()) {
				assertNotNull(property.getName());
				assertNotNull(property.getType());
				assertNotNull(property.getDescription());
				assertNotNull(property.getEditable());
				assertNotNull(property.getType());
				assertNotNull(property.getScope());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully initiate a user action on a document.")
	class TestInitiateDocumentUserAction {
		DocumentActionInitiateResponse response = null;
		List<Integer> docIds = new ArrayList<>();
		int majorVersion = 0;
		int minorVersion = 1;
		String userActionName;

		@BeforeAll
		public void setup() {
			DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
			assertTrue(createResponse.isSuccessful());
			docIds.add(createResponse.getData().get(0).getDocument().getId());

			DocumentActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveDocumentUserActions(docIds.get(0), majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@AfterAll
		public void teardown() {
			DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
			assertTrue(deleteResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.initiateDocumentUserAction(docIds.get(0), majorVersion, minorVersion, userActionName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getId());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully initiate a user action on multiple documents.")
	class TestInitiateBulkDocumentUserActions {
		VaultResponse response = null;
		List<Integer> docIdsList = new ArrayList<>();
		String docIds;
		int majorVersion = 0;
		int minorVersion = 1;
		String userActionName;

		@BeforeAll
		public void setup() {
			DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 2);
			assertTrue(createResponse.isSuccessful());

			StringBuilder docStringBuilder = new StringBuilder();
			for (DocumentResponse documentResponse : createResponse.getData()) {
				docStringBuilder.append(documentResponse.getDocument().getId()).append(":")
						.append(majorVersion).append(":")
						.append(minorVersion).append(",");
				docIdsList.add(documentResponse.getDocument().getId());
			}
			docIds = docStringBuilder.toString();

			DocumentActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveDocumentUserActions(docIdsList.get(0), majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@AfterAll
		public void teardown() {
			DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIdsList);
			assertTrue(deleteResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.initiateBulkDocumentUserActions(userActionName, docIds, DOC_LIFECYCLE, DRAFT_STATE);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all available user actions on a specific version of a binder")
	class TestRetrieveBinderUserActions {
		BinderActionResponse response = null;
		int binderId;
		int majorVersion;
		int minorVersion;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = BinderRequestHelper.queryForBinderId(vaultClient);
			binderId = queryResponse.getData().get(0).getInteger("id");
			majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderUserActions(binderId, majorVersion, minorVersion);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getLifecycleActions());
			for (DocumentActionResponse.LifecycleAction lifecycleAction : response.getLifecycleActions()) {
				assertNotNull(lifecycleAction.getName());
				assertNotNull(lifecycleAction.getLabel());
				assertNotNull(lifecycleAction.getLifecycleActionType());
				assertNotNull(lifecycleAction.getLifecycle());
				assertNotNull(lifecycleAction.getState());
				assertNotNull(lifecycleAction.getExecutable());
				assertNotNull(lifecycleAction.getEntryRequirements());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all available user actions on multiple binder versions")
	class TestRetrieveUserActionsOnMultipleBinders {
		BinderActionResponse response = null;
		StringBuilder binderIdsBuilder = new StringBuilder();
		String binderIds;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = BinderRequestHelper.queryForBinderId(vaultClient);
			int docId = queryResponse.getData().get(0).getInteger("id");
			int majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			int minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");
			binderIdsBuilder.append(docId).append(":").append(majorVersion).append(":").append(minorVersion);
			binderIds = binderIdsBuilder.toString();
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveUserActionsOnMultipleBinders(binderIds);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getLifecycleActions());
			for (DocumentActionResponse.LifecycleAction lifecycleAction : response.getLifecycleActions()) {
				assertNotNull(lifecycleAction.getName());
				assertNotNull(lifecycleAction.getLabel());
				assertNotNull(lifecycleAction.getLifecycleActionType());
				assertNotNull(lifecycleAction.getLifecycle());
				assertNotNull(lifecycleAction.getState());
				assertNotNull(lifecycleAction.getExecutable());
				assertNotNull(lifecycleAction.getEntryRequirements());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve the binder entry criteria for a user action.")
	class TestRetrieveBinderEntryCriteria {
		BinderActionEntryCriteriaResponse response = null;
		int binderId;
		int majorVersion;
		int minorVersion;
		String userActionName;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = BinderRequestHelper.queryForBinderId(vaultClient);
			binderId = queryResponse.getData().get(0).getInteger("id");
			majorVersion = queryResponse.getData().get(0).getInteger("major_version_number__v");
			minorVersion = queryResponse.getData().get(0).getInteger("minor_version_number__v");

			BinderActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderUserActions(binderId, majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderEntryCriteria(binderId, majorVersion, minorVersion, userActionName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getProperties());
			for (EntryCriteriaResponse.Property property : response.getProperties()) {
				assertNotNull(property.getName());
				assertNotNull(property.getType());
				assertNotNull(property.getDescription());
				assertNotNull(property.getEditable());
				assertNotNull(property.getType());
				assertNotNull(property.getScope());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully initiate a user action on a binder.")
	class TestInitiateBinderUserAction {
		BinderActionInitiateResponse response = null;
		int binderId;
		int majorVersion = 0;
		int minorVersion = 1;
		String userActionName;

		@BeforeAll
		public void setup() {
			BinderResponse createResponse = BinderRequestHelper.createBinder(vaultClient);
			binderId = createResponse.getDocument().getId();

			BinderActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderUserActions(binderId, majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@AfterAll
		public void teardown() {
			BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
					.deleteBinder(binderId);
			assertTrue(deleteResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.initiateBinderUserAction(binderId, majorVersion, minorVersion, userActionName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getId());
		}
	}

	@Nested
	@Disabled
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully initiate a user action on a binder that starts a legacy workflow.")
	class TestInitiateBinderUserActionStartLegacyWorkflow {
		BinderActionInitiateResponse response = null;
		int binderId;
		int majorVersion = 0;
		int minorVersion = 1;
		String userActionName;
		Map<String, Object> bodyParams = new HashMap<String, Object>() {{
			put("user_control_single__c", "user:13857979");
		}};

		@BeforeAll
		public void setup() {
			BinderResponse createResponse = BinderRequestHelper.createBinder(vaultClient);
			binderId = createResponse.getDocument().getId();

			BinderActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderUserActions(binderId, majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());

			for (DocumentActionResponse.LifecycleAction action : retrieveActionResponse.getLifecycleActions()) {
				if ("workflow".equals(action.getLifecycleActionType())) {
					userActionName = action.getName();
					break;
				}
			}
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.setBodyParams(bodyParams)
					.initiateBinderUserAction(binderId, majorVersion, minorVersion, userActionName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getId());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully initiate a user action on multiple binders.")
	class TestInitiateBulkBinderUserActions {
		VaultResponse response = null;
		List<Integer> binderIdsList = new ArrayList<>();
		String binderIds;
		int majorVersion = 0;
		int minorVersion = 1;
		String userActionName;

		@BeforeAll
		public void setup() {
			BinderResponse createResponse1 = BinderRequestHelper.createBinder(vaultClient);
			BinderResponse createResponse2 = BinderRequestHelper.createBinder(vaultClient);
			binderIdsList.add(createResponse1.getDocument().getId());
			binderIdsList.add(createResponse2.getDocument().getId());

			StringBuilder binderStringBuilder = new StringBuilder();
			for (int binderId : binderIdsList) {
				binderStringBuilder.append(binderId).append(":")
						.append(majorVersion).append(":")
						.append(minorVersion).append(",");
			}
			binderIds = binderStringBuilder.toString();

			BinderActionResponse retrieveActionResponse = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.retrieveBinderUserActions(binderIdsList.get(0), majorVersion, minorVersion);
			assertTrue(retrieveActionResponse.isSuccessful());
			userActionName = retrieveActionResponse.getLifecycleActions().get(0).getName();
		}

		@AfterAll
		public void teardown() {
			for (int binderId : binderIdsList) {
				BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
						.deleteBinder(binderId);
				assertTrue(deleteResponse.isSuccessful());
			}
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(DocumentLifecycleRequest.class)
					.initiateBulkBinderUserActions(userActionName, binderIds, DOC_LIFECYCLE, DRAFT_STATE);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
		}
	}

	@Test
	@Disabled
	public void testRetrieveLifecycleRoleAssignmentRules() {
		DocumentLifecycleRoleAssignmentResponse response = vaultClient.newRequest(DocumentLifecycleRequest.class)
				.setLifecycleName("general_lifecycle__c").setRoleName("editor__c")
				.retrieveLifecycleRoleAssignmentRules();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

}
