package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.BulkWorkflowActionDetailsResponse;
import com.veeva.vault.vapil.api.model.response.BulkWorkflowActionsResponse;
import com.veeva.vault.vapil.api.model.response.BulkWorkflowJobStartResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.Arrays;
import java.util.List;

@Tag("BulkWorkflow")
@ExtendWith(VaultClientParameterResolver.class)
public class BulkActiveWorkflowActionRequestTest {

    @Test
    public void restCancelTasks(VaultClient vaultClient) {
        List<Integer> ids = Arrays.asList(new Integer[]{1000,1001});
        //Dummy IDs, the job will be initialized successfully, but likely to fail since the IDs are not real.
        BulkWorkflowJobStartResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .setTaskIds(ids)
                .cancelWorkflowTasks();
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotNull(resp.getData().getJobId());

    }

    @Test
    public void restCancelWorkflow(VaultClient vaultClient) {
        List<Integer> ids = Arrays.asList(new Integer[]{1000,1001});
        //Dummy IDs, the job will be initialized successfully, but likely to fail since the IDs are not real.
        BulkWorkflowJobStartResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .cancelWorkflows(ids);
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotNull(resp.getData().getJobId());

    }

    @Test
    public void testReassignTasks(VaultClient vaultClient) {
        BulkWorkflowJobStartResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .reassignTasks(12345, 67890);
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotNull(resp.getData().getJobId());
    }

    @Test
    public void testReplaceWorkflowOwner(VaultClient vaultClient) {
        BulkWorkflowJobStartResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .replaceWorkflowOwner(12345, 67890);
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotNull(resp.getData().getJobId());
    }

    @Test
    public void testRetrieveBulkWorkflowActions( VaultClient vaultClient) {
        BulkWorkflowActionsResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .retrieveBulkWorkflowActions();
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotEquals(0, resp.getData().size());
    }

    @Test
    public void testRetrieveBulkWorkflowActionDetails( VaultClient vaultClient) {
        BulkWorkflowActionsResponse prep = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .retrieveBulkWorkflowActions();
        Assertions.assertTrue(prep.isSuccessful());
        Assertions.assertNotNull(prep.getData());
        Assertions.assertFalse(prep.getData().isEmpty());

        String action = prep.getData().get(0).getName();
        BulkWorkflowActionDetailsResponse resp = vaultClient.newRequest(BulkActiveWorkflowActionRequest.class)
                .retrieveBulkWorkflowActionDetails(action);
        Assertions.assertTrue(resp.isSuccessful());
        Assertions.assertNotNull(resp.getData());

        BulkWorkflowActionDetailsResponse.BulkWorkflowActionDetails detail = resp.getData();
        Assertions.assertNotNull(detail.getName());
        Assertions.assertNotNull(detail.getControls());
        Assertions.assertFalse(detail.getControls().isEmpty());

        BulkWorkflowActionDetailsResponse.Control control = detail.getControls().get(0);
        Assertions.assertNotNull(control.getLabel());
        Assertions.assertNotNull(control.getType());
        Assertions.assertNotNull(control.getPrompts());
        Assertions.assertFalse(control.getPrompts().isEmpty());

        BulkWorkflowActionDetailsResponse.Prompt prompt = control.getPrompts().get(0);
        Assertions.assertNotNull(prompt.getRequired());
        Assertions.assertNotNull(prompt.getLabel());
        Assertions.assertNotNull(prompt.getMultiValue());
        Assertions.assertNotNull(prompt.getName());

    }


}
