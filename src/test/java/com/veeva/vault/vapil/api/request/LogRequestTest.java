/*---------------------------------------------------------------------
*	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
*	This code is based on pre-existing content developed and
*	owned by Veeva Systems Inc. and may only be used in connection
*	with the deliverable with which it was provided to Customer.
*---------------------------------------------------------------------
*/
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import org.junit.jupiter.api.*;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Tag("LogRequest")
@ExtendWith(VaultClientParameterResolver.class)
public class LogRequestTest {

	@Test
	public void testRetrieveAuditTypes(VaultClient vaultClient) {
		AuditTypesResponse response = vaultClient.newRequest(LogRequest.class)
				.retrieveAuditTypes();

		Assertions.assertTrue(response.isSuccessful());
		for (AuditTrail a : response.getAuditTrails()) {
			Assertions.assertNotNull(a.getName());
		}
	}

	@Test
	public void testRetrieveAuditMetadata(VaultClient vaultClient) {
		AuditMetadataResponse response = vaultClient.newRequest(LogRequest.class)
				.retrieveAuditMetadata(LogRequest.AuditTrailType.DOCUMENT);

		Assertions.assertTrue(response.isSuccessful());
		AuditMetadata metadata = response.getData();
		Assertions.assertNotNull(metadata.getName());

		for (AuditMetadata.Field field : metadata.getFields()) {
			Assertions.assertNotNull(field.getName());
			Assertions.assertNotNull(field.getType());
		}
	}

