/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.TestProperties;
import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.request.FileStagingRequest.Kind;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Tag("FileStaging")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileStagingRequestTest {

	static final String RESOURCES_FOLDER_PATH = "src\\test\\resources\\";

	@Test
	public void testListItemsAtAPath(VaultClient vaultClient) {
		FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.listItemsAtAPath("/");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotEquals(0, response.getData().size());
		System.out.println(response.getResponse());

	}

	@Test
	public void testListItemsByPage(VaultClient vaultClient) {
		FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.setLimit(2)
				.setRecursive(true)
				.listItemsAtAPath("/");
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotEquals(0, response.getData().size());
		System.out.println(response.getResponse());

		if (response.isPaginated()) {
			FileStagingItemBulkResponse paginatedResponse = vaultClient.newRequest(FileStagingRequest.class)
					.listItemsAtPathByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	public void testDownloadItemContentBinary(VaultClient vaultClient) throws IOException {

//		Create file
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(Kind.FILE, "test_download_bytes_file.txt");

//		Download file
		VaultResponse downloadResp = vaultClient.newRequest(FileStagingRequest.class)
				.downloadItemContent("/test_download_bytes_file.txt");
		Assertions.assertTrue(downloadResp.isSuccessful());
		Assertions.assertNotNull(downloadResp.getBinaryContent());
		byte[] byteArray = downloadResp.getBinaryContent();
		String string = new String(byteArray, StandardCharsets.UTF_8);
		System.out.println("Bytes : " + string);
	}

	@Test
	public void testDownloadItemContentToFile(VaultClient vaultClient) throws IOException {

//		Create file
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(Kind.FILE, "test_download_file.txt");

//		Download to file
		VaultResponse downloadResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOutputPath(RESOURCES_FOLDER_PATH + "test_download_file.txt")
				.downloadItemContent("/test_download_file.txt");
		Assertions.assertTrue(downloadResp.isSuccessful());
	}

	@Test
	public void testCreateFolder(VaultClient vaultClient) {
		FileStagingItemResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createFolderOrFile(Kind.FOLDER, "/TestCreateFolder");
		Assertions.assertTrue(resp.isSuccessful());
	}

	@Test
	public void testCreateFile(VaultClient vaultClient) throws IOException {
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(Kind.FILE, "test_create_file.txt");

		Assertions.assertTrue(resp.isSuccessful());
	}

	@Test
	public void testUpdateFileOrFolder(VaultClient vaultClient) throws IOException {

//		Create file
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_create_file.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(Kind.FILE, "test_update_file.txt");

//		Update file
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setName("test_update_filex.txt")
				.updateFolderOrFile("/test_update_file.txt");

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getJobId());
	}

	@Test
	public void testDeleteFileOrFolder(VaultClient vaultClient) throws InterruptedException {

//		Create folder
		FileStagingItemResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createFolderOrFile(Kind.FOLDER, "TestDeleteFolder");

		Thread.sleep(2000);

//		Delete folder
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setRecursive(true)
				.deleteFolderOrFile("/TestDeleteFolder");
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getJobId());
	}

	@Test
	public void testCreateResumableUploadSession(VaultClient vaultClient) {
		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_create_resumable_upload_session.txt", 51130017);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
		System.out.println("Resumable Upload ID: " + resp.getData().getId());
	}

	@Test
	public void testUploadToASession(VaultClient vaultClient) throws IOException, InterruptedException {

//		Create session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_upload_to_a_session.txt", 51130017);

		String uploadSessionId = createResp.getData().getId();

		Thread.sleep(5000);

//		Upload to session
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_resumable_upload.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setFile(testFile.getName(), bytes)
				.uploadToASession(uploadSessionId, 1);

		Assertions.assertTrue(resp.isSuccessful());
		System.out.println(resp.getData().toJsonString());
	}

	@Test
	public void testCommitUploadSession(VaultClient vaultClient) throws IOException, InterruptedException {

//		Create session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_commit_upload_session.txt", 51130017);

		String uploadSessionId = createResp.getData().getId();

		Thread.sleep(5000);

//		Upload to Session
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_resumable_upload.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingSessionResponse uploadResp = vaultClient.newRequest(FileStagingRequest.class)
				.setFile(testFile.getName(), bytes)
				.uploadToASession(uploadSessionId, 1);

		Thread.sleep(5000);

//		Commit session
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.commitUploadSession(uploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		System.out.println(resp.getData().getJobId());
	}

	@Test
	public void testListUploadSessions(VaultClient vaultClient) throws InterruptedException {

//		Create session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_list_upload_sessions.txt", 51130017);

		Thread.sleep(5000);

//		List upload sessions
		FileStagingSessionBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.listUploadSessions();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotEquals(0, response.getData().size());
		for(FileStagingSessionBulkResponse.FileStagingSession fileStagingSession : response.getData()) {
			System.out.println(fileStagingSession.getName());
		}

		if (response.isPaginated()) {
			FileStagingSessionBulkResponse paginatedResponse = vaultClient.newRequest(FileStagingRequest.class)
					.listUploadSessionsByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	public void testGetUploadSessionDetails(VaultClient vaultClient) throws InterruptedException {

//		Create session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_get_upload_session_details.txt", 51130017);

		String uploadSessionId = createResp.getData().getId();

		Thread.sleep(5000);

//		Get upload session details
		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.getUploadSessionDetails(uploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
	}

	@Test
	public void testListFilePartsUploadedToASession(VaultClient vaultClient) throws InterruptedException, IOException {
//		Create Session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_list_file_parts_uploaded_to_a_session.txt", 51130017);

		String uploadSessionId = createResp.getData().getId();

		Thread.sleep(5000);

//		Upload to session
		File testFile = new File(RESOURCES_FOLDER_PATH + "test_resumable_upload.txt");
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingSessionResponse uploadResp = vaultClient.newRequest(FileStagingRequest.class)
				.setFile(testFile.getName(), bytes)
				.uploadToASession(uploadSessionId, 1);

		Thread.sleep(5000);

//		List uploaded file parts
		FileStagingSessionBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setLimit(1)
				.listFilePartsUploadedToASession(uploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotEquals(0, resp.getData().size());

		if (resp.getResponseDetails() != null)
			System.out.println(resp.getResponseDetails().getNextPage());
	}


	@Test
	public void testAbortUploadSession(VaultClient vaultClient) throws InterruptedException {
//		Create session
		FileStagingSessionResponse createResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/test_abort_upload_session.txt", 51130017);

		String uploadSessionId = createResp.getData().getId();

		Thread.sleep(5000);

//		Abort session
		VaultResponse abortResponse = vaultClient.newRequest(FileStagingRequest.class)
				.abortUploadSession(uploadSessionId);
		Assertions.assertTrue(abortResponse.isSuccessful());
	}

}
