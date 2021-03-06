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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("MetaDataRequestObject")
@ExtendWith(VaultClientParameterResolver.class)
public class MetaDataRequestObjectTest {

	@Test
	public void testGetAllObjects(VaultClient vaultClient) {
		MetaDataObjectBulkResponse response = vaultClient.newRequest(MetaDataRequest.class).retrieveObjectCollection();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getObjects());
	}

	@Test
	public void testGetObject(VaultClient vaultClient) {
		MetaDataObjectResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveObjectMetadata("user__sys");
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
	public void testGetObjectField(VaultClient vaultClient) {
		MetaDataObjectFieldResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrieveObjectFieldMetaData("user__sys", "preferred_currency__sys");
		Assertions.assertTrue(response.isSuccessful());

		VaultObjectField fieldMetaData = response.getField();
		Assertions.assertNotNull(fieldMetaData);
		
		if (fieldMetaData.getObjectReference() != null) {
			Assertions.assertNotNull(fieldMetaData.getObjectReference().getUrl());
		}
	}

	@Test
	public void testGetObjectPageLayouts(VaultClient vaultClient) {
		MetaDataObjectPageLayoutResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrievePageLayouts("test_object__c");

		Assertions.assertTrue(response.isSuccessful());

		VaultObjectPageLayout layout = response.getData().get(0);
		Assertions.assertNotNull(layout);

		if (layout != null) {
			Assertions.assertNotNull(layout.getName());
		}
	}

	@Test
	public void testGetObjectPageLayoutMetadata(VaultClient vaultClient) {
		MetaDataObjectPageLayoutResponse response = vaultClient.newRequest(MetaDataRequest.class)
				.retrievePageLayoutMetadata("test_object__c", "test_object_detail_page_layout__c");

		Assertions.assertTrue(response.isSuccessful());

		VaultObjectPageLayout layout = response.getData().get(0);
		Assertions.assertNotNull(layout);

		if (layout != null) {
			Assertions.assertNotNull(layout.getName());
		}
	}
}
