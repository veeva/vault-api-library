package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DomainResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DomainRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Domain request should")
public class DomainRequestTest {

    private static final String SUCCESS_STATUS = "SUCCESS";
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    @DisplayName("successfully let Domain Admons retrieve a list of all Vaults currently their domain")
    public void testRetrieveDomainInformation(TestReporter reporter) {
        DomainResponse resp = vaultClient.newRequest(DomainRequest.class)
                .setIncludeApplications(true)
                .retrieveDomainInformation();
        assertEquals(SUCCESS_STATUS, resp.getResponseStatus());
        assertNotNull(resp.getDomain());
        assertNotNull(resp.getDomain().getVaults());
        reporter.publishEntry(String.format("Domain Name = %s, Domain Type = %s\n",resp.getDomain().getDomainName(),resp.getDomain().getDomainType()));
        StringBuilder sb = new StringBuilder("\n");
        resp.getDomain().getVaults().forEach(dv -> sb.append(String.format("\tName = %s, Id = %s\n", dv.getVaultName(),dv.getId())));
        reporter.publishEntry(sb.toString());
    }

    @Test
    @DisplayName("successfully let Non-domain Admins retrieve a list of all their domains, including the domain of the current Vault")
    public void testRetrieveDomains() {
        VaultResponse resp = vaultClient.newRequest(DomainRequest.class)
                .retrieveDomains();
        assertTrue(resp.isSuccessful());
        assertEquals(SUCCESS_STATUS, resp.getResponseStatus());
    }
}
