package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse.*;
import com.veeva.vault.vapil.extension.FileHelper;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Tag("VaultRequestTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Vault Request Test should")
public class VaultRequestTest {

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
    @DisplayName("return a success response for a valid request")
    class TestSendSuccess {
        VaultResponse sendSuccessResponse;

        @Test
        @Order(1)
        void testRequest() {
            VaultClient client = VaultClient.newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS(basicSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(basicSettingsNode.get("vaultClientId").asText())
                    .withVaultUsername(basicSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword(basicSettingsNode.get("vaultPassword").asText())
                    .build();

            sendSuccessResponse = client.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        void testResponse() {
            assertNotNull(sendSuccessResponse);
            assertEquals("SUCCESS", sendSuccessResponse.getResponseStatus());
            assertTrue(sendSuccessResponse.isSuccessful());
            assertFalse(sendSuccessResponse.hasErrors());
            assertFalse(sendSuccessResponse.hasWarnings());
            assertNull(sendSuccessResponse.getErrors());
            assertNull(sendSuccessResponse.getWarnings());
            assertNotNull(sendSuccessResponse.getResponse());
            assertNotNull(sendSuccessResponse.getHeaders());
            assertNotNull(sendSuccessResponse.getVaultModelData());
            assertNotNull(sendSuccessResponse.getFieldNames());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("return a failure response for an invalid request")
    class TestSendFailure {
        VaultResponse sendFailureResponse;

        @Test
        @Order(1)
        void testRequest() {
            VaultClient client = VaultClient.newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS(basicSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(basicSettingsNode.get("vaultClientId").asText())
                    .withVaultUsername(basicSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword("invalid")
                    .build();

            sendFailureResponse = client.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        void testResponse() {
            assertNotNull(sendFailureResponse);
            assertEquals("FAILURE", sendFailureResponse.getResponseStatus());
            assertFalse(sendFailureResponse.isSuccessful());
            assertTrue(sendFailureResponse.hasErrors());
            assertFalse(sendFailureResponse.hasWarnings());
            assertNotNull(sendFailureResponse.getErrors());
            for (APIResponseError error : sendFailureResponse.getErrors()) {
                assertNotNull(error.getType());
                assertNotNull(error.getMessage());
            }
            assertNull(sendFailureResponse.getWarnings());
            assertNotNull(sendFailureResponse.getResponse());
            assertNotNull(sendFailureResponse.getHeaders());
            assertNotNull(sendFailureResponse.getVaultModelData());
            assertNotNull(sendFailureResponse.getFieldNames());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("return a warning response for a valid request")
    class TestSendWarning {
        VaultResponse sendWarningResponse;

        @Test
        @Order(1)
        void testRequest() {
            VaultClient client = VaultClient.newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS(basicSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(basicSettingsNode.get("vaultClientId").asText())
                    .withVaultUsername(basicSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword(basicSettingsNode.get("vaultPassword").asText())
                    .build();

            String vql = "SELECT id FROM documents MAXROWS 1";
            client.newRequest(QueryRequest.class).query(vql);
            sendWarningResponse = client.newRequest(QueryRequest.class).query(vql);
        }

        @Test
        @Order(2)
        void testResponse() {
            assertNotNull(sendWarningResponse);
            assertEquals("WARNING", sendWarningResponse.getResponseStatus());
            assertFalse(sendWarningResponse.isSuccessful());
            assertFalse(sendWarningResponse.hasErrors());
            assertTrue(sendWarningResponse.hasWarnings());
            assertNull(sendWarningResponse.getErrors());
            assertNotNull(sendWarningResponse.getWarnings());
            for (APIResponseWarning warning : sendWarningResponse.getWarnings()) {
                assertNotNull(warning.getType());
                assertNotNull(warning.getMessage());
            }
            assertNotNull(sendWarningResponse.getResponse());
            assertNotNull(sendWarningResponse.getHeaders());
            assertNotNull(sendWarningResponse.getVaultModelData());
            assertNotNull(sendWarningResponse.getFieldNames());
        }
    }

}
