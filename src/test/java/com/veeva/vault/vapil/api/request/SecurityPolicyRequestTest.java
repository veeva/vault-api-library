package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.SecurityPoliciesResponse;
import com.veeva.vault.vapil.api.model.response.SecurityPolicyResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.List;

@Tag("SecurityPolicy")
@ExtendWith(VaultClientParameterResolver.class)
public class SecurityPolicyRequestTest {

    public final String POLICY_NAME = "6653"; //Depends on Vault where test is run

    @Test
    public void testRetrieveAllSecurityPolicies(VaultClient vaultClient) {
        SecurityPolicyRequest request = vaultClient.newRequest(SecurityPolicyRequest.class);
        SecurityPoliciesResponse response = request.retrieveAllSecurityPolicies();

        Assertions.assertTrue(response.isSuccessful());
        List<SecurityPoliciesResponse.SecurityPolicy> securityPolicies = response.getSecurityPolicies();
        Assertions.assertNotNull(securityPolicies);
        securityPolicies.forEach(securityPolicy -> {
            Assertions.assertNotNull(securityPolicy.getName());
            Assertions.assertNotNull(securityPolicy.getLabel());
            Assertions.assertNotNull(securityPolicy.getValue());
        });
    }

    @Test
    public void testRetrieveSecurityPolicy(VaultClient vaultClient) {

        SecurityPolicyResponse response = vaultClient.newRequest(SecurityPolicyRequest.class).retrieveSecurityPolicy(POLICY_NAME);
        Assertions.assertTrue(response.isSuccessful());
        SecurityPolicyResponse.SecurityPolicy securityPolicy = response.getSecurityPolicy();
        Assertions.assertNotNull(securityPolicy);

        SecurityPolicyResponse.SecurityPolicy.PolicyDetails policyDetails = securityPolicy.getPolicyDetails();
        Assertions.assertNotNull(policyDetails);
        Assertions.assertNotNull(policyDetails.getName());
        Assertions.assertNotNull(policyDetails.getLabel());
        Assertions.assertNotNull(policyDetails.getDescription());

        SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings policySecuritySettings = securityPolicy.getPolicySecuritySettings();
        SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings.AuthenticationType authenticationType = policySecuritySettings.getAuthenticationType();
        Assertions.assertNotNull(authenticationType.getName());
        Assertions.assertNotNull(authenticationType.getLabel());
    }
}
