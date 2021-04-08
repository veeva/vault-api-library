package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.JobStatusResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(VaultClientParameterResolver.class)
public class ConfigurationMigrationRequestTest {

    // Run manually
    @Test
    public void validateInboundPackageTest(VaultClient vaultClient, TestReporter reporter) {

        try {
            JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                       .setBinaryFile("file", Files.readAllBytes(new File(getClass().getClassLoader().getResource("import_test_package.vpk").getPath()).toPath()))
                       .importPackage();
            Thread.sleep(5000);
            JobStatusResponse resp2 = vaultClient.newRequest(JobRequest.class)
                    .retrieveJobStatus(resp.getJobId());
            if (!resp2.getData().getStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
                Thread.sleep(15000);
                resp2 = vaultClient.newRequest(JobRequest.class)
                        .retrieveJobStatus(resp.getJobId());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }
}
