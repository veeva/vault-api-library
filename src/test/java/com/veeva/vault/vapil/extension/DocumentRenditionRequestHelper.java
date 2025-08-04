package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentRenditionRequestHelper {

    public static final String PATH_RESOURCES_DOCUMENT_RENDITIONS_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_renditions";
    public static final String PATH_ADD_MULTIPLE_RENDITIONS_CSV = PATH_RESOURCES_DOCUMENT_RENDITIONS_FOLDER + File.separator + "add_multiple_document_renditions.csv";
    public static final String PATH_UPDATE_MULTIPLE_RENDITIONS_CSV = PATH_RESOURCES_DOCUMENT_RENDITIONS_FOLDER + File.separator + "update_multiple_document_renditions.csv";

    public static void writeToAddMultipleRenditionsFile(VaultClient vaultClient, int docId) {
        FileStagingHelper.createTestFileOnFileStaging(vaultClient);
        FileHelper.createFile(PATH_ADD_MULTIPLE_RENDITIONS_CSV);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{
                "file",
                "id",
                "rendition_type__v",
                "major_version_number__v",
                "minor_version_number__v"
        });

        data.add(new String[]{
                FileStagingHelper.PATH_FILE_STAGING_TEST_PDF,
                String.valueOf(docId),
                "viewable_rendition__v",
                String.valueOf(0),
                String.valueOf(1)
        });

        FileHelper.writeCsvFile(PATH_ADD_MULTIPLE_RENDITIONS_CSV, data);
    }
}

