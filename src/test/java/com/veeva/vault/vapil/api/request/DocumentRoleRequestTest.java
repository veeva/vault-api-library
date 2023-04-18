package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentRequestType;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Tag("DocumentRoleRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class DocumentRoleRequestTest {
	
	private static final String OWNER_ROLE = "owner__v";
    private static final String VIEWER_ROLE = "viewer__v";
    private static final Long ALL_INTERNAL_USERS_ID = 1L;
    private static final Long USER_ID = 8701969L;
    private static final String TEST_CSV = "testRoleCsv.csv";
    private static final int DOC_ID = 5;
    private static Integer BINDER_ID = 1;


    @Test
    public void testRetrieveDocumentRoles(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        DocumentRoleRetrieveResponse response = documentRoleRequest.retrieveRoles(DocumentRequestType.DOCUMENTS, DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocumentRoles());
    }

    @Test
    public void testRetrieveBinderRoles(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        DocumentRoleRetrieveResponse response = documentRoleRequest.retrieveRoles(DocumentRequestType.BINDERS, BINDER_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocumentRoles());
    }

    @Test
    public void testRetrieveDocumentRolesWithFilter(VaultClient vaultClient){
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        DocumentRoleRetrieveResponse response = documentRoleRequest.retrieveRoles(DocumentRequestType.DOCUMENTS, DOC_ID, OWNER_ROLE);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocumentRoles());
    }

    @Test
    public void testRetrieveBinderRolesWithFilter(VaultClient vaultClient){
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        DocumentRoleRetrieveResponse response = documentRoleRequest.retrieveRoles(DocumentRequestType.BINDERS, BINDER_ID, OWNER_ROLE);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getDocumentRoles());
    }

    @Test
    public void testAssignUserToRoleOnSingleDocument(VaultClient vaultClient){
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setBodyParams(Collections.singletonMap(VIEWER_ROLE + ".users", USER_ID));
        DocumentRoleChangeResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnASingleDocument(DocumentRequestType.DOCUMENTS, DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getUpdatedRoles());
    }

    @Test
    public void testAssignGroupToRoleOnSingleDocument(VaultClient vaultClient){
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setBodyParams(Collections.singletonMap(VIEWER_ROLE + ".groups", ALL_INTERNAL_USERS_ID));
        DocumentRoleChangeResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnASingleDocument(DocumentRequestType.DOCUMENTS, DOC_ID);
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getUpdatedRoles());
    }

    @Test
    public void  testRemoveUserFromRoleOnSingleDocument(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);

        DocumentRoleChangeResponse response = documentRoleRequest
                .removeUsersAndGroupsFromRolesOnASingleDocument(
                        DocumentRequestType.DOCUMENTS, DOC_ID, VIEWER_ROLE, DocumentRoleRequest.MemberType.USER, USER_ID
                );
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getUpdatedRoles());
    }

    @Test
    public void  testRemoveGroupFromRoleOnSingleDocument(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        DocumentRoleChangeResponse response = documentRoleRequest
                .removeUsersAndGroupsFromRolesOnASingleDocument(
                        DocumentRequestType.DOCUMENTS, DOC_ID, VIEWER_ROLE, DocumentRoleRequest.MemberType.GROUP, ALL_INTERNAL_USERS_ID
                );
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getUpdatedRoles());
    }

    // Test Manually
    @Test
    public void testAssignUsersAndGroupsToRolesOnMultipleDocumentsLocalFile(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        documentRoleRequest.setInputPath(TEST_CSV);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    // Test Manually
    @Test
    public void testRemoveUsersAndGroupsFromRolesOnMultiplesDocumentsLocalFile(VaultClient vaultClient){
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        documentRoleRequest.setInputPath(TEST_CSV);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testAssignUsersAndGroupsToRolesOnMultipleDocumentsBodyParams(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeXForm();
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("docIds", Integer.toString(DOC_ID));
        bodyParams.put(VIEWER_ROLE + ".users", USER_ID);
        bodyParams.put(VIEWER_ROLE + ".groups", ALL_INTERNAL_USERS_ID);
        documentRoleRequest.setBodyParams(bodyParams);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testRemoveUsersAndGroupsFromRolesOnMultipleDocumentsBodyParams(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeXForm();
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("docIds", Integer.toString(DOC_ID));
        bodyParams.put(VIEWER_ROLE + ".users", USER_ID);
        bodyParams.put(VIEWER_ROLE + ".groups", ALL_INTERNAL_USERS_ID);
        documentRoleRequest.setBodyParams(bodyParams);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testAssignUsersAndGroupsToRolesOnMultipleDocumentsBinaryContent(VaultClient vaultClient) throws IOException {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        byte[] csv = Files.readAllBytes(new File(TEST_CSV).toPath());
        documentRoleRequest.setBinaryFile(TEST_CSV, csv);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    public void testRemoveUsersAndGroupsFromRolesOnMultipleDocumentsBinaryContent(VaultClient vaultClient) throws IOException {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        byte[] csv = Files.readAllBytes(new File(TEST_CSV).toPath());
        documentRoleRequest.setBinaryFile(TEST_CSV, csv);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }
}
