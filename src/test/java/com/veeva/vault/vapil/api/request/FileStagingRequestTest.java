/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.request.FileStagingRequest.Kind;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.FileHelper;
import com.veeva.vault.vapil.extension.JobStatusHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Tag("FileStagingRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("File Staging request should")
public class FileStagingRequestTest {
	static final String RESOURCES_FOLDER_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
	static final String TEST_FOLDER_FSS_NAME = "TestCreateFolder";
	static final String TEST_UPDATE_FOLDER_FSS_NAME = "TestUpdateFolder";
	static final String TEST_FILE_FSS_NAME = "vapil_test_document.docx";
	static final String TEST_FILE_LOCAL_NAME = "test_create_file.txt";
	static final String TEST_FILE_LOCAL_PATH = FileHelper.getPathTestFile();
	static final String TEST_DOWNLOAD_FILE_NAME = "test_download_file.txt";
	static final String TEST_UPDATE_FILE_NAME = "test_update_file.txt";
	static final String TEST_RESUMABLE_UPLOAD_FILE_NAME = "test_resumable_upload.txt";
	static final String TEST_RESUMABLE_UPLOAD_FILE_PATH = RESOURCES_FOLDER_PATH + TEST_RESUMABLE_UPLOAD_FILE_NAME;
	static String resumableUploadSessionId;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully create a folder at the specified path")
	public void testCreateFolder() {
		FileStagingItemResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createFolderOrFile(Kind.FOLDER, "/" + TEST_FOLDER_FSS_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getKind());
		Assertions.assertNotNull(resp.getData().getPath());
		Assertions.assertNotNull(resp.getData().getName());
	}

	@Test
	@Order(2)
	@DisplayName("successfully create a file at the specified path")
	public void testCreateFile() throws IOException {
		File testFile = new File(TEST_FILE_LOCAL_PATH);
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingItemResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.setFile(testFile.getPath(), bytes)
				.createFolderOrFile(Kind.FILE, "/" + TEST_FOLDER_FSS_NAME + "/" + TEST_FILE_LOCAL_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getKind());
		Assertions.assertNotNull(resp.getData().getPath());
		Assertions.assertNotNull(resp.getData().getName());
	}


	@Test
	@Order(3)
	@DisplayName("successfully retrieve a list of files and folders for the specified path")
	public void testListItemsAtAPath() {
		FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.listItemsAtAPath("/" + TEST_FOLDER_FSS_NAME);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotEquals(0, response.getData().size());

	}

	@Test
	@Order(4)
	@DisplayName("successfully retrieve a list of files, folders, and a next_page url for the specified path")
	public void testListItemsByPage() {
		FileStagingItemBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.setLimit(1)
				.setRecursive(true)
				.listItemsAtAPath("/");

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getData());
		Assertions.assertNotEquals(0, response.getData().size());

		if (response.isPaginated()) {
			FileStagingItemBulkResponse paginatedResponse = vaultClient.newRequest(FileStagingRequest.class)
					.listItemsAtPathByPage(response.getResponseDetails().getNextPage());

			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	@Order(5)
	@DisplayName("successfully download bytes from a file at the specified path")
	public void testDownloadItemContentBinary() throws IOException {
		VaultResponse downloadResp = vaultClient.newRequest(FileStagingRequest.class)
				.downloadItemContent("/" + TEST_FOLDER_FSS_NAME + "/" + TEST_FILE_LOCAL_NAME);

		Assertions.assertTrue(downloadResp.isSuccessful());
		Assertions.assertNotNull(downloadResp.getBinaryContent());
		byte[] byteArray = downloadResp.getBinaryContent();
		String string = new String(byteArray, StandardCharsets.UTF_8);
		System.out.println("Bytes : " + string);
	}

	@Test
	@Order(6)
	@DisplayName("successfully download a file at the specified path to a local path")
	public void testDownloadItemContentToFile() throws IOException {
		VaultResponse downloadResp = vaultClient.newRequest(FileStagingRequest.class)
				.setOutputPath(RESOURCES_FOLDER_PATH + TEST_DOWNLOAD_FILE_NAME)
				.downloadItemContent("/" + TEST_FOLDER_FSS_NAME + "/" + TEST_FILE_LOCAL_NAME);

		Assertions.assertTrue(downloadResp.isSuccessful());
	}

	@Test
	@Order(7)
	@DisplayName("successfully create a resumable upload session")
	public void testCreateResumableUploadSession() {
		File resumableUploadFile = new File(RESOURCES_FOLDER_PATH + TEST_RESUMABLE_UPLOAD_FILE_NAME);
		int fileSize = (int) resumableUploadFile.length();

		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/" + TEST_FOLDER_FSS_NAME + "/" + TEST_RESUMABLE_UPLOAD_FILE_NAME, fileSize);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
		resumableUploadSessionId = resp.getData().getId();
	}

	@Test
	@Order(8)
	@DisplayName("successfully list active resumable upload sessions")
	public void testListUploadSessions() throws InterruptedException {
		FileStagingSessionBulkResponse response = vaultClient.newRequest(FileStagingRequest.class)
				.listUploadSessions();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotEquals(0, response.getData().size());

		for(FileStagingSessionBulkResponse.FileStagingSession fileStagingSession : response.getData()) {
			Assertions.assertNotNull(fileStagingSession.getId());
		}

		if (response.isPaginated()) {
			FileStagingSessionBulkResponse paginatedResponse = vaultClient.newRequest(FileStagingRequest.class)
					.listUploadSessionsByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	@Order(9)
	@DisplayName("successfully retrieve details of an active upload session")
	public void testGetUploadSessionDetails() throws InterruptedException {
		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.getUploadSessionDetails(resumableUploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getId());
		Assertions.assertNotNull(resp.getData().getPath());
	}

	@Test
	@Order(10)
	@DisplayName("successfully upload a part to an active upload session")
	public void testUploadToASession() throws IOException, InterruptedException {
		File testFile = new File(TEST_RESUMABLE_UPLOAD_FILE_PATH);
		byte[] bytes = Files.readAllBytes(testFile.toPath());

		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setFile(testFile.getName(), bytes)
				.uploadToASession(resumableUploadSessionId, 1);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotNull(resp.getData().getSize());
	}

	@Test
	@Order(11)
	@DisplayName("successfully list file parts uploaded to an active upload session")
	public void testListFilePartsUploadedToASession() throws InterruptedException, IOException {
		FileStagingSessionBulkResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setLimit(1)
				.listFilePartsUploadedToASession(resumableUploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertNotEquals(0, resp.getData().size());
	}

	@Test
	@Order(12)
	@DisplayName("successfully commit an active upload session")
	public void testCommitUploadSession() throws IOException, InterruptedException {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.commitUploadSession(resumableUploadSessionId);
		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
	}

	@Test
	@Order(13)
	@DisplayName("successfully abort an active upload session")
	public void testAbortUploadSession() throws InterruptedException {
		File resumableUploadFile = new File(RESOURCES_FOLDER_PATH + TEST_RESUMABLE_UPLOAD_FILE_NAME);
		int fileSize = (int) resumableUploadFile.length();

		FileStagingSessionResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setOverwrite(true)
				.createResumableUploadSession("/" + TEST_FOLDER_FSS_NAME + "/" + TEST_RESUMABLE_UPLOAD_FILE_NAME, fileSize);

		Assertions.assertNotNull(resp.getData().getId());

		String uploadSessionId = resp.getData().getId();

//		Abort session
		VaultResponse abortResponse = vaultClient.newRequest(FileStagingRequest.class)
				.abortUploadSession(uploadSessionId);

		Assertions.assertTrue(abortResponse.isSuccessful());
	}

	@Test
	@Order(14)
	@DisplayName("successfully update a file name at the specified path")
	public void testUpdateFile() throws IOException {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setName(TEST_UPDATE_FILE_NAME)
				.updateFolderOrFile("/" + TEST_FOLDER_FSS_NAME + "/" + TEST_FILE_LOCAL_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
	}

	@Test
	@Order(15)
	@DisplayName("successfully update a folder name at the specified path")
	public void testUpdateFolder() throws IOException {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setName(TEST_UPDATE_FOLDER_FSS_NAME)
				.updateFolderOrFile("/" + TEST_FOLDER_FSS_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
	}

	@Test
	@Order(16)
	@DisplayName("successfully delete a file at the specified path")
	public void testDeleteFile() throws InterruptedException {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.deleteFolderOrFile("/" + TEST_UPDATE_FOLDER_FSS_NAME + "/" + TEST_UPDATE_FILE_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
	}

	@Test
	@Order(17)
	@DisplayName("successfully delete a folder at the specified path")
	public void testDeleteFolder() throws InterruptedException {
		FileStagingJobResponse resp = vaultClient.newRequest(FileStagingRequest.class)
				.setRecursive(true)
				.deleteFolderOrFile("/" + TEST_UPDATE_FOLDER_FSS_NAME);

		Assertions.assertTrue(resp.isSuccessful());
		Assertions.assertTrue(JobStatusHelper.checkJobCompletion(vaultClient, resp.getData().getJobId()));
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully create a resumable upload session")
	class TestCreateResumableUploadSession {
		FileStagingSessionResponse createResumableUploadSessionResponse = null;

		@AfterAll
		public void teardown() {
			VaultResponse response = vaultClient.newRequest(FileStagingRequest.class)
					.abortUploadSession(createResumableUploadSessionResponse.getData().getId());

			assertNotNull(response);
			assertTrue(response.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			String filePath = FileHelper.getPathResumableUploadFile();
			long fileSize = new File(filePath).length();
			FileStagingSessionResponse response = vaultClient.newRequest(FileStagingRequest.class)
					.setOverwrite(true)
					.createResumableUploadSession(TEST_FILE_FSS_NAME, fileSize);
			assertNotNull(response);
			createResumableUploadSessionResponse = response;
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(createResumableUploadSessionResponse.isSuccessful());
			assertNotNull(createResumableUploadSessionResponse.getData());
			assertNotNull(createResumableUploadSessionResponse.getData().getId());
			assertEquals(String.format("/%s", TEST_FILE_FSS_NAME), createResumableUploadSessionResponse.getData().getPath());
			assertEquals(TEST_FILE_FSS_NAME, createResumableUploadSessionResponse.getData().getName());
			assertNotNull(createResumableUploadSessionResponse.getData().getSize());
			assertNotNull(createResumableUploadSessionResponse.getData().getUploadedParts());
			assertNotNull(createResumableUploadSessionResponse.getData().getUploadedParts());
			assertNotNull(createResumableUploadSessionResponse.getData().getUploaded());
			assertNotNull(createResumableUploadSessionResponse.getData().getCreatedDate());
			assertNotNull(createResumableUploadSessionResponse.getData().getExpirationDate());
			assertNotNull(createResumableUploadSessionResponse.getData().getLastUploadedDate());
			assertNotNull(createResumableUploadSessionResponse.getData().getOverwrite());
			assertNotNull(createResumableUploadSessionResponse.getData().getOwner());
		}
	}

}
