package com.veeva.vault.vapil.extension;

public class CtmsClientParameterResolver extends AbstractVaultClientParameterResolver {

    private static final String VAULT_PROP_PREFIX = "ctms";

    @Override
    protected String getVaultPropertyTag() {
        return VAULT_PROP_PREFIX;
    }
}
