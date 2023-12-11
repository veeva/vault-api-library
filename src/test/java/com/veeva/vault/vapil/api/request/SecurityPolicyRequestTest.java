package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.metadata.SecurityPolicy;
import com.veeva.vault.vapil.api.model.response.MetaDataSecurityPolicyResponse;
import com.veeva.vault.vapil.api.model.response.SecurityPoliciesResponse;
import com.veeva.vault.vapil.api.model.response.SecurityPolicyResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.List;

@Tag("SecurityPolicyRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Security Policy Request should")
public class SecurityPolicyRequestTest {

    private static String policyName;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    @DisplayName("successfully retrieve security policy metadata")
    public void testRetrieveSecurityPolicyMetadata() {
        MetaDataSecurityPolicyResponse response = vaultClient.newRequest(SecurityPolicyRequest.class)
                .retrieveSecurityPolicyMetaData();

        Assertions.assertTrue(response.isSuccessful());

        SecurityPolicy metadata = response.getMetadata();
        Assertions.assertNotNull(metadata);
        Assertions.assertEquals("security_policy", metadata.getName());
        Assertions.assertEquals("Security Policy", metadata.getDescription());

        List<SecurityPolicy.Property> properties = metadata.getProperties();
        Assertions.assertNotNull(properties);
        properties.forEach(property -> {
            Assertions.assertNotNull(property.getName());
            Assertions.assertNotNull(property.getDescription());
            Assertions.assertNotNull(property.getType());
            Assertions.assertNotNull(property.getObjectTypeReferenced());
        });

        List<SecurityPolicy.Object> objects = metadata.getObjects();
        Assertions.assertNotNull(objects);
        objects.forEach(object -> {
            Assertions.assertNotNull(object.getName());
            Assertions.assertNotNull(object.getDescription());
            Assertions.assertNotNull(object.getProperties());
        });

        List<SecurityPolicy.Object.Property> objectProperties = objects.get(0).getProperties();
        Assertions.assertNotNull(objectProperties);
        objectProperties.forEach(objectProperty -> {
            Assertions.assertNotNull(objectProperty.getName());
            Assertions.assertNotNull(objectProperty.getDescription());
            Assertions.assertNotNull(objectProperty.getType());
        });
    }

    @Test
    @Order(1)
    @DisplayName("successfully retrieve all security policies")
    public void testRetrieveAllSecurityPolicies() {
        SecurityPoliciesResponse response = vaultClient.newRequest(SecurityPolicyRequest.class)
                .retrieveAllSecurityPolicies();

        Assertions.assertTrue(response.isSuccessful());

        List<SecurityPoliciesResponse.SecurityPolicy> securityPolicies = response.getSecurityPolicies();
        Assertions.assertNotNull(securityPolicies);
        securityPolicies.forEach(securityPolicy -> {
            Assertions.assertNotNull(securityPolicy.getName());
            Assertions.assertNotNull(securityPolicy.getLabel());
            Assertions.assertNotNull(securityPolicy.getValue());
        });

        policyName = securityPolicies.get(0).getName();
    }

    @Test
    @Order(2)
    @DisplayName("successfully retrieve a security policy by name__v value")
    public void testRetrieveSecurityPolicy() {
        SecurityPolicyResponse response = vaultClient.newRequest(SecurityPolicyRequest.class)
                .retrieveSecurityPolicy(policyName);
        Assertions.assertTrue(response.isSuccessful());
        SecurityPolicyResponse.SecurityPolicy securityPolicy = response.getSecurityPolicy();
        Assertions.assertNotNull(securityPolicy);

        SecurityPolicyResponse.SecurityPolicy.PolicyDetails policyDetails = securityPolicy.getPolicyDetails();
        Assertions.assertNotNull(policyDetails);
        Assertions.assertNotNull(policyDetails.getName());
        Assertions.assertNotNull(policyDetails.getLabel());

        SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings policySecuritySettings = securityPolicy.getPolicySecuritySettings();
        Assertions.assertNotNull(policySecuritySettings);
        SecurityPolicyResponse.SecurityPolicy.PolicySecuritySettings.AuthenticationType authenticationType = policySecuritySettings.getAuthenticationType();
        Assertions.assertNotNull(authenticationType.getName());
        Assertions.assertNotNull(authenticationType.getLabel());
    }
}
