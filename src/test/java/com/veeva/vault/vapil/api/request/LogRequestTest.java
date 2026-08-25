/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.SdkDebugSession;
import com.veeva.vault.vapil.api.model.common.SdkProfilingSession;
import com.veeva.vault.vapil.extension.FileHelper;
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Tag("LogRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@DisplayName("Loq Request should")
public class LogRequestTest {

	private static final String PROFILING_SESSION_NAME = "vapil_test_profiling_session__c";
	private static final String DEBUG_SESSION_LABEL = "VAPIL Test Debug";
	private static final String RESOURCES_LOGS_FOLDER = FileHelper.getPathLogsFolder();
	private static final String TEST_OBJECT_NAME = "vapil_test_object__c";
	private static VaultClient vaultClient;

	@BeforeAll
	public static void setup(VaultClient client) throws IOException {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit types")
	class TestRetrieveAuditTypes {

		AuditTypesResponse retrieveAuditTypesResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditTypesResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditTypes();

			assertNotNull(retrieveAuditTypesResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditTypesResponse.isSuccessful());
			for (AuditTrail auditTrail : retrieveAuditTypesResponse.getAuditTrails()) {
				assertNotNull(auditTrail.getName());
				assertNotNull(auditTrail.getLabel());
				assertNotNull(auditTrail.getUrl());
			}

		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit metadata: Object")
	class TestRetrieveAuditMetadataObject {

		AuditMetadataResponse retrieveAuditMetadataResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditMetadataResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(LogRequest.AuditTrailType.OBJECT);

			assertNotNull(retrieveAuditMetadataResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditMetadataResponse.isSuccessful());
			AuditMetadata metadata = retrieveAuditMetadataResponse.getData();
			assertNotNull(metadata.getName());
			assertNotNull(metadata.getLabel());
			assertNotNull(metadata.getFields());
			for (AuditMetadata.Field field : metadata.getFields()) {
				assertNotNull(field.getName());
				assertNotNull(field.getType());
				assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit metadata: Document")
	class TestRetrieveAuditMetadataDocument {

		AuditMetadataResponse retrieveAuditMetadataResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditMetadataResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(LogRequest.AuditTrailType.DOCUMENT);

			assertNotNull(retrieveAuditMetadataResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditMetadataResponse.isSuccessful());
			AuditMetadata metadata = retrieveAuditMetadataResponse.getData();
			assertNotNull(metadata.getName());
			assertNotNull(metadata.getLabel());
			assertNotNull(metadata.getFields());
			for (AuditMetadata.Field field : metadata.getFields()) {
				assertNotNull(field.getName());
				assertNotNull(field.getType());
				assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit metadata: Domain")
	class TestRetrieveAuditMetadataDomain {

		AuditMetadataResponse retrieveAuditMetadataResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditMetadataResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(LogRequest.AuditTrailType.DOMAIN);

			assertNotNull(retrieveAuditMetadataResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditMetadataResponse.isSuccessful());
			AuditMetadata metadata = retrieveAuditMetadataResponse.getData();
			assertNotNull(metadata.getName());
			assertNotNull(metadata.getLabel());
			assertNotNull(metadata.getFields());
			for (AuditMetadata.Field field : metadata.getFields()) {
				assertNotNull(field.getName());
				assertNotNull(field.getType());
				assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit metadata: System")
	class TestRetrieveAuditMetadataSystem {

		AuditMetadataResponse retrieveAuditMetadataResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditMetadataResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(LogRequest.AuditTrailType.SYSTEM);

			assertNotNull(retrieveAuditMetadataResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditMetadataResponse.isSuccessful());
			AuditMetadata metadata = retrieveAuditMetadataResponse.getData();
			assertNotNull(metadata.getName());
			assertNotNull(metadata.getLabel());
			assertNotNull(metadata.getFields());
			for (AuditMetadata.Field field : metadata.getFields()) {
				assertNotNull(field.getName());
				assertNotNull(field.getType());
				assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit metadata: Login")
	class TestRetrieveAuditMetadataLogin {

		AuditMetadataResponse retrieveAuditMetadataResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditMetadataResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditMetadata(LogRequest.AuditTrailType.LOGIN);

			assertNotNull(retrieveAuditMetadataResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditMetadataResponse.isSuccessful());
			AuditMetadata metadata = retrieveAuditMetadataResponse.getData();
			assertNotNull(metadata.getName());
			assertNotNull(metadata.getLabel());
			assertNotNull(metadata.getFields());
			for (AuditMetadata.Field field : metadata.getFields()) {
				assertNotNull(field.getName());
				assertNotNull(field.getType());
				assertNotNull(field.getType());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: Document")
	class TestRetrieveAuditDetailsDocument {

		DocumentAuditResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.setLimit(10)
					.setEvents(new HashSet<>(Arrays.asList("UploadDocBulk", "ExportBinder")))
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOCUMENT);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			AuditDetailsResponse.ResponseDetails auditDetails = retrieveAuditDetailsResponse.getResponseDetails();
			assertNotNull(auditDetails.getDetailsObject().getName());
			assertNotNull(auditDetails.getDetailsObject().getUrl());

			for (DocumentAudit documentAuditData : retrieveAuditDetailsResponse.getData()) {
				assertNotNull(documentAuditData.getId());
				assertNotNull(documentAuditData.getTimestamp());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: Domain")
	class TestRetrieveAuditDetailsDomain {

		DomainAuditResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.setLimit(4)
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = retrieveAuditDetailsResponse.getResponseDetails();
			assertNotNull(details.getDetailsObject().getName());
			assertNotNull(details.getDetailsObject().getUrl());

			for (DomainAuditData data : retrieveAuditDetailsResponse.getData()) {
				assertNotNull(data.getId());
				assertNotNull(data.getTimestamp());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: Login")
	class TestRetrieveAuditDetailsLogin {

		LoginAuditResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
					.retrieveAuditDetails(LogRequest.AuditTrailType.LOGIN);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = retrieveAuditDetailsResponse.getResponseDetails();
			assertNotNull(details.getDetailsObject().getName());
			assertNotNull(details.getDetailsObject().getUrl());

			for (LoginAuditData data : retrieveAuditDetailsResponse.getData()) {
				assertNotNull(data.getId());
				assertNotNull(data.getTimestamp());
				assertNotNull(data.getUserName());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve audit details: Object")
	class TestRetrieveAuditDetailsObject {

		private ObjectAuditResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			HashSet<String> objects = new HashSet<>(Arrays.asList("vapil_test_object__c","perf_stats__v"));
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setObjects(objects)
					.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = retrieveAuditDetailsResponse.getResponseDetails();
			assertNotNull(details);
			assertNotNull(details.getOffset());
			assertNotNull(details.getLimit());
			assertNotNull(details.getSize());
			assertNotNull(details.getTotal());

			AuditDetailsResponse.ResponseDetails.DetailsObject detailsObject = details.getDetailsObject();
			assertNotNull(detailsObject);
			assertNotNull(detailsObject.getName());
			assertNotNull(detailsObject.getUrl());
			assertNotNull(detailsObject.getLabel());

			List<ObjectAuditData> data = retrieveAuditDetailsResponse.getData();
			assertNotNull(data);
			for (ObjectAuditData objectAuditData : data) {
				assertNotNull(objectAuditData.getId());
				assertNotNull(objectAuditData.getTimestamp());
				assertNotNull(objectAuditData.getUserName());
				assertNotNull(objectAuditData.getFullName());
				assertNotNull(objectAuditData.getAction());
				assertNotNull(objectAuditData.getItem());
//				assertNotNull(objectAuditData.getFieldName());
//				assertNotNull(objectAuditData.getFieldLabel());
//				assertNotNull(objectAuditData.getOldValue());
//				assertNotNull(objectAuditData.getOldDisplayValue());
//				assertNotNull(objectAuditData.getNewValue());
//				assertNotNull(objectAuditData.getNewDisplayValue());
				assertNotNull(objectAuditData.getRecordId());
				assertNotNull(objectAuditData.getObjectName());
				assertNotNull(objectAuditData.getObjectLabel());
//				assertNotNull(objectAuditData.getWorkflowName());
//				assertNotNull(objectAuditData.getTaskName());
//				assertNotNull(objectAuditData.getVerdict());
//				assertNotNull(objectAuditData.getReason());
//				assertNotNull(objectAuditData.getCapacity());
				assertNotNull(objectAuditData.getEventDescription());
//				assertNotNull(objectAuditData.getOnBehalfOf());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve audit details: System")
	class TestRetrieveAuditDetailsSystem {

		private SystemAuditResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			AuditDetailsResponse.ResponseDetails details = retrieveAuditDetailsResponse.getResponseDetails();
			assertNotNull(details);
			assertNotNull(details.getOffset());
			assertNotNull(details.getLimit());
			assertNotNull(details.getSize());
			assertNotNull(details.getTotal());

			AuditDetailsResponse.ResponseDetails.DetailsObject detailsObject = details.getDetailsObject();
			assertNotNull(detailsObject);
			assertNotNull(detailsObject.getName());
			assertNotNull(detailsObject.getUrl());
			assertNotNull(detailsObject.getLabel());

			List<SystemAuditData> data = retrieveAuditDetailsResponse.getData();
			assertNotNull(data);
			for (SystemAuditData systemAuditData : data) {
				assertNotNull(systemAuditData.getId());
				assertNotNull(systemAuditData.getTimestamp());
				assertNotNull(systemAuditData.getUserId());
				assertNotNull(systemAuditData.getUserName());
				assertNotNull(systemAuditData.getFullName());
				assertNotNull(systemAuditData.getAction());
				assertNotNull(systemAuditData.getItem());
//				assertNotNull(systemAuditData.getFieldName());
//				assertNotNull(systemAuditData.getOldValue());
//				assertNotNull(systemAuditData.getNewValue());
				assertNotNull(systemAuditData.getEventDescription());
//				assertNotNull(systemAuditData.getOnBehalfOf());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: Object All Dates")
	@Disabled("This test is disabled because it will only work once every 24 hours")
	class TestRetrieveAuditDetailsObjectAllDates {

		JobCreateResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			assertNotNull(retrieveAuditDetailsResponse.getJobId());
			assertNotNull(retrieveAuditDetailsResponse.getUrl());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: Domain All Dates")
	@Disabled("This test is disabled because it will only work once every 24 hours")
	class TestRetrieveAuditDetailsDomainAllDates {

		JobCreateResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			assertNotNull(retrieveAuditDetailsResponse.getJobId());
			assertNotNull(retrieveAuditDetailsResponse.getUrl());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve audit details: System All Dates")
	@Disabled("This test is disabled because it will only work once every 24 hours")
	class TestRetrieveAuditDetailsSystemAllDates {

		JobCreateResponse retrieveAuditDetailsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditDetailsResponse = vaultClient.newRequest(LogRequest.class)
					.setAllDates(true)
					.setFormatResult(LogRequest.FormatResultType.CSV)
					.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);

			assertNotNull(retrieveAuditDetailsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditDetailsResponse.isSuccessful());
			assertNotNull(retrieveAuditDetailsResponse.getJobId());
			assertNotNull(retrieveAuditDetailsResponse.getUrl());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve complete audit history for a single document.")
	class TestRetrieveCompleteAuditHistoryForASingleDocument {

		DocumentAuditResponse retrieveAuditHistoryDocumentResponse = null;
		int docId;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
					.query("SELECT id FROM documents MAXROWS 1");
			docId = queryResponse.getData().get(0).getInteger("id");
		}

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditHistoryDocumentResponse = vaultClient.newRequest(LogRequest.class)
					.setEvents(new HashSet<>(Arrays.asList("GetDocumentVersion", "UploadDoc")))
					.retrieveCompleteAuditHistoryForASingleDocument(docId);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditHistoryDocumentResponse.isSuccessful());

			AuditDetailsResponse.ResponseDetails details = retrieveAuditHistoryDocumentResponse.getResponseDetails();
			assertNotNull(details.getDetailsObject().getName());
			assertNotNull(details.getDetailsObject().getLabel());
			assertNotNull(details.getDetailsObject().getUrl());

			for (DocumentAudit data : retrieveAuditHistoryDocumentResponse.getData()) {
				assertNotNull(data.getId());
				assertNotNull(data.getAction());
				assertNotNull(data.getDocumentUrl());
				assertNotNull(data.getTimestamp());
				assertNotNull(data.getUserName());
				assertNotNull(data.getFullName());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve complete audit history for a single object record.")
	class TestRetrieveCompleteAuditHistoryForASingleObjectRecord {

		ObjectAuditResponse retrieveAuditHistoryObjectResponse = null;
		String recordId;

		@BeforeAll
		public void setup() {
			QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
					.query(String.format("SELECT id FROM %s MAXROWS 1", TEST_OBJECT_NAME));
			recordId = queryResponse.getData().get(0).getString("id");
		}

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAuditHistoryObjectResponse = vaultClient.newRequest(LogRequest.class)
					.setEvents(new HashSet<>(Arrays.asList("Create", "Edit")))
					.retrieveCompleteAuditHistoryForASingleObjectRecord(TEST_OBJECT_NAME, recordId);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAuditHistoryObjectResponse.isSuccessful());

			AuditDetailsResponse.ResponseDetails details = retrieveAuditHistoryObjectResponse.getResponseDetails();
			assertNotNull(details.getDetailsObject().getName());
			assertNotNull(details.getDetailsObject().getLabel());
			assertNotNull(details.getDetailsObject().getUrl());

			for (ObjectAuditData data : retrieveAuditHistoryObjectResponse.getData()) {
				assertNotNull(data.getId());
				assertNotNull(data.getAction());
				assertNotNull(data.getTimestamp());
				assertNotNull(data.getUserName());
				assertNotNull(data.getFullName());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download api usage log to file")
	class TestDownloadDailyApiUsageToFile {

		private VaultResponse downloadDailyApiUsageResponse = null;
		Path outputPath = Paths.get(RESOURCES_LOGS_FOLDER, "api_usage_log.zip");

		@Test
		@Order(1)
		public void testRequest() {
			LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();
			downloadDailyApiUsageResponse = vaultClient.newRequest(LogRequest.class)
					.setOutputPath(outputPath.toString())
					.setLogFormat(LogRequest.LogFormatType.CSV)
					.retrieveDailyAPIUsage(date);

			assertNotNull(downloadDailyApiUsageResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadDailyApiUsageResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download api usage log to bytes")
	class TestDownloadDailyApiUsageToBytes {

		private VaultResponse downloadDailyApiUsageResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();
			downloadDailyApiUsageResponse = vaultClient.newRequest(LogRequest.class)
					.setLogFormat(LogRequest.LogFormatType.LOGFILE)
					.retrieveDailyAPIUsage(date);

			assertNotNull(downloadDailyApiUsageResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadDailyApiUsageResponse.isSuccessful());
			assertNotNull(downloadDailyApiUsageResponse.getBinaryContent());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download sdk runtime log to file")
	class TestDownloadSdkRuntimeLogToFile {

		private VaultResponse downloadSdkRuntimeLogResponse = null;
		Path outputPath = Paths.get(RESOURCES_LOGS_FOLDER, "sdk_runtime_log.zip");

		@Test
		@Order(1)
		public void testRequest() {
			LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();
			downloadSdkRuntimeLogResponse = vaultClient.newRequest(LogRequest.class)
					.setOutputPath(outputPath.toString())
					.downloadSdkRuntimeLog(date);

			assertNotNull(downloadSdkRuntimeLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadSdkRuntimeLogResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download sdk runtime log to bytes")
	class TestDownloadSdkRuntimeLogToBytes {

		private VaultResponse downloadSdkRuntimeLogResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			LocalDate date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate();
			downloadSdkRuntimeLogResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveDailyAPIUsage(date);

			assertNotNull(downloadSdkRuntimeLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadSdkRuntimeLogResponse.isSuccessful());
			assertNotNull(downloadSdkRuntimeLogResponse.getBinaryContent());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully Retrieve Email notification history")
	class TestRetrieveEmailNotificationHistory {

		EmailNotificationHistoryResponse retrieveEmailHistoryResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveEmailHistoryResponse = vaultClient.newRequest(LogRequest.class)
					.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(30))
					.setEndDateTime(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
					.retrieveEmailNotificationHistory();

			assertNotNull(retrieveEmailHistoryResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveEmailHistoryResponse.isSuccessful());
			for (EmailNotification data : retrieveEmailHistoryResponse.getData()) {
				assertNotNull(data.getNotificationId());
				assertNotNull(data.getSendDate());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all sdk profiling sessions in the Vault")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestRetrieveAllProfilingSessions {

		private SdkProfilingSessionBulkResponse retrieveAllProfilingSessionsResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAllProfilingSessionsResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveAllProfilingSessions();

			assertNotNull(retrieveAllProfilingSessionsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAllProfilingSessionsResponse.isSuccessful());

			assertNotNull(retrieveAllProfilingSessionsResponse.getData());
			List<SdkProfilingSession> sessions = retrieveAllProfilingSessionsResponse.getData();
			for (SdkProfilingSession session : sessions) {
				assertNotNull(session.getId());
				assertNotNull(session.getLabel());
				assertNotNull(session.getName());
//			assertNotNull(session.getDescription());
				assertNotNull(session.getStatus());
//			assertNotNull(session.getUserId());
				assertNotNull(session.getCreatedDate());
				assertNotNull(session.getExpirationDate());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve an sdk profiling session")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestRetrieveProfilingSession {

		private SdkProfilingSessionResponse retrieveProfilingSessionResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			retrieveProfilingSessionResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveProfilingSession(PROFILING_SESSION_NAME);

			assertNotNull(retrieveProfilingSessionResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveProfilingSessionResponse.isSuccessful());
			assertNotNull(retrieveProfilingSessionResponse.getData());
			assertNotNull(retrieveProfilingSessionResponse.getData().getId());
			assertNotNull(retrieveProfilingSessionResponse.getData().getLabel());
			assertNotNull(retrieveProfilingSessionResponse.getData().getName());
//			assertNotNull(retrieveProfilingSessionResponse.getData().getDescription());
			assertNotNull(retrieveProfilingSessionResponse.getData().getStatus());
//			assertNotNull(retrieveProfilingSessionResponse.getData().getUserId());
			assertNotNull(retrieveProfilingSessionResponse.getData().getCreatedDate());
			assertNotNull(retrieveProfilingSessionResponse.getData().getExpirationDate());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully create an sdk profiling session")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestCreateProfilingSession {

		private SdkProfilingSessionCreateResponse createProfilingSessionResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			long currentEpochTime = Instant.now().getEpochSecond();

			createProfilingSessionResponse = vaultClient.newRequest(LogRequest.class)
					.setUserId(vaultClient.getUserId())
					.setDescription("Vapil Test Description")
					.createProfilingSession(String.format("VAPIL Test Profiling Session %d", currentEpochTime));

			assertNotNull(createProfilingSessionResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(createProfilingSessionResponse.isSuccessful());
			assertNotNull(createProfilingSessionResponse.getData());
			assertNotNull(createProfilingSessionResponse.getData().getId());
			assertNotNull(createProfilingSessionResponse.getData().getName());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully end an sdk profiling session")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestEndProfilingSession {

		private VaultResponse endProfilingSessionResponse = null;

		@Test
		@Order(1)
		public void testRequest() {
			endProfilingSessionResponse = vaultClient.newRequest(LogRequest.class)
					.endProfilingSession("test__c");

			assertNotNull(endProfilingSessionResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(endProfilingSessionResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully delete an sdk profiling session")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestDeleteProfilingSession {

		private VaultResponse deleteProfilingSession = null;

		@Test
		@Order(1)
		public void testRequest() {
			deleteProfilingSession = vaultClient.newRequest(LogRequest.class)
					.deleteProfilingSession("test__c");

			assertNotNull(deleteProfilingSession);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(deleteProfilingSession.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download sdk profiling session results to a file")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestDownloadSdkProfilingSessionResultsFile {

		private VaultResponse downloadProfilingSessionResults = null;
		Path outputPath = Paths.get(System.getProperty("user.home"), "Downloads", "profiler_results.zip");

		@Test
		@Order(1)
		public void testRequest() {
			downloadProfilingSessionResults = vaultClient.newRequest(LogRequest.class)
					.setOutputPath(outputPath.toString())
					.downloadProfilingSessionResults(PROFILING_SESSION_NAME);

			assertNotNull(downloadProfilingSessionResults);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadProfilingSessionResults.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download sdk profiling session results bytes")
	@Disabled("Profiling sessions need to complete processing before they can be read/downloaded/deleted/etc, " +
			"which takes a considerable amount of time, and is not conducive to automated testing")
	class TestDownloadSdkProfilingSessionResultsBytes {

		private VaultResponse downloadProfilingSessionResults = null;

		@Test
		@Order(1)
		public void testRequest() {
			downloadProfilingSessionResults = vaultClient.newRequest(LogRequest.class)
					.downloadProfilingSessionResults(PROFILING_SESSION_NAME);

			assertNotNull(downloadProfilingSessionResults);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertNotNull(downloadProfilingSessionResults.getBinaryContent());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve all SDK Debug log sessions in the Vault")
	class TestRetrieveAllDebugLogs {

		private SdkDebugSessionBulkResponse retrieveAllDebugLogsResponse = null;
		private String debugSessionId;

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			retrieveAllDebugLogsResponse = vaultClient.newRequest(LogRequest.class)
					.setUserId(vaultClient.getUserId())
					.setIncludeInactive(true)
					.retrieveAllDebugLogs();

			assertNotNull(retrieveAllDebugLogsResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveAllDebugLogsResponse.isSuccessful());

			assertNotNull(retrieveAllDebugLogsResponse.getData());
			List<SdkDebugSession> sessions = retrieveAllDebugLogsResponse.getData();
			for (SdkDebugSession session : sessions) {
				assertNotNull(session.getId());
				assertNotNull(session.getName());
				assertNotNull(session.getUserId());
				assertNotNull(session.getLogLevel());
				assertNotNull(session.getExpirationDate());
//				assertNotNull(session.getClass());
				assertNotNull(session.getStatus());
				assertNotNull(session.getCreatedDate());
			}
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve single debug log from Vault")
	class TestRetrieveSingleDebugLog {

		private SdkDebugSessionResponse retrieveSingleDebugLogResponse = null;
		private String debugSessionId;

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			retrieveSingleDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.retrieveSingleDebugLog(debugSessionId);

			assertNotNull(retrieveSingleDebugLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(retrieveSingleDebugLogResponse.isSuccessful());

			SdkDebugSession session = retrieveSingleDebugLogResponse.getData();
			assertNotNull(session.getId());
			assertNotNull(session.getName());
			assertNotNull(session.getUserId());
			assertNotNull(session.getLogLevel());
			assertNotNull(session.getExpirationDate());
//			assertNotNull(session.getClassFilters());
			assertNotNull(session.getStatus());
			assertNotNull(session.getCreatedDate());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download SDK debug log session files to file")
	class TestDownloadDebugLogFilesToFile {

		private VaultResponse downloadDebugLogFilesResponse = null;
		private String debugSessionId;
		Path outputPath = Paths.get(RESOURCES_LOGS_FOLDER, "debug_log_files.zip");

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			downloadDebugLogFilesResponse = vaultClient.newRequest(LogRequest.class)
					.setOutputPath(outputPath.toString())
					.downloadDebugLogFiles(debugSessionId);

			assertNotNull(downloadDebugLogFilesResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(downloadDebugLogFilesResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully download SDK debug log session files to bytes")
	class TestDownloadDebugLogFilesToBytes {

		private VaultResponse downloadDebugLogFilesResponse = null;
		private String debugSessionId;

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			downloadDebugLogFilesResponse = vaultClient.newRequest(LogRequest.class)
					.downloadDebugLogFiles(debugSessionId);

			assertNotNull(downloadDebugLogFilesResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertNotNull(downloadDebugLogFilesResponse.getBinaryContent());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully create an SDK Debug log session")
	class TestCreateDebugLog {

		private SdkDebugSessionCreateResponse createDebugLogResponse = null;
		private String debugSessionId;
		private Set<String> classFilters = new HashSet<>(Arrays.asList("com.veeva.vault.custom.entrypoints.triggers.record.TestObject", "com.veeva.vault.custom.entrypoints.triggers.record.TestObject2"));

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.setLogLevel("info__sys")
					.setClassFilters(classFilters)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());

			assertNotNull(createDebugLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(createDebugLogResponse.isSuccessful());
			assertNotNull(createDebugLogResponse.getData());
			assertNotNull(createDebugLogResponse.getData().getId());

			debugSessionId = createDebugLogResponse.getData().getId();
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully reset an sdk debug session")
	class TestResetDebugLog {

		private VaultResponse resetDebugLogResponse = null;
		private String debugSessionId;

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@AfterAll
		public void teardown() {
			VaultResponse deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}

		@Test
		@Order(1)
		public void testRequest() {
			resetDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.resetDebugLog(debugSessionId);

			assertNotNull(resetDebugLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(resetDebugLogResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully delete an sdk debug session")
	class TestDeleteDebugLog {

		private VaultResponse deleteDebugLogResponse = null;
		private String debugSessionId;

		@BeforeAll
		public void setup() throws InterruptedException {
			SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.createDebugLog(DEBUG_SESSION_LABEL, vaultClient.getUserId());
			assertTrue(createDebugLogResponse.isSuccessful());
			debugSessionId = createDebugLogResponse.getData().getId();
			Thread.sleep(5000);
		}

		@Test
		@Order(1)
		public void testRequest() {
			deleteDebugLogResponse = vaultClient.newRequest(LogRequest.class)
					.deleteDebugLog(debugSessionId);

			assertNotNull(deleteDebugLogResponse);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(deleteDebugLogResponse.isSuccessful());
		}
	}
}
