/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.ObjectRecordRoleChangeResponse;
import com.veeva.vault.vapil.api.model.response.ObjectRecordRoleResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

@Tag("ObjectRoleRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class ObjectRecordRoleRequestTest {

	@Test
	public void testAssignUsersAndGroupsToRolesOnObjectRecords(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String inputPath = "";
		ObjectRecordRoleChangeResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setInputPath(inputPath)
				.setContentTypeCsv()
				.assignUsersAndGroupsToRolesOnObjectRecords(objectName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
	
	@Test
	public void testAssignUsersAndGroupsToRolesOnObjectRecordsRequestString(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String requestString = "id,editor__v.users,editor__v.groups\nOOW000000000B03,\"1334729, 3467809\",\nOOW000000000704,\"1334729, 3467809 \",";

		ObjectRecordRoleChangeResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setRequestString(requestString)
				.setContentTypeCsv()
				.assignUsersAndGroupsToRolesOnObjectRecords(objectName);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
	
	@Test
	public void testRemoveUsersAndGroupsFromRolesOnObjectRecords(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String inputPath = "";
		ObjectRecordRoleChangeResponse response = vaultClient.newRequest(ObjectRecordRequest.class)
				.setInputPath(inputPath)
				.setContentTypeCsv()
				.removeUsersAndGroupsFromRolesOnObjectRecords(objectName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
	
	@Test
	public void testRetrieveObjectRecordRoles(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String recordId = "OOW000000000401";
		ObjectRecordRoleResponse response = vaultClient.newRequest(ObjectRecordRequest.class).retrieveObjectRecordRoles(objectName, recordId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
	
	@Test
	public void testRetrieveObjectRecordRole(VaultClient vaultClient) {
		String objectName = "tt_claim__c";
		String recordId = "OOW000000000401";
		String roleName = "editor__v";

		ObjectRecordRoleResponse response = vaultClient.newRequest(ObjectRecordRequest.class).retrieveObjectRecordRole(objectName, recordId, roleName);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
	}
}
