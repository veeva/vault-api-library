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

    public static final String PATH_CREATE_ANNOTATIONS_JSON = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "create_multiple_annotations.json";
    public static final String PATH_UPDATE_ANNOTATIONS_JSON = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "update_annotations.json";
    public static final String PATH_DELETE_ANNOTATIONS_CSV = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "delete_annotations.csv";
    public static final String PATH_ADD_REPLIES_JSON = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "add_annotation_replies.json";
    public static final String PATH_ANNOTATED_DOC = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "documents" + File.separator + "document_annotations" + File.separator + "VAPIL Annotations Doc (Do Not Delete).pdf";


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

    public static QueryResponse.QueryResult getVideoAnnotationsDoc(VaultClient vaultClient) {
        String query = "SELECT id,\n" +
                "name__v,\n" +
                "major_version_number__v,\n" +
                "minor_version_number__v\n" +
                "FROM documents\n" +
                "WHERE name__v LIKE 'VAPIL Video Annotations Doc%'";

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

        FileHelper.createFile(PATH_CREATE_ANNOTATIONS_JSON);
        File createAnnotationsFile = new File(PATH_CREATE_ANNOTATIONS_JSON);
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

    public static void writeAddRepliesFile(List<String> docIds, List<String> annotationIds) throws IOException {
        FileHelper.createFile(PATH_ADD_REPLIES_JSON);
        File addRepliesFile = new File(PATH_ADD_REPLIES_JSON);

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

    public static void writeUpdateAnnotationsFile(List<String> docIds, List<String> annotationIds) throws IOException {
        FileHelper.createFile(PATH_UPDATE_ANNOTATIONS_JSON);
        File updateAnnotationsFile = new File(PATH_UPDATE_ANNOTATIONS_JSON);

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
                .setInputPath(PATH_CREATE_ANNOTATIONS_JSON)
                .createMultipleAnnotations();

        assertNotNull(createMultipleAnnotationsResponse);
        assertTrue(createMultipleAnnotationsResponse.isSuccessful());
        return createMultipleAnnotationsResponse;
    }

    public static DocumentAnnotationBulkResponse deleteAnnotations(VaultClient vaultClient, List<String> docIds, List<String> annotationIds) {
        FileHelper.createFile(PATH_DELETE_ANNOTATIONS_CSV);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id__sys", "document_version_id__sys"});
        int size = docIds.size();
        for (int i = 0; i < size; i++) {
            data.add(new String[]{annotationIds.get(i), docIds.get(i)});
        }

        FileHelper.writeCsvFile(PATH_DELETE_ANNOTATIONS_CSV, data);
        DocumentAnnotationBulkResponse deleteAnnotationsResponse = vaultClient.newRequest(DocumentAnnotationRequest.class)
                .setContentTypeCsv()
                .setInputPath(PATH_DELETE_ANNOTATIONS_CSV)
                .deleteAnnotations();

        return deleteAnnotationsResponse;
    }
}
