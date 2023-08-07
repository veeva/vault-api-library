/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.common.Binder.Node.BinderSection;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.common.DocumentRelationship;
import com.veeva.vault.vapil.api.model.response.BinderExportResponse;
import com.veeva.vault.vapil.api.model.response.BinderResponse;
import com.veeva.vault.vapil.api.model.response.BinderSectionResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipResponse;
import com.veeva.vault.vapil.api.model.response.DocumentRelationshipRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Binder Requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#binders">https://developer.veevavault.com/api/23.1/#binders</a>
 * <p>
 * <i>Covered in other classes for ease of use:</i><br>
 * <a href="https://developer.veevavault.com/api/23.1/#binder-templates">https://developer.veevavault.com/api/23.1/#binder-templates</a>
 */
public class BinderRequest extends VaultRequest {

	// API Endpoints
	private static final String URL_BINDER = "/objects/binders";
	private static final String URL_BINDER_ID = "/objects/binders/{binder_id}";
	private static final String URL_BINDER_VERSIONS = "/objects/binders/{binder_id}/versions";
	private static final String URL_BINDER_VERSION = "/objects/binders/{binder_id}/versions/{major_version}/{minor_version}";
	private static final String URL_BINDER_ACTION = "/objects/binders/{binder_id}/actions";
	private static final String URL_BINDER_RELATIONSHIP = "/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships";
	private static final String URL_BINDER_RELATIONSHIP_ID = "/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}";
	private static final String URL_BINDER_SECTION = "/objects/binders/{binder_id}/sections";
	private static final String URL_BINDER_SECTION_ID = "/objects/binders/{binder_id}/sections/{section_id}";
	private static final String URL_BINDER_SECTION_VERSION = "/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/sections/{section_id}";
	private static final String URL_BINDER_EXPORT = "/objects/binders/{binder_id}/actions/export";
	private static final String URL_BINDER_EXPORT_VERSION = "/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/actions/export";
	private static final String URL_BINDER_EXPORT_RESULTS = "/objects/binders/actions/export/{job_id}/results";
	private static final String URL_BINDER_DOCUMENTS = "/objects/binders/{binder_id}/documents";
	private static final String URL_BINDER_DOCUMENTS_SECTION = "/objects/binders/{binder_id}/documents/{section_id}";
	private static final String URL_BINDER_BINDING_RULE = "/objects/binders/{binder_id}/binding_rule";
	private static final String URL_BINDER_BINDING_RULE_SECTION = "/objects/binders/{binder_id}/sections/{node_id}/binding_rule";
	private static final String URL_BINDER_BINDING_RULE_DOCUMENT = "/objects/binders/{binder_id}/documents/{node_id}/binding_rule";

	// API Request Parameters
	private Boolean depth;
	private Boolean async;
	private AttachmentType exportAttachmentType;
	private Boolean exportAudit;
	private AuditFormatType exportAuditFormatType;
	private Boolean exportDocumentMetadata;
	private DocumentVersionType exportDocumentVersionType;
	private String exportRenditionType;
	private Boolean exportSource;


	private BinderRequest() {
		depth = false;
		async = false;
	}

