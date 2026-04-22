package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class AgentRequestHelper {

  public static final String PATH_EXECUTE_AGENT_ACTION_JSON = FileHelper.PATH_RESOURCES_FOLDER + "agents" + File.separator + "execute_agent_action.json";

  public static void writeToExecuteAgentActionFile(String agentName, String actionName, String recordId) throws IOException {
    FileHelper.createFile(PATH_EXECUTE_AGENT_ACTION_JSON);
    File executeAgentActionFile = new File(PATH_EXECUTE_AGENT_ACTION_JSON);

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode requestNode = mapper.createObjectNode();

    requestNode.put("agent_name", agentName);
    requestNode.put("action_name", actionName);
    requestNode.put("object", ObjectRecordRequestHelper.OBJECT_NAME);
    requestNode.put("record_id", recordId);

    mapper.writerWithDefaultPrettyPrinter().writeValue(executeAgentActionFile, requestNode);
  }
}
