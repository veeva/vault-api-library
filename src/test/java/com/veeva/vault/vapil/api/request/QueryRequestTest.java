/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DocumentDeletionResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


@Tag("QueryRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class QueryRequestTest {
	
	@Test
	public void testBasicQuery(VaultClient vaultClient) {
		String query = "select id, username__sys from user__sys";
		QueryResponse resp = vaultClient.newRequest(QueryRequest.class)
				.setDescribeQuery(true)
				.query(query);
		Assertions.assertNotNull(resp);
		Assertions.assertTrue(resp.isSuccessful());
	}
	
	@Test
	public void testManualPagination(VaultClient vaultClient) {
		String query = "select id,name__v,title__v,merge_fields__v,major_version_number__v from documents limit 3";

		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.query(query);
		Assertions.assertNotNull(response);
		Assertions.assertTrue(response.isSuccessful());

		if (response.isPaginated()) {
			QueryResponse paginatedResponse = vaultClient.newRequest(QueryRequest.class)
					.queryByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
			Assertions.assertNotNull(paginatedResponse.getResponseDetails().getSize());
		}
	}

	@Test
	public void testRecordProperties(VaultClient vaultClient) {
		String query = "select id, username__sys from user__sys";
		QueryResponse response = vaultClient.newRequest(QueryRequest.class)
				.setRecordProperties(QueryRequest.RecordPropertyType.ALL)
				.query(query);

		Assertions.assertNotNull(response);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getRecordProperties());
		Assertions.assertNotNull(response.getRecordProperties().get(0).getId());
		Assertions.assertNotNull(response.getRecordProperties().get(0).getFieldAdditionalData());
		Assertions.assertNotNull(response.getRecordProperties().get(0).getFieldProperties());
	}
}
