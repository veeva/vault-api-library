package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


@ExtendWith(VaultClientParameterResolver.class)
public class VaultRequestTest {

    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    void testFailureResponse() {
        VaultResponse resp = vaultClient.newRequest(QueryRequest.class).query("select * ");
        Assertions.assertNotNull(resp);
        Assertions.assertTrue(resp.hasErrors());
        Assertions.assertFalse(resp.isSuccessful());
        Assertions.assertNotNull(resp.getErrors());
        Assertions.assertNotEquals(0, resp.getErrors().size());
        Assertions.assertNotNull(resp.getHeaders());
        Assertions.assertNotEquals(0, resp.getHeaders().size());


    }

    @Test
    void testSuccessResponse() {
        VaultResponse resp = vaultClient.newRequest(QueryRequest.class).query("select id, name__v from user__sys where id < 100");
        Assertions.assertNotNull(resp);
        Assertions.assertFalse(resp.hasErrors());
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNull(resp.getErrors());
        Assertions.assertNotNull(resp.getHeaders());
        Assertions.assertNotEquals(0, resp.getHeaders().size());
    }

}
