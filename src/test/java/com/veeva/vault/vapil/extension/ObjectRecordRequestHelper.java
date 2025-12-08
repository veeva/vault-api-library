package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.ObjectRecordRequest;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ObjectRecordRequestHelper {

    public static final String OBJECT_NAME = "vapil_test_object__c";
    public static final String OBJECT_NAME_PARENT = "vapil_test_parent_object__c";
    public static final String VAPIL_OBJECT_TYPE_NAME = "vapil_test_type_1_object__c";

    public static final String PATH_RESOURCES_VAULT_OBJECTS_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "vault_objects";
    public static final String PATH_CREATE_OBJECT_RECORDS_CSV = PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "create_object_records.csv";
    public static final String PATH_CREATE_SINGLE_OBJECT_RECORD_CSV = PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "create_single_object_record.csv";
    public static final String PATH_UPDATE_OBJECT_RECORDS_CSV = PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "update_object_records.csv";
    public static final String PATH_DELETE_OBJECT_RECORDS_CSV = PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "delete_object_records.csv";
    public static final String PATH_MERGE_OBJECT_RECORDS_CSV = PATH_RESOURCES_VAULT_OBJECTS_FOLDER + File.separator + "merge_object_records.csv";

    public static QueryResponse queryForRecordId(VaultClient vaultClient) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id ");
        query.append("FROM vapil_test_object__c ");
        query.append("ORDER BY id ASC ");
        query.append("MAXROWS 1");

        QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                .query(query.toString());

        assertFalse(queryResponse.isFailure());
        return queryResponse;
    }

    public static ObjectRecordBulkResponse createMultipleObjectRecords(VaultClient vaultClient, int numOfRecords) throws IOException {
//        Create CSV File
        FileHelper.createFile(PATH_CREATE_OBJECT_RECORDS_CSV);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"name__v", "description__c"});
        for (int i = 0; i < numOfRecords; i++) {
            String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
            String description = "VAPIL Test";
            data.add(new String[]{name, description});
        }

        FileHelper.writeCsvFile(PATH_CREATE_OBJECT_RECORDS_CSV, data);

//		Create Objects
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_CREATE_OBJECT_RECORDS_CSV)
                .createAndUpsertObjectRecords(OBJECT_NAME);


        return response;
    }

    public static ObjectRecordBulkResponse deleteObjectRecords(VaultClient vaultClient, List<String> recordIds) throws IOException {
        FileHelper.createFile(PATH_DELETE_OBJECT_RECORDS_CSV);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id"});
        for (String recordId : recordIds) {
            data.add(new String[]{recordId});
        }

        FileHelper.writeCsvFile(PATH_DELETE_OBJECT_RECORDS_CSV, data);

//		Delete Objects
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_DELETE_OBJECT_RECORDS_CSV)
                .deleteObjectRecords(OBJECT_NAME);

        return response;
    }
}
