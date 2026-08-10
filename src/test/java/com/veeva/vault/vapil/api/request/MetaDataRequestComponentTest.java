/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ComponentRecord;
import com.veeva.vault.vapil.api.model.common.ComponentType;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.ComponentContentResponse.ComponentContent;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.MetadataRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("MetadataRequestComponentTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Metadata request should")
public class MetaDataRequestComponentTest {

	private static final String PATH_MDL_RECREATE_SCRIPT = MetadataRequestHelper.PATH_MDL_RECREATE_SCRIPT;
	private static final String PATH_MDL_ALTER_SCRIPT = MetadataRequestHelper.PATH_MDL_ALTER_SCRIPT;
	private static final String RAW_OBJECT_NAME = "test_raw_object__c";
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully execute an MDL Script")
	class TestExecuteMdlScript {
		MdlExecuteResponse response = null;
		String alterScript;

		@BeforeAll
		public void setup() throws IOException {
			File alterScriptFile = new File(PATH_MDL_ALTER_SCRIPT);
			alterScript = new String(
					Files.readAllBytes(alterScriptFile.toPath()), StandardCharsets.UTF_8);
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.setRequestString(alterScript)
					.executeMDLScript();

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());

			assertNotNull(response.getScriptExecution());
			assertNotNull(response.getScriptExecution().getCode());
			assertNotNull(response.getScriptExecution().getMessage());
			assertNotNull(response.getScriptExecution().getWarnings());
			assertNotNull(response.getScriptExecution().getFailures());
			assertNotNull(response.getScriptExecution().getExceptions());
			assertNotNull(response.getScriptExecution().getComponentsAffected());
			assertNotNull(response.getScriptExecution().getExecutionTime());

