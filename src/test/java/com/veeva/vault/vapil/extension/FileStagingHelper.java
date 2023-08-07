package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.FileStagingItemResponse;
import com.veeva.vault.vapil.api.request.FileStagingRequest;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileStagingHelper {

    private static Logger log = Logger.getLogger(FileStagingHelper.class);
    static final String TEST_FILE_LOCAL_PATH = FileHelper.getPathTestFile();
    static final String LOADER_FILE_LOCAL_PATH = FileHelper.getPathLoaderFile();
    static final String TEST_FILE_FSS_PATH = "/vapil_test_document.docx";
    static final String LOADER_FILE_FSS_PATH = "/loader_file.csv";

    public static String getPathFileStagingTestFilePath() {
        return TEST_FILE_FSS_PATH;
    }
    public static String getPathFileStagingLoaderFilePath() {
        return LOADER_FILE_FSS_PATH;
    }
    public static void createTestFileOnFileStaging(VaultClient vaultClient) {
        File testFile = new File(TEST_FILE_LOCAL_PATH);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(testFile.toPath());
        } catch (IOException e) {
            log.error("Error reading file: " + TEST_FILE_LOCAL_PATH);
            e.printStackTrace();
        }

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(true)
                .setFile(testFile.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, "vapil_test_document.docx");
        Assertions.assertTrue(fileStagingResponse.isSuccessful());
    }

    public static void createLoaderFileOnFileStaging(VaultClient vaultClient) {
        File testFile = new File(LOADER_FILE_LOCAL_PATH);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(testFile.toPath());
        } catch (IOException e) {
            log.error("Error reading file: " + LOADER_FILE_LOCAL_PATH);
            e.printStackTrace();
        }

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(true)
                .setFile(testFile.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, "loader_file.csv");
        Assertions.assertTrue(fileStagingResponse.isSuccessful());
    }
}
