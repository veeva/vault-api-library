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
import com.veeva.vault.vapil.api.model.common.SCIMAttribute;
import com.veeva.vault.vapil.api.model.common.SCIMUser;
import com.veeva.vault.vapil.api.model.response.SCIMResponse;
import com.veeva.vault.vapil.api.model.response.SCIMResponse.Resource;
import com.veeva.vault.vapil.api.model.response.SCIMUserBulkResponse;
import com.veeva.vault.vapil.api.model.response.SCIMUserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.ArrayList;
import java.util.List;

@Tag("SCIMRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class SCIMRequestTest {

    final String USER_ID = "280259";

    @Test
    public void testRetrieveSingleSCIMResource(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class)
                .retrieveSingleSCIMResource( "SecurityProfiles","business_admin__v");
        Assertions.assertNotNull(response.getSchemas());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    public void testRetrieveSCIMResources(VaultClient vaultClient) {
        String type = "User";
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class)
                .retrieveSCIMResources(type);

        Assertions.assertNotNull(response.getSchemas());
        System.out.println("Schemas: " + response.getSchemas());
    }

    @Test
    public void testCreateUserWithSCIMJSON(VaultClient vaultClient) {

        String jsonString ="{\n" +
                "    \"schemas\": [\n" +
                "        \"urn:ietf:params:scim:schemas:extension:veevavault:2.0:User\",\n" +
                "        \"urn:ietf:params:scim:schemas:core:2.0:User\"\n" +
                "    ],\n" +
                "    \"userName\": \"pat.jones@verteobiotech.com\",\n" +
                "    \"emails\": [\n" +
                "        {\n" +
                "            \"value\": \"pat.jones@verteobiotech.com\",\n" +
                "             \"type\": \"work\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"name\": {\n" +
                "                \"familyName\": \"Jones\",\n" +
                "                \"givenName\": \"Pat\"\n" +
                "            },\n" +
                "    \"preferredLanguage\": \"en\",\n" +
                "    \"locale\": \"en_US\",\n" +
                "    \"timezone\": \"America/Los_Angeles\",\n" +
                "    \"urn:ietf:params:scim:schemas:extension:veevavault:2.0:User\": {\n" +
                "    \"securityProfile\": {\n" +
                "                    \"value\": \"system_admin__v\"\n" +
                "                }\n" +
                "  }\n" +
                "}";
        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUserJSON(jsonString)
                .createUser();
        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUserName());

        for(SCIMUser.Email email : user.getEmails()){
            Assertions.assertNotNull(email.getValue());
        }
    }

    @Test
    public void testCreateUserWithSCIMObject(VaultClient vaultClient) {
        SCIMUser scimUser = new SCIMUser();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:extension:veevavault:2.0:User");
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        scimUser.setSchemas(schemas);

        scimUser.setDisplayName("Test User");

        List<SCIMUser.Email> emails = new ArrayList<>();
        emails.add(new SCIMUser.Email("work","sameer.mehta@veeva.com"));
        scimUser.setEmails(emails);

        SCIMUser.Name name = new SCIMUser.Name("Tes","User");
        scimUser.setName(name);

        scimUser.setUserName("username@sameerm.com");
        scimUser.setPreferredLanguage("en");
        scimUser.setLocale("en_US");
        scimUser.setTimezone("America/Los_Angeles");

        SCIMUser.SchemaUserObject.ReferenceValue securityProfile = new SCIMUser.SchemaUserObject.ReferenceValue();
        securityProfile.setValue("system_admin__v");
        SCIMUser.SchemaUserObject schemaUserObject = new SCIMUser.SchemaUserObject();

        schemaUserObject.setSecurityProfile(securityProfile);
        scimUser.setSchemaUserObject(schemaUserObject);

        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUser(scimUser)
                .createUser();
        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUserName());

        for(SCIMUser.Email email : user.getEmails()){
            Assertions.assertNotNull(email.getValue());
        }
    }

    @Test
    public void testUpdateUserWithSCIMObject(VaultClient vaultClient) {

        SCIMUser scimUser = new SCIMUser();
        scimUser.setDisplayName("Ricardo Bronson");
        SCIMUser.Name name = new SCIMUser.Name("Ricardo","Bronson");
        scimUser.setName(name);

        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUser(scimUser)
                .updateUser(USER_ID);
        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUserName());

        for(SCIMUser.Email email : user.getEmails()){
            Assertions.assertNotNull(email.getValue());
        }
    }


    @Test
    public void testUpdateUserWithSCIMJSON(VaultClient vaultClient) {
        System.out.println("\n****** Get User by id with SCIM ******");
        String jsonString = "{\n" +
                "    \"schemas\": [\n" +
                "        \"urn:ietf:params:scim:schemas:extension:veevavault:2.0:User\",\n" +
                "        \"urn:ietf:params:scim:schemas:core:2.0:User\"\n" +
                "    ],\n" +
                "    \"name\": {\n" +
                "                \"familyName\": \"Bronson Sr\",\n" +
                "                \"givenName\": \"Rich\"\n" +
                "    }\n" +
                "}";

        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUserJSON(jsonString)
                .updateUser(USER_ID);
        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUserName());

        for(SCIMUser.Email email : user.getEmails()){
            Assertions.assertNotNull(email.getValue());
        }
    }

    @Test
    public void testRetrieveSingleUserWithSCIM(VaultClient vaultClient) {
        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .retrieveUser(USER_ID);

            SCIMUser user = response.getUser();
            Assertions.assertNotNull(user);
            Assertions.assertNotNull(user.getName());
            Assertions.assertNotNull(user.getUserName());

            for(SCIMUser.Email email : user.getEmails()){
                Assertions.assertNotNull(email.getValue());
            }
    }

    @Test
    public void testCurrentUserWithSCIM(VaultClient vaultClient) {
        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .retrieveCurrentUser();

        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUserName());

        for(SCIMUser.Email email : user.getEmails()){
            Assertions.assertNotNull(email.getValue());
        }
    }

    @Test
    public void testUpdateCurrentUserWithSCIMObject(VaultClient vaultClient) {
        SCIMUser scimUser = new SCIMUser();
        scimUser.setDisplayName("Richie Bronson");
        SCIMUser.Name name = new SCIMUser.Name("Richie","Bronson");
        scimUser.setName(name);
        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUser(scimUser)
                .updateCurrentUser();

        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(name.getGivenName(),  user.getName().getGivenName());
        Assertions.assertEquals(name.getFamilyName(),  user.getName().getFamilyName());
    }

    @Test
    public void testUpdateCurrentUserWithSCIMJSON(VaultClient vaultClient) {
        String scimUserUpdate = "{\n" +
                "    \"schemas\": [\n" +
                "        \"urn:ietf:params:scim:schemas:extension:veevavault:2.0:User\",\n" +
                "        \"urn:ietf:params:scim:schemas:core:2.0:User\"\n" +
                "    ],\n" +
                "    \"name\": {\n" +
                "                \"familyName\": \"Bronson\",\n" +
                "                \"givenName\": \"Rich\"\n" +
                "    }\n" +
                "}";


        SCIMUserResponse response = vaultClient.newRequest(SCIMRequest.class)
                .setSCIMUserJSON(scimUserUpdate)
                .updateCurrentUser();
        SCIMUser user = response.getUser();
        Assertions.assertNotNull(user);
    }

    @Test
    public void testRetrieveAllUsersWithSCIM(VaultClient vaultClient) {
        SCIMUserBulkResponse response = vaultClient.newRequest(SCIMRequest.class)
                .retrieveAllUsers();

        List<SCIMUser> users = response.getResources();
        for(SCIMUser user : users) {
            Assertions.assertNotNull(user.getUserName());
            for(SCIMUser.Email email : user.getEmails()){
                Assertions.assertNotNull(email.getValue());
            }
        }
    }

    @Test
    public void testRetrieveSCIMProviderConfig(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class).retrieveSCIMProvider();

        Assertions.assertNotNull(response.getSchemas());
        Assertions.assertNotNull(response.getBulk().getSupported());
        Assertions.assertNotNull(response.getBulk().getMaxOperations());
        Assertions.assertNotNull(response.getBulk().getMaxPayloadSize());
        Assertions.assertNotNull(response.getFilter().getSupported());
        Assertions.assertNotNull(response.getFilter().getMaxResults());
    }

    @Test
    public void testRetrieveSCIMSchemas(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class).retrieveSCIMProvider();
        Assertions.assertNotNull(response.getSchemas());

        if (!response.hasResourceErrors()) {
            List<Resource> schemaResources = response.getResources();
            for (Resource resource : schemaResources) {
                Assertions.assertNotNull(resource.getSchemas());
                List<SCIMAttribute> resourceAttributes = resource.getAttributes();
                for (SCIMAttribute attribute : resourceAttributes) {
                    Assertions.assertNotNull(attribute.getName());
                }
            }
        }
    }

    @Test
    public void testRetrieveSingleSCIMSchema(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class).retrieveSchema("urn:ietf:params:scim:schemas:extension:veevavault:2.0:User");
        Assertions.assertNotNull(response.getSchemas());
        Assertions.assertEquals(1, response.getSchemas().size());
        if (!response.hasAttributeErrors()) {
            List<SCIMAttribute> attributes = response.getAttributes();
            for(SCIMAttribute attribute : attributes) {
                Assertions.assertNotNull(attribute.getName());
            }
        }
    }

    @Test
    public void testRetrieveSCIMResourceTypes(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class).retrieveResourceTypes();
        Assertions.assertNotNull(response.getTotalResults());

        if (!response.hasResourceErrors()) {
            List<Resource> resources = response.getResources();
            for(Resource resource : resources) {
                Assertions.assertNotNull(resource.getId());
            }
        }
    }

    @Test
    public void testRetrieveSingleSCIMResourceType(VaultClient vaultClient) {
        SCIMResponse response = vaultClient.newRequest(SCIMRequest.class).retrieveResourceType("User");
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getName());
    }
}
