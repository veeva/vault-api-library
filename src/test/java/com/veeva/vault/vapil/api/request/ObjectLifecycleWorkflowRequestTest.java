package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectMultiRecordWorkflowInitiateResponse;
import com.veeva.vault.vapil.api.model.response.ObjectMultiRecordWorkflowDetailsResponse;
import com.veeva.vault.vapil.api.model.response.ObjectMultiRecordWorkflowsResponse;
import com.veeva.vault.vapil.api.model.response.ObjectMultiRecordWorkflowDetailsResponse.Control;
import com.veeva.vault.vapil.api.model.response.ObjectMultiRecordWorkflowDetailsResponse.Control.Prompt;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@Tag("ObjectLifecycleWorkflowRequest")
@ExtendWith(VaultClientParameterResolver.class)
class ObjectLifecycleWorkflowRequestTest {

    @Test
    @DisplayName("Retrieve Multi-Record Workflows")
    void testRetrieveMultiRecordWorkflows(VaultClient vaultClient) {
        ObjectMultiRecordWorkflowsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveMultiRecordWorkflows();

        assertTrue(response.isSuccessful());
        System.out.println("Response Status: " + response.getResponseStatus());
        System.out.println("Response: " + response.getResponse());
        System.out.println();

        for (ObjectMultiRecordWorkflowsResponse.MultiRecordWorkflow workflow : response.getData()) {
            System.out.println("Name: " + workflow.getName());
            System.out.println("Label: " + workflow.getLabel());
            System.out.println("Type: " + workflow.getType());
            System.out.println("Cardinality: " + workflow.getCardinality());
            System.out.println();
        }
    }

    @Test
    @DisplayName("Retrieve Multi-Record Workflow Details")
    void testRetrieveMultiRecordWorkflowDetails(VaultClient vaultClient) {
        ObjectMultiRecordWorkflowDetailsResponse response = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .retrieveMultiRecordWorkflowDetails("Objectworkflow.test_workflow__c");

        assertTrue(response.isSuccessful());
        System.out.println("Response Status: " + response.getResponseStatus());
        System.out.println("Response: " + response.getResponse());

        System.out.println("Name: " + response.getData().getName());
        System.out.println("Label: " + response.getData().getLabel());
        System.out.println("Type: " + response.getData().getType());
        System.out.println("Cardinality: " + response.getData().getCardinality());
        if (response.getData().getControls() != null) {
            System.out.println("Controls: ");
            for (Control control : response.getData().getControls()) {
                System.out.println("    Control Label: " + control.getLabel());
                System.out.println("    Control Type: " + control.getType());
                System.out.println("    Prompts: ");
                for (Prompt prompt : control.getPrompts()) {
                    System.out.println("        Prompt Label: " + prompt.getLabel());
                    System.out.println("        Prompt Name: " + prompt.getName());
                }
            }
        }
    }

    @Test
    void testInitiateMultiRecordWorkflow(VaultClient vaultClient) {

        Map<String, Object> bodyParams = new HashMap<>();
        String workflowName = "Objectworkflow.test_workflow__c";
        String record = "Object:test_object__c.V4O000000004001";
        String description = "Description for Test Workflow";
        String owners = "user:1008109";
        bodyParams.put("contents__sys", record);
        bodyParams.put("description__sys", description);
        bodyParams.put("part_owners__c", owners);

        ObjectMultiRecordWorkflowInitiateResponse resp = vaultClient.newRequest(ObjectLifecycleWorkflowRequest.class)
                .setBodyParams(bodyParams)
                .initiateMultiRecordWorkflow(workflowName);

        assertTrue(resp.isSuccessful());
        System.out.println("Response Status: " + resp.getResponseStatus());
        System.out.println("Response :" + resp.getResponse());
        System.out.println("Envelope Id " + resp.getData().getRecordId());
        System.out.println("Workflow Id " + resp.getData().getRecordId());


    }
}