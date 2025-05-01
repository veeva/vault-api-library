package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.DocumentBulkResponse;
import com.veeva.vault.vapil.api.model.response.DocumentResponse;
import com.veeva.vault.vapil.api.request.DocumentRequest;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentRequestHelper {

    static final int MAJOR_VERSION = 0;
    static final int MINOR_VERSION = 1;
    static final String DOC_LIFECYCLE = "VAPIL Test Doc Lifecycle";
    static final String DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    static final String DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    static final String DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    static final String FILE_STAGING_FILE = FileStagingHelper.getPathFileStagingTestFilePath();
    static final String CREATE_DOCUMENTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "documents" + File.separator + "create_multiple_documents.csv";
    static final String UPDATE_DOCUMENTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "documents" + File.separator + "update_multiple_documents.csv";
    static final String DELETE_DOCUMENTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "documents" + File.separator + "delete_multiple_documents.csv";
    static final String RECLASSIFY_DOCUMENTS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "documents" + File.separator + "reclassify_multiple_documents.csv";
    static final String UNDO_COLLAB_CHECKOUT_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "documents" + File.separator + "undo_collab_checkout.csv";

    public static String getPathCreateMultipleDocuments() {
        FileHelper.createFile(CREATE_DOCUMENTS_CSV_PATH);
        return CREATE_DOCUMENTS_CSV_PATH;
    }

    public static String getPathUpdateMultipleDocuments() {
        FileHelper.createFile(UPDATE_DOCUMENTS_CSV_PATH);
        return UPDATE_DOCUMENTS_CSV_PATH;
    }

    public static String getPathDeleteMultipleDocuments() {
        FileHelper.createFile(DELETE_DOCUMENTS_CSV_PATH);
        return DELETE_DOCUMENTS_CSV_PATH;
    }

    public static String getPathReclassifyMultipleDocuments() {
        FileHelper.createFile(RECLASSIFY_DOCUMENTS_CSV_PATH);
        return RECLASSIFY_DOCUMENTS_CSV_PATH;
    }

    public static String getPathUndoCollabCheckout() {
        return UNDO_COLLAB_CHECKOUT_CSV_PATH;
    }

    public static void writeToCreateMultipleDocumentsFile(int numOfDocuments) {
        FileHelper.createFile(CREATE_DOCUMENTS_CSV_PATH);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"file", "name__v", "description__c", "type__v", "subtype__v",
                "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
        for (int i = 0; i < numOfDocuments; i++) {
            String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
            String description = "VAPIL Test";
            data.add(new String[]{FILE_STAGING_FILE, name, description, DOC_TYPE_LABEL, DOC_SUBTYPE_LABEL, DOC_CLASSIFICATION_LABEL,
                    DOC_LIFECYCLE, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
        }

        FileHelper.writeCsvFile(CREATE_DOCUMENTS_CSV_PATH, data);
    }

    public static DocumentBulkResponse createMultipleDocuments(VaultClient vaultClient, int numOfDocuments) {
        FileStagingHelper.createTestFileOnFileStaging(vaultClient);
        writeToCreateMultipleDocumentsFile(numOfDocuments);

        DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(CREATE_DOCUMENTS_CSV_PATH)
                .createMultipleDocuments();

        assertTrue(createResponse.isSuccessful());

        return createResponse;
    }

    public static DocumentBulkResponse deleteDocuments(VaultClient vaultClient, List<Integer> docIds) {
        FileHelper.createFile(DELETE_DOCUMENTS_CSV_PATH);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id"});
        for (Integer docId : docIds) {
            data.add(new String[]{String.valueOf(docId)});
        }

        FileHelper.writeCsvFile(DELETE_DOCUMENTS_CSV_PATH, data);

        DocumentBulkResponse deleteDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(DELETE_DOCUMENTS_CSV_PATH)
                .deleteMultipleDocuments();

        return deleteDocumentsResponse;
    }
}
