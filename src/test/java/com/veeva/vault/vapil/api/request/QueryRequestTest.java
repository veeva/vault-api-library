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


@Tag("QueryRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Query request should")
public class QueryRequestTest {
	
	@Test
	@DisplayName("successfully send a valid query")
	public void testQuery(VaultClient vaultClient) {
		String query = "SELECT id, username__sys FROM user__sys";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setDescribeQuery(true)
				.query(query);
		Assertions.assertNotNull(response);
		Assertions.assertTrue(!response.hasErrors());
	}
	
	@Test
	@DisplayName("successfully paginate query results")
	public void testQueryPagination(VaultClient vaultClient) {
		String query = "SELECT id, username__sys FROM user__sys PAGESIZE 1";

		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.query(query);
		Assertions.assertNotNull(response);
		Assertions.assertTrue(!response.hasErrors());

		if (response.isPaginated()) {
			QueryResponse paginatedResponse = vaultClient.newRequest(QueryRequest.class)
					.queryByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(!paginatedResponse.hasErrors());
			Assertions.assertNotNull(paginatedResponse.getResponseDetails().getSize());
		}
	}

	@Test
	@DisplayName("successfully return the record properties object with query results")
	public void testQueryRecordProperties(VaultClient vaultClient) {
		String query = "SELECT id, username__sys FROM user__sys";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setRecordProperties(QueryRequest.RecordPropertyType.ALL)
				.query(query);

		Assertions.assertNotNull(response);
		Assertions.assertTrue(!response.hasErrors());
		Assertions.assertNotNull(response.getRecordProperties());
		for (QueryResponse.RecordProperty recordProperty : response.getRecordProperties()) {
			Assertions.assertNotNull(recordProperty.getId());
			Assertions.assertNotNull(recordProperty.getFieldAdditionalData());
			Assertions.assertNotNull(recordProperty.getFieldProperties());
		}
	}
}
