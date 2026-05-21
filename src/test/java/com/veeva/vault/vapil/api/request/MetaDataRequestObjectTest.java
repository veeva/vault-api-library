/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.metadata.VaultObject;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectField;
import com.veeva.vault.vapil.api.model.metadata.VaultObjectPageLayout;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectBulkResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectFieldResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectPageLayoutResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataObjectResponse;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("MetaDataRequestObjectTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Metadata object request should")
public class MetaDataRequestObjectTest {

    static final String OBJECT_NAME = "vapil_test_object__c";
    static final String FIELD_NAME = "test_subtype_field__c";
    static final String OBJECT_PAGE_LAYOUT_NAME = "vapil_test_action_layout__c";
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Test
    @DisplayName("successfully retrieve standard and custom Vault Objects")
    public void testRetrieveObjectCollection() {
        MetaDataObjectBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveObjectCollection();

        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getObjects());
    }

    @Test
    @DisplayName("successfully retrieve all metadata configured on a standard or custom Vault Object")
    public void testRetrieveObjectMetadata() {
        MetaDataObjectResponse response = vaultClient.newRequest(MetaDataRequest.class)
                .retrieveObjectMetadata(OBJECT_NAME);
        Assertions.assertTrue(response.isSuccessful());

        VaultObject objectMetaData = response.getObject();
        Assertions.assertNotNull(objectMetaData);
        Assertions.assertNotNull(objectMetaData.getName());

        if (objectMetaData.getAvailableLifecycles() != null) {
            for (String lifecycle : objectMetaData.getAvailableLifecycles())
                Assertions.assertNotNull(lifecycle);
        }

        if (objectMetaData.getRelationships() != null) {
            for (VaultObject.Relationship relationship : objectMetaData.getRelationships()) {
                Assertions.assertNotNull(relationship.getRelationshipName());
                Assertions.assertNotNull(relationship.getField());
            }
        }

        if (objectMetaData.getUserRoleSetupObject() != null) {
            Assertions.assertNotNull(objectMetaData.getUserRoleSetupObject().getUrl());
        }
    }

    @Test
    @Disabled("")
    @DisplayName("successfully retrieve all metadata configured on the specific Vault Object field")
    public void testRetrieveObjectFieldMetadata() {
        MetaDataObjectFieldResponse response = vaultClient.newRequest(MetaDataRequest.class)
                .retrieveObjectFieldMetaData(OBJECT_NAME, FIELD_NAME);
        Assertions.assertTrue(response.isSuccessful());

        VaultObjectField fieldMetaData = response.getField();
        Assertions.assertNotNull(fieldMetaData);

        if (fieldMetaData.getObjectReference() != null) {
            Assertions.assertNotNull(fieldMetaData.getObjectReference().getUrl());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all page layouts associated with an object")
    class TestRetrievePageLayouts {
        MetaDataObjectPageLayoutResponse retrievePageLayoutsResponse = null;

        @Test
        @Order(1)
        public void testRequest() {
            retrievePageLayoutsResponse = vaultClient.newRequest(MetaDataRequest.class)
                    .retrievePageLayouts(OBJECT_NAME);

            assertNotNull(retrievePageLayoutsResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrievePageLayoutsResponse.isSuccessful());

            List<VaultObjectPageLayout> layouts = retrievePageLayoutsResponse.getData();
            assertNotNull(layouts);
            for (VaultObjectPageLayout layout : layouts) {
                assertNotNull(layout.getName());
                assertNotNull(layout.getLabel());
                assertNotNull(layout.getObjectType());
                assertNotNull(layout.getUrl());
                assertNotNull(layout.getActive());
                assertNotNull(layout.getDescription());
                assertNotNull(layout.getDefaultLayout());
                assertNotNull(layout.getDisplayLifecycleStages());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve the metadata for a specific page layout")
    class TestRetrievePageLayoutMetadata {
        MetaDataObjectPageLayoutResponse retrievePageLayoutMetadataResponse = null;

        @Test
        @Order(1)
        public void testRequest() {
            retrievePageLayoutMetadataResponse = vaultClient.newRequest(MetaDataRequest.class)
                    .retrievePageLayoutMetadata(OBJECT_NAME, OBJECT_PAGE_LAYOUT_NAME);

            assertNotNull(retrievePageLayoutMetadataResponse);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrievePageLayoutMetadataResponse.isSuccessful());

            List<VaultObjectPageLayout> layouts = retrievePageLayoutMetadataResponse.getData();
            assertNotNull(layouts);
            VaultObjectPageLayout layout = layouts.get(0);
            assertNotNull(layout);

            assertNotNull(layout.getName());
            assertNotNull(layout.getLabel());
            assertNotNull(layout.getObject());
            assertNotNull(layout.getObjectType());
            assertNotNull(layout.getActive());
            assertNotNull(layout.getDescription());
            assertNotNull(layout.getDefaultLayout());
            assertNotNull(layout.getDisplayLifecycleStages());
            assertNotNull(layout.getCreatedDate());
            assertNotNull(layout.getLastModifiedDate());

            List<VaultObjectPageLayout.PageLayoutRule> layoutRules = layout.getLayoutRules();
            for (VaultObjectPageLayout.PageLayoutRule rule : layoutRules) {
                assertNotNull(rule.getEvaluationOrder());
                assertNotNull(rule.getStatus());
                assertNotNull(rule.getFieldsToHide());
//				assertNotNull(rule.getSectionsToHide());
//				assertNotNull(rule.getControlsToHide());
                assertNotNull(rule.getHideLayout());
//				assertNotNull(rule.getHiddenPages());
                assertNotNull(rule.getDisplayedAsReadonlyFields());
                assertNotNull(rule.getDisplayedAsRequiredFields());
                assertNotNull(rule.getFocusOnLayout());
                assertNotNull(rule.getExpression());

            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve object field metadata")
    class TestRetrieveObjectFieldMetadata {
        MetaDataObjectFieldResponse retrieveObjectFieldMetadata = null;

        @Test
        @Order(1)
        public void testRequest() {
            retrieveObjectFieldMetadata = vaultClient.newRequest(MetaDataRequest.class)
                    .retrieveObjectFieldMetaData(OBJECT_NAME, FIELD_NAME);

            assertNotNull(retrieveObjectFieldMetadata);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(retrieveObjectFieldMetadata.isSuccessful());
            assertNotNull(retrieveObjectFieldMetadata.getField());
            assertNotNull(retrieveObjectFieldMetadata.getField().getName());
            assertNotNull(retrieveObjectFieldMetadata.getField().getLabel());
            assertNotNull(retrieveObjectFieldMetadata.getField().getEditable());
//            assertNotNull(retrieveObjectFieldMetadata.getField().getHelpContent());
            assertNotNull(retrieveObjectFieldMetadata.getField().getSource());
            assertNotNull(retrieveObjectFieldMetadata.getField().getType());
            assertNotNull(retrieveObjectFieldMetadata.getField().getCreatedBy());
//            assertNotNull(retrieveObjectFieldMetadata.getField().getFormatMask());
//            assertNotNull(retrieveObjectFieldMetadata.getField().getSubtype());
        }
    }
}
