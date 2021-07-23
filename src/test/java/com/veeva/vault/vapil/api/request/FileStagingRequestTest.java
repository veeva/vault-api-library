/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.request.FileStagingRequest.Kind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Tag("FileStaging")
@ExtendWith(VaultClientParameterResolver.class)
public class FileStagingRequestTest {

	@Test
	public void testCustom(VaultClient vaultClient) {
		FileStagingRequest request = vaultClient.newRequest(FileStagingRequest.class);

	}

	@Test
	public void testListItemsAtAPath(VaultClient vaultClient) {
		FileStagingItemBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class).listItemsAtAPath("/");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData());
		Assertions.assertNotEquals(0, resp.getData().size());
		System.out.println(resp.getResponse());
	}

	@Test
	public void testGetItemContentBinary(VaultClient vaultClient) {
		VaultResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.getItemContent("/Test/upload.txt");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getBinaryContent());
	}

	@Test
	public void testGetItemContentToFile(VaultClient vaultClient) {
		TestProperties testProperties = new TestProperties();
		VaultResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOutputPath(testProperties.getTestFile())
				.getItemContent("/Test/upload.txt");
		Assertions.assertTrue(resp.isSuccessful());
	}

	@Test
	public void testCreateFolder(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
		fileStagingRequest.setOverwrite(true);

		FileStagingItemResponse resp = fileStagingRequest.createFolderOrFile(Kind.FOLDER, "/Test");
		Assertions.assertTrue(resp.isSuccessful());
	}

	@Test
	public void testCreateFile(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
		fileStagingRequest.setSize(new Integer(14));
		fileStagingRequest.setOverwrite(true);

		TestProperties testProperties = new TestProperties();
		File file = new File(testProperties.getTestFile());
		try {
			fileStagingRequest.setFile(file.getName(), Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileStagingItemResponse resp = fileStagingRequest.createFolderOrFile(Kind.FILE, "/Test/upload.txt");
		Assertions.assertTrue(resp.isSuccessful());
	}

	@Test
	public void testUpdateFileOrFolder(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
		fileStagingRequest.setName("change.txt");

		FileStagingJobResponse resp = fileStagingRequest.updateFolderOrFile("/Test/upload.txt");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getJobId());
	}

	@Test
	public void testDeleteFileOrFolder(VaultClient vaultClient) {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class).deleteFolderOrFile("/Test/change.txt");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getJobId());
	}

	@Test
	public void testCreateResumableUploadSession(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
		fileStagingRequest.setOverwrite(true);

		FileStagingSessionResponse resp = fileStagingRequest.createResumableUploadSession("/Test/verteobiotech.pdf", 14789842);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
		System.out.println(resp.getData().getId());
	}

	@Test
	public void testUploadToASession(VaultClient vaultClient) {
		TestProperties testProperties = new TestProperties();
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);
		File file = new File(testProperties.getTestFile());

		try {
			fileStagingRequest.setFile(file.getName(), Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileStagingSessionResponse resp = fileStagingRequest.uploadToASession("caa075c08839bcbaab736cb6bd737969", 1);
		Assertions.assertTrue(resp.isSuccessful());
		System.out.println(resp.getData().toJsonString());
	}

	@Test
	public void testCommitUploadSession(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);

		FileStagingJobResponse resp = fileStagingRequest.commitUploadSession("caa075c08839bcbaab736cb6bd737969");
		Assertions.assertTrue(resp.isSuccessful());
		System.out.println(resp.getData().getJobId());
	}

	@Test
	public void testListUploadSessions(VaultClient vaultClient) {
		FileStagingSessionBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class).listUploadSessions();
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotEquals(0, resp.getData().size());
		for(FileStagingSessionBulkResponse.FileStagingSession fileStagingSession : resp.getData()) {
			System.out.println(fileStagingSession.getName());
		}
	}

	@Test
	public void testGetUploadSessionDetails(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);

		FileStagingSessionResponse resp = fileStagingRequest.getUploadSessionDetails("caa075c08839bcbaab736cb6bd737969");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
	}

	@Test
	public void testListFilePartsUploadedToASession(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);

		FileStagingSessionBulkResponse resp = fileStagingRequest
				.setLimit(1)
				.listFilePartsUploadedToASession("caa075c08839bcbaab736cb6bd737969");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotEquals(0, resp.getData().size());

		if (resp.getResponseDetails() != null)
			System.out.println(resp.getResponseDetails().getNextPage());
	}


	@Test
	public void testAbortUploadSession(VaultClient vaultClient) {
		FileStagingRequest fileStagingRequest = vaultClient.newRequest(FileStagingRequest.class);

		VaultResponse resp = fileStagingRequest.abortUploadSession("caa075c08839bcbaab736cb6bd737969");
		Assertions.assertTrue(resp.isSuccessful());
	}

}
