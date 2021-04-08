/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ComponentType;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("MetadataRequestComponent")
@ExtendWith(VaultClientParameterResolver.class)
public class MetaDataRequestComponentTest {

	@Test
	public void testGetAllComponents(VaultClient vaultClient) {
		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveComponentTypes();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testGetComponent(VaultClient vaultClient) {
		MetaDataComponentTypeResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentTypeMetadata("Picklist");
		Assertions.assertTrue(response.isSuccessful());

		ComponentType comp = response.getData();
		Assertions.assertNotNull(comp);

		Assertions.assertNotNull(comp.getName());
	}

	@Test
	public void testGetMdl(VaultClient vaultClient) {
		System.out.println("\n****** Get Mdl ******");
		MdlResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentRecordMdl("Picklist","group_types__sys");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testExecuteMdl(VaultClient vaultClient) {
		String mdl = "ALTER Object person__sys (order(0));";
		System.out.println(mdl);
		VaultResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(mdl)
				.executeMDLScript();
		System.out.println(response.getResponseStatus());
		System.out.println("Test Complete...");
	}

	@Test
	public void testGetComponentRecords(VaultClient vaultClient) {
		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveComponentRecords("Picklist");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testGetComponentRecord(VaultClient vaultClient) {
		MetaDataComponentRecordResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentRecordXmlJson("Picklist", "language__v");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
}
