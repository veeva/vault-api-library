package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.FileStagingItemResponse;
import com.veeva.vault.vapil.api.request.FileStagingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileStagingHelper {

    private static Logger log = LoggerFactory.getLogger(FileStagingHelper.class);
    static final String FILE_STAGING_LOADER_FOLDER = "/loader";
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
    public static String getPathFileStagingLoaderFolder() { return FILE_STAGING_LOADER_FOLDER; }
    public static void createFileOnFileStaging(
            VaultClient vaultClient,
            File file,
            String loaderPath,
            boolean overwrite
    ) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Error reading file: " + TEST_FILE_LOCAL_PATH);
            e.printStackTrace();
        }

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(overwrite)
                .setFile(file.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, loaderPath);

        Assertions.assertTrue(fileStagingResponse.isSuccessful());
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
