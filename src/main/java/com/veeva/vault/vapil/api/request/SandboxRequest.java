/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.SandboxEntitlementResponse;
import com.veeva.vault.vapil.api.model.response.SandboxResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Sandbox Vaults
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/22.2/#sandbox-vaults">https://developer.veevavault.com/api/22.2/#sandbox-vaults</a>
 */
public class SandboxRequest extends VaultRequest {


	private static final String URL_SANDBOX_ENTITLEMENTS = "/objects/sandbox_entitlements";
	private static final String URL_SANDBOX_ENTITLEMENTS_SET = "/objects/sandbox/entitlements/set";
	private static final String URL_SANDBOX = "/objects/sandbox";
	private static final String URL_SANDBOX_DETAILS = "/objects/sandbox/{vault_id}";
	private static final String URL_DELETE_SANDBOX = "/objects/sandbox/{name}";
	private static final String URL_PRODUCTION_BUILD = "/api/{version}/objects/sandbox/actions/buildproduction";
	private static final String URL_PRODUCTION_PROMOTE = "/api/{version}/objects/sandbox/actions/promoteproduction";

	private static final String SANDBOX_ADD_REQUESTER = "add_requster";
	private static final String SANDBOX_ALLOWANCE = "allowance";
	private static final String SANDBOX_DOMAIN = "domain";
	private static final String SANDBOX_GRANT = "grant";
	private static final String SANDBOX_NAME = "name";
	private static final String SANDBOX_RELEASE = "release";
	private static final String SANDBOX_SOURCE = "source";
	private static final String SANDBOX_TEMPORARY_ALLOWANCE = "temporary_allowance";
	private static final String SANDBOX_TYPE = "type";


	private Boolean addRequester;
	private ReleaseType release;

	/**
	 * <b>Retrieve Sandboxes</b>
	 * <p>
	 * Retrieve information about the sandbox vaults for the authenticated vault.
	 *
	 * @return SandboxResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-sandboxes' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-sandboxes</a>
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
	 * @return SandboxResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/sandbox/{vault_id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-sandbox-details-by-id' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-sandbox-details-by-id</a>
	 */
	public SandboxResponse retrieveSandboxDetailsById(int vaultId) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_DETAILS).replace("{vault_id}", String.valueOf(vaultId));
		HttpRequestConnector request = new HttpRequestConnector(url);

		SandboxResponse response = send(HttpMethod.GET, request, SandboxResponse.class);
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#retrieve-sandbox-entitlements' target='_blank'>https://developer.veevavault.com/api/22.2/#retrieve-sandbox-entitlements</a>
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
	 * @return SandboxResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/entitlements/set</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#set-sandbox-entitlements' target='_blank'>https://developer.veevavault.com/api/22.2/#set-sandbox-entitlements</a>
	 */
	public SandboxResponse setSandboxEntitlements(String name,
												  String type,
												  int allowance,
												  boolean grant,
												  int temporaryAllowance) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX_ENTITLEMENTS_SET);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SANDBOX_NAME, name);
		request.addBodyParam(SANDBOX_TYPE, type);
		request.addBodyParam(SANDBOX_ALLOWANCE, allowance);
		request.addBodyParam(SANDBOX_GRANT, grant);
		request.addBodyParam(SANDBOX_TEMPORARY_ALLOWANCE, temporaryAllowance);

		SandboxResponse response = send(HttpMethod.POST, request, SandboxResponse.class);
		return response;
	}

	/**
	 * Create a new sandbox for the currently authenticated vault. Providing a name which already exists will refresh the existing sandbox vault.
	 *
	 * @param type   The type of sandbox, such as config.
	 * @param domain The domain to use for the new sandbox.
	 * @param name   The name of the sandbox vault, which appears on the My Vaults page.
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#create-or-refresh-sandbox' target='_blank'>https://developer.veevavault.com/api/22.2/#create-or-refresh-sandbox</a>
	 */
	public JobCreateResponse createOrRefreshSandbox(String type, String domain, String name) {
		String url = vaultClient.getAPIEndpoint(URL_SANDBOX);
		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addBodyParam(SANDBOX_TYPE, type);
		request.addBodyParam(SANDBOX_DOMAIN, domain);
		request.addBodyParam(SANDBOX_NAME, name);

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
	 * Delete a sandbox vault. Note that you cannot delete a sandbox which was created or refreshed within the last 24 hours.
	 *
	 * @param name The name of the sandbox vault to delete. This is the name which appears on the My Vaults page.
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/objects/sandbox/{name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#delete-sandbox' target='_blank'>https://developer.veevavault.com/api/22.2/#delete-sandbox</a>
	 */
	public VaultResponse deleteSandbox(String name) {
		String url = vaultClient.getAPIEndpoint(URL_DELETE_SANDBOX).replace("{name}", name);
		HttpRequestConnector request = new HttpRequestConnector(url);

		VaultResponse response = send(HttpMethod.DELETE, request, VaultResponse.class);
		return response;
	}


	/**
	 * Given a built pre-production vault, promote it to a production vault. This is analogous to the Promote action in the Vault UI.
	 *
	 * @param source he name of the source vault to build. This can be the current pre-production vault or a sandbox vault.
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/sandbox/actions/buildproduction</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#build-production-vault' target='_blank'>https://developer.veevavault.com/api/22.2/#build-production-vault</a>
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
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/22.2/#build-production-vault' target='_blank'>https://developer.veevavault.com/api/22.2/#build-production-vault</a>
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
}
