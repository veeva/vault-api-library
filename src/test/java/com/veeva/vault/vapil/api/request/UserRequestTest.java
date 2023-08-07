/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.metadata.User;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.UserPermissionResponse.UserPermissions;
import com.veeva.vault.vapil.api.model.response.UserPermissionResponse.UserPermissions.PermissionSet;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.UserRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag("UserRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User request should")
public class UserRequestTest {

    private static final int VAULT_ID = 166231;
    static final String CREATE_USERS_CSV_PATH = UserRequestHelper.getPathCreateMultipleUsers();
    static final String USER_NAME__V = "user_name__v";
    static final String USER_FIRST_NAME__V = "user_first_name__v";
    static final String USER_LAST_NAME__V = "user_last_name__v";
    static final String USER_EMAIL__V = "user_email__v";
    static final String USER_TIMEZONE__V = "user_timezone__v";
    static final String USER_LOCALE__V = "user_locale__v";
    static final String USER_LANGUAGE__V = "user_language__v";
    static final String SECURITY_PROFILE__V = "security_profile__v";
    static final String SECURITY_POLICY_ID__V = "security_policy_id__v";
    static final String LICENSE_TYPE__V = "license_type__v";
    static final String USER_EMAIL = "vapil.test-user@veeva.com";
    static final String TIMEZONE = "America/New_York";
    static final String LOCALE = "en_US";
    static final String LANGUAGE = "en";
    static final String SECURITY_PROFILE = "copy_of_document_user__c";
    static final String SECURITY_POLICY_ID = "49687";
    static final String LICENSE_TYPE = "full__v";
    private static String userId;

    @Test
    @DisplayName("successfully retrieve user metadata")
    public void testRetrieveUserMetadata(VaultClient vaultClient) {
        MetaDataUserResponse response = vaultClient.newRequest(UserRequest.class).retrieveUserMetadata();
        Assertions.assertTrue(response.isSuccessful());

        Assertions.assertNotNull(response.getProperties());
        for (User user : response.getProperties()) {
            Assertions.assertNotNull(user.getName());
            Assertions.assertNotNull(user.getType());
            Assertions.assertNotNull(user.getLength());
            Assertions.assertNotNull(user.getEditable());
            Assertions.assertNotNull(user.getQueryable());
            Assertions.assertNotNull(user.getRequired());
            Assertions.assertNotNull(user.getMultivalue());
        }
    }

    @Test
    @DisplayName("successfully retrieve all users")
    public void testRetrieveAllUsers(VaultClient vaultClient) {
        UserRetrieveResponse response = vaultClient.newRequest(UserRequest.class)
                .retrieveAllUsers();
        Assertions.assertTrue(response.isSuccessful());

        for (UserRetrieveResponse.UserNode users : response.getUsers()) {
            com.veeva.vault.vapil.api.model.common.User user = users.getUser();

            Assertions.assertNotNull(user.getUserName());
            Assertions.assertNotNull(user.getId());
            Assertions.assertNotNull(user.getUserFirstName());
            Assertions.assertNotNull(user.getUserLastName());
        }
    }

    @Test
    @DisplayName("successfully retrieve all users not in current vault")
    public void testRetrieveAllUsersNotCurrentVault(VaultClient vaultClient) {
        UserRetrieveResponse response = vaultClient.newRequest(UserRequest.class)
                .setVaults("-1")
                .retrieveAllUsers();

        for (UserRetrieveResponse.UserNode users : response.getUsers()) {
            com.veeva.vault.vapil.api.model.common.User user = users.getUser();

            Assertions.assertNotNull(user.getUserName());
            Assertions.assertNotNull(user.getId());
            Assertions.assertNotNull(user.getUserFirstName());
            Assertions.assertNotNull(user.getUserLastName());
        }
    }

    @Test
    @Order(1)
    @DisplayName("successfully create a user")
    public void testCreateSingleUser(VaultClient vaultClient) {
        Map<String,Object> formData = createFormData();
        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeForm()
                .setBodyParams(formData)
                .createSingleUser();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
        userId = response.getId();
    }

