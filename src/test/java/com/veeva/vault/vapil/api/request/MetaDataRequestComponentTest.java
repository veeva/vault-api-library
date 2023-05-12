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

import java.util.HashMap;

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
				.retrieveComponentTypeMetadata("Tab");
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
	public void testAsyncMdlExecution(VaultClient vaultClient) {
		String mdl = "ALTER Object hvo_test_object__c (MODIFY Field test_field__c (max_length(1000)));";
		System.out.println(mdl);
		JobCreateResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(mdl)
				.executeMDLScriptAsynchronously();

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testSuccessfulAsynchMdlExecution(VaultClient vaultClient) {
		String mdl = "ALTER Object hvo_test_object__c (MODIFY Field test_field__c (max_length(1000)));";
		System.out.println(mdl);
		JobCreateResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(mdl)
				.executeMDLScriptAsynchronously();

		try {
			Thread.sleep(5000);
			MdlResponse jobResultResponse = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveAsynchronousMDLScriptResults(response.getJobId().toString());

			System.out.println(jobResultResponse.getResponseStatus());
			Assertions.assertTrue(jobResultResponse.isSuccessful());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testFailureAsynchMdlExecution(VaultClient vaultClient) {
		String mdl = "ALTER Object hvo_test_object__c (MODIFY Field test_field__c (max_length(1000000000)));";
		System.out.println(mdl);
		JobCreateResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(mdl)
				.executeMDLScriptAsynchronously();

		try {
			Thread.sleep(5000);
			MdlResponse jobResultResponse = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveAsynchronousMDLScriptResults(response.getJobId().toString());

			System.out.println(jobResultResponse.getResponseStatus());
			Assertions.assertFalse(jobResultResponse.isSuccessful());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testCancelHvoDeployment(VaultClient vaultClient) {
		String mdl = "ALTER Object hvo_test_object__c(\n" +
				"    ADD Field test_picklist__c(\n" +
				"      label('Test Picklist'),\n" +
				"      type('Picklist'),\n" +
				"      active(true),\n" +
				"      required(false),\n" +
				"      unique(false),\n" +
				"      help_content(),\n" +
				"      list_column(false),\n" +
				"      order(11),\n" +
				"      multi_value(false),\n" +
				"      picklist('Picklist.test_picklist__c'),\n" +
				"      no_copy(false),\n" +
				"      lookup_relationship_name(),\n" +
				"      lookup_source_field()\n" +
				"    ),\n" +
				"    ADD Field vapil_object__c(\n" +
				"      label('Vapil Object'),\n" +
				"      type('Object'),\n" +
				"      active(true),\n" +
				"      required(false),\n" +
				"      unique(false),\n" +
				"      help_content(),\n" +
				"      list_column(false),\n" +
				"      create_object_inline(false),\n" +
				"      order(9),\n" +
				"      object('vapil_test_import_validate__c'),\n" +
				"      relationship_type('reference'),\n" +
				"      relationship_outbound_name('vapil_object__cr'),\n" +
				"      relationship_inbound_name('hvo_test_objects__cr'),\n" +
				"      relationship_inbound_label('Test Objects'),\n" +
				"      relationship_deletion('block'),\n" +
				"      relationship_criteria(),\n" +
				"      no_copy(false),\n" +
				"      lookup_relationship_name(),\n" +
				"      lookup_source_field(),\n" +
				"      secure_relationship(false)\n" +
				"    )\n" +
				");";
		System.out.println(mdl);
		JobCreateResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(mdl)
				.executeMDLScriptAsynchronously();
		System.out.println(response.getResponse());

		try {
			Thread.sleep(1000);
			VaultResponse cancelResponse = vaultClient.newRequest(MetaDataRequest.class)
					.cancelHvoDeployment("hvo_test_object__c");

			Assertions.assertTrue(cancelResponse.isSuccessful());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testGetComponentRecords(VaultClient vaultClient) {
		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveComponentRecords("Link");
//		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveComponentRecords("Picklist");

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
