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

public class DocumentRequestHelper {

    public static final String VAPIL_TEST_DOC_LIFECYCLE_LABEL = "VAPIL Test Doc Lifecycle";
    public static final String VAPIL_TEST_DOC_LIFECYCLE_NAME = "vapil_test_doc_lifecycle__c";
    public static final String VAPIL_TEST_DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    public static final String VAPIL_TEST_DOC_TYPE_NAME = "vapil_test_doc_type__c";
    public static final String VAPIL_TEST_DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    public static final String VAPIL_TEST_DOC_SUBTYPE_NAME = "vapil_test_doc_subtype__c";
    public static final String VAPIL_TEST_DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    public static final String VAPIL_TEST_DOC_CLASSIFICATION_NAME = "vapil_test_doc_classification__c";
    public static final String VAPIL_TEST_RECLASSIFY_TYPE_NAME = "vapil_test_reclassify_type__c";
    public static final String VAPIL_TEST_RECLASSIFY_TYPE_LABEL = "VAPIL Test Reclassify Type";

    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 1;
    private static final String PATH_FILE_STAGING_TEST_PDF = FileStagingHelper.PATH_FILE_STAGING_TEST_PDF;

    public static final String PATH_RESOURCES_DOCUMENTS_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents";
    public static final String PATH_CREATE_MULTIPLE_DOCUMENTS_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "create_multiple_documents.csv";
    public static final String PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "update_multiple_documents.csv";
    public static final String PATH_DELETE_MULTIPLE_DOCUMENTS_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "delete_multiple_documents.csv";
    public static final String PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "create_multiple_document_versions.csv";
    public static final String PATH_RECLASSIFY_MULTIPLE_DOCUMENTS_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "reclassify_multiple_documents.csv";
    public static final String PATH_UNDO_COLLAB_CHECKOUT_CSV = PATH_RESOURCES_DOCUMENTS_FOLDER + File.separator + "undo_collab_checkout.csv";


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

    public static void writeToCreateMultipleDocumentsFile(int numOfDocuments) {
        FileHelper.createFile(PATH_CREATE_MULTIPLE_DOCUMENTS_CSV);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"file", "name__v", "description__c", "type__v", "subtype__v",
                "classification__v", "lifecycle__v", "major_version__v", "minor_version__v"});
        for (int i = 0; i < numOfDocuments; i++) {
            String name = "VAPIL Test Create Multiple Documents " + ZonedDateTime.now() + " " + i;
            String description = "VAPIL Test";
            data.add(new String[]{PATH_FILE_STAGING_TEST_PDF, name, description, VAPIL_TEST_DOC_TYPE_LABEL, VAPIL_TEST_DOC_SUBTYPE_LABEL, VAPIL_TEST_DOC_CLASSIFICATION_LABEL,
                    VAPIL_TEST_DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION), String.valueOf(MINOR_VERSION)});
        }

        FileHelper.writeCsvFile(PATH_CREATE_MULTIPLE_DOCUMENTS_CSV, data);
    }

    public static void writeToUpdateMultipleDocumentsFile(List<Integer> docIds) {
        FileHelper.createFile(PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV);
        String updatedTitle = "VAPIL Test Update multiple documents";

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "title__v"});

        for (int docId : docIds) {
            updateData.add(new String[]{String.valueOf(docId), updatedTitle});
        }

        FileHelper.writeCsvFile(PATH_UPDATE_MULTIPLE_DOCUMENTS_CSV, updateData);
    }

    public static void writeToDeleteMultipleDocumentsFile(List<Integer> docIds) {
        FileHelper.createFile(PATH_DELETE_MULTIPLE_DOCUMENTS_CSV);

        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id"});

        for (int docId : docIds) {
            updateData.add(new String[]{String.valueOf(docId)});
        }

        FileHelper.writeCsvFile(PATH_DELETE_MULTIPLE_DOCUMENTS_CSV, updateData);
    }

    public static void writeToReclassifyMultipleDocumentsFile(List<Integer> docIds) {
        List<String[]> reclassifyData = new ArrayList<>();
        reclassifyData.add(new String[]{"id", "lifecycle__v", "type__v"});

        for (int docId : docIds) {
            reclassifyData.add(new String[]{String.valueOf(docId), VAPIL_TEST_DOC_LIFECYCLE_NAME, VAPIL_TEST_RECLASSIFY_TYPE_NAME});
        }

        FileHelper.writeCsvFile(PATH_RECLASSIFY_MULTIPLE_DOCUMENTS_CSV, reclassifyData);
    }

    public static void writeToUpdateMultipleDocumentVersionsFile(int docId) {
        FileHelper.createFile(PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"file", "id", "name__v", "type__v", "subtype__v",
                "classification__v", "lifecycle__v", "major_version_number__v", "minor_version_number__v", "status__v"});

        for (int i=0; i<2 ; i++) {
            String name = "VAPIL Test Create Multiple Document Versions " + i;
            data.add(new String[]{
                    PATH_FILE_STAGING_TEST_PDF, String.valueOf(docId),
                    name, VAPIL_TEST_DOC_TYPE_LABEL,
                    VAPIL_TEST_DOC_SUBTYPE_LABEL, VAPIL_TEST_DOC_CLASSIFICATION_LABEL,
                    VAPIL_TEST_DOC_LIFECYCLE_LABEL, String.valueOf(MAJOR_VERSION),
                    String.valueOf(i+2), "Draft"});
        }

        FileHelper.writeCsvFile(PATH_CREATE_MULTIPLE_DOCUMENT_VERSIONS_CSV, data);
    }

    public static DocumentBulkResponse createMultipleDocuments(VaultClient vaultClient, int numOfDocuments) {
        FileStagingHelper.createTestFileOnFileStaging(vaultClient);
        writeToCreateMultipleDocumentsFile(numOfDocuments);

        DocumentBulkResponse createResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(PATH_CREATE_MULTIPLE_DOCUMENTS_CSV)
                .createMultipleDocuments();

        assertTrue(createResponse.isSuccessful());

        return createResponse;
    }

    public static DocumentBulkResponse deleteDocuments(VaultClient vaultClient, List<Integer> docIds) {
        writeToDeleteMultipleDocumentsFile(docIds);

        DocumentBulkResponse deleteDocumentsResponse = vaultClient.newRequest(DocumentRequest.class)
                .setInputPath(PATH_DELETE_MULTIPLE_DOCUMENTS_CSV)
                .deleteMultipleDocuments();

        return deleteDocumentsResponse;
    }
}
