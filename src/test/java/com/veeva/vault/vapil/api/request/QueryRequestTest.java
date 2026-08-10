/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import static org.junit.jupiter.api.Assertions.*;


@Tag("QueryRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Query request should")
public class QueryRequestTest {

	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}
	
	@Test
	@DisplayName("successfully send a valid query")
	public void testQuery() {
		String query = "SELECT id, username__sys FROM user__sys";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setDescribeQuery(true)
				.query(query);
		assertNotNull(response);
    assertFalse(response.hasErrors());
	}
	
	@Test
	@DisplayName("successfully paginate query results")
	public void testQueryPagination() {
		String query = "SELECT id, username__sys FROM user__sys PAGESIZE 1";

		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.query(query);
		assertNotNull(response);
    assertFalse(response.hasErrors());

		if (response.isPaginated()) {
			QueryResponse paginatedResponse = vaultClient.newRequest(QueryRequest.class)
					.queryByPage(response.getResponseDetails().getNextPage());
      assertFalse(paginatedResponse.hasErrors());
			assertNotNull(paginatedResponse.getResponseDetails().getSize());
		}
	}

	@Test
	@DisplayName("successfully return the document properties object with query results")
	public void testQueryDocumentProperties() {
		String query = "SELECT id, name__v FROM documents MAXROWS 2";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setDocumentProperties(QueryRequest.DocumentPropertyType.ALL)
				.query(query);

		assertNotNull(response);
    assertFalse(response.hasErrors());
		assertNotNull(response.getDocumentProperties());
		for (QueryResponse.DocumentProperty documentProperty : response.getDocumentProperties()) {
			assertNotNull(documentProperty.getId());
			assertNotNull(documentProperty.getVersionId());

			QueryResponse.DocumentProperty.FieldProperties fieldProperties = documentProperty.getFieldProperties();
			assertNotNull(fieldProperties);
			assertNotNull(fieldProperties.getEdit());
			assertNotNull(fieldProperties.getReadOnly());

			QueryResponse.DocumentProperty.DocumentPermissions permissions = documentProperty.getPermissions();
			assertNotNull(permissions);
			assertNotNull(permissions.getAnnotate());
			assertNotNull(permissions.getChangeCoordinator());
			assertNotNull(permissions.getChangeOwner());
			assertNotNull(permissions.getCreateAnchors());
			assertNotNull(permissions.getDelete());
			assertNotNull(permissions.getDownloadSource());
			assertNotNull(permissions.getEditDocument());
			assertNotNull(permissions.getEditFields());
			assertNotNull(permissions.getEditRelationships());
			assertNotNull(permissions.getEditSharingSettings());
			assertNotNull(permissions.getManageViewableRendition());
			assertNotNull(permissions.getMultiChannelActions());
			assertNotNull(permissions.getReclassify());
			assertNotNull(permissions.getVersion());
			assertNotNull(permissions.getViewContent());
			assertNotNull(permissions.getViewDocument());
		}
	}

	@Test
	@DisplayName("successfully return the record properties object with query results")
	public void testQueryRecordProperties() {
		String query = "SELECT id, username__sys FROM user__sys MAXROWS 2";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setRecordProperties(QueryRequest.RecordPropertyType.ALL)
				.query(query);

		assertNotNull(response);
    assertFalse(response.hasErrors());
		assertNotNull(response.getRecordProperties());
		for (QueryResponse.RecordProperty recordProperty : response.getRecordProperties()) {
			assertNotNull(recordProperty.getId());
			assertNotNull(recordProperty.getFieldAdditionalData());
			assertNotNull(recordProperty.getFieldProperties());
		}
	}
}
