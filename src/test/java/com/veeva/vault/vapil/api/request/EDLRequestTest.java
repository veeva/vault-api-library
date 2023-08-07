package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.EDLResponse;
import com.veeva.vault.vapil.api.model.response.JobCreateResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.CtmsClientParameterResolver;

import java.util.Arrays;
import java.util.List;

@Tag("EDL")
@Disabled
public class EDLRequestTest {

    @Test
    @ExtendWith(CtmsClientParameterResolver.class)
    public void testCreatePlaceholderFromEDLItem( VaultClient vaultClient) {
        EDLResponse resp = vaultClient.newRequest(EDLRequest.class)
                .retrieveAllRootNodes(EDLRequest.NodeType.TEMPLATE);
        Assertions.assertTrue(resp.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS));
        Assertions.assertNotNull(resp.getData());
        Assertions.assertNotEquals(0, resp.getData().size());
    }

    @Test
    @ExtendWith(CtmsClientParameterResolver.class)
    public void testRetrieveEDLTemplate_SpecificRootNodes( VaultClient vaultClient) {
        List<String> referenceIDs = Arrays.asList("0EP000000000A10","0EP000000000A11");
        EDLResponse resp = vaultClient.newRequest(EDLRequest.class)
                .retrieveSpecificRootNodes(EDLRequest.NodeType.TEMPLATE, referenceIDs);
        Assertions.assertNotNull(resp);
        Assertions.assertNotNull(resp.getData());
        resp.getData().stream().allMatch(node -> node.getResponseStatus().equals("SUCCESS"));
        Assertions.assertNotEquals(0, resp.getData().size());
        Assertions.assertEquals(2, resp.getData().size());
    }

    @Test
    @ExtendWith(CtmsClientParameterResolver.class)
    public void testRetrieveEDLTemplate_AllRootNode(VaultClient vaultClient) {
        List<EDLResponse.EDLNode> resp = vaultClient.newRequest(EDLRequest.class)
                .retrieveAllRootNodes(EDLRequest.NodeType.TEMPLATE)
                .getData();
        Assertions.assertNotNull(resp);
        Assertions.assertNotEquals(0, resp.size());
    }

    @Test
    public void testCreatePlaceholderFromEDLItem(VaultClient vaultClient, List<String> edlItemIds) {
        System.out.println("****** Test Create Placeholder From EDL Item Start ******");

        JobCreateResponse resp = vaultClient.newRequest(EDLRequest.class)
                .createPlaceholderFromEDLItem(edlItemIds);

        System.out.println("Status = " + resp.getResponseStatus());
        if (resp.isSuccessful()) {
            System.out.println("JobId = " + resp.getJobId());
            System.out.println("Url = " + resp.getUrl());

            if (resp.getWarnings() != null) {
                for (VaultResponse.APIResponseWarning warning : resp.getWarnings()) {
                    System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
                }
            }
        }

        System.out.println("****** Test Create Placeholder From EDL Item Completed ******");
    }

    @Test
    public void testRetrieveEDLRootNode(VaultClient vaultClient) {
        System.out.println("\n****** Test Retrieve EDL Root Node Start ******");

        List<EDLResponse.EDLNode> resp = vaultClient.newRequest(EDLRequest.class)
                .retrieveAllRootNodes(EDLRequest.NodeType.TEMPLATE)
                .getData();
        resp.forEach(node->System.out.println(node));
        System.out.println("****** Test Retrieve EDL Root Node Complete ******");
    }

    @Test
    public void testRetrieveEDLNodeChildren(VaultClient vaultClient, String parentNodeId) {
        System.out.println("\n****** Test Retrieve EDL Node Children Start ******");
        List<EDLResponse.EDLNode> resp = vaultClient.newRequest(EDLRequest.class)
                .retrieveNodeChildren(EDLRequest.NodeType.TEMPLATE, parentNodeId)
                .getData();
        resp.forEach(node->System.out.println(node));
        System.out.println("****** Test Retrieve EDL Node Children Complete ******");
    }

    @Test
    public void testUpdateNodeOrderJson(VaultClient vaultClient, String parentNodeId, String inputFilePath) {
        System.out.println("\n****** Test Update EDL Node Order JSON Start ******");
        VaultResponse resp = vaultClient.newRequest(EDLRequest.class)
                .setContentTypeJson()
                .setInputPath(inputFilePath)
                .updateNodeOrder(EDLRequest.NodeType.TEMPLATE, parentNodeId);

        System.out.println(resp.getResponse());
        System.out.println("****** Test Update EDL Node Order JSON Complete ******");
    }

    @Test
    public void testUpdateNodeOrderCSV(VaultClient vaultClient, String parentNodeId, String inputFilePath) {
        System.out.println("\n****** Test Update EDL Node Order CSV Start ******");
        VaultResponse resp = vaultClient.newRequest(EDLRequest.class)
                .setContentTypeCsv()
                .setInputPath(inputFilePath)
                .updateNodeOrder(EDLRequest.NodeType.TEMPLATE, parentNodeId);

        System.out.println(resp.getResponse());
        System.out.println("****** Test Update EDL Node Order CSV Complete ******");
    }

    @Test
    public void testAddEdlMatchedDocuments(VaultClient vaultClient, String inputFilePath) {
        System.out.println("\n****** Test Add EDL Matched Document Start******");
        VaultResponse resp = vaultClient.newRequest(EDLRequest.class)
                .setContentTypeJson()
                .setInputPath(inputFilePath)
                .addEdlMatchedDocuments();

        System.out.println(resp.getResponse());
        System.out.println("****** Test Add EDL Matched Document Complete ******");
    }

    @Test
    public void testRemoveEdlMatchedDocuments(VaultClient vaultClient, String inputFilePath) {
        System.out.println("\n****** Test Remove EDL Matched Document Start******");
        VaultResponse resp = vaultClient.newRequest(EDLRequest.class)
                .setContentTypeJson()
                .setInputPath(inputFilePath)
                .removeEdlMatchedDocuments();

        System.out.println(resp.getResponse());
        System.out.println("****** Test Remove EDL Matched Document Complete ******");
    }
}
