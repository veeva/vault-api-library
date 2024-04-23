package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVWriter;
import com.veeva.vault.vapil.api.client.VaultClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileHelper {
    static final String TEST_FILE_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "vapil_test_document.docx";
    static final String LOADER_FILE_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "loader_file.csv";
    static final String RESUMABLE_UPLOAD_FILE_PATH = "src" + File.separator + "test" +
            File.separator + "resources" + File.separator + "test_resumable_upload.txt";
    private static Logger log = Logger.getLogger(FileHelper.class);

    public static String getPathTestFile() {
        return TEST_FILE_PATH;
    }
    public static String getPathResumableUploadFile() {
        return RESUMABLE_UPLOAD_FILE_PATH;
    }
    public static String getPathLoaderFile() {
        return LOADER_FILE_PATH;
    }
    public static void createCsvFile(String filePath) {
        File csvFile = new File(filePath);
        if (csvFile.exists()) {
            csvFile.delete();
        }
        try {
            csvFile.createNewFile();
        } catch (IOException e) {
            log.error("Error creating CSV file: " + filePath);
            e.printStackTrace();
        }
    }

    public static void writeCsvFile(String csvPath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            writer.writeAll((data));
        } catch (IOException e) {
            log.error("Error writing CSV file: " + csvPath);
            e.printStackTrace();
        }
    }

    public static String readTextFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileRead = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileRead);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            log.error("Error reading Text file: " + filePath);
        }

        return stringBuilder.toString();
    }

    public static File getSettingsFile(String filePath) {
        File settingsFile = new File(filePath);
        if (!settingsFile.exists()) {
            String errorMessage = String.format("JSON settings file '%s' not found", settingsFile.getAbsolutePath());
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        return settingsFile;
    }

    public static JsonNode readSettingsFile(File settingsFile) {
        JsonNode rootNode;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rootNode = objectMapper.readTree(settingsFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read JSON settings file", e);
        }
        return rootNode;
    }

    public static Map<String, String> convertJsonNodeToMap(JsonNode jsonNode) {
        Map<String, String> map = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            String value = field.getValue().asText();
            map.put(key, value);
        }
        return map;
    }

    public static String convertJsonFileToString(File jsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(objectMapper.readTree(jsonFile));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to convert JSON file to string", e);
        }
    }

    public static void writeSessionId(String sessionId, JsonNode rootNode, File settingsFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Update existing key-value pair
            ((ObjectNode) rootNode).put("vaultSessionId", sessionId);

            // Write updated JSON to file
            objectMapper.writeValue(settingsFile, rootNode);

            log.info("Session ID written to JSON settings file successfully!");

        } catch (IOException e) {
            log.error("Unable to write Session ID to JSON settings file");
        }
    }

    public static void writeOauthToken(String oauthToken, JsonNode rootNode, File settingsFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Update existing key-value pair
            ((ObjectNode) rootNode).put("idpOauthAccessToken", oauthToken);

            // Write updated JSON to file
            objectMapper.writeValue(settingsFile, rootNode);

            log.info("Oauth Token written to JSON settings file successfully!");

        } catch (IOException e) {
            log.error("Unable to write Oauth Token to JSON settings file");
        }
    }
}
