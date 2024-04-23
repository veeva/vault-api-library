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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("MetaDataRequestObjectTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Metadata object request should")
public class MetaDataRequestObjectTest {

	static final String OBJECT_NAME = "vapil_test_object__c";
	static final String FIELD_NAME = "name__v";
	static final String OBJECT_PAGE_LAYOUT_NAME = "vapil_test_object_detail_page_layout__c";
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

	@Test
	@DisplayName("successfully retrieve all page layouts associated with and object")
	public void testRetrievePageLayouts() {
		MetaDataObjectPageLayoutResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrievePageLayouts(OBJECT_NAME);

		Assertions.assertTrue(response.isSuccessful());

		VaultObjectPageLayout layout = response.getData().get(0);
		Assertions.assertNotNull(layout);

		if (layout != null) {
			Assertions.assertNotNull(layout.getName());
		}
	}

	@Test
	@DisplayName("successfully retrieve the metadata for a specific page layout")
	public void testRetrievePageLayoutMetadata() {
		MetaDataObjectPageLayoutResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrievePageLayoutMetadata(OBJECT_NAME, OBJECT_PAGE_LAYOUT_NAME);

		Assertions.assertTrue(response.isSuccessful());

		VaultObjectPageLayout layout = response.getData().get(0);
		Assertions.assertNotNull(layout);

		if (layout != null) {
			Assertions.assertNotNull(layout.getName());
		}
	}
}
