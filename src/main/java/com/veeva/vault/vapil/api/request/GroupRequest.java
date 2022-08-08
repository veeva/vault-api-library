/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.GroupRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.GroupResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataGroupResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Retrieve Domain specific information
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/22.2/#domain-information">https://developer.veevavault.com/api/22.2/#domain-information</a>
 */
public class GroupRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(GroupRequest.class);

	// API Endpoints
	private static final String URL_GROUP_METADATA = "/metadata/objects/groups";
	private static final String URL_GROUP_RETRIEVE = "/objects/groups/{group_id}";
	private static final String URL_GROUPS_RETRIEVE_ALL = "/objects/groups";
	private static final String URL_GROUPS_RETRIEVE_ALL_AUTO = "/objects/groups/auto";
	private static final String URL_GROUP_UPDATE_DELETE = "/objects/groups/{group_id}";
	private static final String URL_GROUP_CREATE = "/objects/groups";

	private static final String DELIM_COMMA = ",";

	private static final String PARAM_LABEL = "label__v";
	private static final String PARAM_MEMBERS = "members__v";
	private static final String PARAM_SECURITY_PROFILES = "security_profiles__v";
	private static final String PARAM_ACTIVE = "active__v";
	private static final String PARAM_GROUP_DESCRIPTION = "group_description__v";
	private static final String PARAM_ALLOW_DELEGATION_AMONG_MEMBERS = "allow_delegation_among_members__v";

	// API Request Parameters
	private String label;
	private Boolean includeImplied;
	private List<Integer> members;
	private List<String> securityProfiles;
	private Boolean active;
	private String groupDescription;
	private Boolean allowDelegationAmongMembers;

	private GroupRequest() {
		this.includeImplied = false;
	}

	/**
	 * Retrieve Group Metadata
	 * <br>
	 * Retrieve metadata for the group object
	 *
	 * @return The response
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/groups</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-group-metadata' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-group-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataGroupResponse resp = vaultClient.newRequest(GroupRequest.class)
	 * 				.retrieveGroupMetadata();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 *
	 * List&lt;Group&gt; allGroupMetaData = resp.getProperties();
	 *
	 * if (allGroupMetaData == null || allGroupMetaData.isEmpty()) {
	 *   System.out.println("ERROR: No group metadata returned");
	 * } else {
	 *   int i = 0;
	 *   for (Group g : allGroupMetaData) {
	 *     System.out.println("Name = " + g.getName());
	 *     System.out.println("Type = " + g.getType());
	 *     System.out.println("Object = " + g.getObject());
	 *     System.out.println("Length = " + g.getLength());
	 *     System.out.println("Editable = " + g.getEditable());
	 *     System.out.println("Queryable = " + g.getQueryable());
	 *     System.out.println("Required = " + g.getRequired());
	 *     System.out.println("Multivalue = " + g.getMultivalue());
	 *     System.out.println("On Create Editable = " + g.getOnCreateEditable());
	 *     System.out.println("*********************");
	 *     i++;
	 *     if (i &gt; 10) break;
	 *   }
	 * }</pre>
	 */
	public MetaDataGroupResponse retrieveGroupMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_GROUP_METADATA));

		return send(HttpMethod.GET, request, MetaDataGroupResponse.class);
	}

	/**
	 * Retrieve all groups except Auto Managed groups.
	 *
	 * @param autoManaged Set to true to retrieve Auto Managed groups, false to retrieve all groups except Auto Managed groups
	 * @return GroupRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/groups</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-all-groups' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-all-groups</a>
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/groups/auto</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#' target='_blank'>https://developer.veevavault.com/api/22.2/#</a>
	 * @vapil.request <pre>
	 * GroupRetrieveResponse resp = vaultClient.newRequest(GroupRequest.class)
	 * 				.setIncludeImplied(includeImplied)
	 * 				.retrieveAllGroups(autoManaged);</pre>
	 * @vapil.response <pre>
	 * System.out.println("autoManaged = " + autoManaged);
	 * System.out.println("includeImplied = " + includeImplied);
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * </pre>
	 */
	public GroupRetrieveResponse retrieveAllGroups(boolean autoManaged) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(autoManaged ? URL_GROUPS_RETRIEVE_ALL_AUTO : URL_GROUPS_RETRIEVE_ALL));

		if (includeImplied != null && includeImplied)
			request.addQueryParam("includeImplied", String.valueOf(includeImplied).toLowerCase());

		return send(HttpMethod.GET, request, GroupRetrieveResponse.class);
	}

	/**
	 * Retrieve Group
	 *
	 * @param groupId The id for the group
	 * @return GroupRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/groups/{group_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-group' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-group</a>
	 * @vapil.request <pre>
	 * GroupRetrieveResponse resp = vaultClient.newRequest(GroupRequest.class)
	 * 				.setIncludeImplied(includeImplied)
	 * 				.retrieveGroup(groupId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("includeImplied = " + includeImplied);
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Group ID = " + groupId);
	 * </pre>
	 */
	public GroupRetrieveResponse retrieveGroup(long groupId) {
		String url = vaultClient.getAPIEndpoint(URL_GROUP_RETRIEVE);
		url = url.replace("{group_id}", Long.valueOf(groupId).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);

		if (includeImplied != null && includeImplied)
			request.addQueryParam("includeImplied", Boolean.toString(includeImplied));

		return send(HttpMethod.GET, request, GroupRetrieveResponse.class);
	}

	/**
	 * Create a group
	 *
	 * @param label Group label. Vault uses this to create the group name__v value.
	 * @return GroupResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/groups</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#create-group' target='_blank'>https://developer.veevavault.com/api/22.2/#create-group</a>
	 * @vapil.request <pre>
	 * GroupResponse resp = vaultClient.newRequest(GroupRequest.class)
	 * 				.createGroup(label);</pre>
	 * @vapil.response <pre>
	 * if (resp != null) {
	 *   System.out.println("Status = " + resp.getResponseStatus());
	 *   System.out.println("Message = " + resp.getResponseMessage());
	 *   System.out.println("Label = " + label);
	 *   System.out.println("Group ID = " + resp.getId());
	 * } else {
	 *   System.out.println("Null response. Invalid request.");
	 * }</pre>
	 */
	public GroupResponse createGroup(String label) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_GROUP_CREATE));

		request.addBodyParam(PARAM_LABEL, label);

		return sendGroupRequest(HttpMethod.POST, request);
	}

	/**
	 * Update editable group field values. Add or remove group members and security profiles.
	 *
	 * @param groupId ID of the group to update
	 * @return GroupResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/groups/{group_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#update-group' target='_blank'>https://developer.veevavault.com/api/22.2/#update-group</a>
	 * @vapil.request <pre>
	 * GroupResponse resp = vaultClient.newRequest(GroupRequest.class).setLabel("testGroup" + rndNumStr)
	 * 				.setActive(false)
	 * 				.setGroupDescription("Description " + rndNumStr)
	 * 				.setAllowDelegationAmongMembers(true)
	 * 				.updateGroup(groupId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Message = " + resp.getResponseMessage());
	 * System.out.println("Group ID = " + resp.getId());
	 * </pre>
	 */
	public GroupResponse updateGroup(long groupId) {
		String url = vaultClient.getAPIEndpoint(URL_GROUP_UPDATE_DELETE).replace("{group_id}", Long.valueOf(groupId).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);

		if ((label != null) && (!label.isEmpty())) {
			request.addBodyParam(PARAM_LABEL, label);
		}

		return sendGroupRequest(HttpMethod.PUT, request);
	}

	/**
	 * Delete a user-defined group. You cannot delete system-managed groups.
	 *
	 * @param groupId ID of group to delete
	 * @return GroupResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/groups/{group_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#delete-group' target='_blank'>https://developer.veevavault.com/api/22.2/#delete-group</a>
	 * @vapil.request <pre>
	 * GroupResponse resp = vaultClient.newRequest(GroupRequest.class).deleteGroup(groupId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * System.out.println("Group ID = " + resp.getId());
	 * </pre>
	 */
	public GroupResponse deleteGroup(long groupId) {
		String url = vaultClient.getAPIEndpoint(URL_GROUP_UPDATE_DELETE).replace("{group_id}", Long.valueOf(groupId).toString());
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		return send(HttpMethod.DELETE, request, GroupResponse.class);
	}

	/**
	 * Private method to combine build, validation and send of create/update methods
	 * to a single call
	 *
	 * @param method  The HTTP Method to perform
	 * @param request The HTTP request
	 * @return GroupResponse
	 */
	private GroupResponse sendGroupRequest(HttpMethod method, HttpRequestConnector request) {
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		if (members != null && !members.isEmpty()) {
			request.addBodyParam(PARAM_MEMBERS, members.stream().map(String::valueOf).collect(Collectors.joining(DELIM_COMMA)));
		}
		if (securityProfiles != null && !securityProfiles.isEmpty()) {
			request.addBodyParam(PARAM_SECURITY_PROFILES, String.join(DELIM_COMMA, securityProfiles));
		}
		if (active != null) {
			request.addBodyParam(PARAM_ACTIVE, active);
		}
		//Allow empty string
		if (groupDescription != null) {
			request.addBodyParam(PARAM_GROUP_DESCRIPTION, groupDescription);
		}
		if (allowDelegationAmongMembers != null) {
			request.addBodyParam(PARAM_ALLOW_DELEGATION_AMONG_MEMBERS, allowDelegationAmongMembers);
		}


		return (isValidCRUDRequest(request)) ? send(method, request, GroupResponse.class) : null;
	}

	/**
	 * Determine if the Create-Read-Update-Delete request
	 * is properly formed before sending
	 *
	 * @return True if the request is properly formed
	 */
	private boolean isValidCRUDRequest(HttpRequestConnector request) {

		// Verify there is data name/value pairs
		if (request.getBodyParams() == null || request.getBodyParams().isEmpty()) {
			log.error("Invalid request - no source data");
			return false;
		}
		return true;
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set the label__v body parameter. Vault uses this to create the group name__v value.
	 *
	 * @param label Group name/label
	 * @return The request
	 */
	public GroupRequest setLabel(String label) {
		this.label = label;
		return this;
	}

	/**
	 * To include the implied_members__v field in the response,
	 * set includeImplied to true. If false, the response includes only the
	 * members__v field
	 *
	 * @param includeImplied Set to true to include implied members
	 * @return The request
	 */
	public GroupRequest setIncludeImplied(Boolean includeImplied) {
		this.includeImplied = includeImplied;
		return this;
	}

	/**
	 * Set the members__v body parameter to manually assign individual users to the group
	 *
	 * @param members List of user IDs
	 * @return The request
	 */
	public GroupRequest setMembers(List<Integer> members) {
		this.members = members;
		return this;
	}

	/**
	 * Set the security_profiles__v body parameter to automatically adds all users with the security profile
	 * to the group. These are implied_members__v.
	 *
	 * @param securityProfiles List of security profiles
	 * @return The request
	 */
	public GroupRequest setSecurityProfiles(List<String> securityProfiles) {
		this.securityProfiles = securityProfiles;
		return this;
	}

	/**
	 * Set the active__v body parameter to create the group as active or inactive. By default, the new
	 * group will be created as active. To set the group to inactive, set this value to false
	 *
	 * @param active Active/inactive state of group
	 * @return The request
	 */
	public GroupRequest setActive(Boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * Set the group_description__v body parameter add a description of the group
	 *
	 * @param groupDescription Group description
	 * @return The request
	 */
	public GroupRequest setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
		return this;
	}

	/**
	 * Set the allow_delegation_among_members__v body parameter to specifiy that members of this group will only
	 * be allowed to delegate access to other members of the same group. You can set this field for user and
	 * system managed groups, with the exception of the Vault Owners group. If omitted, defaults to false.
	 *
	 * @param allowDelegationAmongMembers True to allow delegate access to other members of the group
	 * @return The request
	 */
	public GroupRequest setAllowDelegationAmongMembers(Boolean allowDelegationAmongMembers) {
		this.allowDelegationAmongMembers = allowDelegationAmongMembers;
		return this;
	}
}
