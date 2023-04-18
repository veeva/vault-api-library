package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.SecurityPoliciesResponse;
import com.veeva.vault.vapil.api.model.response.SecurityPolicyResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * All Security Policy requests
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/23.1/#retrieve-all-security-policies">https://developer.veevavault.com/api/23.1/#retrieve-all-security-policies</a>
 */
public class SecurityPolicyRequest extends VaultRequest {
	//API Endpoints
	private static final String URL_SECURITY_POLICIES = "/objects/securitypolicies";
	private static final String URL_SECURITY_POLICY = "/objects/securitypolicies/{security_policy_name}";

	private SecurityPolicyRequest() {
	}

	/**
	 * <b>Retrieve All Security Policies</b>
	 *
	 * @return SecurityPoliciesResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/securitypolicies</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-all-security-policies' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-all-security-policies</a>
	 * @vapil.request <pre>
	 * SecurityPolicyRequest request = vaultClient.newRequest(SecurityPolicyRequest.class).retrieveAllSecurityPolicies();</pre>
	 * @vapil.response <pre>System.out.println("Retrieving all Security policies");
	 * SecurityPoliciesResponse response = request.retrieveAllSecurityPolicies();
	 *
	 * System.out.println("Security Policies:");
	 * for (SecurityPolicy sp : response.getSecurityPolicies){
	 *   System.out.println("\tName:  " + sp.getName());
	 *   System.out.println("\tLabel:  " + sp.getLabel());
	 *   System.out.println("\tValue:  " + sp.getValue());
	 * }</pre>
	 */
	public SecurityPoliciesResponse retrieveAllSecurityPolicies() {
		String url = vaultClient.getAPIEndpoint(URL_SECURITY_POLICIES);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, SecurityPoliciesResponse.class);
	}

	/**
	 * <b>Retrieve Security Policy</b>
	 * <p>
	 * Depending on the configuration, the response may include some or all of the parameters
	 * in {@link SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings}
	 *
	 * @param securityPolicyName Name of the Security Policy
	 * @return SecurityPolicyResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/securitypolicies/{security_policy_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/23.1/#retrieve-security-policy' target='_blank'>https://developer.veevavault.com/api/23.1/#retrieve-security-policy</a>
	 * @vapil.request <pre>
	 * SecurityPolicyRequest request = vaultClient.newRequest(SecurityPolicyRequest.class).retrieveSecurityPolicy(name);</pre>
	 * @vapil.response <pre>System.out.println("Retrieving Security policy " + name);
	 * SecurityPolicyResponse response = request.retrieveSecurityPolicy(name);
	 * System.out.println("Security Policy:");
	 * SecurityPolicyResponse.SecurityPolicy securityPolicy = response.getSecurityPolicy();
	 * System.out.println("\tPolicy Details:");
	 * SecurityPolicyResponse.SecurityPolicy.PolicyDetails policyDetails = securityPolicy.getPolicyDetails();
	 * System.out.println("\t\tName:  " + policyDetails.getName());
	 * System.out.println("\t\tLabel:  " + policyDetails.getLabel());
	 * System.out.println("\t\tDescription:  " + policyDetails.getDescription());
	 * System.out.println("\t\tIs Active:" + policyDetails.getIsActive());
	 * System.out.println("\tPolicy Security Settings:");
	 * SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings policySecuritySettings = securityPolicy.getPolicySecuritySettings();
	 * System.out.println("\t\tAuthentication Type:  ");
	 * SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings.AuthenticationType authenticationType = policySecuritySettings.getAuthenticationType();
	 * System.out.println("\t\t\tName:  " + authenticationType.getName());
	 * System.out.println("\t\t\tLabel:  " + authenticationType.getLabel());
	 * System.out.println("\t\tPasswords Require Number:  " + policySecuritySettings.getPasswordsRequireNumber());
	 * System.out.println("\t\tPasswords Require Uppercase Letter:  " + policySecuritySettings.getPasswordsRequireUpperCaseLetter());
	 * System.out.println("\t\tPasswords Require Non-Alphanumeric Character:  " + policySecuritySettings.getPasswordsRequireNonAlphaChar());
	 * System.out.println("\t\tMinimum Password Length:  " + policySecuritySettings.getMinPasswordLength());
	 * System.out.println("\t\tPassword Expiration:  " + policySecuritySettings.getPasswordExpiration());
	 * System.out.println("\t\tPassword History Reuse:  " + policySecuritySettings.getPasswordHistoryReuse());
	 * System.out.println("\t\tRequire Question on Password Reset:  " + policySecuritySettings.getRequireQuestionOnPasswordReset());
	 * System.out.println("\t\tAllow Delegated Auth SFDC:  " + policySecuritySettings.getAllowDelegatedAuthSFDC());
	 * System.out.println("\t\tSalesforce Org ID:  " + policySecuritySettings.getSfdcOrgId());</pre>
	 */
	public SecurityPolicyResponse retrieveSecurityPolicy(String securityPolicyName) {
		String url = vaultClient.getAPIEndpoint(URL_SECURITY_POLICY);
		url = url.replace("{security_policy_name}", securityPolicyName);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, SecurityPolicyResponse.class);
	}

}
