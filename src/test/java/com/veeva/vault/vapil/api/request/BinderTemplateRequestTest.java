package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;;
import com.veeva.vault.vapil.extension.BinderTemplateHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import com.veeva.vault.vapil.api.model.common.BinderTemplate;
import com.veeva.vault.vapil.api.model.response.BinderTemplateBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateMetadataResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateNodeBulkResponse;
import com.veeva.vault.vapil.api.model.response.BinderTemplateResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Tag("BinderTemplateRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Binder template request should")
public class BinderTemplateRequestTest {
	private static String DOCUMENT_TYPE = "general__c";
	private static final String DOC_TYPE_NAME = "vapil_test_doc_type__c";
	private static final String DOC_SUBTYPE_NAME = "vapil_test_doc_subtype__c";
	private static final String DOC_CLASSIFICATION_NAME = "vapil_test_doc_classification__c";
	static List<String> templateNames = new ArrayList<>();
	static String templateNodeId;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully create a binder template")
	public void testCreateBinderTemplate() {
		BinderTemplate template = new BinderTemplate();
		template.setLabel("VAPIL Test Binder Template 1");
		template.setType(DOC_TYPE_NAME);
		template.setSubType(DOC_SUBTYPE_NAME);
		template.setClassification(DOC_CLASSIFICATION_NAME);
		template.setActive(true);
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.createBinderTemplate(template);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotNull(response.getData().get(0).getName());
		templateNames.add(response.getData().get(0).getName());
	}

	@Test
	@Order(2)
	@DisplayName("successfully retrieve binder template attributes")
	public void testRetrieveBinderTemplateAttributes() {
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateAttributes(templateNames.get(0));

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		for (BinderTemplate template : response.getData()) {
			Assertions.assertNotNull(template.getName());
			Assertions.assertNotNull(template.getLabel());
			Assertions.assertNotNull(template.getActive());
		}
	}

	@Test
	@Order(3)
	@DisplayName("successfully create a binder template node")
	public void testCreateBinderTemplateNode() {

		// Create a root node in the binder
		BinderTemplate templateNode = new BinderTemplate();
		templateNode.setId("01");
		templateNode.setLabel("Section 1");
		templateNode.setParentId("");
		templateNode.setNodeType(BinderTemplate.NodeType.SECTION);

		// Create a child node in the node from above
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.createBinderTemplateNode(templateNames.get(0), templateNode);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData().get(0).getId());
		templateNodeId = response.getData().get(0).getId();

		BinderTemplate templateSubNode = new BinderTemplate();
		templateSubNode.setId("01.01");
		templateSubNode.setLabel("Section 1-1");
		templateSubNode.setParentId(templateNodeId); // Generated id from above
		templateSubNode.setNodeType(BinderTemplate.NodeType.SECTION);

		response = vaultClient.newRequest(BinderTemplateRequest.class)
				.createBinderTemplateNode(templateNames.get(0), templateSubNode);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData().get(0).getId());
	}

	@Test
	@Order(4)
	@DisplayName("successfully retrieve binder template node attributes")
	public void testRetrieveBinderTemplateNodeAttributes() {
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateNodeAttributes(templateNames.get(0));

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		for (BinderTemplate template : response.getData()) {
			Assertions.assertNotNull(template.getId());
			Assertions.assertNotNull(template.getLabel());
			Assertions.assertNotNull(template.getNodeType());
		}
	}

	@Test
	@Order(5)
	@DisplayName("successfully update a binder template")
	public void testUpdateBinderTemplate() {
		BinderTemplate template = new BinderTemplate();
		template.setType(DOC_TYPE_NAME);

		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.updateBinderTemplate(templateNames.get(0), template);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		for (BinderTemplate binderTemplate : response.getData()) {
			Assertions.assertNotNull(binderTemplate.getName());
		}
	}

	@Test
	@Order(6)
	@DisplayName("successfully replace existing binder template nodes")
	public void testReplaceBinderTemplateNodes() {

		// Create a root node in the binder
		BinderTemplate templateNode = new BinderTemplate();
		templateNode.setId(templateNodeId);
		templateNode.setLabel("Section 1");
		templateNode.setParentId("");
		templateNode.setNodeType(BinderTemplate.NodeType.SECTION);

		// Create a child node in the node from above
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.createBinderTemplateNode(templateNames.get(0), templateNode);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData().get(0).getId());
		templateNodeId = response.getData().get(0).getId();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData().get(0).getId());
	}

	@Test
	@Order(7)
	@DisplayName("successfully bulk create binder templates using JSON")
	public void testBulkCreateBinderTemplatesJSON() {
		File jsonFile = new File(BinderTemplateHelper.getJsonPathCreateMultipleBinderTemplates());
		Assertions.assertTrue(jsonFile.exists());
		String jsonString = FileHelper.convertJsonFileToString(jsonFile);

		BinderTemplateBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setContentTypeJson()
				.setRequestString(jsonString)
				.bulkCreateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());

		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertTrue(result.isSuccessful());
			Assertions.assertNotNull(result.getName());
		}
	}

	@Test
	@Order(8)
	@DisplayName("successfully bulk update binder templates using JSON")
	public void testBulkUpdateBinderTemplatesJSON() {
		File jsonFile = new File(BinderTemplateHelper.getJsonPathUpdateMultipleBinderTemplates());
		Assertions.assertTrue(jsonFile.exists());
		String jsonString = FileHelper.convertJsonFileToString(jsonFile);

		BinderTemplateBulkResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.setContentTypeJson()
				.setRequestString(jsonString)
				.bulkUpdateBinderTemplates();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());

		for (BinderTemplateBulkResponse.TemplateResult result : response.getData()) {
			Assertions.assertTrue(result.isSuccessful());
			Assertions.assertNotNull(result.getName());
			templateNames.add(result.getName());
		}
	}

	@Test
	@Order(9)
	@DisplayName("successfully delete a binder template")
	public void testDeleteBinderTemplate() {
		for (String templateName : templateNames) {
			BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
					.deleteBinderTemplate(templateName);

			Assertions.assertTrue(response.isSuccessful());
		}
	}

	@Test
	@DisplayName("successfully retrieve binder template metadata")
	public void testRetrieveBinderTemplateMetadata() {
		BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateMetadata();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@DisplayName("successfully retrieve binder template node metadata")
	public void testRetrieveBinderTemplateNodeMetadata() {
		BinderTemplateMetadataResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateNodeMetadata();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@DisplayName("successfully retrieve binder template collection")
	public void testRetrieveBinderTemplateCollection() {
		BinderTemplateResponse response = vaultClient.newRequest(BinderTemplateRequest.class)
				.retrieveBinderTemplateCollection();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	@Disabled
	public void testReplaceBinderTemplateNodesJSON() {
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

	// Run manually
	@Test
	@Disabled
	public void testBulkCreateBinderTemplatesCSVFile() {

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
	@Disabled
	public void testBulkCreateBinderTemplatesCSVBytes() {

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

	@Test
	@Disabled
	public void testBulkUpdateBinderTemplatesCSVFile() {

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
	@Disabled
	public void testBulkUpdateBinderTemplatesCSVBytes() {

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

	// Run manually
	@Test
	@Disabled
	public void testBulkCreateBinderTemplateNodesCSV() {
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
	@Disabled
	public void testBulkCreateBinderTemplateNodesCSVBytes() {
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
	@Disabled
	public void testBulkCreateBinderTemplateNodesJSON() {
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
	@Disabled
	public void testReplaceBinderTemplateNodesCSVFile() {
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
	@Disabled
	public void testReplaceBinderTemplateNodesCSVBytes() {
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
