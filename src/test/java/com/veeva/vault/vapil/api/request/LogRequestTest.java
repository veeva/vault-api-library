/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.api.model.response.AuditMetadataResponse.AuditMetadata;
import com.veeva.vault.vapil.api.model.response.AuditTypesResponse.AuditTrail;
import com.veeva.vault.vapil.api.model.response.DocumentAuditResponse.DocumentAudit;
import com.veeva.vault.vapil.api.model.response.DomainAuditResponse.DomainAuditData;
import com.veeva.vault.vapil.api.model.response.LoginAuditResponse.LoginAuditData;
import com.veeva.vault.vapil.api.model.response.ObjectAuditResponse.ObjectAuditData;
import com.veeva.vault.vapil.api.model.response.SystemAuditResponse.SystemAuditData;
import com.veeva.vault.vapil.api.model.response.EmailNotificationHistoryResponse.EmailNotification;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Tag("LogRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Loq Request should")
public class LogRequestTest {

	private static Set<LogRequest.AuditTrailType> auditTrailTypes = new HashSet<>(Arrays.asList(
			LogRequest.AuditTrailType.DOCUMENT,
			LogRequest.AuditTrailType.DOMAIN,
			LogRequest.AuditTrailType.LOGIN,
			LogRequest.AuditTrailType.OBJECT,
			LogRequest.AuditTrailType.SYSTEM));

	private static String USER__SYS = "user__sys";
	private static String VAPIL_USER_ID = "17042915";
	private static int docId;
	private static VaultClient vaultClient;

	@BeforeAll
	public static void setup(VaultClient client) throws IOException {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());

