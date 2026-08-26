package com.veeva.vault.vapil.api.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.response.AuthenticationResponse;
import com.veeva.vault.vapil.api.model.response.OauthTokenResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.api.request.AuthenticationRequest;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpResponseConnector;
import com.veeva.vault.vapil.extension.AbstractVaultClientParameterResolver;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("VaultClientTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Vault Client should")
public class VaultClientTest {
    private static final String VAPIL_BASIC_SETTINGS_FILE_GR = "settings_vapil_basic.json";
    private static final String VAPIL_BASIC_SETTINGS_FILE_LR = "settings_vapil_basic_lr.json";
    private static final String VAPIL_SESSION_ID_SETTINGS_FILE_GR = "settings_vapil_session_id.json";
    private static final String VAPIL_SESSION_ID_SETTINGS_FILE_LR = "settings_vapil_session_id_lr.json";
    private static final String VAPIL_API_ACCESS_TOKEN_SETTINGS_FILE_GR = "settings_vapil_api_access_token_id.json";
    private static final String VAPIL_API_ACCESS_TOKEN_SETTINGS_FILE_LR = "settings_vapil_api_access_token_id_lr.json";
    private static String vapilBasicSettingsFileName;
    private static String vapilSessionIdSettingsFileName;
    private static String vapilApiAccessTokenSettingsFileName;

    @BeforeAll
    public static void setup() throws IOException {
        String vapilVersion = AbstractVaultClientParameterResolver.getVapilVersion();
        if (vapilVersion.contains("BETA")) {
            vapilBasicSettingsFileName = VAPIL_BASIC_SETTINGS_FILE_LR;
            vapilSessionIdSettingsFileName = VAPIL_SESSION_ID_SETTINGS_FILE_LR;
            vapilApiAccessTokenSettingsFileName = VAPIL_API_ACCESS_TOKEN_SETTINGS_FILE_LR;
        } else {
            vapilBasicSettingsFileName = VAPIL_BASIC_SETTINGS_FILE_GR;
            vapilSessionIdSettingsFileName = VAPIL_SESSION_ID_SETTINGS_FILE_GR;
            vapilApiAccessTokenSettingsFileName = VAPIL_API_ACCESS_TOKEN_SETTINGS_FILE_GR;
        }
    }

    private String getOauthToken(JsonNode oauthTokenSettingsNode) {

        try {
            HttpRequestConnector request = new HttpRequestConnector(oauthTokenSettingsNode.get("idpAccessTokenUrl").asText());
            request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);
            request.addBodyParam("grant_type", oauthTokenSettingsNode.get("idpGrantType").asText());
            request.addBodyParam("scope", oauthTokenSettingsNode.get("idpOauthScope").asText());
            request.addBodyParam("client_id", oauthTokenSettingsNode.get("idpClientId").asText());
            request.addBodyParam("client_secret", oauthTokenSettingsNode.get("idpClientSecret").asText());

            HttpResponseConnector response = request.sendPost();
            OauthTokenResponse tokenResponse = getBaseObjectMapper().readValue(response.getResponse(), OauthTokenResponse.class);
            if (tokenResponse != null) {
                return tokenResponse.getAccessToken();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectMapper getBaseObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    @Test
    public void testRequestMissingClientId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS("test.vault.com")
                    .withVaultUsername("testuser")
                    .withVaultPassword("testpassword")
                    .build();
        });

