package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DomainResponse;
import com.veeva.vault.vapil.api.model.response.DomainsResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DomainRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Domain request should")
public class DomainRequestTest {

    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully let Domain Admins retrieve a list of all Vaults currently their domain")
    class TestRetrieveDomainInformation {

        private DomainResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DomainRequest.class)
                    .setIncludeApplications(true)
                    .retrieveDomainInformation();

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getDomain());
            assertNotNull(response.getDomain().getDomainName());
            assertNotNull(response.getDomain().getDomainType());
            List<DomainResponse.Domain.DomainVault> vaults = response.getDomain().getVaults();
            for (DomainResponse.Domain.DomainVault vault : vaults) {
                assertNotNull(vault.getId());
                assertNotNull(vault.getVaultName());
                assertNotNull(vault.getVaultStatus());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve domains")
    class TestRetrieveDomains {

        private DomainsResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DomainRequest.class)
                    .retrieveDomains();

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            List<DomainsResponse.Domain> domains = response.getDomains();
            assertNotNull(domains);
            for (DomainsResponse.Domain domain : domains) {
                assertNotNull(domain.getName());
                assertNotNull(domain.getType());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully set and return Reference ID Header")
    class TestSetHeaderReferenceId {

        private VaultResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DomainRequest.class)
                    .setHeaderReferenceId("test")
                    .retrieveDomains();

            assertTrue(response != null);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertEquals(response.getHeaderReferenceId(), "test");
        }
    }
}
