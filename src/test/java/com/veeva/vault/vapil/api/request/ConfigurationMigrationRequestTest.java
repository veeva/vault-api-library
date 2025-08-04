package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.common.PackageLog;
import com.veeva.vault.vapil.api.model.common.PackageStep;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//        Inbound Packages are saved as 'vault_package__v' records. These are system managed, and cannot be deleted.
//        Inbound packages will have to be manually deleted through the UI after these tests are run
@Tag("ConfigurationMigrationRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Configuration Migration Request should")
public class ConfigurationMigrationRequestTest {

    private static final String RESOURCES_CONFIGURATION_MIGRATION_PATH = FileHelper.PATH_RESOURCES_FOLDER + "configuration_migration" + File.separator;
    private static final String VAULT_CONFIG_REPORT_FILE_PATH = RESOURCES_CONFIGURATION_MIGRATION_PATH + "vault_configuration_report.zip";
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

//    retrieveObjectRecordCollection() was deprecated. Refactor to use VQL
//    @Nested
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    @DisplayName("successfully retrieve package deploy results")
//    class TestRetrievePackageDeployResults {
//
//        private PackageDeploymentResultsResponse retrievePackageDeployResultsResponse = null;
//        private String packageId;
//
//        @BeforeAll
//        public void setup() throws IOException {
////            Import Package
//            JobCreateResponse importResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
//                    .setInputPath(VPK_FILE_PATH)
//                    .importPackage();
//
//            Assertions.assertTrue(importResponse.isSuccessful());
//            JobStatusHelper.checkJobCompletion(vaultClient, importResponse.getJobId());
//
////            Retrieve Package ID
//            ObjectRecordCollectionResponse recordResponse = vaultClient.newRequest(ObjectRecordRequest.class)
//                    .retrieveObjectRecordCollection("vault_package__v");
//            Assertions.assertTrue(recordResponse.isSuccessful());
//            Assertions.assertNotNull(recordResponse.getData());
//
//            List<ObjectRecord> records = recordResponse.getData();
//            packageId = records.get(records.size() - 1).getId();
//
////            Deploy Package
//            JobCreateResponse deployResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
//                    .deployPackage(packageId);
//
//            Assertions.assertTrue(deployResponse.isSuccessful());
//            Assertions.assertNotNull(deployResponse.getJobId());
//            Assertions.assertNotNull(deployResponse.getUrl());
//            Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, deployResponse.getJobId()));
//        }
//
//        @Test
//        @Order(1)
//        public void testRequest() {
//            retrievePackageDeployResultsResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
//                    .retrievePackageDeployResults(packageId);
//
//            assertNotNull(retrievePackageDeployResultsResponse);
//        }
//
//        @Test
//        @Order(2)
//        public void testResponse() {
//            assertTrue(retrievePackageDeployResultsResponse.isSuccessful());
//            PackageDeploymentResultsResponse.ResponseDetails responseDetails = retrievePackageDeployResultsResponse.getResponseDetails();
//            assertNotNull(responseDetails);
//            assertNotNull(responseDetails.getTotalSteps());
//            assertNotNull(responseDetails.getDeployedComponents());
//            assertNotNull(responseDetails.getDeployedWithWarnings());
//            assertNotNull(responseDetails.getDeployedWithFailures());
//            assertNotNull(responseDetails.getDeployedWithError());
//            assertNotNull(responseDetails.getFailed());
//            assertNotNull(responseDetails.getSkipped());
//            assertNotNull(responseDetails.getPackageStatus());
//
//            List<PackageLog> deploymentLog = responseDetails.getDeploymentLog();
//            assertNotNull(deploymentLog);
//            for (PackageLog packageLog : deploymentLog) {
//                assertNotNull(packageLog.getFilename());
//                assertNotNull(packageLog.getUrl());
//                assertNotNull(packageLog.getCreatedDate());
//            }
//
//            List<PackageStep> packageSteps = retrievePackageDeployResultsResponse.getPackageSteps();
//            assertNotNull(packageSteps);
//            for (PackageStep packageStep : packageSteps) {
//                assertNotNull(packageStep.getName());
//                assertNotNull(packageStep.getStepType());
//                assertNotNull(packageStep.getStepName());
//                assertNotNull(packageStep.getType());
//                assertNotNull(packageStep.getDeploymentStatus());
//            }
//        }
//    }

    @Nested
    @Tag("SmokeTest")
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
    @Tag("SmokeTest")
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

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate a Vault Configuration Report job")
    class TestVaultConfigurationReport {

        private JobCreateResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .vaultConfigurationReport();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getJobId());
            assertNotNull(response.getUrl());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve configuration report results as binary")
    class TestRetrieveConfigurationReportResultsBinary {

        private VaultResponse response = null;
        private int jobId;

        @BeforeAll
        public void setup() {
            JobCreateResponse jobCreateResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .vaultConfigurationReport();

            assertTrue(jobCreateResponse.isSuccessful());
            jobId = jobCreateResponse.getJobId();

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .retrieveConfigurationReportResults(String.valueOf(jobId));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getBinaryContent());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve configuration report results as file")
    class TestRetrieveConfigurationReportResultsFile {

        private VaultResponse response = null;
        private int jobId;

        @BeforeAll
        public void setup() {
            JobCreateResponse jobCreateResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .vaultConfigurationReport();

            assertTrue(jobCreateResponse.isSuccessful());
            jobId = jobCreateResponse.getJobId();

            assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, jobId));
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .setOutputPath(VAULT_CONFIG_REPORT_FILE_PATH)
                    .retrieveConfigurationReportResults(String.valueOf(jobId));

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully query for vault_component__v definition")
    class TestComponentDefinitionQueryComponent {

        private ComponentQueryResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            String vql = "SELECT id,name__v,component_name__v FROM vault_component__v";
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                            .componentDefinitionQuery(vql);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertFalse(response.hasErrors());
            assertNotNull(response.getData());
            for (ComponentQueryResponse.QueryResult queryResult : response.getData()) {
                assertNotNull(queryResult.get("id"));
                assertNotNull(queryResult.get("name__v"));
                assertNotNull(queryResult.get("component_name__v"));
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully query for vault_package_component__v definition")
    class TestComponentDefinitionQueryPackageComponent {

        private ComponentQueryResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            String vql = "SELECT id,name__v,component_type__v FROM vault_package_component__v";
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .componentDefinitionQuery(vql);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertFalse(response.hasErrors());
            assertNotNull(response.getData());
            for (ComponentQueryResponse.QueryResult queryResult : response.getData()) {
                assertNotNull(queryResult.get("id"));
                assertNotNull(queryResult.get("name__v"));
                assertNotNull(queryResult.get("component_type__v"));
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully query page url for vault_component__v definition")
    class TestComponentDefinitionQueryByPage {

        private ComponentQueryResponse response = null;
        private String nextPageUrl;

        @BeforeAll
        public void setup() {
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .componentDefinitionQuery("SELECT id,name__v,component_name__v FROM vault_component__v");
            assertNotNull(response);
            assertFalse(response.hasErrors());
            assertNotNull(response.getResponseDetails().getNextPage());
            nextPageUrl = response.getResponseDetails().getNextPage();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
                    .componentDefinitionQueryByPage(nextPageUrl);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertFalse(response.hasErrors());
            assertNotNull(response.getData());
            for (ComponentQueryResponse.QueryResult queryResult : response.getData()) {
                assertNotNull(queryResult.get("id"));
                assertNotNull(queryResult.get("name__v"));
                assertNotNull(queryResult.get("component_name__v"));
            }
        }
    }
}
