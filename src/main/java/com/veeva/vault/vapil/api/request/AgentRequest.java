/*---------------------------------------------------------------------
 *	Copyright (c) 2024 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.model.response.AgentActionBulkResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionCancelResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionExecutionResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionExecutionStatusResponse;
import com.veeva.vault.vapil.api.model.response.AgentActionResponse;
import com.veeva.vault.vapil.api.model.response.AgentBulkResponse;
import com.veeva.vault.vapil.api.model.response.AgentResponse;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;

/**
 * The Agent API allows you to retrieve agents
 *
 * @vapil.apicoverage <a href="https://general.veevavault.dev/ai-agents/api/">https://general.veevavault.dev/ai-agents/api/</a>
 */
public class AgentRequest extends VaultRequest<AgentRequest> {

    // API Endpoints
    private static final String URL_AGENTS = "/services/ai/agents";
    private static final String URL_AGENT = "/services/ai/agents/{agent_name}";
    private static final String URL_AGENT_ACTIONS = "/services/ai/agent_actions";
    private static final String URL_AGENT_ACTION = "/services/ai/agent_action/{agent_name}/{action_name}";
    private static final String URL_EXECUTE_AGENT_ACTION = "/services/ai/agent_actions/actions/execute";
    private static final String URL_CANCEL_AGENT_ACTION = "/services/ai/agent_actions/actions/cancel";
    private static final String URL_RETRIEVE_AGENT_ACTION_EXECUTION_STATUS = "/services/ai/agent_actions/status";

    // Builder Parameters
    private HttpRequestConnector.BinaryFile binaryFile;
    private String inputPath;
    private String requestString;
    private String externalId;
    private String executionId;

    private AgentRequest() {
        // Defaults for the request
    }

