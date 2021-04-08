package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DomainResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VaultClientParameterResolver.class)
public class DomainRequestTest {

    @Test
    public void retrieveDomainInformation(VaultClient vaultClient, TestReporter reporter) {
        DomainResponse resp = vaultClient.newRequest(DomainRequest.class)
                .setIncludeApplications(true)
                .retrieveDomainInformation();
        assertEquals(TestProperties.VAULT_API_SUCCESS_STATUS, resp.getResponseStatus());
        assertNotNull(resp.getDomain());
        assertNotNull(resp.getDomain().getVaults());
        reporter.publishEntry(String.format("Domain Name = %s, Domain Type = %s\n",resp.getDomain().getDomainName(),resp.getDomain().getDomainType()));
        StringBuilder sb = new StringBuilder("\n");
        resp.getDomain().getVaults().forEach(dv -> sb.append(String.format("\tName = %s, Id = %s\n", dv.getVaultName(),dv.getId())));
        reporter.publishEntry(sb.toString());
    }

    @Test
    public void retrieveDomainsTest(VaultClient vaultClient) {
        VaultResponse resp = vaultClient.newRequest(DomainRequest.class)
                .retrieveDomains();
        assertTrue(resp.isSuccessful());
        assertEquals(TestProperties.VAULT_API_SUCCESS_STATUS, resp.getResponseStatus());
    }
}
