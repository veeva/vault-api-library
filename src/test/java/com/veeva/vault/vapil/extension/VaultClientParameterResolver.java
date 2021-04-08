package com.veeva.vault.vapil.extension;


public class VaultClientParameterResolver extends AbstractVaultClientParameterResolver {

    private static final String VAULT_PROP_PREFIX = "vault";


    @Override
    protected String getVaultPropertyTag() {
        return VAULT_PROP_PREFIX;
    }

}
