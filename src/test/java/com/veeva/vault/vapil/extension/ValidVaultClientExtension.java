package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.client.VaultClientBuilder;
import com.veeva.vault.vapil.api.client.VaultClientId;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Logger;


public class ValidVaultClientExtension implements BeforeAllCallback, AfterAllCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ValidVaultClientExtension.class);
    public static final String VAULT_CLIENT = "vault_client_object";
    private static final Logger logger = Logger.getLogger(ValidVaultClientExtension.class.getName());

    private ExtensionContext.Store getStoreForMethodContext(ExtensionContext ctx) {
        return ctx.getStore(ExtensionContext.Namespace.create(getClass(), ctx.getRequiredTestMethod(), ctx.getDisplayName()));

    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        extensionContext.getStore(NAMESPACE).remove(VAULT_CLIENT, VaultClient.class);
    }


    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        TestProperties prop = new TestProperties();
        VaultClientId vaultClientId = new VaultClientId(prop.getClientIdCompany(),
                prop.getClientIdOrganization(),
                prop.getClientIdTeam(),
                prop.isClient(),
                getClass().getSimpleName());

        VaultClient vaultClient = VaultClientBuilder
                .newClientBuilder(VaultClient.AuthenticationType.BASIC)
                .withVaultDNS(prop.getVaultDNS())
                .withVaultUsername(prop.getVaultUsername())
                .withVaultPassword(prop.getVaultPassword())
                .withVaultClientId(vaultClientId)
                .build();

        extensionContext.getStore(NAMESPACE).put(VAULT_CLIENT, vaultClient);

    }

}
