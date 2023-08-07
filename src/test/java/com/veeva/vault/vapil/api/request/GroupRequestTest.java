/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.metadata.Group;
import com.veeva.vault.vapil.api.model.response.GroupResponse;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveAutoManagedResponse;
import com.veeva.vault.vapil.api.model.response.MetaDataGroupResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


import java.util.List;
import java.util.Random;

@Tag("GroupRequest")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class GroupRequestTest {

	@Test
	public void testRetrieveGroupMetaData(VaultClient vaultClient) {
		MetaDataGroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.retrieveGroupMetadata();

		Assertions.assertTrue(response.isSuccessful());
		List<Group> allGroupMetaData = response.getProperties();
		Assertions.assertNotNull(allGroupMetaData);
		Assertions.assertFalse(allGroupMetaData.isEmpty());
	}

	@Test
	public void testRetrieveAllGroups(VaultClient vaultClient) {
		GroupRetrieveResponse response = vaultClient.newRequest(GroupRequest.class)
				.setIncludeImplied(true)
				.retrieveAllGroups();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveAllGroupsAutoManaged(VaultClient vaultClient) {
		GroupRetrieveAutoManagedResponse response = vaultClient.newRequest(GroupRequest.class)
				.retrieveAllGroupsAutoManaged();
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveGroup(VaultClient vaultClient) {
		GroupRetrieveResponse response = vaultClient.newRequest(GroupRequest.class)
				.setIncludeImplied(true)
				.retrieveGroup(1); // All Internal Users
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testCreateGroup(VaultClient vaultClient) {
		String label = "testGroup" + getRandomNumberAsString(1, 100, 3);
		GroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.createGroup(label);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());
	}

	@Nested
	@DisplayName("Tests that depend on an existing group")
	class TestGroupUpdateAndDelete {
		Long groupId;

		@BeforeEach
		public void beforeEach(VaultClient vaultClient){
			String label = "testGroup" + getRandomNumberAsString(1, 100, 3);
			GroupResponse response = vaultClient.newRequest(GroupRequest.class)
					.createGroup(label);
			Assertions.assertTrue(response.isSuccessful());
			groupId = response.getId();
		}

		@Test
		public void testUpdateGroup(VaultClient vaultClient) {
			GroupResponse response = vaultClient.newRequest(GroupRequest.class)
					.setLabel("testGroup" + groupId)
					.setActive(false)
					.setGroupDescription("Description " + groupId)
					.setAllowDelegationAmongMembers(true)
					.updateGroup(groupId);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getId());
		}

		@Test
		public void testDeleteGroup(VaultClient vaultClient) {
			GroupResponse response = vaultClient.newRequest(GroupRequest.class).deleteGroup(groupId);
			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getId());
		}

	}

	public String getRandomNumberAsString(int min, int max, int paddedNumLength) {
		Random rnd = new Random();
		int rndInt = rnd.nextInt((max - min) + 1) + min;
		return String.format("%0" + paddedNumLength + "d", rndInt);
	}
}
