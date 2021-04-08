package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import com.veeva.vault.vapil.api.model.common.BinderTemplate;
import com.veeva.vault.vapil.api.model.response.BinderTemplateBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateMetadataResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateNodeBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag("BinderTemplateRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class BinderTemplateRequestTest {
	private static String DOCUMENT_TYPE = "general__c";

	@Test
	public void testRetrieveBinderTemplateMetadata(VaultClient vaultClient) {
		BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateMetadata();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveBinderTemplateNodeMetadata(VaultClient vaultClient) {
		BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateNodeMetadata();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveBinderTemplateCollection(VaultClient vaultClient) {
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateCollection();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}


	@Nested
	@DisplayName("Tests that require a template collection")
	class TestTemplateAttributes {

		String templateName;

		// Load Template Collection
		@BeforeEach
		public void beforeEach(VaultClient vaultClient) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.retrieveBinderTemplateCollection();
			templateName = response.getData().get(0).getName();
		}

		@Test
		public void testRetrieveBinderTemplateAttributes(VaultClient vaultClient) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.retrieveBinderTemplateAttributes(templateName);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getData());
		}


		@Test
		public void testRetrieveBinderTemplateNodeAttributes(VaultClient vaultClient) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.retrieveBinderTemplateNodeAttributes(templateName);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getData());
		}

	}

	@Test
	public void testCreateBinderTemplate(VaultClient vaultClient) {
		BinderTemplate template = new BinderTemplate();
		template.setLabel("Binder Template 1");
		template.setType(DOCUMENT_TYPE);
		template.setActive(true);
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
			.createBinderTemplate(template);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Nested
	@DisplayName("Tests that require a binder template")
	class TestBinderTemplate {

		String templateName;

		// Load Template
		@BeforeEach
		public void beforeEach(VaultClient vaultClient) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.retrieveBinderTemplateCollection();
			templateName = response.getData().get(0).getName();
		}

		@Test
		public void testUpdateBinderTemplate(VaultClient vaultClient) {
			BinderTemplate template = new BinderTemplate();
			template.setLabel("Binder Template - Updated");
			template.setActive(false);

			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.updateBinderTemplate(templateName, template);

			Assertions.assertTrue(response.isSuccessful());
		}


		@Test
		public void testCreateBinderTemplateNode(VaultClient vaultClient) {

			// Create a root node in the binder
			BinderTemplate templateNode = new BinderTemplate();
			templateNode.setId("01");
			templateNode.setLabel("Single Root Node 1");
			templateNode.setNumber("01");
			templateNode.setParentId("");
			templateNode.setNodeType(BinderTemplate.NodeType.SECTION);

			// Create a child node in the node from above
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.createBinderTemplateNode(templateName, templateNode);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getData().get(0).getId());

			BinderTemplate templateSubNode = new BinderTemplate();
			templateSubNode.setId("01.01");
			templateSubNode.setLabel("Sub Node 1");
			templateSubNode.setNumber("01.01");
			templateSubNode.setParentId(response.getData().get(0).getId()); // Generated id from above
			templateSubNode.setNodeType(BinderTemplate.NodeType.SECTION);

			response = vaultClient.newRequest(BinderTemplateRequest.class)
					.createBinderTemplateNode(templateName, templateSubNode);

			Assertions.assertTrue(response.isSuccessful());
		}


		@Test
		public void testDeleteBinderTemplate(VaultClient vaultClient) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.deleteBinderTemplate(templateName);
			Assertions.assertTrue(response.isSuccessful());
		}
	}

	// Run manually
	@Test
	public void testBulkCreateBinderTemplatesCSVFile(VaultClient vaultClient) {

		// In this test, the API should create binder_template_1_csv__c and binder_template_2_csv__c
		// which will be used in the other bulk tests and the binder node tests

		String fileName = "binder_template_sample.csv";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,label__v,type__v,subtype__v,classification__v,active__v\n");
		csvText.append("binder_template_1_csv__c,Binder Template 1," + DOCUMENT_TYPE + ",,,true\n");
		csvText.append("binder_template_2_csv__c,Binder Template 2," + DOCUMENT_TYPE + ",,,true\n");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);

		// Test the CSV as a file
		String filePath = saveToDesktop(fileName, csvText.toString());
		request.setInputPath(filePath);
		BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testBulkCreateBinderTemplatesCSVBytes(VaultClient vaultClient) {

		String fileName = "binder_template_sample.csv";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,label__v,type__v,subtype__v,classification__v,active__v\n");
		csvText.append("binder_template_1_bytes__c,Binder Template 1," + DOCUMENT_TYPE + ",,,true\n");
		csvText.append("binder_template_2_bytes__c,Binder Template 2," + DOCUMENT_TYPE + ",,,true\n");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);
		// Test the CSV as bytes
		request.setBinaryFile(fileName, csvText.toString().getBytes());
		BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	// Run manually
	@Test
	public void testBulkCreateBinderTemplatesJSON(VaultClient vaultClient) {

		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"name__v\": \"binder_template_1_json__c\",\"label__v\": \"Binder Template 1\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\"},");
		jsonString.append("{\"name__v\": \"binder_template_2_json__c\",\"label__v\": \"Binder Template 2\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\"}");
		jsonString.append("]");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		BinderTemplateBulkResponse response = request.bulkCreateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}


	@Test
	public void testBulkUpdateBinderTemplatesCSVFile(VaultClient vaultClient) {

		String fileName = "binder_template_sample.csv";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,label__v,active__v\n");
		csvText.append("binder_template_1_csv__c,Binder Template 1 - Updated,true\n");
		csvText.append("binder_template_2_csv__c,Binder Template 2 - Updated,false\n");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);

		// Test the CSV as a file
		String filePath = saveToDesktop("fileName", csvText.toString());
		request.setInputPath(filePath);
		BinderTemplateBulkResponse response = request.bulkUpdateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testBulkUpdateBinderTemplatesCSVBytes(VaultClient vaultClient) {

		String fileName = "binder_template_sample.csv";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,label__v,active__v\n");
		csvText.append("binder_template_1_bytes__c,Binder Template 1 - Updated,true\n");
		csvText.append("binder_template_2_bytes__c,Binder Template 2 - Updated,false\n");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);

		request.setBinaryFile(fileName, csvText.toString().getBytes());
		BinderTemplateBulkResponse response = request.bulkUpdateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testBulkUpdateBinderTemplatesJSON(VaultClient vaultClient) {

		// JSON
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"name__v\": \"binder_template_1_json__c\",\"label__v\": \"Binder Template 1 - Updated\",\"active__v\": \"true\"},");
		jsonString.append("{\"name__v\": \"binder_template_2_json__c\",\"label__v\": \"Binder Template 2 - Updated\",\"active__v\": \"false\"}");
		jsonString.append("]");

		BinderTemplateRequest request = vaultClient.newRequest(BinderTemplateRequest.class);

		// Test the request using JSON input
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		BinderTemplateBulkResponse response = request.bulkUpdateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	// Run manually
	@Test
	public void testBulkCreateBinderTemplateNodesCSV(VaultClient vaultClient) {
		String binderTemplateName = "binder_template_1_csv__c";
		String fileName = "binder_template_node_sample.csv";

		// CSV Root Nodes
		StringBuilder csvText = new StringBuilder();
		csvText.append("id,node_type__v,label__v,order__v,number__v,parent_id__v\n");
		csvText.append("100,section,Root Node 1,1,01,\n");
		csvText.append("200,section,Root Node 2,2,02,\n");
		csvText.append("300,section,Root Node 3,3,03,\n");
		csvText.append("101,section,Sub Node 1,1,01.01,100\n");
		csvText.append("102,section,Sub Node 1,1,02.01,200\n");
		csvText.append("103,section,Sub Node 1,1,03.01,300\n");

		// Test the CSV as a file
		String filePath = saveToDesktop(fileName, csvText.toString());
		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setInputPath(filePath)
				.bulkCreateBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testBulkCreateBinderTemplateNodesCSVBytes(VaultClient vaultClient) {
		String binderTemplateName = "binder_template_1_bytes__c";
		String fileName = "binder_template_node_sample.csv";

		// CSV Root Nodes
		StringBuilder csvText = new StringBuilder();
		csvText.append("id,node_type__v,label__v,order__v,number__v,parent_id__v\n");
		csvText.append("100,section,Root Node 1,1,01,\n");
		csvText.append("200,section,Root Node 2,2,02,\n");
		csvText.append("300,section,Root Node 3,3,03,\n");
		csvText.append("101,section,Sub Node 1,1,01.01,100\n");
		csvText.append("102,section,Sub Node 1,1,02.01,200\n");
		csvText.append("103,section,Sub Node 1,1,03.01,300\n");

		// Test the CSV as bytes
		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setBinaryFile(fileName, csvText.toString().getBytes())
				.bulkCreateBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testBulkCreateBinderTemplateNodesJSON(VaultClient vaultClient) {
		String binderTemplateName = "binder_template_1_json__c";

		// JSON Root Nodes
		StringBuilder jsonText = new StringBuilder();
		jsonText.append("[");
		jsonText.append("{\"id\": \"100\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 1\",\"order__v\": 1,\"number__v\": \"01\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"200\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 2\",\"order__v\": 2,\"number__v\": \"02\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"300\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 3\",\"order__v\": 3,\"number__v\": \"03\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"101\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 1\",\"order__v\": 1,\"number__v\": \"01.01\",\"parent_id__v\": \"100\"},");
		jsonText.append("{\"id\": \"102\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 2\",\"order__v\": 1,\"number__v\": \"01.02\",\"parent_id__v\": \"200\"},");
		jsonText.append("{\"id\": \"103\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 3\",\"order__v\": 1,\"number__v\": \"01.03\",\"parent_id__v\": \"300\"}");
		jsonText.append("]");

		// Test the request using JSON input
		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setContentTypeJson()
				.setRequestString(jsonText.toString())
				.bulkCreateBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	// Run manually
	@Test
	public void testReplaceBinderTemplateNodesCSVFile(VaultClient vaultClient) {
		String binderTemplateName = "binder_template_1_csv__c";
		String fileName = "binder_template_node_sample.csv";

		// CSV Root Nodes
		StringBuilder csvText = new StringBuilder();
		csvText.append("id,node_type__v,label__v,order__v,number__v,parent_id__v\n");
		csvText.append("100,section,Root Node 1 - Replaced,1,01,\n");
		csvText.append("200,section,Root Node 2 - Replaced,2,02,\n");
		csvText.append("300,section,Root Node 3 - Replaced,3,03,\n");
		csvText.append("101,section,Sub Node 1 - Replaced,1,01.01,100\n");
		csvText.append("102,section,Sub Node 1 - Replaced,1,02.01,200\n");
		csvText.append("103,section,Sub Node 1 - Replaced,1,03.01,300\n");


		// Test the CSV as a file
		String filePath = saveToDesktop(fileName, csvText.toString());
		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setInputPath(filePath)
				.replaceBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testReplaceBinderTemplateNodesCSVBytes(VaultClient vaultClient) {
		/**** Very Important ****/
		/** This deletes the entire node tree first, no matter what.  So do not try to use it to replace a single node
		 *  or to update nodes.  Use the create or update endpoints for that.
		 */

		// The testBulkCreateBinderTemplates call should have created binder_template_1_bytes__c

		String binderTemplateName = "binder_template_1_bytes__c";
		String fileName = "binder_template_node_sample.csv";

		// Note that id and parent_id__v values are only used for the API to determine subnode structure during creation.
		// On creation, all ids are replaced by autogenerated values
		// For example, row 1 has id = 100.  After creation, this will be something else.
		// You can use the retrieveBinderTemplateNodeAttributes call to get the real ids

		// CSV Root Nodes
		StringBuilder csvText = new StringBuilder();
		csvText.append("id,node_type__v,label__v,order__v,number__v,parent_id__v\n");
		csvText.append("100,section,Root Node 1 - Replaced,1,01,\n");
		csvText.append("200,section,Root Node 2 - Replaced,2,02,\n");
		csvText.append("300,section,Root Node 3 - Replaced,3,03,\n");
		csvText.append("101,section,Sub Node 1 - Replaced,1,01.01,100\n");
		csvText.append("102,section,Sub Node 1 - Replaced,1,02.01,200\n");
		csvText.append("103,section,Sub Node 1 - Replaced,1,03.01,300\n");

		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setBinaryFile(fileName, csvText.toString().getBytes())
				.replaceBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	@Test
	public void testReplaceBinderTemplateNodesJSON(VaultClient vaultClient) {
		String binderTemplateName = "binder_template_1_json__c";

		// JSON Root Nodes
		StringBuilder jsonText = new StringBuilder();
		jsonText.append("[");
		jsonText.append("{\"id\": \"100\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 1 - Replaced\",\"order__v\": 1,\"number__v\": \"01\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"200\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 2 - Replaced\",\"order__v\": 2,\"number__v\": \"02\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"300\",\"node_type__v\": \"section\",\"label__v\": \"Root Node 3 - Replaced\",\"order__v\": 3,\"number__v\": \"03\",\"parent_id__v\": \"\"},");
		jsonText.append("{\"id\": \"101\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 1 - Replaced\",\"order__v\": 1,\"number__v\": \"01.01\",\"parent_id__v\": \"100\"},");
		jsonText.append("{\"id\": \"102\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 2 - Replaced\",\"order__v\": 1,\"number__v\": \"01.02\",\"parent_id__v\": \"200\"},");
		jsonText.append("{\"id\": \"103\",\"node_type__v\": \"section\",\"label__v\": \"Sub Node 3 - Replaced\",\"order__v\": 1,\"number__v\": \"01.03\",\"parent_id__v\": \"300\"}");
		jsonText.append("]");

		BinderTemplateNodeBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setContentTypeJson()
				.setRequestString(jsonText.toString())
				.replaceBinderTemplateNodes(binderTemplateName);

		Assertions.assertTrue(response.isSuccessful());
		for (BinderTemplateNodeBulkResponse.TemplateNodeResult result : response.getData()) {
			Assertions.assertEquals("SUCCESS", result.getResponseStatus());
		}
	}

	private static String saveToDesktop(String filename, String text) {
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", filename);

		try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
			os.write(text.getBytes());
			return outputFilePath.toString();
		}
		catch (IOException ignored) { }

		return null;
	}
}
