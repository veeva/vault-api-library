package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.builder.LoaderTaskBuilder;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(VaultClientParameterResolver.class)
public class LoaderRequestTest {

    static final String RESOURCES_FOLDER_PATH = "src\\test\\resources\\";

    @Test
    public void testBuilder() {
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'");
        LoaderTask builtTask = taskBuilder.build();

        Assertions.assertNotNull(builtTask);
        Assertions.assertNotNull(builtTask.getFields());
        Assertions.assertEquals(2,builtTask.getFields().size());
        Assertions.assertNotNull(builtTask.getVqlCriteria());
        Assertions.assertNotEquals("",builtTask.getVqlCriteria());
        //System.out.println(taskBuilder.build().toJsonString());
    }

    @Test
    @DisplayName("Should successfully create and run a loader job to extract data")
    public void testExtractDataFiles(VaultClient vaultClient) throws Exception {
//		Create file on file staging server
        File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
        byte[] bytes = Files.readAllBytes(testFile.toPath());

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(true)
                .setFile(testFile.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, "test_create_file.txt");
        Assertions.assertTrue(fileStagingResponse.isSuccessful());

//		Create multiple documents
        String csvFilePath = RESOURCES_FOLDER_PATH + "test_create_multiple_documents.csv";
        DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(csvFilePath)
                .createMultipleDocuments();
        Assertions.assertTrue(createResponse.isSuccessful());
        for (DocumentResponse documentResponse : createResponse.getData()) {
            Assertions.assertTrue(documentResponse.isSuccessful());
        }

//        Build Loader Task
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'")
                .setMaxRows(3)
                .setSkip(1);
        LoaderTask builtTask = taskBuilder.build();

//        Run extract job
        LoaderResponse extractResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(builtTask)
                .extractDataFiles();

        Assertions.assertTrue(extractResponse.isSuccessful());
    }

    @Test
    @Disabled("Needs Further setup/Eval")
    public void testExtract(VaultClient vaultClient) throws Exception {
        //There are dependencies so an autonomous approach will require duplication.
        Integer jobId = null;
        List<Integer> taskIds = new ArrayList<>();

        jobId = 46809;
        taskIds.add(1);

        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'");

        System.out.println(taskBuilder.build().toMap());

        LoaderResponse extractResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(taskBuilder.build())
                .extractDataFiles();


        System.out.println(extractResponse.getResponse());

        if (extractResponse.isSuccessful()) {
            jobId = extractResponse.getJobId();
            if (extractResponse.getTasks() != null) {
                for (LoaderTask tasks : extractResponse.getTasks()) {
                    taskIds.add(tasks.getTaskId());
                }
            }
        }

        if (jobId != null) {
            boolean retry = true;
            while (retry) {


                JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);
                if ((jobStatusResponse != null)
                        && (!jobStatusResponse.hasErrors())
                        && (jobStatusResponse.getData() != null)) {

                    Job job = jobStatusResponse.getData();
                    if (job.getRunEndDate() != null) {
                        retry = false;

                        for (Integer taskId : taskIds) {
                            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveExtractResults(jobId, taskId);
                            System.out.println(new String(resultResponse.getBinaryContent()));

                            VaultResponse renditionResponse = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveExtractRenditionResults(jobId, taskId);
                            System.out.println(new String(renditionResponse.getBinaryContent()));
                        }
                    } else {
                        //NOTE: MUST WAIT 30 SECONDS!
                        //{"responseStatus":"FAILURE","errors":[{"type":"API_LIMIT_EXCEEDED","message":"Too many polling requests"}]}
                        System.out.println("wait 30 seconds - job not complete");
                        Thread.sleep(30000);
                    }
                }
            }
        }
    }

    @Test
    @Disabled("Needs Further setup/Eval")
    public void testLoad(VaultClient vaultClient) throws Exception {
        //There are dependencies so an autonomous approach will require duplication.
        Integer jobId = null;
        List<Integer> taskIds = new ArrayList<>();

        //NOTE: be sure to put a csv file in the FTPS folder at /vapil/products.csv
        //include all required fields
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .setAction(LoaderTaskBuilder.Action.CREATE)
                .setFile("/vapil/products.csv")
                .setObjectType(LoaderTaskBuilder.ObjectType.OBJECTS)
                .setObject("product__v");

        LoaderResponse loadResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(taskBuilder.build())
                .loadDataObjects();

        System.out.println(loadResponse.getResponse());

        if (loadResponse.isSuccessful()) {
            jobId = loadResponse.getJobId();
            if (loadResponse.getTasks() != null) {
                for (LoaderTask tasks : loadResponse.getTasks()) {
                    taskIds.add(tasks.getTaskId());
                }
            }
        }

        if (jobId != null) {
            boolean retry = true;
            while (retry) {
                JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class).retrieveJobStatus(jobId);
                if ((jobStatusResponse != null)
                        && (!jobStatusResponse.hasErrors())
                        && (jobStatusResponse.getData() != null)) {

                    Job job = jobStatusResponse.getData();
                    if (job.getRunEndDate() != null) {
                        retry = false;

                        for (Integer taskId : taskIds) {
                            VaultResponse successLogResults = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoadSuccessLogResults(jobId, taskId);
                            System.out.println(new String(successLogResults.getBinaryContent()));

                            VaultResponse failureLogResults = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoadFailureLogResults(jobId, taskId);
                            System.out.println(new String(failureLogResults.getBinaryContent()));
                        }
                    }
                    else {
                        //NOTE: MUST WAIT 30 SECONDS!
                        //{"responseStatus":"FAILURE","errors":[{"type":"API_LIMIT_EXCEEDED","message":"Too many polling requests"}]}
                        System.out.println("wait 30 seconds - job not complete");
                        Thread.sleep(30000);
                    }
                }
            }
        }
    }
}
