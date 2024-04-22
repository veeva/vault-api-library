package com.veeva.vault.vapil.extension;

import java.io.IOException;

public class CtmsClientParameterResolver extends AbstractVaultClientParameterResolver {

    private static final String VAULT_PROP_PREFIX = "ctms";

    public CtmsClientParameterResolver() throws IOException {
    }

//    @Override
//    protected String getVaultPropertyTag() {
//        return VAULT_PROP_PREFIX;
//    }
}
