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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.HashMap;
import java.util.Map;

@Tag("UserRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class UserRequestTest {

    @Test
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
            System.out.println("\nUser: " + user.getUserName());
            System.out.println("User ID: " + user.getId());
            System.out.println("Name: " + user.getUserFirstName() + " " + user.getUserLastName());
            System.out.println("Email: " + user.getUserEmail());
            System.out.println("Timezone: " + user.getUserTimezone());
            System.out.println("Locale: " + user.getUserLocale());
            System.out.println("Language: " + user.getUserLanguage());
            System.out.println("Company: " + user.getCompany());
            System.out.println("Title: " + user.getUserTitle());
            System.out.println("Office Phone: " + user.getOfficePhone());
            System.out.println("Fax: " + user.getFax());
            System.out.println("Mobile: " + user.getMobilePhone());
            System.out.println("Site: " + user.getSite());
            System.out.println("Domain Admin?: " + user.getIsDomainAdmin());
            System.out.println("Active user?: " + user.getActive());
            System.out.println("Active on domain?: " + user.getDomainActive());
            System.out.println("Security Profile: " + user.getSecurityProfile());
            System.out.println("License type: " + user.getLicenseType());
            System.out.println("Security Policy ID: " + user.getSecurityPolicyId());
            System.out.println("User needs to change password?: " + user.getUserNeedsToChangePassword());
            System.out.println("Created by: " + user.getCreatedBy() + " on " + user.getCreatedDate());
            System.out.println("Last modified by: " + user.getModifiedBy() + " on " + user.getModifiedDate());
            System.out.println("Domain ID: " + user.getDomainId());
            System.out.println("Domain Name: " + user.getDomainName());
            System.out.println("Vault ID: " + user.getVaultId());
            System.out.println("Federated ID: " + user.getFederatedId());
            System.out.println("Salesforce User Name: " + user.getSalesforceUserName());
            System.out.println("Last login at: " + user.getLastLogin());
            System.out.println("Group IDs: " + user.getGroupId());
        }
    }

    public void testRetrieveAllUsersNotCurrentVault(VaultClient vaultClient) {
        System.out.println("\n****** Get All Users not in current Vault (Display First 10) ******");
        UserRetrieveResponse response = vaultClient.newRequest(UserRequest.class)
                .setVaults("-1")
                .retrieveAllUsers();

        int i = 0;

        for (UserRetrieveResponse.UserNode users : response.getUsers()) {
            com.veeva.vault.vapil.api.model.common.User user = users.getUser();
            System.out.println("\nUser: " + user.getUserName());
            System.out.println("User ID: " + user.getId());
            System.out.println("Name: " + user.getUserFirstName() + " " + user.getUserLastName());
            System.out.println("Email: " + user.getUserEmail());
            i++;
            if (i > 10) break;
        }

        System.out.println("Test Complete...\n");
    }

    public void testRetrieveUser(VaultClient vaultClient) {
        int userId = 1069913;
        System.out.println("\n****** Get Single User (" + userId + ") ******");
        UserRetrieveResponse response = vaultClient.newRequest(UserRequest.class).retrieveUser(userId);

        for(UserRetrieveResponse.UserNode users : response.getUsers()) {
            com.veeva.vault.vapil.api.model.common.User user = users.getUser();
            System.out.println("\nUser name: " + user.getUserName());
            System.out.println("User: " + user.getUserFirstName() + " " + user.getUserLastName());
            System.out.println("Email: " + user.getUserEmail());

            System.out.println("Test Complete...\n");
        }
    }
    public void testSingleCreateUser(VaultClient vaultClient) {
        System.out.println("****** Creating a single user ******");
        Map<String,Object> formData = createFormData();
        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeForm()
                .setBodyParams(formData)
                .createSingleUser();
        displayResults(response,true);
        System.out.println("Test Complete...\n");
    }

    public void testSingleUpdateUser(VaultClient vaultClient) {
        System.out.println("****** Updating a single user ******");
        Map<String,Object> formData = createFormDataForUpdate();
        UserResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeXForm()
                .setBodyParams(formData)
                .updateSingleUser(4005489);
        displayResults(response,true);
        System.out.println("Test Complete...\n");
    }

    public void testUpdateVaultMembership(VaultClient vaultClient) {
        System.out.println("****** Updating Vault Membership ******");
        VaultResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeXForm()
                .updateVaultMembership(4005489,23700);
        System.out.println("Status = " + response.isSuccessful());
        System.out.println("Test Complete...\n");
    }

    // Create map data for testing creation of new user
    public Map<String,Object> createFormData() {
        Map<String,Object> mapData = new HashMap<String,Object>();
        mapData.put("user_name__v", "username@verteobiotech.com");
        mapData.put("user_first_name__v","David");
        mapData.put("user_last_name__v","Gilmour");
        mapData.put("user_email__v","username@verteobiotech.com");
        mapData.put("user_timezone__v","America/Phoenix");
        mapData.put("user_locale__v","en_US");
        mapData.put("security_policy_id__v","6017");
        mapData.put("user_language__v","en");
        mapData.put("security_profile__v","");
        mapData.put("license_type__v","full__v");
        mapData.put("file","");

        return mapData;
    }

    // Create map data for testing the updating of a user
    public Map<String,Object> createFormDataForUpdate() {
        Map<String,Object> mapData = new HashMap<String,Object>();
        mapData.put("user_first_name__v","Vapil-Update");
        mapData.put("user_last_name__v","User Update 3");

        return mapData;
    }

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

    public void testBulkCreateUsersJSON_CSV(VaultClient vaultClient, String filePath) {
        System.out.println("****** Creating users in bulk (JSON) ******");
        UserBulkResponse response = vaultClient.newRequest(UserRequest.class)
                .setContentTypeCsv()
                .setInputPath(filePath)
                .createUserRecords();

        displayBulkResults(response,false);
        System.out.println("Test Complete...\n");
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
