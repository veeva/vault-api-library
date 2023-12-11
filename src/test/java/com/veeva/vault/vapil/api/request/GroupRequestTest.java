/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.GroupResponse;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveResponse.*;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveAutoManagedResponse;
import com.veeva.vault.vapil.api.model.response.GroupRetrieveAutoManagedResponse.*;
import com.veeva.vault.vapil.api.model.response.MetaDataGroupResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.List;

@Tag("GroupRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Group Request should")
public class GroupRequestTest {

	private static VaultClient vaultClient;
	private static Long groupId;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@DisplayName("successfully retrieve group metadata")
	public void testRetrieveGroupMetaData() {
		MetaDataGroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.retrieveGroupMetadata();

		Assertions.assertTrue(response.isSuccessful());
		List<com.veeva.vault.vapil.api.model.metadata.Group> groups = response.getProperties();
		Assertions.assertNotNull(groups);

		for (com.veeva.vault.vapil.api.model.metadata.Group group : groups) {
			Assertions.assertNotNull(group.getName());
			Assertions.assertNotNull(group.getType());
			Assertions.assertNotNull(group.getLength());
			Assertions.assertNotNull(group.getEditable());
			Assertions.assertNotNull(group.getRequired());
			Assertions.assertNotNull(group.getMultivalue());
			Assertions.assertNotNull(group.getOnCreateEditable());
		}
	}

	@Test
	@DisplayName("successfully retrieve all groups except auto managed")
	public void testRetrieveAllGroups() {
		GroupRetrieveResponse response = vaultClient.newRequest(GroupRequest.class)
				.setIncludeImplied(true)
				.retrieveAllGroups();
		Assertions.assertTrue(response.isSuccessful());

		List<GroupNode> groups = response.getGroups();
		for (GroupNode groupNode : groups) {
			com.veeva.vault.vapil.api.model.common.Group group = groupNode.getGroup();
			Assertions.assertNotNull(group.getId());
			Assertions.assertNotNull(group.getName());
			Assertions.assertNotNull(group.getActive());
			Assertions.assertNotNull(group.getType());
			Assertions.assertNotNull(group.getEditable());
			Assertions.assertNotNull(group.getAllowDelegationAmongMembers());
			Assertions.assertNotNull(group.getSystemGroup());
			Assertions.assertNotNull(group.getCreatedBy());
			Assertions.assertNotNull(group.getCreatedDate());
			Assertions.assertNotNull(group.getModifiedBy());
			Assertions.assertNotNull(group.getModifiedDate());
		}
	}

	@Test
	@DisplayName("successfully retrieve all auto managed groups")
	public void testRetrieveAllGroupsAutoManaged() {
		GroupRetrieveAutoManagedResponse response = vaultClient.newRequest(GroupRequest.class)
				.retrieveAllGroupsAutoManaged();
		Assertions.assertTrue(response.isSuccessful());

		List<Data> groups = response.getData();
		for (Data data : groups) {
			com.veeva.vault.vapil.api.model.common.Group group = data.getGroup();
			Assertions.assertNotNull(group.getId());
			Assertions.assertNotNull(group.getName());
			Assertions.assertNotNull(group.getActive());
			Assertions.assertNotNull(group.getType());
			Assertions.assertNotNull(group.getEditable());
			Assertions.assertNotNull(group.getAllowDelegationAmongMembers());
			Assertions.assertNotNull(group.getSystemGroup());
			Assertions.assertNotNull(group.getCreatedBy());
			Assertions.assertNotNull(group.getCreatedDate());
			Assertions.assertNotNull(group.getModifiedBy());
			Assertions.assertNotNull(group.getModifiedDate());
		}
	}

	@Test
	@DisplayName("successfully retrieve a group by ID")
	public void testRetrieveGroup() {
		GroupRetrieveResponse response = vaultClient.newRequest(GroupRequest.class)
				.setIncludeImplied(true)
				.retrieveGroup(1); // All Internal Users
		Assertions.assertTrue(response.isSuccessful());

		List<GroupNode> groups = response.getGroups();
		for (GroupNode groupNode : groups) {
			com.veeva.vault.vapil.api.model.common.Group group = groupNode.getGroup();
			Assertions.assertNotNull(group.getId());
			Assertions.assertNotNull(group.getName());
			Assertions.assertNotNull(group.getActive());
			Assertions.assertNotNull(group.getType());
			Assertions.assertNotNull(group.getEditable());
			Assertions.assertNotNull(group.getAllowDelegationAmongMembers());
			Assertions.assertNotNull(group.getSystemGroup());
			Assertions.assertNotNull(group.getCreatedBy());
			Assertions.assertNotNull(group.getCreatedDate());
			Assertions.assertNotNull(group.getModifiedBy());
			Assertions.assertNotNull(group.getModifiedDate());
		}
	}

	@Test
	@Order(1)
	@DisplayName("successfully create a group")
	public void testCreateGroup() {
		String label = "VAPIL Unit Test Group";
		GroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.setGroupDescription("Test")
				.setActive(true)
				.setAllowDelegationAmongMembers(true)
				.createGroup(label);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());
		groupId = response.getId();
	}

	@Test
	@Order(2)
	@DisplayName("successfully update a group")
	public void testUpdateGroup() {
		GroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.setLabel("VAPIL Unit Test Group Updated")
				.setActive(false)
				.setGroupDescription("Test Description")
				.setAllowDelegationAmongMembers(false)
				.updateGroup(groupId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());
	}

	@Test
	@Order(3)
	@DisplayName("successfully delete a group")
	public void testDeleteGroup() {
		GroupResponse response = vaultClient.newRequest(GroupRequest.class)
				.deleteGroup(groupId);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getId());
	}

//	@Nested
//	@DisplayName("Tests that depend on an existing group")
//	class TestGroupUpdateAndDelete {
//		Long groupId;
//
//		@BeforeEach
//		public void beforeEach(){
//			String label = "testGroup" + getRandomNumberAsString(1, 100, 3);
//			GroupResponse response = vaultClient.newRequest(GroupRequest.class)
//					.createGroup(label);
//			Assertions.assertTrue(response.isSuccessful());
//			groupId = response.getId();
//		}
//
//		@Test
//		public void testUpdateGroup() {
//			GroupResponse response = vaultClient.newRequest(GroupRequest.class)
//					.setLabel("testGroup" + groupId)
//					.setActive(false)
//					.setGroupDescription("Description " + groupId)
//					.setAllowDelegationAmongMembers(true)
//					.updateGroup(groupId);
//
//			Assertions.assertTrue(response.isSuccessful());
//			Assertions.assertNotNull(response.getId());
//		}
//
//		@Test
//		public void testDeleteGroup() {
//			GroupResponse response = vaultClient.newRequest(GroupRequest.class).deleteGroup(groupId);
//			Assertions.assertTrue(response.isSuccessful());
//			Assertions.assertNotNull(response.getId());
//		}
//
//	}
//
//	public String getRandomNumberAsString(int min, int max, int paddedNumLength) {
//		Random rnd = new Random();
//		int rndInt = rnd.nextInt((max - min) + 1) + min;
//		return String.format("%0" + paddedNumLength + "d", rndInt);
//	}
}
