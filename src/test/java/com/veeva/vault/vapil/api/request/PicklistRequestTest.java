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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag("PicklistRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Picklist Request should")
public class PicklistRequestTest {

	private static final String PICKLIST_NAME = "vapil_test_picklist__c";
	private static String picklistValueName;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully retrieve all picklists")
	public void testRetrieveAllPicklists() {
		PicklistResponse response = vaultClient.newRequest(PicklistRequest.class).retrieveAllPicklists();

		Assertions.assertTrue(response.isSuccessful());
		for (PicklistResponse.Picklist pickList : response.getPicklists()) {
			Assertions.assertNotNull(pickList.getName());
			Assertions.assertNotNull(pickList.getLabel());
			Assertions.assertNotNull(pickList.getKind());
		}
	}

	@Test
	@Order(2)
	@DisplayName("successfully create a picklist value")
	public void testCreatePicklistValues() {
		List<String> newPicklistValues = new ArrayList<>();
		newPicklistValues.add("VAPIL Test Value_" + System.currentTimeMillis());

		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class)
				.createPicklistValues(PICKLIST_NAME, newPicklistValues);
		Assertions.assertTrue(response.isSuccessful());

		List<PicklistValueResponse.PicklistValue> picklistValuesList = response.getPicklistValues();
		Assertions.assertNotNull(picklistValuesList);

		Assertions.assertNotNull(picklistValuesList.get(0).getName());
		picklistValueName = picklistValuesList.get(0).getName();

		Assertions.assertNotNull(picklistValuesList.get(0).getLabel());
	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve all picklist values for a specified picklist")
	public void testRetrievePicklistValues() {
		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class)
				.retrievePicklistValues(PICKLIST_NAME);

		Assertions.assertTrue(response.isSuccessful());

		List<PicklistValueResponse.PicklistValue> picklistValues = response.getPicklistValues();
		Assertions.assertNotNull(picklistValues);
		picklistValues.forEach(picklistValue -> {
			Assertions.assertNotNull(picklistValue.getName());
			Assertions.assertNotNull(picklistValue.getLabel());
		});
	}

	@Test
	@Order(4)
	@DisplayName("successfully update a picklist value label")
	public void testUpdatePicklistValueLabel() throws InterruptedException {
		Map<String,String> newPicklistLabels = new HashMap<>();
		newPicklistLabels.put(picklistValueName, "VAPIL Test Value Updated");

		PicklistValueResponse response = vaultClient.newRequest(PicklistRequest.class)
				.updatePicklistValueLabel(PICKLIST_NAME, newPicklistLabels);

		Assertions.assertTrue(response.isSuccessful());

		List<PicklistValueResponse.PicklistValue> picklistValuesList = response.getPicklistValues();
		Assertions.assertNotNull(picklistValuesList);
		Assertions.assertNotNull(picklistValuesList.get(0).getName());

		Assertions.assertNotNull(picklistValuesList.get(0).getLabel());
	}

	@Test
	@Order(5)
	@DisplayName("successfully update a picklist value name")
	public void testUpdatePicklistValueName() throws InterruptedException {
		String updatedPicklistValueName = picklistValueName.replace("value", "value_updated").replace("__c", "");
		VaultResponse response = vaultClient.newRequest(PicklistRequest.class)
				.updatePicklistValue(PICKLIST_NAME, picklistValueName, updatedPicklistValueName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
		picklistValueName = updatedPicklistValueName;
		picklistValueName += "__c";
	}

	@Test
	@Order(6)
	@DisplayName("successfully inactivate a picklist value")
	public void testInactivatePicklistValue() {
		VaultResponse response = vaultClient.newRequest(PicklistRequest.class)
				.inactivatePicklistValue(PICKLIST_NAME, picklistValueName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponse());
	}
}
