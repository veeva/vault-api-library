package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.common.PackageLog;
import com.veeva.vault.vapil.api.model.common.PackageStep;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//        Inbound Packages are saved as 'vault_package__v' records. These are system managed, and cannot be deleted.
//        Inbound packages will have to be manually deleted through the UI after these tests are run
@Tag("ConfigurationMigrationRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Configuration Migration Request should")
public class ConfigurationMigrationRequestTest {

    private static final String VPK_FILE_PATH = "src/test/resources/configuration_migration/inbound_package.vpk";
    private static final String OUTBOUND_PACKAGE_NAME = "PKG-0004";
    private static final String OUTBOUND_PACKAGE_ID = "0PO000000004001";
    private static final int TARGET_VAULT_ID = 187925;
    private static String packageId;
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    @DisplayName("successfully export an outbound package")
    public void testExportPackage() {
        JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .exportPackage(OUTBOUND_PACKAGE_NAME);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getJobId());
        Assertions.assertNotNull(response.getUrl());

        Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getJobId()));
    }

    @Test
    @DisplayName("successfully retrieve outbound package dependencies")
    public void testRetrieveOutboundPackageDependencies() {
        OutboundPackageDependenciesResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .retrieveOutboundPackageDependencies(OUTBOUND_PACKAGE_ID);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails());

        OutboundPackageDependenciesResponse.ResponseDetails responseDetails = response.getResponseDetails();
        Assertions.assertNotNull(responseDetails.getTotalDependencies());
        Assertions.assertNotNull(responseDetails.getTargetVaultId());
        Assertions.assertNotNull(responseDetails.getPackageName());
        Assertions.assertNotNull(responseDetails.getPackageId());
        Assertions.assertNotNull(responseDetails.getPackageSummary());
        Assertions.assertNotNull(responseDetails.getPackageDescription());
        Assertions.assertNotNull(responseDetails.getUrl());

        Assertions.assertNotNull(response.getPackageDependencies());
        List<OutboundPackageDependenciesResponse.PackageDependency> packageDependencies = response.getPackageDependencies();
        for (OutboundPackageDependenciesResponse.PackageDependency packageDependency : packageDependencies) {
            Assertions.assertNotNull(packageDependency.getId());
            Assertions.assertNotNull(packageDependency.getName());
            Assertions.assertNotNull(packageDependency.getComponentName());
            Assertions.assertNotNull(packageDependency.getComponentType());
            Assertions.assertNotNull(packageDependency.getReferencedComponentName());
            Assertions.assertNotNull(packageDependency.getReferencedComponentType());
        }
    }

    @Test
    @DisplayName("successfully complete a job to generate a Vault Compare Report")
    public void testVaultCompare() {
        JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .setResultsType(ConfigurationMigrationRequest.ResultsType.DIFFERENCES)
                .setDetailsType(ConfigurationMigrationRequest.DetailsType.SIMPLE)
                .setIncludeDocBinderTemplates(true)
                .setIncludeVaultSettings(true)
                .setComponentsType(Arrays.asList("Doctype", "Doclifecycle", "Object", "Workflow"))
                .setGenerateOutboundPackages(false)
                .vaultCompare(TARGET_VAULT_ID);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getJobId());
        Assertions.assertNotNull(response.getUrl());
        Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getJobId()));
    }

    @Test
    @DisplayName("successfully complete a job to generate a Vault Configuration Report")
    public void testVaultConfigurationReport() {
        ZonedDateTime date = ZonedDateTime.now();
        JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .setIncludeVaultSettings(true)
                .setIncludeInactiveComponents(false)
                .setIncludeComponentsModifiedSince(date.minusDays(5))
                .setIncludeDocBinderTemplates(true)
                .setSuppressEmptyResults(false)
                .setComponentsType(Arrays.asList("Doctype", "Doclifecycle", "Object", "Workflow"))
                .setOutputFormat(ConfigurationMigrationRequest.OutputFormat.EXCEL_MACRO_ENABLED)
                .vaultConfigurationReport();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getJobId());
        Assertions.assertNotNull(response.getUrl());
        Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getJobId()));
    }

    @Test
    @DisplayName("successfully validate a VPK package")
    public void testValidatePackage() {
        ValidatePackageResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .setInputPath(VPK_FILE_PATH)
                .validatePackage();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails());
        ValidatePackageResponse.ResponseDetails responseDetails = response.getResponseDetails();

        Assertions.assertNotNull(responseDetails.getSummary());
        Assertions.assertNotNull(responseDetails.getAuthor());
        Assertions.assertNotNull(responseDetails.getPackageName());
        Assertions.assertNotNull(responseDetails.getPackageId());
        Assertions.assertNotNull(responseDetails.getSourceVault());
        Assertions.assertNotNull(responseDetails.getPackageStatus());
        Assertions.assertNotNull(responseDetails.getTotalSteps());
        Assertions.assertNotNull(responseDetails.getTotalStepsBlocked());
        Assertions.assertNotNull(responseDetails.getStartTime());
        Assertions.assertNotNull(responseDetails.getEndTime());
        Assertions.assertNotNull(responseDetails.getPackageError());

        Assertions.assertNotNull(responseDetails.getPackageSteps());
        List<PackageStep> packageSteps = responseDetails.getPackageSteps();
        for (PackageStep packageStep : packageSteps) {
            Assertions.assertNotNull(packageStep.getName());
            Assertions.assertNotNull(packageStep.getStepType());
            Assertions.assertNotNull(packageStep.getStepLabel());
            Assertions.assertNotNull(packageStep.getStepName());
            Assertions.assertNotNull(packageStep.getType());
            Assertions.assertNotNull(packageStep.getDeploymentStatus());
            Assertions.assertNotNull(packageStep.getDeploymentAction());
            Assertions.assertNotNull(packageStep.getDependencies());
            List<PackageStep.Dependency> dependencies = packageStep.getDependencies();

            for (PackageStep.Dependency dependency : dependencies) {
                Assertions.assertNotNull(dependency.getComponentName());
                Assertions.assertNotNull(dependency.getComponentType());
                Assertions.assertNotNull(dependency.getSubcomponentName());
                Assertions.assertNotNull(dependency.getSubcomponentType());
                Assertions.assertNotNull(dependency.getStatus());
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("successfully import a VPK package")
    public void testImportPackage() throws InterruptedException {
        JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .setInputPath(VPK_FILE_PATH)
                .importPackage();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getUrl());
        Assertions.assertNotNull(response.getJobId());
        JobStatusHelper.checkJobCompletion(vaultClient, response.getJobId());
        ObjectRecordCollectionResponse recordResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                .retrieveObjectRecordCollection("vault_package__v");
        Assertions.assertTrue(recordResponse.isSuccessful());
        Assertions.assertNotNull(recordResponse.getData());

        List<ObjectRecord> records = recordResponse.getData();
        packageId = records.get(records.size() - 1).getId();
        Thread.sleep(10000);
    }

    @Test
    @Order(2)
    @DisplayName("successfully validate an inbound package")
    public void testValidateInboundPackage() {
        ValidatePackageResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .validateInboundPackage(packageId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails());
        ValidatePackageResponse.ResponseDetails responseDetails = response.getResponseDetails();

        Assertions.assertNotNull(responseDetails.getSummary());
        Assertions.assertNotNull(responseDetails.getAuthor());
        Assertions.assertNotNull(responseDetails.getPackageName());
        Assertions.assertNotNull(responseDetails.getPackageId());
        Assertions.assertNotNull(responseDetails.getSourceVault());
        Assertions.assertNotNull(responseDetails.getPackageStatus());
        Assertions.assertNotNull(responseDetails.getTotalSteps());
        Assertions.assertNotNull(responseDetails.getStartTime());
        Assertions.assertNotNull(responseDetails.getEndTime());
        Assertions.assertNotNull(responseDetails.getPackageError());

        Assertions.assertNotNull(responseDetails.getPackageSteps());
        List<PackageStep> packageSteps = responseDetails.getPackageSteps();
        for (PackageStep packageStep : packageSteps) {
            Assertions.assertNotNull(packageStep.getName());
            Assertions.assertNotNull(packageStep.getStepType());
            Assertions.assertNotNull(packageStep.getStepLabel());
            Assertions.assertNotNull(packageStep.getStepName());
            Assertions.assertNotNull(packageStep.getType());
            Assertions.assertNotNull(packageStep.getDeploymentStatus());
            Assertions.assertNotNull(packageStep.getDeploymentAction());
            Assertions.assertNotNull(packageStep.getDependencies());
            List<PackageStep.Dependency> dependencies = packageStep.getDependencies();

            for (PackageStep.Dependency dependency : dependencies) {
                Assertions.assertNotNull(dependency.getComponentName());
                Assertions.assertNotNull(dependency.getComponentType());
                Assertions.assertNotNull(dependency.getSubcomponentName());
                Assertions.assertNotNull(dependency.getSubcomponentType());
                Assertions.assertNotNull(dependency.getStatus());
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("successfully deploy an inbound package")
    public void testDeployPackage() {
        JobCreateResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .deployPackage(packageId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getJobId());
        Assertions.assertNotNull(response.getUrl());
        Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, response.getJobId()));
    }

    @Test
    @Order(4)
    @DisplayName("successfully retrieve package deploy results")
    public void testRetrievePackageDeployResults() {
        PackageDeploymentResultsResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                .retrievePackageDeployResults(packageId);

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getResponseDetails());
        PackageDeploymentResultsResponse.ResponseDetails responseDetails = response.getResponseDetails();

        Assertions.assertNotNull(responseDetails.getTotalSteps());
        Assertions.assertNotNull(responseDetails.getDeployedComponents());
        Assertions.assertNotNull(responseDetails.getDeployedWithWarnings());
        Assertions.assertNotNull(responseDetails.getDeployedWithFailures());
        Assertions.assertNotNull(responseDetails.getDeployedWithError());
        Assertions.assertNotNull(responseDetails.getFailed());
        Assertions.assertNotNull(responseDetails.getSkipped());
        Assertions.assertNotNull(responseDetails.getPackageStatus());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve package deploy results")
    class TestRetrievePackageDeployResults {

        private PackageDeploymentResultsResponse retrievePackageDeployResultsResponse = null;
        private String packageId;

        @BeforeAll
        public void setup() throws IOException {
//            Import Package
            JobCreateResponse importResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .setInputPath(VPK_FILE_PATH)
                    .importPackage();

            Assertions.assertTrue(importResponse.isSuccessful());
            JobStatusHelper.checkJobCompletion(vaultClient, importResponse.getJobId());

//            Retrieve Package ID
            ObjectRecordCollectionResponse recordResponse = vaultClient.newRequest(ObjectRecordRequest.class)
                    .retrieveObjectRecordCollection("vault_package__v");
            Assertions.assertTrue(recordResponse.isSuccessful());
            Assertions.assertNotNull(recordResponse.getData());

            List<ObjectRecord> records = recordResponse.getData();
            packageId = records.get(records.size() - 1).getId();

//            Deploy Package
            JobCreateResponse deployResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .deployPackage(packageId);

            Assertions.assertTrue(deployResponse.isSuccessful());
            Assertions.assertNotNull(deployResponse.getJobId());
            Assertions.assertNotNull(deployResponse.getUrl());
            Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, deployResponse.getJobId()));
        }

        @Test
        @Order(1)
        public void testRequest() {
            retrievePackageDeployResultsResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .retrievePackageDeployResults(packageId);

            assertNotNull(retrievePackageDeployResultsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrievePackageDeployResultsResponse.isSuccessful());
            PackageDeploymentResultsResponse.ResponseDetails responseDetails = retrievePackageDeployResultsResponse.getResponseDetails();
            assertNotNull(responseDetails);
            assertNotNull(responseDetails.getTotalSteps());
            assertNotNull(responseDetails.getDeployedComponents());
            assertNotNull(responseDetails.getDeployedWithWarnings());
            assertNotNull(responseDetails.getDeployedWithFailures());
            assertNotNull(responseDetails.getDeployedWithError());
            assertNotNull(responseDetails.getFailed());
            assertNotNull(responseDetails.getSkipped());
            assertNotNull(responseDetails.getPackageStatus());

            List<PackageLog> deploymentLog = responseDetails.getDeploymentLog();
            assertNotNull(deploymentLog);
            for (PackageLog packageLog : deploymentLog) {
                assertNotNull(packageLog.getFilename());
                assertNotNull(packageLog.getUrl());
                assertNotNull(packageLog.getCreatedDate());
            }

            List<PackageStep> packageSteps = retrievePackageDeployResultsResponse.getPackageSteps();
            assertNotNull(packageSteps);
            for (PackageStep packageStep : packageSteps) {
                assertNotNull(packageStep.getName());
                assertNotNull(packageStep.getStepType());
                assertNotNull(packageStep.getStepName());
                assertNotNull(packageStep.getType());
                assertNotNull(packageStep.getDeploymentStatus());
            }

        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully enable configuration mode")
    class TestEnableConfigurationMode {

        private VaultResponse enableConfigurationModeResponse = null;

        @Test
        @Order(1)
        public void testRequest() {
            enableConfigurationModeResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .enableConfigurationMode();

            assertNotNull(enableConfigurationModeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(enableConfigurationModeResponse.isSuccessful());
            assertNotNull(enableConfigurationModeResponse.getResponseMessage());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully disable configuration mode")
    class TestDisableConfigurationMode {

        private VaultResponse disableConfigurationModeResponse = null;

        @BeforeAll
        public void setup() {
            VaultResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .enableConfigurationMode();

            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            disableConfigurationModeResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .disableConfigurationMode();

            assertNotNull(disableConfigurationModeResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(disableConfigurationModeResponse.isSuccessful());
            assertNotNull(disableConfigurationModeResponse.getResponseMessage());
        }
    }
}
