package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobRequestHelper {

    public static final String VAPIL_TEST_JOB_NAME = "vapil_test_job__c";
    public static final String VAPIL_TEST_JOB_LABEL = "VAPIL Test Job";

    public static final String PATH_RESOURCES_JOBS_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "jobs";
    public static final String PATH_CANCEL_JOB_JSON_FILE = PATH_RESOURCES_JOBS_FOLDER + File.separator + "cancel_jobs.json";


    public static void writeToCancelJobFile(List<Integer> jobIds) throws IOException {
        FileHelper.createFile(PATH_CANCEL_JOB_JSON_FILE);
        File cancelJobFile = new File(PATH_CANCEL_JOB_JSON_FILE);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();
        int size = jobIds.size();

        for (int i = 0; i < size; i++) {
            ObjectNode jobNode = mapper.createObjectNode();
            jobNode.put("job_id", jobIds.get(i));
            rootNode.add(jobNode);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(cancelJobFile, rootNode);
    }
}
