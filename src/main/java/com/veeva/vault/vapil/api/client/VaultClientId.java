/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.client;

/**
 * Construct a Vault Client Id, which is required by VAPIL for all integration calls.
 * <p>
 * The Vault Client Id appears in the API Usage Logs for tracking and logging purposes. It is in the form:<br>
 * {company}-{organization}-{component/team}-{server | client}-{program}
 * <p>
 * For example, an integration developed by Verteo Biotech, Inc could be:<br>
 * verteobiotech-vault-quality-client-myapp
 * <p>
 * The Vault Client Id can be set via the following approaches.
 * <p>
 * Set in the constructor:
 * <pre>
 * VaultClientId clientId = new VaultClientId("verteobiotech","vault","quality",true,"myapp");</pre>
 * Set via setters after instantiating. This example sets it using builder pattern setters.
 * <pre>
 * VaultClientId clientId = new VaultClientId()
 * 		.setCompany("verteobiotech")
 * 		.setOrganization("vault")
 * 		.setComponentTeam("quality")
 * 		.setIsClient(true)
 * 		.setProgramName("myapp");</pre>
 *
 * @see <a href="https://developer.veevavault.com/docs/#client-id">https://developer.veevavault.com/docs/#client-id</a>
 */
public class VaultClientId {

	private String company;
	private String organization;
	private String componentTeam;
	private Boolean isClient;
	private String programName;

	/**
	 * Instantiate the Vault Client Id and later set the parameters,
	 * such as using the builder pattern setters.
	 * <p>
	 * Example
	 * <pre>
	 * VaultClientId clientId = new VaultClientId()
	 * 		.setCompany("veeva")
	 * 		.setOrganization("vault")
	 * 		.setComponentTeam("devsupport")
	 * 		.setIsClient(true)
	 * 		.setProgramName("vapiltest");
	 * </pre>
	 */
	public VaultClientId() {
	}


	/**
	 * Instantiate the Vault Client Id by passing in all parameters
	 *
	 * @param company       The customer
	 * @param organization  The customer organization
	 * @param componentTeam The development team
	 * @param isClient      True if a client
	 * @param programName   Interface name
	 */
	public VaultClientId(String company, String organization, String componentTeam, Boolean isClient, String programName) {
		this.company = company;
		this.organization = organization;
		this.componentTeam = componentTeam;
		this.isClient = isClient;
		this.programName = programName;
	}

	/**
	 * Retrieve the Vault Client Id in the form
	 * {company}-{organization}-{component/team}-{server | client}-{program}
	 * <p>
	 * Example: verteobiotech-vault-quality-client-myapp
	 *
	 * @return Fully formed Vault Client Id string
	 */
	public String getClientId() {
		String clientServer = "server";
		if (isClient) clientServer = "client";

		String clientId = company + "-" + organization + "-" + componentTeam + "-" + clientServer + "-" + programName;

		return clientId;
	}

	/**
	 * Determine if all Vault Client Id parameters are populated, as determined by not null and not empty properties
	 *
	 * @return True for valid
	 */
	public Boolean isValid() {
		Boolean b = true;

		if (company == null || company.isEmpty()) return false;
		if (organization == null || organization.isEmpty()) return false;
		if (componentTeam == null || componentTeam.isEmpty()) return false;
		if (isClient == null) return false;
		if (programName == null || programName.isEmpty()) return false;

		return b;
	}

	public String getCompany() {
		return company;
	}

	public VaultClientId setCompany(String company) {
		this.company = company;
		return this;
	}

	public String getOrganization() {
		return organization;
	}

	public VaultClientId setOrganization(String organization) {
		this.organization = organization;
		return this;
	}

	public String getComponentTeam() {
		return componentTeam;
	}

	public VaultClientId setComponentTeam(String componentTeam) {
		this.componentTeam = componentTeam;
		return this;
	}

	public Boolean getIsClient() {
		return isClient;
	}

	public VaultClientId setIsClient(Boolean isClient) {
		this.isClient = isClient;
		return this;
	}

	public String getProgramName() {
		return programName;
	}

	public VaultClientId setProgramName(String programName) {
		this.programName = programName;
		return this;
	}

}
