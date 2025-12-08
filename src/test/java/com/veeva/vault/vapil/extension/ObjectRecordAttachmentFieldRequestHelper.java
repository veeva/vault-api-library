package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ObjectRecordAttachmentFieldRequestHelper {

    public static final String ATTACHMENT_FIELD_NAME = "vapil_attachment_field__c";
    public static final String PATH_RESOURCES_VAULT_OBJECTS_ATTACHMENT_FIELDS_FOLDER = ObjectRecordRequestHelper.PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "attachment_fields";
    public static final String PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON = PATH_RESOURCES_VAULT_OBJECTS_ATTACHMENT_FIELDS_FOLDER + File.separator + "export_attachment_field_files.json";
    public static final String PATH_DOWNLOAD_ATTACHMENT_FIELD_FILE = PATH_RESOURCES_VAULT_OBJECTS_ATTACHMENT_FIELDS_FOLDER + File.separator + "test_download_attachment_field_file.txt";
    public static final String PATH_DOWNLOAD_ATTACHMENT_FIELD_FILES = PATH_RESOURCES_VAULT_OBJECTS_ATTACHMENT_FIELDS_FOLDER + File.separator + "test_download_attachment_field_files.zip";
    private static final String OBJECT_NAME = ObjectRecordRequestHelper.OBJECT_NAME;

    public static QueryResponse queryForRecordIdWithAttachmentField(VaultClient vaultClient) {
        String query = """
                SELECT id,
                FILENAME(%s)
                FROM %s
                WHERE %s != null
                MAXROWS 1
                """.formatted(ATTACHMENT_FIELD_NAME, OBJECT_NAME, ATTACHMENT_FIELD_NAME);

        return vaultClient.newRequest(QueryRequest.class)
                .query(query);
    }

    public static void writeToExportAttachmentFieldFilesJson(List<String> recordIds) throws IOException {
        FileHelper.createFile(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON);
        File exportAttachmendFieldFilesFile = new File(PATH_EXPORT_ATTACHMENT_FIELD_FILES_JSON);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();

        for (String id : recordIds) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("id", id);
            rootNode.add(objectNode);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(exportAttachmendFieldFilesFile, rootNode);
    }
}