	/**
	 * <b>Retrieve Binder</b>
	 * <p>
	 * Retrieve the first level of the binder (depth = false)
	 * or additional levels of the binder (depth = true)
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Setting Depth</i>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.setDepth(depthAll)
	 * 			.retrieveBinder(binderId);</pre>
	 * @vapil.response <pre>
	 * Binder binder = resp.getBinder();
	 *
	 * for (Node node : binder.getNodes()) {
	 *   for (BinderSection binderSection : node.getProperties()) {
	 *     System.out.println("Id = " + binderSection.getId());
	 *     System.out.println("Document Id = " + binderSection.getDocumentId());
	 *     System.out.println("Name = " + binderSection.getName());
	 *     System.out.println("Order = " + binderSection.getOrder());
	 *     System.out.println("ParentId = " + binderSection.getParentId());
	 *     System.out.println("Section Number = " + binderSection.getSectionNumber());
	 *     System.out.println("Type = " + binderSection.getType());
	 *     System.out.println("Major Version = " + binderSection.getMajorVersionNumber());
	 *     System.out.println("Minor Version = " + binderSection.getMinorVersionNumber());
	 *   }
	 *
	 *   if (node.hasChildren())
	 *     this(node.getNodes());
	 * }
	 *
	 * Document doc = resp.getDocument();
	 * System.out.println("Name = " + doc.getName());
	 * System.out.println("Major Version = " + doc.getMajorVersionNumber());
	 * System.out.println("Minor Version = " + doc.getMinorVersionNumber());
	 * System.out.println("Status = " + doc.getStatus());</pre>
	 */
	public BinderResponse retrieveBinder(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ID)
				.replace("{binder_id}", String.valueOf(binderId));
		if (depth != null && depth) url += "?depth=all";

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, BinderResponse.class);
	}

	/**
	 * <b>Retrieve All Binder Versions</b>
	 * <p>
	 * Retrieve all versions of a binder
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}/versions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-binder-versions' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-binder-versions</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.retrieveAllBinderVersions(binderId);</pre>
	 * @vapil.response <pre>
	 * for (Version version : resp.getVersions()) {
	 *   System.out.println("-----------------");
	 * 	 System.out.println(version.getNumber());
	 * 	 System.out.println(version.getValue());
	 * }</pre>
	 */
	public BinderResponse retrieveAllBinderVersions(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_VERSIONS)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, BinderResponse.class);
	}

	/**
	 * <b>Retrieve Binder Version</b>
	 * <p>
	 * Retrieve the fields and values configured on a specific version of a specific binder.
	 * Retrieve the first level of the binder (depth = false)
	 * or additional levels of the binder (depth = true)
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}/versions/(major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-version' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-version</a>
	 * @vapil.request <pre>BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.setDepth(depthAll)
	 * 			.retrieveBinderVersion(binderId, majorVersion, minorVersion);	</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * Binder binder = resp.getBinder();
	 * </pre>
	 */
	public BinderResponse retrieveBinderVersion(int binderId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		if (depth != null && depth) url += "?depth=all";

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, BinderResponse.class);
	}

	/**
	 * <b>Create Binder</b>
	 * <p>
	 * All required binder (document) fields must be included in the request.
	 * When creating a binder, no file is included in the request.
	 *
	 * @param doc Document fields for the binder
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder</a>
	 * @vapil.request <pre>
	 * Document doc = new Document();
	 *
	 * doc.setName("VAPIL Binder");
	 * doc.setLifecycle("General Lifecycle");
	 * doc.setType("General");
	 * doc.setTitle("Test Upload VAPIL Binder");
	 *
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.setAsync(true)
	 * 			.createBinder(doc);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Status = " + resp.getResponseMessage());
	 * System.out.println("Status = " + resp.getDocument().getId());</pre>
	 */
	public BinderResponse createBinder(Document doc) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER);
		if (async != null && async) url += "?async=true";

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());

		return send(HttpMethod.POST, request, BinderResponse.class);
	}

	/**
	 * <b>Create Binder from Template</b>
	 * <p>
	 * Specify the API name of the template and document fields
	 * to create a binder from a template
	 *
	 * @param doc          Document fields for the binder
	 * @param templateName The API name of the binder template
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-from-template' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-from-template</a>
	 * @vapil.request <pre>
	 * resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.createBinderFromTemplate(doc, "example_binder_template__c");</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Status = " + resp.getResponseMessage());
	 * System.out.println("Status = " + resp.getDocument().getId());</pre>
	 */
	public BinderResponse createBinderFromTemplate(Document doc, String templateName) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER);

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());
		request.addBodyParam("fromTemplate", templateName);

		return send(HttpMethod.POST, request, BinderResponse.class);
	}

	/**
	 * <b>Create Binder Version</b>
	 * <p>
	 * All required binder (document) fields must be included in the request.
	 * When creating a binder, no file is included in the request.
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/binders/{binder_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-version' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-version</a>
	 * @vapil.request <pre>
	 * resp = vaultClient.newRequest(BinderRequest.class)
	 * .createBinderVersion(resp.getDocument().getId());</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Create Binder Version******");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Status = " + resp.getResponseMessage());
	 * System.out.println("Status = " + resp.getDocument().getMajorVersionNumber());
	 * System.out.println("Status = " + resp.getDocument().getMinorVersionNumber());</pre>
	 */
	public BinderResponse createBinderVersion(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ID)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, BinderResponse.class);
	}

	/**
	 * <b>Update Binder</b>
	 * <p>
	 * Update the binder (document) fields
	 *
	 * @param binderId The id of the binder (document)
	 * @param doc      Document fields for the binder
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/binders/{binder_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder</a>
	 * @vapil.request <pre>
	 * resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.updateBinder(binderId, doc);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderResponse updateBinder(int binderId, Document doc) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ID)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());

		return send(HttpMethod.PUT, request, BinderResponse.class);
	}

	/**
	 * <b>Reclassify Binder</b>
	 * <p>
	 * Reclassify allows you to change the document type of an existing binder. A document "type" is the
	 * combination of the type__v, subtype__v, and classification__v fields on a binder. When you reclassify,
	 * Vault may add or remove certain fields on the binder. You can only reclassify the latest version of a
	 * binder and only one binder at a time. The API does not currently support Bulk Reclassify.
	 * You can also add or remove values for any other editable field.
	 *
	 * @param binderId The id of the binder (document)
	 * @param doc      Document fields for the binder with needed doc type fields set
	 *                 (type__v, subtype__v, classification__v, and lifecycle__v). Additional
	 *                 doc fields can be specified.
	 * @return BinderResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/binders/{binder_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder</a>
	 * @vapil.request <pre>
	 * doc.setType("Artwork");
	 * doc.setLifecycle("Artwork LC");
	 * doc.setName("Reclassified binder");
	 *
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * .reclassifyBinder(binderId, doc);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * </pre>
	 */
	public BinderResponse reclassifyBinder(int binderId, Document doc) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ID)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());
		request.addBodyParam("reclassify", "true");

		return send(HttpMethod.PUT, request, BinderResponse.class);
	}

	/**
	 * <b>Refresh Binder Auto-Filing</b>
	 * <p>
	 * This is only available in eTMF vaults on binders configured with the TMF Reference Models.
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/actions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#refresh-binder-auto-filing' target='_blank'>https://developer.veevavault.com/api/23.1/#refresh-binder-auto-filing</a>
	 */
	public BinderResponse refreshBinderAutoFiling(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ACTION)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("action", "refresh_auto_filing");

		return send(HttpMethod.POST, request, BinderResponse.class);
	}

	/**
	 * <b>Update Binder Version</b>
	 * <p>
	 * Update the binder (document) fields on a specific version
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @param doc          Document fields for the binder
	 * @return BinderResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder-version' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder-version</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.updateBinderVersion(binderId, majorVersion, minorVersion, doc);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderResponse updateBinderVersion(int binderId, int majorVersion, int minorVersion, Document doc) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(doc.toMap());

		return send(HttpMethod.PUT, request, BinderResponse.class);
	}

	/**
	 * <b>Delete Binder</b>
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>DELETE /api/{version}/objects/binders/{binder_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-binder</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.deleteBinder(binderId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Delete Binder ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * </pre>
	 */
	public BinderResponse deleteBinder(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_ID)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, BinderResponse.class);
	}

	/**
	 * <b>Delete Binder Version</b>
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>DELETE /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-binder-version' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-binder-version</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.deleteBinderVersion(binderId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Delete Binder Version ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * </pre>
	 */
	public BinderResponse deleteBinderVersion(int binderId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, BinderResponse.class);
	}

	/**
	 * <b>Retrieve Binder Sections (top-level root node)</b>
	 * <p>
	 * Retrieve all sections (documents and subsections) in a binder's top-level root node
	 *
	 * @param binderId The id of the binder (document)
	 * @return BinderResponse
	 * @vapil.api <pre>GET /api/{version}/objects/binders/{binder_id}/sections</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-sections' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-sections</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.retrieveBinderSections(binderId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * printBinder(resp.getBinder().getNodes());
	 *
	 * System.out.println("\n****** Retrieve Binder Section - Specific Node ******\n");
	 * // Note the different response model to handle differences with specific node response format
	 * BinderSectionResponse resp2 = vaultClient.newRequest(BinderRequest.class)
	 * .retrieveBinderSections(binderId, sectionId);
	 *
	 * System.out.println("Status = " + resp2.getResponseStatus());
	 * List&lt;BinderSection&gt; binderSections = resp2.getNode().getProperties();
	 * for (BinderSection binderSection : binderSections)
	 * printBinderSection(binderSection);
	 *
	 * printBinder(resp2.getNode().getNodes());
	 * </pre>
	 */
	public BinderResponse retrieveBinderSections(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, BinderResponse.class);
	}

	/**
	 * <b>Retrieve Binder Sections (sub-level node)</b>
	 * <p>
	 * Retrieve all sections (documents and subsections) in a binder's sub-level node.
	 *
	 * @param binderId  The id of the binder (document)
	 * @param sectionId The section id (also referred to as "node_id" in the API documentation)
	 * @return BinderSectionResponse
	 * @vapil.api <pre>GET /api/{version}/objects/binders/{binder_id}/sections/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-sections' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-sections</a>
	 */
	public BinderSectionResponse retrieveBinderSections(int binderId, String sectionId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION_ID)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, BinderSectionResponse.class);
	}

	/**
	 * <b>Retrieve Binder Version Sections (sub-level node)</b>
	 * <p>
	 * Retrieve all sections (documents and subsections) in a binder's sub-level node
	 * (for the specified binder version)
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @param sectionId    The section id (also referred to as "node_id" in the API documentation)
	 * @return BinderSectionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/sections/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-version-section' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-version-section</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.retrieveBinderVersionSections(binderId, majorVersion, minorVersion, sectionId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * List&lt;BinderSection&gt; binderSections = resp.getNode().getProperties();
	 * for (BinderSection binderSection : binderSections)
	 * printBinderSection(binderSection);
	 *
	 * printBinder(resp.getNode().getNodes());
	 * </pre>
	 */
	public BinderSectionResponse retrieveBinderVersionSections(int binderId, int majorVersion, int minorVersion, String sectionId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		// Nodes/Properties can be either single value or arrays
		ObjectMapper objectMapper = super.getBaseObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		return send(HttpMethod.GET, request, objectMapper, BinderSectionResponse.class);
	}

	/**
	 * <b>Create Binder Section</b>
	 * <p>
	 * Create a binder section with the passed in section properties, including set values for
	 * name__v (required), section_number__v, parent_id__v, order__v
	 *
	 * @param binderId      The id of the binder (document)
	 * @param binderSection Properties of the BinderSection, with name__v as the only required field
	 * @return BinderSectionResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/sections</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-section' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-section</a>
	 * @vapil.request <pre>
	 * <i>Example 1</i>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.createBinderSection(binderId, binderSection);</pre>
	 * @vapil.response <pre>
	 * <i>Example 1</i>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Created Id = " + resp.getId());</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Sub Section</i>	 *
	 * binderSection = new BinderSection();
	 * binderSection.setName(name + " Child");
	 * binderSection.setParentId(resp.getId());
	 *
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.createBinderSection(binderId, binderSection);</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - Sub Section</i>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Created Id = " + resp.getId());</pre>
	 */
	public BinderSectionResponse createBinderSection(int binderId, BinderSection binderSection) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(binderSection.toMap());

		return send(HttpMethod.POST, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Update Binder Section</b>
	 * <p>
	 * Update a binder section with the passed in section properties, including set values for
	 * name__v, section_number__v, parent_id__v, order__v
	 *
	 * @param binderId      The id of the binder (document)
	 * @param sectionId     The section id (also referred to as "node_id" in the API documentation
	 * @param binderSection Properties of the BinderSection, with name__v as the only required field
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/sections/{node_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder-section' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder-section</a>
	 * @vapil.request <pre>
	 * binderSection.setName("Changed Name");
	 *
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 		.updateBinderSection(binderId, resp.getId(), binderSection);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse updateBinderSection(int binderId, String sectionId, BinderSection binderSection) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION_ID)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(binderSection.toMap());

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Delete Binder Section</b>
	 *
	 * @param binderId  The id of the binder (document)
	 * @param sectionId The section id (also referred to as "node_id" in the API documentation
	 * @return BinderSectionResponse
	 * @vapil.api <pre>DELETE /api/{version}/objects/binders/{binder_id}/sections/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-binder-section' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-binder-section</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * .deleteBinderSection(binderId, resp.getId());</pre>
	 * @vapil.response <pre>temp</pre>
	 */
	public BinderSectionResponse deleteBinderSection(int binderId, String sectionId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_SECTION_ID)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Add Document to Binder</b>
	 *
	 * @param binderId The id of the binder (document)
	 * @param docId    The id of the document being added to the binder
	 * @param parentId The section id of the parent section, if the document will be in a section rather than top-level
	 *                 Note: the section id is unique no matter where it is in the binder hierarchy. Blank means adding the document at the top-level binder
	 * @return BinderSectionResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-document-to-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#add-document-to-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class).addDocumentToBinder(binderId, docId, parentId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse addDocumentToBinder(int binderId, int docId, String parentId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("document_id__v", docId);
		request.addBodyParam("parent_id__v", parentId);

		return send(HttpMethod.POST, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Add Document to Binder</b>
	 * <p>
	 * Specify binding rule of when adding document to the binder
	 *
	 * @param binderId    The id of the binder (document)
	 * @param docId       The id of the document being added to the binder
	 * @param parentId    The section id of the parent section, if the document will be in a section rather than top-level
	 * @param bindingRule Indicates which binding rule to apply (which document versions to link to the section). Options are:
	 *                    <ul>
	 *                    		<li>steady-state (bind to latest version in a steady-state)
	 *                    		<li>current (bind to current version)
	 *                    </ul>
	 * @return BinderSectionResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-document-to-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#add-document-to-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.addDocumentToBinder(binderId, docId, parentId, bindingRule);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse addDocumentToBinder(int binderId, int docId, String parentId, BindingRule bindingRule) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("document_id__v", docId);
		request.addBodyParam("parent_id__v", parentId);
		request.addBodyParam("binding_rule__v", bindingRule.getValue());


		return send(HttpMethod.POST, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Add Document to Binder</b>
	 * <p>
	 * Set binding rule to specific when adding document to the binder, specifying the major/minor version
	 *
	 * @param binderId     The id of the binder (document)
	 * @param docId        The id of the document being added to the binder
	 * @param parentId     The section id of the parent section, if the document will be in a section rather than top-level
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @return BinderSectionResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/documents</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#add-document-to-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#add-document-to-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class).addDocumentToBinder(binderId, docId, parentId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse addDocumentToBinder(int binderId, int docId, String parentId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("document_id__v", docId);
		request.addBodyParam("parent_id__v", parentId);
		request.addBodyParam("binding_rule__v", "specific");
		request.addBodyParam("major_version_number__v", majorVersion);
		request.addBodyParam("minor_version_number__v", minorVersion);

		return send(HttpMethod.POST, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Move Document in Binder</b>
	 *
	 * @param binderId  The id of the binder (document)
	 * @param sectionId The section (node) id to be removed
	 * @param parentId  Value of the new parent node to move the document to a different section or from a section to the binder's root node
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/documents/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#move-document-to-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#move-document-to-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.moveDocumentInBinder(binderId, sectionId, parentId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse moveDocumentInBinder(int binderId, String sectionId, String parentId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS_SECTION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("parent_id__v", parentId);

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Move Document in Binder</b>
	 * <p>
	 * Move document and also set a new order
	 *
	 * @param binderId  The id of the binder (document)
	 * @param sectionId The section (node) id to be removed
	 * @param parentId  Value of the new parent node to move the document to a different section or from a section to the binder's root node
	 * @param order     Enter a number reflecting the new position of the document within the binder or section
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/documents/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#move-document-to-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#move-document-to-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.moveDocumentInBinder(binderId, sectionId, parentId, order);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse moveDocumentInBinder(int binderId, String sectionId, String parentId, int order) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS_SECTION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("parent_id__v", parentId);
		request.addBodyParam("order__v", order);

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Remove Document from Binder</b>
	 *
	 * @param binderId  The id of the binder (document)
	 * @param sectionId The section (node) id to be removed
	 * @return BinderSectionResponse
	 * @vapil.api <pre>DELETE /api/{version}/objects/binders/{binder_id}/documents/{section_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#remove-document-from-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#remove-document-from-bindern</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.removeDocumentFromBinder(binderId, sectionId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse removeDocumentFromBinder(int binderId, String sectionId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_DOCUMENTS_SECTION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{section_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Retrieve Binder Relationship (All Binder Relationships)</b>
	 * <p>
	 * Retrieve all relationships for a binder
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @return DocumentRelationshipRetrieveResponse
	 * @vapil.api <pre>GET /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-relationship</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipRetrieveResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.retrieveBinderRelationship(binderId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * for (Relationship respRel : resp.getRelationships()) {
	 *   DocumentRelationship rel = respRel.getRelationship();
	 *
	 *   System.out.println("");
	 *   relationshipId = rel.getId();
	 *   System.out.println("Relationship Id = " + relationshipId);
	 *   System.out.println("Type = " + rel.getRelationshipType());
	 *   System.out.println("Created By = " + rel.getCreatedBy());
	 *   System.out.println("Created Date = " + rel.getCreatedDate());
	 *   System.out.println("Source Doc Id = " + rel.getSourceDocId());
	 *   System.out.println("Target Doc Id = " + rel.getTargetDocId());
	 *   System.out.println("Major = " + rel.getTargetMajorVersion());
	 *   System.out.println("Minor = " + rel.getTargetMinorVersion());
	 * }</pre>
	 */
	public DocumentRelationshipRetrieveResponse retrieveBinderRelationship(int binderId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_RELATIONSHIP)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentRelationshipRetrieveResponse.class);
	}

	/**
	 * <b>Retrieve Binder Relationship (Single Binder Relationship)</b>
	 * <p>
	 * Retrieve a specific/single binder relationship, as specified by relationship id
	 *
	 * @param binderId       The id of the binder (document)
	 * @param majorVersion   The major version of the binder (document)
	 * @param minorVersion   The minor version of the binder (document)
	 * @param relationshipId The binder relationship id field value.
	 * @return DocumentRelationshipRetrieveResponse
	 * @vapil.api <pre>GET /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-relationship</a>
	 * @vapil.request <pre>
	 * System.out.println("\n****** Retrieve Binder Relationships (Single) ******\n");
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.retrieveBinderRelationship(binderId, majorVersion, minorVersion, relationshipId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * for (Relationship respRel : resp.getRelationships()) {
	 *   DocumentRelationship rel = respRel.getRelationship();
	 *
	 *   System.out.println("");
	 *   relationshipId = rel.getId();
	 *   System.out.println("Relationship Id = " + relationshipId);
	 *   System.out.println("Type = " + rel.getRelationshipType());
	 *   System.out.println("Created By = " + rel.getCreatedBy());
	 *   System.out.println("Created Date = " + rel.getCreatedDate());
	 *   System.out.println("Source Doc Id = " + rel.getSourceDocId());
	 *   System.out.println("Target Doc Id = " + rel.getTargetDocId());
	 *   System.out.println("Major = " + rel.getTargetMajorVersion());
	 *   System.out.println("Minor = " + rel.getTargetMinorVersion());
	 * }</pre>
	 */
	public DocumentRelationshipRetrieveResponse retrieveBinderRelationship(int binderId, int majorVersion, int minorVersion, int relationshipId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_RELATIONSHIP_ID)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion))
				.replace("{relationship_id}", String.valueOf(relationshipId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, DocumentRelationshipRetrieveResponse.class);
	}

	/**
	 * <b>Create Binder Relationship</b>
	 *
	 * @param binderId           The id of the binder (document)
	 * @param majorVersion       The major version of the binder (document)
	 * @param minorVersion       The minor version of the binder (document)
	 * @param binderRelationship Object with properties for creating the relationship, including:
	 *                           <ul>
	 *                           <li>targetDocId (target_doc_id__v) - Set the target_doc_id__v to the document id of the "target document" to which a relationship will be established with the binder</li>
	 *                           <li>relationshipType (relationship_type__v) - Set the relationship_type__v to the field value of one of the desired relationshipTypes from the ï¿½Documents Relationships Metadataï¿½ call</li>
	 *                           <li>targetMajorVersion (target_major_version__v) - Required if creating a relationship with a specific version of the target document</li>
	 *                           <li>targetMinorVersion (target_minor_version__v) - Required if creating a relationship with a specific version of the target document</li>
	 *                           </ul>
	 * @return DocumentRelationshipResponse, with the getId method returning the resulting id from the created relationship
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-binder-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#create-binder-relationship</a>
	 * @vapil.request <pre>
	 * System.out.println("\n****** Create Binder Relationships ******\n");
	 * DocumentRelationship binderRelationship = new DocumentRelationship();
	 * binderRelationship.setRelationshipType("supporting_documents__c");
	 * binderRelationship.setTargetDocId(targetDocId);
	 *
	 * DocumentRelationshipResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 			.createBinderRelationship(binderId, majorVersion, minorVersion, binderRelationship);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Message = " + resp.getResponseMessage());
	 * System.out.println("Id " + resp.getId());
	 * </pre>
	 */
	public DocumentRelationshipResponse createBinderRelationship(int binderId, int majorVersion, int minorVersion, DocumentRelationship binderRelationship) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_RELATIONSHIP)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.setBodyParams(binderRelationship.getVaultModelData());
		return send(HttpMethod.POST, request, DocumentRelationshipResponse.class);
	}

	/**
	 * <b>Delete Binder Relationship</b>
	 *
	 * @param binderId       The id of the binder (document)
	 * @param majorVersion   The major version of the binder (document)
	 * @param minorVersion   The minor version of the binder (document)
	 * @param relationshipId The binder relationship id field value.
	 * @return DocumentRelationshipResponse
	 * @vapil.api <pre>DELETE /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/relationships/{relationship_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-binder-relationship' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-binder-relationship</a>
	 * @vapil.request <pre>
	 * DocumentRelationshipResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 					.deleteBinderRelationship(binderId, majorVersion, minorVersion, relationshipId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Message = " + resp.getResponseMessage());</pre>
	 */
	public DocumentRelationshipResponse deleteBinderRelationship(int binderId, int majorVersion, int minorVersion, int relationshipId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_RELATIONSHIP_ID)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion))
				.replace("{relationship_id}", String.valueOf(relationshipId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.DELETE, request, DocumentRelationshipResponse.class);
	}

	/**
	 * <b>Export Binder</b>
	 * <p>
	 * Export the latest version of the complete binder,
	 * including all binder sections and documents.
	 * After initiating an export, you can retrieve its status,
	 * results, and download the exported binder.
	 *
	 * @param binderId The id of the binder (document)
	 * @return JobCreateResponse Contains the Job Id to monitor status and results
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/actions/export</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#export-binder</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.exportBinder(binderId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * jobId = resp.getJobId();
	 * System.out.println("Job Id = " + jobId);</pre>
	 */
	public JobCreateResponse exportBinder(int binderId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_EXPORT)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (exportAttachmentType != null)
			request.addQueryParam("attachments", exportAttachmentType.getValue());
		if (exportAudit != null)
			request.addQueryParam("audit", exportAudit);
		if (exportAuditFormatType != null)
			request.addQueryParam("audit_format", exportAuditFormatType.getValue());
		if (exportDocumentMetadata != null)
			request.addQueryParam("docfield", exportDocumentMetadata);
		if (exportDocumentVersionType != null)
			request.addQueryParam("docversion", exportDocumentVersionType.getValue());
		if (exportRenditionType != null)
			request.addQueryParam("renditiontype", exportRenditionType);
		if (exportSource != null)
			request.addQueryParam("source", exportSource);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Export Binder</b>
	 * <p>
	 * Export a specific version of the complete binder,
	 * including all binder sections and documents.
	 * After initiating an export, you can retrieve its status,
	 * results, and download the exported binder.
	 *
	 * @param binderId     The id of the binder (document)
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @return JobCreateResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/actions/export</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-binder' target='_blank'>https://developer.veevavault.com/api/23.1/#export-binder</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.exportBinder(binderId, majorVersion, minorVersion);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Export Binder (Version) ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * jobId = resp.getJobId();
	 * System.out.println("Job Id = " + jobId);</pre>
	 */
	public JobCreateResponse exportBinder(int binderId, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_EXPORT_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (exportAttachmentType != null)
			request.addQueryParam("attachments", exportAttachmentType.getValue());
		if (exportAudit != null)
			request.addQueryParam("audit", exportAudit);
		if (exportAuditFormatType != null)
			request.addQueryParam("audit_format", exportAuditFormatType.getValue());
		if (exportDocumentMetadata != null)
			request.addQueryParam("docfield", exportDocumentMetadata);
		if (exportDocumentVersionType != null)
			request.addQueryParam("docversion", exportDocumentVersionType.getValue());
		if (exportRenditionType != null)
			request.addQueryParam("renditiontype", exportRenditionType);
		if (exportSource != null)
			request.addQueryParam("source", exportSource);


		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Export Binder Sections</b>
	 * <p>
	 * Export specific binder sections for the latest version of the binder.
	 * Exporting a binder section node will automatically include all of its subsections and documents therein.
	 * Vault will ignore id values which are invalid, but will export all which are valid.
	 *
	 * @param binderId   The binder id to export
	 * @param sectionIds Set of ids to export, either the section or document node
	 * @return JobCreateResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/actions/export</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-binder-sections' target='_blank'>https://developer.veevavault.com/api/23.1/#export-binder-sections</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.exportBinderSections(binderId, sectionIds);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Export Binder Section ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * jobId = resp.getJobId();
	 * System.out.println("Job Id = " + jobId);</pre>
	 */
	public JobCreateResponse exportBinderSections(int binderId, Set<String> sectionIds) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_EXPORT)
				.replace("{binder_id}", String.valueOf(binderId));

		StringBuilder requestString = new StringBuilder("id\n");
		for (String s : sectionIds) {
			requestString.append(s);
			requestString.append("\n");
		}

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);
		request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, requestString.toString());

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Export Binder Sections</b>
	 * <p>
	 * Export specific binder sections for a specific version of the binder.
	 * <p>
	 * Exporting a binder section node will automatically include all of its subsections and documents therein.
	 * Vault will ignore id values which are invalid, but will export all which are valid.
	 *
	 * @param binderId     The binder id to export
	 * @param majorVersion The major version of the binder (document)
	 * @param minorVersion The minor version of the binder (document)
	 * @param sectionIds   Set of ids to export, either the section or document node
	 * @return JobCreateResponse
	 * @vapil.api <pre>POST /api/{version}/objects/binders/{binder_id}/versions/{major_version}/{minor_version}/actions/export</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#export-binder-sections' target='_blank'>https://developer.veevavault.com/api/23.1/#export-binder-sections</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.exportBinderSections(binderId, majorVersion, minorVersion, sectionIds);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Export Binder Section (Version) ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * jobId = resp.getJobId();
	 * System.out.println("Job Id = " + jobId);</pre>
	 */
	public JobCreateResponse exportBinderSections(int binderId, int majorVersion, int minorVersion, Set<String> sectionIds) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_EXPORT_VERSION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{major_version}", String.valueOf(majorVersion))
				.replace("{minor_version}", String.valueOf(minorVersion));

		StringBuilder requestString = new StringBuilder("id\n");
		for (String s : sectionIds) {
			requestString.append(s);
			requestString.append("\n");
		}

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_CSV);
		request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV, requestString.toString());

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * <b>Retrieve Binder Export Results</b>
	 * <p>
	 * After submitting a request to export a binder from your vault,
	 * you can query vault to determine the results of the request.
	 *
	 * @param jobId The id value of the requested export job
	 * @return BinderExportResponse
	 * @vapil.api <pre>GET /api/{version}/objects/binders/actions/export/{job_id}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-binder-export-results' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-binder-export-results</a>
	 * @vapil.request <pre>
	 * BinderExportResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.retrieveBinderExportResults(jobId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Retrieve Binder Export Results ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Id = " + resp.getId());
	 * System.out.println("File = " + resp.getFile());
	 * System.out.println("Major Version = " + resp.getMajorVersionNumber());
	 * System.out.println("Minor Version = " + resp.getMinorVersionNumber());
	 * System.out.println("User Id = " + resp.getUserId());
	 * </pre>
	 */
	public BinderExportResponse retrieveBinderExportResults(int jobId) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_EXPORT_RESULTS)
				.replace("{job_id}", String.valueOf(jobId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, BinderExportResponse.class);
	}

	/**
	 * <b>Update Binding Rule</b>
	 *
	 * @param binderId            The binder id
	 * @param bindingRule         Indicates which binding rule to apply (which document versions to link to the section). Options are:
	 *                            <ul>
	 *                            		<li>default (bind to the latest available version)
	 *                            		<li>steady-state (bind to latest version in a steady-state)
	 *                            		<li>current (bind to current version)
	 *                            </ul>
	 * @param bindingRuleOverride Set to true or false to indicate if the specified binding rule should override
	 *                            documents or sections which already have binding rules set.
	 *                            If set to true, the binding rule is applied to all documents and sections within the current section.
	 *                            If false, the binding rule is applied only to documents and sections within the current section that
	 *                            do not have a binding rule specified
	 * @return BinderResponse The resulting response contains status and the binder id
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/binding_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binding-rule' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binding-rules</a>
	 * @vapil.request <pre>
	 * BinderResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.updateBindingRule(binderId, bindingRule, bindingRuleOverride);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Update Binding Rule Results ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderResponse updateBindingRule(int binderId, String bindingRule, Boolean bindingRuleOverride) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_BINDING_RULE)
				.replace("{binder_id}", String.valueOf(binderId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("binding_rule__v", bindingRule);
		request.addBodyParam("binding_rule_override__v", bindingRuleOverride);

		return send(HttpMethod.PUT, request, BinderResponse.class);
	}

	/**
	 * <b>Update Binder Section Binding Rule</b>
	 * <p>
	 * The resulting response contains status and the binder section id
	 *
	 * @param binderId            The binder id
	 * @param sectionId           The section id (also referred to as "node_id" in the API documentation)
	 * @param bindingRule         Indicates which binding rule to apply (which document versions to link to the section). Options are:
	 *                            <ul>
	 *                            		<li>default (bind to the latest available version)
	 *                            		<li>steady-state (bind to latest version in a steady-state)
	 *                            		<li>current (bind to current version)
	 *                            </ul>
	 * @param bindingRuleOverride Set to true or false to indicate if the specified binding rule should override
	 *                            documents or sections which already have binding rules set.
	 *                            If set to true, the binding rule is applied to all documents and sections within the current section.
	 *                            If false, the binding rule is applied only to documents and sections within the current section that
	 *                            do not have a binding rule specified
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/sections/{node_id}/binding_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder-section-binding-rule' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder-section-binding-rule</a>
	 * @vapil.request <pre>
	 * BinderSectionResponse resp = vaultClient.newRequest(BinderRequest.class)
	 * 				.updateBinderSectionBindingRule(binderId, sectionId, bindingRule, bindingRuleOverride);</pre>
	 * @vapil.response <pre>
	 * System.out.println("\n****** Update Binding Rule Results ******\n");
	 * System.out.println("Status = " + resp.getResponseStatus());</pre>
	 */
	public BinderSectionResponse updateBinderSectionBindingRule(int binderId, String sectionId, String bindingRule, Boolean bindingRuleOverride) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_BINDING_RULE_SECTION)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{node_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("binding_rule__v", bindingRule);
		request.addBodyParam("binding_rule_override__v", bindingRuleOverride);

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Update Binder Document Binding Rule</b>
	 *
	 * @param binderId    The binder id
	 * @param sectionId   The section id (also referred to as "node_id" in the API documentation)
	 * @param bindingRule Indicates which binding rule to apply (which document versions to link to the section). Options are:
	 *                    <ul>
	 *                    		<li>default (bind to the latest available version)
	 *                    		<li>steady-state (bind to latest version in a steady-state)
	 *                    		<li>current (bind to current version)
	 *                    </ul>
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/documents/{node_id}/binding_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder-document-binding-rule' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder-document-binding-rule</a>
	 */
	public BinderSectionResponse updateBinderDocumentBindingRule(int binderId, String sectionId, String bindingRule) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_BINDING_RULE_DOCUMENT)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{node_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("binding_rule__v", bindingRule);

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

	/**
	 * <b>Update Binder Document Binding Rule</b>
	 *
	 * @param binderId     The binder id
	 * @param sectionId    The section id (also referred to as "node_id" in the API documentation)
	 * @param bindingRule  Indicates which binding rule to apply (which document versions to link to the section). Options are:
	 *                     <ul>
	 *                     		<li>default (bind to the latest available version)
	 *                     		<li>steady-state (bind to latest version in a steady-state)
	 *                     		<li>current (bind to current version)
	 *                     </ul>
	 * @param majorVersion If the binding_rule is specific, then this is required and indicates the major version of the document to be linked
	 * @param minorVersion If the binding_rule is specific, then this is required and indicates the minor version of the document to be linked
	 * @return BinderSectionResponse
	 * @vapil.api <pre>PUT /api/{version}/objects/binders/{binder_id}/documents/{node_id}/binding_rule</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-binder-document-binding-rule' target='_blank'>https://developer.veevavault.com/api/23.1/#update-binder-document-binding-rule</a>
	 */
	public BinderSectionResponse updateBinderDocumentBindingRule(int binderId, String sectionId, String bindingRule, int majorVersion, int minorVersion) {
		String url = vaultClient.getAPIEndpoint(URL_BINDER_BINDING_RULE_DOCUMENT)
				.replace("{binder_id}", String.valueOf(binderId))
				.replace("{node_id}", sectionId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
		request.addBodyParam("binding_rule__v", bindingRule);
		request.addBodyParam("major_version_number__v", String.valueOf(majorVersion));
		request.addBodyParam("minor_version_number__v", String.valueOf(minorVersion));

		return send(HttpMethod.PUT, request, BinderSectionResponse.class);
	}

		/*
		Enums
	*/

	public enum AttachmentType {
		ALL("all"),
		LATEST("latest");

		private String value;

		AttachmentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum AuditFormatType {
		CSV("csv_v"),
		LATEST("pdf_v"),
		TEXT("text_v");

		private String value;

		AuditFormatType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum DocumentVersionType {
		MAJOR("major"),
		MAJOR_MINOR("major_minor");

		private String value;

		DocumentVersionType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum BindingRule {
		DEFAULT("default"),
		STEADY_STATE("steady-state"),
		CURRENT("current"),
		SPECIFIC("specific");

		private String value;

		BindingRule(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Binder retrieves - specify whether to retrieve all binder nodes
	 * when getting a binder
	 *
	 * @param depth Set to true to retrieve all binder nodes
	 * @return The request
	 */
	public BinderRequest setDepth(Boolean depth) {
		this.depth = depth;
		return this;
	}

	/**
	 * Binder creates - specify async.
	 * When creating a binder, the binder metadata is indexed synchronously by default.
	 * To process the indexing asynchronously, include a query parameter async set to
	 * true (objects/binders?async=true). This helps speed up the response time
	 * from Vault when processing large amounts of data.
	 *
	 * @param async Set to true to process async
	 * @return The request
	 */
	public BinderRequest setAsync(Boolean async) {
		this.async = async;
		return this;
	}

	/**
	 * Export Binder with Attachments
	 * Include latest or all attachments
	 *
	 * @param exportAttachmentType all or latest
	 * @return The request
	 */
	public BinderRequest setExportAttachmentType(AttachmentType exportAttachmentType) {
		this.exportAttachmentType = exportAttachmentType;
		return this;
	}

	/**
	 * Export Binder with Audit Trails
	 * Include document audit trails
	 *
	 * @param exportAudit true/false
	 * @return The request
	 */
	public BinderRequest setExportAudit(Boolean exportAudit) {
		this.exportAudit = exportAudit;
		return this;
	}

	/**
	 * Export Binder with Audit Trail Format
	 * Audit Trail format can be CSV, PDF, or TEXT
	 *
	 * @param exportAuditFormatType csv, pdf or text
	 * @return The request
	 */
	public BinderRequest setExportAuditFormatType(AuditFormatType exportAuditFormatType) {
		this.exportAuditFormatType = exportAuditFormatType;
		return this;
	}

	/**
	 * Export Binder with Document Metadata
	 * Include document fields
	 *
	 * @param exportDocumentMetadata true/false
	 * @return The request
	 */
	public BinderRequest setExportDocumentMetadata(Boolean exportDocumentMetadata) {
		this.exportDocumentMetadata = exportDocumentMetadata;
		return this;
	}

	/**
	 * Export Binder Document Versions
	 * Include major or major/minor versions
	 *
	 * @param exportDocumentVersionType major or major_minor
	 * @return The request
	 */
	public BinderRequest setExportDocumentVersionType(DocumentVersionType exportDocumentVersionType) {
		this.exportDocumentVersionType = exportDocumentVersionType;
		return this;
	}

	/**
	 * Export Binder with Rendition Types
	 * Include a specific rendition type
	 *
	 * @param exportRenditionType rendition api name (viewable_rendition__v_
	 * @return The request
	 */
	public BinderRequest setExportSource(String exportRenditionType) {
		this.exportRenditionType = exportRenditionType;
		return this;
	}

	/**
	 * Export Binder with Document Source Content
	 * Include document content
	 *
	 * @param exportSource true/false
	 * @return The request
	 */
	public BinderRequest setExportSource(Boolean exportSource) {
		this.exportSource = exportSource;
		return this;
	}
}