			List<MdlExecuteResponse.StatementExecution> statementExecutions = response.getStatementExecution();
			assertNotNull(statementExecutions);
			for (MdlExecuteResponse.StatementExecution statementExecution : statementExecutions) {
				assertNotNull(statementExecution.getVault());
				assertNotNull(statementExecution.getStatement());
				assertNotNull(statementExecution.getCommand());
				assertNotNull(statementExecution.getComponent());
				assertNotNull(statementExecution.getMessage());
				assertNotNull(statementExecution.getResponse());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully execute an MDL Script asynchronously")
	class TestExecuteMdlScriptAsynchronously {
		MdlExecuteAsyncResponse response = null;
		String alterScript;
		int jobId;

		@BeforeAll
		public void setup() throws IOException {
			File alterScriptFile = new File(PATH_MDL_ALTER_SCRIPT);
			alterScript = new String(
					Files.readAllBytes(alterScriptFile.toPath()), StandardCharsets.UTF_8);
		}

		@AfterAll
		public void teardown() {
			assertTrue(JobStatusHelper.checkMdlJobCompletion(vaultClient, jobId));
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.setRequestString(alterScript)
					.executeMDLScriptAsynchronously();

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getJobId());
			assertNotNull(response.getUrl());
			assertNotNull(response.getScriptExecution().getCode());
			assertNotNull(response.getScriptExecution().getMessage());
			assertNotNull(response.getScriptExecution().getWarnings());
			assertNotNull(response.getScriptExecution().getFailures());
			assertNotNull(response.getScriptExecution().getExceptions());
			assertNotNull(response.getScriptExecution().getComponentsAffected());
			assertNotNull(response.getScriptExecution().getExecutionTime());
			jobId = response.getJobId();
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("unsuccessfully execute an MDL Script asynchronously")
	class TestExecuteMdlScriptAsynchronouslyFailure {
		MdlExecuteAsyncResponse response = null;
		String alterScript = "ALTER Object vapil_test_object__c (" +
				"MODIFY Field test_field__c (" +
				"max_length(1000000000)" +
				"));";
		int jobId;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.setRequestString(alterScript)
					.executeMDLScriptAsynchronously();

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getJobId());
			assertNotNull(response.getUrl());
			jobId = response.getJobId();
			assertFalse(JobStatusHelper.checkMdlJobCompletion(vaultClient, jobId));
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve asynchronous MDL Script results")
	class TestRetrieveAsynchronousMdlScriptResults {
		MdlExecuteResponse response = null;
		String alterScript;
		int jobId;

		@BeforeAll
		public void setup() throws IOException {
			File alterScriptFile = new File(PATH_MDL_ALTER_SCRIPT);
			alterScript = new String(
					Files.readAllBytes(alterScriptFile.toPath()), StandardCharsets.UTF_8);

			MdlExecuteAsyncResponse response = vaultClient.newRequest(MetaDataRequest.class)
					.setRequestString(alterScript)
					.executeMDLScriptAsynchronously();

			assertNotNull(response);
			assertTrue(response.isSuccessful());
			jobId = response.getJobId();
			assertTrue(JobStatusHelper.checkMdlJobCompletion(vaultClient, jobId));
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveAsynchronousMDLScriptResults(String.valueOf(jobId));

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());

			assertNotNull(response.getScriptExecution());
			assertNotNull(response.getScriptExecution().getCode());
			assertNotNull(response.getScriptExecution().getMessage());
			assertNotNull(response.getScriptExecution().getWarnings());
			assertNotNull(response.getScriptExecution().getFailures());
			assertNotNull(response.getScriptExecution().getExceptions());
			assertNotNull(response.getScriptExecution().getComponentsAffected());
			assertNotNull(response.getScriptExecution().getExecutionTime());

			List<MdlExecuteResponse.StatementExecution> statementExecutions = response.getStatementExecution();
			assertNotNull(statementExecutions);
			for (MdlExecuteResponse.StatementExecution statementExecution : statementExecutions) {
				assertNotNull(statementExecution.getVault());
				assertNotNull(statementExecution.getStatement());
				assertNotNull(statementExecution.getCommand());
				assertNotNull(statementExecution.getComponent());
				assertNotNull(statementExecution.getMessage());
				assertNotNull(statementExecution.getResponse());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve metadata of all component types in a Vault")
	class TestRetrieveAllComponentMetadata {
		MetaDataComponentTypeBulkResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveAllComponentMetadata();

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<ComponentType> data = response.getData();
			assertNotNull(data);
			for (ComponentType componentType : data) {
				assertNotNull(componentType.getName());
				assertNotNull(componentType.getLabel());
				assertNotNull(componentType.getActive());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve metadata of a specific component type")
	class TestRetrieveComponentTypeMetadata {
		MetaDataComponentTypeResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveComponentTypeMetadata("Tab");

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			ComponentType componentType = response.getData();
			assertNotNull(componentType);
			assertNotNull(componentType.getName());
			assertNotNull(componentType.getClass());
			assertNotNull(componentType.getActive());

			List<ComponentType.Attribute> attributes = componentType.getAttributes();
			assertNotNull(attributes);
			for (ComponentType.Attribute attribute : attributes) {
				assertNotNull(attribute.getName());
				assertNotNull(attribute.getType());
				assertNotNull(attribute.getRequiredness());
				assertNotNull(attribute.getEditable());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Disabled("Requires the job to be in 'IN_DEPLOYMENT' status. Test needs to be rewritten to handle")
	@DisplayName("successfully deployment of a raw object")
	class TestCancelRawObjectDeployment {
		VaultResponse response = null;
		String recreateScript;
		int jobId;

		@BeforeAll
		public void setup() throws IOException, InterruptedException {
			File recreateScriptFile = new File(PATH_MDL_RECREATE_SCRIPT);
			recreateScript = new String(
					Files.readAllBytes(recreateScriptFile.toPath()), StandardCharsets.UTF_8);

			MdlExecuteAsyncResponse response = vaultClient.newRequest(MetaDataRequest.class)
					.setRequestString(recreateScript)
					.executeMDLScriptAsynchronously();

			assertNotNull(response);
			assertTrue(response.isSuccessful());
			jobId = response.getJobId();
			Thread.sleep(1000);
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.cancelRawObjectDeployment(RAW_OBJECT_NAME);

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
	@DisplayName("successfully retrieve all records for a specific component type")
	class TestComponentRecordCollection {
		MetaDataComponentTypeBulkResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveComponentRecords("Objectvalidation.vapil_test_object__c");

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<ComponentType> data = response.getData();
			assertNotNull(data);
			for (ComponentType componentType : data) {
				assertNotNull(componentType.getName());
				assertNotNull(componentType.getLabel());
				assertNotNull(componentType.getActive());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve metadata of a specific component record as JSON or XML")
	class TestRetrieveComponentRecordXmlJson {
		MetaDataComponentRecordResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveComponentRecordXmlJson("Doctype", "vapil_test_doc_type__c");

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			ComponentRecord data = response.getData();
			assertNotNull(data);
			assertNotNull(data.getName());
			assertNotNull(data.getActive());
			assertNotNull(data.getLabel());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve metadata of a specific component record as MDL")
	class TestRetrieveComponentRecordMdl {
		MdlResponse response = null;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveComponentRecordMdl("Picklist", "language__v");

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getComponentType());
			assertNotNull(response.getCommandType());
			assertNotNull(response.getRecordName());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve the content file for a Reportexceltemplate component record")
	class TestRetrieveContentFile {
		ComponentContentResponse response = null;
		String recordName;

		@BeforeAll
		public void setup() {
			MetaDataComponentTypeBulkResponse recordsResponse = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveComponentRecords("Reportexceltemplate");

			assertNotNull(recordsResponse);
			assertTrue(recordsResponse.isSuccessful());
			List<ComponentType> data = recordsResponse.getData();
			assertNotNull(data);
			assertFalse(data.isEmpty());
			recordName = data.get(0).getName();
			assertNotNull(recordName);
		}

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(MetaDataRequest.class)
					.retrieveContentFile("Reportexceltemplate", recordName);

			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			List<ComponentContent> data = response.getData();
			assertNotNull(data);
			for (ComponentContent content : data) {
				assertNotNull(content.getFormat());
				assertNotNull(content.getName());
				assertNotNull(content.getOriginalName());
				assertNotNull(content.getSha1Checksum());
				assertNotNull(content.getSize());
			}
		}
	}
}