        assertEquals("Vault Client ID is required", exception.getMessage());
    }

    @Test
    public void testRequestMissingDNS() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultClientId("testclientid")
                    .withVaultUsername("testuser")
                    .withVaultPassword("testpassword")
                    .build();
        });

        assertEquals("Vault DNS is required", exception.getMessage());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client with a valid username and password")
    @Disabled
    class TestAuthenticationTypeBasic {
        private JsonNode basicSettingsNode;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            File basicSettingsFile = FileHelper.getSettingsFile(vapilBasicSettingsFileName);
            basicSettingsNode = FileHelper.readSettingsFile(basicSettingsFile);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultClientId(basicSettingsNode.get("vaultClientId").asText())
                    .withVaultDNS(basicSettingsNode.get("vaultDNS").asText())
                    .withVaultUsername(basicSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword(basicSettingsNode.get("vaultPassword").asText())
                    .withValidation(true)
                    .withApiErrorLogging(true)
                    .withHttpTimeout(60)
                    .withAllowAllCertificates(basicSettingsNode.get("allowAllCertificates").asBoolean())
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
            assertNotNull(authResponse.getVaultId());
            assertNotNull(authResponse.getResponse());
            assertNotNull(authResponse.getUserId());
            assertNotNull(authResponse.getVaultIds());
            for (AuthenticationResponse.Vault vault : authResponse.getVaultIds()) {
                assertNotNull(vault.getId());
                assertNotNull(vault.getName());
                assertNotNull(vault.getUrl());
            }
        }

        @Test
        public void testRequestMissingUsername() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                VaultClient testClient = VaultClient
                        .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                        .withVaultClientId("testclientid")
                        .withVaultDNS("test.vault.com")
                        .withVaultPassword("testpassword")
                        .build();
            });

            assertEquals("Vault user name is required", exception.getMessage());
        }

        @Test
        public void testRequestMissingPassword() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                VaultClient testClient = VaultClient
                        .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                        .withVaultClientId("testclientid")
                        .withVaultDNS("test.vault.com")
                        .withVaultUsername("testuser")
                        .build();
            });

            assertEquals("Vault password is required", exception.getMessage());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client from a settings file")
    class TestAuthenticationTypeBasicSettings {

        private File basicSettingsFile = null;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            basicSettingsFile = FileHelper.getSettingsFile(vapilBasicSettingsFileName);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilderFromSettings(basicSettingsFile)
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
            assertNotNull(authResponse.getVaultId());
            assertNotNull(authResponse.getResponse());
            assertNotNull(authResponse.getUserId());
            assertNotNull(authResponse.getVaultIds());
            for (AuthenticationResponse.Vault vault : authResponse.getVaultIds()) {
                assertNotNull(vault.getId());
                assertNotNull(vault.getName());
                assertNotNull(vault.getUrl());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client from a session ID")
    class TestAuthenticationTypeSessionId {

        private JsonNode sessionIdSettingsNode;
        private String sessionId;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            File settingsFile = FileHelper.getSettingsFile(vapilSessionIdSettingsFileName);
            sessionIdSettingsNode = FileHelper.readSettingsFile(settingsFile);
            VaultClient testClient = VaultClient.newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS(sessionIdSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(sessionIdSettingsNode.get("vaultClientId").asText())
                    .withVaultUsername(sessionIdSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword(sessionIdSettingsNode.get("vaultPassword").asText())
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            assertNotNull(testClient.getAuthenticationResponse().getSessionId());
            sessionId = testClient.getAuthenticationResponse().getSessionId();
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
                    .withVaultDNS(sessionIdSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(sessionIdSettingsNode.get("vaultClientId").asText())
                    .withVaultSessionId(sessionId)
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
        }

        @Test
        public void testRequestMissingSessionId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                VaultClient testClient = VaultClient
                        .newClientBuilder(VaultClient.AuthenticationType.SESSION_ID)
                        .withVaultDNS("test.vault.com")
                        .withVaultClientId("testclientid")
                        .build();
            });

            assertEquals("Vault session ID is required", exception.getMessage());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client from a settings file")
    class TestAuthenticationTypeSessionIdSettings {
        private File sessionIdSettingsFile = null;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            sessionIdSettingsFile = FileHelper.getSettingsFile(vapilSessionIdSettingsFileName);
            JsonNode sessionIdSettingsNode = FileHelper.readSettingsFile(sessionIdSettingsFile);
            VaultClient testClient = VaultClient.newClientBuilder(VaultClient.AuthenticationType.BASIC)
                    .withVaultDNS(sessionIdSettingsNode.get("vaultDNS").asText())
                    .withVaultClientId(sessionIdSettingsNode.get("vaultClientId").asText())
                    .withVaultUsername(sessionIdSettingsNode.get("vaultUsername").asText())
                    .withVaultPassword(sessionIdSettingsNode.get("vaultPassword").asText())
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            assertNotNull(testClient.getAuthenticationResponse().getSessionId());
            String sessionId = testClient.getAuthenticationResponse().getSessionId();
            FileHelper.writeSessionId(sessionId, sessionIdSettingsNode, sessionIdSettingsFile);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilderFromSettings(sessionIdSettingsFile)
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client with an API access token")
    class TestAuthenticationTypeApiAccessToken {
        private JsonNode apiAccessTokenSettingsNode;
        private VaultResponse keepAliveResponse = null;

        @BeforeAll
        public void setup() {
            File settingsFile = FileHelper.getSettingsFile(vapilApiAccessTokenSettingsFileName);
            apiAccessTokenSettingsNode = FileHelper.readSettingsFile(settingsFile);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.API_ACCESS_TOKEN)
                    .withVaultClientId(apiAccessTokenSettingsNode.get("vaultClientId").asText())
                    .withVaultDNS(apiAccessTokenSettingsNode.get("vaultDNS").asText())
                    .withVaultApiAccessToken(apiAccessTokenSettingsNode.get("vaultApiAccessToken").asText())
                    .build();

            assertTrue(testClient.hasApiAccessToken());
            keepAliveResponse = testClient.newRequest(AuthenticationRequest.class)
                .sessionKeepAlive();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(keepAliveResponse.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client from an API access token settings file")
    class TestAuthenticationTypeApiAccessTokenSettings {
        private File apiAccessTokenSettingsFile = null;
        private VaultResponse keepAliveResponse = null;

        @BeforeAll
        public void setup() {
            apiAccessTokenSettingsFile = FileHelper.getSettingsFile(vapilApiAccessTokenSettingsFileName);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilderFromSettings(apiAccessTokenSettingsFile)
                    .build();

            assertTrue(testClient.hasApiAccessToken());
            keepAliveResponse = testClient.newRequest(AuthenticationRequest.class).sessionKeepAlive();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(keepAliveResponse.isSuccessful());
        }
    }

    @Disabled
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client with a valid Oauth access token")
    class TestAuthenticationTypeOAuthAccessToken {
        private JsonNode oauthTokenSettingsNode;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            String oauthTokenSettings = "settings_vapil_oauth_token.json";
            File oauthTokenSettingsFile = FileHelper.getSettingsFile(oauthTokenSettings);
            oauthTokenSettingsNode = FileHelper.readSettingsFile(oauthTokenSettingsFile);
        }

        @Test
        @Order(1)
        public void testRequest() {
            String oauthToken = getOauthToken(oauthTokenSettingsNode);
            VaultClient testClient = VaultClient
                    .newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
                    .withVaultClientId(oauthTokenSettingsNode.get("vaultClientId").asText())
                    .withVaultDNS(oauthTokenSettingsNode.get("vaultDNS").asText())
                    .withVaultOauthProfileId(oauthTokenSettingsNode.get("vaultOauthProfileId").asText())
                    .withIdpOauthAccessToken(oauthToken)
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
            assertNotNull(authResponse.getVaultId());
            assertNotNull(authResponse.getResponse());
            assertNotNull(authResponse.getUserId());
            assertNotNull(authResponse.getVaultIds());
            for (AuthenticationResponse.Vault vault : authResponse.getVaultIds()) {
                assertNotNull(vault.getId());
                assertNotNull(vault.getName());
                assertNotNull(vault.getUrl());
            }
        }

        @Test
        public void testRequestMissingAccessToken() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                VaultClient testClient = VaultClient
                        .newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
                        .withVaultDNS("test.vault.com")
                        .withVaultClientId("testclientid")
                        .withVaultOauthProfileId("testoauthprofileid")
                        .build();
            });

            assertEquals("IDP OAuth Access Token is required", exception.getMessage());
        }

        @Test
        public void testRequestMissingProfileId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                VaultClient testClient = VaultClient
                        .newClientBuilder(VaultClient.AuthenticationType.OAUTH_ACCESS_TOKEN)
                        .withVaultClientId("testclientid")
                        .withVaultDNS("test.vault.com")
                        .withIdpOauthAccessToken("testidpoauthtoken")
                        .build();
            });

            assertEquals("Vault OAuth Profile Id is required", exception.getMessage());
        }
    }

    @Disabled
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully build a client from a settings file")
    class TestAuthenticationTypeOauthTokenSettings {

        private File oauthTokenSettingsFile = null;
        private AuthenticationResponse authResponse = null;

        @BeforeAll
        public void setup() {
            String oauthTokenSettingsFileName = "settings_vapil_oauth_token.json";
            oauthTokenSettingsFile = FileHelper.getSettingsFile(oauthTokenSettingsFileName);
            JsonNode oauthTokenSettingsNode = FileHelper.readSettingsFile(oauthTokenSettingsFile);
            String oauthToken = getOauthToken(oauthTokenSettingsNode);
            FileHelper.writeOauthToken(oauthToken, oauthTokenSettingsNode, oauthTokenSettingsFile);
        }

        @Test
        @Order(1)
        public void testRequest() {
            VaultClient testClient = VaultClient
                    .newClientBuilderFromSettings(oauthTokenSettingsFile)
                    .build();

            assertNotNull(testClient.getAuthenticationResponse());
            authResponse = testClient.getAuthenticationResponse();
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertEquals("SUCCESS", authResponse.getResponseStatus());
            assertNotNull(authResponse.getSessionId());
            assertNotNull(authResponse.getVaultId());
            assertNotNull(authResponse.getResponse());
            assertNotNull(authResponse.getUserId());
            assertNotNull(authResponse.getVaultIds());
            for (AuthenticationResponse.Vault vault : authResponse.getVaultIds()) {
                assertNotNull(vault.getId());
                assertNotNull(vault.getName());
                assertNotNull(vault.getUrl());
            }
        }
    }
}
