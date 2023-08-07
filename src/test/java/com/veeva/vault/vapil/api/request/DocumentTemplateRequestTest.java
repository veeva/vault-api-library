/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentTemplate;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


@Tag("DocumentTemplateRequest")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class DocumentTemplateRequestTest {

	// From your test Vault, find a document type to use for creation of document templates
	public String DOCUMENT_TYPE = "general__c";
	public String DOCUMENT_TEMPLATE_LABEL1 = "Sample Document Template1";
	public String DOCUMENT_TEMPLATE_LABEL2 = "Sample Document Template2";
	public String DOCUMENT_TEMPLATE_LABEL1_UPDATED = "Sample Document Template1 Updated";
	public String DOCUMENT_TEMPLATE_LABEL2_UPDATED = "Sample Document Template2 Updated";
	public String DOCUMENT_TEMPLATE_NAME1 = "sample_document_template1__c";
	public String DOCUMENT_TEMPLATE_NAME2 = "sample_document_template2__c";
	public String DOCUMENT_TEMPLATE_NAME1_UPDATED = "sample_document_template1_updated__c";
	public String DOCUMENT_TEMPLATE_NAME2_UPDATED = "sample_document_template2_updated__c";
	
	@Test
	public void testRetrieveDocumentTemplateMetadata(VaultClient vaultClient) {
		DocumentTemplateMetadataResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateMetadata();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentTemplateCollection(VaultClient vaultClient) {
		DocumentTemplatesResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateCollection();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testRetrieveDocumentTemplateAttributes(VaultClient vaultClient) {
		DocumentTemplatesResponse templates = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateCollection();

		String templateName = templates.getData().get(0).getName();
		DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateAttributes(templateName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	// Run Manually
	@Test
	public void testDownloadDocumentTemplateFile(VaultClient vaultClient) {
		// Get a template to use
		DocumentTemplatesResponse templates = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateCollection();

		String templateName = templates.getData().get(0).getName();
		String outputPath = "";

		VaultResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.setOutputPath(outputPath)
				.downloadDocumentTemplateFile(templateName);

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testDownloadDocumentTemplateBytes(VaultClient vaultClient) {
		// Get a template to use
		DocumentTemplatesResponse templates = vaultClient.newRequest(DocumentTemplateRequest.class)
				.retrieveDocumentTemplateCollection();

		String templateName = templates.getData().get(0).getName();

		VaultResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.setOutputPath(null)
				.downloadDocumentTemplateFile(templateName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());

	}

	// Run Manually
	@Test
	public void testCreateSingleDocumentTemplateFile(VaultClient vaultClient) {
		DocumentTemplate documentTemplate = new DocumentTemplate();
		documentTemplate.setLabel(DOCUMENT_TEMPLATE_LABEL1);
		documentTemplate.setType(DOCUMENT_TYPE);
		documentTemplate.setActive(true);

		String inputPath = "";
		DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.setInputPath(inputPath)
				.createSingleDocumentTemplate(documentTemplate);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getName());
	}

	@Test
	public void testCreateSingleDocumentTemplateBytes(VaultClient vaultClient) {
		DocumentTemplate documentTemplate = new DocumentTemplate();
		documentTemplate.setLabel(DOCUMENT_TEMPLATE_LABEL1);
		documentTemplate.setType(DOCUMENT_TYPE);
		documentTemplate.setActive(true);

		DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.setBinaryFile("DocTemplate.rtf", getRTFContent().getBytes())
				.createSingleDocumentTemplate(documentTemplate);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getName());
	}

	// Run Manually
	@Test
	public void testCreateMultipleDocumentTemplatesCSVFile(VaultClient vaultClient) {
		String filePath = "";

		String templateFileName1 = "";
		String templateFileName2 = "";

		// CSV
		StringBuilder csvText = new StringBuilder("file,name__v,label__v,type__v,subtype__v,classification__v,active__v,is_controlled__v\n");
		csvText.append(templateFileName1 + ",," + DOCUMENT_TEMPLATE_LABEL1 + "," + DOCUMENT_TYPE + ",,,true,\n");
		csvText.append(templateFileName2 + ",," + DOCUMENT_TEMPLATE_LABEL2 + "," + DOCUMENT_TYPE + ",,,true,");


		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setInputPath(filePath);
		DocumentTemplateBulkResponse response = request.createMultipleDocumentTemplates();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testCreateMultipleDocumentTemplatesBytes(VaultClient vaultClient) {
		String fileName = "";

		String templateFileName1 = "";
		String templateFileName2 = "";

		// CSV
		StringBuilder csvText = new StringBuilder("file,name__v,label__v,type__v,subtype__v,classification__v,active__v,is_controlled__v\n");
		csvText.append(templateFileName1 + ",," + DOCUMENT_TEMPLATE_LABEL1 + "," + DOCUMENT_TYPE + ",,,true,\n");
		csvText.append(templateFileName2 + ",," + DOCUMENT_TEMPLATE_LABEL2 + "," + DOCUMENT_TYPE + ",,,true,");

		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setBinaryFile(fileName, csvText.toString().getBytes());
		DocumentTemplateBulkResponse response = request.createMultipleDocumentTemplates();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testCreateMultipleDocumentTemplatesJSON(VaultClient vaultClient) {
		String templateFileName1 = "";
		String templateFileName2 = "";

		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"file\": \"" + templateFileName1 + "\",\"name__v\": \"\",\"label__v\": \"" + DOCUMENT_TEMPLATE_LABEL1 + "\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\",\"is_controlled__v\": \"\"},");
		jsonString.append("{\"file\": \"" + templateFileName2 + "\",\"name__v\": \"\",\"label__v\": \"" + DOCUMENT_TEMPLATE_LABEL2 + "\",\"type__v\": \"" + DOCUMENT_TYPE + "\",\"subtype__v\": \"\",\"classification__v\": \"\",\"active__v\": \"true\",\"is_controlled__v\": \"\"}");
		jsonString.append("]");


		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		DocumentTemplateBulkResponse response = request.createMultipleDocumentTemplates();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testUpdateMultipleDocumentTemplatesCSVFIle(VaultClient vaultClient) {
		String filePath = "";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,new_name,label__v,active__v\n");
		csvText.append(DOCUMENT_TEMPLATE_NAME1 + "," + DOCUMENT_TEMPLATE_NAME1_UPDATED + "," + DOCUMENT_TEMPLATE_LABEL1_UPDATED + ",false\n");
		csvText.append(DOCUMENT_TEMPLATE_NAME2 + "," + DOCUMENT_TEMPLATE_NAME2_UPDATED + "," + DOCUMENT_TEMPLATE_LABEL2_UPDATED + ",false");

		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setInputPath(filePath);
		DocumentTemplateBulkResponse response = request.updateMultipleDocumentTemplates();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	// Test Manually
	@Test
	public void testUpdateMultipleDocumentTemplatesCSVBytes(VaultClient vaultClient) {
		String fileName = "";

		// CSV
		StringBuilder csvText = new StringBuilder("name__v,new_name,label__v,active__v\n");
		csvText.append(DOCUMENT_TEMPLATE_NAME1 + "," + DOCUMENT_TEMPLATE_NAME1_UPDATED + "," + DOCUMENT_TEMPLATE_LABEL1_UPDATED + ",false\n");
		csvText.append(DOCUMENT_TEMPLATE_NAME2 + "," + DOCUMENT_TEMPLATE_NAME2_UPDATED + "," + DOCUMENT_TEMPLATE_LABEL2_UPDATED + ",false");

		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setBinaryFile(fileName, csvText.toString().getBytes());
		DocumentTemplateBulkResponse response = request.updateMultipleDocumentTemplates();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}

	@Test
	public void testUpdateMultipleDocumentTemplatesJSON(VaultClient vaultClient) {
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("[");
		jsonString.append("{\"name__v\": \"" + DOCUMENT_TEMPLATE_NAME1 + "\",\"new_name\": \""+ DOCUMENT_TEMPLATE_NAME1_UPDATED + "\",\"label__v\": \"" + DOCUMENT_TEMPLATE_LABEL1_UPDATED + "\",\"active__v\": \"false\"},");
		jsonString.append("{\"name__v\": \"" + DOCUMENT_TEMPLATE_NAME2 + "\",\"new_name\": \""+ DOCUMENT_TEMPLATE_NAME2_UPDATED + "\",\"label__v\": \"" + DOCUMENT_TEMPLATE_LABEL2_UPDATED + "\",\"active__v\": \"false\"}");
		jsonString.append("]");


		DocumentTemplateRequest request = vaultClient.newRequest(DocumentTemplateRequest.class);
		request.setContentTypeJson();
		request.setRequestString(jsonString.toString());
		DocumentTemplateBulkResponse response = request.updateMultipleDocumentTemplates();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}


	// Test Manually
	@Test
	public void testDeleteBasicDocumentTemplate(VaultClient vaultClient) {
		String documentTemplateName = "";

		DocumentTemplateResponse response = vaultClient.newRequest(DocumentTemplateRequest.class)
				.deleteBasicDocumentTemplate(documentTemplateName);
		Assertions.assertTrue(response.isSuccessful());
	}


	public String getRTFContent() {
		String fileContent = "{\\rtf1\\ansi\\ansicpg1252\\cocoartf2512\n" +
				"\\cocoatextscaling0\\cocoaplatform0{\\fonttbl\\f0\\froman\\fcharset0 Times-Italic;\\f1\\froman\\fcharset0 Times-Roman;}\n" +
				"{\\colortbl;\\red255\\green255\\blue255;}\n" +
				"{\\*\\expandedcolortbl;;}\n" +
				"\\margl1440\\margr1440\\vieww21600\\viewh16800\\viewkind0\n" +
				"\\deftab720\n" +
				"\\pard\\pardeftab720\\qc\\partightenfactor0\n" +
				"\n" +
				"\\f0\\i\\fs34 \\cf0 \\expnd0\\expndtw0\\kerning0\n" +
				"\\ul \\ulc0 Sample Document Template\n" +
				"\\f1\\i0\\fs24 \\kerning1\\expnd0\\expndtw0 \\ulnone \\\n" +
				"\\pard\\pardeftab720\\partightenfactor0\n" +
				"\n" +
				"\\fs22 \\cf0 \\expnd0\\expndtw0\\kerning0\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"<Insert Date Here>\n" +
				"\\fs24 \\kerning1\\expnd0\\expndtw0 \\\n" +
				"\n" +
				"\\fs22 \\expnd0\\expndtw0\\kerning0\n" +
				"\\\n" +
				"<Insert Address Here>\n" +
				"\\fs24 \\kerning1\\expnd0\\expndtw0 \\\n" +
				"\n" +
				"\\fs22 \\expnd0\\expndtw0\\kerning0\n" +
				"\\\n" +
				"\\\n" +
				"To whom it may concern:\n" +
				"\\fs24 \\kerning1\\expnd0\\expndtw0 \\\n" +
				"\n" +
				"\\fs22 \\expnd0\\expndtw0\\kerning0\n" +
				"\\\n" +
				"\\\n" +
				"<Insert text here>\n" +
				"\\fs24 \\kerning1\\expnd0\\expndtw0 \\\n" +
				"\n" +
				"\\fs22 \\expnd0\\expndtw0\\kerning0\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"\\\n" +
				"<Insert Signature Here>\n" +
				"\\fs24 \\kerning1\\expnd0\\expndtw0 \\\n" +
				"}";

		return fileContent;
	}
}
