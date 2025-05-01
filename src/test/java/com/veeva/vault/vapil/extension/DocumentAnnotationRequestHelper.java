package com.veeva.vault.vapil.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.request.DocumentAnnotationRequest;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentAnnotationRequestHelper {

    static final String CREATE_ANNOTATIONS_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "create_multiple_annotations.json";
    static final String UPDATE_ANNOTATIONS_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "update_annotations.json";
    static final String DELETE_ANNOTATIONS_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "delete_annotations.csv";
    static final String ADD_REPLIES_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "add_annotation_replies.json";
    static final String ANNOTATED_DOC_FILE_PATH = FileHelper.getPathResourcesFolder() + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "VAPIL Annotations Doc (Do Not Delete).pdf";

    public static String getCreateAnnotationsFilePath() {
        return CREATE_ANNOTATIONS_FILE_PATH;
    }
    public static String getUpdateAnnotationsFilePath() {
        return UPDATE_ANNOTATIONS_FILE_PATH;
    }
    public static String getAnnotatedDocFilePath() {
        return ANNOTATED_DOC_FILE_PATH;
    }
    public static String getDeleteAnnotationsFilePath() {
        return DELETE_ANNOTATIONS_FILE_PATH;
    }

    public static String getAddRepliesFilePath() {
        return ADD_REPLIES_FILE_PATH;
    }


    public static QueryResponse.QueryResult getAnnotationsDoc(VaultClient vaultClient) {
//        Get VAPIL Annotations Doc
        String query = "SELECT id,\n" +
                "name__v,\n" +
                "major_version_number__v,\n" +
                "minor_version_number__v\n" +
                "FROM documents\n" +
                "WHERE name__v LIKE '\n" +
                "VAPIL Annotations Doc%'";

        QueryResponse response = vaultClient.newRequest(QueryRequest.class)
                .query(query);

        return response.getData().get(0);
    }

    public static void writeCreateAnnotationsFile(VaultClient vaultClient) throws IOException {
        QueryResponse.QueryResult annotationsDoc = getAnnotationsDoc(vaultClient);
        int docId = annotationsDoc.getInteger("id");
        int majorVersionNumber = annotationsDoc.getInteger("major_version_number__v");
        int minorVersionNumber = annotationsDoc.getInteger("minor_version_number__v");
        String docVersionId = docId + "_" + majorVersionNumber + "_" + minorVersionNumber;

        FileHelper.createFile(CREATE_ANNOTATIONS_FILE_PATH);
        File createAnnotationsFile = new File(CREATE_ANNOTATIONS_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();
        ObjectNode annotationNode = mapper.createObjectNode();
        annotationNode.put("type__sys", "note__sys");
        annotationNode.put("document_version_id__sys", docVersionId);
        annotationNode.put("color__sys", "orange_dark__sys");
        annotationNode.put("comment__sys", "VAPIL Test");
        ObjectNode placemarkNode = mapper.createObjectNode();
        placemarkNode.put("type__sys", "sticky__sys");
        placemarkNode.put("page_number__sys", 1);
        placemarkNode.put("x_coordinate__sys", 100);
        placemarkNode.put("y_coordinate__sys", 100);
        annotationNode.set("placemark", placemarkNode);
        rootNode.add(annotationNode);
        mapper.writerWithDefaultPrettyPrinter().writeValue(createAnnotationsFile, rootNode);
    }

    public static void writeAddRepliesFile(VaultClient vaultClient, List<String> docIds, List<String> annotationIds) throws IOException {
        FileHelper.createFile(ADD_REPLIES_FILE_PATH);
        File addRepliesFile = new File(ADD_REPLIES_FILE_PATH);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();
        int size = docIds.size();

        for (int i = 0; i < size; i++) {
            ObjectNode annotationNode = mapper.createObjectNode();
            annotationNode.put("type__sys", "reply__sys");
            annotationNode.put("document_version_id__sys", docIds.get(i));
            annotationNode.put("comment__sys", "VAPIL test annotation reply");

            ObjectNode placemarkNode = mapper.createObjectNode();
            placemarkNode.put("type__sys", "reply__sys");
            placemarkNode.put("reply_parent__sys", annotationIds.get(i));
            annotationNode.set("placemark", placemarkNode);
            rootNode.add(annotationNode);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(addRepliesFile, rootNode);
    }

    public static void writeUpdateAnnotationsFile(VaultClient vaultClient, List<String> docIds, List<String> annotationIds) throws IOException {
        FileHelper.createFile(UPDATE_ANNOTATIONS_FILE_PATH);
        File updateAnnotationsFile = new File(UPDATE_ANNOTATIONS_FILE_PATH);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();
        int size = docIds.size();

        for (int i = 0; i < size; i++) {
            ObjectNode annotationNode = mapper.createObjectNode();
            annotationNode.put("id__sys", annotationIds.get(i));
            annotationNode.put("document_version_id__sys", docIds.get(i));
            annotationNode.put("comment__sys", "VAPIL test update annotation");
            rootNode.add(annotationNode);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(updateAnnotationsFile, rootNode);
    }

    public static DocumentAnnotationBulkResponse createMultipleAnnotations(VaultClient vaultClient) throws IOException {
        writeCreateAnnotationsFile(vaultClient);

        DocumentAnnotationBulkResponse createMultipleAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                .setInputPath(CREATE_ANNOTATIONS_FILE_PATH)
                .createMultipleAnnotations();

        assertNotNull(createMultipleAnnotationsResponse);
        assertTrue(createMultipleAnnotationsResponse.isSuccessful());
        return createMultipleAnnotationsResponse;
    }

    public static DocumentAnnotationBulkResponse deleteAnnotations(VaultClient vaultClient, List<String> docIds, List<String> annotationIds) {
        FileHelper.createFile(DELETE_ANNOTATIONS_FILE_PATH);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id__sys", "document_version_id__sys"});
        int size = docIds.size();
        for (int i = 0; i < size; i++) {
            data.add(new String[]{annotationIds.get(i), docIds.get(i)});
        }

        FileHelper.writeCsvFile(DELETE_ANNOTATIONS_FILE_PATH, data);
        DocumentAnnotationBulkResponse deleteAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                .setContentTypeCsv()
                .setInputPath(DELETE_ANNOTATIONS_FILE_PATH)
                .deleteAnnotations();

        return deleteAnnotationsResponse;
    }
}
