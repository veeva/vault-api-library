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
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.MdlHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("MetadataRequestComponentTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Metadata object request should")
public class MetaDataRequestComponentTest {

	static final String MDL_RECREATE_SCRIPT = MdlHelper.getMdlRecreateScript();
	static final String MDL_ALTER_SCRIPT = MdlHelper.getMdlAlterScript();
	static final String MDL_DROP_SCRIPT = MdlHelper.getMdlDropScript();
	static int jobId;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@AfterAll
	static void teardown() {
		VaultResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(MDL_DROP_SCRIPT)
				.executeMDLScript();

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully execute an MDL script on a Vault")
	public void testExecuteMdlScript() {
		VaultResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(MDL_RECREATE_SCRIPT)
				.executeMDLScript();

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@Order(2)
	@DisplayName("successfully start an asynchronous MDL script execution")
	public void testExecuteMdlScriptAsynchronously() {
		JobCreateResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.setRequestString(MDL_ALTER_SCRIPT)
				.executeMDLScriptAsynchronously();

		Assertions.assertTrue(response.isSuccessful());
		jobId = response.getJobId();
	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve the results of an asynchronous MDL script execution")
	public void testRetrieveAsynchronousMdlScriptResults() {
		boolean status = JobStatusHelper.checkMdlJobCompletion(vaultClient, jobId);
		Assertions.assertTrue(status);

		MdlResponse jobResultResponse = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveAsynchronousMDLScriptResults(String.valueOf(jobId));

		Assertions.assertTrue(jobResultResponse.isSuccessful());
	}

	@Test
	@DisplayName("successfully retrieve metadata of all component types a Vault")
	public void testRetrieveAllComponentMetadata() {
		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveAllComponentMetadata();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@DisplayName("successfully retrieve metadata of a specific component type")
	public void testRetrieveComponentTypeMetadata() {
		MetaDataComponentTypeResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentTypeMetadata("Tab");
		Assertions.assertTrue(response.isSuccessful());

		ComponentType comp = response.getData();
		Assertions.assertNotNull(comp);

		Assertions.assertNotNull(comp.getName());
	}

	@Test
	@Disabled
	public void testFailureAsynchMdlExecution() {
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
	@Disabled
	public void testCancelHvoDeployment() {
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
	@DisplayName("successfully retrieve all records for a specific component type")
	public void testRetrieveComponentRecords() {
		MetaDataComponentTypeBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveComponentRecords("Picklist");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@DisplayName("successfully retrieve metadata of a specific component record as JSON or XML")
	public void testRetrieveComponentRecordXmlJson() {
		MetaDataComponentRecordResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentRecordXmlJson("Picklist", "language__v");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@DisplayName("successfully retrieve metadata of a specific component record as MDL")
	public void testRetrieveComponentRecordMdl() {
		MdlResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveComponentRecordMdl("Picklist", "language__v");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}
}
