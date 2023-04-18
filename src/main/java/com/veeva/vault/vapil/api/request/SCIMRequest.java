/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

/**
 * SCIM Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#scim">https://developer.veevavault.com/api/23.1/#scim</a>
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.common.SCIMUser;
import com.veeva.vault.vapil.api.model.response.SCIMUserBulkResponse;
import com.veeva.vault.vapil.api.model.response.SCIMResponse;
import com.veeva.vault.vapil.api.model.response.SCIMUserResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

public class SCIMRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_SCIM = "/scim/";
	private static final String URL_SCIM_VERSION = "v2";
	private static final String URL_BASE = URL_SCIM + URL_SCIM_VERSION;

	private static final String URL_SERVICE_PROVIDER_CONFIG = URL_BASE + "/ServiceProviderConfig";
	private static final String URL_SCHEMAS = URL_BASE + "/Schemas";
	private static final String URL_SCHEMA_ID = URL_BASE + "/Schemas/{id}";
	private static final String URL_RESOURCE_TYPES = URL_BASE + "/ResourceTypes";
	private static final String URL_RESOURCE_TYPE_ID = URL_BASE + "/ResourceTypes/{type}";
	private static final String URL_USERS = URL_BASE + "/Users";
	private static final String URL_USER_ID = URL_BASE + "/Users/{id}";
	private static final String URL_RESOURCES = URL_BASE + "/{type}";
	private static final String URL_RESOURCE_ID = URL_RESOURCES + "/{id}";

	// API Query string parameters
	private static final String HTTP_QUERYSTRING_FILTER = "filter";
	private static final String HTTP_QUERYSTRING_ATTRIBUTES = "attributes";
	private static final String HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES = "excludedAttributes";
	private static final String HTTP_QUERYSTRING_SORT_BY = "sortBy";
	private static final String HTTP_QUERYSTRING_SORT_ORDER = "sortOrder";
	private static final String HTTP_QUERYSTRING_COUNT = "count";
	private static final String HTTP_QUERYSTRING_START_INDEX = "startIndex";

	// API Request Parameters
	private String filter;
	private String attributes;
	private String excludedAttributes;
	private String sortBy;
	private String sortOrder;
	private Integer count;
	private Integer startIndex;
	private SCIMUser scimUser;
	private String scimUserJSON;

	private SCIMRequest() {
	}

	/**
	 * Retrieve SCIM Provider
	 * <p>
	 * Retrieve a JSON that describes the SCIM specification features available on the currently authenticated vault.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/ServiceProviderConfig</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-scim-provider' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-scim-provider</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class).retrieveSCIMProvider();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("Schemas: " + scimResponse.getSchemas());
	 * System.out.println("Documentation URI: " + scimResponse.getDocumentationUri());
	 * System.out.println("Patch: " + scimResponse.getPatch().getSupported());
	 * System.out.println("Bulk - Supported: " + scimResponse.getBulk().getSupported());
	 * System.out.println("Bulk - Max Operations: " + scimResponse.getBulk().getMaxOperations());
	 * System.out.println("Bulk - Max Payload Size: " + scimResponse.getBulk().getMaxPayloadSize());
	 * System.out.println("Filter - Supported: " + scimResponse.getFilter().getSupported());
	 * System.out.println("Filter - Max Results: " + scimResponse.getFilter().getMaxResults());
	 * System.out.println("Change Password supported: " + scimResponse.getChangePassword().getSupported());
	 * System.out.println("Sort supported: " + scimResponse.getSort().getSupported());
	 * System.out.println("eTag supported: " + scimResponse.getEtag().getSupported());
	 * System.out.println("Authentication Schemes: ");
	 * System.out.println("-----");
	 *
	 * System.out.println();
	 * System.out.println("Meta - Resource Type: " + scimResponse.getMeta().getResourceType());
	 * System.out.println("Meta - Location: " + scimResponse.getMeta().getLocation());

	 * System.out.println("Schemas: " + scimResponse.getSchemas());
	 * System.out.println("Total Results: " + scimResponse.getTotalResults());
	 * System.out.println("Resources: ");
	 * System.out.println("-----");
	 *
	 *
	 * List&lt;Resource&gt; schemaResources = scimResponse.getResources();
	 * for (Resource r : schemaResources) {
	 *   System.out.println("\tSchemas: " + r.getSchemas());
	 *   System.out.println("\tID: " + r.getId());
	 *   System.out.println("\tName: " + r.getName());
	 *   System.out.println("\tDescription: " + r.getDescription());
	 *   System.out.println("\tAttributes:");
	 *   System.out.println("-----");
	 *
	 *
	 *   List&lt;Attribute&gt; resourceAttributes = r.getAttributes();
	 *   for (Attribute a : resourceAttributes) {
	 *     System.out.println("\t\tName: " + a.getName());
	 *     System.out.println("\t\tType: " + a.getType());
	 *     System.out.println("\t\tMulti Valued: " + a.getMultiValued());
	 *     System.out.println("\t\tDescription: " + a.getDescription());
	 *     System.out.println("\t\tRequired: " + a.getRequired());
	 *     System.out.println("\t\tExact Case: " + a.getCaseExact());
	 *     System.out.println("\t\tMutability: " + a.getMutability());
	 *     System.out.println("\t\tReturned: " + a.getReturned());
	 *     System.out.println("\t\tUniqueness: " + a.getUniqueness());
	 *     System.out.println("-----");
	 *   }
	 *   System.out.println("-----");
	 * }</pre>
	 *
	 * @return SCIMResponse
	 */
	public SCIMResponse retrieveSCIMProvider() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_SERVICE_PROVIDER_CONFIG));

		return send(HttpMethod.GET, request, SCIMResponse.class);
	}

	/**
	 * Retrieve All SCIM Schema Information
	 * <br>
	 * Retrieve information about all SCIM schema specifications supported by a Vault SCIM service provider
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/Schemas</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-scim-schema-information' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-scim-schema-information</a>
	 *
	 * @return SCIMResponse
	 */
	public SCIMResponse retrieveSchemaInformation() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_SCHEMAS));

		return send(HttpMethod.GET, request, SCIMResponse.class);
	}

	/**
	 * Retrieve Single SCIM Schema Information
	 * <br>
	 * Retrieve information about a single SCIM schema specification supported by a Vault SCIM service provider
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/Schemas/{id}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-single-scim-resource' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-single-scim-resource</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class).retrieveSchema(schema);</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("Schemas: " + scimResponse.getSchemas());
	 * System.out.println("Name: " + scimResponse.getName());
	 * System.out.println("Description: " + scimResponse.getDescription());
	 * System.out.println("Attributes: ");
	 * System.out.println("-----");
	 *
	 * if (!scimResponse.hasAttributeErrors()) {
	 *   List&lt;Attribute&gt; attributes = scimResponse.getAttributes();
	 *   for(Attribute a : attributes) {
	 *     System.out.println("Name: " + a.getName());
	 *     System.out.println("-----");
	 *   }
	 *   System.out.println();
	 * }</pre>
	 *
	 * @param schemaId Schema ID
	 * @return SCIMResponse
	 */
	public SCIMResponse retrieveSchema(String schemaId) {
		String url = vaultClient.getAPIEndpoint(URL_SCHEMA_ID).replace("{id}", schemaId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, SCIMResponse.class);
	}

	/**
	 * Retrieve All SCIM Resource Types
	 * <br>
	 * Retrieve the types of SCIM resources available.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/ResourceTypes</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-scim-resource-types' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-scim-resource-types</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class).retrieveResourceTypes();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("Total Results: " + scimResponse.getTotalResults());
	 * System.out.println("-----");
	 *
	 * if (!scimResponse.hasResourceErrors()) {
	 *   List&lt;Resource&gt; resources = scimResponse.getResources();
	 *   for(Resource r : resources) {
	 *     System.out.println("ID: " + r.getId());
	 *     System.out.println("Name: " + r.getName());
	 *     System.out.println("-----");
	 *   }
	 * }</pre>
	 *
	 * @return SCIMResponse
	 */
	public SCIMResponse retrieveResourceTypes() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_RESOURCE_TYPES));

		return send(HttpMethod.GET, request, SCIMResponse.class);
	}

	/**
	 * Retrieve Single SCIM Resource Type
	 * <br>
	 * Retrieve a single SCIM resource type. Defines the endpoints, the core schema URI which defines this resource, and any supported schema extensions.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/ResourceTypes/{type}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-scim-resource-types' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-scim-resource-types</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class).retrieveResourceType(resourceType);</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("ID: " + scimResponse.getId());
	 * System.out.println("Name: " + scimResponse.getName());
	 * System.out.println("Description: " + scimResponse.getDescription());</pre>
	 *
	 * @param type Resource Type ID
	 * @return SCIMResponse
	 */
	public SCIMResponse retrieveResourceType(String type) {
		String url = vaultClient.getAPIEndpoint(URL_RESOURCE_TYPE_ID).replace("{type}", type);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, SCIMResponse.class);
	}

	/**
	 * Create User with SCIM
	 * <br>
	 * Crete users with SCIM.
	 *
	 * @vapil.api
	 * <pre>
	 * POST /api/{version}/scim/v2/Users</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#create-user-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#create-user-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * <i>Example 1 - JSON</i>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 .setSCIMUserJSON(jsonString)
	 *                 .createUser();</pre>
	 *
	 * @vapil.request
	 * <pre>
	 * <i>Example 2 - SCIM User</i>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 .setSCIMUser(scimUser)
	 *                 .createUser();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * SCIMUser user = scimResponse.getUser();
	 * System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 * System.out.println("\tDisplay Name: " + user.getDisplayName());
	 * System.out.println("\tUser Name: " + user.getUserName());
	 *
	 * for(SCIMUser.Email email : user.getEmails()){
	 *   System.out.println("\t" + email.getType() + "Email: " + email.getValue());
	 * }</pre>
	 *
	 * @return SCIMUserResponse
	 */
	public SCIMUserResponse createUser() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_USERS));
		String createUserJSON = null;


		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);
		if (scimUserJSON != null) {
			createUserJSON = scimUserJSON;
		} else if (scimUser != null) {
			createUserJSON = scimUser.toJsonString();
		} else {
			throw new IllegalArgumentException("Either a SCIM User or a SCIM User JSON required");
		}

		//Update Raw String for updating.
		request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_SCIM_JSON, createUserJSON);

		SCIMUserResponse response = send(HttpMethod.POST, request, SCIMUserResponse.class);
		return response;
	}

	/**
	 * Retrieve All Users with SCIM
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/Users</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-users-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-users-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMUserBulkResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 //.setFilter("userName eq \"bob.jones@verteobiotech.com\"")
	 *                 //.setAttributes("username,emails")
	 *                 //.setExcludedAttributes("username,emails")
	 *                 .setSortBy("name.familyName")
	 *                 .setSortOrder("descending")
	 *                 //.setCount(3)
	 *                 .retrieveAllUsers();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * if (scimResponse.isSuccessful()) {
	 *   List&lt;SCIMUser&gt; users = scimResponse.getUsers();
	 *
	 *   for(SCIMUser user : users) {
	 *     System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 *     System.out.println("\tDisplay Name: " + user.getDisplayName());
	 *     System.out.println("\tUser Name: " + user.getUserName());
	 *
	 *     for(SCIMUser.Email email : user.getEmails()){
	 *       System.out.println("\t" + email.getType() + "Email: " + email.getValue());
	 *     }
	 *   }
	 * }</pre>
	 *
	 * @return SCIMUserBulkResponse
	 */
	public SCIMUserBulkResponse retrieveAllUsers() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_USERS));

		if (filter != null && !filter.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_FILTER, filter);
		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);
		if (sortBy != null && !sortBy.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_SORT_BY, sortBy);
		if (sortOrder != null && !sortOrder.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_SORT_ORDER, sortOrder);
		if (count != null)
			request.addQueryParam(HTTP_QUERYSTRING_COUNT, count);
		if (startIndex != null)
			request.addQueryParam(HTTP_QUERYSTRING_START_INDEX, startIndex);

		return send(HttpMethod.GET, request, SCIMUserBulkResponse.class);
	}


	/**
	 * Retrieve Single User with SCIM
	 * <br>
	 * Retrieve a specific user with SCIM.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/Users/{id}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-single-user-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-single-user-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 //.setAttributes("username,emails")
	 *                 //.setExcludedAttributes("username,emails")
	 *                 .retrieveUser(id);</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * SCIMUser user = scimResponse.getUser();
	 * System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 * System.out.println("\tDisplay Name: " + user.getDisplayName());
	 * System.out.println("\tUser Name: " + user.getUserName());
	 *
	 * for(SCIMUser.Email email : user.getEmails()){
	 *   System.out.println("\t" + email.getType() + "Email: " + email.getValue());
	 * }</pre>
	 *
	 * @param userId Resource Type ID
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMUserResponse retrieveUser(String userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_ID).replace("{id}", userId);

		ObjectMapper objectMapper = getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (filter != null && !filter.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_FILTER, filter);
		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);

		return send(HttpMethod.GET, request, objectMapper, SCIMUserResponse.class);
	}

	/**
	 * Retrieve Current User with SCIM
	 * <br>
	 * Retrieve the currently authenticated user with SCIM.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/Me</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-current-user-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-current-user-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 //.setAttributes("username,emails")
	 *                 //.setExcludedAttributes("username,emails")
	 *                 .retrieveCurrentUser();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * SCIMUser user = scimResponse.getUser();
	 * System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 * System.out.println("\tDisplay Name: " + user.getDisplayName());
	 * System.out.println("\tUser Name: " + user.getUserName());
	 *
	 * for(SCIMUser.Email email : user.getEmails()){
	 *   System.out.println("\t" + email.getType() + "Email: " + email.getValue());
	 * }</pre>
	 *
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMUserResponse retrieveCurrentUser() {
		String url = vaultClient.getAPIEndpoint(URL_BASE + "/Me");

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);

		return send(HttpMethod.GET, request, SCIMUserResponse.class);
	}

	/**
	 * Update Current SCIM User
	 * <br>
	 * Update the currently authenticated user with SCIM.
	 *
	 * @vapil.api
	 * <pre>
	 * PUT /api/{version}/scim/v2/Me</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#update-current-user-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#update-current-user-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 //.setAttributes("username,emails")
	 *                 //.setExcludedAttributes("username,emails")
	 *                 .setSCIMUser(scimUser)
	 *                 .updateCurrentUser();</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * SCIMUser user = scimResponse.getUser();
	 * System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 * System.out.println("\tDisplay Name: " + user.getDisplayName());
	 * System.out.println("\tUser Name: " + user.getUserName());
	 *
	 * for(SCIMUser.Email emailresp : user.getEmails()){
	 *   System.out.println("\t" + emailresp.getType() + "Email: " + emailresp.getValue());
	 * }</pre>
	 *
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMUserResponse updateCurrentUser() {
		String updateJSON = null;
		String url = vaultClient.getAPIEndpoint(URL_BASE + "/Me");

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_SCIM_JSON);

		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);

		if (scimUserJSON != null) {
			updateJSON = scimUserJSON;
		} else if (scimUser != null) {
			updateJSON = scimUser.toJsonString();
		} else {
			throw new IllegalArgumentException("Either a SCIM User or a SCIM User JSON required");
		}

		//Update Raw String for updating.
		request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_SCIM_JSON, updateJSON);

		return send(HttpMethod.PUT, request, SCIMUserResponse.class);
	}

	/**
	 * Retrieve Single SCIM Resources
	 * <br>
	 * Retrieve a single SCIM resource.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/{type}/{id}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-single-scim-resource' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-single-scim-resource</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 .retrieveSingleSCIMResource(type, id);</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("Schemas: " + scimResponse.getSchemas());
	 * System.out.println("\tId: " + scimResponse.getId());
	 * System.out.println("\tDisplay Name: " + scimResponse.getDisplayName());
	 * System.out.println("\tActive: " + scimResponse.getActive());
	 * </pre>
	 *
	 * @param type type of resource
	 * @param id id of resource
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMResponse retrieveSingleSCIMResource(String type, String id) {
		String url = vaultClient.getAPIEndpoint(URL_RESOURCE_ID).replace("{type}", type).replace("{id}", id);
		HttpRequestConnector request = new HttpRequestConnector(url);
		SCIMResponse response = send(HttpMethod.GET, request, SCIMResponse.class);
		return response;
	}

	/**
	 * Retrieve SCIM Resources
	 * <br>
	 * Retrieve a single SCIM resource type. Defines the endpoints, the core schema URI which defines this resource, and any supported schema extensions.
	 *
	 * @vapil.api
	 * <pre>
	 * GET /api/{version}/scim/v2/{type}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#retrieve-scim-resources' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-scim-resources</a>
	 *
	 * @vapil.request
	 * <pre>
	 * SCIMResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 //.setFilter("userName eq \"username@verteobiotech.com\"")
	 *                 //.setAttributes("name,description")
	 *                 .setExcludedAttributes("id,name")
	 *                 //.setSortBy("name.familyName")
	 *                 //.setSortOrder("descending")
	 *                 //.setCount(3)
	 *                 .retrieveSCIMResources(type);</pre>
	 *
	 * @vapil.response
	 * <pre>
	 * System.out.println("Schemas: " + scimResponse.getSchemas());
	 * System.out.println("\tName: " + scimResponse.getName());
	 * System.out.println("\tDescription: " + scimResponse.getDescription());
	 * System.out.println("\tEndpoint: " + scimResponse.getEndpoint()); 
	 * </pre>
	 *
	 * @param type type of resource
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMResponse retrieveSCIMResources(String type) {
		String url = vaultClient.getAPIEndpoint(URL_RESOURCES).replace("{type}", type);

		HttpRequestConnector request = new HttpRequestConnector(url);
		if (filter != null && !filter.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_FILTER, filter);
		if (attributes != null && !attributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_ATTRIBUTES, attributes);
		if (excludedAttributes != null && !excludedAttributes.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDED_ATTRIBUTES, excludedAttributes);
		if (sortBy != null && !sortBy.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_SORT_BY, sortBy);
		if (sortOrder != null && !sortOrder.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_SORT_ORDER, sortOrder);
		if (count != null)
			request.addQueryParam(HTTP_QUERYSTRING_COUNT, count);
		if (startIndex != null)
			request.addQueryParam(HTTP_QUERYSTRING_START_INDEX, startIndex);

		SCIMResponse response = send(HttpMethod.GET, request, SCIMResponse.class);
		return response;

	}

	/**
	 * Update SCIM User
	 * <br>
	 * Update User with SCIM.
	 *
	 * @vapil.api
	 * <pre>
	 * PUT /api/{version}/scim/v2/Users/{id}</pre>
	 *
	 * @vapil.vaultlink
	 * <a href='https://developer.veevavault.com/api/23.1/#update-current-user-with-scim' target='_blank'>https://developer.veevavault.com/api/23.1/#update-current-user-with-scim</a>
	 *
	 * @vapil.request
	 * <pre>
	 * <i>Example 1 - SCIM User</i>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 .setSCIMUser(scimUser)
	 *                 .updateUser(id);</pre>
	 *
	 * @vapil.request
	 * <pre>
	 * <i>Example 2 - JSON</i>
	 * SCIMUserResponse scimResponse = vaultClient.newRequest(SCIMRequest.class)
	 *                 .setSCIMUserJSON(jsonString)
	 *                 .updateUser(id);</pre>
	 *
	 * @vapil.response
	 * <pre>SCIMUser user = scimResponse.getUser();
	 * System.out.println("\tName: " + user.getName().getGivenName() + " " + user.getName().getFamilyName() );
	 * System.out.println("\tDisplay Name: " + user.getDisplayName());
	 * System.out.println("\tUser Name: " + user.getUserName());
	 *
	 * for(SCIMUser.Email email : user.getEmails()){
	 *   System.out.println("\t" + email.getType() + "Email: " + email.getValue());
	 * }</pre>
	 *
	 * @param userId id of user
	 * @return SCIMUserResponse.Resource
	 */
	public SCIMUserResponse updateUser(String userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_ID).replace("{id}", userId);
		String updateJSON = null;

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_SCIM_JSON);

		if (scimUserJSON != null) {
			updateJSON = scimUserJSON;
		} else if (scimUser != null) {
			updateJSON = scimUser.toJsonString();
		} else {
			throw new IllegalArgumentException("Either a SCIM User or a SCIM User JSON required");
		}

		//Update Raw String for updating.
		request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_SCIM_JSON, updateJSON);

		return send(HttpMethod.PUT, request, SCIMUserResponse.class);
	}
	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set parameter for filtering SCIM user retrieval results
	 * <p>
	 * @param filter the filter to apply to retrieve users
	 * @return SCIMRequest
	 */
	public SCIMRequest setFilter(String filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Set parameter for attributes to retrieve for SCIM users
	 * <p>
	 * @param attributes the attributes to include in retrieving SCIM users
	 * @return SCIMRequest
	 */
	public SCIMRequest setAttributes(String attributes) {
		this.attributes = attributes;
		return this;
	}

	/**
	 * Set parameter for attributes to exclude from SCIM user retrieval
	 * <p>
	 * @param excludedAttributes the attributes to exclude in retrieving SCIM users
	 * @return SCIMRequest
	 */
	public SCIMRequest setExcludedAttributes(String excludedAttributes) {
		this.excludedAttributes = excludedAttributes;
		return this;
	}

	/**
	 * Set parameter for sorting to retrieve for SCIM users
	 * <p>
	 * @param sortBy the sort parameter to include in retrieving SCIM users
	 * @return SCIMRequest
	 */
	public SCIMRequest setSortBy(String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	/**
	 * Set parameter for order of sorting to retrieve for SCIM users
	 * <p>
	 * @param sortOrder the order to sort retrieved SCIM users
	 * @return SCIMRequest
	 */
	public SCIMRequest setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	/**
	 * Set parameter for count of retrieved SCIM users
	 * <p>
	 * @param count the count of users to retrieve
	 * @return SCIMRequest
	 */
	public SCIMRequest setCount(Integer count) {
		this.count = count;
		return this;
	}

	/**
	 * Set parameter for start index of retrieved SCIM users
	 * <p>
	 * @param startIndex the starting index of users to retrieve
	 * @return SCIMRequest
	 */
	public SCIMRequest setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	/**
	 * Set parameter for SCIM User Object for creating or updating SCIM Users
	 * <p>
	 * @param scimUser SCIM User defining a new or updated user
	 * @return SCIMRequest
	 */
	public SCIMRequest setSCIMUser(SCIMUser scimUser) {
		this.scimUser = scimUser;
		return this;
	}

	/**
	 * Set parameter for SCIM User Json for creating or updating SCIM Users
	 * <p>
	 * @param scimUserJSON json definining a SCIM User
	 * @return SCIMRequest
	 */
	public SCIMRequest setSCIMUserJSON(String scimUserJSON) {
		this.scimUserJSON = scimUserJSON;
		return this;
	}
}