    /**
     * <b>Retrieve All Agents</b>
     * <p>
     * Retrieve all of the agents in the authenticated Vault. Includes both System and custom agents.
     *
     * @return AgentBulkResponse
     * @vapil.api <pre>
     * GET /api/{version}/services/ai/agents</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/retrieve-agents/' target='_blank'>https://general.veevavault.dev/ai-agents/api/retrieve-agents/</a>
     * @vapil.request <pre>
     * AgentBulkResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.retrieveAllAgents();</pre>
     * @vapil.response <pre>
     * for (AgentBulkResponse.Agent agent : response.getAgents()) {
     * 		System.out.println("-------- Agent --------");
     * 		System.out.println("Name = " + agent.getName());
     * 		System.out.println("Label = " + agent.getLabel());
     * 		System.out.println("Url = " + agent.getUrl());
     * }
     * </pre>
     */
    public AgentBulkResponse retrieveAllAgents() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_AGENTS));

        return send(HttpMethod.GET, request, AgentBulkResponse.class);
    }

    /**
     * <b>Retrieve Agent</b>
     * <p>
     * Retrieve details of the specified agent, including all of the agent’s actions.
     *
     * @param agentName The name of the agent to retrieve. For example, promomats_document_chat__v.
     * @return AgentResponse
     * @vapil.api <pre>
     * GET /api/{version}/services/ai/agents/{agent_name}</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/retrieve-agent/' target='_blank'>https://general.veevavault.dev/ai-agents/api/retrieve-agent/</a>
     * @vapil.request <pre>
     * AgentResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.retrieveAgent(AGENT_NAME);</pre>
     * @vapil.response <pre>
     * Agent agent = response.getAgent();
     * System.out.println("Name = " + agent.getName());
     * System.out.println("Label = " + agent.getLabel());
     * System.out.println("Status = " + agent.getStatus());
     * System.out.println("Agent Class = " + agent.getAgentClass());
     *
     * for (AgentAction action : agent.getActions()) {
     * 		System.out.println("Action Name = " + action.getName());
     * 		System.out.println("Action Label = " + action.getLabel());
     * 		System.out.println("Action Url = " + action.getUrl());
     * }</pre>
     */
    public AgentResponse retrieveAgent(String agentName) {
        String url = vaultClient.getAPIEndpoint(URL_AGENT)
                .replace("{agent_name}", agentName);
        HttpRequestConnector request = new HttpRequestConnector(url);

        return send(HttpMethod.GET, request, AgentResponse.class);
    }

    /**
     * <b>Retrieve All Agent Actions</b>
     * <p>
     * Retrieves all agent actions in the currently authenticated Vault.
     *
     * @return AgentActionBulkResponse
     * @vapil.api <pre>
     * GET /api/{version}/services/ai/agent_actions</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/retrieve-agent-actions/' target='_blank'>https://general.veevavault.dev/ai-agents/api/retrieve-agent-actions/</a>
     * @vapil.request <pre>
     * AgentActionBulkResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.retrieveAllAgentActions();</pre>
     * @vapil.response <pre>
     * for (AgentActionBulkResponse.Agent agent : response.getAgents()) {
     * 		System.out.println("-------- Agent --------");
     * 		System.out.println("Agent Name = " + agent.getAgentName());
     * 		System.out.println("Agent Label = " + agent.getAgentLabel());
     *
     * 		for (AgentActionBulkResponse.Agent.AgentAction action : agent.getActions()) {
     * 			System.out.println("-------- Action --------");
     * 			System.out.println("Action Name = " + action.getName());
     * 			System.out.println("Action Label = " + action.getLabel());
     * 		}
     * }
     * </pre>
     */
    public AgentActionBulkResponse retrieveAllAgentActions() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_AGENT_ACTIONS));

        return send(HttpMethod.GET, request, AgentActionBulkResponse.class);
    }

    /**
     * <b>Retrieve Agent Action</b>
     * <p>
     * Retrieve the details of a specific agent action.
     *
     * @param agentName The name of the agent to retrieve. For example, promomats_document_chat__v.
     * @param actionName The name of the action to retrieve. For example, spelling_grammar__v.
     * @return AgentActionResponse
     * @vapil.api <pre>
     * GET /api/{version}/services/ai/agent_action/{agent_name}/{action_name}</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/retrieve-agent-action/' target='_blank'>https://general.veevavault.dev/ai-agents/api/retrieve-agent-action/</a>
     * @vapil.request <pre>
     * AgentActionResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.retrieveAgentAction("quick_check__v", "spelling_grammar__v");</pre>
     * @vapil.response <pre>
     * AgentAction action = response.getAgentAction();
     * System.out.println("Name = " + action.getName());
     * System.out.println("Label = " + action.getLabel());
     * System.out.println("Active = " + action.getActive());
     * System.out.println("Source = " + action.getSource());
     * System.out.println("Description = " + action.getDescription());
     * System.out.println("Support Chat = " + action.getSupportChat());
     * System.out.println("Tool Evaluation = " + action.getToolEvaluation());
     * </pre>
     */
    public AgentActionResponse retrieveAgentAction(String agentName, String actionName) {
        String url = vaultClient.getAPIEndpoint(URL_AGENT_ACTION)
                .replace("{agent_name}", agentName)
                .replace("{action_name}", actionName);
        HttpRequestConnector request = new HttpRequestConnector(url);

        return send(HttpMethod.GET, request, AgentActionResponse.class);
    }

    /**
     * <b>Execute Agent Action</b>
     * <p>
     * Execute the specified agent action. The authenticated user must have permission to execute the action.
     *
     * @return AgentActionExecutionResponse
     * @vapil.api <pre>
     * POST /api/{version}/services/ai/agent_actions/actions/execute</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/execute-agent-action/' target='_blank'>https://general.veevavault.dev/ai-agents/api/execute-agent-action/</a>
     * @vapil.request <pre>
     * <i>Example 1 - Input File</i>
     * AgentActionExecutionResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.setInputPath(inputFilePath)
     * 		.executeAgentAction();
     *
     * <i>Example 2 - JSON String</i>
     * AgentActionExecutionResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.setRequestString(jsonString)
     * 		.executeAgentAction();
     *
     * <i>Example 3 - Binary File</i>
     * AgentActionExecutionResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.setBinaryFile(file.getName(), Files.readAllBytes(file.toPath()))
     * 		.executeAgentAction();
     * </pre>
     * @vapil.response <pre>
     * AgentActionExecutionResponse.ActionResponse actionResponse = response.getActionResponse();
     * System.out.println("Action Status = " + actionResponse.getActionStatus());
     * System.out.println("Execution ID = " + actionResponse.getExecutionId());
     * </pre>
     */
    public AgentActionExecutionResponse executeAgentAction() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_EXECUTE_AGENT_ACTION));
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        if (inputPath != null && !inputPath.isEmpty()) {
            request.addFile(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, inputPath);
        }

        if (requestString != null && !requestString.isEmpty()) {
            request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestString);
        }

        if (binaryFile != null) {
            request.addBinary(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, binaryFile.getBinaryContent());
        }

        return send(HttpMethod.POST, request, AgentActionExecutionResponse.class);
    }

    /**
     * <b>Cancel Agent Action Execution</b>
     * <p>
     * Request the cancellation of an in-progress agent action.
     *
     * @param executionId The execution ID of the agent action to cancel
     * @return AgentActionCancelResponse
     * @vapil.api <pre>
     * POST /api/{version}/services/ai/agent_actions/actions/cancel</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/cancel-agent-action-execution/' target='_blank'>https://general.veevavault.dev/ai-agents/api/cancel-agent-action-execution/</a>
     * @vapil.request <pre>
     * AgentActionCancelResponse response = vaultClient.newRequest(AgentRequest.class)
     * 		.cancelAgentActionExecution("VAB000000001003");
     * </pre>
     * @vapil.response <pre>
     * System.out.println("Response Status = " + response.getResponseStatus());
     * System.out.println("Action Status = " + response.getData().getActionStatus());
     * System.out.println("Execution ID = " + response.getData().getExecutionId());
     * </pre>
     */
    public AgentActionCancelResponse cancelAgentActionExecution(String executionId) {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_CANCEL_AGENT_ACTION));
        request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestNode = mapper.createObjectNode();
        requestNode.put("execution_id", executionId);
        
        request.addRawString(HttpRequestConnector.HTTP_CONTENT_TYPE_JSON, requestNode.toString());

        return send(HttpMethod.POST, request, AgentActionCancelResponse.class);
    }

    /**
     * <b>Retrieve Agent Action Execution Status</b>
     * <p>
     * Retrieve the status of an executed agent action. Requires at least one query parameter, execution_id or external_id, to identify the action.
     *
     * @return AgentActionExecutionStatusResponse
     * @vapil.api <pre>
     * GET /api/{version}/services/ai/agent_actions/status</pre>
     * @vapil.vaultlink <a href='https://general.veevavault.dev/ai-agents/api/retrieve-agent-action-execution-status/' target='_blank'>https://general.veevavault.dev/ai-agents/api/retrieve-agent-action-execution-status/</a>
     * @vapil.request <pre>
     * AgentActionExecutionStatusResponse response = vaultClient.newRequest(AgentRequest.class)
     *      .setExecutionId("VAB000000001003")
     *      .retrieveAgentActionExecutionStatus();
     * </pre>
     * @vapil.response <pre>
     * AgentActionExecutionStatusResponse.ActionResponse actionResponse = response.getActionResponse();
     * System.out.println("Action Status = " + actionResponse.getActionStatus());
     * System.out.println("Execution ID = " + actionResponse.getExecutionId());
     * System.out.println("Agent Action = " + actionResponse.getAgentAction());
     *
     * for (AgentActionExecutionStatusResponse.ActionResponse.ActionOutput output : actionResponse.getActionOutputs()) {
     *      System.out.println("-------- Output --------");
     *      System.out.println("Output Type = " + output.getType());
     *      System.out.println("Output Value = " + output.getValue());
     * }
     * </pre>
     */
    public AgentActionExecutionStatusResponse retrieveAgentActionExecutionStatus() {
        HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_RETRIEVE_AGENT_ACTION_EXECUTION_STATUS));

        if (executionId != null && !executionId.isEmpty()) {
            request.addQueryParam("execution_id", executionId);
        }

        if (externalId != null && !externalId.isEmpty()) {
            request.addQueryParam("external_id", externalId);
        }

        return send(HttpMethod.GET, request, AgentActionExecutionStatusResponse.class);
    }

    /*
     *
     * Request parameter setters
     *
     */

    /**
     * Specify source data in an input file
     *
     * @param filename      file name (no path)
     * @param binaryContent byte array of the file content
     * @return The Request
     */
    public AgentRequest setBinaryFile(String filename, byte[] binaryContent) {
        this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
        return this;
    }

    /**
     * Specify source data in an input file
     *
     * @param inputPath Absolute path to the file for the request
     * @return The Request
     */
    public AgentRequest setInputPath(String inputPath) {
        this.inputPath = inputPath;
        return this;
    }

    /**
     * Specify source data in an input string, such as a JSON request
     *
     * @param requestString The source request as a string
     * @return The Request
     */
    public AgentRequest setRequestString(String requestString) {
        this.requestString = requestString;
        return this;
    }

    /**
     * Specify the execution ID of the agent action execution.
     *
     * @param executionId The execution ID
     * @return The Request
     */
    public AgentRequest setExecutionId(String executionId) {
        this.executionId = executionId;
        return this;
    }

    /**
     * Specify the external ID of the agent action execution.
     *
     * @param externalId The external ID
     * @return The Request
     */
    public AgentRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }
}
