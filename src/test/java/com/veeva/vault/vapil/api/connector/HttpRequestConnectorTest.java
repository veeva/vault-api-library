package com.veeva.vault.vapil.api.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpResponseConnector;
import com.veeva.vault.vapil.extension.FileHelper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

@Tag("HttpRequestConnectorTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("HttpRequestConnector should")
public class HttpRequestConnectorTest {

    private static final String BASIC_SETTINGS_FILE_NAME = "settings_vapil_basic.json";
    private static JsonNode basicSettingsNode;

    @BeforeAll
    static void setup() {
        File settingsFile = FileHelper.getSettingsFile(BASIC_SETTINGS_FILE_NAME);
        basicSettingsNode = FileHelper.readSettingsFile(settingsFile);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully send an API request to Vault")
    class TestSend {

        private HttpResponseConnector responseConnector = null;

        @Test
        @Order(1)
        void testRequest() {
            String vaultDns = basicSettingsNode.get("vaultDNS").asText();
            String endpoint = String.format("/api/%s/auth", VaultClient.VAULT_API_VERSION);
            String url = String.format("https://%s%s", vaultDns, endpoint);
            String vaultUsername = basicSettingsNode.get("vaultUsername").asText();
            String vaultPassword = basicSettingsNode.get("vaultPassword").asText();

            HttpRequestConnector requestConnector = new HttpRequestConnector(url);
            requestConnector.addBodyParam("username", vaultUsername);
            requestConnector.addBodyParam("password", vaultPassword);

            responseConnector = requestConnector.send(HttpRequestConnector.HttpMethod.POST);
            assertNotNull(responseConnector);
        }

        @Test
        @Order(2)
        void testResponse() {
            assertEquals(200, responseConnector.getStatusCode());
            assertEquals("application/json;charset=UTF-8", responseConnector.getContentType());
            assertNotNull(responseConnector.getHeaders());
            assertNotNull(responseConnector.getResponse());
        }
    }
}
