/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.MetaDataUserResponse;
import com.veeva.vault.vapil.api.model.response.UserBulkResponse;
import com.veeva.vault.vapil.api.model.response.UserPermissionResponse;
import com.veeva.vault.vapil.api.model.response.UserResponse;
import com.veeva.vault.vapil.api.model.response.UserRetrieveResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Users
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/22.2/#users">https://developer.veevavault.com/api/22.2/#users</a>
 */
public class UserRequest extends VaultRequest {
	private static Logger log = Logger.getLogger(UserRequest.class);

	//API Endpoints
	private static final String URL_METADATA = "/metadata/objects/users";
	private static final String URL_USERS = "/objects/users";
	private static final String URL_USER_ID = "/objects/users/{user_id}";
	private static final String URL_USER_CHANGE_PASSWORD = "/objects/users/{user_id}/password";
	private static final String URL_USER_VAULT_MEMBERSHIP = "/objects/users/{user_id}/vault_membership/{vault_id}";
	private static final String URL_USER_PERMISSIONS = "/objects/users/{user_id}/permissions";

	// query string parameters for user retrieval
	private static final String HTTP_QUERYSTRING_VAULTS = "vaults";
	private static final String HTTP_QUERYSTRING_LIMIT = "limit";
	private static final String HTTP_QUERYSTRING_START = "start";
	private static final String HTTP_QUERYSTRING_SORT = "sort";
	private static final String HTTP_QUERYSTRING_EXCLUDE_VAULT = "exclude_vault_membership";
	private static final String HTTP_QUERYSTRING_EXCLUDE_APP = "exclude_app_licensing";

	// parameters for changing user password
	private static final String CURRENT_PASSWORD = "password__v";
	private static final String NEW_PASSWORD = "new_password__v";

	// parameter for user permissions filter
	private static final String PERMISSIONS_FILTER = "filter";

	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private Map<String, Object> bodyParams = null;
	private String headerAccept;
	private String headerContentType;
	private String inputPath;
	private String idParam;
	private String operation;
	private String outputPath;
	private String vaults;
	private String limit;
	private String start;
	private String sort;
	private Boolean domain = false;
	private Boolean excludeVaultMembership;
	private Boolean excludeAppLicensing;
	private String filter;

