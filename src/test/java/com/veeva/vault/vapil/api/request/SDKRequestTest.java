/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.QueueResponse;
import com.veeva.vault.vapil.api.model.response.SDKResponse;
import com.veeva.vault.vapil.api.model.response.ValidatePackageResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Tag("SDKRequest")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class SDKRequestTest {

		final String CLASS_NAME = "com.veeva.vault.custom.triggers.HelloWorld";
		final String SDK_FILE_NAME = "";
		final String FILE_PATH = "";
		final String PACKAGE_FILE_NAME = "";
		final String PACKAGE_ID = "";
		final String CERT_FILE_NAME = "";

	@Test
	public void testRetrieveSDKBinary(VaultClient vaultClient) {
		SDKResponse response = vaultClient.newRequest(SDKRequest.class)
				.retrieveSingleSourceCodeFile(CLASS_NAME);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getBinaryContent());
	}

	@Test
	public void testRetrieveSDKFile(VaultClient vaultClient) {
		SDKResponse response = vaultClient.newRequest(SDKRequest.class)
				.setInputPath("SDKFile")
				.retrieveSingleSourceCodeFile(CLASS_NAME);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void disableSDK(VaultClient vaultClient) {
		SDKResponse response = vaultClient.newRequest(SDKRequest.class)
				.disableVaultExtension(CLASS_NAME);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void enableSDK(VaultClient vaultClient) {
		SDKResponse response = vaultClient.newRequest(SDKRequest.class)
				.enableVaultExtension(CLASS_NAME);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void addSDKBinary(VaultClient vaultClient) {
		
		File sdkFile = new File(FILE_PATH);
		SDKResponse response = null;
		try {
			response = vaultClient.newRequest(SDKRequest.class)
					.setBinaryFile(sdkFile.getName(), Files.readAllBytes(sdkFile.toPath()))
					.addOrReplaceSingleSourceCodeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getUrl());
	}

	@Test
	public void addSDKFile(VaultClient vaultClient) {
		
		SDKResponse response = vaultClient.newRequest(SDKRequest.class)
				.setInputPath(SDK_FILE_NAME)
				.addOrReplaceSingleSourceCodeFile();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getUrl());
	}

	@Test
	public void deleteSDK(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(SDKRequest.class)
				.deleteSingleSourceCodeFile(CLASS_NAME);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void validatePackageLocal(VaultClient vaultClient) {
		ValidatePackageResponse response = vaultClient.newRequest(SDKRequest.class)
				.setInputPath(PACKAGE_FILE_NAME)
				.validatePackage();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponseDetails().getAuthor());
	}

	@Test
	public void validatePackageImported(VaultClient vaultClient) {
		ValidatePackageResponse response = vaultClient.newRequest(SDKRequest.class)
				.validateImportedPackage(PACKAGE_ID);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getResponseDetails().getAuthor());
	}

	@Test
	public void retrieveAllQueues(VaultClient vaultClient) {
		QueueResponse response = vaultClient.newRequest(SDKRequest.class)
				.retrieveAllQueues();
		Assertions.assertTrue(response.isSuccessful());
	}

//	@Nested
//	@DisplayName("Tests that depend on a queue")
//	class TestQueues {
//		String queueName;
//
//		@BeforeEach
//		public void beforeEach(VaultClient vaultClient) {
//			QueueResponse response = vaultClient.newRequest(SDKRequest.class)
//					.retrieveAllQueues();
//
//
//			if (response.isSuccessful()) {
//				for (QueueResponse.Queue queue : response.getData()) {
//					queueName = queue.getName();
//				}
//			}
//		}
//
//		@Test
//		public void testRetrieveQueueStatus(VaultClient vaultClient) {
//			if (!queueName.isEmpty()) {
//				QueueResponse response = vaultClient.newRequest(SDKRequest.class)
//						.retrieveQueueStatus(queueName);
//				Assertions.assertTrue(response.isSuccessful());
//			}
//		}
//
//		@Test
//		public void testEnableQueue(VaultClient vaultClient) {
//			VaultResponse response = vaultClient.newRequest(SDKRequest.class)
//				.enableDelivery(queueName);
//			Assertions.assertTrue(response.isSuccessful());
//		}
//		@Test
//		public void testResetQueue(VaultClient vaultClient) {
//			VaultResponse response = vaultClient.newRequest(SDKRequest.class)
//					.resetQueue(queueName);
//			Assertions.assertTrue(response.isSuccessful());
//		}
//	}

	@Test
	public void testRetrieveCert(VaultClient vaultClient) {
		VaultResponse response = vaultClient.newRequest(SDKRequest.class)
				.setOutputPath(CERT_FILE_NAME)
				.retrieveSigningCertificate("00001");
		Assertions.assertTrue(response.isSuccessful());
	}
}