    @Test
    @Order(2)
    @DisplayName("successfully update a user")
    public void testSingleUpdateUser(VaultClient vaultClient) {
        Map<String,Object> mapData = new HashMap<String,Object>();
        mapData.put("user_first_name__v","Vapil-Update");

        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeXForm()
                .setBodyParams(mapData)
                .updateSingleUser(Integer.valueOf(userId));

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    @Order(3)
    @DisplayName("successfully update vault membership")
    public void testUpdateVaultMembership(VaultClient vaultClient) {
        Map<String,Object> mapData = new HashMap<String,Object>();
        mapData.put("active__v", false);

        VaultResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeXForm()
                .updateVaultMembership(Integer.valueOf(userId),VAULT_ID);

        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    @Order(4)
    @DisplayName("successfully retrieve a specific user")
    public void testRetrieveUser(VaultClient vaultClient) {
        UserRetrieveResponse response = vaultClient.newRequest(UserRequest.class)
                .retrieveUser(Integer.valueOf(userId));

        for(UserRetrieveResponse.UserNode users : response.getUsers()) {
            com.veeva.vault.vapil.api.model.common.User user = users.getUser();

            Assertions.assertNotNull(user.getUserName());
            Assertions.assertNotNull(user.getId());
            Assertions.assertNotNull(user.getUserFirstName());
            Assertions.assertNotNull(user.getUserLastName());
        }
    }

    @Test
    @Disabled
    @Order(5)
    @DisplayName("successfully create multiple users")
    public void testCreateMultipleUsers(VaultClient vaultClient) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{USER_NAME__V, USER_FIRST_NAME__V, USER_LAST_NAME__V, USER_EMAIL__V,
                USER_TIMEZONE__V, USER_LOCALE__V, USER_LANGUAGE__V, SECURITY_PROFILE__V,
                SECURITY_POLICY_ID__V, LICENSE_TYPE__V});
        for (int i = 0; i < 3; i++) {
            String username = "vapiltestuser" + i + "@sb-developersupport.com";
            String firstName = "Vapil";
            String lastName = "Test-User" + i;

            data.add(new String[]{username, firstName, lastName, USER_EMAIL, TIMEZONE, LOCALE,
                    LANGUAGE, SECURITY_PROFILE, SECURITY_POLICY_ID, LICENSE_TYPE});
        }

        FileHelper.writeCsvFile(CREATE_USERS_CSV_PATH, data);

        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setInputPath(CREATE_USERS_CSV_PATH)
                .createUserRecords();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
        for (UserResponse user : response.getData()) {
            Assertions.assertNotNull(user.getId());
        }
    }

    @Test
    @Disabled
    public void testBulkCreateUsersCSV_CSV(VaultClient vaultClient, String filePath) {
        System.out.println("****** Creating users in bulk (CSV) ******");
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setAcceptCSV()
                .setInputPath(filePath)
                .createUserRecords();

        displayBulkResults(response,false);
        System.out.println("Test Complete...\n");
    }

    @Test
    @Disabled
    public void testBulkCreateUsersJSON_CSV(VaultClient vaultClient, String filePath) {
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setInputPath(filePath)
                .createUserRecords();

        Assertions.assertTrue(response.isSuccessful());
    }

    public void testBulkUpsertUsersCSV(VaultClient vaultClient, String filePath, String idParam) {
        System.out.println("****** Upserting Users ******");
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setInputPath(filePath)
                .setOperation("upsert")
                .setIdParam(idParam)
                .createUserRecords();

        displayBulkResults(response,false);
        System.out.println("Test Complete...\n");
    }

    public void testBulkUpdateUsersJSON_CSV(VaultClient vaultClient, String filePath) {
        System.out.println("****** Updating Users ******");
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setInputPath(filePath)
                .updateUserRecords();

        displayBulkResults(response,false);
        System.out.println("Test Complete...\n");
    }

    public void testBulkUpdateUsersCSV_CSV(VaultClient vaultClient, String filePath) {
        System.out.println("****** Updating Users ******");
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setAcceptCSV()
                .setInputPath(filePath)
                .updateUserRecords();

        displayBulkResults(response,false);
        System.out.println("Test Complete...\n");
    }

    public void testDisableUserSingleVault(VaultClient vaultClient) {
        System.out.println("****** Disabling user ******");
        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .disableUser(4005489);

        displayResults(response, true);
        System.out.println("Test Complete...\n");
    }

    public void testDisableUserDomain(VaultClient vaultClient) {
        System.out.println("****** Disabling user ******");
        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .setDomainAsTrue()
                .disableUser(4005489);

        displayResults(response, true);
        System.out.println("Test Complete...\n");
    }