		DocumentResponse response = DocumentRequestHelper.createSingleDocument(vaultClient);
		Assertions.assertTrue(response.isSuccessful());
		docId = response.getDocument().getId();
	}

	@AfterAll
	public static void teardown() {
		List<Integer> docIdList = Arrays.asList(docId);
		DocumentBulkResponse response = DocumentRequestHelper.deleteDocuments(vaultClient, docIdList);
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("successfully retrieve all available audit types you have permission to access")
	public void testRetrieveAuditTypes() {
		AuditTypesResponse response = vaultClient.newRequest(LogRequest.class)
				.retrieveAuditTypes();

		Assertions.assertTrue(response.isSuccessful());
		for (AuditTrail auditTrail : response.getAuditTrails()) {
			Assertions.assertNotNull(auditTrail.getName());
		}
	}

	@Test
	@DisplayName("successfully retrieve all fields and their metadata for a specified audit trail or log type.")
	public void testRetrieveAuditMetadata() {
		for (LogRequest.AuditTrailType auditTrailType : auditTrailTypes) {
			AuditMetadataResponse response = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(auditTrailType);

			Assertions.assertTrue(response.isSuccessful());
			AuditMetadata metadata = response.getData();
			Assertions.assertNotNull(metadata.getName());

			for (AuditMetadata.Field field : metadata.getFields()) {
				Assertions.assertNotNull(field.getName());
				Assertions.assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@DisplayName("successfully Retrieve audit details")
	class testRetrieveAuditDetails {
		@Test
		@DisplayName("for specific dates of audit type: Document")
		public void testRetrieveDocumentAuditDetails() {
			DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.setLimit(10)
					.setEvents(new HashSet<>(Arrays.asList("UploadDocBulk", "ExportBinder")))
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOCUMENT);

			Assertions.assertTrue(response.isSuccessful());
			AuditDetailsResponse.ResponseDetails auditDetails = response.getResponseDetails();
			Assertions.assertNotNull(auditDetails.getDetailsObject().getName());
			Assertions.assertNotNull(auditDetails.getDetailsObject().getUrl());

			for (DocumentAudit documentAuditData : response.getData()) {
				Assertions.assertNotNull(documentAuditData.getId());
				Assertions.assertNotNull(documentAuditData.getTimestamp());
			}

		}

		@Test
		@DisplayName("for specific dates of audit type: Domain")
		public void testRetrieveDomainAuditDetails() {
			DomainAuditResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.setLimit(4)
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);

			Assertions.assertTrue(response.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
			Assertions.assertNotNull(details.getDetailsObject().getName());
			Assertions.assertNotNull(details.getDetailsObject().getUrl());

			for (DomainAuditData data : response.getData()) {
				Assertions.assertNotNull(data.getId());
				Assertions.assertNotNull(data.getTimestamp());
			}
		}

		@Test
		@DisplayName("for specific dates of audit type: Login")
		public void testRetrieveLoginAuditDetails() {
			LoginAuditResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.retrieveAuditDetails(LogRequest.AuditTrailType.LOGIN);

			Assertions.assertTrue(response.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
			Assertions.assertNotNull(details.getDetailsObject().getName());
			Assertions.assertNotNull(details.getDetailsObject().getUrl());

			for (LoginAuditData data : response.getData()) {
				Assertions.assertNotNull(data.getId());
				Assertions.assertNotNull(data.getTimestamp());
				Assertions.assertNotNull(data.getUserName());
			}
		}

		@Test
		@DisplayName("for specific dates of audit type: Object")
		public void testRetrieveObjectAuditDetails() {
			ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(20))
					.setLimit(10)
					.setEvents(new HashSet<>(Arrays.asList("Create", "Update")))
					.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);

			Assertions.assertTrue(response.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
			Assertions.assertNotNull(details.getDetailsObject().getName());
			Assertions.assertNotNull(details.getDetailsObject().getUrl());

			for (ObjectAuditData data : response.getData()) {
				Assertions.assertNotNull(data.getId());
				Assertions.assertNotNull(data.getTimestamp());
				Assertions.assertNotNull(data.getRecordId());
			}

			if (response.isPaginated()) {
				ObjectAuditResponse paginatedResponse = vaultClient.newRequest(LogRequest.class)
						.retrieveAuditDetailsByPage(LogRequest.AuditTrailType.OBJECT,
								response.getResponseDetails().getNextPage());
				Assertions.assertTrue(paginatedResponse.isSuccessful());
			}
		}

		@Test
		@DisplayName("for specific dates of audit type: System")
		public void testRetrieveSystemAuditDetails() {
			SystemAuditResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.setLimit(10)
					.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);

			Assertions.assertTrue(response.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
			Assertions.assertNotNull(details.getDetailsObject().getName());
			Assertions.assertNotNull(details.getDetailsObject().getUrl());

			for (SystemAuditData data : response.getData()) {
				Assertions.assertNotNull(data.getId());
				Assertions.assertNotNull(data.getTimestamp());
				Assertions.assertNotNull(data.getAction());
			}

			if (response.isPaginated()) {
				SystemAuditResponse paginatedResponse = vaultClient.newRequest(LogRequest.class)
						.retrieveAuditDetailsByPage(LogRequest.AuditTrailType.SYSTEM,
								response.getResponseDetails().getNextPage());
				Assertions.assertTrue(paginatedResponse.isSuccessful());
			}
		}

		@Test
		@Disabled
		@DisplayName("for all dates of audit type: Object")
		public void testRetrieveObjectAuditDetailsAllDates() {
			JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getJobId());
			Assertions.assertNotNull(response.getUrl());
		}

		@Test
		@Disabled
		@DisplayName("for all dates of audit type: Domain")
		public void testRetrieveDomainAuditDetailsAllDates() {
			JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getJobId());
			Assertions.assertNotNull(response.getUrl());
		}

		@Test
		@Disabled
		@DisplayName("for all dates of audit type: System")
		public void testRetrieveSystemAuditDetailsAsCsv() {
			JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);

			Assertions.assertTrue(response.isSuccessful());
			Assertions.assertNotNull(response.getJobId());
			Assertions.assertNotNull(response.getUrl());
		}

	}

	@Test
	@DisplayName("successfully retrieve complete audit history for a single document.")
	public void testRetrieveCompleteAuditHistoryForASingleDocument() {
		DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setEvents(new HashSet<>(Arrays.asList("GetDocumentVersion", "UploadDoc")))
				.setLimit(4) // Just pull 4 records so the results can be viewed more easily
				.retrieveCompleteAuditHistoryForASingleDocument(docId);
		Assertions.assertTrue(response.isSuccessful());

		AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
		Assertions.assertNotNull(details.getDetailsObject().getName());
		Assertions.assertNotNull(details.getDetailsObject().getLabel());
		Assertions.assertNotNull(details.getDetailsObject().getUrl());

		for (DocumentAudit data : response.getData()) {
			Assertions.assertNotNull(data.getId());
			Assertions.assertNotNull(data.getAction());
			Assertions.assertNotNull(data.getDocumentUrl());
		}
	}

	@Test
	@DisplayName("successfully retrieve complete audit history for a single document as a CSV file.")
	public void testRetrieveCompleteAuditHistoryForASingleDocumentAsCsv() {
		DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveCompleteAuditHistoryForASingleDocument(docId);

		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	@DisplayName("successfully retrieve complete audit history for a single object record.")
	public void testRetrieveCompleteAuditHistoryForASingleObjectRecord() {
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setEvents(new HashSet<>(Arrays.asList("Create", "Edit")))
				.retrieveCompleteAuditHistoryForASingleObjectRecord(USER__SYS, VAPIL_USER_ID);

		Assertions.assertTrue(response.isSuccessful());
		AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
		Assertions.assertNotNull(details.getDetailsObject().getName());
		Assertions.assertNotNull(details.getDetailsObject().getLabel());
		Assertions.assertNotNull(details.getDetailsObject().getUrl());

		for (ObjectAuditData data : response.getData()) {
			Assertions.assertNotNull(data.getId());
			Assertions.assertNotNull(data.getUserName());
		}
	}

	@Test
	@DisplayName("successfully retrieve complete audit history for a single object record as a CSV file.")
	public void testRetrieveCompleteAuditHistoryForASingleObjectRecordAsCsv() {
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveCompleteAuditHistoryForASingleObjectRecord(USER__SYS, VAPIL_USER_ID);

		Assertions.assertTrue(response.isSuccessful());
		String results = new String(response.getBinaryContent());
		System.out.println(results);
	}


	@Nested
	@DisplayName("retrieve email notification history")
	class testRetrieveEmailNotificationHistory {
		@Test
		@DisplayName("with no query parameters successfully")
		void noQueryParameters() {
			EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
					.retrieveEmailNotificationHistory();

			assertTrue(response.isSuccessful());

			for (EmailNotification data : response.getData()) {
				assertNotNull(data.getNotificationId());
				assertNotNull(data.getSendDate());
			}
		}

		@Test
		@DisplayName("with invalid query parameters unsuccessfully")
		void invalidQueryParameters() {
			ZonedDateTime startDate = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29);

			EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(startDate)
					.retrieveEmailNotificationHistory();

			assertFalse(response.isSuccessful());

		}

		@Test
		@DisplayName("with start and end date/time query parameters successfully")
		void startAndEndDateTimeQueryParameters() {

			EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(30))
					.setEndDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
					.retrieveEmailNotificationHistory();

			assertTrue(response.isSuccessful());

			EmailNotificationHistoryResponse.ResponseDetails details = response.getResponseDetails();

			for (EmailNotification data : response.getData()) {
				assertNotNull(data.getNotificationId());
				assertNotNull(data.getSendDate());
			}
		}

		@Test
		@DisplayName("with start and end date query parameters successfully")
		void startAndEndDateQueryParameters() {

			EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
					.setStartDate(LocalDate.now().minusDays(30))
					.setEndDate(LocalDate.now().minusDays(1))
					.retrieveEmailNotificationHistory();

			assertTrue(response.isSuccessful());

			for (EmailNotification data : response.getData()) {
				assertNotNull(data.getNotificationId());
				assertNotNull(data.getSendDate());
			}
		}

		@Test
		@Disabled
		@DisplayName("with all_dates = true query parameter successfully")
		void allDatesEqualsTrueQueryParameters() {
//			This will only work once every 24 hours
			JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveEmailNotificationHistory();

			assertTrue(response.isSuccessful());
			assertNotNull(response.getJobId());

		}
	}


	@Test
	@DisplayName("successfully download the API Usage Log for a single day as a file")
	public void testRetrieveDailyAPIUsageToFile() {
		// Get yesterdays logs
		LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setOutputPath(outputFilePath.toString())
				.retrieveDailyAPIUsage(date);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getOutputFilePath());
	}

	@Test
	@DisplayName("successfully download the API Usage Log for a single day as bytes")
	public void testRetrieveDailyAPIUsageToBytes() {
		// Get yesterdays logs
		LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		// Retrieve the Zip file as bytes in the response
		// Be sure and call setOutputPath(null) here. This is a shared value and can be used in multiple reqs,
		// so safest to set to null in case another call set this value
		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setLogFormat(LogRequest.LogFormatType.LOGFILE)
				.setOutputPath(null)
				.retrieveDailyAPIUsage(date);

		Assertions.assertTrue(response.isSuccessful());
		if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
			try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
				os.write(response.getBinaryContent());
			}
			catch (IOException ignored){}
		}
	}

	@Test
	@DisplayName("successfully download the SDK Runtime Log for a single day as a file")
	public void testDownloadSdkRuntimeLogsToFile() {
		// Get yesterdays logs
		LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setOutputPath(outputFilePath.toString())
				.downloadSdkRuntimeLog(date);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getOutputFilePath());
	}

	@Test
	@DisplayName("successfully download the SDK Runtime Log for a single day as bytes")
	public void testDownloadSdkRuntimeLogsToBytes() {
		// Get yesterdays logs
		LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		// Retrieve the Zip file as bytes in the response
		// Be sure and call setOutputPath(null) here. This is a shared value and can be used in multiple reqs,
		// so safest to set to null in case another call set this value
		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setLogFormat(LogRequest.LogFormatType.LOGFILE)
				.setOutputPath(null)
				.downloadSdkRuntimeLog(date);

		Assertions.assertTrue(response.isSuccessful());
		if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
			try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
				os.write(response.getBinaryContent());
			}
			catch (IOException ignored){}
		}
	}
}
