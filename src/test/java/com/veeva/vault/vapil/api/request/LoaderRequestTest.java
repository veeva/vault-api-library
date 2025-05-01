package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.builder.LoaderTaskBuilder;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag("LoaderRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Loader Request should")
public class LoaderRequestTest {

    static final int MAJOR_VERSION = 0;
    static final int MINOR_VERSION = 1;
    static final String DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    static final String DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    static final String DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    static final String DOC_LIFECYCLE = "VAPIL Test Doc Lifecycle";
    static final String FILE_STAGING_FILE = FileStagingHelper.getPathFileStagingLoaderFilePath();
    static final String LOADER_FILE_CSV_PATH = FileHelper.getPathLoaderFile();
    static LoaderTask loaderTask;
    static int loadJobId;
    static int extractJobId;
    static List<Integer> loadTasks = new ArrayList<>();
    static List<Integer> extractTasks = new ArrayList<>();
    static List<Integer> docIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) throws IOException {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"file", "name__v", "type__v", "subtype__v",
                "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
        for (int i = 0; i < 3; i++) {
            String name = "VAPIL Loader " + ZonedDateTime.now() + " " + i;
            data.add(new String[]{FILE_STAGING_FILE, name, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                    DOC_LIFECYCLE, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
        }
        FileHelper.writeCsvFile(LOADER_FILE_CSV_PATH, data);

        FileStagingHelper.createLoaderFileOnFileStaging(vaultClient);
    }

    @AfterAll
    static void teardown() {
        DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
        Assertions.assertTrue(response.isSuccessful());
        for (DocumentResponse documentResponse : response.getData()) {
            Assertions.assertTrue(documentResponse.isSuccessful());
        }
    }

    @Test
    @Order(1)
    @DisplayName("successfully create a loader job and load a set of data files")
    public void testLoadDataObjects() throws InterruptedException {
        String jsonString = "[\n" +
                "  {\n" +
                "    \"object_type\": \"documents__v\",\n" +
                "    \"action\": \"create\",\n" +
                "    \"file\": \"loader_file.csv\",\n" +
                "    \"order\": 1\n" +
                "  }\n" +
                "]";

        LoaderResponse loadResponse = vaultClient.newRequest(LoaderRequest.class)
                .setJson(jsonString)
                .loadDataObjects();

        Assertions.assertTrue(loadResponse.isSuccessful());
        Assertions.assertNotNull(loadResponse.getJobId());
        loadJobId = loadResponse.getJobId();
        Assertions.assertNotNull(loadResponse.getUrl());
        Assertions.assertNotNull(loadResponse.getTasks());
        for (LoaderTask task : loadResponse.getTasks()) {
            Assertions.assertNotNull(task.getTaskId());
            loadTasks.add(task.getTaskId());
        }
        Thread.sleep(5000);
    }

    @Test
    @Order(2)
    @DisplayName("successfully retrieve success logs of the loader results")
    public void testRetrieveLoadSuccessLogResults() {
        for (Integer taskId : loadTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoadSuccessLogResults(loadJobId, taskId);

            Assertions.assertTrue(resultResponse.isSuccessful());
            Assertions.assertNotNull(resultResponse.getBinaryContent());
            byte[] byteArray = resultResponse.getBinaryContent();
            String responseString = new String(byteArray, StandardCharsets.UTF_8);
            extractDocIds(responseString);
        }
    }


    @Test
    @Order(3)
    @DisplayName("successfully build a loader task")
    public void testBuild() {
        LoaderTaskBuilder taskBuilder = new LoaderTaskBuilder()
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_RENDITIONS)
                .addExtractOption(LoaderTaskBuilder.ExtractOption.INCLUDE_SOURCE)
                .setObjectType(LoaderTaskBuilder.ObjectType.DOCUMENTS)
                .addField("id")
                .addField("name__v")
                .appendWhere("name__v != 'X'");
        loaderTask = taskBuilder.build();

        Assertions.assertNotNull(loaderTask);
        Assertions.assertNotNull(loaderTask.getFields());
        Assertions.assertEquals(2,loaderTask.getFields().size());
        Assertions.assertNotNull(loaderTask.getVqlCriteria());
        Assertions.assertNotEquals("",loaderTask.getVqlCriteria());
    }

    @Test
    @Order(4)
    @DisplayName("successfully create and run a loader job to extract data")
    public void testExtractDataFiles() throws Exception {
        LoaderResponse extractResponse = vaultClient.newRequest(LoaderRequest.class)
                .addLoaderTask(loaderTask)
                .extractDataFiles();

        Assertions.assertTrue(extractResponse.isSuccessful());
        Assertions.assertNotNull(extractResponse.getJobId());
        extractJobId = extractResponse.getJobId();
        Assertions.assertNotNull(extractResponse.getUrl());
        Assertions.assertNotNull(extractResponse.getTasks());
        for (LoaderTask task : extractResponse.getTasks()) {
            Assertions.assertNotNull(task.getTaskId());
            extractTasks.add(task.getTaskId());
        }
        Thread.sleep(5000);
    }

    @Test
    @Order(5)
    @DisplayName("successfully retrieve results of a specified job task")
    public void testRetrieveLoaderExtractResults() {
        for (Integer taskId : extractTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractResults(extractJobId, taskId);
            Assertions.assertNotNull(resultResponse);
            Assertions.assertNotNull(resultResponse.getBinaryContent());
        }
    }

    @Test
    @Order(6)
    @DisplayName("successfully retrieve results of a specified job task that includes renditions requested with documents.")
    public void testRetrieveLoaderExtractRenditionsResults() {
        for (Integer taskId : extractTasks) {
            VaultResponse resultResponse = vaultClient.newRequest(LoaderRequest.class)
                    .retrieveLoaderExtractRenditionsResults(extractJobId, taskId);
            Assertions.assertNotNull(resultResponse);
            Assertions.assertNotNull(resultResponse.getBinaryContent());
        }
    }

    @Test
    @Disabled("Needs Further setup/Eval")
    public void testExtract() throws Exception {
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
                                    .retrieveLoaderExtractResults(jobId, taskId);
                            System.out.println(new String(resultResponse.getBinaryContent()));

                            VaultResponse renditionResponse = vaultClient.newRequest(LoaderRequest.class)
                                    .retrieveLoaderExtractRenditionsResults(jobId, taskId);
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
    public void testLoad() throws Exception {
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

    public void extractDocIds(String csvResponse) {
        String[] lines = csvResponse.split("\n");
        // Skip the header line
        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].split(",");

            if (columns.length >= 2) {
                String docId = columns[1];
                docIds.add(Integer.valueOf(docId));
            }
        }
    }
}