    public void testChangeUserPassword(VaultClient vaultClient, int userId, String oldPass, String newPass) {
        System.out.println("\n****** Changing User Password (" + userId + ") ******");
        VaultResponse response = vaultClient.newRequest(UserRequest.class).changePassword(userId, oldPass, newPass);
        System.out.println(response.getResponseStatus());
        System.out.println("Test Complete...");
    }

    public void testRetrieveUserPermissions(VaultClient vaultClient, int userId) {
        int i = 0;
        UserPermissionResponse response = vaultClient.newRequest(UserRequest.class)
                .retrieveUserPermissions(userId);

        for(UserPermissions user : response.getData()) {
            System.out.println("Name: " + user.getName());
            PermissionSet p = user.getPermissions();
            System.out.println("\tRead: " + p.getRead());
            System.out.println("\tEdit: " + p.getEdit());
            System.out.println("\tCreate: " + p.getCreate());
            System.out.println("\tDelete: " + p.getDelete());

            if (i >= 10) {
                break;
            }
            i++;

        }
        System.out.println("Test Complete...");
    }

    public void testRetrieveUserPermissionsFilter(VaultClient vaultClient, int userId) {
        int i = 0;
        UserPermissionResponse response = vaultClient.newRequest(UserRequest.class)
                .setFilter("object.product__v.object_actions")
                .retrieveUserPermissions(userId);

        for(UserPermissions user : response.getData()) {
            System.out.println("Name: " + user.getName());
            PermissionSet p = user.getPermissions();
            System.out.println("\tRead: " + p.getRead());
            System.out.println("\tEdit: " + p.getEdit());
            System.out.println("\tCreate: " + p.getCreate());
            System.out.println("\tDelete: " + p.getDelete());

            if (i >= 10) {
                break;
            }
            i++;

        }
        System.out.println("Test Complete...");
    }

    // Create map data for testing creation of new user
    public Map<String,Object> createFormData() {
        Map<String,Object> mapData = new HashMap<String,Object>();
        mapData.put(USER_NAME__V, "vapiltestuser@sb-developersupport.com");
        mapData.put(USER_FIRST_NAME__V,"Vapil");
        mapData.put(USER_LAST_NAME__V,"Test-User");
        mapData.put(USER_EMAIL__V,USER_EMAIL);
        mapData.put(USER_TIMEZONE__V,TIMEZONE);
        mapData.put(USER_LOCALE__V,LOCALE);
        mapData.put(USER_LANGUAGE__V,LANGUAGE);
        mapData.put(SECURITY_PROFILE__V,SECURITY_PROFILE);
        mapData.put(SECURITY_POLICY_ID__V,SECURITY_POLICY_ID);
        mapData.put(LICENSE_TYPE__V,LICENSE_TYPE);

        return mapData;
    }

    public void displayResults(UserResponse response, Boolean singleOperation) {
        System.out.println(response.getResponse());

        if (response.isSuccessful()) {
            System.out.println("Errors exist: ");

            if (response.getErrors() != null) {
                for (VaultResponse.APIResponseError e : response.getErrors()) {
                    System.out.println("VaultResponse Error: " + e.getMessage());
                }
            }
        }
        if (response.getErrors() == null) {
            if (singleOperation) {
                System.out.println("Status: " + response.getResponseStatus());
                System.out.println("User ID: " + response.getId());
            }
        }


    }

    public void displayBulkResults(UserBulkResponse response, Boolean singleOperation) {
        System.out.println(response.getResponse());

        if (response.hasUserResultErrors()) {
            System.out.println("Errors exist: ");

            if (response.getErrors() != null) {
                for (VaultResponse.APIResponseError e : response.getErrors()) {
                    System.out.println("VaultResponse Error: " + e.getMessage());
                }
            }
        }
        if (response.getErrors() == null) {
            for (UserResponse ur : response.getData()) {
                System.out.println("User record successful?: " + ur.isSuccessful());

                if (ur.isSuccessful()) {
                    System.out.println(ur.getId() + " " + ur.getResponseStatus());
                }

                if (ur.getErrors() != null) {
                    for (VaultResponse.APIResponseError err : ur.getErrors()) {
                        System.out.println("Error (" + err.getType() + ") - " + err.getMessage());
                    }
                }
            }
        }


    }
}
