package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.*;

import static com.veeva.vault.vapil.api.client.VaultClient.AuthenticationType.BASIC;
import static com.veeva.vault.vapil.api.client.VaultClient.AuthenticationType.SESSION_ID;

public abstract class AbstractVaultClientParameterResolver implements ParameterResolver {
    private final static String VAPIL_SETTINGS_FILE = "settings_vapil_basic.json";
    private File settingsFile;
    private VaultClient vaultClient;
    private String sessionId;
    private JsonNode rootNode;

    public AbstractVaultClientParameterResolver() {
        initializeSettings();
        buildVaultClient();
    }

    private void initializeSettings() {
        settingsFile = FileHelper.getSettingsFile(VAPIL_SETTINGS_FILE);
        rootNode = FileHelper.readSettingsFile(settingsFile);
        setSessionId();
    }

    private void setSessionId() {
        // Get Session ID
        if (rootNode != null) {
            sessionId = rootNode.get("vaultSessionId").asText();
        }
    }

    private void buildVaultClient() {
        switch (sessionId.isEmpty() ? BASIC : SESSION_ID) {
            case SESSION_ID:
                buildVaultClient(SESSION_ID);
                break;
            case BASIC:
                buildVaultClient(BASIC);
                break;
        }
    }

    private void buildVaultClient(VaultClient.AuthenticationType authType) {
        switch (authType.getTypeName()) {
            case "SESSION_ID":
                vaultClient = VaultClient
                        .newClientBuilder(SESSION_ID)
                        .withVaultDNS(rootNode.get("vaultDNS").asText())
                        .withVaultUsername(rootNode.get("vaultUsername").asText())
                        .withVaultPassword(rootNode.get("vaultPassword").asText())
                        .withVaultSessionId(rootNode.get("vaultSessionId").asText())
                        .withVaultClientId(rootNode.get("vaultClientId").asText())
                        .build();

                // Build Vault Client from username/password if session ID is not valid
                if (!vaultClient.getAuthenticationResponse().getResponseStatus().equals("SUCCESS")) {
                    buildVaultClient(BASIC);
                }
                break;

            case "BASIC":
                vaultClient = VaultClient
                        .newClientBuilder(BASIC)
                        .withVaultDNS(rootNode.get("vaultDNS").asText())
                        .withVaultUsername(rootNode.get("vaultUsername").asText())
                        .withVaultPassword(rootNode.get("vaultPassword").asText())
                        .withVaultClientId(rootNode.get("vaultClientId").asText())
                        .build();

                if (vaultClient.getAuthenticationResponse().getResponseStatus().equals("SUCCESS")) {
                    sessionId = vaultClient.getAuthenticationResponse().getSessionId();
                    FileHelper.writeSessionId(sessionId, rootNode, settingsFile);
                } else {
                    throw new IllegalArgumentException("Unable to build Vault Client");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid authentication type: " + authType.getTypeName());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (parameterContext.getParameter().getType() == VaultClient.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return vaultClient;
    }
}