	@Test
	public void testRetrieveDocumentAuditDetails(VaultClient vaultClient) {
		DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29))
				.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
				.setLimit(4)
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
	public void testRetrieveDomainAuditDetails(VaultClient vaultClient) {
		DomainAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
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
	public void testRetrieveLoginAuditDetails(VaultClient vaultClient) {
		LoginAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
				.retrieveAuditDetails(LogRequest.AuditTrailType.LOGIN);

		Assertions.assertTrue(response.isSuccessful());
		AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
		Assertions.assertNotNull(details.getDetailsObject().getName());
		Assertions.assertNotNull(details.getDetailsObject().getUrl());

		for (LoginAuditData data : response.getData()) {
			Assertions.assertNotNull(data.getId());
			Assertions.assertNotNull(data.getTimestamp());
			Assertions.assertNotNull(data.getVaultId());
		}
	}

	@Test
	public void testRetrieveObjectAuditDetails(VaultClient vaultClient) {
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(20))
				.setLimit(10)
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
	}

	@Test
	public void testRetrieveObjectAuditDetailsAllPages(VaultClient vaultClient) {
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(20))
				.setLimit(10)
				.retrieveAuditDetailsAllPages(LogRequest.AuditTrailType.OBJECT);

		Assertions.assertTrue(response.isSuccessful());
		AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
		Assertions.assertNotNull(details.getDetailsObject().getName());
		Assertions.assertNotNull(details.getDetailsObject().getUrl());

		for (ObjectAuditData data : response.getData()) {
			Assertions.assertNotNull(data.getId());
			Assertions.assertNotNull(data.getTimestamp());
			Assertions.assertNotNull(data.getRecordId());
		}
	}

	@Test
	public void testRetrieveObjectAuditDetailsAsync(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
				.setAllDates(true)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
	}

	@Test
	public void testRetrieveSystemAuditDetails(VaultClient vaultClient) {
		LogRequest request = vaultClient.newRequest(LogRequest.class);
		request.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(20));
		request.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")));
		request.setAllDates(false);
		request.setFormatResult(LogRequest.FormatResultType.JSON);
		request.setLimit(4);

		SystemAuditResponse response = request.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);
		Assertions.assertTrue(response.isSuccessful());

		AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
		Assertions.assertNotNull(details.getDetailsObject().getName());
		Assertions.assertNotNull(details.getDetailsObject().getUrl());

		for (SystemAuditData data : response.getData()) {
			Assertions.assertNotNull(data.getId());
			Assertions.assertNotNull(data.getTimestamp());
			Assertions.assertNotNull(data.getAction());
		}

		// Test paging
		if (details.hasNextPage()) {
			response = request.retrieveAuditDetailsOffset(LogRequest.AuditTrailType.SYSTEM, details.getNextPage());
			details = response.getResponseDetails();
			Assertions.assertNotNull(details.getNextPage());
			Assertions.assertNotNull(details.getPreviousPage());
			Assertions.assertNotNull(details.getTotal());
		}

		if (details.hasPreviousPage()) {
			response = request.retrieveAuditDetailsOffset(LogRequest.AuditTrailType.SYSTEM, details.getPreviousPage());
			details = response.getResponseDetails();
			Assertions.assertNotNull(details.getNextPage());
			Assertions.assertNotNull(details.getTotal());
		}
	}

	@Test
	public void testRetrieveDomainFullAuditTrailAsCsv(VaultClient vaultClient) {
		JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
				.setAllDates(true)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
		Assertions.assertNotNull(response.getUrl());
	}

	@Test
	public void testRetrieveSystemAuditDetailsAsCsv(VaultClient vaultClient) {

		JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29))
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveAuditDetails(LogRequest.AuditTrailType.SYSTEM);

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
		Assertions.assertNotNull(response.getUrl());
	}

	@Test
	public void testRetrieveSingleDocumentAuditDetails(VaultClient vaultClient) {
		String vql = String.format("select id from documents where version_modified_date__v > '%s'",
				ZonedDateTime.now(ZoneId.of("UTC")).minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

		QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class).query(vql);
		Assertions.assertTrue(queryResponse.isSuccessful());

		Integer id = 1;
		for (QueryResponse.QueryResult rec : queryResponse.getData()) {
			id = rec.getInteger("id");
			break;
		}

		// Omit start and end dates to use the defaults (see the API guide)
		DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setLimit(4) // Just pull 4 records so the results can be viewed more easily
				.setFormatResult(LogRequest.FormatResultType.JSON)
				.retrieveDocumentAuditTrail(id);
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
	public void testRetrieveSingleDocumentAuditDetailsAsCsv(VaultClient vaultClient) {
		String vql = String.format("select id from documents where version_modified_date__v > '%s'",
				ZonedDateTime.now(ZoneId.of("UTC")).minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

		QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class).query(vql);
		Assertions.assertTrue(queryResponse.isSuccessful());

		Integer id = 1;
		for (QueryResponse.QueryResult rec : queryResponse.getData()) {
			id = rec.getInteger("id");
			break;
		}

		DocumentAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveDocumentAuditTrail(id);
		Assertions.assertTrue(response.isSuccessful());
	}


	public void testRetrieveSingleObjectAuditDetails(VaultClient vaultClient) {

		// Omit start and end dates to use the defaults (see the API guide)
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setFormatResult(LogRequest.FormatResultType.JSON)
				.retrieveObjectAuditTrail("product__v", "00P000000000601");

		Assertions.assertTrue(response.isSuccessful());
		if (response.isSuccessful()) {
			AuditDetailsResponse.ResponseDetails details = response.getResponseDetails();
			System.out.println("Offset = " + details.getOffset());
			System.out.println("Limit = " + details.getLimit());
			System.out.println("Size = " + details.getSize());
			System.out.println("Total = " + details.getTotal());
			System.out.println("Object/Name = " + details.getDetailsObject().getName());
			System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
			System.out.println("Object/Url = " + details.getDetailsObject().getUrl());

			System.out.println("Items ****");
			for (ObjectAuditData data : response.getData()) {
				System.out.println("\n**** Data Item **** ");
				System.out.println("id = " + data.getId());
				System.out.println("timestamp = " + data.getTimestamp());
				System.out.println("UserName = " + data.getUserName());
				System.out.println("Full Name = " + data.getFullName());
				System.out.println("Action = " + data.getAction());
				System.out.println("Item = " + data.getItem());
				System.out.println("Record ID = " + data.getRecordId());
				System.out.println("Object Label = " + data.getObjectLabel());
				System.out.println("Workflow Name = " + data.getWorkflowName());
				System.out.println("Task Name = " + data.getTaskName());
				System.out.println("Verdict = " + data.getVerdict());
				System.out.println("Reason = " + data.getReason());
				System.out.println("Capacity = " + data.getCapacity());
				System.out.println("Event Description = " + data.getEventDescription());
			}
		}
	}

	public void testRetrieveSingleObjectAuditDetailsAsCsv(VaultClient vaultClient) {
		System.out.println("\n****** Retrieve Single Object Audit Details As CSV ******");

		// Omit start and end dates to use the defaults (see the API guide)
		ObjectAuditResponse response = vaultClient.newRequest(LogRequest.class)
				.setFormatResult(LogRequest.FormatResultType.CSV)
				.retrieveObjectAuditTrail("product__v", "00P000000000601");

		if (response.isSuccessful()) {
			String results = new String(response.getBinaryContent());
			System.out.println(results);
		}

		System.out.println("Test complete...");
	}

	@Test
	public void testRetrieveDailyAPIUsageToFile(VaultClient vaultClient) {
		// Get yesterdays logs
		ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1);

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setLogDate(date)
				.setOutputPath(outputFilePath.toString())
				.retrieveDailyAPIUsage();

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getOutputFilePath());
	}

	@Test
	public void testRetrieveDailyAPIUsageToBytes(VaultClient vaultClient) {
		// Get yesterdays logs
		ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1);

		// Set output file path
		Path outputFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "response.zip");

		// Retrieve the Zip file as bytes in the response
		// Be sure and call setOutputPath(null) here. This is a shared value and can be used in multiple reqs,
		// so safest to set to null in case another call set this value
		VaultResponse response = vaultClient.newRequest(LogRequest.class)
				.setLogDate(date)
				.setLogFormat(LogRequest.LogFormatType.LOGFILE)
				.setOutputPath(null)
				.retrieveDailyAPIUsage();

		Assertions.assertTrue(response.isSuccessful());
		if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
			try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
				os.write(response.getBinaryContent());
			}
			catch (IOException ignored){}
		}
	}
}
