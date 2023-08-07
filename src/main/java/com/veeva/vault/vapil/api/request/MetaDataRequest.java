/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * MetaData Requests
 * <p>
 * Coverage for all endpoints containing:
 * <ul>
 * <li>"/metadata/"</li>
 * <li>"/configuration/"</li>
 * <li>"/mdl/"</li>
 * </ul>
 * <p>
 * MetaData endpoints include all components and object specific
 * components that reside in the Object section of the API documentation.
 * The response formats vary for each meta-data components properties (there
 * are over 35 metadata components). Common/Shared properties are included in the response.
 * Additional properties are parsed in developers reading of JSON/POJO properties.
 *
 * @vapil.apicoverage <ul>
 * <li><a href="https://developer.veevavault.com/api/23.1/retrieve-component-type-metadata">https://developer.veevavault.com/api/23.1/retrieve-component-type-metadata</a></li>
 * <li><a href="https://developer.veevavault.com/api/23.1/#retrieve-object-metadata">https://developer.veevavault.com/api/23.1/#retrieve-object-metadata</a></li>
 * <li><a href="https://developer.veevavault.com/api/23.1/#retrieve-object-field-metadata">https://developer.veevavault.com/api/23.1/#retrieve-object-field-metadata</a></li>
 * </ul>
 */
public class MetaDataRequest extends VaultRequest {
	// API Endpoints
	private static final String URL_COMPONENTS = "/metadata/components";
	private static final String URL_COMPONENT_TYPE = "/metadata/components/{component_type}";
	private static final String URL_CONFIGURATION_COMPONENT_TYPE = "/configuration/{component_type}";
	private static final String URL_MDL_COMPONENT_RECORD = "/mdl/components/{component_type}.{record_name}";
	private static final String URL_MDL_CONTENT_FILES = "mdl/components/{component_type}.{record_name}/files";
	private static final String URL_MDL_EXECUTE = "mdl/execute";
	private static final String URL_MDL_EXECUTE_ASYNC = "mdl/execute_async";
	private static final String URL_MDL_EXECUTE_ASYNC_CANCEL_DEPLOYMENT = "/metadata/vobjects/{object_name}/actions/canceldeployment";
	private static final String URL_MDL_EXECUTE_ASYNC_JOB_STATUS = "mdl/execute_async/{job_id}/results";
	private static final String URL_MDL_UPLOAD_CONTENT_FILE = "/api/mdl/files";
	private static final String URL_CONFIGURATION_COMPONENT_RECORD = "/configuration/{component_type}.{record_name}";
	private static final String URL_OBJECTS = "/metadata/vobjects";
	private static final String URL_OBJECT_NAME = "/metadata/vobjects/{object_name}";
	private static final String URL_OBJECT_FIELD_NAME = "/metadata/vobjects/{object_name}/fields/{object_field_name}";
	private static final String URL_OBJECT_PAGE_LAYOUTS = "/metadata/vobjects/{object_name}/page_layouts";
	private static final String URL_OBJECT_PAGE_LAYOUT_METADATA = "/metadata/vobjects/{object_name}/page_layouts/{layout_name}";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private String headerAccept;
	private Boolean localized;
	private String inputPath;
	private String requestString; // For raw request

