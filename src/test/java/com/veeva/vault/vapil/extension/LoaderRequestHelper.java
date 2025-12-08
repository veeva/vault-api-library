package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.DocumentBulkResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.DocumentRequest;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoaderRequestHelper {

    public static final String NAME_LOAD_DOCUMENTS_CSV = "load_data_documents.csv";
    public static final String NAME_LOAD_OBJECT_RECORDS_CSV = "load_data_object_records.csv";
    public static final String NAME_LOAD_CREATE_ATTACHMENTS_CSV = "load_data_create_attachments.csv";
    public static final String NAME_LOAD_DELETE_ATTACHMENTS_CSV = "load_data_delete_attachments.csv";
    public static final String NAME_LOAD_ASSIGN_ROLES_CSV = "load_data_assign_roles.csv";
    public static final String NAME_LOAD_REMOVE_ROLES_CSV = "load_data_remove_roles.csv";
    public static final String NAME_LOAD_CHANGE_OBJECT_TYPE_CSV = "load_data_change_object_type.csv";

    public static final String PATH_RESOURCES_LOADER_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "vault_loader";
    public static final String PATH_LOAD_DOCUMENTS_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_DOCUMENTS_CSV;
    public static final String PATH_LOAD_OBJECT_RECORDS_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_OBJECT_RECORDS_CSV;
    public static final String PATH_LOAD_CREATE_ATTACHMENTS_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_CREATE_ATTACHMENTS_CSV;
    public static final String PATH_LOAD_DELETE_ATTACHMENTS_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_DELETE_ATTACHMENTS_CSV;
    public static final String PATH_LOAD_ASSIGN_ROLES_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_ASSIGN_ROLES_CSV;
    public static final String PATH_LOAD_REMOVE_ROLES_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_REMOVE_ROLES_CSV;
    public static final String PATH_LOAD_CHANGE_OBJECT_TYPE_CSV = PATH_RESOURCES_LOADER_FOLDER + File.separator + NAME_LOAD_CHANGE_OBJECT_TYPE_CSV;
    public static final String PATH_RETRIEVE_EXTRACT_TEXT_RESULTS_FILE = PATH_RESOURCES_LOADER_FOLDER + File.separator + "retrieve_extract_text_results.txt";


    public static QueryResponse queryForDocId(VaultClient vaultClient) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id, major_version_number__v, minor_version_number__v ");
        query.append("FROM documents ");
        query.append("WHERE binder__v = false ");
        query.append("ORDER BY id ASC ");
        query.append("MAXROWS 1");

        QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                .query(query.toString());

        assertFalse(queryResponse.isFailure());
        return queryResponse;
    }

    public static void writeToLoadCreateAttachmentsFile(List<String> recordIds) {
        FileHelper.createFile(PATH_LOAD_CREATE_ATTACHMENTS_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "file", "filename__v"});

        for (String recordId : recordIds) {
            updateData.add(new String[]{recordId, FileStagingHelper.NAME_FILE_STAGING_TEST_PDF, FileStagingHelper.NAME_FILE_STAGING_TEST_PDF});
        }

        FileHelper.writeCsvFile(PATH_LOAD_CREATE_ATTACHMENTS_CSV, updateData);
    }

    public static void writeToLoadDeleteAttachmentsFile(List<String> recordIds, List<Integer> attachmentIds) {
        FileHelper.createFile(PATH_LOAD_DELETE_ATTACHMENTS_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "attachment_id"});

        for (int i = 0; i < recordIds.size(); i++) {
            updateData.add(new String[]{recordIds.get(i), String.valueOf(attachmentIds.get(i))});
        }

        FileHelper.writeCsvFile(PATH_LOAD_DELETE_ATTACHMENTS_CSV, updateData);
    }

    public static void writeToLoadAssignRolesFile(List<String> recordIds, String userId) {
        FileHelper.createFile(PATH_LOAD_ASSIGN_ROLES_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "viewer__v.users"});

        for (int i = 0; i < recordIds.size(); i++) {
            updateData.add(new String[]{recordIds.get(i), userId});
        }

        FileHelper.writeCsvFile(PATH_LOAD_ASSIGN_ROLES_CSV, updateData);
    }

    public static void writeToLoadRemoveRolesFile(List<String> recordIds, String userId) {
        FileHelper.createFile(PATH_LOAD_REMOVE_ROLES_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "viewer__v.users"});

        for (int i = 0; i < recordIds.size(); i++) {
            updateData.add(new String[]{recordIds.get(i), userId});
        }

        FileHelper.writeCsvFile(PATH_LOAD_REMOVE_ROLES_CSV, updateData);
    }

    public static void writeToLoadChangeObjectTypeFile(List<String> recordIds, String objectTypeId) {
        FileHelper.createFile(PATH_LOAD_CHANGE_OBJECT_TYPE_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "object_type__v"});

        for (int i = 0; i < recordIds.size(); i++) {
            updateData.add(new String[]{recordIds.get(i), objectTypeId});
        }

        FileHelper.writeCsvFile(PATH_LOAD_CHANGE_OBJECT_TYPE_CSV, updateData);
    }
}
