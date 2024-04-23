package com.veeva.vault.vapil.extension;

import com.opencsv.CSVWriter;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectRecordBulkResponse;
import com.veeva.vault.vapil.api.request.ObjectRecordRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObjectRecordRequestHelper {

    static final String OBJECT_NAME = "vapil_test_object__c";
    static final String CREATE_OBJECTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "object_records" + File.separator + "create_object_records.csv";
    static final String UPDATE_OBJECTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "object_records" + File.separator + "update_object_records.csv";
    static final String DELETE_OBJECTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "object_records" + File.separator + "delete_object_records.csv";
    static final String CREATE_SINGLE_OBJECT_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "object_records" + File.separator + "create_single_object_record.csv";

    public static String getPathCreateObjectRecordsCsv() {
        FileHelper.createCsvFile(CREATE_OBJECTS_CSV_PATH);
        return CREATE_OBJECTS_CSV_PATH;
    }

    public static String getPathUpdateObjectRecordsCsv() {
        FileHelper.createCsvFile(UPDATE_OBJECTS_CSV_PATH);
        return UPDATE_OBJECTS_CSV_PATH;
    }

    public static String getPathDeleteObjectRecordsCsv() {
        FileHelper.createCsvFile(DELETE_OBJECTS_CSV_PATH);
        return DELETE_OBJECTS_CSV_PATH;
    }

    public static String getPathCreateSingleObjectRecordCsv() {
        FileHelper.createCsvFile(CREATE_SINGLE_OBJECT_CSV_PATH);
        return CREATE_SINGLE_OBJECT_CSV_PATH;
    }
    public static ObjectRecordBulkResponse createObjectRecords(VaultClient vaultClient) throws IOException {
//        Create CSV File
        FileHelper.createCsvFile(CREATE_OBJECTS_CSV_PATH);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"name__v", "description__c"});
        for (int i = 0; i < 5; i++) {
            String name = "VAPIL Test Create Object " + ZonedDateTime.now() + " " + i;
            String description = "VAPIL Test";
            data.add(new String[]{name, description});
        }

        FileHelper.writeCsvFile(CREATE_OBJECTS_CSV_PATH, data);

//		Create Objects
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(CREATE_OBJECTS_CSV_PATH)
                .createObjectRecords(OBJECT_NAME);

        return response;
    }

    public static ObjectRecordBulkResponse deleteObjectRecords(VaultClient vaultClient, List<String> recordIds) throws IOException {
        FileHelper.createCsvFile(DELETE_OBJECTS_CSV_PATH);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id"});
        for (String recordId : recordIds) {
            data.add(new String[]{recordId});
        }

        FileHelper.writeCsvFile(DELETE_OBJECTS_CSV_PATH, data);

//		Delete Objects
        ObjectRecordBulkResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
                .setContentTypeCsv()
                .setInputPath(DELETE_OBJECTS_CSV_PATH)
                .deleteObjectRecords(OBJECT_NAME);

        return response;
    }
}
