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

    static final String PATH_LOCAL_TEST_FILE = FileHelper.PATH_LOCAL_TEST_FILE;
    public static final String PATH_FILE_STAGING_LOADER_FOLDER = "/vault_loader";
    public static final String NAME_FILE_STAGING_TEST_PDF = "vapil_test_document.pdf";
    public static final String PATH_FILE_STAGING_TEST_PDF = String.format("/%s", NAME_FILE_STAGING_TEST_PDF);

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
            log.error("Error reading file: " + PATH_LOCAL_TEST_FILE);
            e.printStackTrace();
        }

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(overwrite)
                .setFile(file.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, loaderPath);

        Assertions.assertTrue(fileStagingResponse.isSuccessful());
    }

    public static void createTestFileOnFileStaging(VaultClient vaultClient) {
        File testFile = new File(PATH_LOCAL_TEST_FILE);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(testFile.toPath());
        } catch (IOException e) {
            log.error("Error reading file: " + PATH_LOCAL_TEST_FILE);
            e.printStackTrace();
        }

        FileStagingItemResponse fileStagingResponse = vaultClient.newRequest(FileStagingRequest.class)
                .setOverwrite(true)
                .setFile(testFile.getPath(), bytes)
                .createFolderOrFile(FileStagingRequest.Kind.FILE, "vapil_test_document.pdf");
        Assertions.assertTrue(fileStagingResponse.isSuccessful());
    }
}
