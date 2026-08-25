package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.ObjectRecordRequestHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@Tag("ObjectLifecycleWorkflowRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Object Lifecycle Workflow Request should")
class ObjectLifecycleWorkflowRequestTest {

    static final String OBJECT_NAME = "vapil_test_object__c";
    static final String ENVELOPE__SYS = "envelope__sys";
    static final String MULTI_RECORD_WORKFLOW_NAME = "Objectworkflow.vapil_test_object_workflow__c";
    static final String WORKFLOW_ACTION_NAME = "Objectlifecyclestateuseraction.vapil_test_object__c.active_state__c.start_vapil_test_object_workflow_useract__c";
    static List<String> recordIds = new ArrayList<>();
    private static VaultClient vaultClient;


    @BeforeAll
    static void setup(VaultClient client) throws IOException {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Order(1)
    @Test
    @DisplayName("successfully retrieve all available user actions that can be initiated on a specific object record")
    void testRetrieveObjectRecordUserActions() {
        ObjectRecordActionResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveObjectRecordUserActions(OBJECT_NAME, recordIds.get(0));

        Assertions.assertTrue(response.isSuccessful());

        for (ObjectRecordActionResponse.Action action : response.getData()) {
            Assertions.assertNotNull(action.getName());
        }
    }

    @Order(2)
    @Test
    @DisplayName("successfully retrieve the details for a specific user action")
    void testRetrieveObjectRecordUserActionDetails() {
        ObjectRecordActionResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveObjectRecordUserActionDetails(OBJECT_NAME, recordIds.get(0), WORKFLOW_ACTION_NAME);

        Assertions.assertTrue(response.isSuccessful());

        for (ObjectRecordActionResponse.Action action : response.getData()) {
            Assertions.assertNotNull(action.getName());
        }
    }

    @Order(3)
    @Test
    @DisplayName("successfully initiate a user action on a single object record")
    void testInitiateObjectActionOnASingleRecord() {
        ObjectRecordActionResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .initiateObjectActionOnASingleRecord(OBJECT_NAME, recordIds.get(0), WORKFLOW_ACTION_NAME);

        Assertions.assertTrue(response.isSuccessful());
    }

    @Order(4)
    @Test
    @DisplayName("successfully initiate a user action on multiple object records")
    void testInitiateObjectActionOnMultipleRecords() {
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 1; i < recordIds.size(); i++) {
            recordIdSet.add(recordIds.get(i));
        }

        ObjectRecordActionBulkResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .initiateObjectActionOnMultipleRecords(OBJECT_NAME, recordIdSet, WORKFLOW_ACTION_NAME);

        Assertions.assertTrue(response.isSuccessful());

        for (ObjectRecordAttachmentResponse objectResponse : response.getData()) {
            Assertions.assertTrue(objectResponse.isSuccessful());
        }
    }

    @Order(5)
    @Test
    @DisplayName("successfully retrieve Multi-Record Workflows")
    void testRetrieveMultiRecordWorkflows() {
        ObjectMultiRecordWorkflowsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveMultiRecordWorkflows();

        assertTrue(response.isSuccessful());

        for (ObjectMultiRecordWorkflowsResponse.MultiRecordWorkflow workflow : response.getData()) {
            Assertions.assertNotNull(workflow.getName());
        }
    }

    @Order(6)
    @Test
    @DisplayName("successfully retrieve Multi-Record Workflow Details")
    void testRetrieveMultiRecordWorkflowDetails() {
        ObjectMultiRecordWorkflowDetailsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveMultiRecordWorkflowDetails(MULTI_RECORD_WORKFLOW_NAME);

        assertTrue(response.isSuccessful());

        Assertions.assertNotNull(response.getData().getName());
    }

