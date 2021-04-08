/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.PicklistResponse;
import com.veeva.vault.vapil.api.model.response.PicklistValueResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag("PicklistRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class PicklistRequestTest {

	@Test
	public void testRetrieveAllPicklists(VaultClient vaultClient) {
		PicklistResponse response = vaultClient.newRequest(PicklistRequest.class).retrieveAllPicklists();

		Assertions.assertTrue(response.isSuccessful());
		for (PicklistResponse.Picklist pickList : response.getPicklists()) {
			Assertions.assertNotNull(pickList.getName());
			Assertions.assertNotNull(pickList.getLabel());
			Assertions.assertNotNull(pickList.getKind());
		}
	}

	@Test
	public void testRetrievePicklistValue(VaultClient vaultClient) {
		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class).retrievePicklistValues("activity_type__v");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getPicklistValues());
	}

	@Test
	public void testCreatePicklistValues(VaultClient vaultClient) {
		String picklistName = "color__c";
		List<String> newPicklistValues = new ArrayList<>();
		newPicklistValues.add("White");
		newPicklistValues.add("Orange");
		newPicklistValues.add("Yellow");

		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class).createPicklistValues(picklistName, newPicklistValues);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}

	@Test
	public void testUpdatePicklistValueLabel(VaultClient vaultClient) {
		String picklistName = "color__c";
		Map<String,String> newPicklistLabels = new HashMap<>();
		newPicklistLabels.put("red__c", "Rouge");

		VaultResponse response = vaultClient.newRequest(PicklistRequest.class).updatePicklistValueLabel(picklistName, newPicklistLabels);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}

	@Test
	public void testUpdatePicklistValueName(VaultClient vaultClient) {
		String picklistName = "color__c";
		String picklistValueName = "red__c";
		String newName = "rouge";

		VaultResponse response = vaultClient.newRequest(PicklistRequest.class).updatePicklistValue(picklistName, picklistValueName, newName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}

	@Test
	public void testUpdatePicklistValueStatus(VaultClient vaultClient) {
		String picklistName = "color__c";
		String picklistValueName = "rouge__c";
		VaultResponse response = vaultClient.newRequest(PicklistRequest.class).updatePicklistValue(picklistName, picklistValueName, true);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}


	@Test
	public void testInactivatePicklistValue(VaultClient vaultClient) {
		String picklistName = "color__c";
		String picklistValueName = "rouge__c";
		VaultResponse response = vaultClient.newRequest(PicklistRequest.class).inactivatePicklistValue(picklistName, picklistValueName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}
}