	private UserRequest() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
	}

	/**
	 * <b>Retrieve User Metadata</b>
	 * <p>
	 * Get a full list of fields for users
	 *
	 * @return MetaDataUserResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/objects/users</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-user-metadata' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-user-metadata</a>
	 * @vapil.request <pre>
	 * MetaDataUserResponse resp = vaultClient.newRequest(UserRequest.class).retrieveUserMetadata();</pre>
	 * @vapil.response <pre>
	 *
	 * for (User u : resp.getProperties()) {
	 *   System.out.println("Name: " + u.getName());
	 *   System.out.println("Type: " + u.getType());
	 *   System.out.println("Length: " + u.getLength());
	 *   System.out.println("Editable?: " + u.getEditable());
	 *   System.out.println("Queryable?: " + u.getQueryable());
	 *   System.out.println("Required?: " + u.getRequired());
	 *   System.out.println("Multi-value?: " + u.getMultivalue());
	 *   System.out.println("Editable on create?: " + u.getOnCreateEditable());
	 *   if (u.getValues() != null) {
	 *     for (Values v : u.getValues()) {
	 *       System.out.println("Value Name: " + v.getValue());
	 *       System.out.println("Value Label: " + v.getLabel());
	 *     }
	 *   }
	 * }
	 * </pre>
	 */
	public MetaDataUserResponse retrieveUserMetadata() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_METADATA));

		return send(HttpMethod.GET, request, MetaDataUserResponse.class);
	}

	/**
	 * <b>Retrieve All Users</b>
	 * <p>
	 * Get a list of all metadata components and their properties.
	 * Note that the Vault API returns only the active users.
	 * Reference "metadata" endpoint for inactive users.
	 *
	 * @return UserRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/users</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-all-users' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-all-users</a>
	 * @vapil.request <pre>
	 * UserRetrieveResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .retrieveAllUsers();</pre>
	 * @vapil.response <pre>
	 * for (UserRetrieveResponse.Users users : resp.getUsers()) {
	 *   User u = users.getUser();
	 *   System.out.println("\nUser: " + u.getUserName());
	 *   System.out.println("User ID: " + u.getId());
	 *   System.out.println("Name: " + u.getUserFirstName() + " " + u.getUserLastName());
	 *   System.out.println("Email: " + u.getUserEmail());
	 *   System.out.println("Timezone: " + u.getUserTimezone());
	 *   System.out.println("Locale: " + u.getUserLocale());
	 *   System.out.println("Language: " + u.getUserLanguage());
	 *   System.out.println("Company: " + u.getCompany());
	 *   System.out.println("Title: " + u.getUserTitle());
	 *   System.out.println("Office Phone: " + u.getOfficePhone());
	 *   System.out.println("Fax: " + u.getFax());
	 *   System.out.println("Mobile: " + u.getMobilePhone());
	 *   System.out.println("Site: " + u.getSite());
	 *   System.out.println("Domain Admin?: " + u.getIsDomainAdmin());
	 *   System.out.println("Active user?: " + u.getActive());
	 *   System.out.println("Active on domain?: " + u.getDomainActive());
	 *   System.out.println("Security Profile: " + u.getSecurityProfile());
	 *   System.out.println("License type: " + u.getLicenseType());
	 *   System.out.println("Security Policy ID: " + u.getSecurityPolicyId());
	 *   System.out.println("User needs to change password?: " + u.getUserNeedsToChangePassword());
	 *   System.out.println("Created by: " + u.getCreatedBy() + " on " + u.getCreatedDate());
	 *   System.out.println("Last modified by: " + u.getModifiedBy() + " on " + u.getModifiedDate());
	 *   System.out.println("Domain ID: " + u.getDomainId());
	 *   System.out.println("Domain Name: " + u.getDomainName());
	 *   System.out.println("Vault ID: " + u.getVaultId());
	 *   System.out.println("Federated ID: " + u.getFederatedId());
	 *   System.out.println("Salesforce User Name: " + u.getSalesforceUserName());
	 *   System.out.println("Last login at: " + u.getLastLogin());
	 *   System.out.println("Group IDs: " + u.getGroupId());	 *
	 * }</pre>
	 */
	public UserRetrieveResponse retrieveAllUsers() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_USERS));

		if (vaults != null && !vaults.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_VAULTS, vaults);
		if (limit != null && !limit.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_LIMIT, limit);
		if (start != null && !start.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_START, start);
		if (sort != null && !sort.isEmpty())
			request.addQueryParam(HTTP_QUERYSTRING_SORT, sort);
		if (excludeVaultMembership != null)
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDE_VAULT, excludeVaultMembership);
		if (excludeAppLicensing != null)
			request.addQueryParam(HTTP_QUERYSTRING_EXCLUDE_APP, excludeAppLicensing);

		return send(HttpMethod.GET, request, UserRetrieveResponse.class);
	}

	/**
	 * <b>Retrieve User</b>
	 * <p>
	 * Retrieve information for one user.
	 *
	 * @param userId user id
	 * @return UserRetrieveResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/users/{id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-user' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-user</a>
	 * @vapil.request <pre>
	 * UserRetrieveResponse resp = vaultClient.newRequest(UserRequest.class).retrieveUser(userId);</pre>
	 * @vapil.response <pre>
	 * for(UserRetrieveResponse.Users users : resp.getAllUsers()) {
	 *   User u = users.getUser();
	 *   System.out.println("\nUser name: " + u.getUserName());
	 *   System.out.println("User: " + u.getUserFirstName() + " " + u.getUserLastName());
	 *   System.out.println("Email: " + u.getUserEmail());
	 * }
	 * </pre>
	 */
	public UserRetrieveResponse retrieveUser(int userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_ID)
				.replace("{user_id}", String.valueOf(userId));

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, UserRetrieveResponse.class);
	}

	/**
	 * <b>Create Single User</b>
	 *
	 * @return UserResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/users</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#create-single-user' target='_blank'>https://developer.veevavault.com/api/22.2/#create-single-user</a>
	 * @vapil.request <pre>
	 * UserResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setContentTypeForm()
	 *                 .setBodyParams(formData)
	 *                 .createSingleUser();</pre>
	 * @vapil.response <pre>displayResults(resp,true);
	 * </pre>
	 */
	public UserResponse createSingleUser() {
		return sendUserRequest(HttpMethod.POST, URL_USERS);
	}

	/**
	 * <b>Update Single User</b>
	 * <p>
	 * Update metadata for a single user. Can also be used to update
	 * the current user with the 'me' parameter
	 *
	 * @param userId user id
	 * @return UserResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/users/{id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#update-single-user' target='_blank'>https://developer.veevavault.com/api/22.2/#update-single-user</a>
	 * @vapil.request <pre>
	 * UserResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setContentTypeXForm()
	 *                 .setBodyParams(formData)
	 *                 .updateSingleUser(4005489);</pre>
	 * @vapil.response <pre>
	 * displayResults(resp,true);</pre>
	 */
	public UserResponse updateSingleUser(int userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_ID)
				.replace("{user_id}", String.valueOf(userId));
		return sendUserRequest(HttpMethod.PUT, url);
	}


	/**
	 * <b>Change Your Password</b>
	 * <p>
	 * Change the password for the currently authenticated user
	 *
	 * @param userId          The user 'id' field value. You can also use the value 'me' to change your password
	 * @param currentPassword The user's current password
	 * @param newPassword     The user's new password to set in Vault
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/users/{user_id}/password</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#change-your-password' target='_blank'>https://developer.veevavault.com/api/22.2/#change-your-password</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(UserRequest.class).changePassword(userId, oldPass, newPass);</pre>
	 * @vapil.response <pre>System.out.println(resp.getResponseStatus());
	 * }</pre>
	 */
	public VaultResponse changePassword(int userId, String currentPassword, String newPassword) {
		String url = vaultClient.getAPIEndpoint(URL_USER_CHANGE_PASSWORD)
				.replace("{user_id}", String.valueOf(userId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addBodyParam(CURRENT_PASSWORD, currentPassword);
		request.addBodyParam(NEW_PASSWORD, newPassword);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Retrieve User Permissions</b>
	 * <p>
	 * Retrieve all object and object field permissions (read, edit, create, delete)
	 * assigned to a specific user.
	 *
	 * @param userId The user 'id' field value. You can also use the value 'me' to change your password
	 * @return UserResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/users/{id}/permissions</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-user-permissions' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-user-permissions</a>
	 * @vapil.request <pre>
	 * UserPermissionResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .retrieveUserPermissions(userId);</pre>
	 * @vapil.response <pre>
	 * for(UserPermissions u : resp.getData()) {
	 *   System.out.println("Name: " + u.getName());
	 *   PermissionSet p = u.getPermissions();
	 *   System.out.println("\tRead: " + p.getRead());
	 *   System.out.println("\tEdit: " + p.getEdit());
	 *   System.out.println("\tCreate: " + p.getCreate());
	 *   System.out.println("\tDelete: " + p.getDelete());
	 * }</pre>
	 */
	public UserPermissionResponse retrieveUserPermissions(int userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_PERMISSIONS)
				.replace("{user_id}", String.valueOf(userId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		if (filter != null && !filter.isEmpty())
			request.addQueryParam(PERMISSIONS_FILTER, filter);

		return send(HttpMethod.GET, request, UserPermissionResponse.class);
	}

	/**
	 * <b>Disable User</b>
	 * <p>
	 * Disable a user in a specific vault, or disable a user in all vaults in the domain.
	 *
	 * @param userId The user 'id' field value
	 * @return UserResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/users/{user_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#disable-user' target='_blank'>https://developer.veevavault.com/api/22.2/#disable-user</a>
	 * @vapil.request <pre>
	 * UserResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setDomainAsTrue()
	 *                 .disableUser(4005489);</pre>
	 * @vapil.response <pre>
	 * displayResults(resp, true);
	 * }</pre>
	 */
	public UserResponse disableUser(int userId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_ID)
				.replace("{user_id}", String.valueOf(userId));


		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addQueryParam("domain", domain);

		return send(HttpMethod.DELETE, request, UserResponse.class);
	}

	/**
	 * <b>Bulk create users</b>
	 * <p>
	 * Note that it is up to the calling code to batch the data.
	 *
	 * @return UserBulkResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/users</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#create-multiple-users' target='_blank'>https://developer.veevavault.com/api/22.2/#create-multiple-users</a>
	 * @vapil.request <pre>
	 * UserBulkResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setContentTypeCsv()
	 *                 .setInputPath(filePath)
	 *                 .setOperation("upsert")
	 *                 .setIdParam(idParam)
	 *                 .createUserRecords();</pre>
	 * @vapil.response <pre>
	 * for (UserResponse ur : resp.getData()) {
	 *   System.out.println("User record successful?: " + ur.isSuccessful());
	 *     if (ur.isSuccessful()) {
	 *       if (ur.getUserResultInfo() != null)
	 *         System.out.println(ur.getUserResultInfo().getId() + " " + ur.getUserResultInfo().getResponseStatus());
	 *       else
	 *         System.out.println(ur.getId() + " " + ur.getResponseStatus());
	 *       }
	 *
	 *       if (ur.getErrors() != null) {
	 *         for (VaultResponse.APIResponseError err : ur.getErrors()) {
	 *           System.out.println("Error (" + err.getType() + ") - " + err.getMessage());
	 *         }
	 *       }
	 *    }
	 * }</pre>
	 */
	public UserBulkResponse createUserRecords() {
		return sendBulkUserRequest(HttpMethod.POST, URL_USERS);
	}

	/**
	 * <b>Bulk update users</b>
	 * <p>
	 * Note that it is up to the calling code to batch the data.
	 *
	 * @return UserBulkResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/users</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#update-multiple-users' target='_blank'>https://developer.veevavault.com/api/22.2/#update-multiple-users</a>
	 * @vapil.request <pre>
	 * UserBulkResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setContentTypeCsv()
	 *                 .setInputPath(filePath)
	 *                 .updateUserRecords();</pre>
	 * @vapil.response <pre>
	 * for (UserResponse ur : resp.getData()) {
	 *   System.out.println("User record successful?: " + ur.isSuccessful());
	 *     if (ur.isSuccessful()) {
	 *       if (ur.getUserResultInfo() != null)
	 *         System.out.println(ur.getUserResultInfo().getId() + " " + ur.getUserResultInfo().getResponseStatus());
	 *       else
	 *         System.out.println(ur.getId() + " " + ur.getResponseStatus());
	 *       }
	 *
	 *       if (ur.getErrors() != null) {
	 *         for (VaultResponse.APIResponseError err : ur.getErrors()) {
	 *           System.out.println("Error (" + err.getType() + ") - " + err.getMessage());
	 *         }
	 *       }
	 *    }
	 * }</pre>
	 */
	public UserBulkResponse updateUserRecords() {
		return sendBulkUserRequest(HttpMethod.PUT, URL_USERS);
	}

	/**
	 * <b>Update Vault Membership</b>
	 * <p>
	 * Update vault membership for an existing user. This cannot
	 * be used to create new users or update other profile information.
	 *
	 * @param userId  The ID of the user
	 * @param vaultId The ID of the Vault
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/objects/users/{user_id}/vault_membership/{vault_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#update-vault-membership' target='_blank'>https://developer.veevavault.com/api/22.2/#update-vault-membership</a>
	 * @vapil.request <pre>
	 * VaultResponse resp = vaultClient.newRequest(UserRequest.class)
	 *                 .setContentTypeXForm()
	 *                 .updateVaultMembership(4005489,23700);</pre>
	 * @vapil.response <pre>System.out.println("Status = " + resp.isSuccessful());</pre>
	 */
	public VaultResponse updateVaultMembership(int userId, int vaultId) {
		String url = vaultClient.getAPIEndpoint(URL_USER_VAULT_MEMBERSHIP)
				.replace("{user_id}", String.valueOf(userId))
				.replace("{vault_id}", String.valueOf(vaultId));

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		return send(HttpMethod.PUT, request, VaultResponse.class);
	}

	/**
	 * Private method to combine create/update methods
	 * to a single call
	 *
	 * @param method The HTTP Method to perform
	 * @return UserResponse
	 */
	private UserResponse sendUserRequest(HttpMethod method, String APIEndpoint) {
		if (!isValidCRUDRequest()) return null;

		String url = vaultClient.getAPIEndpoint(APIEndpoint);

		ObjectMapper objectMapper = getBaseObjectMapper();

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		}

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		if (method.equals(HttpMethod.POST)) {
			if (operation != null && !operation.isEmpty()
					&& idParam != null && !idParam.isEmpty()) {
				request.addQueryParam("operation", operation);
				request.addQueryParam("idParam", idParam);
			}
		}

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV))
			if (outputPath != null) {
				return sendToFile(method, request, outputPath, UserResponse.class);
			} else {
				return sendReturnBinary(method, request, UserResponse.class);
			}
		else
			return send(method, request, objectMapper, UserResponse.class);
	}

	/**
	 * Private method to combine create/update methods
	 * to a single call
	 *
	 * @param method The HTTP Method to perform
	 * @return UserBulkResponse
	 */
	private UserBulkResponse sendBulkUserRequest(HttpMethod method, String APIEndpoint) {
		if (!isValidCRUDRequest()) return null;

		String url = vaultClient.getAPIEndpoint(APIEndpoint);

		ObjectMapper objectMapper = getBaseObjectMapper();

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, headerContentType);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, headerAccept);

		if (bodyParams != null && !bodyParams.isEmpty()) {
			request.setBodyParams(bodyParams);
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		}

		if (inputPath != null && !inputPath.isEmpty())
			request.addFile(headerContentType, inputPath);

		if (binaryFile != null)
			request.addBinary(headerContentType, binaryFile.getBinaryContent());

		if (method.equals(HttpMethod.POST)) {
			if (operation != null && !operation.isEmpty()
					&& idParam != null && !idParam.isEmpty()) {
				request.addQueryParam("operation", operation);
				request.addQueryParam("idParam", idParam);
			}
		}

		// Return binary if the Accept is CSV
		if (headerAccept.equalsIgnoreCase(HttpRequestConnector.HTTP_CONTENT_TYPE_CSV))
			if (outputPath != null) {
				return sendToFile(method, request, outputPath, UserBulkResponse.class);
			} else {
				return sendReturnBinary(method, request, UserBulkResponse.class);
			}
		else
			return send(method, request, objectMapper, UserBulkResponse.class);
	}

	/**
	 * Determine if the Create-Read-Update-Delete request
	 * is properly formed before sending
	 *
	 * @return True if the request is properly formed
	 */
	private boolean isValidCRUDRequest() {
		if (headerContentType == null) {
			log.error("Invalid request - no content type is set");
			return false;
		}

		// Verify there is data - file, binary, or name/value pairs
		if (inputPath == null || inputPath.isEmpty()) {
			if (binaryFile == null) {
				if (bodyParams == null || bodyParams.isEmpty()) {
					log.error("Invalid request - no source data");
					return false;
				}
			}
		}
		return true;
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Set parameter for retrieving users from vaults
	 * <p>
	 *
	 * @param vaults the vaults to retrieve users from
	 * @return UserRequest
	 */
	public UserRequest setVaults(String vaults) {
		this.vaults = vaults;
		return this;
	}

	/**
	 * Set limit value on retrieving all users
	 * <p>
	 *
	 * @param limit the size of the result set in the page
	 * @return UserRequest
	 */
	public UserRequest setLimit(String limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Set start value on retrieving all users
	 * <p>
	 *
	 * @param start the starting record number
	 * @return UserRequest
	 */
	public UserRequest setStart(String start) {
		this.start = start;
		return this;
	}

	/**
	 * Set the sorting option and field on retrieving all users
	 * <p>
	 *
	 * @param sort the sort order for the result set
	 * @return UserRequest
	 */
	public UserRequest setSort(String sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * Set whether to exclude vault membership when retrieving all users
	 * <p>
	 *
	 * @param excludeVaultMembership set whether to exclude vault membership fields
	 * @return UserRequest
	 */
	public UserRequest setExcludeVaultMembership(Boolean excludeVaultMembership) {
		this.excludeVaultMembership = excludeVaultMembership;
		return this;
	}

	/**
	 * Set the filter to retrieve user permissions
	 *
	 * @param permissionName format object.{object name}.{object or field}_actions. Wildcards are not supported. See example below.
	 * @return UserRequest
	 */
	public UserRequest setFilter(String permissionName) {
		this.filter = "name__v::" + permissionName;
		return this;
	}

	/**
	 * Set whether to exclude app licensing when retrieving all users
	 * <p>
	 *
	 * @param excludeAppLicensing set whether to exclude app licensing fields
	 * @return UserRequest
	 */
	public UserRequest setExcludeAppLicensing(Boolean excludeAppLicensing) {
		this.excludeAppLicensing = excludeAppLicensing;
		return this;
	}

	/**
	 * Set the Header Accept to return CSV
	 *
	 * @return The Request
	 */
	public UserRequest setAcceptCSV() {
		this.headerAccept = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public UserRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Single record form ObjectRecordRequest with name/value body parameters
	 *
	 * @param bodyParams Key/Value map of the body parameters
	 * @return The Request
	 */
	public UserRequest setBodyParams(Map<String, Object> bodyParams) {
		this.bodyParams = bodyParams;
		headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM;
		return this;
	}

	/**
	 * Set the Header Content Type to CSV
	 *
	 * @return The Request
	 */
	public UserRequest setContentTypeCsv() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_CSV;
		return this;
	}

	/**
	 * Set the Header Content Type to JSON
	 *
	 * @return The Request
	 */
	public UserRequest setContentTypeJson() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_JSON;
		return this;
	}

	/**
	 * Set the Header Content Type to multipart/form-data
	 *
	 * @return The Request
	 */
	public UserRequest setContentTypeForm() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM;
		return this;
	}

	/**
	 * Set the Header Content Type to x-www-form-urlencoded
	 *
	 * @return The Request
	 */
	public UserRequest setContentTypeXForm() {
		this.headerContentType = HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public UserRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Specify an UPSERT operation via the idParam for creating users
	 *
	 * @param idParam API for the UPSERT
	 * @return The Request
	 */
	public UserRequest setIdParam(String idParam) {
		this.idParam = idParam;
		return this;
	}

	/**
	 * Specify an UPSERT operation for creating users
	 *
	 * @param operation API for the UPSERT
	 * @return The Request
	 */
	public UserRequest setOperation(String operation) {
		this.operation = operation;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public UserRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Set the domain query parameter as true
	 *
	 * @return The Request
	 */
	public UserRequest setDomainAsTrue() {
		this.domain = true;
		return this;
	}

}