	private MetaDataRequest() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		this.localized = false;
	}

	/*
	 *
	 * META DATA - COMPONENTS
	 *
	 */

	/**
	 * <b>Execute MDL Script</b>
	 * <p>
	 * This endpoint executes the given MDL script on a vault.
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/mdl/execute</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#execute-mdl-script' target='_blank'>https://developer.veevavault.com/api/23.1/#execute-mdl-script</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.setRequestString(mdl)
	 * 				.executeMDLScript();</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());</pre>
	 */
	public VaultResponse executeMDLScript() {
		String url = vaultClient.getAPIEndpoint(URL_MDL_EXECUTE, false);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, requestString);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Execute MDL Script Asynchronously</b>
	 * <p>
	 * This asynchronous endpoint executes the given MDL script on a vault.
	 * <p>
	 * While you can execute any MDL script asynchronously, this endpoint is required if you’re operating on 10,000+ high volume object records and executing one of the following operations:
	 * <p>
	 * Enabling lifecycles
	 * Enabling object types
	 * Adding or removing a field
	 * Updating the max length of a text field
	 * Adding or removing an Index
	 * Changing the fields that compose an Index
	 *
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/mdl/execute_asyc</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#execute-mdl-script-asynchronously' target='_blank'>https://developer.veevavault.com/api/23.1/#execute-mdl-script-asynchronously</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.setRequestString(mdl)
	 * 				.executeMDLScriptAsynchronously();</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());</pre>
	 */
	public JobCreateResponse executeMDLScriptAsynchronously() {
		String url = vaultClient.getAPIEndpoint(URL_MDL_EXECUTE_ASYNC, false);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, inputPath);

		if (requestString != null && !requestString.isEmpty())
			request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT, requestString);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Retrieve Asynchronous MDL Script Results</b>
	 * <p>
	 *After submitting a request to deploy an MDL script asynchronously, you can query Vault to determine the results of the request.
	 *
	 * @param jobId id of the job executing the MDL script
	 * @return MdlResponse
	 * @vapil.api <pre>GET /api/mdl/execute_async/{job_id}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-asynchronous-mdl-script-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-asynchronous-mdl-script-results</a>
	 * @vapil.request <pre>
	 * MdlResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 *				.getAsynchronousMDLJobResults(jobId);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());</pre>
	 *
	 */
	public MdlResponse retrieveAsynchronousMDLScriptResults(String jobId) {
		String url = vaultClient.getAPIEndpoint(URL_MDL_EXECUTE_ASYNC_JOB_STATUS, false)
				.replace("{job_id}", jobId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		return send(HttpMethod.GET, request, MdlResponse.class);
	}

	/**
	 * <b>Cancel HVO Deployment</b>
	 * <p>
	 *Cancel a deployment of configuration changes to a high volume object. To use this endpoint, your HVO configuration_state must be IN_DEPLOYMENT.
	 *
	 * @param objectName of the object being converted to an HVO
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/metadata/vobjects/{object_name}/actions/canceldeployment</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#cancel-hvo-deployment' target='_blank'>https://developer.veevavault.com/api/23.1/#cancel-hvo-deployment</a>
	 * @vapil.request <pre>
	 * MdlResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 *				.cancelHvoDeployment(objectName);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());</pre>
	 *
	 */
	public VaultResponse cancelHvoDeployment(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_MDL_EXECUTE_ASYNC_CANCEL_DEPLOYMENT, true)
				.replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Retrieve All Component Metadata</b>
	 * <p>
	 * Retrieve metadata of all component types in your Vault.
	 *
	 * @return MetaDataComponentTypeBulkResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/components</pre>
	 * @vapil.request <pre>
	 * MetaDataComponentTypeBulkResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveAllComponentMetadata();</pre>
	 * @vapil.response <pre>
	 * for (ComponentType c : resp.getData())
	 *   System.out.println(c.getName() + " s" + c.getLabel());</pre>
	 */
	public MetaDataComponentTypeBulkResponse retrieveAllComponentMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_COMPONENTS));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		return send(HttpMethod.GET, request, MetaDataComponentTypeBulkResponse.class);
	}

	/**
	 * <b>Retrieve Component Type Metadata</b>
	 * <p>
	 * Retrieve metadata of a specific component type.
	 * <ul>
	 * <li> If component parameter is empty, the response will return the metadata of all the components in the Vault.</li>
	 * </ul>
	 *
	 * @param component to retrieve
	 * @return MetaDataComponentTypeResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/components/{component_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-component-type-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-component-type-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataComponentTypeResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveComponentTypeMetadata(componentName);</pre>
	 * @vapil.response <pre>
	 * MetaDataComponent comp = resp.getData();
	 *
	 * System.out.println("\nName = " + comp.getName());
	 * System.out.println("Class = " + comp.getClassName());
	 * System.out.println("Abbreviation = " + comp.getAbbreviation());
	 * System.out.println("Active = " + comp.getActive());
	 *
	 * System.out.println("\nAdditional Properties");
	 * System.out.println("------------------------");
	 * for (String s : comp.getProperties().keySet())
	 * System.out.println(s + " = " + comp.getProperties().get(s));
	 *
	 * System.out.println("\nAttributes");
	 * System.out.println("------------------------");
	 * for (Attribute attr : comp.getAttributes()) {
	 *   System.out.println(attr.getName() +  " " + attr.getType());
	 *
	 *   for (String s : attr.getProperties().keySet())
	 *   System.out.println(s + " = " + attr.getProperties().get(s));
	 * }
	 *
	 * System.out.println("\nSubcomponents");
	 * System.out.println("------------------------");
	 * for (SubComponentType subcomp : comp.getSubComponents()) {
	 *   System.out.println("\n" + subcomp.getName());
	 *   System.out.println(" ");
	 *   for (Attribute attr : subcomp.getAttributes()) {
	 *     System.out.println(attr.getName() +  " " + attr.getType());
	 *
	 *     for (String s : attr.getProperties().keySet())
	 *     System.out.println(s + " = " + attr.getProperties().get(s));
	 *   }
	 * }</pre>
	 */
	public MetaDataComponentTypeResponse retrieveComponentTypeMetadata(String component) {
		String url = vaultClient.getAPIEndpoint(URL_COMPONENT_TYPE).replace("{component_type}", component);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		return send(HttpMethod.GET, request, MetaDataComponentTypeResponse.class);
	}

	/*
	 *
	 * META DATA - VOBJECTS
	 *
	 */

	/**
	 * <b>Retrieve Object Metadata</b>
	 * <p>
	 * Retrieve all metadata configured on a standard or custom Vault Object.
	 * Localized strings can be included via the "setLocalized" method in the builder.
	 *
	 * @param objectName Object name to retrieve
	 * @return MetaDataObjectResponse for single object
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects/{object_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataObjectResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveObjectMetadata(objectName);</pre>
	 * @vapil.response <pre>VaultObject objectMetaData = resp.getObject();
	 *
	 * System.out.println("Name = " + objectMetaData.getName());
	 * System.out.println("Help = " + objectMetaData.getHelpContent());
	 * System.out.println("Label = " + objectMetaData.getLabel());
	 * System.out.println("URL Field = " + objectMetaData.getUrls().getField());
	 * System.out.println("URL Record = " + objectMetaData.getUrls().getRecord());
	 * System.out.println("URL List = " + objectMetaData.getUrls().getList());
	 * System.out.println("UserRoleSetupObject = " + objectMetaData.getUserRoleSetupObject());
	 *
	 *
	 * if (objectMetaData.getAvailableLifecycles() != null) {
	 *   for (String s : objectMetaData.getAvailableLifecycles())
	 *   System.out.println("Lifecycle = " + s);
	 *   }
	 *
	 * if (objectMetaData.getRelationships() != null) {
	 *   for (Object.Relationship relationship : objectMetaData.getRelationships())
	 *   System.out.println("Relationship = " + relationship.getRelationshipName() + " " + relationship.getField() + " " + relationship.getObjectReference().getName());
	 *   }
	 *
	 * if (objectMetaData.getUserRoleSetupObject() != null) {
	 *   System.out.println("UserRoleSetupObject URL " + objectMetaData.getUserRoleSetupObject().getUrl());
	 *   System.out.println("UserRoleSetupObject Name " + objectMetaData.getUserRoleSetupObject().getName());
	 *   System.out.println("UserRoleSetupObject Label " + objectMetaData.getUserRoleSetupObject().getLabel());
	 * }</pre>
	 */
	public MetaDataObjectResponse retrieveObjectMetadata(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJECT_NAME).replace("{object_name}", objectName);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (localized != null && localized)
			request.addQueryParam("loc", localized.toString());

		return send(HttpMethod.GET, request, MetaDataObjectResponse.class);
	}


	/**
	 * <b>Retrieve Object Field Metadata</b>
	 * <p>
	 * Localized strings can be included via the "setLocalized" method in the builder
	 *
	 * @param objectName Object name to retrieve
	 * @param fieldName  Field name to retrieve
	 * @return MetaDataObjectFieldResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects/{object_name}/fields/{object_field_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-field-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-field-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataObjectFieldResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveObjectFieldMetaData(objectName, fieldName);</pre>
	 * @vapil.response <pre>VaultObjectField fieldMetaData = resp.getField();
	 *
	 * System.out.println("Name = " + fieldMetaData.getName());
	 * System.out.println("Label = " + fieldMetaData.getLabel());
	 * System.out.println("Required = " + fieldMetaData.getRequired());
	 * System.out.println("Help = " + fieldMetaData.getHelpContent());
	 * System.out.println("Relationship Name = " + fieldMetaData.getLookupRelationshipName());
	 *
	 * if (fieldMetaData.getObjectReference() != null) {
	 *   System.out.println("Object referencer URL " + fieldMetaData.getObjectReference().getUrl());
	 *   System.out.println("Object referencer Name " + fieldMetaData.getObjectReference().getName());
	 *   System.out.println("Object referencer Label " + fieldMetaData.getObjectReference().getLabel());
	 * }</pre>
	 */
	public MetaDataObjectFieldResponse retrieveObjectFieldMetaData(String objectName, String fieldName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJECT_FIELD_NAME);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{object_field_name}", fieldName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (localized != null && localized)
			request.addQueryParam("loc", localized.toString());

		return send(HttpMethod.GET, request, MetaDataObjectFieldResponse.class);
	}

	/**
	 * <b>Retrieve Object Collection</b>
	 * <p>
	 * Localized strings can be included via the "setLocalized" method in the builder
	 *
	 * @return MetaDataObjectBulkResponse for all objects
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-object-collection' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-object-collection</a>
	 * @vapil.request <pre>
	 * MetaDataObjectBulkResponse resp = vaultClient.newRequest(MetaDataRequest.class).retrieveObjectCollection();</pre>
	 * @vapil.response <pre>
	 * for (Object c : resp.getObjects()) {
	 *   System.out.println(c.getName() + " " + c.getLabel() + " " + c.getUrl());
	 * }</pre>
	 */
	public MetaDataObjectBulkResponse retrieveObjectCollection() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_OBJECTS));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (localized != null && localized)
			request.addQueryParam("loc", localized.toString());

		return send(HttpMethod.GET, request, MetaDataObjectBulkResponse.class);
	}

	/*
	 *
	 * META DATA - COMPONENT TYPES
	 *
	 */

	/**
	 * <b>Retrieve Component Record Collection</b>
	 * <p>
	 * Retrieve all records for a specific component type.
	 *
	 * @param component to retrieve
	 * @return MetaDataComponentTypeBulkResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/configuration/{component_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#component-record-collection' target='_blank'>https://developer.veevavault.com/api/23.1/#component-record-collection</a>
	 * @vapil.request <pre>
	 * MetaDataComponentTypeBulkResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveComponentRecords(componentType);</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   for (ComponentType componentType : resp.getData()) {
	 *     System.out.println(component.getName());
	 *     }
	 * }</pre>
	 */
	public MetaDataComponentTypeBulkResponse retrieveComponentRecords(String component) {
		String url = vaultClient.getAPIEndpoint(URL_CONFIGURATION_COMPONENT_TYPE).replace("{component_type}", component);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, MetaDataComponentTypeBulkResponse.class);
	}

	/**
	 * <b>Retrieve Component Record XML/JSON</b>
	 *
	 * @param componentType The component type name (Picklist, Docfield, Doctype, etc.)
	 * @param recordName    The name of the record to retrieve metadata
	 * @return MetaDataComponentRecordResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/configuration/{component_type}.{record_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-component-record-xml-json' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-component-record-xml-json</a>
	 * @vapil.request <pre>
	 * MetaDataComponentRecordResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveComponentRecordXmlJson(componentType, recordName);</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   System.out.println(resp.getData().getName());
	 * }</pre>
	 */
	public MetaDataComponentRecordResponse retrieveComponentRecordXmlJson(String componentType, String recordName) {
		String url = vaultClient.getAPIEndpoint(URL_CONFIGURATION_COMPONENT_RECORD);
		url = url.replace("{component_type}", componentType);
		url = url.replace("{record_name}", recordName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		return send(HttpMethod.GET, request, MetaDataComponentRecordResponse.class);
	}

	/**
	 * <b>Retrieve Component Record MDL</b>
	 *
	 * @param componentType The component type name (Picklist, Docfield, Doctype, etc.)
	 * @param recordName    The name of the record to retrieve metadata
	 * @return MdlResponse
	 * @vapil.api <pre>
	 * GET /api/mdl/components/{component_type}.{record_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-component-record-mdl' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-component-record-mdl</a>
	 * @vapil.request <pre>
	 * MdlResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 				.retrieveComponentRecordMdl("Picklist","test__c");</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   System.out.println(new String(resp.getBinaryContent()));
	 * }</pre>
	 */
	public MdlResponse retrieveComponentRecordMdl(String componentType, String recordName) {
		String url = vaultClient.getAPIEndpoint(URL_MDL_COMPONENT_RECORD, false);
		url = url.replace("{component_type}", componentType);
		url = url.replace("{record_name}", recordName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		MdlResponse response = sendReturnBinary(HttpMethod.GET, request, MdlResponse.class);
		if (response != null) {
			response.setComponentType(componentType);
			response.setRecordName(recordName);
		}
		return response;
	}

	/**
	 * <b>Upload Content File</b>
	 * <p>
	 * This endpoint allows you to upload a content file to be referenced by a component.
	 * <p>
	 * Once uploaded, Vault stores the file in a generic files staging area
	 * where they will remain until referenced by a component.
	 * Once referenced, Vault cannot access the named file from the staging area.
	 *
	 * @return ComponentContentResponse
	 * @vapil.api <pre>
	 * POST /api/mdl/files</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upload-content-file' target='_blank'>https://developer.veevavault.com/api/23.1/#upload-content-file</a>
	 */
	public ComponentContentResponse uploadContentFile() {
		String url = vaultClient.getAPIEndpoint(URL_MDL_UPLOAD_CONTENT_FILE, false);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT);

		if (inputPath != null && !inputPath.isEmpty()) {
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
			request.addFileMultiPart("file", inputPath);
		}

		if (binaryFile != null) {
			request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}


		return send(HttpMethod.POST, request, ComponentContentResponse.class);
	}

	/**
	 * <b>Retrieve Content File</b>
	 *
	 * @param componentType vault component type
	 * @param recordName    api name of component
	 * @return ComponentContentResponse
	 * @vapil.api <pre>
	 * GET /api/mdl/components/{component_type}.{record_name}/files</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-content-file' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-content-file</a>
	 */
	public ComponentContentResponse retrieveContentFile(String componentType, String recordName) {
		String url = vaultClient.getAPIEndpoint(URL_MDL_CONTENT_FILES, false);
		url = url.replace("{component_type}", componentType);
		url = url.replace("{record_name}", recordName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_PLAINTEXT);

		return send(HttpMethod.GET, request, ComponentContentResponse.class);
	}

	/**
	 * <b>Retrieve Page Layouts</b>
	 * <p>Given an object, retrieve all page layouts associated with that object.
	 * @param objectName Vault object name
	 * @return MetaDataObjectPageLayoutResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects/{object_name}/page_layouts</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-page-layouts' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-page-layouts</a>
	 * @vapil.request <pre>
	 * MetaDataObjectPageLayoutResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 *  	.retrievePageLayouts("user__sys");</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 * 	 System.out.println(resp.getData().get(0).getName());
	 * 	}</pre>
	 */
	public MetaDataObjectPageLayoutResponse retrievePageLayouts(String objectName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJECT_PAGE_LAYOUTS, true);
		url = url.replace("{object_name}", objectName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, MetaDataObjectPageLayoutResponse.class);
	}

	/**
	 * <b>Retrieve Page Layout Metadata</b>
	 * <p>
	 * Given a page layout name, retrieve the metadata for that specific page layout.
	 * @param objectName Vault object name
	 * @param layoutName Vault page layout name
	 * @return MetaDataObjectPageLayoutResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/vobjects/{object_name}/page_layouts/{layout_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-page-layout-metadata' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-page-layout-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataObjectPageLayoutResponse resp = vaultClient.newRequest(MetaDataRequest.class)
	 * 	  .retrievePageLayoutMetadata("user__sys", "user_detail_page_layout__c");</pre>
	 * 	@vapil.response <pre>
	 * 	System.out.println(resp.getResponseStatus());
	 * </pre>
	 */
	public MetaDataObjectPageLayoutResponse retrievePageLayoutMetadata(String objectName, String layoutName) {
		String url = vaultClient.getAPIEndpoint(URL_OBJECT_PAGE_LAYOUT_METADATA, true);
		url = url.replace("{object_name}", objectName);
		url = url.replace("{layout_name}", layoutName);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, MetaDataObjectPageLayoutResponse.class);
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public MetaDataRequest setAcceptJSON() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public MetaDataRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set to true to retrieve localized (translated) strings for the
	 * label, label_plural, and help_content object fields.
	 *
	 * @param localized True for localized strings
	 * @return The request
	 */
	public MetaDataRequest setLocalized(Boolean localized) {
		this.localized = localized;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public MetaDataRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify source data in an input string, such as a JSON request
	 *
	 * @param requestString The source request as a string
	 * @return The Request
	 */
	public MetaDataRequest setRequestString(String requestString) {
		this.requestString = requestString;
		return this;
	}
}
