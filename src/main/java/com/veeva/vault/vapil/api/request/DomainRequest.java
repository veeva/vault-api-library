/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.DomainResponse;
import com.veeva.vault.vapil.api.model.response.DomainsResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * Retrieve Domain specific information
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/24.2/#domain-information">https://developer.veevavault.com/api/24.2/#domain-information</a>
 */
public class DomainRequest extends VaultRequest<DomainRequest> {

	// API Endpoints
	private static final String URL_DOMAIN = "/objects/domain";
	private static final String URL_DOMAINS = "/objects/domains";

	// API Request Parameters
	private Boolean includeApplications;

	private DomainRequest() {
		this.includeApplications = false;
	}

	/**
	 * <b>Retrieve Domain Information</b>
	 * <p>
	 * Domain Admins can use this request to retrieve a list of all vaults currently in their domain.
	 *
	 * @return DomainResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/domain</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-domain-information' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-domain-information</a>
	 * @vapil.request <pre>
	 * DomainResponse resp = vaultClient.newRequest(DomainRequest.class)
	 * 		.setIncludeApplications(includeApplication)
	 * 		.retrieveDomainInformation();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Domain Name = " + response.getDomain().getDomainName());
	 * System.out.println("Domain Type = " + response.getDomain().getDomainType());
	 * for (DomainResponse.Domain.DomainVault vault : response.getDomain().getVaults()) {
	 * 		System.out.println(("--------Vault--------"));
	 * 		System.out.println("Id = " + vault.getId());
	 * 		System.out.println("Name = " + vault.getVaultName());
	 * 		System.out.println("Status = " + vault.getVaultStatus());
	 * }
	 * </pre>
	 */
	public DomainResponse retrieveDomainInformation() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOMAIN));

		if (includeApplications != null && includeApplications)
			request.addQueryParam("include_application", Boolean.toString(includeApplications));

		return send(HttpMethod.GET, request, DomainResponse.class);
	}

	/**
	 * <b>Retrieve Domains</b>
	 * <p>
	 * Retrieve a list of all vaults currently in their domain,
	 * including the domain of the current vault. This endpoint can
	 * be used for non-domain admins.
	 *
	 * @return DomainsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/domains</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/24.2/#retrieve-domains' target='_blank'>https://developer.veevavault.com/api/24.2/#retrieve-domains</a>
	 * @vapil.request <pre>
	 * DomainsResponse response = vaultClient.newRequest(DomainRequest.class)
	 * 		.retrieveDomains();
	 * </pre>
	 * @vapil.response <pre>
	 * for (DomainsResponse.Domain domain : response.getDomains()) {
	 * 		System.out.println(("--------Domain--------"));
	 * 		System.out.println("Name = " + domain.getName());
	 * 		System.out.println("Type = " + domain.getType());
	 * }
	 * </pre>
	 */
	public DomainsResponse retrieveDomains() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DOMAINS));

		return send(HttpMethod.GET, request, DomainsResponse.class);
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * To include Vault application type information in the response,
	 * set includeApplications to true. If false, application information
	 * is not included.
	 *
	 * @param includeApplications Set to true to include application type information
	 * @return The request
	 */
	public DomainRequest setIncludeApplications(Boolean includeApplications) {
		this.includeApplications = includeApplications;
		return this;
	}

}
