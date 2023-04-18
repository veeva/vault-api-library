/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.SandboxEntitlementResponse;
import com.veeva.vault.vapil.api.model.response.SandboxResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Sandbox Vaults
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#sandbox-vaults">https://developer.veevavault.com/api/23.1/#sandbox-vaults</a>
 */
public class SandboxRequest extends VaultRequest {


	private static final String URL_SANDBOX_ENTITLEMENTS = "/objects/sandbox_entitlements";
	private static final String URL_SANDBOX_ENTITLEMENTS_SET = "/objects/sandbox/entitlements/set";
	private static final String URL_SANDBOX = "/objects/sandbox";
	private static final String URL_SANDBOX_DETAILS = "/objects/sandbox/{vault_id}";
	private static final String URL_RECHECK_USAGE_LIMIT = "/objects/sandbox/actions/recheckusage";
	private static final String URL_CHANGE_SANDBOX_SIZE = "/objects/sandbox/batch/changesize";
	private static final String URL_REFRESH_SANDBOX_FROM_SNAPSHOT = "/objects/sandbox/{vault_id}/actions/refresh";
	private static final String URL_DELETE_SANDBOX = "/objects/sandbox/{name}";
	private static final String URL_SANDBOX_SNAPSHOTS = "/objects/sandbox/snapshot";
	private static final String URL_UPDATE_SNAPSHOT = "/objects/sandbox/snapshot/{api_name}/actions/update";
	private static final String URL_UPGRADE_SNAPSHOT = "/objects/sandbox/snapshot/{api_name}/actions/upgrade";
	private static final String URL_DELETE_SNAPSHOT = "/objects/sandbox/snapshot/{api_name}";
	private static final String URL_PRODUCTION_BUILD = "/api/{version}/objects/sandbox/actions/buildproduction";
	private static final String URL_PRODUCTION_PROMOTE = "/api/{version}/objects/sandbox/actions/promoteproduction";

	private static final String SANDBOX_ADD_REQUESTER = "add_requster";
	private static final String SANDBOX_ALLOWANCE = "allowance";
	private static final String SANDBOX_DOMAIN = "domain";
	private static final String SANDBOX_GRANT = "grant";
	private static final String SANDBOX_NAME = "name";
	private static final String SANDBOX_RELEASE = "release";
	private static final String SANDBOX_SOURCE = "source";
	private static final String SANDBOX_SIZE = "size";
	private static final String SANDBOX_TEMPORARY_ALLOWANCE = "temporary_allowance";
	private static final String SANDBOX_TYPE = "type";
	private static final String SANDBOX_SOURCE_SNAPSHOT = "source_snapshot";
	private static final String SOURCE_SANDBOX = "source_sandbox";
	private static final String DESCRIPTION = "description";
	private static final String INCLUDE_DATA = "include_data";


	private Boolean addRequester;
	private ReleaseType release;
	private SandboxSource source;
	private String sourceSnapshot;
	private SandboxSize size;

	/**
	 * <b>Retrieve Sandboxes</b>
	 * <p>
	 * Retrieve information about the sandbox vaults for the authenticated vault.
	 *
	 * @return SandboxResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-sandboxes' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-sandboxes</a>
	 */
	public SandboxResponse retrieveSandboxes() {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX);
		HttpRequestConnector request = new HttpRequestConnector(url);