    @Order(7)
    @Test
    @Disabled
    @DisplayName("successfully initiate a Multi-Record Workflow")
    void testInitiateMultiRecordWorkflow() {
        String record1 = String.format("Object:%s.%s", OBJECT_NAME, recordIds.get(recordIds.size() - 2));
        String record2 = String.format("Object:%s.%s", OBJECT_NAME, recordIds.get(recordIds.size() - 1));

        Map<String, Object> bodyParams = new HashMap<>();
        String workflowName = MULTI_RECORD_WORKFLOW_NAME;
        String record = String.format("%s,%s", record1, record2);
        String description = "Description for Test Workflow";
//        String owners = "user:1008109";
        bodyParams.put("contents__sys", record);
        bodyParams.put("description__sys", description);
//        bodyParams.put("part_owners__c", owners);

        ObjectMultiRecordWorkflowInitiateResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .setBodyParams(bodyParams)
                .initiateMultiRecordWorkflow(workflowName);

        assertTrue(resp.isSuccessful());
        assertNotNull(resp.getData().getRecordId());
        assertNotNull(resp.getData().getWorkflowId());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate a user action on multiple object records")
    class TestInitiateObjectActionOnMultipleRecords {

        ObjectRecordActionBulkResponse initiateObjectActionOnMultipleRecordsResponse = null;
        List<String> recordIds = new ArrayList<>();
        List<Integer> workflowIds = new ArrayList<>();

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }
        }

