/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.DocumentEvent;
import com.veeva.vault.vapil.api.model.response.DocumentEventResponse;
import com.veeva.vault.vapil.api.model.response.DocumentEventSubtypeResponse;
import com.veeva.vault.vapil.api.model.response.DocumentEventTypeResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;


@Tag("DocumentEvent")
@ExtendWith(VaultClientParameterResolver.class)
@Disabled
public class DocumentEventRequestTest {

	@Test
	public void testRetrieveDocumentEventTypes(VaultClient vaultClient) {
		DocumentEventTypeResponse response = vaultClient.newRequest(DocumentEventRequest.class)
				.retrieveDocumentEventTypesandSubtypes();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getEvents());
	}

	@Test
	public void testRetrieveDocumentSubtypeMetadata(VaultClient vaultClient) {
		String type =  "distribution__v";
		String subtype = "approved_email__v";
		DocumentEventSubtypeResponse response = vaultClient.newRequest(DocumentEventRequest.class)
				.retrieveDocumentEventSubTypeMetadata(type, subtype);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getProperties());
	}

	@Test
	public void testCreateDocumentEvent(VaultClient vaultClient) {

		int docId = 9;
		int majorVersion = 0;
		int minorVersion = 1;
		String type = "Distribution Event";
		String subtype = "Approved Email";
		String classification = "Download";
		String externalId = "EX-0001";
		String userEmail = "sameer.mehta+jtester@veeva.com";

		DocumentEvent event = new DocumentEvent();
		event.getEventProperties().setEventType(type);
		event.getEventProperties().setEventSubtype(subtype);
		event.getEventProperties().setClassification(classification);
		event.getEventProperties().setExternalId(externalId);
		event.getEventProperties().setUserEmail(userEmail);

		VaultResponse response = vaultClient.newRequest(DocumentEventRequest.class)
				.createDocumentEvent(docId, majorVersion, minorVersion, event.getEventProperties());
		Assertions.assertTrue(response.isSuccessful());
	}

	@Test
	public void testRetrieveDocumentEvents(VaultClient vaultClient) {
		int docId = 9;
		DocumentEventResponse response = vaultClient.newRequest(DocumentEventRequest.class)
				.retrieveDocumentEvents(docId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getEvents());
	}
}
