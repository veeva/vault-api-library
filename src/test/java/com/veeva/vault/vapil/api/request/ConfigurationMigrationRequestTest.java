package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(VaultClientParameterResolver.class)
@Disabled
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

    @Test
    public void importPackageTest(VaultClient vaultClient) {
        try {
            JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .setBinaryFile("file", Files.readAllBytes(new File(getClass().getClassLoader().getResource("import_test_package.vpk").getPath()).toPath()))
                    .importPackage();
            boolean successful = false;
            JobStatusResponse resp2;
            do {
                resp2 = vaultClient.newRequest(JobRequest.class)
                        .retrieveJobStatus(response.getJobId());
                if (!resp2.getData().getStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
                    Thread.sleep(10000);
                    resp2 = vaultClient.newRequest(JobRequest.class)
                            .retrieveJobStatus(response.getJobId());
                } else {
                    successful = true;
                }
            } while (successful);

            PackageImportResultsResponse response3 = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                        .retrievePackageImportResultsByHref(resp2.getData().getLinks().stream()
                                .filter(link ->
                                link.getRel().equals("artifacts")).findFirst().get().getHref());

            Assertions.assertTrue(response3.getVaultPackage().getStatus().equals("Active"));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void deployPackageTest(VaultClient vaultClient) {

        try {
            JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .setBinaryFile("file", Files.readAllBytes(new File(getClass().getClassLoader().getResource("import_test_package.vpk").getPath()).toPath()))
                    .importPackage();
            boolean successful = false;
            JobStatusResponse resp2;
            do {
                resp2 = vaultClient.newRequest(JobRequest.class)
                        .retrieveJobStatus(response.getJobId());
                if (!resp2.getData().getStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
                    Thread.sleep(10000);
                    resp2 = vaultClient.newRequest(JobRequest.class)
                            .retrieveJobStatus(response.getJobId());
                } else {
                    successful = true;
                }
            } while (successful);

            PackageImportResultsResponse response3 = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .retrievePackageImportResultsByHref(resp2.getData().getLinks().stream()
                            .filter(link ->
                                    link.getRel().equals("artifacts")).findFirst().get().getHref());

           if (response3.isSuccessful()) {
               String packageId = response3.getVaultPackage().getId();
               JobCreateResponse response4 = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                       .deployPackage(packageId);

               successful = false;
               JobStatusResponse response5;
               do {
                   response5 = vaultClient.newRequest(JobRequest.class)
                           .retrieveJobStatus(response4.getJobId());
                   if (!response5.getData().getStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
                       Thread.sleep(10000);
                       response5 = vaultClient.newRequest(JobRequest.class)
                               .retrieveJobStatus(response.getJobId());
                   } else {
                       successful = true;
                   }
               } while (successful);

               PackageDeploymentResultsResponse response6 = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                       .retrievePackageDeployResults(packageId);
               Assertions.assertTrue(response6.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS));
           }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void retrieveOutboundPackageDependenciesTest(VaultClient vaultClient) {
        OutboundPackageDependenciesResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .retrieveOutboundPackageDependencies("0PO000000001001");

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails().getPackageId());
        Assertions.assertNotNull(response.getPackageDependencies().get(0).getComponentName());
    }
}