        @AfterAll
        public void teardown() throws IOException {
            for (String recordId : recordIds) {
                ObjectWorkflowResponse workflowResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                        .retrieveWorkflows(OBJECT_NAME, recordId, null);

                assertTrue(workflowResponse.isSuccessful());
                workflowIds.add(workflowResponse.getData().get(0).getId());
            }

            for (Integer workflowId : workflowIds) {
                VaultResponse actionResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                                .initiateWorkflowAction(workflowId, "cancel");

                assertTrue(actionResponse.isSuccessful());
            }

            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        void testRequest() {
            Set<String> recordIdSet = new HashSet<>();
            for (String recordId : recordIds) {
                recordIdSet.add(recordId);
            }

            initiateObjectActionOnMultipleRecordsResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .initiateObjectActionOnMultipleRecords(OBJECT_NAME, recordIdSet, WORKFLOW_ACTION_NAME);

            Assertions.assertNotNull(initiateObjectActionOnMultipleRecordsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(initiateObjectActionOnMultipleRecordsResponse.isSuccessful());
            assertNotNull(initiateObjectActionOnMultipleRecordsResponse.getData());

            for (ObjectRecordAttachmentResponse objectResponse : initiateObjectActionOnMultipleRecordsResponse.getData()) {
                Assertions.assertTrue(objectResponse.isSuccessful());
            }
        }
    }

    @Nested
    @Tag("SmokeTest")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully initiate mdwcomplete task action")
    class TestInitiateWorkflowTaskActionMdwComplete {

        VaultResponse response = null;
        List<String> recordIds = new ArrayList<>();
        List<Integer> workflowIds = new ArrayList<>();
        String envelopeId = null;
        int taskId;
        String taskName = "mdwcomplete";

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//            Create an Object Record
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }
            Thread.sleep(2000);

//            Start a workflow on the record
            Map<String, Object> bodyParams = new HashMap<>();
            String record = String.format("Object:%s.%s", OBJECT_NAME, recordIds.get(0));
            String description = "Description for Test Workflow";
            bodyParams.put("contents__sys", record);
            bodyParams.put("description__sys", description);
            ObjectMultiRecordWorkflowInitiateResponse workflowResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .setBodyParams(bodyParams)
                    .initiateMultiRecordWorkflow(MULTI_RECORD_WORKFLOW_NAME);
            assertTrue(workflowResponse.isSuccessful());
            workflowIds.add(Integer.parseInt(workflowResponse.getData().getWorkflowId()));
            envelopeId = workflowResponse.getData().getRecordId();
            Thread.sleep(2000);

//            Query for Job Step ID
            String query = String.format("SELECT id FROM active_workflow_job__sys WHERE workflow__sys = %s", workflowIds.get(0));
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query);
            assertTrue(queryResponse.isSuccessful());
            String jobStepId = queryResponse.getData().get(0).getString("id");

//            Complete Job Step
            VaultResponse completeJobStepResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .completeJobStep(jobStepId, ObjectLifecycleWorkflowRequest.CompletionStatus.SUCCESS);
            assertTrue(completeJobStepResponse.isSuccessful());
            Thread.sleep(2000);

//            Get a Task ID
            ObjectWorkflowTaskResponse tasksResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .retrieveObjectWorkflowTasks(ENVELOPE__SYS, envelopeId, vaultClient.getUserId());
            assertTrue(tasksResponse.isSuccessful());
            for (ObjectWorkflowTaskResponse.ObjectWorkflowTask task : tasksResponse.getData()) {
                if (task.getStatus().get(0).equals("assigned__v")) {
                    taskId = task.getId();
                    break;
                }
            }
        }

        @AfterAll
        public void teardown() throws IOException {
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        void testRequest() throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode contentsArray = objectMapper.createArrayNode();
            ObjectNode taskNode = objectMapper.createObjectNode();
            taskNode.put("object__v", OBJECT_NAME);
            taskNode.put("record_id__v", recordIds.get(0));
            taskNode.put("verdict_public_key__c", "verdict_approve__c");
            contentsArray.add(taskNode);
            rootNode.set("contents__sys", contentsArray);

            response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .setContentTypeJson()
                    .setRequestString(rootNode.toString())
                    .initiateWorkflowTaskAction(taskId, taskName);

            Assertions.assertNotNull(response);

        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Tag("SmokeTest")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully complete a workflow job step")
    class TestCompleteJobStep {

        VaultResponse response = null;
        List<String> recordIds = new ArrayList<>();
        String workflowId = null;
        String jobStepId = null;

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//            Create Object record
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }
            Thread.sleep(3000);

//            Initiate Workflow on Object record
            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("contents__sys", String.format("Object:%s.%s", OBJECT_NAME, recordIds.get(0)));
            bodyParams.put("description__sys", "Description for Test Workflow");

            ObjectMultiRecordWorkflowInitiateResponse initiateWorkflowResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .setBodyParams(bodyParams)
                    .initiateMultiRecordWorkflow(MULTI_RECORD_WORKFLOW_NAME);
            assertTrue(initiateWorkflowResponse.isSuccessful());
            workflowId = initiateWorkflowResponse.getData().getWorkflowId();
            Thread.sleep(3000);

//            Query for Job Step ID
            String query = String.format("SELECT id FROM active_workflow_job__sys WHERE workflow__sys = %s", workflowId);
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query);
            assertTrue(queryResponse.isSuccessful());
            jobStepId = queryResponse.getData().get(0).getString("id");
        }

        @AfterAll
        public void teardown() throws IOException {
//            Cancel Workflow
            VaultResponse actionResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .initiateWorkflowAction(Integer.parseInt(workflowId), "cancel");
            assertTrue(actionResponse.isSuccessful());

//            Delete Object record
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        void testRequest() {
            response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .completeJobStep(jobStepId, ObjectLifecycleWorkflowRequest.CompletionStatus.SUCCESS);

            Assertions.assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("retrieve workflow action details")
    class TestRetrieveWorkflowActionDetails {

        ObjectWorkflowActionDetailsResponse response = null;
        List<String> recordIds = new ArrayList<>();
        int workflowId;

        @BeforeAll
        public void setup() throws InterruptedException, IOException {
//            Create Object record
            ObjectRecordBulkResponse createResponse = ObjectRecordRequestHelper.createMultipleObjectRecords(vaultClient, 1);
            assertTrue(createResponse.isSuccessful());

            for (ObjectRecordResponse objectRecordResponse : createResponse.getData()) {
                assertTrue(objectRecordResponse.isSuccessful());
                recordIds.add(objectRecordResponse.getData().getId());
            }
            Thread.sleep(3000);

//            Initiate Workflow on Object record
            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("contents__sys", String.format("Object:%s.%s", OBJECT_NAME, recordIds.get(0)));
            bodyParams.put("description__sys", "Description for Test Workflow");

            ObjectMultiRecordWorkflowInitiateResponse initiateWorkflowResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .setBodyParams(bodyParams)
                    .initiateMultiRecordWorkflow(MULTI_RECORD_WORKFLOW_NAME);
            assertTrue(initiateWorkflowResponse.isSuccessful());
            workflowId = Integer.parseInt(initiateWorkflowResponse.getData().getWorkflowId());
            Thread.sleep(3000);
        }

        @AfterAll
        public void teardown() throws IOException {
//            Cancel Workflow
            VaultResponse actionResponse = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .initiateWorkflowAction(workflowId, "cancel");
            assertTrue(actionResponse.isSuccessful());

//            Delete Object record
            ObjectRecordBulkResponse deleteResponse = ObjectRecordRequestHelper.deleteObjectRecords(vaultClient, recordIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        void testRequest() {
            response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                    .retrieveWorkflowActionDetails(workflowId, "replaceworkflowowner");

            Assertions.assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getName());
        }
    }
}