		SandboxResponse sandboxResponse = send(HttpMethod.GET, request, SandboxResponse.class);
		return sandboxResponse;
	}

	/**
	 * <b>Retrieve Sandbox Details by ID</b>
	 * <p>
	 * Retrieve information about the sandbox for the given vault ID.
	 *
	 * @param vaultId vault id
	 * @return SandboxDetailsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox/{vault_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-sandbox-details-by-id' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-sandbox-details-by-id</a>
	 */
	public SandboxDetailsResponse retrieveSandboxDetailsById(int vaultId) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_DETAILS).replace("{vault_id}", String.valueOf(vaultId));
		HttpRequestConnector request = new HttpRequestConnector(url);

		SandboxDetailsResponse response = send(HttpMethod.GET, request, SandboxDetailsResponse.class);
		return response;
	}

	/**
	 * <b>Recheck Sandbox Usage Limit</b>
	 * <p>
	 * Recalculate the usage values of the sandbox Vaults for the authenticated Vault. This action can be initiated up to three (3) times in a 24-hour period.
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/actions/recheckusage</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#recheck-sandbox-usage-limit' target='_blank'>https://developer.veevavault.com/api/23.1/#recheck-sandbox-usage-limit</a>
	 */
	public VaultResponse recheckUsageLimit() {
		String url = vaultClient.getAPIEndpoint(URL_RECHECK_USAGE_LIMIT);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Change Sandbox Size</b>
	 * <p>
	 * Change the size of a sandbox Vault for the authenticated Vault.
	 * You can initiate this action if there are sufficient allowances and the current sandbox meets the data and user limits of the requested size.
	 *
	 * @param name The name of the sandbox
	 * @param sandboxSize The sandbox size to be set
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/batch/changesize</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#change-sandbox-size' target='_blank'>https://developer.veevavault.com/api/23.1/#change-sandbox-size</a>
	 */
	public VaultResponse changeSandboxSize(String name, SandboxSize sandboxSize) {
		String url = vaultClient.getAPIEndpoint(URL_CHANGE_SANDBOX_SIZE);
		HttpRequestConnector request = new HttpRequestConnector(url);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("size", sandboxSize.getValue());
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonObject);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addRawString("application/json", jsonArray.toString());
		VaultResponse response = send(HttpMethod.POST, request, VaultResponse.class);
		return response;
	}

	/**
	 * <b>Retrieve Sandbox Entitlements</b>
	 * <p>
	 * Retrieve the total number of available and number of in-use sandbox vaults for the authenticated vault.
	 *
	 * @return SandboxEntitlementResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox_entitlements</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-sandbox-entitlements' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-sandbox-entitlements</a>
	 */
	public SandboxEntitlementResponse retrieveSandboxEntitlements() {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_ENTITLEMENTS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		SandboxEntitlementResponse response = send(HttpMethod.GET, request, SandboxEntitlementResponse.class);
		return response;
	}

	/**
	 * <b>Set Sandbox Entitlements</b>
	 * <p>
	 * Set new sandbox entitlements, including granting and revoking allowances, for the given sandbox name.
	 *
	 * @param name               The name of the sandbox vault, which appears on the My Vaults page
	 * @param type               The type of sandbox. At this time, the only available type is config.
	 * @param allowance          The number of entitlements to grant or revoke.
	 * @param grant              Allowed values true and false. True grants allowances and false revokes them.
	 * @param temporaryAllowance The number of temporary sandbox allowances to grant or revoke.
	 * @param size size of the sandbox
	 * @return SandboxResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/entitlements/set</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#set-sandbox-entitlements' target='_blank'>https://developer.veevavault.com/api/23.1/#set-sandbox-entitlements</a>
	 */
	public SandboxResponse setSandboxEntitlements(String name,
												  String type,
												  int allowance,
												  boolean grant,
												  int temporaryAllowance,
												  SandboxSize size) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_ENTITLEMENTS_SET);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SANDBOX_NAME, name);
		request.addBodyParam(SANDBOX_TYPE, type);
		request.addBodyParam(SANDBOX_ALLOWANCE, allowance);
		request.addBodyParam(SANDBOX_GRANT, grant);
		request.addBodyParam(SANDBOX_TEMPORARY_ALLOWANCE, temporaryAllowance);
		request.addBodyParam(SANDBOX_SIZE, size.getValue());

		SandboxResponse response = send(HttpMethod.POST, request, SandboxResponse.class);
		return response;
	}

	/**
	 * Create a new sandbox for the currently authenticated Vault.
	 * Include the source_snapshot parameter in the request body to create a new sandbox from an existing snapshot.
	 * Providing a name which already exists will refresh the existing sandbox Vault. You can also refresh a sandbox from a snapshot.
	 *
	 * @param type   The type of sandbox, such as config.
	 * @param domain The domain to use for the new sandbox.
	 * @param name   The name of the sandbox vault, which appears on the My Vaults page.
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-or-refresh-sandbox' target='_blank'>https://developer.veevavault.com/api/23.1/#create-or-refresh-sandbox</a>
	 */
	public JobCreateResponse createOrRefreshSandbox(String type, String domain, String name) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SANDBOX_TYPE, type);
		request.addBodyParam(SANDBOX_DOMAIN, domain);
		request.addBodyParam(SANDBOX_NAME, name);

		if (this.source != null) {
			request.addBodyParam(SANDBOX_SOURCE, source);
		}

		if (this.sourceSnapshot != null) {
			request.addBodyParam(SANDBOX_SOURCE_SNAPSHOT, sourceSnapshot);
		}

		if (this.size != null) {
			request.addBodyParam(SANDBOX_SIZE, size.getValue());
		}

		if (this.addRequester != null) {
			request.addBodyParam(SANDBOX_ADD_REQUESTER, addRequester);
		}
		if (this.release != null) {
			request.addBodyParam(SANDBOX_RELEASE, release);
		}

		JobCreateResponse response = send(HttpMethod.POST, request, JobCreateResponse.class);
		return response;
	}

	/**
	 * Refresh a sandbox Vault in the currently authenticated Vault from an existing snapshot.
	 *
	 * @param vaultId The Vault ID of the sandbox to be refreshed
	 * @param sourceSnapshot The api_name of the snapshot to refresh the sandbox from
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/{vault_id}/actions/refresh</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#refresh-sandbox-from-snapshot' target='_blank'>https://developer.veevavault.com/api/23.1/#refresh-sandbox-from-snapshot</a>
	 */
	public JobCreateResponse refreshSandboxFromSnapshot(int vaultId, String sourceSnapshot) {
		String url = vaultClient.getAPIEndpoint(URL_REFRESH_SANDBOX_FROM_SNAPSHOT).replace("{vault_id}", String.valueOf(vaultId));
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SANDBOX_SOURCE_SNAPSHOT, sourceSnapshot);

		JobCreateResponse response = send(HttpMethod.POST, request, JobCreateResponse.class);
		return response;
	}


	/**
	 * Delete a sandbox vault. Note that you cannot delete a sandbox which was created or refreshed within the last 24 hours.
	 *
	 * @param name The name of the sandbox vault to delete. This is the name which appears on the My Vaults page
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/sandbox/{name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-sandbox' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-sandbox</a>
	 */
	public VaultResponse deleteSandbox(String name) {
		String url = vaultClient.getAPIEndpoint(URL_DELETE_SANDBOX).replace("{name}", name);
		HttpRequestConnector request = new HttpRequestConnector(url);

		VaultResponse response = send(HttpMethod.DELETE, request, VaultResponse.class);
		return response;
	}

	/**
	 * <b>Create Sandbox Snapshot</b>
	 * <p>
	 * Create a new sandbox snapshot for the indicated sandbox Vault.
	 *
	 * @param sourceSandbox 	The name of the sandbox Vault to take a snapshot of
	 * @param name 				The name of the new snapshot
	 * @param description 		The description of the new snapshot
	 * @param includeData		Set to true to include data as part of the snapshot. Set to false to include only configuration
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/snapshot</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#create-sandbox-snapshot' target='_blank'>https://developer.veevavault.com/api/23.1/#create-sandbox-snapshot</a>
	 */
	public JobCreateResponse createSandboxSnapshot(String sourceSandbox, String name, String description, Boolean includeData) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_SNAPSHOTS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SOURCE_SANDBOX, sourceSandbox);
		request.addBodyParam(SANDBOX_NAME, name);
		request.addBodyParam(DESCRIPTION, description);
		request.addBodyParam(INCLUDE_DATA, includeData);

		JobCreateResponse response = send(HttpMethod.POST, request, JobCreateResponse.class);
		return response;
	}

	/**
	 * <b>Retrieve Sandbox Snapshots</b>
	 * <p>
	 * Retrieve information about sandbox snapshots managed by the authenticated Vault.
	 *
	 * @return SandboxSnapshotResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox/snapshot</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-sandbox-snapshots' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-sandbox-snapshots</a>
	 */

	public SandboxSnapshotResponse retrieveSandboxSnapshots() {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_SNAPSHOTS);
		HttpRequestConnector request = new HttpRequestConnector(url);

		SandboxSnapshotResponse response = send(HttpMethod.GET, request, SandboxSnapshotResponse.class);
		return response;
	}

	/**
	 * <b>Delete Sandbox Snapshot</b>
	 * <p>
	 * Delete a sandbox snapshot managed by the authenticated Vault. Deleted snapshots cannot be recovered.
	 *
	 * @param apiName The API name of the sandbox snapshot to delete
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/sandbox/snapshot/{api_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#delete-sandbox-snapshot' target='_blank'>https://developer.veevavault.com/api/23.1/#delete-sandbox-snapshot</a>
	 */
	public VaultResponse deleteSandboxSnapshot(String apiName) {
		String url = vaultClient.getAPIEndpoint(URL_DELETE_SNAPSHOT).replace("{api_name}", apiName);
		HttpRequestConnector request = new HttpRequestConnector(url);

		VaultResponse response = send(HttpMethod.DELETE, request, VaultResponse.class);

		return response;
	}

	/**
	 * <b>Update Sandbox Snapshot</b>
	 * <p>
	 * Recreate a sandbox snapshot for the same source sandbox Vault. This request replaces the existing snapshot with the newly created one.
	 *
	 * @param apiName The API name of the sandbox snapshot to update
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/snapshot/{api_name}/actions/update</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#update-sandbox-snapshot' target='_blank'>https://developer.veevavault.com/api/23.1/#update-sandbox-snapshot</a>
	 */

	public JobCreateResponse updateSandboxSnapshot(String apiName) {
		String url = vaultClient.getAPIEndpoint(URL_UPDATE_SNAPSHOT).replace("{api_name}", apiName);;
		HttpRequestConnector request = new HttpRequestConnector(url);

		JobCreateResponse response = send(HttpMethod.POST, request, JobCreateResponse.class);
		return response;
	}

	/**
	 * <b>Upgrade Sandbox Snapshot</b>
	 * <p>
	 * Upgrade a sandbox snapshot to match the release version of the source sandbox Vault.
	 * Your request to upgrade a snapshot is only valid if the upgrade_status=Upgrade Available or Upgrade Required.
	 *
	 * @param apiName The API name of the sandbox snapshot to upgrade
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/snapshot/{api_name}/actions/upgrade</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#upgrade-sandbox-snapshot' target='_blank'>https://developer.veevavault.com/api/23.1/#upgrade-sandbox-snapshot</a>
	 */

	public JobCreateResponse upgradeSandboxSnapshot(String apiName) {
		String url = vaultClient.getAPIEndpoint(URL_UPGRADE_SNAPSHOT).replace("{api_name}", apiName);;
		HttpRequestConnector request = new HttpRequestConnector(url);

		JobCreateResponse response = send(HttpMethod.POST, request, JobCreateResponse.class);
		return response;
	}


	/**
	 * Given a built pre-production vault, promote it to a production vault. This is analogous to the Promote action in the Vault UI.
	 *
	 * @param source the name of the source vault to build. This can be the current pre-production vault or a sandbox vault.
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/actions/buildproduction</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#build-production-vault' target='_blank'>https://developer.veevavault.com/api/23.1/#build-production-vault</a>
	 */
	public JobCreateResponse buildProductionVault(String source) {
		String url = vaultClient.getAPIEndpoint(URL_PRODUCTION_BUILD);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam(SANDBOX_SOURCE, source);
		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Given a pre-production vault, build a production vault. This is analogous to the Build action in the Vault UI. After building your vault, you can promote it to production.
	 *
	 * You can build or rebuild the source vault for a given pre-production vault no more than three times in a 24 hour period.
	 *
	 * @param name The name of the pre-production vault to promote.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/actions/promoteproduction</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#build-production-vault' target='_blank'>https://developer.veevavault.com/api/23.1/#build-production-vault</a>
	 */
	public VaultResponse promoteToProduction(String name) {
		String url = vaultClient.getAPIEndpoint(URL_PRODUCTION_PROMOTE);
		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam(SANDBOX_NAME, name);
		return send(HttpMethod.POST, request, VaultResponse.class);
	}


	/**
	 * Set the AddRequester for createOrRefreshSandbox
	 *
	 * @param addRequester addRequester
	 * @return SandboxRequest
	 */
	public SandboxRequest setAddRequester(Boolean addRequester) {
		this.addRequester = addRequester;
		return this;
	}

	/**
	 * Set the release type for createOrRefreshSandbox
	 *
	 * @param release release
	 * @return SandboxRequest
	 */
	public SandboxRequest setRelease(ReleaseType release) {
		this.release = release;
		return this;
	}

	/**
	 * Set the source to refresh the sandbox from for createOrRefreshSandbox
	 *
	 * @param source source
	 * @return SandboxRequest
	 */
	public SandboxRequest setSource(SandboxSource source) {
		this.source = source;
		return this;
	}

	/**
	 * If the source is a snapshot, provide the api_name of the snapshot to create the sandbox from.
	 *
	 * @param sourceSnapshot sourceSnapshot
	 * @return SandboxRequest
	 */
	public SandboxRequest setSourceSnapshot(String sourceSnapshot) {
		this.sourceSnapshot = sourceSnapshot;
		return this;
	}

	/**
	 * Set the size of the sandbox for createOrRefreshSandbox
	 *
	 * @param size size
	 * @return SandboxRequest
	 */
	public SandboxRequest setSize(SandboxSize size) {
		this.size = size;
		return this;
	}

	/**
	 * The source to refresh the sandbox from. This can be a vault or a snapshot.
	 */
	public enum SandboxSource {
		VAULT("vault"),
		SNAPSHOT("snapshot");

		String value;
		SandboxSource(String value) {
			if (value != null) {
				this.value = value;
			}
		}

		public String getValue() {return value;}
	}

	/**
	 * The type of release. This can be general, limited, or prerelease.
	 */
	public enum ReleaseType {
		GENERAL("general"),
		LIMITED("limited"),
		PRERELEASE("prerelease");

		String value;

		ReleaseType(String value) {
			if (value != null) {
				this.value = value.toUpperCase().trim();
			}
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * The sandbox sizes. This can be Small, Large, or Full.
	 */
	public enum SandboxSize {
		SMALL("Small"),
		LARGE("Large"),
		FULL("Full");

		String value;

		SandboxSize(String value) {
			if (value != null) {
				this.value = value;
			}
		}

		public String getValue() {return value;}
	}

	private class SandboxSizeParams {
		@JsonProperty("size")
		private String size;

		@JsonProperty("name")
		private String name;

		SandboxSizeParams(String size, String name) {
			this.size = size;
			this.name = name;
		}
	}
}
