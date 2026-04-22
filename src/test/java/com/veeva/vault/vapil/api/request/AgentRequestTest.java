/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.AgentActionBulkResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionCancelResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionExecutionResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionExecutionStatusResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionResponse;
import com.veeva.vault.vapil.api.model.response.AgentBulkResponse;
import com.veeva.vault.vapil.api.model.response.AgentResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.extension.AgentRequestHelper;
import com.veeva.vault.vapil.extension.ObjectRecordRequestHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("AgentRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Agent Request should")
public class AgentRequestTest {

  private static final String AGENT_NAME = "base_object_chat_agent__sys";
  private static final String AGENT_ACTION_NAME = "summarize_record__sys";
  private static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;
  private static final String PATH_EXECUTE_AGENT_ACTION_JSON = AgentRequestHelper.PATH_EXECUTE_AGENT_ACTION_JSON;
  private static VaultClient vaultClient;

  @BeforeAll
  public static void setup(VaultClient client) throws IOException {
    vaultClient = client;
    Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Retrieve All Agents")
  class TestRetrieveAllAgents {

    AgentBulkResponse retrieveAllAgentsResponse = null;

    @Test
    @Order(1)
    public void testRequest() {
      retrieveAllAgentsResponse = vaultClient.newRequest(AgentRequest.class)
          .retrieveAllAgents();

      assertNotNull(retrieveAllAgentsResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(retrieveAllAgentsResponse.isSuccessful());
      assertNotNull(retrieveAllAgentsResponse.getAgents());

      for (AgentBulkResponse.Agent agent : retrieveAllAgentsResponse.getAgents()) {
        assertNotNull(agent.getName());
        assertNotNull(agent.getLabel());
        assertNotNull(agent.getUrl());
      }
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Retrieve Agent")
  class TestRetrieveAgent {

    AgentResponse retrieveAgentResponse = null;

    @Test
    @Order(1)
    public void testRequest() {
      retrieveAgentResponse = vaultClient.newRequest(AgentRequest.class)
          .retrieveAgent(AGENT_NAME);

      assertNotNull(retrieveAgentResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(retrieveAgentResponse.isSuccessful());
      assertNotNull(retrieveAgentResponse.getAgent());

      AgentResponse.Agent agent = retrieveAgentResponse.getAgent();
      assertNotNull(agent.getName());
      assertNotNull(agent.getLabel());
      assertNotNull(agent.getStatus());
      assertNotNull(agent.getAgentClass());
      assertNotNull(agent.getDescription());
      assertNotNull(agent.getSource());
      assertNotNull(agent.getAgentUser());
      assertNotNull(agent.getAgentModelLevel());

      assertNotNull(agent.getActions());
      for (AgentResponse.Agent.AgentAction action : agent.getActions()) {
        assertNotNull(action.getName());
        assertNotNull(action.getLabel());
        assertNotNull(action.getUrl());
      }
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Retrieve All Agent Actions")
  class TestRetrieveAllAgentActions {

    AgentActionBulkResponse retrieveAllAgentActionsResponse = null;

    @Test
    @Order(1)
    public void testRequest() {
      retrieveAllAgentActionsResponse = vaultClient.newRequest(AgentRequest.class)
          .retrieveAllAgentActions();

      assertNotNull(retrieveAllAgentActionsResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(retrieveAllAgentActionsResponse.isSuccessful());
      assertNotNull(retrieveAllAgentActionsResponse.getAgents());

      for (AgentActionBulkResponse.Agent agent : retrieveAllAgentActionsResponse.getAgents()) {
        assertNotNull(agent.getAgentName());
        assertNotNull(agent.getAgentLabel());
        assertNotNull(agent.getDescription());

        if (agent.getActions() != null) {
          for (AgentActionBulkResponse.Agent.AgentAction action : agent.getActions()) {
            assertNotNull(action.getName());
            assertNotNull(action.getLabel());
            assertNotNull(action.getUrl());
            assertNotNull(action.getDescription());
          }
        }
      }
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Retrieve Agent Action")
  class TestRetrieveAgentAction {

    AgentActionResponse retrieveAgentActionResponse = null;

    @Test
    @Order(1)
    public void testRequest() {
      retrieveAgentActionResponse = vaultClient.newRequest(AgentRequest.class)
          .retrieveAgentAction(AGENT_NAME, AGENT_ACTION_NAME);

      assertNotNull(retrieveAgentActionResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(retrieveAgentActionResponse.isSuccessful());
      assertNotNull(retrieveAgentActionResponse.getAgentAction());

      AgentActionResponse.AgentAction action = retrieveAgentActionResponse.getAgentAction();
      assertNotNull(action.getName());
      assertNotNull(action.getLabel());
      assertNotNull(action.getActive());
      assertNotNull(action.getSource());
      assertNotNull(action.getDescription());
      assertNotNull(action.getSupportChat());
      assertNotNull(action.getToolEvaluation());
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Execute Agent Action (String)")
  class TestExecuteAgentActionFromRequestString {

    AgentActionExecutionResponse executeAgentActionResponse = null;
    String recordId;
    String requestString;

    @BeforeAll
    public void setup() throws IOException {
      QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
      recordId = String.valueOf(queryResponse.getData().get(0).getString("id"));

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode requestNode = mapper.createObjectNode();
      requestNode.put("agent_name", AGENT_NAME);
      requestNode.put("action_name", AGENT_ACTION_NAME);
      requestNode.put("object", OBJECT_NAME);
      requestNode.put("record_id", recordId);
      requestString = requestNode.toString();
    }

    @Test
    @Order(1)
    public void testRequest() {
      executeAgentActionResponse = vaultClient.newRequest(AgentRequest.class)
          .setRequestString(requestString)
          .executeAgentAction();

      assertNotNull(executeAgentActionResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(executeAgentActionResponse.isSuccessful());
      assertNotNull(executeAgentActionResponse.getActionResponse());

      AgentActionExecutionResponse.ActionResponse actionResponse = executeAgentActionResponse.getActionResponse();
      assertNotNull(actionResponse.getActionStatus());
      assertNotNull(actionResponse.getExecutionId());
      assertNotNull(actionResponse.getUrls());
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Execute Agent Action (File)")
  class TestExecuteAgentActionFromInputPath {

    AgentActionExecutionResponse executeAgentActionResponse = null;
    String recordId;

    @BeforeAll
    public void setup() throws IOException {
      QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
      recordId = String.valueOf(queryResponse.getData().get(0).getString("id"));

      AgentRequestHelper.writeToExecuteAgentActionFile(AGENT_NAME, AGENT_ACTION_NAME, recordId);
    }

    @Test
    @Order(1)
    public void testRequest() {
      executeAgentActionResponse = vaultClient.newRequest(AgentRequest.class)
          .setInputPath(PATH_EXECUTE_AGENT_ACTION_JSON)
          .executeAgentAction();

      assertNotNull(executeAgentActionResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(executeAgentActionResponse.isSuccessful());
      assertNotNull(executeAgentActionResponse.getActionResponse());

      AgentActionExecutionResponse.ActionResponse actionResponse = executeAgentActionResponse.getActionResponse();
      assertNotNull(actionResponse.getActionStatus());
      assertNotNull(actionResponse.getExecutionId());
      assertNotNull(actionResponse.getUrls());
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Execute Agent Action (Binary)")
  class TestExecuteAgentActionFromBinaryFile {

    AgentActionExecutionResponse executeAgentActionResponse = null;
    String recordId;

    @BeforeAll
    public void setup() throws IOException {
      QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
      recordId = String.valueOf(queryResponse.getData().get(0).getString("id"));

      AgentRequestHelper.writeToExecuteAgentActionFile(AGENT_NAME, AGENT_ACTION_NAME, recordId);
    }

    @Test
    @Order(1)
    public void testRequest() throws IOException {
      File file = new File(PATH_EXECUTE_AGENT_ACTION_JSON);

      executeAgentActionResponse = vaultClient.newRequest(AgentRequest.class)
          .setBinaryFile(file.getName(), Files.readAllBytes(file.toPath()))
          .executeAgentAction();

      assertNotNull(executeAgentActionResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(executeAgentActionResponse.isSuccessful());
      assertNotNull(executeAgentActionResponse.getActionResponse());

      AgentActionExecutionResponse.ActionResponse actionResponse = executeAgentActionResponse.getActionResponse();
      assertNotNull(actionResponse.getActionStatus());
      assertNotNull(actionResponse.getExecutionId());
      assertNotNull(actionResponse.getUrls());
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Cancel Agent Action Execution")
  class TestCancelAgentActionExecution {

    AgentActionCancelResponse cancelAgentActionResponse = null;
    String executionId;

    @BeforeAll
    public void setup() throws IOException, InterruptedException {
      QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
      String recordId = String.valueOf(queryResponse.getData().get(0).getString("id"));

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode executeRequestNode = mapper.createObjectNode();
      executeRequestNode.put("agent_name", AGENT_NAME);
      executeRequestNode.put("action_name", AGENT_ACTION_NAME);
      executeRequestNode.put("object", OBJECT_NAME);
      executeRequestNode.put("record_id", recordId);
      String executeRequestString = executeRequestNode.toString();

      AgentActionExecutionResponse executeResponse = vaultClient.newRequest(AgentRequest.class)
          .setRequestString(executeRequestString)
          .executeAgentAction();

      assertTrue(executeResponse.isSuccessful());
      assertNotNull(executeResponse.getActionResponse());
      executionId = executeResponse.getActionResponse().getExecutionId();

      // Delay slightly to ensure execution is registered before cancellation
      Thread.sleep(1000);
    }

    @Test
    @Order(1)
    public void testRequest() {
      cancelAgentActionResponse = vaultClient.newRequest(AgentRequest.class)
          .cancelAgentActionExecution(executionId);

      assertNotNull(cancelAgentActionResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(cancelAgentActionResponse.isSuccessful());
      assertNotNull(cancelAgentActionResponse.getData());
      
      AgentActionCancelResponse.Data data = cancelAgentActionResponse.getData();
      assertNotNull(data.getActionStatus());
      assertNotNull(data.getExecutionId());
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("successfully Retrieve Agent Action Execution Status")
  class TestRetrieveAgentActionExecutionStatus {

    AgentActionExecutionStatusResponse retrieveAgentActionExecutionStatusResponse = null;
    String executionId;

    @BeforeAll
    public void setup() throws IOException, InterruptedException {
      QueryResponse queryResponse = ObjectRecordRequestHelper.queryForRecordId(vaultClient);
      String recordId = String.valueOf(queryResponse.getData().get(0).getString("id"));

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode executeRequestNode = mapper.createObjectNode();
      executeRequestNode.put("agent_name", AGENT_NAME);
      executeRequestNode.put("action_name", AGENT_ACTION_NAME);
      executeRequestNode.put("object", OBJECT_NAME);
      executeRequestNode.put("record_id", recordId);
      String executeRequestString = executeRequestNode.toString();

      AgentActionExecutionResponse executeResponse = vaultClient.newRequest(AgentRequest.class)
          .setRequestString(executeRequestString)
          .executeAgentAction();

      assertTrue(executeResponse.isSuccessful());
      assertNotNull(executeResponse.getActionResponse());
      executionId = executeResponse.getActionResponse().getExecutionId();

      Thread.sleep(10000);
    }

    @Test
    @Order(1)
    public void testRequest() {
      retrieveAgentActionExecutionStatusResponse = vaultClient.newRequest(AgentRequest.class)
          .setExecutionId(executionId)
          .retrieveAgentActionExecutionStatus();

      assertNotNull(retrieveAgentActionExecutionStatusResponse);
    }

    @Test
    @Order(2)
    public void testResponse() {
      assertTrue(retrieveAgentActionExecutionStatusResponse.isSuccessful());
      assertNotNull(retrieveAgentActionExecutionStatusResponse.getActionResponse());

      AgentActionExecutionStatusResponse.ActionResponse actionResponse = retrieveAgentActionExecutionStatusResponse.getActionResponse();
      assertNotNull(actionResponse.getActionStatus());
      assertNotNull(actionResponse.getExecutionId());
      assertNotNull(actionResponse.getAgentAction());

      if (actionResponse.getActionOutputs() != null) {
          for (AgentActionExecutionStatusResponse.ActionResponse.ActionOutput output : actionResponse.getActionOutputs()) {
              assertNotNull(output.getType());
              assertNotNull(output.getValue());
          }
      }
    }
  }
}
